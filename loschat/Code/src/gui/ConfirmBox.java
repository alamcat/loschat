package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * this class is to show alert box of type modal
 * @author Yan
 * @version 2020-3-9
 */
public class ConfirmBox {
    //boolean variable to store the answers
    static boolean answer;

    static boolean display(String title, String message){
        Stage window = new Stage();
        window.setTitle(title);
        Label label = new Label(message);

        //block input events or interactions with other windows
        //until this one is taken care of
        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (模态窗口)

        //two buttons: yes & no
        Button yesbutton = new Button("Yes");
        Button nobutton = new Button("No");

        yesbutton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        nobutton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        HBox layoutButtons = new HBox(20);
        layoutButtons.getChildren().addAll(yesbutton, nobutton);
        layoutButtons.setAlignment(Pos.CENTER);
        VBox layout =new VBox(30);
        layout.getChildren().addAll(label, layoutButtons);
        layout.setAlignment(Pos.CENTER);

        //remove default focus
        yesbutton.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue){
                layout.requestFocus(); // Delegate the focus to container
            }
        });


//        window.setMinWidth(400);
        Scene scene = new Scene(layout, 300, 250);
        window.setScene(scene);
        window.showAndWait(); //shows this stage and waits for it to be hidden before return to the caller

        return answer;
    }
}
