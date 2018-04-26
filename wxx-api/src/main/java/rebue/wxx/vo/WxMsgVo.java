package rebue.wxx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class WxMsgVo {
    @XmlElement(name = "ToUserName")
    private String  toUserName;
    @XmlElement(name = "FromUserName")
    private String  fromUserName;
    @XmlElement(name = "CreateTime")
    private Integer createTime;
    @XmlElement(name = "MsgType")
    private String  msgType;
    @XmlElement(name = "Content")
    private String  content;
    @XmlElement(name = "MsgId")
    private Long    msgId;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "MsgVo [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime=" + createTime
                + ", MsgType=" + msgType + ", content=" + content + ", msgId=" + msgId + "]";
    }

}
