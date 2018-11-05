package rebue.wxx.access.token.svc;

public interface WxxAccessTokenSvc {
    /**
     * 获取access token
     * 
     * @return 如果还未请求获取到token，则返回null
     */
    String getAccessToken();

    /**
     * 强制刷新access token(不用等到下次请求时间)
     */
    void forceRefreshAccessToken();

    /**
     * 刷新access token(如果当前时间大于下次请求时间才会发出请求)
     */
    void refreshAccessToken();

}
