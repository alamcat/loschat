package service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.RegisterPackage;
import bean.User;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import service.UserService;
/**
 * We use this class to implements UserService to complete some methods that can be used by server controller.
 * These methods can handle some transactions
 * @author Wenbin Hu
 */
public class UserServiceImpl implements UserService {
	UserDao ud = new UserDaoImpl();
	
	/**
	 * we use this method to save user information 
	 * @param u user information
	 * @return weather database has saved information successfully
	 */
	@Override	
	public boolean save(RegisterPackage u) {
		return ud.save(u);

	}

	/**
	 * we use this method to check weather user can login
	 * @param name user account
	 * @param passwrod user's password
	 * @return weather user can login successfully
	 */
	@Override
	public boolean login(String name, String passwrod) {
		User user = ud.getUserByAccount(name);
		if(user == null) {
			return false;
		}	
		
		//we need trim to clear the space of the  user.getPassword(),it's length is 255
		return user.getPassword().trim().equals(passwrod);

	}

	/**
	 * we use this method to get all clients in a group
	 * @param group_id the id of the group
	 * @return all clients in this group
	 */
	@Override
	public Set<String> getGroupClientsByGroupId(int groupId) {
		Set<Integer> set = ud.getGroupClientsByGroupId(groupId);
		Set<String> res = new HashSet<String>();
		for(int id : set) {
			res.add(this.getAccountByUserId(id));
		}
		return res;
	}

	/**
	 * we use this method to get the user class by the account 
	 * @param account the account of the user
	 * @return the user class by the account 
	 */
	@Override
	public User getUserByAccount(String account) {
		return ud.getUserByAccount(account);
	}
	
	/**
	 * we use this method to get the group id and account by the user id
	 * @param userId the id of the user
	 * @return group name and group id
	 */
	@Override
	public Map<Integer,String> getGroupIdByUserId(Integer userId) {		
		return ud.getGroupIdByUserId(userId);
	}

	/**
	 * we use this method to get the user id by the user account
	 * @param userAccount the account of the user
	 * @return the id of the user
	 */
	@Override
	public Integer getUserIdByAccount(String userAccount) {
		return ud.getUserIdByAccount(userAccount);
	}

	/**
	 * we use this method to create a group
	 * @param groupName the name of the group
	 * @param accounts the accounts in the group
	 * @return the id of the group
	 */
	@Override
	public Integer createGroup(String groupName, List<String> accounts) {
		boolean flag = ud.createGroup(groupName);
		int groupId;
		
		if(flag) {
			groupId = ud.getGroupIdByName(groupName);
		}else {
			return null;
		}
		
		List<Integer> list = new ArrayList<Integer>();
		
		for(int i=0;i<accounts.size();i++) {
			list.add(ud.getUserIdByAccount(accounts.get(i)));
		}
		
		boolean flag2 = ud.createClientsOfGroup(groupId, list);
		
		if(flag2) {
			return groupId;
		}else {
			return null;
		}
	}

	/**
	 * we use this method to get the account of the user
	 * @param userId the id of the user
	 * @return the account of the user
	 */
	@Override
	public String getAccountByUserId(int userId) {
		
		return ud.getAccountByUserId(userId);
		
	}

	/**
	 * we use this method to delete group 
	 * @param groupId the id of the group
	 * @return weather the group can be deleted successfully
	 */
	@Override
	public boolean dropGroup(int groupId) {
		
		boolean flag = ud.dropGroupRelation(groupId);
		
		if(flag) {
			flag = ud.dropGroupId(groupId);
		}

		return flag;
	}

	@Override
	public boolean changePassword(String name, String password, String newPassword) {
		return ud.changePassword(name,password,newPassword);
	}
	
}
