package rebue.wxx.wxpay.msg;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 微信支付的支付完成通知
 */
@JsonInclude(Include.NON_NULL)
@Data
public class WxpayPayDoneMsg {
    /**
     * 用户ID
     */
    private Long       userId;
    /**
     * 支付账户ID
     */
    private String     payAccountId;
    /**
     * 支付交易的金额(单位为元)
     */
    private BigDecimal payAmount;
    /**
     * 微信支付交易ID
     */
    private String     tradeId;
    /**
     * 订单号
     */
    private String     orderId;
    /**
     * 支付完成时间
     */
    private Date       payTime;

}
