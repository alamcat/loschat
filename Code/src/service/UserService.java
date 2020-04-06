package service;


import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.RegisterPackage;
import bean.User;

/**
 * We use this interface to define some methods that can be used by server controller.
 * These methods can handle some transactions
 * @author Wenbin Hu
 */
public interface UserService {
	/**
	 * we use this method to save user information 
	 * @param u user information
	 * @return weather database has saved information successfully
	 */
	boolean save(RegisterPackage u);
	
	/**
	 * we use this method to check weather user can login
	 * @param name user account
	 * @param passwrod user's password
	 * @return weather user can login successfully
	 */
	boolean login(String name, String passwrod);
	
	/**
	 * we use this method to get all clients in a group
	 * @param group_id the id of the group
	 * @return all clients in this group
	 */
	Set<String> getGroupClientsByGroupId(int group_id);

	/**
	 * we use this method to get the user class by the account 
	 * @param account the account of the user
	 * @return the user class by the account 
	 */
	User getUserByAccount(String account);
	
	/**
	 * we use this method to get the group id and account by the user id
	 * @param userId the id of the user
	 * @return group name and group id
	 */
	Map<Integer,String> getGroupIdByUserId(Integer userId);
	
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
	Integer createGroup(String groupName, List<String> accounts);
	
	/**
	 * we use this method to get the account of the user
	 * @param userId the id of the user
	 * @return the account of the user
	 */
	String getAccountByUserId(int userId);
	
	/**
	 * we use this method to delete group 
	 * @param groupId the id of the group
	 * @return weather the group can be deleted successfully
	 */
	boolean dropGroup(int groupId);

	boolean changePassword(String name, String password, String newPassword);
}
