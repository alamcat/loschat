package gui;

import client.ClientMain;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

/**
 * this is sign up page
 * 3-17 log: --Yan
 * add user detail: gender & age
 *
 * modify log:
 * sign Button action: add clear & connectd to client ---Yan
 * local variable name changed: style2 to alert2 ----Yan
 *
 * @author Yan
 * @version 2020-3-14
 */
public class ShowScene {

    static Stage window;
    static String accountName;
    //run the client---Yan
    private static ClientMain clientMain;

    public ShowScene(Stage window) {
        this.window = window;
    }
    /**
     * sign up
     */
    public static void showScene(Scene origin, Scene newScene) {
        //new client Object
        clientMain = new ClientMain();

        Exit exitProgram = new Exit(window);
//        Main user = new Main(textField, passwordField);

        String styles1 =//login button
                "-fx-background-color: #000000;" +
                        "-fx-border-color: #000000;";

        String alert2 =
                "-fx-border-width: 0 1 1 1, 1 0 0 0;\n" +
                        "-fx-border-radius: 5, 5;\n" +
                        "-fx-border-color: #FA6565, #FA6565;\n" +
                        "-fx-background-color: #000000;";
        String alert =
                "-fx-border-color: #FA6565;\n" +
                        "-fx-background-color: #ffffff;";
        String styles4 =
                "-fx-border-color: #D0D0D0;\n" +
                        "-fx-background-color: #ffffff;";

//----------------------
        //sign up part
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //add items
        Label title = new Label("Welcome! Create an account");
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.BLACK);
        grid.add(title, 0,0, 2,1);

        //gender
        Label genderLabel = new Label("Gender");
        genderLabel.setMaxWidth(200);
        genderLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(genderLabel, 0,2);

        ComboBox<String> genderInput = new ComboBox<>();
        genderInput.getItems().addAll("Female", "Male", "No-binary");
        genderInput.setMaxWidth(200);
        genderInput.setStyle(styles4);
        grid.add(genderInput, 1,2);

        //age
        Label ageLabel = new Label("Age");
        ageLabel.setMaxWidth(200);
        ageLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(ageLabel, 0,3);

        TextField ageInput = new TextField();
        ageInput.setMaxWidth(200);
        ageInput.setStyle(styles4);
        grid.add(ageInput, 1,3);

        //name input
        Label nameLabel = new Label("Username");
        nameLabel.setMaxWidth(200);
        nameLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(nameLabel, 0,4);

        TextField nameInput = new TextField();
        nameInput.setMaxWidth(200);
        nameInput.setStyle(styles4);
        grid.add(nameInput, 1,4);

        Label tip1 = new Label("* Letters only, at most 20 characters, .");
        tip1.setFont(Font.font("Arial", 9));
        tip1.setTextFill(Color.GRAY);
        grid.add(tip1, 1,5);

        //password input
        Label passLabel = new Label("Password");
        passLabel.setMaxWidth(200);
        passLabel.setAlignment(Pos.CENTER_RIGHT);
        grid.add(passLabel, 0,6);

        PasswordField passInput = new PasswordField();
        passInput.setMaxWidth(200);
        passInput.setStyle(styles4);
        grid.add(passInput, 1,6);

        Label tip2 = new Label("* Numbers or letters at least 8 characters.");
        tip2.setFont(Font.font("Arial", 9));
        tip2.setTextFill(Color.GRAY);
        grid.add(tip2, 1,7);

//-------------------------------------
        Button sign = new Button("Join");
        sign.setPrefWidth(250);
        sign.setStyle(styles1);
        sign.setFont(Font.font("Arial", 14));
        sign.setTextFill(Color.WHITE);
//-------------------------------------
        Label hint = new Label("Already have an account?");
        hint.setFont(Font.font("Arial", 10));
        Label login = new Label("Log in");
        HBox log = new HBox(5);
        log.setMaxWidth(250);
//        log.setStyle("-fx-background-color: #ffffff;");
        login.setFont(Font.font("Arial", 10));
        login.setTextFill(Color.rgb(245,166,35));
        login.hoverProperty().addListener((v, oldValue, newValue) -> {
            if(newValue)
                login.setUnderline(true);
            else if(oldValue)
                login.setUnderline(false);
        });
        log.setAlignment(Pos.CENTER_RIGHT);
        log.getChildren().addAll(hint, login);

        //------add elements-------------------------------------------------
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(
                grid, sign, log
        );
//        vBox.setStyle("-fx-background-color: #f00000;");
        vBox.setAlignment(Pos.CENTER);

//-------------------------------------
        /**
         * get user input & judge valid input
         * //username is required
         * //Your password should be at least 8 characters long.
         * // reset your password
         * // Either your email or password was incorrect. Please try again or click the "Forgot password?" link below.
         */
        sign.setOnAction(e -> {

            if(!IsValidInput.isInt(ageInput, ageInput.getText())){
                ageInput.clear();
                ageInput.setPromptText("Numbers only!");
                ageInput.setStyle(alert);
            }
            else if(!IsValidInput.isValidAccount(nameInput)){
                nameInput.clear();
                nameInput.setPromptText("Username is invalid!");
                nameInput.setStyle(alert);
            }
            else if(!IsValidInput.isValidPass(passInput)){
                passInput.clear();
                passInput.setPromptText("Reset your password!");
                passInput.setStyle(alert);
            }

            else if(IsValidInput.isInt(ageInput, ageInput.getText()) && IsValidInput.isValidAccount(nameInput) && IsValidInput.isValidPass(passInput)){
                /**
                 * switch to mainPage
                 * pass user detail to client
                 */
                try {
                    //pass user detail

                    clientMain.register(nameInput.getText(), passInput.getText(), genderInput.getValue(), Integer.parseInt(ageInput.getText()));
                    System.out.println("name: " + nameInput.getText() + "\nage: " + ageInput.getText() + "\ngender: " + genderInput.getValue() + "\npassword: " + passInput.getText() + "\nRegistered successfully!");

                    //show main page (chat)
                    ShowMain showMain = new ShowMain(window, nameInput.getText());
                    ShowMain.showMain(origin); //original scene
                    clientMain.createStream(nameInput.getText());
                    utils.Analysis.setAccount(nameInput.getText());

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

        });

//-------------------------------------
        //reset style normal
        ageInput.focusedProperty().addListener((e, old, newV) ->{
            if(newV)
                ageInput.setStyle(styles4);
        });
        nameInput.focusedProperty().addListener((e, old, newV) ->{
            if(newV)
                nameInput.setStyle(styles4);
        });
        passInput.focusedProperty().addListener((e, old, newV) ->{
            if(newV)
                passInput.setStyle(styles4);
        });

//------sign up page ------------------
        /**
         * switch to sign up page
         */
        login.setOnMouseClicked(e -> window.setScene(origin));

//------close program----------------------------------------------
        /**
         * close the program
         * if scene = login -> close
         * else -> sure exit? -> yes -> close
         */
        window.setOnCloseRequest(e -> window.close());

//------remove default focus--------------------------------------------
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        genderInput.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                vBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });


////useless
////example listen for selection changes(choiceBox)
//// -----------------------------------------------------------------
//        ChoiceBox<String> choiceBox = new ChoiceBox<>();
//        //get items
//        choiceBox.getItems().add("see all users");
//        choiceBox.getItems().add("create group chat");
//        choiceBox.getItems().add("exit");
//
//        //listen for selection changes
//        choiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) ->
//                System.out.println(newValue));
//// -----------------------------------------------------------------
//
////example listen for selection changes(comboBox)
//// -----------------------------------------------------------------
//        ComboBox<String> comboBox = new ComboBox<>();
//        //get items
//        comboBox.getItems().addAll(
//                "see all users",
//                "create group chat",
//                "exit"
//                );
//
//        comboBox.setPromptText("...");
//
//        comboBox.setEditable(true); //can make it editable
//
//        //get choice
//        comboBox.setOnAction(e -> System.out.println("user choose to " + comboBox.getValue()));
//// -----------------------------------------------------------------

        newScene = new Scene(vBox, 600, 400);
        window.setScene(newScene);
    }
}
