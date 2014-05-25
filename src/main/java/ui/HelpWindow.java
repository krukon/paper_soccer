package ui;

/**
 * @author wTendera
 */


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelpWindow extends Stage {
	public HelpWindow() {
		initModality(Modality.NONE);
		setTitle("Help");
		
		Button close = new Button("Close");
		close.setCancelButton(true);
		close.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("close");
				close();
			}
		});
		
		Text text = new Text();
		text.setFont(new Font(20));
		text.setWrappingWidth(200);
		text.setTextAlignment(TextAlignment.JUSTIFY);
		text.setText("The quick brown fox jumps over the lazy dog");
		
		HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(40.0);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.getChildren().addAll(close);
        
		BorderPane borderPane = new BorderPane();
		borderPane.setBottom(hBox);
		borderPane.setCenter(text);
		

        setScene(new Scene(borderPane, 300, 600));
	}
}
