package rebue.wxx.svc.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import rebue.wheel.XmlUtils;
import rebue.wxx.ro.WxRequestWebAccessTokenRo;
import rebue.wxx.svc.WxxRequestSvc;
import rebue.wxx.svc.WxxResponseSvc;
import rebue.wxx.utils.WxxAuthorizeSignUtils;
import rebue.wxx.utils.WxxMsgCryptUtils;
import rebue.wxx.vo.WxAuthorizeVo;

@Service
public class WxxResponseSvcImpl implements WxxResponseSvc {
    private final static Logger _log = LoggerFactory.getLogger(WxxResponseSvcImpl.class);

    /**
     * 公众账号ID
     */
    @Value("${wxx.app.id}")
    private String              wxAppId;
    /**
     * 微信公众号绑定网站的域名时，会通过此token进行校验
     */
    @Value("${wxx.app.token}")
    private String              wxToken;
    /**
     * 微信加解密消息用的key
     */
    @Value("${wxx.app.encodingAesKey:null}")
    private String              wxEncodingAesKey;
    /**
     * 自动回复用户关注事件的文字
     */
    @Value("${wxx.autoreply.subscribe}")
    private String              wxAutoReplySubscribe;

    @Resource
    private WxxRequestSvc       wxxRequestSvc;

    /**
     * 提供给微信验证本服务器身份的接口
     */
    @Override
    public String authorize(WxAuthorizeVo vo) {
        _log.info("接收到微信验证本服务器的请求");

        _log.info("检查传入参数的是否齐全");
        if (StringUtils.isAnyBlank(vo.getSignature(), vo.getTimestamp(), vo.getNonce(), vo.getEchostr())) {
            String msg = "微信初始化验证传入参数不能有null值，不排除有人在试图模仿微信官方服务器发来信息";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        if (WxxAuthorizeSignUtils.verify(vo, wxToken)) {
            _log.info("微信初始化验证成功：" + vo);
            return vo.getEchostr();
        } else {
            String msg = "微信初始化验证传入参数验证没有通过，不排除有人在试图模仿微信官方服务器发来信息";
            _log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 处理微信发来消息的接口
     */
    @Override
    public String handleMsg(Map<String, Object> requestMap) {
        _log.info("接收到消息：{}", requestMap);
        String encrypt = (String) requestMap.get("Encrypt");
        // 如果是加密的消息
        if (!StringUtils.isBlank(encrypt)) {
            try {
                requestMap = XmlUtils.xmlToMap(WxxMsgCryptUtils.decrypt(encrypt, wxAppId, wxEncodingAesKey));
            } catch (DocumentException e) {
                _log.error("解析XML异常", e);
                return null;
            }
        }
        if (requestMap.get("MsgType").equals("event")) {
            _log.info("接收到事件: {}", requestMap);
            if (requestMap.get("Event").equals("subscribe")) {
                _log.info("用户关注事件");
                return getTextMsg(String.valueOf(requestMap.get("ToUserName")), String.valueOf(requestMap.get("FromUserName")), wxAutoReplySubscribe);
            } else if (requestMap.get("Event").equals("unsubscribe")) {
                _log.info("用户取消关注事件");
            }
        } else if (requestMap.get("MsgType").equals("text")) {
            _log.info("接收到用户发来的文本消息");
            return getTextMsg(String.valueOf(requestMap.get("ToUserName")), String.valueOf(requestMap.get("FromUserName")), "对不起，我不明白您在说什么。");
        }
        return null;
    }

    private String getTextMsg(String fromUserName, String toUserName, String content) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("FromUserName", fromUserName);
        result.put("ToUserName", toUserName);
        result.put("CreateTime", String.valueOf(System.currentTimeMillis() / 1000));
        result.put("MsgType", "text");
        result.put("Content", content);
        _log.info("返回数据: {}", result);
        return XmlUtils.mapToXml(result);
    }

    /**
     * 网页授权第一步：用户同意授权，获取到code
     * 网页授权第二步：通过code换取网页授权的web_access_token
     * 网页授权第三步：刷新web_access_token缓存时限
     * 网页授权第四步：获取用户信息
     * 网页授权第五步：回调登录页面(请求参数为添加签名后的用户信息map)
     * 
     * @param code
     *            获取到授权的code
     */
    @Override
    public String authorizeCode(String code) throws IOException {
        _log.info("网页授权第一步：用户同意授权，获取到code:{}", code);
        _log.info("网页授权第二步：通过code换取网页授权web_access_token");
        WxRequestWebAccessTokenRo webAccessToken = wxxRequestSvc.getWebAccessToken(code);
        _log.info("网页授权第三步：刷新web_access_token缓存时限");
        wxxRequestSvc.refreshWebAccessToken(webAccessToken.getRefresh_token());
        _log.info("网页授权第四步：获取用户信息");
        Map<String, Object> userInfo = wxxRequestSvc.getUserInfo(webAccessToken.getAccess_token(), webAccessToken.getOpenid());
        _log.info("回调登录页面(请求参数为添加签名后的用户信息map)");
        return wxxRequestSvc.callbackLogin(userInfo);
    }

}
