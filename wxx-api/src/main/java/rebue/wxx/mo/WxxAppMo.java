package rebue.wxx.mo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;

/**
 * App信息
 *
 * 数据库表: WXX_APP
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@JsonInclude(Include.NON_NULL)
public class WxxAppMo implements Serializable {

    /**
     *    APP_ID
     *
     *    数据库字段: WXX_APP.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String id;

    /**
     *    APP名称
     *
     *    数据库字段: WXX_APP.NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String name;

    /**
     *    商户号
     *
     *    数据库字段: WXX_APP.MCH_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String mchId;

    /**
     *    APP_SECRET
     *
     *    数据库字段: WXX_APP.APP_SECRET
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String appSecret;

    /**
     *    TOKEN
     *
     *    数据库字段: WXX_APP.TOKEN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String token;

    /**
     *    ENCODEING_AES_KEY
     *
     *    数据库字段: WXX_APP.ENCODEING_AES_KEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String encodeingAesKey;

    /**
     *    用户关注后自动回复的文本
     *
     *    数据库字段: WXX_APP.SUBSCRIBE_AUTO_REPLY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String subscribeAutoReply;

    /**
     *    自定义菜单
     *
     *    数据库字段: WXX_APP.MENU
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String menu;

    /**
     *    登录回调链接
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_URL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String loginCallbackUrl;

    /**
     *    登录回调方法类型
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_METHOD_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String loginCallbackMethodType;

    /**
     *    登录回调签名密钥
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_SIGNKEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String loginCallbackSignkey;

    /**
     *    微信支付完成通知的URL
     *
     *    数据库字段: WXX_APP.WXPAY_NOTIFY_URL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private String wxpayNotifyUrl;

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
     *    APP_ID
     *
     *    数据库字段: WXX_APP.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getId() {
        return id;
    }

    /**
     *    APP_ID
     *
     *    数据库字段: WXX_APP.ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *    APP名称
     *
     *    数据库字段: WXX_APP.NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getName() {
        return name;
    }

    /**
     *    APP名称
     *
     *    数据库字段: WXX_APP.NAME
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *    商户号
     *
     *    数据库字段: WXX_APP.MCH_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getMchId() {
        return mchId;
    }

    /**
     *    商户号
     *
     *    数据库字段: WXX_APP.MCH_ID
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    /**
     *    APP_SECRET
     *
     *    数据库字段: WXX_APP.APP_SECRET
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     *    APP_SECRET
     *
     *    数据库字段: WXX_APP.APP_SECRET
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     *    TOKEN
     *
     *    数据库字段: WXX_APP.TOKEN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getToken() {
        return token;
    }

    /**
     *    TOKEN
     *
     *    数据库字段: WXX_APP.TOKEN
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *    ENCODEING_AES_KEY
     *
     *    数据库字段: WXX_APP.ENCODEING_AES_KEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getEncodeingAesKey() {
        return encodeingAesKey;
    }

    /**
     *    ENCODEING_AES_KEY
     *
     *    数据库字段: WXX_APP.ENCODEING_AES_KEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setEncodeingAesKey(String encodeingAesKey) {
        this.encodeingAesKey = encodeingAesKey;
    }

    /**
     *    用户关注后自动回复的文本
     *
     *    数据库字段: WXX_APP.SUBSCRIBE_AUTO_REPLY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getSubscribeAutoReply() {
        return subscribeAutoReply;
    }

    /**
     *    用户关注后自动回复的文本
     *
     *    数据库字段: WXX_APP.SUBSCRIBE_AUTO_REPLY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setSubscribeAutoReply(String subscribeAutoReply) {
        this.subscribeAutoReply = subscribeAutoReply;
    }

    /**
     *    自定义菜单
     *
     *    数据库字段: WXX_APP.MENU
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getMenu() {
        return menu;
    }

    /**
     *    自定义菜单
     *
     *    数据库字段: WXX_APP.MENU
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    /**
     *    登录回调链接
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_URL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getLoginCallbackUrl() {
        return loginCallbackUrl;
    }

    /**
     *    登录回调链接
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_URL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setLoginCallbackUrl(String loginCallbackUrl) {
        this.loginCallbackUrl = loginCallbackUrl;
    }

    /**
     *    登录回调方法类型
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_METHOD_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getLoginCallbackMethodType() {
        return loginCallbackMethodType;
    }

    /**
     *    登录回调方法类型
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_METHOD_TYPE
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setLoginCallbackMethodType(String loginCallbackMethodType) {
        this.loginCallbackMethodType = loginCallbackMethodType;
    }

    /**
     *    登录回调签名密钥
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_SIGNKEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getLoginCallbackSignkey() {
        return loginCallbackSignkey;
    }

    /**
     *    登录回调签名密钥
     *
     *    数据库字段: WXX_APP.LOGIN_CALLBACK_SIGNKEY
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setLoginCallbackSignkey(String loginCallbackSignkey) {
        this.loginCallbackSignkey = loginCallbackSignkey;
    }

    /**
     *    微信支付完成通知的URL
     *
     *    数据库字段: WXX_APP.WXPAY_NOTIFY_URL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public String getWxpayNotifyUrl() {
        return wxpayNotifyUrl;
    }

    /**
     *    微信支付完成通知的URL
     *
     *    数据库字段: WXX_APP.WXPAY_NOTIFY_URL
     *
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    public void setWxpayNotifyUrl(String wxpayNotifyUrl) {
        this.wxpayNotifyUrl = wxpayNotifyUrl;
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", mchId=").append(mchId);
        sb.append(", appSecret=").append(appSecret);
        sb.append(", token=").append(token);
        sb.append(", encodeingAesKey=").append(encodeingAesKey);
        sb.append(", subscribeAutoReply=").append(subscribeAutoReply);
        sb.append(", menu=").append(menu);
        sb.append(", loginCallbackUrl=").append(loginCallbackUrl);
        sb.append(", loginCallbackMethodType=").append(loginCallbackMethodType);
        sb.append(", loginCallbackSignkey=").append(loginCallbackSignkey);
        sb.append(", wxpayNotifyUrl=").append(wxpayNotifyUrl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WxxAppMo other = (WxxAppMo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()));
    }

    /**
     *    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
}
