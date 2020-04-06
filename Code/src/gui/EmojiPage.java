package gui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ClientHistoryReader;
import utils.StringHistorySearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this class is to generate the emoji page to add emoji into chat.
 * @author Zongshi
 * @version 2020-3-17
 */
public class EmojiPage {
	private static Map<String, String> emoji = new HashMap<>();
	// Generate a labelList which will contains all the labels associated with emojis.
	static List<Label> labelList = new ArrayList<>();
	
        
	/**
	 * Display the page which contains the emoji function.
	 * @throws UnsupportedEncodingException
	 */
	static void display() throws UnsupportedEncodingException {
		// Add some essential emoji info into the emoji map.
		emoji.put("\uD83D\uDE01", "[:smile]");
        emoji.put("\uD83D\uDE02", "[:laughTear]");
        emoji.put("\uD83D\uDE04", "[:laugh]");
        emoji.put("\uD83D\uDE05", "[:laughSweat]");
        emoji.put("\uD83D\uDE06", "[:laughClosedEye]");
        emoji.put("\uD83D\uDE09", "[:wink]");
        emoji.put("\uD83D\uDE0A", "[:sweet]");
        emoji.put("\uD83D\uDE0C", "[:relief]");
        emoji.put("\uD83D\uDE0D", "[:heartEyes]");
        emoji.put("\uD83D\uDE12", "[:assume]");
        emoji.put("\uD83D\uDE14", "[:pensive]");
        emoji.put("\uD83D\uDE16", "[:confound]");
        emoji.put("\uD83D\uDE18", "[:kiss]");
        emoji.put("\uD83D\uDE1C", "[:tongue]");
        emoji.put("\uD83D\uDE22", "[:crying]");
        emoji.put("\uD83D\uDE24", "[:triumph]");
        emoji.put("\uD83D\uDE2D", "[:loudlyCrying]");
        emoji.put("\uD83D\uDE31", "[:fear]");
        emoji.put("\uD83D\uDE32", "[:astonish]");
        emoji.put("\uD83D\uDE33", "[:flush]");
        emoji.put("\uD83D\uDE37", "[:flush]");


        Stage window = new Stage();
        window.setTitle("emoji");
        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (模态窗口)
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(10);
        flow.setHgap(10);
        flow.setPrefWrapLength(4);// Set the width of the page.
        flow.setStyle("-fx-background-color: DAE6F3;");


        // Generate an emoji set to get each key and value associated with the key.
        Set<Map.Entry<String, String>> emojiSet = emoji.entrySet();
        // Use for loop to 
        for (Map.Entry<String, String> entry : emojiSet) {
        	Label newLabel = new Label();
        	newLabel.setWrapText(true);
        	newLabel.setManaged(true);
        	newLabel.setMinSize(25, 25);
        	newLabel.setText(entry.getKey());
        	// Listener of mouse clicked movement to add the description of selected emoji into the content of input in ShowMain.
        	newLabel.setOnMouseClicked((event->{
        		ShowMain.input.setText(ShowMain.input.getText() + entry.getValue());
                window.close();
            }));
        	// Add the new generated label into the labelList.
        	labelList.add(newLabel);
        }
        
        // Use a for loop to add all labels into the FlowPane.
        for(Label label : labelList) {
        	flow.getChildren().add(label);
        }
        Scene scene = new Scene(flow, 400, 100);

        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.showAndWait();
        
	}

}



