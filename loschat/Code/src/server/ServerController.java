package server;

import java.util.HashMap;
import java.util.Set;

import service.UserService;
import service.impl.UserServiceImpl;
/**
 * We use this class to start server controller
 * These maps can store information of the user and group
 * @author Wenbin Hu
 */
public class ServerController {
	private static UserService us = new UserServiceImpl();
	
	static HashMap<String, DataExchanger> onlineClients = new HashMap<String, DataExchanger>();

	static HashMap<Integer,Set<String>> groupclients = new HashMap<Integer,Set<String>>();
	
	//start login/register thread
	public static void main(String[] args) {
		new Thread(new LoginThread(56678)).start();
		System.out.println("server started");
	}
	
	/**
	 * we use this method to get dataexchanger of online user
	 * @param account account of user
	 * @return dataexchanger of online user
	 */
	public static DataExchanger getDataExchangerById(String account) {
		return onlineClients.get(account);
	}


	
}
