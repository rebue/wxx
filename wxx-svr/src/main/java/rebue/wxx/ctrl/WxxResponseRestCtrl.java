package rebue.wxx.ctrl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rebue.wheel.XmlUtils;
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

    /**
     * 提供给微信验证本服务器身份的接口
     * 微信公众号绑定网站的域名时，会向此url发送GET请求进行校验，
     * 而要注意，微信通过同样的url，发出POST请求时却是推送消息过来，
     * 所以GET和POST要区分对待，本方法是处理GET的，下一个方法是处理POST的
     */
    @GetMapping("/wxx/response")
    String authorize(WxAuthorizeVo vo) {
        _log.info("received authorize params: {}", vo);
        return wxxResponseSvc.authorize(vo);
    }

    /**
     * 接收微信服务器发来的消息
     */
    @PostMapping("/wxx/response")
    String receiveMsg(HttpServletRequest req) throws IOException, DocumentException {
        _log.info("received msg");
        return wxxResponseSvc.handleMsg(XmlUtils.xmlToMap(req.getInputStream()));
    }

    /**
     * 网页授权需要验证此链接
     */
    @GetMapping("/wxx/MP_verify_{id}.txt")
    String mpVerify(@PathVariable("id") String id) {
        _log.info("mp_verify_{}", id);
        return id;
    }

    /**
     * 网页授权第一步：用户同意授权，获取到code
     * 网页授权第二步：通过code换取网页授权的web_access_token
     * 网页授权第三步：刷新web_access_token缓存时限
     * 网页授权第四步：获取用户信息
     * 网页授权第五步：回调登录页面
     * 
     * @param code
     *            获取到授权的code
     */
    @GetMapping(value = "/wxx/response/authorizecode", produces = MediaType.TEXT_HTML_VALUE)
    String authorizeCode(@RequestParam("code") String code) throws IOException {
        _log.info("received authorize code params: {}", code);
        String html = wxxResponseSvc.authorizeCode(code);
        _log.info("返回网站登录页面:{}", html);
        return html;
    }

}
