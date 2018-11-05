package rebue.wxx.access.token.ctrl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import rebue.wxx.access.token.svc.WxxAccessTokenSvc;

@RestController
public class WxxAccessTokenCtrl {

    private final static Logger _log = LoggerFactory.getLogger(WxxAccessTokenCtrl.class);

    @Resource
    private WxxAccessTokenSvc   accessTokenSvc;

    @GetMapping("/wxx")
    public String home() {
        return "Hello, world!";
    }

    /**
     * 获取access token
     * 
     * @return 如果还未请求获取到token，则返回null
     */
    @GetMapping(value = "/wxx/access/token", produces = MediaType.TEXT_PLAIN_VALUE)
//    @GetMapping("/wxx/access/token")
    public String getAccessToken() {
        _log.info("接收到获取Access Token的请求");
        return accessTokenSvc.getAccessToken();
    }

    /**
     * @return 刷新access token
     */
    @PostMapping("/wxx/access/token")
    public void refreshAccessToken() {
        _log.info("接收到刷新Access Token的请求");
        accessTokenSvc.refreshAccessToken();
    }

}
