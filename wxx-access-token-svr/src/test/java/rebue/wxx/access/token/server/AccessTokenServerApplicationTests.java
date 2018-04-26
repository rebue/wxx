package rebue.wxx.access.token.server;

import java.io.IOException;

import org.junit.Test;

import rebue.wheel.OkhttpUtils;

public class AccessTokenServerApplicationTests {

    private final static String _hostUrl = "http://120.77.220.106/wxx-access-token-svr";

    @Test
    public void test01() throws IOException {
        String url = _hostUrl + "/wx/access/token";
        OkhttpUtils.post(url);
    }

}
