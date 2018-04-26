package rebue.wxx.access.token.svr.svc.impl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import rebue.wxx.access.token.svr.svc.WxxAccessTokenSvc;
import rebue.wxx.svr.feign.WxxRequestSvc;

@Service
@RefreshScope
public class WxxAccessTokenSvcImpl implements WxxAccessTokenSvc, ApplicationListener<ApplicationStartedEvent> {
//public class WxxAccessTokenSvcImpl implements WxxAccessTokenSvc {

    private final static Logger _log         = LoggerFactory.getLogger(WxxAccessTokenSvcImpl.class);

    /**
     * 启动标志，防止多次启动
     */
    private boolean             bStartedFlag = false;

    /**
     * 请求access token的线程
     */
    private Thread              _requestAccessTokenThread;

    @Resource
    private WxxRequestSvc       _wxxRequestSvc;

    private String              _accessToken;

    private Long                _requestLastTime;

//    @PostConstruct
//    private void init() throws IOException {
//        _log.info("AccessToken服务器初始化，准备发出刷新Access Token的请求");
//        requestAccessToken();
//    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 防止多次启动
        if (bStartedFlag)
            return;
        bStartedFlag = true;

        _log.info("AccessToken服务器初始化，准备发出刷新Access Token的请求");
        requestAccessToken();
    }

    /**
     * @return 获取access token
     */
    @Override
    public String getAccessToken() {
        if (_accessToken == null)
            requestAccessToken();
        return _accessToken;
    }

    /**
     * @return 刷新access token
     */
    @Override
    public void refreshAccessToken() {
        requestAccessToken();
    }

    private synchronized void requestAccessToken() {
        if (_requestLastTime == null) {
            _requestLastTime = System.currentTimeMillis();
        } else {
            if (System.currentTimeMillis() - _requestLastTime < 5000) {
                _log.error("请求时间间隔太短");
                return;
            }
        }

        if (_requestAccessTokenThread != null) {
            _log.info("中断之前请求access token的线程，再重新创建请求线程");
            _requestAccessTokenThread.interrupt();
            try {
                _requestAccessTokenThread.join(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        _requestAccessTokenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        try {
                            _log.info("正式发出刷新Access Token的请求(需要在公众号中设置IP白名单，否则微信服务器会返回40164错误)");
                            Map<String, Object> map = _wxxRequestSvc.getAccessToken();
                            if (map == null) {
                                throw new IOException();
                            }
                            String accessToken = (String) map.get("access_token");
                            Integer expiresIn = (Integer) map.get("expires_in");
//                            Integer expiresIn = 30;
                            if (!StringUtils.isBlank(accessToken) && expiresIn != null) {
                                _log.info("获取到access token: {}", accessToken);
                                _accessToken = accessToken;
                                // AccessToken有时效性，延迟一段时间后（失效前5分钟）再次发出请求
                                Thread.sleep((expiresIn - 5 * 60) * 1000);
                            } else {
                                _log.error("接收到不正常的返回结果");
                                Thread.sleep(30000);
                            }
                        } catch (IOException e) {
                            _log.error("请求access token出现异常", e);
                            e.printStackTrace();
                            Thread.sleep(5000);
                        }
                    }
                } catch (InterruptedException e) {
                    _log.error("线程" + Thread.currentThread().getName() + "被中断", e);
                    e.printStackTrace();
                }
            }
        });

        _requestAccessTokenThread.start();
    }

}
