package bean;

import java.io.Serializable;

/**
 * The messenger object to be sent.
 * @author Peng Jiang
 * @version 2020-03-07
 */
public class Messenger implements Serializable {

    private static final long serialVersionUID = 3156661581057222262L;
    private Boolean isGroup;
    private String msgDetail;
    private String msgFrom;
    private String msgTo;

    /**
     * The constructor with parameters of Messenger.
     * @param isGroup if this message is sent to a group
     * @param msgDetail the content of this message
     * @param msgFrom the user who sent this message
     * @param msgTo the user/group to be sent
     */
    public Messenger(Boolean isGroup, String msgDetail, String msgFrom, String msgTo) {
        this.isGroup = isGroup;
        this.msgDetail = msgDetail;
        this.msgFrom = msgFrom;
        this.msgTo = msgTo;
    }

    /**
     * Getter for the group.
     * @return the result of group
     * true if this message will be sent to a group
     * or not if it is sent to a user
     */
    public Boolean getGroup() {
        return isGroup;
    }

    /**
     * Setter for the group.
     * @param group if it will be sent to a group
     */
    public void setGroup(Boolean group) {
        isGroup = group;
    }

    /**
     * Getter for the msgDetail
     * @return the message detail
     */
    public String getMsgDetail() {
        return msgDetail;
    }

    /**
     * Setter for the msgDetail.
     * @param msgDetail the message detail to be sent
     */
    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    /**
     * Getter for the msgFrom.
     * @return the user's account
     */
    public String getMsgFrom() {
        return msgFrom;
    }

    /**
     * Setter for the msgFrom.
     * @param msgFrom the user who sent the message
     */
    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    /**
     * Getter for the msgTo.
     * @return the user's account or group id.
     */
    public String getMsgTo() {
        return msgTo;
    }

    /**
     * Setter for the msgTo.
     * @param msgTo the user who receives the message
     */
    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }
}
