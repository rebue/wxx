package rebue.wxx.ro;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 向微信服务器发出请求的响应结果
 *
 */
@Getter
@Setter
@ToString
public class WxRequestShortUrlRo extends WxRequestRo {
    /**
     * 短url
     */
    private String short_url;

}
