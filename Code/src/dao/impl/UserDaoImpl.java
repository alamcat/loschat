package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.RegisterPackage;
import bean.User;
import dao.UserDao;
import utils.JDBCUtils;

public class UserDaoImpl implements UserDao {
	
	/**
	 * we use this method to save user information 
	 * @param u user information
	 * @return weather database has saved information successfully
	 */
	@Override
	public boolean save(RegisterPackage u) {
		Connection con = JDBCUtils.getConnection();
		String sql = "INSERT INTO ACCOUNT(USER_ACCOUNT,PASSWORD,GENDER,AGE) VALUES(?,?,?,?)";
		try {
			PreparedStatement pre = con.prepareStatement(sql);
			pre.setString(1, u.getAccount());
			pre.setString(2, u.getPassword());
			pre.setString(3, u.getGender());
			pre.setInt(4, u.getAge());
			int rowCount = pre.executeUpdate();
			if(rowCount != 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(con, null, null);
		}
		return true;
	}

	/**
	 * we use this method to get the user class by the account 
	 * @param account the account of the user
	 * @return the user class by the account 
	 */
	@Override
	public User getUserByAccount(String account) {
		List<User> list = new ArrayList<User>();
		Connection con = JDBCUtils.getConnection();
		String sql = "select * from account where user_account=?";
		PreparedStatement pre = null;
		ResultSet res = null;
		try {
			pre = con.prepareStatement(sql);
			pre.setString(1, account);
			res = pre.executeQuery();
			while(res.next()) {
				User u = new User();
				u.setName(res.getString("user_account"));
				u.setPassword(res.getString("password"));
				u.setGender(res.getString("gender"));
				u.setAge(res.getInt("age"));
				list.add(u);
			}
			if(list.size() == 0) {
				return null;
			}else {
				return list.get(0);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to quary the user");
		}finally {
			JDBCUtils.close(con, pre, res);
		}
	}

	/**
	 * we use this method to get the account of the user
	 * @param userId the id of the user
	 * @return the account of the user
	 */
	@Override
	public String getAccountByUserId(int id) {
		List<String> list = new ArrayList<String>();
		Connection con = JDBCUtils.getConnection();
		String sql = "select user_account from account where user_id=?";
		PreparedStatement pre = null;
		ResultSet res = null;
		try {
			pre = con.prepareStatement(sql);
			pre.setInt(1, id);
			res = pre.executeQuery();
			while(res.next()) {
				String s = res.getString("user_account");
				list.add(s);
			}
			return list.get(0);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to quary the user");
		}finally {
			JDBCUtils.close(con, pre, res);
		}
	}

	/**
	 * we use this method to get all clients in a group
	 * @param group_id the id of the group
	 * @return all clients in this group
	 */
	@Override
	public Set<Integer> getGroupClientsByGroupId(int groupId) {
		Set<Integer> set = new HashSet<Integer>();
		Connection con = JDBCUtils.getConnection();
		String sql = "select user_id from PersonToGroup where group_id=?";
		PreparedStatement pre = null;
		ResultSet res = null;
		try {
			pre = con.prepareStatement(sql);
			pre.setInt(1, groupId);
			res = pre.executeQuery();
			while(res.next()) {
				Integer userId = res.getInt("user_id");
				set.add(userId);
			}
			return set;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to quary the user");
		}finally {
			JDBCUtils.close(con, pre, res);
		}
	}

	/**
	 * we use this method to get the group id and account by the user id
	 * @param userId the id of the user
	 * @return group name and group id
	 */
	@Override
	public Map<Integer,String> getGroupIdByUserId(Integer userId) {
		Map<Integer,String> map = new HashMap<Integer,String>();
		Connection con = JDBCUtils.getConnection();
		String sql = "select group_id,group_name from PersonToGroup join chatgroup using(group_id) where user_id=?";
		PreparedStatement pre = null;
		ResultSet res = null;
		try {
			pre = con.prepareStatement(sql);
			pre.setInt(1, userId);
			res = pre.executeQuery();
			while(res.next()) {
				int groupId = res.getInt("group_id");
				String groupName = res.getString("group_name");
				map.put(groupId,groupName);
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to quary the user");
		}finally {
			JDBCUtils.close(con, pre, res);
		}
	}

	/**
	 * we use this method to get the user id by the user account
	 * @param userAccount the account of the user
	 * @return the id of the user
	 */
	@Override
	public Integer getUserIdByAccount(String userAccount) {
		List<Integer> list = new ArrayList<Integer>();
		Connection con = JDBCUtils.getConnection();
		String sql = "select user_id from account where user_account=?";
		PreparedStatement pre = null;
		ResultSet res = null;
		try {
			pre = con.prepareStatement(sql);
			pre.setString(1, userAccount);
			res = pre.executeQuery();
			while(res.next()) {
				Integer re = res.getInt("user_id");
				list.add(re);
			}
			
			if(list != null && list.size() > 0) {
				return list.get(0);
			}else {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to quary the user");
		}finally {
			JDBCUtils.close(con, pre, res);
		}
		
	}

	/**
	 * we use this method to create a group
	 * @param groupName the name of the group
	 * @param accounts the accounts in the group
	 * @return the id of the group
	 */
	@Override
	public Boolean createGroup(String groupName) {
		Connection con = JDBCUtils.getConnection();
		String sql = "INSERT INTO CHATGROUP(GROUP_NAME) VALUES(?)";
		try {
			PreparedStatement pre = con.prepareStatement(sql);
			pre.setString(1, groupName);
			int rowCount = pre.executeUpdate();
			if(rowCount != 1) {
				return false;
			}else {
				return true;
			}
									
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(con, null, null);
		}
		return true;
	}

	/**
	 * we use this method to get group id by group name
	 * @param groupName the name of the group
	 * @return the id of the group
	 */
	@Override
	public Integer getGroupIdByName(String groupName) {
		List<Integer> list = new ArrayList<Integer>();
		Connection con = JDBCUtils.getConnection();
		String sql = "select group_id from chatgroup where group_name=?";
		PreparedStatement pre = null;
		ResultSet res = null;
		try {
			pre = con.prepareStatement(sql);
			pre.setString(1, groupName);
			res = pre.executeQuery();
			while(res.next()) {
				Integer re = res.getInt("group_id");
				list.add(re);
			}
			
			if(list != null && list.size() > 0) {
				return list.get(list.size()-1);
			}else {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Fail to quary the user");
		}finally {
			JDBCUtils.close(con, pre, res);
		}
	}

	/**
	 * we use this method to create relation between group id and user id
	 * @param groupId the id of the group
	 * @param list the list of the user id
	 * @return weather we have created relation between group id and user id successfully
	 */
	@Override
	public Boolean createClientsOfGroup(int groupId,List<Integer> list) {
		Connection con = JDBCUtils.getConnection();
		String sql = "INSERT INTO persontogroup(group_id,user_id) VALUES(?,?)";
		try {
			for(int i=0;i<list.size();i++) {
				PreparedStatement pre = con.prepareStatement(sql);
				pre.setInt(1, groupId);
				pre.setInt(2, list.get(i));
				int rowCount = pre.executeUpdate();
				if(rowCount != 1) {
					return false;
				}

			}

			return true;
			
									
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(con, null, null);
		}
		return true;
	}

	/**
	 * we use this method to delete relation between group id and user id
	 * @param groupId the id of the group
	 * @return weather we have deleted relation between group id and user id successfully
	 */
	@Override
	public boolean dropGroupRelation(int groupId) {
		Connection con = JDBCUtils.getConnection();
		String sql = "delete from persontogroup where group_id=?";
		try {
				PreparedStatement pre = con.prepareStatement(sql);
				pre.setInt(1, groupId);
				int rowCount = pre.executeUpdate();
				if(rowCount == 0) {
					return false;
				}
				return true;												
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(con, null, null);
		}
		return true;
	}

	/**
	 * we use this method to delete group
	 * @param groupId the id of the group
	 * @return weather we have deleted group successfully
	 */
	@Override
	public boolean dropGroupId(int groupId) {
		Connection con = JDBCUtils.getConnection();
		String sql = "delete from chatgroup where group_id=?";
		try {
				PreparedStatement pre = con.prepareStatement(sql);
				pre.setInt(1, groupId);
				int rowCount = pre.executeUpdate();
				if(rowCount != 1) {
					return false;
				}
				return true;												
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(con, null, null);
		}
		return true;
	}

	@Override
	public boolean changePassword(String name, String password, String newPassword) {
		Connection con = JDBCUtils.getConnection();
		String sql = "UPDATE ACCOUNT SET PASSWORD=? WHERE USER_ACCOUNT=?";
		try {
			PreparedStatement pre = con.prepareStatement(sql);
			pre.setString(1, newPassword);
			pre.setString(2, name);

			int rowCount = pre.executeUpdate();
			if(rowCount != 1) {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(con, null, null);
		}
		return true;
	}



}

	
	

