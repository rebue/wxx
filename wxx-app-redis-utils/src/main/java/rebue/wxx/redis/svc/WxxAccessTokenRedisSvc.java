package rebue.wxx.redis.svc;

import rebue.wxx.redis.eo.WxxAccessTokenEo;

public interface WxxAccessTokenRedisSvc {
//    /**
//     * 从redis中获取所有access token的信息
//     */
//    List<WxxAccessTokenEo> listAll();

    /**
     * 从redis中获取access token的信息
     */
    WxxAccessTokenEo get(String appId);

    /**
     * 在redis中设置access token的信息
     * 
     * @param accessTokenEo
     * @param expiresIn
     *            保持这个KV多少秒
     */
    void set(WxxAccessTokenEo accessTokenEo, Integer expiresIn);

}
