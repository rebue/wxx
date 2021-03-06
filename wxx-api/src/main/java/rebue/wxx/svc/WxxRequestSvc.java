package rebue.wxx.svc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import rebue.wxx.ro.WxRequestWebAccessTokenRo;
import rebue.wxx.to.LongUrlTo;

public interface WxxRequestSvc {

    /**
     * 获取AccessToken
     */
    Map<String, Object> getAccessToken();

    /**
     * 网页授权第二步之前：先通过code查找是否已有成功获取用户信息的缓存
     * 
     * @param code
     *            网页授权第一步获取的code
     */
    Map<String, Object> getUserInfoCache(String code);

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
    Map<String, Object> getUserInfo(String webAccessToken, String openId, String code) throws IOException;

    /**
     * 是否关注微信公众号
     */
    Boolean isSubscribe(String openId);

    /**
     * 获取短链接
     */
    String getShortUrl(LongUrlTo to);

    /**
     * 获取微信服务器IP地址
     */
    List<String> getWxServerIps();

    /**
     * 更新微信公众号菜单
     */
    String updateMenu();

}