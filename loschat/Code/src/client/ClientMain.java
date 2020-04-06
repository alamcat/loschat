package client;

import bean.RegisterPackage;
import utils.Analysis;
import bean.AuthenticationPackage;
import bean.GroupPackage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the main class of the Client.
 * @author Peng Jiang
 * @version 2020-03-07
 */
public class ClientMain extends ClientAux {
    private Socket socket;
    private ClientChatThread chat;
    private ExecutorService service;
    private ObjectInputStream inObject;
    private ObjectOutputStream outObject;
    private String account;

    /**
     * The constructor of the ClientMain. A socket
     * and it corresponding output and input stream
     * will be constructed here.
     */
    public ClientMain() {
        Emoji emoji = new Emoji();
        try {
            // Initialise the client before connection
            socket = new Socket("localhost", 56678);
            outObject = new ObjectOutputStream(socket.getOutputStream());
            inObject = new ObjectInputStream(socket.getInputStream());
            service = Executors.newCachedThreadPool();
        } catch (IOException e) {
            println("Could not connect to the server. " + e.getMessage());
        }
    }

    /**
     * This method creates the output and input stream.
     * @param account the account of current user
     */
    public void createStream(String account) {
        chat = new ClientChatThread(socket, inObject, outObject);
        service.submit(chat);
        this.account = account;
        println("Welcome back! " + account);
        // Create history folder;Still need to change the path.
        File file=new File("src/","history");
		if(!file.exists()){
			file.mkdir();
			System.out.println("History folder has been created.");
		} else {
			System.out.println("History folder exists.");
		}
    }

    /**
     * This method generates a new Authentication object,
     * send it to the server and check the result.
     * @param account the account of a user
     * @param password the password of a user
     * @return true if the information is corrected or
     * false if not.
     */
    public boolean login(String account, String password) {
        try {
            AuthenticationPackage auAccount =
                    new AuthenticationPackage(account, password);
            outObject.writeObject(auAccount);
            Object objResult = inObject.readObject();
            boolean result = Analysis.resultPackage(objResult);
            if (result == false) {
                try {
                    // Initialise the client before connection
                    socket = new Socket("localhost", 56678);
                    outObject = new ObjectOutputStream(socket.getOutputStream());
                    inObject = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    println("Could not connect to the server. " + e.getMessage());
                }
            }
            return result;
        } catch (ClassNotFoundException | IOException e) {
            println("Ops, sorry! Something bad happened. " +
                    "We cannot process your request now. " + e.getMessage());
            return false;
        }
    }

    /**
     * This method generates a new Register object, send
     * it to the server and check the result.
     * @param account the account of a user
     * @param password the password of a user
     * @param gender the gender of a user
     * @param age the age of a user
     * @return true if success or not if the account exists
     */
    public boolean register(String account, String password, String gender, int age) {
        try {
            RegisterPackage reAccount =
                    new RegisterPackage(account, password, gender, age);
            outObject.writeObject(reAccount);
            Object objResult = inObject.readObject();
            return Analysis.resultPackage(objResult);
        } catch (IOException | ClassNotFoundException e) {
            println("Ops, sorry! Something bad happened. " +
                    "We cannot process your request now. " + e.getMessage());
            return false;
        }
    }

    /**
     * This method delete the group from user's account.
     * @param id the group id
     */
    public void endGroup(String id) {
        GroupPackage delete = new GroupPackage(null, null, null);
        delete.setGroupID(id);
        chat.sendPack(delete);
    }

    /**
     * Getter for the chat thread.
     * @return the send thread
     */
    public ClientChatThread getChat() {
        return chat;
    }

    /**
     * Getter for the account.
     * @return the use's account
     */
    public String getAccount() {
        return account;
    }

}
