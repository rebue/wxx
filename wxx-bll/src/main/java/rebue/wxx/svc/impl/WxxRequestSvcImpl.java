package rebue.wxx.svc.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import rebue.sbs.redis.RedisClient;
import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;
import rebue.wxx.access.token.svr.feign.WxxAccessTokenSvc;
import rebue.wxx.ro.WxRequestShortUrlRo;
import rebue.wxx.ro.WxRequestUserInfoRo;
import rebue.wxx.ro.WxRequestWebAccessTokenRo;
import rebue.wxx.svc.WxxRequestSvc;
import rebue.wxx.to.LongUrlTo;

@Service
public class WxxRequestSvcImpl implements WxxRequestSvc {
    private final static Logger _log                         = LoggerFactory.getLogger(WxxRequestSvcImpl.class);

    /**
     * 缓存微信授权登录获取用户信息的Key的前缀 后面跟用户的用户code拼接成Key
     * Value为获取到的用户信息
     */
    private static final String REDIS_KEY_USER_INFO_PREFIX   = "rebue.wxx.request.svc.userinfo.";

    /**
     * 获取微信AccessToken的请求的url
     */
    private final static String GET_WX_ACCESS_TOKEN_URL      = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 网页授权第二步：通过code换取网页授权的web_access_token的请求的url
     */
    private final static String GET_WEB_ACCESS_TOKEN_URL     = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 网页授权第三步：刷新web_access_token缓存时限的请求的url
     */
    private final static String REFRESH_WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    /**
     * 网页授权第四步：获取用户信息的请求的url
     */
    private final static String GET_USER_INFO_URL            = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    /**
     * 单独获取用户信息
     */
    private final static String GET_USERINFO_URL             = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
    /**
     * 获取长链接的请求的url
     */
    private final static String GET_LONGURL_URL              = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=%s";
    /**
     * 获取微信服务器的IP地址的请求的url
     */
    private final static String GET_WX_SERVER_IPS_URL        = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=%s";
    /**
     * 更新公众号菜单的请求的url
     */
    private final static String UPDATE_MENU_URL              = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";

    /**
     * 公众账号ID
     */
    @Value("${wxx.app.id}")
    private String              wxAppId;
    /**
     * 公众账号密钥
     */
    @Value("${wxx.app.secret}")
    private String              wxAppSecret;
    /**
     * 公众号菜单的自定义json字符串
     */
    @Value("${wxx.menu}")
    private String              wxMenu;

    @Resource
    private WxxAccessTokenSvc   accessTokenSvc;

    @Resource
    private RedisClient         redisClient;
    @Resource
    private JsonParser          jsonParser;
    @Resource
    private ObjectMapper        objectMapper;

    /**
     * 获取AccessToken
     */
    @Override
    public Map<String, Object> getAccessToken() {
        _log.info("准备向微信服务器发送请求以获取access token(需要在公众号中设置IP白名单，否则微信服务器会返回40164错误)");
        final String url = String.format(GET_WX_ACCESS_TOKEN_URL, wxAppId, wxAppSecret);
        try {
            final Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.get(url));
            return resultMap;
        } catch (final IOException e) {
            _log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 网页授权第二步之前：先通过code查找是否已有成功获取用户信息的缓存
     * 
     * @param code
     *            网页授权第一步获取的code
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getUserInfoCache(final String code) {
        _log.info("网页授权第二步之前：先通过code查找是否已有成功获取用户信息的缓存: code={}", code);
        return redisClient.getObj(REDIS_KEY_USER_INFO_PREFIX + code, Map.class);
    }

    /**
     * 网页授权第二步：通过code换取网页授权的web_access_token
     */
    @Override
    public WxRequestWebAccessTokenRo getWebAccessToken(final String code) throws IOException {
        _log.info("准备向微信服务器发送请求以获取WebAccessToken: code={}", code);
        final String url = String.format(GET_WEB_ACCESS_TOKEN_URL, wxAppId, wxAppSecret, code);
        return objectMapper.readValue(OkhttpUtils.get(url), WxRequestWebAccessTokenRo.class);
    }

    /**
     * 网页授权第三步：刷新web_access_token缓存时限
     */
    @Override
    public void refreshWebAccessToken(final String refreshToken) {
        _log.info("准备向微信服务器发送请求以刷新WebAccessToken缓存时限: code={}", refreshToken);
        final String url = String.format(REFRESH_WEB_ACCESS_TOKEN_URL, wxAppId, refreshToken);
        try {
            OkhttpUtils.get(url);
        } catch (final IOException e) {
            _log.error("请求异常", e);
        }
    }

    /**
     * 网页授权第四步：获取用户信息
     */
    @Override
    public Map<String, Object> getUserInfo(final String webAccessToken, final String openId, final String code) throws IOException {
        _log.info("准备向微信服务器发送请求以获取用户信息: webAccessToken={},openId={}", webAccessToken, openId);
        final String url = String.format(GET_USER_INFO_URL, webAccessToken, openId);
        final String resp = OkhttpUtils.get(url);
        final Map<String, Object> result = jsonParser.parseMap(resp);
        if (result.get("errcode") != null) {
            final String msg = "获取微信的用户信息不成功";
            _log.error("{}: {}", msg, resp);
            return null;
        }
        try {
            redisClient.setObj(REDIS_KEY_USER_INFO_PREFIX + code, result, 20);
        } catch (final RedisSetException e) {
            _log.error("设置缓存出现异常", e);
        }
        return result;
    }

    /**
     * 是否关注微信公众号
     */
    @Override
    public Boolean isSubscribe(final String openId) {
        _log.info("准备向微信服务器发送请求以获取是否关注微信公众号: openId={}", openId);
        // 1、先获取AccessToken
        final String accessToken = accessTokenSvc.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            final String msg = "尚未获取到AccessToken";
            _log.error(msg);
            return null;
        }
        // 2. 获取是否关注微信公众号
        final String url = String.format(GET_USERINFO_URL, accessToken, openId);
        try {
            final WxRequestUserInfoRo ro = objectMapper.readValue(OkhttpUtils.get(url), WxRequestUserInfoRo.class);
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
            _log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 获取短链接
     */
    @Override
    public String getShortUrl(final LongUrlTo to) {
        _log.info("准备向微信服务器发送请求以获取短链接: 长链接={}", to.getLongUrl());
        // 1、先获取AccessToken
        final String accessToken = accessTokenSvc.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            final String msg = "尚未获取到AccessToken";
            _log.error(msg);
            return null;
        }
        // 2. 获取长链接
        final String url = String.format(GET_LONGURL_URL, accessToken);
        try {
            final String requestParams = "{\"action\":\"long2short\",\"long_url\":\"" + to.getLongUrl() + "\"}";
            final WxRequestShortUrlRo ro = objectMapper.readValue(OkhttpUtils.postByJsonParams(url, requestParams), WxRequestShortUrlRo.class);
            final Integer errcode = ro.getErrcode();
            if (errcode == null || errcode.equals(0)) {
                return ro.getShort_url();
            }
            return null;
        } catch (final IOException e) {
            _log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 获取微信服务器IP地址
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getWxServerIps() {
        _log.info("准备向微信服务器发送请求以获取微信服务器的IP地址");
        // 1、先获取AccessToken
        final String accessToken = accessTokenSvc.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            final String msg = "尚未获取到AccessToken";
            _log.error(msg);
            return null;
        }
        // 2、向微信服务器发送请求以获取微信服务器的IP地址
        final String url = String.format(GET_WX_SERVER_IPS_URL, accessToken);
        try {
            final Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.get(url));
            return (List<String>) resultMap.get("ip_list");
        } catch (final IOException e) {
            _log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 更新微信公众号菜单
     */
    @Override
    public String updateMenu() {
        _log.info("更新微信公众号的菜单：" + wxMenu);
        try {
            // 1、先获取AccessToken
            final String accessToken = accessTokenSvc.getAccessToken();
            if (StringUtils.isBlank(accessToken)) {
                final String msg = "尚未获取到AccessToken";
                _log.error(msg);
                return msg;
            }

            // 2、向微信服务器提交更新菜单请求(AccessToken为必要参数)
            final String url = String.format(UPDATE_MENU_URL, accessToken);
            final Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.postByJsonParams(url, wxMenu));
            if (resultMap != null && resultMap.get("errcode").equals(0) && resultMap.get("errmsg").equals("ok")) {
                _log.info("更新微信公众号的菜单成功!");
                return "ok";
            } else {
                _log.error("更新微信公众号的菜单不成功:" + resultMap);
                return "fail:" + resultMap;
            }
        } catch (final IOException e) {
            _log.error("向微信服务器发送创建自定义菜单出现异常:", e);
            e.printStackTrace();
            return "fail";
        }
    }

}
