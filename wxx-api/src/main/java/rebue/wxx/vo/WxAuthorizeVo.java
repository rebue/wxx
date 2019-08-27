package rebue.wxx.vo;

import lombok.Data;

@Data
public class WxAuthorizeVo {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
