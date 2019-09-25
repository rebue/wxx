package rebue.wxx.ctrl;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
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

import lombok.extern.slf4j.Slf4j;
import rebue.wheel.XmlUtils;
import rebue.wheel.turing.SignUtils;
import rebue.wxx.jo.WxxAppJo;
import rebue.wxx.svc.WxxAppSvc;
import rebue.wxx.svc.WxxResponseSvc;
import rebue.wxx.vo.WxAuthorizeVo;

/**
 * 响应微信发过来请求的控制器
 */
@RestController
@RefreshScope
@Slf4j
public class WxxResponseCtrl {
    @Resource
    private WxxResponseSvc svc;
    @Resource
    private WxxAppSvc      appSvc;

    /**
     * 微信登录回调的地址
     */
    @Value("${wxx.loginCallback.url}")
    private String         wxLoginCallbackUrl;
    /**
     * 微信登录回调的地址方法类型(GET/POST/PUT/DELETE....)
     */
    @Value("${wxx.loginCallback.methodType:GET}")
    private String         wxLoginCallbackMethodType;
    /**
     * 微信登录回调的签名key
     */
    @Value("${wxx.loginCallback.signKey}")
    private String         wxLoginCallbackSignKey;

    /**
     * 提供给微信验证本服务器身份的接口
     * 微信公众号绑定网站的域名时，会向此url发送GET请求进行校验，
     * 而要注意，微信通过同样的url，发出POST请求时却是推送消息过来，
     * 所以GET和POST要区分对待，本方法是处理GET的，下一个方法是处理POST的
     */
    @GetMapping(value = "/wxx/response/{appId}", produces = MediaType.TEXT_PLAIN_VALUE)
    String authorize(@PathVariable(name = "appId") final String appId, final WxAuthorizeVo vo) {
        log.info("接收到验证本服务器身份的请求: appId-{} vo-{}", appId, vo);
        return svc.authorize(appId, vo);
    }

    /**
     * 接收微信服务器发来的消息
     * 
     * @throws SAXException
     */
    @PostMapping(value = "/wxx/response/{appId}", produces = MediaType.TEXT_PLAIN_VALUE)
    String receiveMsg(@PathVariable("appId") final String appId, final HttpServletRequest req) throws IOException, DocumentException, SAXException {
        final String xml = XmlUtils.getXmlFromRequest(req);
        log.info("接收微信服务器发来的消息: appId-{} xml-{}", appId, xml);
        return svc.handleMsg(appId, XmlUtils.xmlToMap(xml));
    }

    /**
     * 网页授权需要验证此链接
     */
    @GetMapping(value = "/MP_verify_{id}.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    String mpVerify(@PathVariable("id") final String id) {
        log.info("mp_verify_{}", id);
        return id;
    }

    /**
     * 微信授权回调接口，重定向登录页面
     * 
     * @param code
     *            获取到授权的code
     */
    @GetMapping("/wxx/response/authorizecode")
    ModelAndView authorizeCode(@RequestParam("appid") final String appId, @RequestParam("code") final String code,
            @RequestParam(value = "state", required = false) final String state) throws IOException {
        log.info("接收到微信授权回调: appId-{}, code-{}, state-{}", appId, code, state);

        log.info("在获取网页授权之前，先通过appId获取App信息: appId-{}", appId);
        final WxxAppJo appJo = appSvc.getJoById(appId);
        if (appJo == null) {
            final String msg = "未找到App信息";
            log.error(msg + ": appId-{}", appId);
            throw new RuntimeException(msg);
        }
        log.info("获取到App的信息: appJo-{}", appJo);

        final Map<String, Object> userInfo = svc.authorizeCode(appJo, code);
        ModelAndView modelAndView;
        if (userInfo == null) {
            log.info("返回获取微信用户信息失败页面");
            modelAndView = new ModelAndView("GetWxUserInfoFail");
        } else {
            // 给用户信息map添加state
            userInfo.put("state", state);
            if (!StringUtils.isBlank(appJo.getLoginCallbackSignkey())) {
                log.info("给用户信息map添加签名(重定向登录页面需要签名)");
                SignUtils.sign1(userInfo, appJo.getLoginCallbackSignkey());
            }
            log.info("跳转用户登录页面: {}:{}", appJo.getLoginCallbackMethodType(), appJo.getLoginCallbackUrl());
            modelAndView = new ModelAndView("ForwardUserLogin");
            modelAndView.addObject("methodType", appJo.getLoginCallbackMethodType());
            modelAndView.addObject("forwardUrl", appJo.getLoginCallbackUrl());
            modelAndView.addObject("userInfo", userInfo);
        }
        return modelAndView;
    }

}
