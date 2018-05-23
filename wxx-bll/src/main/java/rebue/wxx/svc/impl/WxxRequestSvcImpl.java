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

import rebue.wheel.HtmlUtils;
import rebue.wheel.OkhttpUtils;
import rebue.wheel.turing.SignUtils;
import rebue.wxx.access.token.svr.feign.WxxAccessTokenSvc;
import rebue.wxx.ro.WxRequestWebAccessTokenRo;
import rebue.wxx.svc.WxxRequestSvc;

@Service
public class WxxRequestSvcImpl implements WxxRequestSvc {
    private final static Logger _log                         = LoggerFactory.getLogger(WxxRequestSvcImpl.class);

    /**
     * 获取微信AccessToken的请求的url
     */
    private final static String GET_WX_ACCESS_TOKEN_URL      = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 获取微信WebAccessToken的请求的url
     */
    private final static String GET_WEB_ACCESS_TOKEN_URL     = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    /**
     * 刷新微信WebAccessToken的请求的url
     */
    private final static String REFRESH_WEB_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    /**
     * 获取微信用户信息的请求的url
     */
    private final static String GET_USER_INFO_URL            = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
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
    /**
     * 微信登录回调的地址
     */
    @Value("${wxx.loginCallback.url}")
    private String              wxLoginCallbackUrl;
    /**
     * 微信登录回调的签名key
     */
    @Value("${wxx.loginCallback.signKey}")
    private String              wxLoginCallbackSignKey;

    @Resource
    private WxxAccessTokenSvc   accessTokenSvc;

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
        String url = String.format(GET_WX_ACCESS_TOKEN_URL, wxAppId, wxAppSecret);
        try {
            Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.get(url));
            return resultMap;
        } catch (IOException e) {
            _log.error("请求异常", e);
            return null;
        }
    }

    /**
     * 网页授权第二步：通过code换取网页授权的web_access_token
     */
    @Override
    public WxRequestWebAccessTokenRo getWebAccessToken(String code) throws IOException {
        _log.info("准备向微信服务器发送请求以获取WebAccessToken: code={}", code);
        String url = String.format(GET_WEB_ACCESS_TOKEN_URL, wxAppId, wxAppSecret, code);
        return objectMapper.readValue(OkhttpUtils.get(url), WxRequestWebAccessTokenRo.class);
    }

    /**
     * 网页授权第三步：刷新web_access_token缓存时限
     */
    @Override
    public void refreshWebAccessToken(String refreshToken) {
        _log.info("准备向微信服务器发送请求以刷新WebAccessToken缓存时限: code={}", refreshToken);
        String url = String.format(REFRESH_WEB_ACCESS_TOKEN_URL, wxAppId, refreshToken);
        try {
            OkhttpUtils.get(url);
        } catch (IOException e) {
            _log.error("请求异常", e);
        }
    }

    /**
     * 网页授权第四步：获取用户信息
     */
    @Override
    public Map<String, Object> getUserInfo(String webAccessToken, String openId) throws IOException {
        _log.info("准备向微信服务器发送请求以获取用户信息: webAccessToken={},openId={}", webAccessToken, openId);
        String url = String.format(GET_USER_INFO_URL, webAccessToken, openId);
        String resp = OkhttpUtils.get(url);
        Map<String, Object> result = jsonParser.parseMap(resp);
        if (result.get("errcode") != null) {
            String msg = "获取微信的用户信息不成功";
            _log.error("{}: {}", msg, resp);
            throw new RuntimeException(msg);
        }
        return result;
    }

    /**
     * 网页授权第五步：回调登录页面
     */
    @Override
    public String callbackLogin(Map<String, Object> userInfo) {
        _log.info("准备向网站发送请求以获取网站登录的回调页面: {}", userInfo);
        SignUtils.sign1(userInfo, wxLoginCallbackSignKey);
//        try {
//            return OkhttpUtils.get(wxLoginCallbackUrl, userInfo);
        return HtmlUtils.autoPostByFormParams(wxLoginCallbackUrl, userInfo);
//        } catch (IOException e) {
//            String msg = "获取网站登录的回调页面出错";
//            _log.error(msg, e);
//            return String.format("<!DOCTYPE HTML>\n"  //
//                    + "<html>\n" //
//                    + "<body>\n"  //
//                    + "<span style=\"font-size:70px\">%s</span>" //
//                    + "</body>\n" //
//                    + "</html>", msg);
//        }
    }

    /**
     * 获取微信服务器IP地址
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getWxServerIps() {
        _log.info("准备向微信服务器发送请求以获取微信服务器的IP地址");
        // 1、先获取AccessToken
        String accessToken = accessTokenSvc.getAccessToken();
        if (StringUtils.isBlank(accessToken)) {
            String msg = "尚未获取到AccessToken";
            _log.error(msg);
            return null;
        }
        // 2、向微信服务器发送请求以获取微信服务器的IP地址
        String url = String.format(GET_WX_SERVER_IPS_URL, accessToken);
        try {
            Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.get(url));
            return (List<String>) resultMap.get("ip_list");
        } catch (IOException e) {
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
            String accessToken = accessTokenSvc.getAccessToken();
            if (StringUtils.isBlank(accessToken)) {
                String msg = "尚未获取到AccessToken";
                _log.error(msg);
                return msg;
            }

            // 2、向微信服务器提交更新菜单请求(AccessToken为必要参数)
            String url = String.format(UPDATE_MENU_URL, accessToken);
            Map<String, Object> resultMap = jsonParser.parseMap(OkhttpUtils.postByJsonParams(url, wxMenu));
            if (resultMap != null && resultMap.get("errcode").equals(0) && resultMap.get("errmsg").equals("ok")) {
                _log.info("更新微信公众号的菜单成功!");
                return "ok";
            } else {
                _log.error("更新微信公众号的菜单不成功:" + resultMap);
                return "fail:" + resultMap;
            }
        } catch (IOException e) {
            _log.error("向微信服务器发送创建自定义菜单出现异常:", e);
            e.printStackTrace();
            return "fail";
        }
    }

}
