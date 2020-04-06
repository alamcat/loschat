package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * We use this class to implements Runnable to build socket with the client.
 * @author Wenbin Hu
 */
public class LoginThread implements Runnable {

	
	private static ServerSocket serverSocket;
	
	/**
	 * Constructor of the LoginThread, build serversocket
	 * @param port port of the server
	 */
	public LoginThread(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("fail to start the login thread");
			e.printStackTrace();
		}
	}
	
	/**
	 * start check thread
	 */
	@Override
	public void run() {
		try {
			while (true) {

				Socket userSocket = serverSocket.accept();

				System.out.println(userSocket.getInetAddress() + " receive login information from client");


				new Thread(new CheckConnection(userSocket)).start();
			}
		} catch (IOException e) {
			System.out.println("error in logincheck thread£º" + e.getMessage());
		}
	}

}


