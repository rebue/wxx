package rebue.wxx.redis.svc.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rebue.sbs.redis.RedisClient;
import rebue.wxx.jo.WxxAppJo;
import rebue.wxx.redis.svc.WxxAppRedisSvc;

@Service
@Slf4j
public class WxxAppRedisSvcImpl implements WxxAppRedisSvc {

    /**
     * 缓存微信App的信息
     * 后面跟微信的appid拼接成Key
     * Value为WxxAppJo
     */
    private static final String REDIS_KEY_WXX_APP_PREFIX = "rebue.wxx.redis.svc.app.";

    @Resource
    private RedisClient         redisClient;

    /**
     * 从redis中获取所有app信息
     */
    @Override
    public List<WxxAppJo> listAll() {
        log.info("从redis中获取所有app信息");
        return redisClient.listByWildcard(REDIS_KEY_WXX_APP_PREFIX + "*", WxxAppJo.class);
    }

    /**
     * 从redis中获取app信息
     */
    @Override
    public WxxAppJo get(final String appId) {
        log.info("从redis中获取app信息: appId-{}", appId);
        return redisClient.getObj(REDIS_KEY_WXX_APP_PREFIX + appId, WxxAppJo.class);
    }

    /**
     * 在redis中设置app信息
     */
    @Override
    public void set(final WxxAppJo appJo) {
        log.info("在redis中设置app信息: wxxAppEo-{}", appJo);
        redisClient.setObj(REDIS_KEY_WXX_APP_PREFIX + appJo.getId(), appJo);
    }
}
