package rebue.wxx.redis.svc.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rebue.sbs.redis.RedisClient;
import rebue.wxx.redis.eo.WxxAccessTokenEo;
import rebue.wxx.redis.svc.WxxAccessTokenRedisSvc;

@Service
@Slf4j
public class WxxAccessTokenRedisSvcImpl implements WxxAccessTokenRedisSvc {

    /**
     * 缓存微信access token的信息
     * 后面跟微信的appid拼接成Key
     * Value为WxxAccessTokenEo
     */
    private static final String REDIS_KEY_WXX_ACCESSTOKEN_PREFIX = "rebue.wxx.redis.svc.accesstoken.";

    @Resource
    private RedisClient         redisClient;

//    /**
//     * 从redis中获取所有access token信息
//     */
//    @Override
//    public List<WxxAccessTokenEo> listAll() {
//        log.info("从redis中获取所有access token信息");
//        return redisClient.listByWildcard(REDIS_KEY_WXX_ACCESSTOKEN_PREFIX + "*", WxxAccessTokenEo.class);
//    }

    /**
     * 从redis中获取access token信息
     */
    @Override
    public WxxAccessTokenEo get(final String appId) {
        log.info("从redis中获取access token信息: appId-{}", appId);
        return redisClient.getObj(REDIS_KEY_WXX_ACCESSTOKEN_PREFIX + appId, WxxAccessTokenEo.class);
    }

    /**
     * 在redis中设置access token信息
     */
    @Override
    public void set(final WxxAccessTokenEo accessTokenEo, final Integer expiresIn) {
        log.info("在redis中设置access token信息: accessTokenEo-{}", accessTokenEo);
        redisClient.setObj(REDIS_KEY_WXX_ACCESSTOKEN_PREFIX + accessTokenEo.getId(), accessTokenEo, expiresIn);
    }
}
