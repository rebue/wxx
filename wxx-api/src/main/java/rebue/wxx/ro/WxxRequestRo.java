package rebue.wxx.ro;

import lombok.Data;

/**
 * 向微信服务器发出请求的响应结果
 *
 */
@Data
public class WxxRequestRo {
    /**
     * 错误编码
     */
    private Integer errcode;
    /**
     * 错误信息
     */
    private String  errmsg;

}
