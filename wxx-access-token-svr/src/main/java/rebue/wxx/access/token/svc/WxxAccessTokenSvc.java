package rebue.wxx.access.token.svc;

public interface WxxAccessTokenSvc {
    /**
     * 获取access token
     * 
     * @return 如果还未请求获取到token，则返回null
     */
    String getAccessToken();

    /**
     * 刷新access token
     */
    void refreshAccessToken();

}
