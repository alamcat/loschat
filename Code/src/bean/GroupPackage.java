package bean;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used to generate a group talk.
 * @author Peng Jiang
 * @version 2020-03-08
 */
public class GroupPackage implements Serializable {

    private static final long serialVersionUID = 7113879315875487361L;
    private List<String> memberList;
    private String sponsor;
    private String groupName;
    private String groupID;

    /**
     * Constructor of the group talk class.
     * @param memberList the member list of this group
     * @param sponsor the sponsor of this group
     * @param groupName the group name of this group
     */
    public GroupPackage(List<String> memberList, String sponsor, String groupName) {
        this.memberList = memberList;
        this.sponsor = sponsor;
        this.groupName = groupName;
    }

    /**
     * Getter for the memberList.
     * @return the value of memberList
     */
    public List<String> getMemberList() {
        return memberList;
    }

    /**
     * Getter for the sponsor.
     * @return the value of sponsor
     */
    public String getSponsor() {
        return sponsor;
    }

    /**
     * Getter for the groupName.
     * @return the value of groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Getter for the groupID.
     * @return the value of groupID
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     * Setter for the memberList.
     * @param memberList the memberList to be set
     */
    public void setMemberList(List<String> memberList) {
        this.memberList = memberList;
    }

    /**
     * Setter for the sponsor.
     * @param sponsor the sponsor to be set
     */
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * Setter for the groupName.
     * @param groupName the groupName to be set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Setter for the groupID.
     * @param groupID the groupID to be set
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
