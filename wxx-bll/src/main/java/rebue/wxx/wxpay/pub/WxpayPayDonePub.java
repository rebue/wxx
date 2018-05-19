package rebue.wxx.wxpay.pub;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import rebue.sbs.rabbit.RabbitProducer;
import rebue.wxx.wxpay.co.WxpayExchangeCo;
import rebue.wxx.wxpay.msg.WxpayPayDoneMsg;

/**
 * 微信支付-支付完成通知的发布者
 */
@Component
public class WxpayPayDonePub implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger _log         = LoggerFactory.getLogger(WxpayPayDonePub.class);

    /**
     * 启动标志，防止多次启动
     */
    private boolean             bStartedFlag = false;

    @Resource
    private RabbitProducer      producer;

//    @PostConstruct
//    void init() throws Exception {
//        // 声明Exchange
//        producer.declareExchange(WxpayExchangeCo.PAY_NOTIFY_EXCHANGE_NAME);
//    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 防止多次启动
        if (bStartedFlag)
            return;
        bStartedFlag = true;

        try {
            _log.info("微信支付-声明支付完成消息的Exchange");
            producer.declareExchange(WxpayExchangeCo.PAY_DONE_EXCHANGE_NAME);
        } catch (Exception e) {
            String msg = "微信支付-声明支付完成消息的Exchange失败";
            _log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * 微信支付-发送支付完成的消息
     */
    public void send(WxpayPayDoneMsg ro) {
        _log.info("微信支付-发送支付完成的消息");
        producer.send(WxpayExchangeCo.PAY_DONE_EXCHANGE_NAME, ro);
    }

}
