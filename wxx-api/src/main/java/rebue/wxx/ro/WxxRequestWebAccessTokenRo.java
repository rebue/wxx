package rebue.wxx.ro;

import lombok.Data;

@Data
public class WxxRequestWebAccessTokenRo {
    private String  access_token;
    private Integer expires_in;
    private String  refresh_token;
    private String  openid;
    private String  scope;
}
