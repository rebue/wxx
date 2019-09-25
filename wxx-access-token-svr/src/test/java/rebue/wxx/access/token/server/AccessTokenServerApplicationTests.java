package rebue.wxx.access.token.server;

import java.io.IOException;

import org.junit.Test;

import rebue.wheel.OkhttpUtils;

public class AccessTokenServerApplicationTests {

//    private final static String _hostUrl = "http://127.0.0.1:9000";
    private final static String _hostUrl = "http://127.0.0.1:9333";

    /**
     * 强制刷新access token
     */
    @Test
    public void test01() throws IOException {
        final String url = _hostUrl + "/wxx/access/token/";
        OkhttpUtils.post(url + "wxdf39036bbca9c61a");
        OkhttpUtils.post(url + "wx380f881a4f0a2058");
    }

}
