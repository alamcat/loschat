package client;

import utils.Analysis;
import bean.GroupPackage;
import bean.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestMain extends ClientAux {
    private static ClientMain client;

    public static void main(String[] args) {
        TestMain testMain = new TestMain();
        client = new ClientMain();
        System.out.println("Welcome to MOON, please select your operation:\n1. Register\n2. Login");
        Scanner scanner = new Scanner(System.in);
        String operation = "";
        if (scanner.hasNext()) {
            operation = scanner.next();
        }
        switch (operation) {
            case "1":
                testMain.registerGUI();
                break;

            case "2":
                testMain.loginGUI(scanner);
                break;
        }
    }

    public static void chartInterface() {
        System.out.println("Input \"online\" to check the online list " +
                "\nInput \"chat\" to start a chat\nInput \"createGroup\" to create a new group" +
                "\nInput \"group\" to start a group talk: ");
        String account = "";
        String input;
        String chat;
        final String currentAccount = account;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                input = scanner.nextLine();
                switch (input) {
//                    case "online":
//                        System.out.println("=============Online List===============");
//                        Analysis.getOnlineList().forEach((e) -> {
//                            System.out.print(e + "         ");
//                            if (!e.equals(currentAccount) && Analysis.getMsgList(e) != null) {
//                                System.out.print("(" + Analysis.getMsgList(e).size() + ")" + "\n");
//                            } else {
//                                System.out.println();
//                            }
//                        });
//                        System.out.println("=======================================");
//                        break;
                    case "chat":
                        scanner = new Scanner(System.in);
                        System.out.print("Enter the account you want to talk: ");
                        account = scanner.nextLine();
//                        Analysis.setCurrentChat(account);
//                        if (Analysis.getMsgList(account) != null) {
//                            Analysis.getMsgListClear(account).forEach(e -> {
//                                System.out.println(e);
//                            });
//                        }
                        while (true) {
                            System.out.print("Enter: ");
                            scanner = new Scanner(System.in);
                            if (scanner.hasNext()) {
                                chat = scanner.nextLine();
                                if (chat.equals("returnTo")) {
//                                    Analysis.setCurrentChat("");
                                    break;
                                }
                                client.getChat().sendPack(new Messenger(false, chat, client.getAccount(), account));
                            }
                        }
                        break;
                    case "exit":
                        System.out.println("Thanks for using MOON. Bye!");
                        return;
                    case "createGroup":
                        String name;
                        System.out.print("Group Name: ");
                        scanner = new Scanner(System.in);
                        name = scanner.next();
                        int a = 1;
                        System.out.print("Member" + a + ": ");
                        List<String> memberList = new ArrayList<>();
                        scanner = new Scanner(System.in);
                        String currentLine;
                        while (!(currentLine = scanner.nextLine()).equals("NULL")) {
                            a++;
                            System.out.print("Member" + a + ": ");
                            memberList.add(currentLine);
                        }
                        client.getChat().sendPack(new GroupPackage(memberList, client.getAccount(), name));
                        System.out.println("Waiting for response...");
                        break;
                    case "group":
                        System.out.print("Choose your group id: ");
                        scanner = new Scanner(System.in);
                        String currentID = scanner.next();
                        Analysis.setGroupCurrentChat(currentID);
                        while (true) {
                            System.out.print("Enter: ");
                            scanner = new Scanner(System.in);
                            if (scanner.hasNext()) {
                                chat = scanner.nextLine();
                                if (chat.equals("returnTo")) {
                                    Analysis.setGroupCurrentChat("");
                                    break;
                                }
                                client.getChat().sendPack(new Messenger(true, chat, client.getAccount(), currentID));
                            }
                        }
                        break;
                    case "endGroup":
                        System.out.println("Which group(id) you want to end: ");
                        scanner = new Scanner(System.in);
                        client.endGroup(scanner.next());
                        System.out.println("Waiting for response...");
                        scanner = new Scanner(System.in);
                        while (scanner.next().equals("returnTo")) {
                            break;
                        }
                }
            }
    }
    }


    public void registerGUI() {
        System.out.print("Please enter your account: ");
        Scanner scanner = new Scanner(System.in);
        String account = "";
        String password = "";
        String gender = "";
        String age = "";
        if (scanner.hasNext()) {
            account = scanner.next();
        }
        print("Please enter your password: ");
        scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            password = scanner.next();
        }
        print("Please enter your gender: ");
        scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            gender = scanner.next();
        }
        print("Please enter your age: ");
        scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            age = scanner.next();
        }
        if (client.register(account, password, gender, Integer.parseInt(age))) {
            println("Registered successfully!\n=================================");
            client.createStream(account);
            chartInterface();
        }
    }

    public void loginGUI(Scanner scanner) {
        String account;
        String password;
        print("Please enter your account: ");
        account = "";
        password = "";
        if (scanner.hasNext()) {
            account = scanner.next();
        }
        System.out.print("Please enter your password: ");
        scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            password = scanner.next();
        }

        if (client.login(account, password)) {
            client.createStream(account);
            scanner = new Scanner(System.in);
            chartInterface();
        }
    }
}

