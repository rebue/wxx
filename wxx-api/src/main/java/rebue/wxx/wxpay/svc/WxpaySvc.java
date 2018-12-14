package rebue.wxx.wxpay.svc;

import java.util.Map;

import org.xml.sax.SAXException;

import rebue.wxx.wxpay.ro.WxRefundRo;
import rebue.wxx.wxpay.ro.WxpayOrderQueryRo;
import rebue.wxx.wxpay.ro.WxpayPrepayRo;
import rebue.wxx.wxpay.to.WxRefundTo;
import rebue.wxx.wxpay.to.WxpayPrepayTo;

public interface WxpaySvc {
    /**
     * 微信支付-预支付
     * @throws SAXException 
     */
    WxpayPrepayRo prepay(WxpayPrepayTo to) throws SAXException;

    /**
     * 微信支付-查询订单
     * 
     * @param orderId
     *            销售订单ID
     * @throws SAXException 
     */
    WxpayOrderQueryRo queryOrder(String orderId) throws SAXException;

    /**
     * 微信支付-处理支付完成的通知
     */
    String handleNotify(Map<String, Object> reqParams);
    
    /**
     * 微信退款
     */
    WxRefundRo wxRefund(WxRefundTo to) throws SAXException;
    
}
