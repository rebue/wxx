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
     * 微信登录回调的地址
     */
    @Value("${wxx.loginCallback.url}")
    private String              wxLoginCallbackUrl;
    /**
     * 微信登录回调的地址方法类型(GET/POST/PUT/DELETE....)
     */
    @Value("${wxx.loginCallback.methodType}")
    private String              wxLoginCallbackMethodType;

    /**
     * 微信用户登录测试页面
     */
    @GetMapping("/wxx/response/view/login")
    public String login(@RequestParam final Map<String, Object> requestParams) {
        MapUtils.decodeUrl(requestParams);
        _log.info("wxx login callback: {}", requestParams);
        if (!SignUtils.verify1(requestParams, wxLoginCallbackSignKey)) {
            return null;
        }
        return "Reg";// templates/Reg.ftl
    }

//    /**
//     * 微信授权回调接口，重定向登录页面
//     * 
//     * @param code
//     *            获取到授权的code
//     */
//    @GetMapping("/wxx/response/authorizecode")
//    ModelAndView authorizeCode(@RequestParam("code") final String code, @RequestParam(value = "state", required = false) final String state, final HttpServletResponse resp)
//            throws IOException {
//        _log.info("接收到微信授权回调: {}，{}", code, state);
//        final Map<String, Object> userInfo = wxxResponseSvc.authorizeCode(code);
//        ModelAndView modelAndView;
////        String modelAndView = "GetWxUserInfoFail";
//        if (userInfo == null) {
//            _log.info("返回获取微信用户信息失败页面");
//            modelAndView = new ModelAndView("GetWxUserInfoFail");
//        } else {
//            _log.info("给用户信息map添加签名(重定向登录页面需要签名)");
//            SignUtils.sign1(userInfo, wxLoginCallbackSignKey);
//            userInfo.put("state", state);
//            _log.info("跳转用户登录页面: {}:{}", wxLoginCallbackMethodType, wxLoginCallbackUrl);
////            modelAndView = new ModelAndView("GetWxUserInfoFail");
////            modelAndView = new ModelAndView("ForwardUserLogin");
////            modelAndView.addObject("methodType", wxLoginCallbackMethodType);
////            modelAndView.addObject("forwardUrl", wxLoginCallbackUrl);
////            modelAndView.addObject("userInfo", userInfo);
//            modelAndView = new ModelAndView(new RedirectView(wxLoginCallbackUrl));
//        }
//        return modelAndView;
//    }

}
