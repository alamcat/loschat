package client;

import bean.User;
import utils.Analysis;
import bean.GroupPackage;
import bean.Messenger;
import utils.ClientHistoryWriter;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a subclass of the ClientThread.
 * Its responsibility is to receive all kinds of
 * information from the server.
 * @author Peng Jiang
 * @version 2020-03-17
 */
public class ClientChatThread extends ClientAux implements Runnable {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;

    /**
     * Constructor for the ClientChatThread.
     * @param socket the socket between server and client
     * @param inputStream the inputStream to server
     * @param outputStream the outputStream from server
     */
    public ClientChatThread(Socket socket, ObjectInputStream inputStream,
                            ObjectOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        doShut();
    }

    /**
     * This methods called by the main thread and it sends
     * all kinds of packages to the server.
     * @param pack the packages to be sent
     */
    public <T> void sendPack(T pack) {
        try {
            if (pack != null) {
                outputStream.writeObject(pack);
                println("Sent package to server");
                // Add history writer here.
                if(pack instanceof Messenger) {	
                    ClientHistoryWriter.writeHistory((Messenger)pack,true);
                }
            }
        } catch (IOException e) {
            println("Send thread Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    /**
     * Run method for the chat(received thread). This
     * method receives all packages from the server.
     */
    public void run() {
        Object toBeRead;
        try {
            while (true) {
                Thread.sleep(1);
                toBeRead = inputStream.readObject();
                if (toBeRead instanceof Messenger) {
                    Analysis.msgPackage((Messenger)toBeRead);
                    println("Received Messenger from server");
                } else if (toBeRead instanceof List) {
                    Analysis.setOnlineList((List)toBeRead);
                } else if (toBeRead instanceof Map) {
                    Analysis.setGroupMsgList((HashMap)toBeRead);
                } else if (toBeRead instanceof GroupPackage) {
                    Analysis.groupResult((GroupPackage)toBeRead);
                } else if (toBeRead instanceof User) {
                    User user = (User)toBeRead;
                    if (user.getGender() != null) {
                        Analysis.userInformation(user);
                    } else {
                        Analysis.setPwdResult(user);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            println("Receive thread Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
                println("\nConnection lost from server!");
            } catch (Exception e) {
                println("(socket close)Receive thread Error: " + e.getMessage());
            }
        }
    }

    /**
     * This class close the socket if window is closed.
     */
    private void doShut() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        }
}
