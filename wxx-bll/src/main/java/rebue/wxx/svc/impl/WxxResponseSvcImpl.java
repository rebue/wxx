package rebue.wxx.svc.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rebue.wheel.XmlUtils;
import rebue.wxx.jo.WxxAppJo;
import rebue.wxx.redis.svc.impl.WxxUserInfoMapRedisSvcImpl;
import rebue.wxx.ro.WxxRequestWebAccessTokenRo;
import rebue.wxx.svc.WxxAppSvc;
import rebue.wxx.svc.WxxRequestSvc;
import rebue.wxx.svc.WxxResponseSvc;
import rebue.wxx.utils.WxxAuthorizeSignUtils;
import rebue.wxx.utils.WxxMsgCryptUtils;
import rebue.wxx.vo.WxAuthorizeVo;

@Service
@Slf4j
public class WxxResponseSvcImpl implements WxxResponseSvc {

    @Resource
    private WxxRequestSvc              requestSvc;

    @Resource
    private WxxAppSvc                  appSvc;
    @Resource
    private WxxUserInfoMapRedisSvcImpl userInfoMapRedisSvc;

    /**
     * 提供给微信验证本服务器身份的接口
     */
    @Override
    public String authorize(final String appId, final WxAuthorizeVo vo) {
        log.info("接收到微信验证本服务器的请求:{}", vo);

        log.info("检查传入参数的是否齐全");
        if (StringUtils.isAnyBlank(vo.getSignature(), vo.getTimestamp(), vo.getNonce(), vo.getEchostr())) {
            final String msg = "微信初始化验证传入参数不能有null值，不排除有人在试图模仿微信官方服务器发来信息";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        log.info("获取AccessToken: appId-{}", appId);
        final WxxAppJo appJo = appSvc.getJoById(appId);

        if (WxxAuthorizeSignUtils.verify(vo, appJo.getToken())) {
            log.info("微信初始化验证成功：appId-{} vo-{}", appId, vo);
            return vo.getEchostr();
        } else {
            final String msg = "微信初始化验证传入参数验证没有通过，不排除有人在试图模仿微信官方服务器发来信息";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 处理微信发来消息的接口
     */
    @Override
    public String handleMsg(final String appId, Map<String, Object> requestMap) {
        log.info("接收到消息：{}", requestMap);

        log.info("获取AccessToken: appId-{}", appId);
        final WxxAppJo appJo = appSvc.getJoById(appId);

        final String encrypt = (String) requestMap.get("Encrypt");
        // 如果是加密的消息
        if (!StringUtils.isBlank(encrypt)) {
            try {
                requestMap = XmlUtils.xmlToMap(WxxMsgCryptUtils.decrypt(encrypt, appId, appJo.getEncodeingAesKey()));
            } catch (final DocumentException e) {
                log.error("解析XML异常", e);
                return null;
            }
        }
        if (requestMap.get("MsgType").equals("event")) {
            log.info("接收到事件: {}", requestMap);
            if (requestMap.get("Event").equals("subscribe")) {
                log.info("用户关注事件");
                return getTextMsg(String.valueOf(requestMap.get("ToUserName")), String.valueOf(requestMap.get("FromUserName")), appJo.getSubscribeAutoReply());
            } else if (requestMap.get("Event").equals("unsubscribe")) {
                log.info("用户取消关注事件");
            }
        } else if (requestMap.get("MsgType").equals("text")) {
            log.info("接收到用户发来的文本消息");
            return getTextMsg(String.valueOf(requestMap.get("ToUserName")), String.valueOf(requestMap.get("FromUserName")), "对不起，我不明白您在说什么");
        }
        return null;
    }

    private String getTextMsg(final String fromUserName, final String toUserName, final String content) {
        final Map<String, Object> result = new LinkedHashMap<>();
        result.put("FromUserName", fromUserName);
        result.put("ToUserName", toUserName);
        result.put("CreateTime", String.valueOf(System.currentTimeMillis() / 1000));
        result.put("MsgType", "text");
        result.put("Content", content);
        log.info("返回数据: {}", result);
        return XmlUtils.mapToXml(result);
    }

    /**
     * 网页授权第一步：用户同意授权，微信服务器回调，获取到code
     * 网页授权第二步：通过code换取网页授权的web_access_token
     * 网页授权第三步：刷新web_access_token缓存时限
     * 网页授权第四步：获取用户信息
     * 
     * @param code
     *            获取到授权的code
     * @return 返回有微信用户信息的Map
     */
    @Override
    public Map<String, Object> authorizeCode(final WxxAppJo appJo, final String code) throws IOException {
        log.info("网页授权第一步：用户同意授权，微信服务器回调: appJo-{} code-{}", appJo, code);

        log.info("网页授权第二步之前，先通过code查找是否已有成功获取用户信息的缓存: appId-{} code-{}", appJo.getId(), code);
        final Map<String, Object> userInfoCache = userInfoMapRedisSvc.get(appJo.getId(), code);
        if (userInfoCache != null) {
            log.info("获取到用户信息缓存，直接跳过第二、三、四步，直接返回用户的缓存信息: {}", userInfoCache);
            return userInfoCache;
        }

        log.info("网页授权第二步：通过code换取网页授权web_access_token");
        final WxxRequestWebAccessTokenRo webAccessToken = requestSvc.getWebAccessToken(appJo, code);
        log.info("web access token:", webAccessToken);
        if (StringUtils.isBlank(webAccessToken.getAccess_token())) {
            return null;
        }
        log.info("网页授权第三步：刷新web_access_token缓存时限");
        requestSvc.refreshWebAccessToken(appJo.getId(), webAccessToken.getRefresh_token());
        log.info("网页授权第四步：获取用户信息");
        return requestSvc.getUserInfo(webAccessToken.getAccess_token(), webAccessToken.getOpenid(), appJo.getId(), code);
    }

}
