package rebue.wxx.svc.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;
import rebue.wxx.jo.WxxAppJo;
import rebue.wxx.redis.eo.WxxAccessTokenEo;
import rebue.wxx.redis.svc.WxxAccessTokenRedisSvc;
import rebue.wxx.redis.svc.impl.WxxUserInfoMapRedisSvcImpl;
import rebue.wxx.ro.WxxRequestShortUrlRo;
import rebue.wxx.ro.WxxRequestUserInfoRo;
import rebue.wxx.ro.WxxRequestWebAccessTokenRo;
import rebue.wxx.svc.WxxAppSvc;
import rebue.wxx.svc.WxxRequestSvc;
import rebue.wxx.to.LongUrlTo;

@Service
@Slf4j
public class WxxRequestSvcImpl implements WxxRequestSvc {

    /**
     * 获取微信AccessToken的请求的url
     */
    private final static String        GET_WX_ACCESS_TOKEN_URL      = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 网页授权第二步：通过code换取网页授权的web_access_token的请求的url
     */
    private final static String        GET_WEB_ACCESS_TOKEN_URL     = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 网页授权第三步：刷新web_access_token缓存时限的请求的url
     */
    private final static String        REFRESH_WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    /**
     * 网页授权第四步：获取用户信息的请求的url
     */
    private final static String        GET_USER_INFO_URL            = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    /**
     * 单独获取用户信息
     */
    private final static String        GET_USERINFO_URL             = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
    /**
     * 获取长链接的请求的url
     */
    private final static String        GET_LONGURL_URL              = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=%s";
    /**
     * 获取微信服务器的IP地址的请求的url
     */
    private final static String        GET_WX_SERVER_IPS_URL        = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=%s";
    /**
     * 更新公众号菜单的请求的url
     */
    private final static String        UPDATE_MENU_URL              = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";

    @Resource
    private WxxAppSvc                  appSvc;
    @Resource
    private WxxUserInfoMapRedisSvcImpl userInfoMapRedisSvc;
    @Resource
    private WxxAccessTokenRedisSvc     accessTokenRedisSvc;

    @Resource
    private JsonParser                 jsonParser;
    @Resource
    private ObjectMapper               objectMapper;

    /**
     * 网页授权第二步：通过code换取网页授权的web_access_token
     */
    @Override
    public WxxRequestWebAccessTokenRo getWebAccessToken(final WxxAppJo appJo, final String code) throws IOException {
        log.info("准备向微信服务器发送请求以获取WebAccessToken: appJo-{} code={}", appJo, code);
        final String url = String.format(GET_WEB_ACCESS_TOKEN_URL, appJo.getId(), appJo.getAppSecret(), code);
        return objectMapper.readValue(OkhttpUtils.get(url), WxxRequestWebAccessTokenRo.class);
    }

    /**
     * 网页授权第三步：刷新web_access_token缓存时限
     */
    @Override
    public void refreshWebAccessToken(final String appId, final String refreshToken) {
        log.info("准备向微信服务器发送请求以刷新WebAccessToken缓存时限: appId-{} refreshToken={}", appId, refreshToken);
        final String url = String.format(REFRESH_WEB_ACCESS_TOKEN_URL, appId, refreshToken);
        try {
            OkhttpUtils.get(url);
        } catch (final IOException e) {
            log.error("请求异常", e);
        }
    }

    /**
     * 网页授权第四步：获取用户信息
     */
    @Override
    public Map<String, Object> getUserInfo(final String webAccessToken, final String openId, final String appId, final String code) throws IOException {
        log.info("准备向微信服务器发送请求以获取用户信息: webAccessToken={},openId={}", webAccessToken, openId);
        final String url = String.format(GET_USER_INFO_URL, webAccessToken, openId);
        final String resp = OkhttpUtils.get(url);
        final Map<String, Object> result = jsonParser.parseMap(resp);
        if (result.get("errcode") != null) {
            final String msg = "获取微信的用户信息不成功";
            log.error("{}: {}", msg, resp);
            return null;
        }
        try {
            userInfoMapRedisSvc.set(appId, code, result);
        } catch (final RedisSetException e) {
            log.error("设置缓存出现异常", e);
        }
        return result;
    }

    /**
     * 获取AccessToken
     */
    @Override
    public Map<String, Object> getAccessToken(final String appId) {
        log.info("准备向微信服务器发送请求以获取access token(需要在公众号中设置IP白名单，否则微信服务器会返回40164错误): appId-{}", appId);
        final WxxAppJo appJo = appSvc.getJoById(appId);
        final String url = String.format(GET_WX_ACCESS_TOKEN_URL, appJo.getId(), appJo.getAppSecret());
        try {
            final Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.get(url));
            return resultMap;
        } catch (final IOException e) {
            log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 是否关注微信公众号
     */
    @Override
    public Boolean isSubscribe(final String appId, final String openId) {
        log.info("准备向微信服务器发送请求以获取是否关注微信公众号: openId={}", openId);
        log.info("1. 先获取AccessToken: appId-{}", appId);
        final WxxAccessTokenEo accessTokenEo = accessTokenRedisSvc.get(appId);
        if (accessTokenEo == null || StringUtils.isBlank(accessTokenEo.getAccessToken())) {
            final String msg = "尚未获取到AccessToken";
            log.error(msg);
            return null;
        }
        log.info("2. 获取是否关注微信公众号: accessToken-{}", accessTokenEo.getAccessToken());
        final String url = String.format(GET_USERINFO_URL, accessTokenEo.getAccessToken(), openId);
        try {
            final WxxRequestUserInfoRo ro = objectMapper.readValue(OkhttpUtils.get(url), WxxRequestUserInfoRo.class);
            final Integer errcode = ro.getErrcode();
            if (errcode == null || errcode.equals(0)) {
                final Byte subscribe = ro.getSubscribe();
                if (subscribe == null) {
                    return null;
                }
                return subscribe != 0;
            }
            return null;
        } catch (final IOException e) {
            log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 获取短链接
     */
    @Override
    public String getShortUrl(final LongUrlTo to) {
        log.info("准备向微信服务器发送请求以获取短链接: to={}", to);
        log.info("1. 先获取AccessToken: appId-{}", to.getAppId());
        final WxxAccessTokenEo accessTokenEo = accessTokenRedisSvc.get(to.getAppId());
        if (accessTokenEo == null || StringUtils.isBlank(accessTokenEo.getAccessToken())) {
            final String msg = "尚未获取到AccessToken";
            log.error(msg);
            return null;
        }
        log.info("2. 获取长链接: accessToken-{}", accessTokenEo.getAccessToken());
        final String url = String.format(GET_LONGURL_URL, accessTokenEo.getAccessToken());
        try {
            final String requestParams = "{\"action\":\"long2short\",\"long_url\":\"" + to.getLongUrl() + "\"}";
            final WxxRequestShortUrlRo ro = objectMapper.readValue(OkhttpUtils.postByJsonParams(url, requestParams), WxxRequestShortUrlRo.class);
            final Integer errcode = ro.getErrcode();
            if (errcode == null || errcode.equals(0)) {
                return ro.getShort_url();
            }
            return null;
        } catch (final IOException e) {
            log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 获取微信服务器IP地址
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getWxServerIps(final String appId) {
        log.info("准备向微信服务器发送请求以获取微信服务器的IP地址: appId-{}", appId);
        log.info("1. 先获取AccessToken: appId-{}", appId);
        final WxxAccessTokenEo accessTokenEo = accessTokenRedisSvc.get(appId);
        if (accessTokenEo == null || StringUtils.isBlank(accessTokenEo.getAccessToken())) {
            final String msg = "尚未获取到AccessToken";
            log.error(msg);
            return null;
        }
        log.info("2. 向微信服务器发送请求以获取微信服务器的IP地址: accessToken-{}", accessTokenEo.getAccessToken());
        final String url = String.format(GET_WX_SERVER_IPS_URL, accessTokenEo.getAccessToken());
        try {
            final Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.get(url));
            return (List<String>) resultMap.get("ip_list");
        } catch (final IOException e) {
            log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 更新微信公众号菜单
     */
    @Override
    public String updateMenu(final String appId) {
        log.info("更新微信公众号的菜单：appId-{}", appId);
        try {
            log.info("1. 先获取AccessToken: appId-{}", appId);
            final WxxAccessTokenEo accessTokenEo = accessTokenRedisSvc.get(appId);
            if (accessTokenEo == null || StringUtils.isBlank(accessTokenEo.getAccessToken())) {
                final String msg = "尚未获取到AccessToken";
                log.error(msg);
                return null;
            }

            log.info("2. 向微信服务器提交更新菜单请求(AccessToken为必要参数): accessToken-{}", accessTokenEo.getAccessToken());
            final String url = String.format(UPDATE_MENU_URL, accessTokenEo.getAccessToken());
            final WxxAppJo appJo = appSvc.getJoById(appId);
            final Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.postByJsonParams(url, appJo.getMenu()));
            if (resultMap != null && resultMap.get("errcode").equals(0) && resultMap.get("errmsg").equals("ok")) {
                log.info("更新微信公众号的菜单成功!");
                return "ok";
            } else {
                log.error("更新微信公众号的菜单不成功:" + resultMap);
                return "fail:" + resultMap;
            }
        } catch (final IOException e) {
            log.error("向微信服务器发送创建自定义菜单出现异常:", e);
            e.printStackTrace();
            return "fail";
        }
    }

}
