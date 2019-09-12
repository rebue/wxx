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

    /**
     * 测试登录
     */
    @Test
    public void testLogin() throws IOException, RedisSetException, InterruptedException, DocumentException {
        // 也可在浏览器地址栏或微信中发消息，输入下面的地址
        // https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx380f881a4f0a2058&redirect_uri=http%3A%2F%2Fwww.nnzbz.cn%2Fwxx%2Fresponse%2Fauthorizecode%3Fappid%3Dwx380f881a4f0a2058&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
        final String appId = "wx380f881a4f0a2058";
        final String url = _hostUrl + "/wxx/login/url";
        final Map<String, Object> requestParams = new LinkedHashMap<>();
        requestParams.put("redirecthost", "http://www.nnzbz.cn");
        requestParams.put("appid", appId);
        requestParams.put("state", "STATE");
        String loginUrl = OkhttpUtils.get(url, requestParams);
        loginUrl = loginUrl.substring(1, loginUrl.length() - 1);
        log.info(loginUrl);
        log.info(OkhttpUtils.get(loginUrl));
    }

}
