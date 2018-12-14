package rebue.wxx.wxpay.ro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import rebue.wxx.wxpay.dic.WxRefundResultDic;

/**
 * 微信退款结果
 */
@JsonInclude(Include.NON_NULL)
@Data
public class WxRefundRo {
	 /**
     * 返回结果
     */
    private WxRefundResultDic result;
    
    /**
     * 失败原因
     * (退款失败才会返回)
     */
    private String            failReason;
	
}
