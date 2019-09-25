package rebue.wxx.ctrl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class WxxRequestCtrl {
    private final static Logger _log = LoggerFactory.getLogger(WxxRequestCtrl.class);

    @Resource
    private WxxRequestSvc       requestSvc;

    /**
     * 获取AccessToken
     */
    @GetMapping("/wxx/request/accesstoken")
    public Map<String, Object> getAccessToken(@RequestParam("appId") final String appId) {
        _log.info("wxx getAccessToken");
        return requestSvc.getAccessToken(appId);
    }

    /**
     * 更新微信公众号菜单
     */
    @PutMapping("/wxx/request/menu")
    public String updateMenu(@RequestParam("appId") final String appId) {
        _log.info("wxx updateMenu");
        return requestSvc.updateMenu(appId);
    }

    /**
     * 是否关注微信公众号
     */
    @GetMapping("/wxx/request/issubscribe")
    public Boolean isSubscribe(@RequestParam("appId") final String appId, @RequestParam("openId") final String openId) {
        _log.info("wxx isSubscribe: appId-{} openId-{}", appId, openId);
        return requestSvc.isSubscribe(appId, openId);
    }

    /**
     * 获取短链接
     */
    @PostMapping("/wxx/request/shorturl")
    public String getShortUrl(@RequestBody final LongUrlTo to) {
        _log.info("wxx getShortUrl: {}", to);
        return requestSvc.getShortUrl(to);
    }

    /**
     * 获取微信服务器IP地址
     */
    @GetMapping("/wxx/request/wxserverips")
    public List<String> getWxServerIps(@RequestParam("appId") final String appId) {
        _log.info("wxx getWxServerIps");
        return requestSvc.getWxServerIps(appId);
    }

}
