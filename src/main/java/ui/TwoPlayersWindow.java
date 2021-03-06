package ui;

/**
 * Class representing a multiplayer offline window.
 * 
 * @author jakub
 */

import java.util.concurrent.atomic.AtomicBoolean;

import helpers.Player;
import controller.PaperSoccer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import controller.PaperSoccerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Board;

public class TwoPlayersWindow extends BorderPane {
	private Text windowTitle = new Text("");
	private Insets insets = new Insets(25, 25, 25, 25);
	
	private TextField playerOneName;
	private TextField playerTwoName;
	private TextField boardWidth;
	private TextField boardHeight;
	private Button startButton;
	private final AtomicBoolean correctNameOne = new AtomicBoolean(true);
	private final AtomicBoolean correctNameTwo = new AtomicBoolean(true);
	private final Label correctWidth = new Label("");
	private final Label correctHeight = new Label("");
	

	
	public TwoPlayersWindow() {
		setPrefSize(PaperSoccer.WIDTH, PaperSoccer.HEIGHT);
		setStyle("-fx-background-image: url('paper_soccer_background.jpg');");
		
		playerOneName = new TextField("Player 1");
		playerTwoName = new TextField("Player 2");
		boardWidth = new TextField("8");
		boardHeight = new TextField("10");
		
		windowTitle.setFill(Color.WHITE);
		
		setPadding(insets);
		setTop(addWindowTitle());
		setCenter(addGridPane());
		setBottom(addStartBackButton());
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
	 * Constructs a start button.
	 * @return constructed button
	 */
	private Button getStartButton() {
		startButton = new Button("START");
		startButton.setMinSize(100, 50);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				System.out.println("Constructing board");
				startGame(playerOneName.getText(), playerTwoName.getText());
			}
		});
		
		startButton.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent key) {
				if (key.getCode() == KeyCode.ENTER) {
					System.out.println("Constructing board");
					startGame(playerOneName.getText(), playerTwoName.getText());
				}
			}
		});
		
		return startButton;
	}
	
	/**
	 * Constructs an exit button.
	 * @return constructed button
	 */
	private Button getBackButton() {
		Button exitButton = new Button("BACK");
		exitButton.setMinSize(100, 50);
		exitButton.setCancelButton(true);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				System.out.println("Back to main menu");
				PaperSoccer.getMainWindow().showMenu();
			
			}
		});
		
		return exitButton;
	}
	
	/**
	 * Adds start and exit buttons to the window.
	 * @return HBox with a buttons
	 */
	private HBox addStartBackButton() {
		HBox buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(getStartButton(), getBackButton());
		
		return buttonBox;
	}
	
	/**
	 * Adds labels for text fields to grid.
	 * @param grid grid to place labels
	 */
	private void addPlayersLabels(GridPane grid) {
		Label playerOne = new Label("Player one:");
		Label playerTwo = new Label("Player two:");
		
		playerOne.setTextFill(Color.WHITE);
		playerTwo.setTextFill(Color.WHITE);
		
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
		
		playerOneName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
				if(newValue.matches("^(?=\\s*\\S).*$")){
					correctNameOne.set(true);
					
					if(correctNameTwo.get() && correctWidth.getText()=="" && correctHeight.getText()=="")
						startButton.setDisable(false);
				} else {
					correctNameOne.set(false);
					startButton.setDisable(true);
				}
			}
		});
		playerTwoName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
				if(newValue.matches("^(?=\\s*\\S).*$")){
					correctNameTwo.set(true);
					
					if(correctNameOne.get() && correctWidth.getText()=="" && correctHeight.getText()=="")	
						startButton.setDisable(false);
				} else {
					correctNameTwo.set(false);
					startButton.setDisable(true);
				}
			}
		});
	}
	
	/**
	 * Adds board text fields and labels to grid.
	 * @param grid grid to place text fields and labels
	 */
	private void addBoardSize(GridPane grid) {
		Label width = new Label("Width:");
		Label height = new Label("Height");
		
		width.setTextFill(Color.WHITE);
		height.setTextFill(Color.WHITE);
		grid.add(width, 0, 5);
		grid.add(correctWidth,0, 6, 2, 1);
		grid.add(height, 0, 7);
		grid.add(correctHeight, 0, 8, 2, 1);
		boardWidth.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
				try{
					if(!newValue.isEmpty() && Board.isValidWidth(Integer.parseInt(newValue))){
						correctWidth.setText("");
						if(correctHeight.getText()=="" && correctNameOne.get() && correctNameTwo.get())
							startButton.setDisable(false);
					}
					else{
						correctWidth.setText("Incorrect value");
						startButton.setDisable(true);
					}
				} catch (Exception e){
					correctWidth.setText("Incorrect value");
					startButton.setDisable(true);
				}
			}
		});
		boardHeight.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
				try{
					if(!newValue.isEmpty() && Board.isValidHeight(Integer.parseInt(newValue))){
						correctHeight.setText("");
						if(correctWidth.getText()=="" && correctNameOne.get() && correctNameTwo.get())
							startButton.setDisable(false);
					}
					else{
						correctHeight.setText("Incorrect value");
						startButton.setDisable(true);
					}
				} catch (Exception e){
					correctHeight.setText("Incorrect value");
					startButton.setDisable(true);
				}
			}
		});
		grid.add(boardWidth, 1, 5);
		grid.add(boardHeight, 1, 7);
	}
	
	/**
	 * Constructs base grid and its components for window.
	 * @return constructed grid with components
	 */
	private GridPane addGridPane() {
		GridPane grid = new GridPane();
		grid.setPrefSize(300, 400);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(insets);
		//grid.setStyle("-fx-background-image: url('paper_soccer_background.jpg'); -fx-background-repeat: stretch;");
		
		addPlayersLabels(grid);
		addPlayersTextFields(grid);
		addBoardSize(grid);
		
		return grid;
	}

	/**
	 * Begin new game for two players. Run controller, and
	 * display appropriate views.
	 *
	 * @param playerOne name of the first player
	 * @param playerTwo name of the second player
	 * @param width width of the field
	 * @param height width of the board
	 *
	 * @author krukon
	 */
	private void startGame(final String playerOne, final String playerTwo) {
		final int width = Integer.parseInt(boardWidth.getCharacters().toString());
		final int height = Integer.parseInt(boardHeight.getCharacters().toString());
		
		final Player host = new TwoPlayersGameWindow(playerOne, Color.BLUE, Color.RED, true);
		final Player guest = new TwoPlayersGameWindow(playerTwo, Color.RED, Color.BLUE, false);
		((GameWindow)host).registerNames(playerOne, playerTwo);
		((GameWindow)guest).registerNames(playerOne, playerTwo);
		((TwoPlayersGameWindow)host).registerWindows((GameWindow)guest, (GameWindow)host);
		((TwoPlayersGameWindow)guest).registerWindows((GameWindow)host, (GameWindow)guest);
		
		PaperSoccer.getMainWindow().showTwoPlayersGameWindow((GameWindow)guest, (GameWindow)host);

		Thread controllerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				PaperSoccerController controller = new PaperSoccerController(host, guest, width, height);
				
				((TwoPlayersGameWindow)host).registerController(controller);
				((TwoPlayersGameWindow)guest).registerController(controller);
				
				controller.runGame();
			}
		});
		controllerThread.setDaemon(true);
		controllerThread.start();
	}
}
