package rebue.wxx.svr;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import rebue.sbs.redis.RedisSetException;
import rebue.wheel.OkhttpUtils;
import rebue.wheel.RandomEx;
import rebue.wxx.wxpay.ro.WxpayOrderQueryRo;
import rebue.wxx.wxpay.to.WxpayPrepayTo;

public class WxpayTest {
    private final static Logger _log     = LoggerFactory.getLogger(WxpayTest.class);

    private final static String HOST_URL = "http://nnzbz.free.ngrok.cc";
//    private final static String HOST_URL              = "http://www.duamai.com/wxx-api-svr";

    /**
     * 微信支付-预支付的URL
     */
    private final static String WXPAY_PREPAY_URL      = HOST_URL + "/wxx/wxpay/request/prepay";
    /**
     * 微信支付-查询订单的URL
     */
    private final static String WXPAY_QUERY_ORDER_URL = HOST_URL + "/wxx/wxpay/request/queryorder";

    /**
     * 微信ID
     */
    private final static String WX_ID                 = "onCVJv-B-9g1a5FcUxtI1bXqGWEo";

    private ObjectMapper        _objectMapper         = new ObjectMapper();

    /**
     * 测试微信支付-预支付与查询订单（沙箱测试）
     */
    @Test
    public void test01() throws IOException, RedisSetException, InterruptedException, DocumentException {
        WxpayPrepayTo to = new WxpayPrepayTo();
        to.setUserId(425547210030186496L);
        to.setWxId(WX_ID);
        to.setOrderId(RandomEx.randomUUID());
        to.setTradeTitle("支付交易的标题");
        to.setTradeDetail("支付交易的详情");
        to.setTradeAmount(1.01);
        to.setIp("192.168.1.1");

        String prepayId = OkhttpUtils.postByJsonParams(WXPAY_PREPAY_URL, _objectMapper.writeValueAsString(to));
        Assert.assertNotNull(prepayId);
        _log.info("微信预支付的ID为: {}", prepayId);

        // 延时5秒等待模拟用户支付
        Thread.sleep(5000);

        WxpayOrderQueryRo ro = _objectMapper.readValue(OkhttpUtils.get(WXPAY_QUERY_ORDER_URL + "?orderId=" + to.getOrderId()), WxpayOrderQueryRo.class);
        Assert.assertNotNull(ro);
        _log.info("查询的结果为: {}", ro);
    }

}
