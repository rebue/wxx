package rebue.wxx.access.token.svc.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import rebue.wxx.access.token.svc.WxxAccessTokenSvc;
import rebue.wxx.svr.feign.WxxRequestSvc;

@Service
@RefreshScope
//public class WxxAccessTokenSvcImpl implements WxxAccessTokenSvc, ApplicationListener<ApplicationStartedEvent> {
public class WxxAccessTokenSvcImpl implements WxxAccessTokenSvc {

    private final static Logger _log             = LoggerFactory.getLogger(WxxAccessTokenSvcImpl.class);

    @Resource
    private WxxRequestSvc       _wxxRequestSvc;

    /**
     * 获取到的access token
     */
    private String              _accessToken;

    /**
     * 下次的请求时间
     */
    private Date                _nextRequestTime = new Date();

    /**
     * 获取access token
     * 
     * @return 如果还未请求获取到token，则返回null
     */
    @Override
    public String getAccessToken() {
        return _accessToken;
    }

    /**
     * 强制刷新access token(马上发出请求，不用等到下次请求时间)
     */
    @Override
    public void forceRefreshAccessToken() {
        requestAccessToken();
    }

    /**
     * 刷新access token(如果当前时间大于下次请求时间才会发出请求)
     */
    @Override
    public void refreshAccessToken() {
        final Date now = new Date();
        // 如果当前时间大于下次请求时间，发出请求
        if (now.getTime() > _nextRequestTime.getTime()) {
            requestAccessToken();
        } else {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            _log.trace("还未到计划下次发出请求的时间: {}", sdf.format(_nextRequestTime));
        }

    }

    /**
     * 请求access token
     */
    private void requestAccessToken() {
        try {
            _log.info("正式发出刷新Access Token的请求(需要在公众号中设置IP白名单，否则微信服务器会返回40164错误)");
            final Map<String, Object> map = _wxxRequestSvc.getAccessToken();
            if (map == null) {
                throw new IOException();
            }
            final String accessToken = (String) map.get("access_token");
            final Integer expiresIn = (Integer) map.get("expires_in");
            if (!StringUtils.isBlank(accessToken) && expiresIn != null) {
                _log.info("获取到access token: {}", accessToken);
                _accessToken = accessToken;
                // AccessToken有时效性，延迟一段时间后（失效前5分钟）再次发出请求
                _nextRequestTime = new Date(System.currentTimeMillis() + (expiresIn - 5 * 60) * 1000L);
            } else {
                _log.error("接收到不正常的返回结果");
            }
        } catch (final IOException e1) {
            _log.error("请求access token出现异常", e1);
        }
    }

}
