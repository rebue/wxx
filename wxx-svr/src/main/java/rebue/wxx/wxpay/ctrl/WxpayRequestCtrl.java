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

import rebue.wxx.wxpay.ro.WxpayOrderQueryRo;
import rebue.wxx.wxpay.ro.WxpayPrepayRo;
import rebue.wxx.wxpay.svc.WxpaySvc;
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
     */
    @PostMapping("/wxx/wxpay/request/prepay")
    public WxpayPrepayRo prepay(@RequestBody WxpayPrepayTo to) {
        _log.info("wxpay prepay: {}", to);
        return wxpaySvc.prepay(to);
    }

    /**
     * 微信支付-查询订单
     */
    @GetMapping("/wxx/wxpay/request/queryorder")
    public WxpayOrderQueryRo queryOrder(@RequestParam("orderId") String orderId) {
        _log.info("wxpay queryOrder: {}", orderId);
        return wxpaySvc.queryOrder(orderId);
    }

}
