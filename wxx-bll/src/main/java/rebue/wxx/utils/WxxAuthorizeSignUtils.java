package rebue.wxx.utils;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rebue.wxx.vo.WxAuthorizeVo;

/**
 * 微信支付签名的相关应用
 */
public class WxxAuthorizeSignUtils {
    private final static Logger _log = LoggerFactory.getLogger(WxxAuthorizeSignUtils.class);

    /**
     * 验证微信确认本服务器身份接口的参数的签名是否正确
     * 
     * @return false为校验没有通过，不排除有人在试图模仿微信官方服务器发来信息
     */
    public static boolean verify(WxAuthorizeVo vo, String wxToken) {
        _log.info("校验微信确认本服务器身份接口的参数是否正确");
        String[] array = { wxToken, vo.getTimestamp(), vo.getNonce() };
        Arrays.sort(array);
        String tempSignature = DigestUtils.sha1Hex(array[0] + array[1] + array[2]);
        return tempSignature.equals(vo.getSignature());
    }
}
