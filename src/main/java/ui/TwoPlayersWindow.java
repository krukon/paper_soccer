package ui;

/**
 * Class representing a multiplayer offline window.
 * @author jakub
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TwoPlayers extends Application {
	private String appTitle = "Paper soccer";
	private Text windowTitle = new Text("Enter your names \n and board size");
	private Insets insets = new Insets(25, 25, 25, 25);
	
	private TextField playerOneName = new TextField();
	private TextField playerTwoName = new TextField();
	private TextField boardWidth = new TextField();
	private TextField boardHeight = new TextField();

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(appTitle);
		
		BorderPane border = new BorderPane();
		border.setPadding(insets);
		
		border.setTop(addWindowTitle());
		border.setCenter(addGridPane());
		border.setBottom(addStartButton());
		
		Scene scene = new Scene(border);
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	private HBox addWindowTitle() {
		HBox textBox = new HBox();
		textBox.setAlignment(Pos.CENTER);
		textBox.getChildren().add(windowTitle);
		
		return textBox;
	}
	
	private HBox addStartButton() {
		Button startButton = new Button("START");
		startButton.setMinSize(100, 50);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			//TODO Check names and board size
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Button pressed");	
			}
		});
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(startButton);
		
		return buttonBox;
	}
	
	private void addPlayersLabels(GridPane grid) {
		Label playerOne = new Label("Player one:");
		Label playerTwo = new Label("Player two:");
		grid.add(playerOne, 0, 0);
		grid.add(playerTwo, 0, 1);
	}
	
	private void addPlayersTextFields(GridPane grid) {
		grid.add(playerOneName, 1, 0);
		grid.add(playerTwoName, 1, 1);
	}
	
	private void addBoardSize(GridPane grid) {
		Label width = new Label("Width:");
		Label height = new Label("Height");
		grid.add(width, 0, 5);
		grid.add(height, 0, 6);
		
		grid.add(boardWidth, 1, 5);
		grid.add(boardHeight, 1, 6);
	}
	
	private GridPane addGridPane() {
		GridPane grid = new GridPane();
		grid.setMinSize(300, 400);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(insets);
		
		addPlayersLabels(grid);
		addPlayersTextFields(grid);
		addBoardSize(grid);
		
		return grid;
	}
}
