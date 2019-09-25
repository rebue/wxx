package rebue.wxx.access.token.task;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import rebue.wxx.access.token.svc.WxxAccessTokenSvc;

/**
 * 定时请求微信access token任务
 */
@Component
@Slf4j
public class RequestTask {

    @Resource
    private WxxAccessTokenSvc accessTokenSvc;

    @Scheduled(fixedDelayString = "${wxx.accesstoken.requestFixedDelay:180000}")
    public void executeTasks() {
        log.trace("定时执行请求access token的任务");
        accessTokenSvc.refreshAccessToken();
    }
}
