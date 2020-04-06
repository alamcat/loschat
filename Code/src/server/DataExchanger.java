package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.GroupPackage;
import bean.Messenger;
import bean.User;
import service.UserService;
import service.impl.UserServiceImpl;

/**
 * a
 * @author Wenbin Hu
 */
public class DataExchanger implements Runnable{
	
	private UserService us;

	private ObjectInputStream in;

	private ObjectOutputStream out;
	
	private String account;
	
	private User user;
	
	/**
	 * constructor of the class, initialize inputstream, outputstream,account, userservice
	 * @param account account of the user
	 * @param out ObjectOutputStream of user
	 * @param in ObjectInputStream of user
	 */
	public DataExchanger(ObjectOutputStream out,ObjectInputStream in, String account) {
		this.account = account;
		us = new UserServiceImpl();
		user = us.getUserByAccount(account);
		
		this.in = in;
		this.out = out;


	}
	
	/**
	 * we use this method to send message package to user
	 * @param message message package
	 */
	private void sendMessage(Messenger message) {
		try {
			System.out.println(message.getMsgDetail());
			out.writeObject(message);
		} catch (IOException e) {
			System.out.println(account + "fail to send message to client£º" + e.getMessage());
		}
	}
	
	/**
	 * we use this method to send online list to user
	 * @param list  online list
	 */
	private void sendList(List<String> list) {
		try {
			out.writeObject(list);
		} catch (IOException e) {
			System.out.println(account + "fail to send message to client£º" + e.getMessage());
		}
	}
	
	/**
	 * we use this method to send group id to user
	 * @param Set set of group id
	 */
	private Set sendGroupIdAccountToUser() {

		Integer id = us.getUserIdByAccount(account);
		
		if(id == null) {
			throw new RuntimeException("fail to find user id");
		}
		
		Map<Integer,String> map = us.getGroupIdByUserId(id);
		try {
			out.writeObject(map);
		} catch (IOException e) {
			System.out.println(account + "fail to send message to client£º" + e.getMessage());
		}
		return map.keySet();
	}
	
	/**
	 * we use this method to send online list to user
	 */
	private void sendToAllClients() {
		int onlineSize = ServerController.onlineClients.size();
		System.out.println(onlineSize);
		Set<String> set = ServerController.onlineClients.keySet();
		List<String> list = new ArrayList<String>();
		for(String s:set) {
			list.add(s);
		}
		for (int i = 0; i < onlineSize; i++) {
			
			ServerController.onlineClients.get(list.get(i)).sendList(list);

		}
	}
	
	/**
	 * we use this method to send user package to user
	 */
	private void sendUser(User u) {
		try {
			out.writeObject(u);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(account + "fail to send user to client£º" + e.getMessage());
		}
	}
	
	/**
	 * we use this method to handle message package
	 * @param mess message package
	 */
	public void handleMessage(Messenger mess) {
		
		if(!mess.getGroup()){
			if(ServerController.onlineClients.containsKey(mess.getMsgTo())) {
				System.out.println(mess.getMsgDetail());
				ServerController.getDataExchangerById(mess.getMsgTo()).sendMessage(mess);
			}
		}else {
			Set<String> set = ServerController.groupclients.get(Integer.parseInt(mess.getMsgTo()));
			if(set != null) {
				for(String userAccount : set) {
					if(ServerController.onlineClients.containsKey(userAccount)) {
						ServerController.getDataExchangerById(userAccount).sendMessage(mess);
					}
					
				}
			}
		}
	}
	
	/**
	 * we use this method to create group
	 * @param groupName name of group
	 * @param accounts users in the group
	 * @return id of the group
	 */
	public Integer createGroup(String groupName,List<String> accounts) {
		return us.createGroup(groupName,accounts);
	}
	
	/**
	 * we use this method to send group package
	 * @param groupP a GroupPackage
	 */
	private void sendGroupMessage(GroupPackage groupP) {
		try {
			out.writeObject(groupP);
		} catch (IOException e) {
			System.out.println(account + "fail to send message to client£º" + e.getMessage());
		}
	}
	
	/**
	 * Classifying information and handle it
	 */
	@Override
	public void run() {
		
		sendUser(user);
		
		sendToAllClients();
		
		Set<Integer> setG = sendGroupIdAccountToUser();
		
		for(Integer i:setG) {
			Set<String> set = us.getGroupClientsByGroupId(i);
			ServerController.groupclients.put(i, set);
		}
		User u;					
		Messenger message;
		GroupPackage group;
		try {
			while (true) {
				try {
					Object ob = in.readObject();
					if(ob.getClass().getSimpleName().equals("Messenger")) {
						message = (Messenger) ob;
						handleMessage(message);
					}else if(ob.getClass().getSimpleName().equals("GroupPackage")) {
						group = (GroupPackage)ob;
						Integer groupId = createGroup(group.getGroupName(),group.getMemberList());
						if(groupId == null) {
							sendGroupMessage(group);
							
							Set<String> setNew = us.getGroupClientsByGroupId(groupId);
							
							ServerController.groupclients.put(groupId, setNew);
						}else {
							String s = "";
							
							group.setGroupID(groupId + s);
							
							List<String> list = group.getMemberList();
							if(list != null) {
								for(String userAccount : list) {
									if(ServerController.onlineClients.containsKey(userAccount)) {
										ServerController.getDataExchangerById(userAccount).sendGroupMessage(group);
									}									
								}
							}
							
							Set<String> set = new HashSet<String>();
							for(String soo:list) {
								set.add(soo);
							}
							ServerController.groupclients.put(groupId,set);
							
							
						}
					}else if(ob.getClass().getSimpleName().equals("User")){
						u = (User) ob;
						boolean flag = us.changePassword(u.getName(),u.getPassword(),u.getNewPassword());
						if(flag) {
							u.setPassword(u.getNewPassword());
							this.sendUser(u);
						}
					}
				
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}				
			}
		} catch (IOException e) {

			ServerController.onlineClients.remove(account);
			
			sendToAllClients();
		
			System.out.println("delete key is " + account + " from onlineClients.");

			
		}
	}
}
