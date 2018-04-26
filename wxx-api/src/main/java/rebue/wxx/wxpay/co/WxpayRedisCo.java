package rebue.wxx.wxpay.co;

/**
 * 微信支付Redis的常量
 */
public class WxpayRedisCo {
    /**
     * 缓存支付交易的订单ID的key的前缀，后面跟订单ID拼接成Key
     * Value为支付交易的金额
     */
    public final static String REDIS_KEY_WXPAY_ORDERID_PREFIX    = "rebue.wxpay.pay.orderid.";

    /**
     * 消息队列频道-微信支付-支付通知
     */
    public final static String REDIS_MQ_CHANNEL_WXPAY_PAY_NOTIFY = "rebue.wxpay.pay.notify";
}
