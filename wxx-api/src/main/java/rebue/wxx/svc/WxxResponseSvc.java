package rebue.wxx.svc;

import java.io.IOException;
import java.util.Map;

import rebue.wxx.vo.WxAuthorizeVo;

public interface WxxResponseSvc {

    /**
     * 提供给微信验证本服务器身份的接口
     */
    String authorize(WxAuthorizeVo vo);

    /**
     * 处理微信发来消息的接口
     */
    String handleMsg(Map<String, Object> requestMap);

    /**
     * 网页授权第一步：用户同意授权，获取到code
     * 网页授权第二步：通过code换取网页授权的web_access_token
     * 网页授权第三步：刷新web_access_token缓存时限
     * 网页授权第四步：获取用户信息
     * 网页授权第五步：回调登录页面(请求参数为添加签名后的用户信息map)
     * 
     * @param code
     *            获取到授权的code
     */

    String authorizeCode(String code) throws IOException;
}