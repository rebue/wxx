package rebue.wxx.svr;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Test;

import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;

public class WxxRequestTest {
//    private final static String _hostUrl = "http://127.0.0.1:20080";
    private final static String _hostUrl = "http://www.nnzbz.cn";
//    private final static String _hostUrl = "http://www.duamai.com/wxx-svr";

    private final static String _openid = "ous-5whDpUPOxd18-XpzMRLEEllo";

    /**
     * 创建菜单
     */
    @Test
    public void test01() throws IOException, RedisSetException, InterruptedException, DocumentException {
        final String url = _hostUrl + "/wxx/request/menu";
        Assert.assertEquals("\"ok\"", OkhttpUtils.put(url));
    }

    /**
     * 判断用户是否关注公众号
     */
    @Test
    public void test02() throws IOException, RedisSetException, InterruptedException, DocumentException {
        final String url = _hostUrl + "/wxx/request/issubscribe?openId=" + _openid;
        Assert.assertNotEquals("", OkhttpUtils.get(url));
    }

    /**
     * 长链接转短链接
     */
    @Test
    public void test03() throws IOException, RedisSetException, InterruptedException, DocumentException {
        final String url = _hostUrl + "/wxx/request/shorturl";
        final String jsonParams = "{\"longUrl\": \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx75f0e29692eab341&redirect_uri=https%3A%2F%2Fwww.duamai.com%2Fwxx-svr%2Fwxx%2Fresponse%2Fauthorizecode&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect\"},{\"type\":\"view\",\"name\":\"快递查询\",\"url\":\"https://www.duamai.com/damai-wx-web/kdi/KdiSearch.html\"\"}";
        Assert.assertNotEquals("", OkhttpUtils.postByJsonParams(url, jsonParams));
    }

}
