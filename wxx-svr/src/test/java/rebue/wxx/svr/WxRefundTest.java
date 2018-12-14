package rebue.wxx.svr;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rebue.wheel.OkhttpUtils;
import rebue.wxx.wxpay.to.WxRefundTo;

public class WxRefundTest {

	private final static Logger _log = LoggerFactory.getLogger(WxRefundTest.class);

	private final String hostUrl = "http://localhost:20080";

	@Test
	public void modifyPayOrderIdTest() throws IOException {

		WxRefundTo to = new WxRefundTo();
		
		to.setOrderPayId(String.valueOf(123L));
		to.setOrderAmount(235.00);
		to.setRefundAmount(12.00);
		to.setRefundId(String.valueOf(1555L));
		
		OkhttpUtils.postByJsonParams(hostUrl + "/wxx/wxpay/request/refund", to);
		
		
		
	}
}
