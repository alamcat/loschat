package gui;

import bean.GroupPackage;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Analysis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * create group chat
 * @author Yan
 * @version 2020-3-9
 */
public class CreateGroup {

    /**
     * field variable:
     * userList is all online user
     * account
     * abList
     *
     */

    private ObservableList<HBox> obList;
    private ObservableList<HBox> obListSelected;
    private ListView<HBox> userList = new ListView<>();
    private ListView<HBox> selectedList = new ListView<>();
    private String account;
    private ArrayList<HBox> toBeSeen = new ArrayList<>();
    private ArrayList<HBox> selected = new ArrayList<>();

    private Stage window;

    void display(String title, String currentAccount){
        window = new Stage();
        window.setTitle(title);
        //block input events or interactions with other windows
        //until this one is taken care of
        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (模态窗口)
        account = currentAccount;

//------------------------------------------------
        /**
         * create group - user list
         *         HBox
         *          |
         *    -----------
         *   |           |
         * VBox1       VBox2
         *   |           |
         *   userList   selected
         *
         */
        HBox hBox = new HBox();
        VBox vBox1 = new VBox(5);
        VBox vBox2 = new VBox(15);

        //vBox1---------------------

        // searching userlist
        StackPane searching = new StackPane();
        searching.setMaxSize(200, 40);
        searching.setStyle("-fx-background-color: #EDEDED");
        Rectangle rect = new Rectangle(182,25);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        rect.setStroke(null);
        rect.setFill(Color.WHITE);
        TextField searchUser = new TextField();
        searchUser.setMaxSize(150, 15);
        searchUser.setFont(new Font("Arial", 8));
        String styles1 =
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-color: #FFFFFF;" ;
        searchUser.setStyle(styles1);
        searchUser.setPromptText("Searching");
        searching.getChildren().addAll(rect, searchUser);
        StackPane.setMargin(rect, new Insets(7,13,8,12));

        //remove default focus
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        searchUser.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                hBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        userList.setMaxWidth(200);
        userList.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        vBox1.setAlignment(Pos.CENTER_LEFT);
        vBox1.getChildren().addAll(searching, userList);

        //line
        Line line = new Line(200,0, 200,400);//x1,y1, x2,y2
        line.setStroke(Color.LIGHTGRAY);


        //vBox2---------------
        Label title2 = new Label("Selected");
        title2.setFont(new Font("Arial", 12));
        title2.setTextFill(Color.GRAY);

        //group name
        TextField inputGroupName = new TextField();
        inputGroupName.setPromptText("Create group name");
        inputGroupName.setMaxWidth(190);
        inputGroupName.setStyle(
                "-fx-border-radius: 5;" +
                "-fx-border-color: gray;" +
                "-fx-background-color: #ffffff;");

        selectedList = new ListView<>();
        selectedList.setMaxWidth(190);
        selectedList.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        selectedList.setItems(obListSelected);

        Button done = new Button("Done");
        String border =
                "-fx-border-radius: 5;" +
                        "-fx-border-color: #FFBF11;\n" +
                        "-fx-background-color: #ffffff;";

        String fill = //normal
                "-fx-border-radius: 5;" +
                        "-fx-border-color: #FFBF11;\n" +
                        "-fx-background-color: #FFBF11;";
        done.setStyle(fill);
        Button cancel = new Button("Cancel");
        cancel.setStyle(border);
        HBox btn = new HBox(10);
        btn.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(done, new Insets(0,10,0,0));
        btn.getChildren().addAll(cancel, done);

        vBox2.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(title2, new Insets(10, 0,0,10));

        VBox.setMargin(btn, new Insets(0,0,10,0));
        vBox2.getChildren().addAll(title2, selectedList, inputGroupName, btn);

        //action--------------
        done.setOnAction(e -> {
            List<String> memberList = new ArrayList<>();
            for (HBox member : selected) {
                String memberAccount = ((Label)member.getChildren().get(1)).getText();
                System.out.println(memberAccount);
                System.out.println(member);
                memberList.add(memberAccount);

                //add group to chatting list

            }
            GroupPackage createGroup = new GroupPackage
                    (memberList, account, inputGroupName.getText());
            Main.getClient().getChat().sendPack(createGroup);
            window.close();
            //start group chatting
        });

        cancel.setOnAction(e -> {
            obListSelected = null;
            window.close();
        });

        // Create group here
        userList.setOnMouseClicked(mouseEvent -> {
            int index = userList.getSelectionModel().getSelectedIndex();
            HBox item = userList.getSelectionModel().getSelectedItem();
            userList.getItems().remove(userList.getSelectionModel().getSelectedItem());
            toBeSeen.remove(index);
            selectedList.getSelectionModel().clearSelection();

            selected.add(item);
            obListSelected = FXCollections.observableArrayList(selected);
            selectedList.setItems(obListSelected);
            //
        });

        selectedList.setOnMouseClicked(mouseEvent -> {
            int index = selectedList.getSelectionModel().getSelectedIndex();
            HBox item = selectedList.getSelectionModel().getSelectedItem();
            selectedList.getItems().remove(selectedList.getSelectionModel().getSelectedItem());
            selected.remove(index);
            selectedList.getSelectionModel().clearSelection();

            toBeSeen.add(item);
            obList = FXCollections.observableArrayList(toBeSeen);
            userList.setItems(obList);
        });

//------------------------------------------------
        hBox.getChildren().addAll(vBox1, line, vBox2);
        hBox.setStyle("-fx-background-color: #FFFFFF;");
        Scene scene = new Scene(hBox, 400, 400);
        window.setScene(scene);
        window.show(); //shows this stage and waits for it to be hidden before return to the caller
    }


    public void createOnlineList() {
        List<String> onlineList = Analysis.getOnlineList();
        System.out.println("Online list!!!!!: " + onlineList);

        //action on checkBox


        Platform.runLater(() -> {
            for (int i = 0; i < onlineList.size(); i++) {
                String accountInList = onlineList.get(i);

                //image
                StackPane userImage_i = new StackPane();
                Circle circle = new Circle();
                circle.setRadius(20.0f);
                circle.setStroke(null);

                ViewAll.setColor(accountInList.substring(0,1), circle);
                //name in avatar ------------------------------------
                Label label = new Label(accountInList.substring(0,1));
                label.setTextFill(Color.BLACK);
                label.setFont(new Font("Arial", 15));
                userImage_i.getChildren().addAll(circle,label);

                //name (using checkBox)
//                CheckBox checkBox = new CheckBox(accountInList);

                Label userName = new Label(onlineList.get(i));
                HBox hBox = new HBox(5);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(userImage_i, userName);
                toBeSeen.add(hBox);
                obList = FXCollections.observableArrayList(toBeSeen);
                userList.setItems(obList);

            }
        });
    }
}

