package rebue.wxx.redis.svc;

import java.util.Map;

/**
 * 在一段时间内缓存已经获取到的用户信息，微信服务器网页授权时会重复发送code，这里就可以跳过二、三、四步，以免报错
 */
public interface WxxUserInfoMapRedisSvc {
    /**
     * 从redis中获取用户信息
     */
    Map<String, Object> get(String appId, String code);

    /**
     * 在redis中设置用户信息
     */
    void set(String appId, String code, Map<String, Object> userInfoMap);

}
