package rebue.wxx.wxpay.ctrl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import rebue.wxx.wxpay.ro.WxRefundRo;
import rebue.wxx.wxpay.ro.WxpayOrderQueryRo;
import rebue.wxx.wxpay.ro.WxpayPrepayRo;
import rebue.wxx.wxpay.svc.WxpaySvc;
import rebue.wxx.wxpay.to.WxRefundTo;
import rebue.wxx.wxpay.to.WxpayPrepayTo;

/**
 * 向微信支付服务器发送请求的控制器
 */
@RestController
@RefreshScope
public class WxpayRequestCtrl {
    private final static Logger _log = LoggerFactory.getLogger(WxpayRequestCtrl.class);

    @Resource
    private WxpaySvc            wxpaySvc;

    /**
     * 微信支付-预支付
     * @throws SAXException 
     */
    @PostMapping("/wxx/wxpay/request/prepay")
    public WxpayPrepayRo prepay(@RequestBody WxpayPrepayTo to) throws SAXException {
        _log.info("wxpay prepay: {}", to);
        return wxpaySvc.prepay(to);
    }

    /**
     * 微信支付-查询订单
     * @throws SAXException 
     */
    @GetMapping("/wxx/wxpay/request/queryorder")
    public WxpayOrderQueryRo queryOrder(@RequestParam("orderId") String orderId) throws SAXException {
        _log.info("wxpay queryOrder: {}", orderId);
        return wxpaySvc.queryOrder(orderId);
    }
    
    /**
     * 微信支付-微信退款
     * @throws SAXException 
     */
    @PostMapping("/wxx/wxpay/request/refund")
    public WxRefundRo refund(@RequestBody WxRefundTo to) throws SAXException {
        _log.info("wxpay refund: {}", to);
        return wxpaySvc.wxRefund(to);
    }
}
