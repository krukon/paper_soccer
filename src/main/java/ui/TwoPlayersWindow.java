package ui;

/**
 * Class representing a multiplayer offline window.
 * 
 * @author jakub
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TwoPlayersWindow extends BorderPane {
	private Text windowTitle = new Text("Enter your names \n and board size");
	private Insets insets = new Insets(25, 25, 25, 25);
	
	private TextField playerOneName;
	private TextField playerTwoName;
	private TextField boardWidth;
	private TextField boardHeight;
	
	public TwoPlayersWindow() {
		playerOneName = new TextField();
		playerTwoName = new TextField();
		boardWidth = new TextField();
		boardHeight = new TextField();
		
		setPadding(insets);
		setTop(addWindowTitle());
		setCenter(addGridPane());
		setBottom(addStartButton());
	}
	
	/**
	 * Constructs window title.
	 * @return HBox with a title
	 */
	private HBox addWindowTitle() {
		HBox textBox = new HBox();
		textBox.setAlignment(Pos.CENTER);
		textBox.getChildren().add(windowTitle);
		
		return textBox;
	}
	
	/**
	 * Constructs a button to start new match
	 * @return HBox with a button
	 */
	private HBox addStartButton() {
		Button startButton = new Button("START");
		startButton.setMinSize(100, 50);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			//TODO Check names and board size
			@Override
			public void handle(ActionEvent event) {
				System.out.println("COnstructing board...");
			
			}
		});
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(startButton);
		
		return buttonBox;
	}
	
	/**
	 * Adds labels for text fields to grid.
	 * @param grid grid to place labels
	 */
	private void addPlayersLabels(GridPane grid) {
		Label playerOne = new Label("Player one:");
		Label playerTwo = new Label("Player two:");
		grid.add(playerOne, 0, 0);
		grid.add(playerTwo, 0, 1);
	}
	
	/**
	 * Adds text fields to grid. 
	 * @param grid grid to place text fields
	 */
	private void addPlayersTextFields(GridPane grid) {
		grid.add(playerOneName, 1, 0);
		grid.add(playerTwoName, 1, 1);
	}
	
	/**
	 * Adds board text fields and labels to grid.
	 * @param grid grid to place text fields and labels
	 */
	private void addBoardSize(GridPane grid) {
		Label width = new Label("Width:");
		Label height = new Label("Height");
		grid.add(width, 0, 5);
		grid.add(height, 0, 6);
		
		grid.add(boardWidth, 1, 5);
		grid.add(boardHeight, 1, 6);
	}
	
	/**
	 * Constructs base grid and its components for window.
	 * @return constructed grid with components
	 */
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
