package rebue.wxx.wxpay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rebue.wheel.MoneyUtils;
import rebue.wxx.wxpay.ro.WxpayNotifyRo;

/**
 * 微信支付的相关应用
 *
 */
public class WxpayUtils {
    private final static Logger _log = LoggerFactory.getLogger(WxpayUtils.class);

    /**
     * 微信支付-支付通知
     */
    public static WxpayNotifyRo handleNotify(Map<String, Object> params, String appId, String mchId, String signKey) {
        

    }

}
