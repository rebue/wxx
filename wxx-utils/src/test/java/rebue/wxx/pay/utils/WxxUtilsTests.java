package rebue.wxx.pay.utils;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rebue.wheel.RandomEx;
import rebue.wxx.wxpay.ro.WxpayOrderQueryRo;
import rebue.wxx.wxpay.to.WxpayPrepayTo;
import rebue.wxx.wxpay.utils.WxpayUtils;

public class WxxUtilsTests {
    private final static Logger _log    = LoggerFactory.getLogger(WxxUtilsTests.class);

    /**
     * 公众账号ID
     */
    private final static String APP_ID  = "wx9e24a0de9e3e136c";
    /**
     * 商户号
     */
    private final static String MCH_ID  = "1444599902";
    /**
     * 微信ID
     */
    private final static String WX_ID   = "onCVJv-B-9g1a5FcUxtI1bXqGWEo";

    /**
     * 签名密钥
     */
    private String              signKey = "hsjslejehgsabkl458shgdd4e47ehhss";

    @Before
    public void init() throws IOException, DocumentException {
        WxpayUtils.isSandbox = true;
    }

    @Test
    public void test01() throws InterruptedException {
        WxpayPrepayTo to = new WxpayPrepayTo();
        to.setWxId(WX_ID);
        to.setOrderId(RandomEx.randomUUID());
        to.setTradeTitle("支付交易的标题");
        to.setTradeDetail("支付交易的详情");
        to.setTradeAmount(1.01);
        to.setIp("192.168.1.1");
        String prepayId = WxpayUtils.prepay(APP_ID, MCH_ID, to, "http://120.77.220.106/afc-svr/wxpay/notify", signKey);
        Assert.assertNotNull(prepayId);
        _log.info("微信预支付的ID为: {}", prepayId);

        // 延时5秒等待模拟用户支付
        Thread.sleep(5000);

        WxpayOrderQueryRo result = WxpayUtils.queryOrder(APP_ID, MCH_ID, to.getOrderId(), prepayId);
        Assert.assertNotNull(result);
        _log.info("查询的结果为: {}", result);
    }

}
