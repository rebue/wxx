package rebue.wxx.svr.wxpay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.sbs.feign.FeignConfig;
import rebue.wxx.wxpay.ro.WxpayOrderQueryRo;
import rebue.wxx.wxpay.ro.WxpayPrepayRo;
import rebue.wxx.wxpay.to.WxpayPrepayTo;

@FeignClient(name = "wxx-svr", configuration = FeignConfig.class)
public interface WxpayRequestSvc {
    /**
     * 微信支付-预支付
     */
    @PostMapping("/wxx/wxpay/request/prepay")
    WxpayPrepayRo prepay(@RequestBody WxpayPrepayTo to);

    /**
     * 微信支付-查询订单
     */
    @GetMapping("/wxx/wxpay/request/queryorder")
    WxpayOrderQueryRo queryOrder(@RequestParam("orderId") String orderId);
}
