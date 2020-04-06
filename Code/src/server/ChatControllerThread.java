package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * We use this class to implements Runnable to store information of the client 
 * and start dataexchanger of the client
 * @author Wenbin Hu
 */
public class ChatControllerThread implements Runnable{

	private String account;


	
	private ObjectOutputStream out;

	private ObjectInputStream in;

	/**
	 * constructor of the class, initialize inputstream, outputstream,account
	 * @param account account of the user
	 * @param out ObjectOutputStream of user
	 * @param in ObjectInputStream of user
	 */
	public ChatControllerThread(String account,ObjectOutputStream out,ObjectInputStream in) {
		this.account = account;
		this.in = in;
		this.out = out;

	}
	
	/**
	 * store information of the client and start dataexchanger thread
	 */
	public void run() {


		DataExchanger dataExchanger = new DataExchanger(out,in, account);

		System.out.println(account);
		

		ServerController.onlineClients.put(account, dataExchanger);

		System.out.println("client " + account + " login successfully ");

		dataExchanger.run();
	}
	
	
	
}
