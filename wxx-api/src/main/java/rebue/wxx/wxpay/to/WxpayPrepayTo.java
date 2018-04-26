package rebue.wxx.wxpay.to;

/**
 * 微信支付-预支付的传输对象
 */
public class WxpayPrepayTo {
    /**
     * 用户ID
     */
    private Long   userId;
    /**
     * 微信的openid或unionid
     */
    private String wxId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 支付交易的标题
     */
    private String tradeTitle;
    /**
     * 支付交易的详情
     */
    private String tradeDetail;
    /**
     * 支付交易的金额(单位为“分”)
     */
    private Double tradeAmount;
    /**
     * 用户的IP地址
     */
    private String ip;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeTitle() {
        return tradeTitle;
    }

    public void setTradeTitle(String tradeTitle) {
        this.tradeTitle = tradeTitle;
    }

    public String getTradeDetail() {
        return tradeDetail;
    }

    public void setTradeDetail(String tradeDetail) {
        this.tradeDetail = tradeDetail;
    }

    public Double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "WxpayPrepayTo [userId=" + userId + ", wxId=" + wxId + ", orderId=" + orderId + ", tradeTitle=" + tradeTitle + ", tradeDetail=" + tradeDetail + ", tradeAmount="
                + tradeAmount + ", ip=" + ip + "]";
    }

}
