package rebue.wxx.access.token.task;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import rebue.wxx.access.token.svc.WxxAccessTokenSvc;

/**
 * 定时请求微信access token任务
 */
@Component
public class RequestTask {
    private final static Logger _log = LoggerFactory.getLogger(RequestTask.class);

    @Resource
    private WxxAccessTokenSvc   wxxAccessTokenSvc;

    @Scheduled(fixedDelayString = "${wxx.accesstoken.requestFixedDelay:180000}")
    public void executeTasks() {
        _log.trace("定时执行请求access token的任务");
        wxxAccessTokenSvc.refreshAccessToken();
    }
}
