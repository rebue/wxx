package rebue.wxx.svr;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;

@Slf4j
public class WxxLoginTest {
//    private final static String _hostUrl = "http://127.0.0.1:20080";
    private final static String _hostUrl = "http://www.nnzbz.cn";
//    private final static String _hostUrl = "http://www.duamai.com/wxx-svr";

    private final static String _openid = "ous-5whDpUPOxd18-XpzMRLEEllo";

    /**
     * 测试登录
     */
    @Test
    public void testLogin() throws IOException, RedisSetException, InterruptedException, DocumentException {
        // 也可在浏览器地址栏输入http://www.nnzbz.cn/wxx/login/redirect?redirecthost=http%3A%2F%2Fwww.nnzbz.cn&appid=wxdf39036bbca9c61a&state=STATE
        final String appId = "wx380f881a4f0a2058";
        final String url = _hostUrl + "/wxx/login/url";
        final Map<String, Object> requestParams = new LinkedHashMap<>();
        requestParams.put("redirecthost", "http://www.nnzbz.cn");
        requestParams.put("appid", appId);
        requestParams.put("state", "STATE");
        String loginUrl = OkhttpUtils.get(url, requestParams);
        loginUrl = loginUrl.substring(1, loginUrl.length() - 1);
        log.info(loginUrl);
//        url = _hostUrl + "/wxx/response/authorizecode";
//        requestParams.clear();
//        requestParams.put("appid", appId);
//        requestParams.put("code", RandomEx.randomUUID());
//        requestParams.put("state", "STATE");
        log.info(OkhttpUtils.get(loginUrl));
    }

}
