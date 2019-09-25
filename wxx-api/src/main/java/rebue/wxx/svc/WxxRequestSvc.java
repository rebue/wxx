package rebue.wxx.svc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import rebue.wxx.jo.WxxAppJo;
import rebue.wxx.ro.WxxRequestWebAccessTokenRo;
import rebue.wxx.to.LongUrlTo;

public interface WxxRequestSvc {

    /**
     * 网页授权第二步：通过code换取网页授权的web_access_token
     */
    WxxRequestWebAccessTokenRo getWebAccessToken(WxxAppJo appJo, String code) throws IOException;

    /**
     * 网页授权第三步：刷新web_access_token缓存时限
     */
    void refreshWebAccessToken(String appId, String refreshToken);

    /**
     * 网页授权第四步：获取用户信息
     */
    Map<String, Object> getUserInfo(String webAccessToken, String openId, String appId, String code) throws IOException;

    /**
     * 获取AccessToken
     */
    Map<String, Object> getAccessToken(String appId);

    /**
     * 更新微信公众号菜单
     */
    String updateMenu(String appId);

    /**
     * 是否关注微信公众号
     */
    Boolean isSubscribe(String appId, String openId);

    /**
     * 获取短链接
     */
    String getShortUrl(LongUrlTo to);

    /**
     * 获取微信服务器IP地址
     */
    List<String> getWxServerIps(String appId);

}