package rebue.wxx.ctrl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import rebue.wheel.XmlUtils;
import rebue.wheel.turing.SignUtils;
import rebue.wxx.svc.WxxRequestSvc;
import rebue.wxx.svc.WxxResponseSvc;
import rebue.wxx.vo.WxAuthorizeVo;

/**
 * 响应微信发过来请求的控制器（要求直接响应字符串数据(Restful)的请求）
 */
@RestController
@RefreshScope
public class WxxResponseRestCtrl {
    private static final Logger _log = LoggerFactory.getLogger(WxxResponseRestCtrl.class);

    @Resource
    private WxxResponseSvc      wxxResponseSvc;
    @Resource
    private WxxRequestSvc       wxxRequestSvc;

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
     * 微信登录回调的签名key
     */
    @Value("${wxx.loginCallback.signKey}")
    private String              wxLoginCallbackSignKey;

    /**
     * 提供给微信验证本服务器身份的接口
     * 微信公众号绑定网站的域名时，会向此url发送GET请求进行校验，
     * 而要注意，微信通过同样的url，发出POST请求时却是推送消息过来，
     * 所以GET和POST要区分对待，本方法是处理GET的，下一个方法是处理POST的
     */
    @GetMapping(value = "/wxx/response", produces = MediaType.TEXT_PLAIN_VALUE)
    String authorize(final WxAuthorizeVo vo) {
        _log.info("received authorize params: {}", vo);
        return wxxResponseSvc.authorize(vo);
    }

    /**
     * 接收微信服务器发来的消息
     * 
     * @throws SAXException
     */
    @PostMapping(value = "/wxx/response", produces = MediaType.TEXT_PLAIN_VALUE)
    String receiveMsg(final HttpServletRequest req) throws IOException, DocumentException, SAXException {
        final String xml = XmlUtils.getXmlFromRequest(req);
        _log.info("接收微信服务器发来的消息: {}", xml);
        return wxxResponseSvc.handleMsg(XmlUtils.xmlToMap(xml));
    }

    /**
     * 网页授权需要验证此链接
     */
    @GetMapping(value = "/wxx/MP_verify_{id}.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    String mpVerify(@PathVariable("id") final String id) {
        _log.info("mp_verify_{}", id);
        return id;
    }

    /**
     * 微信授权回调接口，重定向登录页面
     * 
     * @param code
     *            获取到授权的code
     */
    @GetMapping("/wxx/response/authorizecode")
    ModelAndView authorizeCode(@RequestParam("code") final String code, @RequestParam(value = "state", required = false) final String state) throws IOException {
        _log.info("接收到微信授权回调: {}，{}", code, state);
        final Map<String, Object> userInfo = wxxResponseSvc.authorizeCode(code);
        ModelAndView modelAndView;
        if (userInfo == null) {
            _log.info("返回获取微信用户信息失败页面");
            modelAndView = new ModelAndView("GetWxUserInfoFail");
        } else {
            // 给用户信息map添加state
            userInfo.put("state", state);
            _log.info("给用户信息map添加签名(重定向登录页面需要签名)");
            SignUtils.sign1(userInfo, wxLoginCallbackSignKey);
            _log.info("跳转用户登录页面: {}:{}", wxLoginCallbackMethodType, wxLoginCallbackUrl);
            modelAndView = new ModelAndView("ForwardUserLogin");
            modelAndView.addObject("methodType", wxLoginCallbackMethodType);
            modelAndView.addObject("forwardUrl", wxLoginCallbackUrl);
            modelAndView.addObject("userInfo", userInfo);
        }
        return modelAndView;
    }

}
