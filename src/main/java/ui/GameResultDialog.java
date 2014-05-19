package ui;

/**
 * Class representing a simple dialog box with game result.
 * 
 * @author jakub
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

public class GameResultDialog extends Stage {
	public GameResultDialog(GameResult result, final PaperSoccerController controller) {
		initModality(Modality.APPLICATION_MODAL);
		
		Label message = new Label("Scored: " + result.getWinner().getName());
		message.setFont(Font.font(20));
		message.setAlignment(Pos.BASELINE_CENTER);
		
		Label score = new Label(result.toString());
		score.setFont(Font.font(15));
		score.setAlignment(Pos.BASELINE_CENTER);
		
		Button rematch = new Button("Rematch");
		rematch.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Rematch");
				
				Thread controllerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						controller.prepareRematch();
						controller.runGame();
					}
				});
				controllerThread.setDaemon(true);
				controllerThread.start();
				
				close();
			}
		});
		
		Button exit = new Button("Main menu");
		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Back to main menu");
				PaperSoccer.getMainWindow().showMenu();
				close();
			}
		});
		
		HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setSpacing(40.0);
        hBox.getChildren().addAll(rematch, exit);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.setSpacing(20.0);
        vBox.getChildren().addAll(message, score, hBox);

        setScene(new Scene(vBox, 300, 150));
	}
}
