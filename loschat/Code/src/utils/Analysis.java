package utils;

import bean.GroupPackage;
import bean.Messenger;

import bean.User;

import client.ClientAux;
import gui.ShowMain;
import gui.ViewAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class analyses all the packaged from the server.
 * @author Peng Jiang
 * @version 2020-03-16
 */
public class Analysis extends ClientAux {
    private static int age;
    private static String gender;
    private static String account = "";
    private static String currentChat = "";
    private static Boolean pwdResult = null;
    private static String groupCurrentChat = "";
    private static List<String> onlineList = new ArrayList<>();
    private static List<String> chattedList = new ArrayList<>();
    private static Map<String, String> groupOnlineList = new HashMap<>();
    private static Map<String, List<List<String>>> msgList = new HashMap<>();
    private static Map<String, List<List<String>>> groupMsgList = new HashMap<>();
    private static Map<String, List<List<String>>> mapChattedList = new HashMap<>();

    /**
     * This method analyses the login package from the server.
     * @param objResult the object to be converted
     * @return true if information corrected or false if not
     */
    public static boolean resultPackage(Object objResult) {
        String status;
        while (true) {
            if (objResult instanceof Boolean) {
                if (!((Boolean)objResult)) {
                    println("Invalid account or password!");
                }
                return ((Boolean)objResult);
            } else if (objResult instanceof String) {
                status = (String)objResult;
                if (status.equals("AlreadyLogin") || status.equals("AlreadyRegister")) {
                    println("Ops! " + status + "!");
                    return false;
                }
            }
        }
    }

    /**
     * This method analyses the message package from the server.
     * @param msg the message to be analysed
     */
    public static void msgPackage(Messenger msg) {
        Boolean isGroup = msg.getGroup();
        String msgFrom = msg.getMsgFrom();
        String msgTo = msg.getMsgTo();
        String msgDetail = msg.getMsgDetail();
        if (isGroup) {
            // Display on the group
            printMsg(msgTo, msgFrom, msgDetail, groupMsgList, currentChat, onlineList, true);
        } else {
            // Display on the friend
            printMsg(msgFrom, "",msgDetail, msgList, currentChat, chattedList, false);
        }
        ClientHistoryWriter.writeHistory(msg, false);
    }

    /**
     * An auxiliary method of the msgPackage. Message will be
     * classified in this method. Group or personal message.
     * Message will be stored into an List which will be stored
     * in an Map with its account(or group id) as the key.
     * @param listSave the key of the map
     * @param msgDetail message detail from a package
     * @param list the message list
     * @param current whether the user currently talking
     */
    public synchronized static void printMsg(String listSave, String groupFrom, String msgDetail,
                                Map<String, List<List<String>>> list, String current, List<String> onlineList, boolean isGroup) {
        String receivedTime = PathUtils.getReceivedData();

        if (list.containsKey(listSave)) {
            if (!isGroup) {
                if (listSave.equals(current)) {
                    ShowMain.othersMsg(current, msgDetail);
                    ShowMain.renewMsg(current, current + ": " +
                            msgDetail, receivedTime);
                } else {
                    list.get(listSave).get(0).add(receivedTime);
                    list.get(listSave).get(1).add(msgDetail);
                    ShowMain.renewMsg(listSave, listSave + ": " +
                            msgDetail, receivedTime);
                }
            } else {
                if (!groupFrom.equals(account)) {
                    if (listSave.equals(current)) {
                        ShowMain.othersMsg(groupFrom, msgDetail);
                        ShowMain.renewMsg(groupFrom, groupFrom + ": " +
                                msgDetail, receivedTime);
                    } else {
                        list.get(listSave).get(0).add(receivedTime);
                        list.get(listSave).get(1).add(msgDetail);
                        ShowMain.renewMsg(groupFrom, groupFrom + ": " +
                                msgDetail, receivedTime);
                    }
                }
            }

        } else {
            if (!onlineList.contains(listSave) && !isGroup) {
                addNewPal(listSave);
                list.get(listSave).get(0).add(receivedTime);
                list.get(listSave).get(1).add(msgDetail);
                ShowMain.renewMsg(listSave, listSave + ": " +
                        msgDetail, receivedTime);
            } else if (!onlineList.contains(listSave) && isGroup) {
                list.get(listSave).get(0).add(receivedTime);
                list.get(listSave).get(1).add(msgDetail);

                ShowMain.renewMsg(groupFrom, groupFrom + ": " +
                        msgDetail, receivedTime);
            }
        }
    }

    /**
     * This method adds the new user into the chatted list.
     * @param account the account to be added
     */
    public static void addChattedList(String account) {
        if (!chattedList.contains(account)) {
            chattedList.add(account);
            List<List<String>> reList = new ArrayList<>();
            reList.add(new ArrayList<>());
            reList.add(new ArrayList<>());
            reList.add(new ArrayList<>());
            msgList.put(account, reList);
            mapChattedList.put(account, msgList.get(account));
        }
    }

    /**
     * Setter for the chattedList.
     * @param list new chattedList to be set
     */
    public static void setChattedList(List<String> list) {
        chattedList = list;
    }

    /**
     * This method add a new chatted user into current
     * chattedList.
     * @param account a new user to be added
     */
    public static void addNewPal(String account) {
        addChattedList(account);
        ShowMain.createListAux(mapChattedList);
    }

    /**
     * This method sets the online list of the platform.
     * @param list online list from the server
     */
    public static void setOnlineList(List<String> list) {
        onlineList = new ArrayList<>();
        for (String onlineAccount : list) {
            if (onlineAccount.equals(account)){
                onlineList.add(0, onlineAccount);
            } else {
                onlineList.add(onlineAccount);
            }
        }
        ViewAll.createOnlineList(onlineList);
    }

    /**
     * A getter of the groupOnline list.
     * @return the groupOnline list
     */
    public static Map<String, List<List<String>>> getGroupMsgList() {
        return groupMsgList;
    }

    /**
     * A setter of the groupOnline list.
     * @param map the groupOnlineList from the server
     */
    public static void setGroupMsgList(Map<Integer, String> map) {
        map.forEach((id, name) -> {
            groupOnlineList.put(String.valueOf(id), name);
            List<List<String>> reList = new ArrayList<>();
            reList.add(new ArrayList<>());
            reList.add(new ArrayList<>());
            reList.add(new ArrayList<>());
            ArrayList<String> groupName = new ArrayList<>();
            groupName.add(name);
            reList.add(2, groupName);
            groupMsgList.put(String.valueOf(id), reList);
        });
    }

    public static void setGroupOnlineList(String id, String group) {
        groupOnlineList.put(id, group);
    }

    /**
     * This method analyses the result of group creation
     * @param groupPackage the result from the server
     */
    public static void groupResult(GroupPackage groupPackage) {
        if (groupPackage.getGroupID() != null &&
                groupPackage.getMemberList() != null) {
            println("Create group successfully!");
            print("Details of your new group:\nGroup name: " +
                    groupPackage.getGroupName() + "\nID: " + groupPackage.getGroupID() +
                    "\nSponsor: " + groupPackage.getSponsor() + "\nMembers: ");
            groupPackage.getMemberList().forEach(e->print(e + " "));

            groupOnlineList.put(groupPackage.getGroupID(), groupPackage.getGroupName());
            List<List<String>> reList = new ArrayList<>();
            reList.add(new ArrayList<>());
            reList.add(new ArrayList<>());
            reList.add(new ArrayList<>());
            ArrayList<String> groupName = new ArrayList<>();
            groupName.add(groupPackage.getGroupName());
            reList.add(2, groupName);
            groupMsgList.put(groupPackage.getGroupID(), reList);
            ShowMain.createGroupList();

        } else if (groupPackage.getGroupID() != null &&
                groupPackage.getMemberList() == null &&
                groupPackage.getGroupName() == null) {
            println("Delete group successfully.");
        } else {
            println("Sorry, we could not create the group now.");
        }
    }

    /**
     * Getter for the msgList.
     * @param msgFrom the key of the msgList.
     * @return the message list of a user
     */
    public synchronized static List<List<String>> getMsgList(String msgFrom) {
        List<List<String>> temp = new ArrayList<>();
        for (List list : msgList.get(msgFrom)) {
            temp.add(list);
        }
        List<List<String>> reList = new ArrayList<>();
        reList.add(new ArrayList<>());
        reList.add(new ArrayList<>());
        reList.add(new ArrayList<>());
        msgList.replace(msgFrom, reList);
        mapChattedList.put(msgFrom, msgList.get(msgFrom));
        return temp;
    }

    public static List<List<String>> getGroupMsgListList(String msgTo) {
        List<List<String>> temp = new ArrayList<>();
        for (List list : groupMsgList.get(msgTo)) {
            temp.add(list);
        }
        List<List<String>> reList = new ArrayList<>();
        reList.add(new ArrayList<>());
        reList.add(new ArrayList<>());
        reList.add(new ArrayList<>());

        groupMsgList.put(msgTo, reList);
        return temp;
    }

    public static List<String> getOnlineList() {
        return onlineList;
    }

    public static List<String> getGroupOnlineList() {
        List<String> keyList = new ArrayList<>(groupOnlineList.keySet());
        return keyList;
    }
    /**
     * A setter set the personal current chat.
     * @param current the currently talking user index
     * @return the currently talking user
     */
    public static String setCurrentChat(int current) {
        currentChat = chattedList.get(current);
        return currentChat;
    }

    /**
     * A setter for the groupCurrentChat.
     * @param current the currently talking group
     */
    public static void setGroupCurrentChat (String current) {
        currentChat = current;
    }

    /**
     * Setter for the current account of client.
     * @param accountName the account of client
     */
    public static void setAccount(String accountName) {
        account = accountName;
    }

    /**
     * This method analyses the User package from server,
     * and get the information of user who using this client
     * currently.
     * @param user
     */
    public static void userInformation(User user) {
        age = user.getAge();
        gender = user.getGender();
    }

    public static void setPwdResult(User result) {
        pwdResult = result.getPassword().equals
                (result.getNewPassword());
    }

    public static String findGroupName(String id) {
        return groupOnlineList.get(id);
    }
    public static Boolean getPwdResult() {
        return pwdResult;
    }

    public static int getAge() {
        return age;
    }

    public static String getGender() {
        return gender;
    }

    public static Map<String, List<List<String>>> getMapChattedList() {
        return mapChattedList;
    }

}
