package rebue.wxx.redis.svc.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rebue.sbs.redis.RedisClient;
import rebue.wxx.redis.svc.WxxUserInfoMapRedisSvc;

@Service
@Slf4j
public class WxxUserInfoMapRedisSvcImpl implements WxxUserInfoMapRedisSvc {

    /**
     * 缓存微信App的信息
     * 后面跟微信的appid和code拼接成Key
     * Value为用户信息的Map
     */
    private static final String REDIS_KEY_WXX_APP_PREFIX = "rebue.wxx.redis.svc.userinfomap.";

    @Resource
    private RedisClient         redisClient;

    /**
     * 从redis中获取app信息
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> get(final String appId, final String code) {
        log.info("从redis中获取app信息: appId-{}, code-{}", appId, code);
        return redisClient.getObj(REDIS_KEY_WXX_APP_PREFIX + appId + "." + code, Map.class);
    }

    /**
     * 在redis中设置app信息
     */
    @Override
    public void set(final String appId, final String code, final Map<String, Object> userInfoMap) {
        log.info("在redis中设置app信息: appId-{} code-{} userInfoMap-{}", appId, code, userInfoMap);
        redisClient.setObj(REDIS_KEY_WXX_APP_PREFIX + appId + "." + code, userInfoMap, 20); // 只保留20秒
    }
}
