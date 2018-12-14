package rebue.wxx.wxpay.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
@JsonInclude(Include.NON_NULL)
@Data
/**
 * 微信退款请求对象
 */
public class WxRefundTo {
    /**
     * 订单支付ID(与微信订单号二选一)
     */
    private String orderPayId;
    /**
     * 微信订单号(与订单支付ID二选一)
     */
    private String transactionId;
    /**
     * 退款单号,一笔退款失败后重新提交，请不要更换退款单号
     */
    private String refundId;
    /**
     * 定单交易的金额(单位为“分”)
     */
    private Double orderAmount;
    /**
     * 退款金额(单位为“分”)
     */
    private Double refundAmount;
}
