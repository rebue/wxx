package rebue.wxx.redis.svc;

import java.util.List;

import rebue.wxx.jo.WxxAppJo;

public interface WxxAppRedisSvc {
    /**
     * 从redis中获取所有app信息
     */
    List<WxxAppJo> listAll();

    /**
     * 从redis中获取app信息
     */
    WxxAppJo get(String appId);

    /**
     * 在redis中设置app信息
     */
    void set(WxxAppJo appJo);

}
