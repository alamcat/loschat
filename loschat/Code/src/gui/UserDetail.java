package gui;

import bean.User;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import utils.Analysis;
import utils.Encryption;


import javax.swing.*;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * this class is to show user details
 *
 * @author Yan
 * @version 2020-3-9
 */
public class UserDetail {


    static void display(String title, String currentUser, String gender, int age) throws Exception {
        Stage window = new Stage();
        window.setTitle(title);
        Label nickNameLabel = new Label(currentUser);
        Label genderLabel = new Label(gender);
        Label textLabel = new Label("Change my password");
        Label ageLabel = new Label(age + "");
        PasswordField oldPassInput = new PasswordField();
        oldPassInput.setPromptText("Input your old password"); //hint
        PasswordField newPassInput = new PasswordField();
        newPassInput.setPromptText("Input your new password"); //hint
        Button changePasswordButton = new Button("Change");
        changePasswordButton.setStyle("-fx-background-color: #FF9900");
        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (妯℃�佺獥鍙�)
        /**
         * add elements here and replace this line below
         * 1. image of user
         * 2. nickname
         * 3. gender
         * 4. change password
         */

        HBox userDetail = new HBox();
        userDetail.setMinSize(60, 60);
        StackPane userImage_i = new StackPane();
        Circle circle = new Circle();
        circle.setRadius(20.0f);
        circle.setStroke(null);

        ViewAll.setColor(currentUser.substring(0,1), circle);
        Label label = new Label(currentUser.substring(0,2));
        label.setTextFill(Color.BLACK);
        label.setFont(new Font("Arial", 15));
        userImage_i.getChildren().addAll(circle,label);
        userDetail.getChildren().add(userImage_i);

        AnchorPane headerBar = new AnchorPane();
        AnchorPane.setLeftAnchor(userImage_i, 20.0);
        AnchorPane.setTopAnchor(userImage_i, 30.0);
        AnchorPane.setLeftAnchor(nickNameLabel, 80.0);
        AnchorPane.setTopAnchor(nickNameLabel, 30.0);
        AnchorPane.setLeftAnchor(genderLabel, 80.0);
        AnchorPane.setTopAnchor(genderLabel, 50.0);

        AnchorPane.setLeftAnchor(ageLabel,80.0);
        AnchorPane.setTopAnchor(ageLabel,70.0);
        AnchorPane.setTopAnchor(textLabel, 100.0);
        AnchorPane.setLeftAnchor(textLabel, 20.0);
        AnchorPane.setTopAnchor(oldPassInput, 130.0);
        AnchorPane.setLeftAnchor(oldPassInput, 20.0);
        AnchorPane.setTopAnchor(newPassInput, 170.0);
        AnchorPane.setLeftAnchor(newPassInput, 20.0);
        AnchorPane.setTopAnchor(changePasswordButton, 130.0);
        AnchorPane.setLeftAnchor(changePasswordButton, 192.0);
        headerBar.getChildren().addAll(nickNameLabel, genderLabel,
                textLabel, oldPassInput, newPassInput, changePasswordButton,ageLabel, userImage_i);
        changePasswordButton.setOnAction(e -> {
            String old = oldPassInput.getText();
            String newPwd = newPassInput.getText();
            System.out.println("New password:" + old);
            
            
            
            // TODO: 3/15/20
            // If later we use the database to store the password, you can call the interface here to modify it.
            if ((old == null || old.equals("")) && (newPwd == null || newPwd.equals(""))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.titleProperty().set("prompt");
                alert.headerTextProperty().set("New password cannot be empty！");
                alert.showAndWait();
            } else {
                Encryption encryption = new Encryption();
                changePassword(currentUser, encryption.Encryption(old), encryption.Encryption(newPwd));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.titleProperty().set("prompt");

                for (int i = 0; i < 50; i++) {
                    if (Analysis.getPwdResult() != null) {
                        System.out.println(Analysis.getPwdResult());
                        alert.headerTextProperty().set("Password reset complete!");
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (Analysis.getPwdResult() == null) {
                    System.out.println("Sorry we cannot change your password now");
                    alert.headerTextProperty().set("Sorry we cannot change your password now");
                }
//
//                window.close();
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(headerBar);
        Scene scene = new Scene(layout, 300, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void changePassword(String account, String oldPwd, String newPwd) {
        User changePwd = new User();
        changePwd.setName(account);
        changePwd.setPassword(oldPwd);
        changePwd.setNewPassword(newPwd);
        Main.getClient().getChat().sendPack(changePwd);
    }
}
