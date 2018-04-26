package rebue.wxx.wxpay.ro;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 微信支付订单的查询结果
 */
@JsonInclude(Include.NON_NULL)
public class WxpayOrderQueryRo {
    /**
     * 支付交易的金额(单位为“元”，精确到小数点后4位)
     */
    private Double tradeAmount;
    /**
     * 支付订单号
     */
    private String payOrderId;
    /**
     * 支付完成时间
     */
    private Date   payTime;

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return "PayOrderQueryRo [tradeAmount=" + tradeAmount + ", payOrderId=" + payOrderId + ", payTime=" + payTime
                + "]";
    }

}
