package rebue.wxx.access.token.svc.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rebue.wxx.access.token.svc.WxxAccessTokenSvc;
import rebue.wxx.redis.eo.WxxAccessTokenEo;
import rebue.wxx.redis.svc.WxxAccessTokenRedisSvc;
import rebue.wxx.svr.feign.WxxAppSvc;
import rebue.wxx.svr.feign.WxxRequestSvc;

@Service
@RefreshScope
@Slf4j
//public class WxxAccessTokenSvcImpl implements WxxAccessTokenSvc, ApplicationListener<ApplicationStartedEvent> {
public class WxxAccessTokenSvcImpl implements WxxAccessTokenSvc {

    /**
     * 提前刷新access token的分钟数
     */
    @Value("${wxx.accesstoken.earlyMinutes:20}")
    private Short                  earlyMinutes;

    @Resource
    private WxxAppSvc              appSvc;
    @Resource
    private WxxRequestSvc          requestSvc;
    @Resource
    private WxxAccessTokenRedisSvc accessTokenRedisSvc;

    /**
     * 强制刷新access token(马上发出请求，不用等到下次请求时间)
     */
    @Override
    public void forceRefreshAccessToken(final String appId) {
        final WxxAccessTokenEo accessTokenEo = accessTokenRedisSvc.get(appId);
        if (accessTokenEo == null) {
            return;
        }
        requestAccessToken(accessTokenEo);
    }

    /**
     * 刷新access token(如果当前时间大于下次请求时间才会发出请求)
     */
    @Override
    public void refreshAccessToken() {
        for (final String appId : appSvc.listAppIds()) {
            WxxAccessTokenEo accessTokenEo = accessTokenRedisSvc.get(appId);
            if (accessTokenEo == null) {
                accessTokenEo = new WxxAccessTokenEo();
                accessTokenEo.setId(appId);
            }
            // 如果下次请求时间为空或小于当前时间，发出请求
            if (accessTokenEo.getNextRequestTime() == null //
                    || accessTokenEo.getNextRequestTime().compareTo(new Date()) < 0) {
                requestAccessToken(accessTokenEo);
            } else {
                log.trace("还未到计划下次发出请求的时间: appEo-{}", accessTokenEo);
            }
        }
    }

    /**
     * 请求access token
     */
    private void requestAccessToken(final WxxAccessTokenEo accessTokenEo) {
        try {
            log.info("正式发出刷新Access Token的请求(需要在公众号中设置IP白名单，否则微信服务器会返回40164错误)");
            final Map<String, Object> map = requestSvc.getAccessToken(accessTokenEo.getId());
            if (map == null) {
                throw new IOException();
            }
            final String accessToken = (String) map.get("access_token");
            final Integer expiresIn = (Integer) map.get("expires_in");  // 多少秒后失效
            if (!StringUtils.isBlank(accessToken) && expiresIn != null) {
                log.info("获取到access token: {}", accessToken);
                accessTokenEo.setAccessToken(accessToken);
                // XXX wxx: AccessToken有时效性，延迟一段时间后（默认失效前20分钟）再次发出请求
                accessTokenEo.setNextRequestTime(new Date(System.currentTimeMillis() + (expiresIn - earlyMinutes * 60) * 1000L));
                accessTokenRedisSvc.set(accessTokenEo, expiresIn);
            } else {
                log.error("接收到不正常的返回结果: {}", map);
            }
        } catch (final IOException e1) {
            log.error("请求access token出现异常", e1);
        }
    }

}
