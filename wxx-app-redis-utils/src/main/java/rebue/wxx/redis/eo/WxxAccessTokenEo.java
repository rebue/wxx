package rebue.wxx.redis.eo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 记录微信AccessToken的的实体
 *
 */
@Data
public class WxxAccessTokenEo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 微信的appid
     */
    private String            id;

    /**
     * 微信的Access Token
     */
    private String            accessToken;

    /**
     * 下次请求时间
     */
    private Date              nextRequestTime;
}
