package ui;

/**
 * Class representing a simple dialog box while waiting for opponent.
 * 
 * @author jakub, ljk
 */

import controller.PaperSoccer;
import controller.PaperSoccerController;

import helpers.GameResult;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WaitForOpponentDialog extends Stage {
	public WaitForOpponentDialog(final PaperSoccerController controller) {
		initModality(Modality.APPLICATION_MODAL);
		
		Label message = new Label("You need to wait");
		message.setFont(Font.font(30));
		message.setAlignment(Pos.BASELINE_CENTER);
		
		Button exit = new Button("Back");
		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Back to network menu");
				PaperSoccer.getMainWindow().showNetworkWindow();
				close();
			}
		});
		
		HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setSpacing(40.0);
        hBox.getChildren().addAll(exit);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.setSpacing(40.0);
        vBox.getChildren().addAll(message, hBox);

        setScene(new Scene(vBox, 300, 150));
	}
}