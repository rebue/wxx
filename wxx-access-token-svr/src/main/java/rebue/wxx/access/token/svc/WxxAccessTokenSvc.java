package rebue.wxx.access.token.svc;

public interface WxxAccessTokenSvc {
//    /**
//     * 获取access token
//     * 
//     * @param appId
//     *            微信的AppId
//     * 
//     * @return 如果还未请求获取到token，则返回null
//     */
//    String getAccessToken(String appId);

    /**
     * 强制刷新access token(不用等到下次请求时间)
     * 
     * @param appId
     *            微信的AppId
     */
    void forceRefreshAccessToken(String appId);

    /**
     * 刷新access token(如果当前时间大于下次请求时间才会发出请求)
     */
    void refreshAccessToken();

}
