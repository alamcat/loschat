package dao;


import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.RegisterPackage;
import bean.User;
/**
 * We use this interface to define some methods that can be used by service.
 * These methods can handle some atomic operations
 * @author Wenbin Hu
 */
public interface UserDao {
	
	/**
	 * we use this method to get the user class by the account 
	 * @param account the account of the user
	 * @return the user class by the account 
	 */
	User getUserByAccount(String account);
	
	/**
	 * we use this method to save user information 
	 * @param u user information
	 * @return weather database has saved information successfully
	 */
	boolean save(RegisterPackage u);

	/**
	 * we use this method to get all clients in a group
	 * @param group_id the id of the group
	 * @return all clients in this group
	 */
	Set<Integer> getGroupClientsByGroupId(int groupId);

	/**
	 * we use this method to get the group id and account by the user id
	 * @param userId the id of the user
	 * @return group name and group id
	 */
	Map<Integer, String> getGroupIdByUserId(Integer userId);
	
	/**
	 * we use this method to get the user id by the user account
	 * @param userAccount the account of the user
	 * @return the id of the user
	 */
	Integer getUserIdByAccount(String userAccount);

	/**
	 * we use this method to create a group
	 * @param groupName the name of the group
	 * @param accounts the accounts in the group
	 * @return the id of the group
	 */
	Boolean createGroup(String groupName);
	
	/**
	 * we use this method to get group id by group name
	 * @param groupName the name of the group
	 * @return the id of the group
	 */
	Integer getGroupIdByName(String groupName);
	
	/**
	 * we use this method to create relation between group id and user id
	 * @param groupId the id of the group
	 * @param list the list of the user id
	 * @return weather we have created relation between group id and user id successfully
	 */
	Boolean createClientsOfGroup(int groupId, List<Integer> list);

	/**
	 * we use this method to get the account of the user
	 * @param userId the id of the user
	 * @return the account of the user
	 */
	String getAccountByUserId(int userId);

	/**
	 * we use this method to delete relation between group id and user id
	 * @param groupId the id of the group
	 * @return weather we have deleted relation between group id and user id successfully
	 */
	boolean dropGroupRelation(int groupId);

	/**
	 * we use this method to delete group
	 * @param groupId the id of the group
	 * @return weather we have deleted group successfully
	 */
	boolean dropGroupId(int groupId);

	boolean changePassword(String name, String password, String newPassword);
}
