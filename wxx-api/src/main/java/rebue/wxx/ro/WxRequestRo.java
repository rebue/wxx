package rebue.wxx.ro;

/**
 * 向微信服务器发出请求的响应结果
 *
 */
public class WxRequestRo {
    /**
     * 错误编码
     */
    private Integer errcode;
    /**
     * 错误信息
     */
    private String  errmsg;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "WxRequestRo [errcode=" + errcode + ", errmsg=" + errmsg + "]";
    }

}
