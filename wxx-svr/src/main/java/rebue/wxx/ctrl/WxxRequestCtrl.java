package rebue.wxx.ctrl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rebue.wxx.svc.WxxRequestSvc;
import rebue.wxx.to.LongUrlTo;

/**
 * 向微信服务器发送请求的控制器
 */
@RestController
@RefreshScope
public class WxxRequestCtrl {
    private final static Logger _log = LoggerFactory.getLogger(WxxRequestCtrl.class);

    @Resource
    private WxxRequestSvc       wxxRequestSvc;

    /**
     * 获取AccessToken
     */
    @GetMapping("/wxx/request/accesstoken")
    public Map<String, Object> getAccessToken() {
        _log.info("wxx getAccessToken");
        return wxxRequestSvc.getAccessToken();
    }

    /**
     * 是否关注微信公众号
     */
    @GetMapping("/wxx/request/issubscribe")
    public Boolean isSubscribe(@RequestParam("openId") String openId) {
        _log.info("wxx isSubscribe: {}", openId);
        return wxxRequestSvc.isSubscribe(openId);
    }

    /**
     * 获取短链接
     */
    @PostMapping("/wxx/request/shorturl")
    public String getShortUrl(@RequestBody LongUrlTo to) {
        _log.info("wxx getShortUrl: {}", to);
        return wxxRequestSvc.getShortUrl(to);
    }

    /**
     * 获取微信服务器IP地址
     */
    @GetMapping("/wxx/request/wxserverips")
    public List<String> getWxServerIps() {
        _log.info("wxx getWxServerIps");
        return wxxRequestSvc.getWxServerIps();
    }

    /**
     * 更新微信公众号菜单
     */
    @PutMapping("/wxx/request/menu")
    public String updateMenu() {
        _log.info("wxx updateMenu");
        return wxxRequestSvc.updateMenu();
    }

}
