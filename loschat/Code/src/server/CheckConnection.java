package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bean.AuthenticationPackage;
import bean.Messenger;
import bean.RegisterPackage;
import bean.User;
import service.UserService;
import service.impl.UserServiceImpl;
/**
 * We use this class to implements Runnable to check weather user can login or register
 * @author Wenbin Hu
 */
public class CheckConnection implements Runnable {
	private UserService us;
	
	private Socket userSocket;
	
	Thread client;

	/**
	 * we use this constructor to initialize user socket and userservice
	 * @param userSocket
	 */
	public CheckConnection(Socket userSocket) {
		this.userSocket = userSocket;
		us = new UserServiceImpl();
	}
	
	/**
	 * we use this method to check weather user can login/register
	 * @param obj AuthenticationPackage or RegisterPackage
	 * @return result of the check
	 */
	public Object check(Object obj) {

		String objName = obj.getClass().getSimpleName();

		Object result = null;
		System.out.println("now the handle object is " + objName);
		switch (objName) {

		case "AuthenticationPackage":
			AuthenticationPackage loginCheck = (AuthenticationPackage) obj;

			if (ServerController.onlineClients.containsKey(loginCheck.getAccount())) 
				result = "AlreadyLogin";
			else {
				result = us.login(loginCheck.getAccount(), loginCheck.getPassword());


				if (result != null && (Boolean) result == true) {
					System.out.println("login successfully, create a chat thead for this client");
					client = new Thread(new ChatControllerThread(loginCheck.getAccount(),out,in));
				}
			}
			break;
		case "RegisterPackage":
			RegisterPackage register = (RegisterPackage)obj;
			User u = us.getUserByAccount(register.getAccount());
			if (u != null) 
				result = "AlreadyRegister";
			else {
				result = us.save(register);

				if (result != null && (Boolean) result == true) {
					System.out.println("register successfully, create a chat thead for this client");
					client = new Thread(new ChatControllerThread(register.getAccount(),out,in));
				}
			}
			break;
		default:
			break;
		}
		return result;
	}


	ObjectInputStream in;
	ObjectOutputStream out;
	
	/**
	 * send back result and start chatcontroller
	 */
	@Override
	public void run() {
		try {

			in = new ObjectInputStream(userSocket.getInputStream());
			out = new ObjectOutputStream(userSocket.getOutputStream());

				Object obj = in.readObject();


				Object result = check(obj);


				out.writeObject(result);


				if(result.getClass().getSimpleName().equals("String") || (Boolean)result == false) {
					userSocket.close();
				}else {
					
					client.start();
				}
				
				
				
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("VerifyConnection loss connection with server " + e);
		}

	}

}
