package gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class is to show alert box of type modal
 * @author Yan
 * @version 2020-3-9
 */
public class Menu {

    static String borderBlack =
            "-fx-border-radius: 5;" +
                    "-fx-border-color: #000000;\n" +
                    "-fx-background-color: #ffffff;";

    static String fillBlack =
            "-fx-border-radius: 5;" +
                    "-fx-border-color: #000000;\n" +
                    "-fx-background-color: #000000;";
    static void display(String title, String account){
        Stage window = new Stage();
        window.setTitle(title);
        //block input events or interactions with other windows
        //until this one is taken care of
        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (模态窗口)
        VBox vBox =new VBox(20);

//------------------------------------------------
        Button viewAll = new Button("View all users");
        Button exit = new Button("Exit");
        Button createGroup = new Button("Create a group");

        viewAll.setStyle(fillBlack);
        viewAll.setTextFill(Color.WHITE);

        exit.setStyle(borderBlack);
        exit.setTextFill(Color.BLACK);

        createGroup.setStyle(fillBlack);
        createGroup.setTextFill(Color.WHITE);

        viewAll.setMaxSize(150, 100);
        exit.setMaxSize(150, 100);
        createGroup.setMaxSize(150, 100);

        //remove default focus
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        viewAll.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                vBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        //actions-viewAll
        viewAll.setOnAction(e -> {
            window.close();
            ViewAll.display("View all users",account);
        });

        //actions-creGroup
        createGroup.setOnAction(e -> {
            CreateGroup createGroup1 = new CreateGroup();
            createGroup1.createOnlineList();
            createGroup1.display("Create group chat", account);
            window.close();
        });

        //actions-exit
        exit.setOnAction(e -> {
            window.close();
            Exit.exit();
        });

        vBox.getChildren().addAll(viewAll, createGroup, exit); // remove createGroup Button ---Yan
//------------------------------------------------

        vBox.setAlignment(Pos.CENTER);
//        window.setMinWidth(400);
        Scene scene = new Scene(vBox, 200, 150);
        window.setScene(scene);
        window.show(); //shows this stage and waits for it to be hidden before return to the caller

    }
}
