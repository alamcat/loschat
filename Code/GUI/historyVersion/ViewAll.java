import javafx.geometry.Pos;import javafx.scene.Scene;import javafx.scene.control.Button;import javafx.scene.control.Label;import javafx.scene.layout.HBox;import javafx.scene.layout.VBox;import javafx.stage.Modality;import javafx.stage.Stage;/** * this class is to show all users * @author Yan * @version 2020-3-9 */public class ViewAll {    static void display(String title){        Stage window = new Stage();        window.setTitle(title);        //block input events or interactions with other windows        //until this one is taken care of        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (模态窗口)        VBox vBox =new VBox(10);//------------------------------------------------        /**         * @YongQi add elements here         * 1. all user list (online)         * 2. create group         * 3. search         */        vBox.getChildren().addAll();//------------------------------------------------        vBox.setAlignment(Pos.CENTER);        Scene scene = new Scene(vBox, 400, 400);        window.setScene(scene);        window.showAndWait(); //shows this stage and waits for it to be hidden before return to the caller    }}