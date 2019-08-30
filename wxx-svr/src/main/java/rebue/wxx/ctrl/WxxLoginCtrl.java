package rebue.wxx.ctrl;

import java.io.UnsupportedEncodingException;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 响应微信发过来请求的控制器
 */
@RestController
@RefreshScope
@Slf4j
public class WxxLoginCtrl {
    private static final String urlFormat = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%2$s" //
            + "&redirect_uri=%1$s%%2Fwxx-svr%%2Fwxx%%2Fresponse%%2Fauthorizecode%%3Fappid%%3D%2$s"//
            + "&response_type=code&scope=snsapi_userinfo&state=%3$s#wechat_redirect";

    /**
     * 获取微信登录的链接，也可不通过此请求，直接拼接出链接
     * 
     * @param redirectHost
     *            登录重连请求的host，如http://www.nnzbz.cn
     * @param appId
     *            微信公众号或小程序的appId
     * @param state
     *            有特别需求的可传入state给登录成功后跳转到自己的链接传递参数
     * @return 拼接的链接
     * @throws UnsupportedEncodingException
     */
    @GetMapping(value = "/wxx/login/url")
    String getLoginUrl(@RequestParam("redirecthost") final String redirectHost, @RequestParam("appid") final String appId,
            @RequestParam(value = "state", required = false) final String state) throws UnsupportedEncodingException {
        log.info("received get:/wxx/login: redirectHost-{} appId-{} state-{}", redirectHost, appId, state);
        final String url = String.format(urlFormat, redirectHost, appId, state);
        log.info("拼接出的微信登录链接为: {}", url);
        return url;
    }

}
