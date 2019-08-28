package rebue.wxx.mo;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
商户信息，也就是微信支付账户信息

数据库表: WXX_MCH

@mbg.generated 自动生成的注释，如需修改本注释，请删除本行
*/
@JsonInclude(Include.NON_NULL)
public class WxxMchMo implements Serializable {
    /**
    商户号(MCH_ID)
    
    数据库字段: WXX_MCH.ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private String id;

    /**
    商户名称
    
    数据库字段: WXX_MCH.NAME
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private String name;

    /**
    API密钥，签名用的key，在商户平台设置（微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
    
    数据库字段: WXX_MCH.API_KEY
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    private String apiKey;

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    private static final long serialVersionUID = 1L;

    /**
    商户号(MCH_ID)
    
    数据库字段: WXX_MCH.ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public String getId() {
        return id;
    }

    /**
    商户号(MCH_ID)
    
    数据库字段: WXX_MCH.ID
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setId(String id) {
        this.id = id;
    }

    /**
    商户名称
    
    数据库字段: WXX_MCH.NAME
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public String getName() {
        return name;
    }

    /**
    商户名称
    
    数据库字段: WXX_MCH.NAME
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
    API密钥，签名用的key，在商户平台设置（微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
    
    数据库字段: WXX_MCH.API_KEY
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public String getApiKey() {
        return apiKey;
    }

    /**
    API密钥，签名用的key，在商户平台设置（微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
    
    数据库字段: WXX_MCH.API_KEY
    
    @mbg.generated 自动生成，如需修改，请删除本行
    */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", apiKey=").append(apiKey);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
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
        WxxMchMo other = (WxxMchMo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
        ;
    }

    /**
    @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
}