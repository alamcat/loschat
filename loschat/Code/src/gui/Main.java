package gui;

import client.ClientMain;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
/**
 * this is main page
 * 3-17 log:
 * 1. add logo & enjoy chatting label
 * new log:
 * 1. modify close method
 * 2. add logo
 * 3. modify IsValidInput(username)
 * 4. add tips
 *
 *
 *  -------------------------------------------------------------
 * |                       Main layout                           |
 * |                          HBox                               |
 * |                           |                                 |
 * |                           |                                 |
 * |                      logo |  VBox                           |
 * |                               |                             |
 * |                            gridPane                         |
 * |                                                             |
 * |create    Btn  -change scene                                 |
 * |login     Btn  -change scene                                 |
 *  -------------------------------------------------------------
 *
 * @author Yan
 * @version 2020-3-9
 */
public class Main extends Application {

    private static ClientMain clientMain;
    Stage window;
    Scene scene1, newScene;
    static String accountName;

    ShowScene showScene;
    ShowMain showMain;
    Exit exitProgram;

    TextField textField;
    PasswordField passwordField;

//    public Main(TextField textField, PasswordField passwordField) {
//        this.textField = textField;
//        this.passwordField = passwordField;
//    }

//    public Main(TextField textField, PasswordField passwordField){
//        this.textField = textField;
//        this.passwordField = passwordField;
//    }

    public static void main(String args[]){
        Application.launch(args);
    }

    public Stage getWindow() {
        return window;
    }
    public Scene getScene(){
        return scene1;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        clientMain = new ClientMain();

        //window,title,button
        window = primaryStage;
        window.setTitle("LosChat");

        //show main page (chat)
//        showMain = new ShowMain(window, accountName);

        //close & exit
        exitProgram = new Exit(window);
        String styles1 =//login button
                "-fx-background-color: #000000;" +
                "-fx-border-color: #000000, #000000;";

        String alert2 =
//                "-fx-border-width: 0 1 1 1, 1 0 0 0;\n" +
//                        "-fx-border-radius: 5, 5;\n" +
                        "-fx-border-color: #FA6565, #FA6565;\n" +
                        "-fx-background-color: #ffffff;";
        String alert =
                "-fx-border-color: #FA6565;\n" +
                "-fx-background-color: #ffffff;";

        String styles4 = //normal
                "-fx-border-color: #D0D0D0;\n" +
                "-fx-background-color: #ffffff;";

//----------------------
        //logo
        FileInputStream file = new FileInputStream("src/gui/images/logo.png");
        Image detailUserImage = new Image(file);
        ImageView logo = new ImageView(detailUserImage);
//        Label logo = new Label("Logo");

//        logo.setFont(new Font("Arial", 40));
//        logo.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
//----------------------
        //login part

        //add items
        Label enjoy = new Label("Enjoy chatting");
        enjoy.setFont(Font.font("Arial", 16));
        enjoy.setTextFill(Color.BLACK);
        //name input
        Label nameLabel = new Label("Username");
        nameLabel.setFont(Font.font("Arial", 12));
        nameLabel.setTextFill(Color.GRAY);
        TextField nameInput = new TextField();
        nameInput.setStyle(styles4);
//        nameInput.setPromptText("At most 20 characters");
        VBox name = new VBox(3);
        name.getChildren().addAll(nameLabel, nameInput);
        //password input
        Label passLabel = new Label("Password");
        passLabel.setFont(Font.font("Arial", 12));
        passLabel.setTextFill(Color.GRAY);
        PasswordField passInput = new PasswordField();
        passInput.setStyle(styles4);
//        passInput.setPromptText("Numbers"); //hint
        VBox pass = new VBox(3);
        pass.getChildren().addAll(passLabel, passInput);

//-------------------------------------
        Button login = new Button("Log in");
        login.setPrefWidth(200);
        login.setStyle(styles1);
        login.setFont(Font.font("Arial", 14));
        login.setTextFill(Color.WHITE);
//-------------------------------------
        Label hint = new Label("Don't have an account?");
        hint.setFont(Font.font("Arial", 10));
        Label sign = new Label("Sign up");
        HBox signUp = new HBox(5);
        sign.setFont(Font.font("Arial", 10));
        sign.setTextFill(Color.rgb(245,166,35));
        sign.hoverProperty().addListener((v, oldValue, newValue) -> {
            if(newValue)
                sign.setUnderline(true);
            else if(oldValue)
                sign.setUnderline(false);
        });
        signUp.setAlignment(Pos.CENTER_LEFT);
        signUp.getChildren().addAll(hint, sign);

//-------------------------------------
        /**
         * get user input & judge valid input
         * //username is required
         */
////** merged to login action ***************************************************
//        checkPass.setOnAction(e -> {
//            System.out.println(passInput.getText());
//            //call isInt method
//            boolean checkPassResult = IsValidInput.isInt(passInput, passInput.getText());
//            //clear input
////            passInput.clear();
//            //if invalid change promptText
//            // Add the connection with the client ------peng
//            if(!clientMain.login(nameInput.getText(), passInput.getText())){
//                System.out.println(nameInput.getText()+ " " + passInput.getText());
//                passInput.setPromptText("Please enter numbers!");
//            } else {
//                try {
//                    // Pass the client to the showMain -----peng
//                    showMain = new ShowMain(window, nameInput.getText());
//                    clientMain.createStream(nameInput.getText());
//                    utils.Analysis.setAccount(nameInput.getText());
//                    ShowMain.showMain(scene1);
//                } catch (FileNotFoundException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
////*****************************************************
        login.setOnAction(e -> {
            if(nameInput.getText().isEmpty()) {
                nameInput.setPromptText("Username is required");
                nameInput.setStyle(alert);
            }
            else{
                if(passInput.getText().isEmpty()){
                    passInput.setPromptText("Password is required");
                    passInput.setStyle(alert);
                }
                else if(!nameInput.getText().isEmpty() && !passInput.getText().isEmpty()) {
                    // Add the connection with the client ------peng
                    if(!clientMain.login(nameInput.getText(), passInput.getText())){
//                        passInput.setPromptText("Please enter numbers!");
                        //alert
                        login.setFont(Font.font("Arial", 8));
                        login.setTextFill(Color.RED);
                        login.setText("Invalid account or password!\nIf you don't have an account, sign up.");
                        login.setStyle(alert2);
                    } else {
                        try {
                            nameInput.setStyle(styles4); //set style default
                         //Pass the client to the showMain -----peng
                            showMain = new ShowMain(window, nameInput.getText());
                            clientMain.createStream(nameInput.getText());
                            utils.Analysis.setAccount(nameInput.getText());
                            ShowMain.showMain(scene1);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }


//                    System.out.println(nameInput.getText() + ": " + passInput.getText() + " logged in.");
//                    nameInput.setStyle(styles4);
//                    /**
//                     * switch to mainPage
//                     */
//                    try {
//                        ShowMain.showMain(scene1); //original
//                    } catch (FileNotFoundException ex) {
//                        ex.printStackTrace();
//                    }
                }

            }
//            System.out.println(passInput.getText());
//            nameInput.setPromptText("username is required");
//            //call isInt method
//            boolean checkPassResult = IsValidInput.isInt(passInput, passInput.getText());
//            //clear input
//            passInput.clear();
//            //if invalid change promptText
//            if(!checkPassResult){
//                passInput.setPromptText("Please enter numbers!");}
        });
//-------------------------------------
        nameInput.focusedProperty().addListener((e, oldValue, newValue) -> {
            if(newValue){
                nameInput.setStyle(styles4);
                login.setStyle(styles1);
                login.setText("Log in");
                login.setFont(Font.font("Arial", 14));
                login.setTextFill(Color.WHITE);
            }

        });
        passInput.focusedProperty().addListener((e, oldValue, newValue) -> {
            if(newValue){
                passInput.setStyle(styles4);
                login.setStyle(styles1);
                login.setText("Log in");
                login.setFont(Font.font("Arial", 14));
                login.setTextFill(Color.WHITE);
            }
        });

//------sign up page ------------------
        /**
         * switch to sign up page
         */
        showScene = new ShowScene(window);//ignore this page
        sign.setOnMouseClicked(e -> ShowScene.showScene(scene1, newScene));

//------close program----------------------------------------------
        /**
         * close the program
         * if scene = login -> close
         * else -> sure exit? -> yes -> close
         */
        window.setOnCloseRequest(e ->{
            if(window.getScene().equals(scene1)){
                window.close();
            }
            else{
                e.consume(); //consume this event
                Exit.exit();
            }
        });

//------add elements-------------------------------------------------
        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(
                enjoy, name, pass, login, signUp
        );
        vBox.setAlignment(Pos.CENTER_LEFT);

        HBox hBox = new HBox(50);
        hBox.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255), null, null)));
        HBox.setMargin(logo, new Insets(0, 15,0,0)); //top,right,bottom, left
        hBox.getChildren().addAll(logo, vBox);
        hBox.setAlignment(Pos.CENTER_LEFT);

//------remove default focus--------------------------------------------
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        nameInput.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                vBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        scene1 = new Scene(hBox, 600, 400);
        window.setScene(scene1);
        window.show();
    }

    public static ClientMain getClient() {
        return clientMain;
    }
}
