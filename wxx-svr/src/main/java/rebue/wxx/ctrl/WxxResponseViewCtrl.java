package rebue.wxx.ctrl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.wheel.MapUtils;
import rebue.wheel.turing.SignUtils;
import rebue.wxx.svc.WxxResponseSvc;

/**
 * 响应微信发过来请求的控制器（要求响应视图的请求）
 */
@Controller
public class WxxResponseViewCtrl {
    private static final Logger _log = LoggerFactory.getLogger(WxxResponseViewCtrl.class);

    @Resource
    private WxxResponseSvc      wxxResponseSvc;

    /**
     * 微信登录回调的签名key
     */
    @Value("${wxx.loginCallback.signKey}")
    private String              wxLoginCallbackSignKey;

    /**
     * 微信用户登录测试页面
     */
    @GetMapping("/wxx/response/view/login")
    public String login(@RequestParam Map<String, Object> requestParams) {
        MapUtils.decodeUrl(requestParams);
        _log.info("wxx login callback: {}", requestParams);
        if (!SignUtils.verify1(requestParams, wxLoginCallbackSignKey))
            return null;
        return "Reg";// templates/Reg.ftl
    }

}
