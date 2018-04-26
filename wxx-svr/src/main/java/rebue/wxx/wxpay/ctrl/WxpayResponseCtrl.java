package rebue.wxx.wxpay.ctrl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import rebue.wheel.XmlUtils;
import rebue.wxx.wxpay.svc.WxpaySvc;

/**
 * 响应微信支付发过来请求的控制器（要求直接响应字符串数据(Restful)的请求）
 */
@RestController
@RefreshScope
public class WxpayResponseCtrl {
    private static final Logger _log = LoggerFactory.getLogger(WxpayResponseCtrl.class);

    @Resource
    private WxpaySvc            wxpaySvc;

    /**
     * 微信支付-支付通知
     * 
     * @return 返回微信的内容，为null则表示处理时发现有问题
     * @throws DocumentException
     * @throws IOException
     */
    @PostMapping("/wxx/wxpay/response/notify")
    String handleNotify(HttpServletRequest req) throws IOException, DocumentException {
        Map<String, Object> reqParams = XmlUtils.xmlToMap(req.getInputStream());
        _log.info("接收到微信支付-支付完成的通知：{}", reqParams);
        return wxpaySvc.handleNotify(reqParams);
    }

}
