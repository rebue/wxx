package rebue.wxx.access.token.svr.svc;

public interface WxxAccessTokenSvc {
    /**
     * @return 获取access token
     */
    String getAccessToken();

    /**
     * @return 刷新access token
     */
    void refreshAccessToken();

}
