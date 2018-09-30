package rebue.wxx.svr;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Test;

import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;

public class WxxRequestTest {
    private final static String _hostUrl = "http://127.0.0.1:20080";
//    private final static String _hostUrl = "http://www.nnzbz.cc";
//    private final static String _hostUrl = "http://www.duamai.com/wxx-svr";

    /**
     * 创建菜单
     */
    @Test
    public void test01() throws IOException, RedisSetException, InterruptedException, DocumentException {
        String url = _hostUrl + "/wxx/request/menu";
        Assert.assertEquals("ok", OkhttpUtils.put(url));
    }

    /**
     * 创建菜单
     */
    @Test
    public void test02() throws IOException, RedisSetException, InterruptedException, DocumentException {
        String url = _hostUrl + "/wxx/request/shorturl";
        String jsonParams = "{\"longUrl\": \"http://www.nnzbz.cc\"}";
        Assert.assertEquals("ok", OkhttpUtils.postByJsonParams(url, jsonParams));
    }

}
