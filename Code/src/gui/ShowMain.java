package gui;

import bean.Messenger;
import client.Emoji;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import utils.Analysis;
import utils.ClientHistorySearch;
import utils.PathUtils;

import java.io.*;
import java.util.*;

/**
 * this is main page
 * 文件说明：
 *
 *  -------------------------------------------------------------
 * |                   mainPage layout                           |
 * |                        gridPane                             |
 * |                           |                                 |
 * |                           |                                 |
 * |                      VBox |    VBox                         |
 * |                       |         |                           |
 * |                   HBox         AnchorPane                   |
 * |                   stackPane    ScrollPane                   |
 * |                   HBox         HBox                         |
 * |                   ScrollPane                                |
 * |                                                             |
 * |                                                             |
 * |userImage Btn  -popWindow (UserDetail.class) @YongQi         |
 * |menu      Btn-popWindow (Menu.class)                         |
 * |hist      Btn -popWindow (HistoryChat.class) @ZongShi        |
 * |member Btn -popWindow (InviteNewMember.class)                |
 * |-------------------------------------------------------------|
 * |menu-view all Btn -popWindow (ViewAll.class) @YongQi         |
 * |menu-exit     Btn -popWindow (Exit.class)                    |
 * |                                                             |
 *  -------------------------------------------------------------
 *
 */
public class ShowMain {

    private static Stage window;
    private static Scene scene;
    private static ListView<HBox> userList; //list view contains HBox(which is each item of online user) --Yan
    private static ListView<HBox> groupList;
    private static String account;
    private static Map<String, ScrollPane> chattingPane;
    private static Map<String, AnchorPane> header;
    private static VBox vBoxRight;
    private static String currentChat;
    private static Map<String, List<Label>> msgList;
    private static Map<String, List<Label>> groupMsgList;
    private static boolean isGroup;

    private static FileInputStream defaultImage;
    static TextField searchUser = new TextField();
    static TextArea input = new TextArea();


    public ShowMain(Stage window, String accountName) {
        this.window = window;
        this.scene = scene;
        account = accountName;
        userList = new ListView<>();
        chattingPane = new HashMap<>();
        header = new HashMap<>();
        vBoxRight = new VBox();
        msgList = new HashMap<>();
        groupMsgList = new HashMap<>();
        isGroup = false;
        createChattedList();
    }
    /**
     * show mainPage scene
     */
    public static void showMain (Scene origin) throws FileNotFoundException {

        //gridPane contains two elements (VBox1, VBox2)
        GridPane gridPane = new GridPane();

//VBoxLeft/////////////////////////////////////////////////////////////
        //HBox user detail
        AnchorPane userDetail = new AnchorPane();
        userDetail.setPrefSize(200, 55);
        userDetail.setStyle("-fx-background-color: #DCDCDC");
        //user image
        StackPane userImage_i = new StackPane();
        Circle userImage = new Circle();
        userImage.setRadius(20.0f);
        userImage.setStroke(null);

        ViewAll.setColor(account.substring(0,1), userImage);
        Label label = new Label(account.substring(0,1).toUpperCase());
        label.setTextFill(Color.BLACK);
        label.setFont(new Font("Arial", 15));
        userImage_i.getChildren().addAll(userImage,label);

    //-> popWindow
        userImage_i.setOnMouseClicked(e -> {
            try {
                UserDetail.display("User detail", account, Analysis.getGender(),Analysis.getAge());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // setting
        Button menu = new Button(">");
    //-> popWindow
        menu.setOnAction(e -> Menu.display("Menu", account));
        //HBox1-userDetail add elements
        HBox.setMargin(userImage_i, new Insets(10,0,10,13)); //top,right,bottom, left
        HBox.setMargin(menu, new Insets(0,10,0,0)); //top,right,bottom, left
        menu.setAlignment(Pos.CENTER_RIGHT);

        AnchorPane.setLeftAnchor(userImage_i, 10.0);
        AnchorPane.setTopAnchor(userImage_i, 10.0);
        AnchorPane.setBottomAnchor(userImage_i, 10.0);
        AnchorPane.setRightAnchor(menu, 10.0);
        AnchorPane.setTopAnchor(menu, 15.0);
        AnchorPane.setBottomAnchor(menu, 15.0);

        userDetail.getChildren().addAll(userImage_i, menu);

        ///////////////////////////////////////////////////////////////
        //StackPane searching userlist
        StackPane searching = new StackPane();
        searching.setMaxSize(200, 40);
        searching.setStyle("-fx-background-color: #EDEDED");
        Rectangle rect = new Rectangle(182,25);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        rect.setStroke(null);
        rect.setFill(Color.WHITE);
        
        searchUser.setMaxSize(150, 15);
        searchUser.setFont(new Font("Arial", 8));
        String styles1 =
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: #FFFFFF;" ;
        searchUser.setStyle(styles1);
        searchUser.setPromptText("Searching");
        // New in by zongshi to display overall search page
        searchUser.setOnKeyPressed(new EventHandler<KeyEvent>() {
        	
        	public void handle(KeyEvent ke) {
        		
        		if (ke.getCode() == KeyCode.ENTER) {
        			try {
        				OverallSearchPage.display();
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
        		}

        		});
        searching.getChildren().addAll(rect, searchUser);
        StackPane.setMargin(rect, new Insets(7,13,8,12));

        //remove default focus
        menu.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue){
                gridPane.requestFocus(); // Delegate the focus to container
            }
        });

        //HBox switch group chat
        HBox switchGroup = new HBox(5);
//        switchGroup.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        switchGroup.setPrefWidth(200);
        Button single = new Button("Single");
        single.setPrefWidth(90);
        Button group = new Button("Group");
        group.setPrefWidth(90);


        HBox.setMargin(single, new Insets(5, 0, 5, 0));
        HBox.setMargin(single, new Insets(5, 0, 5, 0));
        switchGroup.getChildren().addAll(single, group);
        switchGroup.setAlignment(Pos.CENTER);



        ///////////////////////////////////////////////////////////////
        // userlist
        // change code here --------peng
        userList.setPrefSize(200, 305);
        userList.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        String style =
                "-fx-selection-bar: #FFEDBC;" +
                "-fx-selection-bar-non-focused: #FFFFFF;";
        userList.setStyle(style);


        /*
         * Items have been moved to the method create Online list
         * Idea for this changing is that, I store all the frames(header, chatting pane)
         * in two ArrayLists. When user click another user from left list, the app will
         * set current elements in VoxRight to corresponding elements and show them.
         * --------peng
         * --------Yan: oh no!
         */
    //choose user to chat with-------------------------------------------------------
        userList.setOnMouseClicked(mouseEvent -> {

            int currentSelected = userList.getSelectionModel().getSelectedIndex();
            if (currentSelected >= 0) {
                isGroup = false;
                currentChat = Analysis.setCurrentChat(currentSelected);
                if (!chattingPane.containsKey(currentChat)) {
                    createChatting(currentChat);
                } else {
                    //set chatting
                    vBoxRight.getChildren().set(1,chattingPane.get(currentChat));
                    vBoxRight.getChildren().set(0, header.get(currentChat));
                }
                List<List<String>> msgUnread = Analysis.getMsgList(currentChat);
                for(int i = 0; i < msgUnread.get(1).size(); i++) {
                    othersMsg(currentChat, msgUnread.get(1).get(i));
                }
            }
        });
//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
        //groupList
        groupList = new ListView<>();
        groupList.setPrefSize(200, 305);
        groupList.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        groupList.setStyle(style);
        groupList.setVisible(false); //default



//        Group groupElements = new Group();
//        groupElements.getChildren().addAll(group_i);
        //set actions
        groupList.setOnMouseClicked(e -> {

            //choose group to chat with-------------------------------------------------------

            int currentSelected = groupList.getSelectionModel().getSelectedIndex();
            if (currentSelected >= 0) {
                isGroup = true;
                currentChat = Analysis.getGroupOnlineList().get(currentSelected);
                Analysis.setGroupCurrentChat(currentChat);
                if (!chattingPane.containsKey(currentChat)) {
                    createChatting(currentChat);
                } else {
                    //set chatting
                    vBoxRight.getChildren().set(1,chattingPane.get(currentChat));
                    vBoxRight.getChildren().set(0, header.get(currentChat));
                }
                List<List<String>> msgUnread = Analysis.getGroupMsgListList(currentChat);
                for(int i = 0; i < msgUnread.get(1).size(); i++) {
                    othersMsg(currentChat, msgUnread.get(1).get(i));
                }
            }


        });




        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
        //switchGroup
        single.setOnAction(e -> {
            groupList.setVisible(false);
            userList.setVisible(true);
        });

        group.setOnAction(e -> {
            createGroupList();
            groupList.setVisible(true);
            userList.setVisible(false);
        });
//& &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

//end left/////////////////////////////////////////////////////////////


        AnchorPane headerBar = new AnchorPane();

        ScrollPane chatting = new ScrollPane();

        HBox typingArea = new HBox(10);

        FileInputStream file9 = new FileInputStream("src/gui/images/icon_sticker.png");
        Image stickerImage = new Image(file9);
        ImageView imageOfSticker = new ImageView(stickerImage);
        Button sticker = new Button("", imageOfSticker);
        sticker.setBackground(new Background(new BackgroundFill(null, null, null)));
              // New in by zongshi to start EmojiPage
              sticker.setOnAction(event->{
                try {
                    EmojiPage.display();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        input.setWrapText(true);
        input.setMaxWidth(250);
        input.setMaxHeight(80);

        HBox.setMargin(sticker, new Insets(10,0,10,10)); //top,right,bottom, left
        HBox.setMargin(input, new Insets(10,10,10,0)); //top,right,bottom, left

        //send button ---Yan
        Button send = new Button("Send");


        HBox.setMargin(send, new Insets(10,0,10,0)); //top,right,bottom, left
        typingArea.getChildren().addAll(sticker, input, send);

        /**
         * press enter to new line ---Yan
         */
        KeyCodeCombination kb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN);
        input.setOnKeyPressed(event -> {
            if (kb.match(event)) {
                input.appendText("\n");
            } else if (event.getCode() == KeyCode.ENTER) {
                ShowMain.myMsg(Emoji.stringToEmoji(input.getText()));
                Main.getClient().getChat().sendPack(new Messenger(isGroup, input.getText(), account, currentChat));
                //send msg
                addMyMsg(input.getText());
                input.clear();
            }
        });

        send.setOnAction(e -> {
            ShowMain.myMsg(Emoji.stringToEmoji(input.getText()));
            Main.getClient().getChat().sendPack(new Messenger(isGroup, input.getText(), account, currentChat));
            //send msg
            addMyMsg(input.getText());
            input.clear();
        });

        ///////////////////////////////////////////////////////////////
        //add all
        StackPane list = new StackPane();
        list.getChildren().addAll(groupList, userList);
        VBox vBoxLeft = new VBox(); //userDetail, searching, list
        vBoxLeft.getChildren().addAll(userDetail, searching, switchGroup, list);



        vBoxRight.getChildren().addAll(headerBar, chatting, typingArea);


        GridPane.setConstraints(vBoxLeft, 0,0);
        GridPane.setConstraints(vBoxRight, 1,0);
        vBoxRight.setPrefWidth(400);
        gridPane.getChildren().addAll(vBoxLeft, vBoxRight);
        vBoxRight.setVisible(false);
        Scene scene = new Scene(gridPane, 600, 400);
        window.setScene(scene);
    }


    /**
     * This method creates the chatted list.
     * @param chatMsg the msg list of one user
     */
    public static void createListAux(Map<String, List<List<String>>> chatMsg) {
        StringBuffer chat = new StringBuffer("");
        List<String> newChattedList = new ArrayList<>();

        Platform.runLater(() -> {
            Set<String> tempList = chatMsg.keySet();
            List<HBox> toBeSeen = new ArrayList<>();
            int a = 0;
            for (String key : tempList) {
                List<Label> labelList = new ArrayList<>();

                msgList.put(key, labelList);
                // Set avatar
                StackPane userImage_i = new StackPane();
                Circle circle = new Circle();
                circle.setRadius(20.0f);
                circle.setStroke(null);

                ViewAll.setColor(key.substring(0,1), circle);
                Label label = new Label(key.substring(0,1).toUpperCase());
                label.setTextFill(Color.BLACK);
                label.setFont(new Font("Arial", 15));
                userImage_i.getChildren().addAll(circle,label);

                //last message & time
                String lastMsgTime = "";
                String lastMsg = "";

//item_i-----------------------------------------------------------

                AnchorPane hBox1 = new AnchorPane();
                hBox1.setPrefWidth(140.0);

                Label userName = new Label(key);

                Font timeFont = new Font("Arial", 11);
                Label time = new Label(lastMsgTime); //should use time toolkit
                labelList.add(time);
                time.setMaxWidth(50);
                time.setAlignment(Pos.CENTER_RIGHT);
                time.setFont(timeFont);
                time.setTextFill(Color.GRAY);

                AnchorPane.setRightAnchor(time, 5.0);
                AnchorPane.setLeftAnchor(userName, 0.0);
//                hBox1.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
                hBox1.getChildren().addAll(userName, time);
//                hBox1.setAlignment(Pos.CENTER_LEFT);

                HBox hBox2 = new HBox(30);
                Label lastMes = new Label();
                Font msgFont = new Font("Arial", 12);
                lastMes.setFont(msgFont);
                lastMes.setTextFill(Color.GRAY);
                lastMes.setText(lastMsg);
                lastMes.setMaxWidth(100);
                labelList.add(lastMes);

                hBox2.getChildren().add(lastMes);

                HBox hBox = new HBox(5);
                hBox.setMaxWidth(200);
                VBox vBox1 = new VBox(5);

//                vBox1.setMaxWidth(150); //set max size------------------Yan
                vBox1.getChildren().addAll(hBox1, hBox2);
                hBox.getChildren().addAll(userImage_i, vBox1);

                toBeSeen.add(hBox);
                if (a < chatMsg.size()-1) {
                    chat.append(key+"\n");
                } else {
                    chat.append(key);
                }
                newChattedList.add(key);
                a++;
//END item_i-----------------------------------------------------------
            }
            try {
                String path = "src/log/";
                String fileName = "ChattedList";
                File chattedListFile = new File(path + account + fileName);
                chattedListFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(chattedListFile));
                writer.write(String.valueOf(chat));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Analysis.setChattedList(newChattedList);
            ObservableList<HBox> obList = FXCollections.observableArrayList(toBeSeen);
            userList.setItems(obList);
        });
    }

    /**
     * This method creates a new chat pane if there is no pane in the list -----peng
     * @param currentChat the currently talking user
     */
    public static void createChatting(String currentChat) {
        ScrollPane chatting = new ScrollPane();
        chatting.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        chatting.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        VBox bubbles = new VBox(10); //on the top of chatting

//        bubbles.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
//        bubbles.setMaxWidth(400);
        bubbles.setPrefWidth(380);
        bubbles.setPadding(new Insets(5, 5, 5, 5)); //top,right,bottom,left ------yan
        bubbles.setAlignment(Pos.CENTER);

        chatting.setContent(bubbles);
        chatting.setPrefSize(400,290); //don't change it!!!!!!!!!---Yan
        chattingPane.put(currentChat, chatting);

        ////////////////////////////create head bar/////////////////////////////////
        String currentChatHead = currentChat;
        if (isGroup) {
            currentChatHead  = Analysis.findGroupName(currentChat);
        }
        Label curUser = new Label(currentChatHead);
        curUser.setFont(new Font("Arial", 16));
//        curUser.setStyle("-fx-background-color: #000000");
        curUser.setMaxWidth(200);
        curUser.setMaxHeight(20);
        Button histChat = new Button("histChat"); //button histChat
        //-> popWindow
        histChat.setOnAction(e -> {
            try {
            	HistoryChat.setAccount(account);// Add this by zongshi for history reader.
            	HistoryChat.setChattingAccount(currentChat);// Add this by zongshi for history reader.
                HistoryChat.display("");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        Button mem = new Button("+"); //button group
//-> popWindow
        mem.setOnAction(e -> {
            InviteNewMember.display("");
        });
        HBox hBox3 = new HBox(10);
        hBox3.getChildren().addAll(histChat, mem);

        AnchorPane headerBar = new AnchorPane();
        headerBar.setStyle("-fx-background-color: #EDEDED");
//        headerBar.setMaxSize(400, 55);

        AnchorPane.setLeftAnchor(curUser, 15.0);
        AnchorPane.setTopAnchor(curUser, 19.0);
        AnchorPane.setBottomAnchor(curUser, 19.0);
        AnchorPane.setRightAnchor(hBox3, 15.0);
        AnchorPane.setTopAnchor(hBox3, 16.0);
        headerBar.getChildren().addAll(curUser, hBox3);
        header.put(currentChat,headerBar);
        ///////////////////////////////////////////////////////////////

        ///////////////////////////set all////////////////////////////
        vBoxRight.getChildren().set(0, header.get(currentChat));
        vBoxRight.getChildren().set(1, chattingPane.get(currentChat));
        vBoxRight.setVisible(true);
    }

    /**
     * This method creates the bubble message from other
     * in the screen.
     * @param msg message from other users
     */
    public static void othersMsg(String currentChatLocal, String msg) {
        Platform.runLater(() -> {

            // on the top of chatting
            VBox bubbles = new VBox();
            bubbles.setPrefWidth(380);

            // chatBubble has two elements: image & message
            HBox bubbleOther = new HBox(15);
            bubbleOther.setMaxWidth(380);

            // Set avatar
            StackPane userImage_i = new StackPane();
            Circle circle = new Circle();
            circle.setRadius(20.0f);
            circle.setStroke(null);

            ViewAll.setColor(currentChatLocal.substring(0,1), circle);
            Label label = new Label(currentChatLocal.substring(0,2));
            label.setTextFill(Color.BLACK);
            label.setFont(new Font("Arial", 15));
            userImage_i.getChildren().addAll(circle,label);

            StackPane areaOther = new StackPane();
            areaOther.setMaxWidth(200);
            areaOther.setBackground(new Background(new BackgroundFill(Color.WHITE,
                    new CornerRadii(10), new Insets(0,0,0,0))));

            // create bubble
            Label textAreaOther = new Label();
            textAreaOther.setWrapText(true);
            textAreaOther.setMaxWidth(200);
            textAreaOther.setText(Emoji.stringToEmoji(msg));

            StackPane.setMargin(textAreaOther, new Insets(5, 10,5,10));
            areaOther.getChildren().add(textAreaOther);

            bubbleOther.getChildren().addAll(userImage_i, areaOther);
            bubbleOther.setAlignment(Pos.CENTER_LEFT);

            //top, right, bottom, left
            bubbles.setPadding(new Insets(5, 5, 5, 10));
            bubbles.setAlignment(Pos.CENTER);
            bubbles.getChildren().add(bubbleOther);
            String currentChatPane = currentChatLocal;
            if (isGroup) {
                currentChatPane = currentChat;
            }

            ((VBox)chattingPane.get(currentChatPane).getContent()).getChildren().add(bubbles);
            scrollToBottom(chattingPane.get(currentChatPane));
        });
    }

    /**
     * This method creates the bubble message from user
     * in the screen.
     * @param msg message from user
     */
    public static void myMsg(String msg) {
        // on the top of chatting changing bubbles to anchorPane
        VBox bubbles = new VBox(10);
        bubbles.setPrefWidth(380);

        HBox bubbleMine = new HBox(15);
        bubbleMine.setMaxWidth(380);

        StackPane userImage_i = new StackPane();
        Circle circle = new Circle();
        circle.setRadius(20.0f);
        circle.setStroke(null);
        //color function
        //for i in [a-zA-Z] -> red = index - 50 --Yan don't know how to make it

        ViewAll.setColor(account.substring(0,1), circle);
        // Name
        Label label = new Label(account.substring(0,2));
        label.setTextFill(Color.BLACK);
        label.setFont(new Font("Arial", 15));

        userImage_i.getChildren().addAll(circle,label);
        StackPane areaMine = new StackPane();
        areaMine.setMaxWidth(200);
        areaMine.setBackground(new Background(new BackgroundFill(Color.web("#FFEDBC"),
                new CornerRadii(10), new Insets(0,0,0,0))));

        Label textAreaMine = new Label(); //bubble
        textAreaMine.setWrapText(true);
        textAreaMine.setMaxWidth(200);
        textAreaMine.setText(msg);

        StackPane.setMargin(textAreaMine, new Insets(5, 10,5,10));
        areaMine.getChildren().add(textAreaMine);

        bubbleMine.getChildren().addAll(areaMine, userImage_i);
        bubbleMine.setAlignment(Pos.CENTER_RIGHT);

        //top, right, bottom, left
        bubbles.setPadding(new Insets(5, 5, 5, 5));
        bubbles.setAlignment(Pos.CENTER);
        bubbles.getChildren().add(bubbleMine);

        ((VBox)chattingPane.get(currentChat).getContent()).getChildren().add(bubbles);

        scrollToBottom(chattingPane.get(currentChat));
    }

    /**
     * This method scrolls the pane to the bottom.
     * @param scrollPane pane to be scrolled
     */
    static void scrollToBottom(ScrollPane scrollPane) {
        Animation animation = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                        new KeyValue(scrollPane.vvalueProperty(), 1)));
        animation.play();
    }

    /**
     * This method creates the chatted list when user
     * logs in.
     */
    public static void createChattedList() {
        try {
            String path = "src/log/";
            String fileName = "ChattedList";
            File dir = new File(path);
            File file = new File(path + account + fileName);
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (file.exists() && file.isFile()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));
                BufferedReader reader = new BufferedReader(read);

                String nextLine = null;
                while ((nextLine = reader.readLine()) != null) {
                    if (!nextLine.equals("")) {
                        Analysis.addChattedList(nextLine);
                    }
                }
                reader.close();
                read.close();
            } else {
                file.createNewFile();
            }
            createListAux(Analysis.getMapChattedList());
        } catch (IOException e) {
            System.out.println("File not found!" + e);
        }
    }

    public static void createGroupList() {
        Platform.runLater(() -> {
            Map<String, List<List<String>>> group = Analysis.getGroupMsgList();
            Set<String> temp = group.keySet();
            List<HBox> toBeSeen = new ArrayList<>();
            for (String key : temp) {
                //group_i %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                HBox group_i = new HBox(5);
                //name
                Label name = new Label(group.get(key).get(2).get(0));

                //avatar
                StackPane image_i = new StackPane();
                Circle groupCircle = new Circle();
                groupCircle.setRadius(20.0f);
                groupCircle.setFill(Color.YELLOW);
                groupCircle.setStroke(null);
                Label label2 = new Label(name.getText().substring(0,1).toUpperCase());
                image_i.getChildren().addAll(groupCircle, label2);

                //name & time & last message
                String lastMsgTime = "";
                String lastMsg = "";
                AnchorPane name_time = new AnchorPane();
                name_time.setPrefWidth(140.0);
                Font timeFont = new Font("Arial", 11);
                Label time = new Label(lastMsgTime); //should use time toolkit
                time.setMaxWidth(50);
                time.setAlignment(Pos.CENTER_RIGHT);
                time.setFont(timeFont);
                time.setTextFill(Color.GRAY);

                List<Label> labelList = new ArrayList<>();


                AnchorPane.setRightAnchor(time, 5.0);
                AnchorPane.setLeftAnchor(name, 0.0);
//        name_time.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
                name_time.getChildren().addAll(name, time);

                HBox lastMessage = new HBox(30);
                Label lastMes = new Label();
                Font msgFont = new Font("Arial", 12);
                lastMes.setFont(msgFont);
                lastMes.setTextFill(Color.GRAY);
                lastMes.setText(lastMsg);
                lastMes.setMaxWidth(100);
                lastMessage.getChildren().add(lastMes);

                VBox name_time_lastMes = new VBox(5);
                name_time_lastMes.getChildren().addAll(name_time, lastMessage);

                //add all to group_i
                Analysis.setGroupOnlineList(key,group.get(key).get(2).get(0));
                labelList.add(time);
                labelList.add(lastMes);
                groupMsgList.put(key,labelList);
                group_i.getChildren().addAll(image_i, name_time_lastMes);
                toBeSeen.add(group_i);
            }
            ObservableList<HBox> obList = FXCollections.observableArrayList(toBeSeen);
            groupList.setItems(obList);
        });
    }

    /**
     * This method adds the user's messages to currently
     * talking account message list.
     * @param msgDetail the message from users themselves
     */
    public static void addMyMsg(String msgDetail) {
        String receivedTime = PathUtils.getReceivedData();
        renewMsg(currentChat, msgDetail, receivedTime);
    }

    /**
     * This method renews the message displayed on chatted
     * list when new messages come.
     * @param accountLocal the account to be renewed
     * @param msg the message to be shown
     * @param time the time of the message to be shown
     */
    public static void renewMsg(String accountLocal, String msg, String time) {
        Platform.runLater(() -> {
            if (!isGroup) {
                msgList.get(accountLocal).get(1).setText(msg);
                msgList.get(accountLocal).get(0).setText(time);
            } else {
                groupMsgList.get(currentChat).get(1).setText(msg);
                groupMsgList.get(currentChat).get(0).setText(time);
            }
        });
    }
}
