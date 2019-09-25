package rebue.wxx.svr;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;

public class WxxRequestTest {
//    private final static String _hostUrl = "http://127.0.0.1:20080";
    private final static String _hostUrl = "http://www.nnzbz.cn";
//    private final static String _hostUrl = "http://www.duamai.com/wxx-svr";

    private final static String _appId  = "wx380f881a4f0a2058";
    private final static String _openid = "oOD1W1B9tjcojciC47RpHFAF3QOY";

    /**
     * 创建菜单
     */
    @Test
    public void test01() throws IOException, RedisSetException, InterruptedException, DocumentException {
        final String url = _hostUrl + "/wxx/request/menu?appId=" + _appId;
        Assertions.assertEquals("\"ok\"", OkhttpUtils.put(url));
    }

    /**
     * 判断用户是否关注公众号
     */
    @Test
    public void test02() throws IOException, RedisSetException, InterruptedException, DocumentException {
        final String url = _hostUrl + "/wxx/request/issubscribe?appId=" + _appId + "&openId=" + _openid;
        Assertions.assertNotEquals("", OkhttpUtils.get(url));
    }

    /**
     * 长链接转短链接
     */
    @Test
    public void test03() throws IOException, RedisSetException, InterruptedException, DocumentException {
        final String url = _hostUrl + "/wxx/request/shorturl";
        final String jsonParams = "{\"appId\":\"" + _appId //
                + "\",\"longUrl\": \"https%3A%2F%2Fopen.weixin.qq.com%2Fconnect%2Foauth2%2Fauthorize%3Fappid%3D" + _appId
                + "%26redirect_uri%3Dhttp%253A%252F%252Fwww.nnzbz.cn%252Fwxx%252Fresponse%252Fauthorizecode%253Fappid%253D" + _appId
                + "%26response_type%3Dcode%26scope%3Dsnsapi_userinfo%26state%3DSTATE%23wechat_redirect\"},"//
                + "{\"type\":\"view\",\"name\":\"快递查询\",\"url\":\"https://www.duamai.com/damai-wx-web/kdi/KdiSearch.html\"\"}";
        Assertions.assertNotEquals("", OkhttpUtils.postByJsonParams(url, jsonParams));
    }

    /**
     * 获取微信服务器IP地址
     */
    @Test
    public void test04() throws IOException {
        final String url = _hostUrl + "/wxx/request/wxserverips?appId=" + _appId;
        Assertions.assertNotEquals("", OkhttpUtils.get(url));
    }

}
