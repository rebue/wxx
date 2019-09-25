package rebue.wxx.access.token.ctrl;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import rebue.wxx.access.token.svc.WxxAccessTokenSvc;

@RestController
@Slf4j
public class WxxAccessTokenCtrl {

    @Resource
    private WxxAccessTokenSvc accessTokenSvc;

    @GetMapping("/wxx")
    public String home() {
        return "Hello, world!";
    }

    /**
     * 强制刷新access token(马上发出请求，不用等到下次请求时间)
     */
    @PostMapping("/wxx/access/token/{appId}")
    public void forceRefreshAccessToken(@PathVariable("appId") final String appId) {
        log.info("接收到刷新Access Token的请求: {}", appId);
        accessTokenSvc.forceRefreshAccessToken(appId);
    }

}
