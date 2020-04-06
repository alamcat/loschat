package gui;

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
import utils.ClientHistoryReader;
import utils.ClientHistorySearch;
import utils.StringHistorySearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is to do overall search to all history record.
 * @author Zongshi
 * @version 2020-3-17
 */
public class OverallSearchPage {
	// In each element of this list, index = 0 will store the detail of each search result,
	// And index = 1 will store the path of the file associated with the search result.
	static List<List<String>> searchResult = new ArrayList<>();
	
	// To display the overall search result page.
	static void display() throws Exception {
		ClientHistorySearch.resetList();
		// Generate new object to send the keyword.
        ClientHistorySearch clientHistorySearch = new ClientHistorySearch(ShowMain.searchUser.getText());
        try {
        	// Trigger the search of the keyword.
        	
        	clientHistorySearch.folderTraverse();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        searchResult = ClientHistorySearch.getSearchResult();
        Stage window = new Stage();
        window.setTitle("Overall History Search");
        window.initModality(Modality.APPLICATION_MODAL); //make this window modal (模态窗口)
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefViewportHeight(300);
        scrollPane.managedProperty();
        scrollPane.setBackground(new Background(new BackgroundFill(null, null, null)));
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15,20, 10,10));
        
        for (List<String> eachResult : searchResult) {
        	
        	if(eachResult != null) {
        		// Make each search result of the list a new label.
            	Label newLabel = new Label();
            	newLabel.setWrapText(true);
            	newLabel.setManaged(true);
            	newLabel.setMinSize(25, 25);
        		newLabel.setText(eachResult.get(0));
            	String style = "-fx-border-color: #D0D0D0;\n" + "-fx-background-color: #ffffff;";
            	newLabel.setStyle(style);;
            	newLabel.setOnMouseClicked((event->{
            		try {
            			// Get the path associated with each search result and send it to HistoryChat for future subpage display.
            			HistoryChat.setPath(eachResult.get(1));
    					HistoryChat.display();
    				} catch (FileNotFoundException e) {
    					e.printStackTrace();
    				}
                    window.close();
                }));
            	vbox.getChildren().add(newLabel);
        	} else {
        		System.out.println("No search result");
        	}
        	
        	
        }
        scrollPane.setContent(vbox);
        Scene scene = new Scene(scrollPane, 400, 400);
        window.setScene(scene);
        window.showAndWait();
        
	}

}

