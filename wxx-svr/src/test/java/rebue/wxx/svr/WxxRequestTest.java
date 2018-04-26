package rebue.wxx.svr;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Test;

import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;

public class WxxRequestTest {
    private final static String _hostUrl = "http://nnzbz.free.ngrok.cc";
//    private final static String _hostUrl = "http://www.duamai.com/wxx-svr";

    /**
     * 创建菜单
     */
    @Test
    public void test01() throws IOException, RedisSetException, InterruptedException, DocumentException {
        String url = _hostUrl + "/wxx/request/menu";
        Assert.assertEquals("ok", OkhttpUtils.put(url));
    }

}
