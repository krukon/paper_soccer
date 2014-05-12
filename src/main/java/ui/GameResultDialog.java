package ui;

/**
 * Class representing a simple dialog box with game result.
 * 
 * @author jakub
 */

import controller.PaperSoccer;
import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameResultDialog extends Stage {
	
	public GameResultDialog(GameResult result) {
		initModality(Modality.APPLICATION_MODAL);
		
		Label message = new Label("Won " + result.getWinner().getName());
		message.setFont(Font.font(30));
		message.setAlignment(Pos.BASELINE_CENTER);
		
		Button rematch = new Button("Rematch");
		rematch.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Rematch");
				// TODO start new game				
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
        vBox.setSpacing(40.0);
        vBox.getChildren().addAll(message, hBox);

        setScene(new Scene(vBox, 300, 150));
	}
	/**
	 * @throws InterruptedException 
	 * @deprecated
	 * Only for game result dialog tests.
	 */
	public static void main(final String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainWindow.startNewGame(args);
			}	
		}).start();
		Thread.sleep(2000);
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				Player p = new Player() {

					@Override
					public void startNewGame(int width, int height) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void finishGame(GameResult result) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public Move getNextMove() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void registerMove(Move move) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public String getName() {
						// TODO Auto-generated method stub
						return "XXX";
					}

					@Override
					public Color getColor() {
						// TODO Auto-generated method stub
						return null;
					}
					
				};
				new GameResultDialog(new GameResult(p, 1, 1)).show();
				
			}
		});
	}
}
