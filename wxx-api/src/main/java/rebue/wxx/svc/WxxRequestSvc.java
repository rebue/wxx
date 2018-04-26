package rebue.wxx.svc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import rebue.wxx.ro.WxRequestWebAccessTokenRo;

public interface WxxRequestSvc {

    /**
     * 获取AccessToken
     */
    Map<String, Object> getAccessToken();

    /**
     * 网页授权第二步：通过code换取网页授权的web_access_token
     */
    WxRequestWebAccessTokenRo getWebAccessToken(String code) throws IOException;

    /**
     * 网页授权第三步：刷新web_access_token缓存时限
     */
    void refreshWebAccessToken(String refreshToken);

    /**
     * 网页授权第四步：获取用户信息
     */
    Map<String, Object> getUserInfo(String webAccessToken, String openId) throws IOException;

    /**
     * 网页授权第五步：回调登录页面
     */
    String callbackLogin(Map<String, Object> userInfo) throws IOException;

    /**
     * 获取微信服务器IP地址
     */
    List<String> getWxServerIps();

    /**
     * 更新微信公众号菜单
     */
    String updateMenu();

}