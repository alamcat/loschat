package gui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Analysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * this class is to show all users
 * @author Yan
 * @version 2020-3-9
 */
public class ViewAll {

    private static ListView<HBox> userList = new ListView<>();
    private static String account;
    private static ObservableList<HBox> obList;

    private static Stage window;
    private static TextField searchUser = new TextField();

    public static void display(String title, String currentAccount){
        window = new Stage();
        window.setTitle(title);
        //block input events or interactions with other windows
        //until this one is taken care of
        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (模态窗口)
        VBox vBox = new VBox(10);
        account = currentAccount;


//------------------------------------------------
        /**
         * @YongQi add elements here
         * 1. all user list (online)
         * 2. create group
         * 3. search
         */

        String border =
                "-fx-border-radius: 5;" +
                        "-fx-border-color: #FFBF11;\n" +
                        "-fx-background-color: #ffffff;";
        Button groupChat = new Button("Create Group Chat");
        groupChat.setStyle(border);
        //remove default focus
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        groupChat.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                vBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        StackPane searching = new StackPane();
        searching.setMaxSize(350, 40);
        searching.setStyle("-fx-background-color: #EDEDED");
        Rectangle rect = new Rectangle(350,25);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        rect.setStroke(null);
        rect.setFill(Color.WHITE);
       
        searchUser.setMaxSize(350, 15);
        searchUser.setFont(new Font("Arial", 8));
        String styles1 =
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: #FFFFFF;" ;
        searchUser.setStyle(styles1);
        searchUser.setPromptText("Searching");
        searching.getChildren().addAll(rect, searchUser);
        StackPane.setMargin(rect, new Insets(7,13,8,12));

//        groupChat.setOnAction(e->{
//            //            window.close();
//                        CreateGroup.display("Create GroupChat", account);
//
//                    });
                 // Add action listener by zongshi.
                    searchUser.setOnKeyPressed(new EventHandler<KeyEvent>() {	
                        public void handle(KeyEvent ke) {
                            if (ke.getCode() == KeyCode.ENTER) {
                                try {
                                    createOnlineList(searchOnlineUser());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if(ke.getCode() == KeyCode.BACK_SPACE) { // When delete all the input from the searchUser,re-display all online user.
                                
                                if(searchUser.getText().length() == 1) {
                                    try {
                                        createOnlineList(Analysis.getOnlineList());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            }
                            });
                    userList.setItems(obList);
        vBox.getChildren().addAll(groupChat,searching,userList);
//------------------------------------------------

        VBox.setMargin(groupChat, new Insets(10, 0,0,240)); //top,right,bottom, left
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 400, 400);
        window.setScene(scene);
        window.show(); //shows this stage and waits for it to be hidden before return to the caller

    }

    public static void setColor(String account, Circle circle) {
        int red=255;
        int blue=255;
        int green=255;
        if (Pattern.matches("^[A-Ha-h]+$", account)) {
            circle.setFill(Color.rgb(red,green-50,blue)); //first change red then change green then blue
        } else if (Pattern.matches("^[I-Pi-p]+$", account)){
            circle.setFill(Color.rgb(red-60,green,blue));
        } else {
            circle.setFill(Color.rgb(red,green,blue-90));
        }
    }

    public static void createOnlineList(List<String> onlineList) {
        System.out.println("Online list: " + onlineList);
        Platform.runLater(() -> {
            ArrayList<HBox> toBeSeen = new ArrayList<>();
            for (int i = 0; i < onlineList.size(); i++) {
                String accountInList = onlineList.get(i);

                //image------------------------------------
                StackPane userImage_i = new StackPane();
                Circle circle = new Circle();
                circle.setRadius(20.0f);
                circle.setStroke(null);

                setColor(accountInList.substring(0,1), circle);

                //name------------------------------------
                Label label = new Label(accountInList.substring(0,1).toUpperCase());
                label.setTextFill(Color.BLACK);
                label.setFont(new Font("Arial", 15));
                userImage_i.getChildren().addAll(circle,label);

                Label userName = new Label(accountInList);

                //button------------------------------------
                Button chat = new Button("Chatting");
                chat.setTextFill(Color.web("#FFBF11"));
                chat.setBackground(null);

                final String currentSelected = accountInList;
                chat.setOnAction(e-> {
                    System.out.println("currentSelected:"+ currentSelected);
                    Analysis.addNewPal(currentSelected);
                    window.close(); // added by -- Yan
                });

                //add all
                HBox hBox = new HBox(20);
                hBox.setAlignment(Pos.CENTER_LEFT);
                HBox.setMargin(userImage_i, new Insets(0,0,0,10));
                HBox.setMargin(chat, new Insets(0,0,0,180)); //top,right,bottom, left
                hBox.getChildren().addAll(userImage_i, userName, chat);
                toBeSeen.add(hBox);

            }
            obList = FXCollections.observableArrayList(toBeSeen);
            userList.setItems(obList);
        });
    }
        // Add online user search by zongshi
        public static List<String> searchOnlineUser() {
            String keyWord = searchUser.getText();
            List<String> onlineList = Analysis.getOnlineList();
            List<String> searchResult = new ArrayList<>();
            for(String eachUser: onlineList) {
                if(eachUser.contains(keyWord)) {
                    searchResult.add(eachUser);
                }
            }
            return searchResult;
        }
}

