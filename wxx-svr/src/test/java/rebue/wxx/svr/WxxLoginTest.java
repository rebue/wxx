package rebue.wxx.svr;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;
import rebue.wheel.RandomEx;

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
        final String appId = "wxdf39036bbca9c61a";
        String url = _hostUrl + "/wxx/login/url";
        final Map<String, Object> requestParams = new LinkedHashMap<>();
        requestParams.put("redirecthost", "http://www.nnzbz.cn");
        requestParams.put("appid", appId);
        requestParams.put("state", "STATE");
        final String loginUrl = OkhttpUtils.get(url, requestParams);
        log.info(loginUrl);
        url = _hostUrl + "/wxx/response/authorizecode";
        requestParams.clear();
        requestParams.put("appid", appId);
        requestParams.put("code", RandomEx.randomUUID());
        requestParams.put("state", "STATE");
        log.info(OkhttpUtils.get(url, requestParams));
    }

}
