package rebue.wxx.wxpay.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import rebue.wxx.wxpay.dic.WxpayPrepayResultDic;

/**
 * 微信支付-预支付的结果
 */
@JsonInclude(Include.NON_NULL)
public class WxpayPrepayRo {

    /**
     * 返回结果
     */
    private WxpayPrepayResultDic result;
    /**
     * 预支付ID
     * (预支付成功才会返回)
     */
    private String               prepayId;

    /**
     * 失败原因
     * (预支付失败才会返回)
     */
    private String               failReason;

    public WxpayPrepayResultDic getResult() {
        return result;
    }

    public void setResult(WxpayPrepayResultDic result) {
        this.result = result;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    @Override
    public String toString() {
        return "WxpayPrepayRo [result=" + result + ", prepayId=" + prepayId + ", failReason=" + failReason + "]";
    }

}
