package ui;

/**
 * Class representing a multiplayer online window.
 * 
 * @author ljk
 */

import java.util.concurrent.atomic.AtomicBoolean;

import bots.BotLoader;
import bots.BotLoader.BotLoaderException;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Board;

public class NetworkWindow extends BorderPane {
	private Text windowTitle = new Text("Network game is waiting for you!");
	private Insets insets = new Insets(25, 25, 25, 25);
	
	private TextField playerName;
	private TextField boardWidth;
	private TextField boardHeight;
	private Button createButton;
	private Button joinButton;
	private final AtomicBoolean correctName = new AtomicBoolean(true);
	private final Label correctWidth = new Label("");
	private final Label correctHeight = new Label("");

	
	
	public NetworkWindow() {
		playerName = new TextField("Player");
		boardWidth = new TextField("8");
		boardHeight = new TextField("10");
		
		windowTitle.setFill(Color.WHITE);
		
		setPadding(insets);
		setTop(addWindowTitle());
		setCenter(addGridPane());
		setBottom(addSettingsBackButton());
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
	 * Constructs a create button.
	 * @return constructed button
	 */
	private Button getCreateButton() {
		createButton = new Button("CREATE");
		createButton.setMinSize(100, 50);
		
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				int width = Integer.parseInt(boardWidth.getCharacters().toString());
				int height = Integer.parseInt(boardHeight.getCharacters().toString());
				
				System.out.println("Creating board " + width + " x " + height);
				createNetworkGame(playerName.getText(), width, height);
			}
		});
		
		return createButton;
	}
	
	/**
	 * Constructs a join button.
	 * @return constructed button
	 */
	private Button getJoinButton() {
		joinButton = new Button("JOIN GAME");
		joinButton.setMinSize(100, 50);
		
		joinButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Looking for network games");
				PaperSoccer.getMainWindow().joinNetworkGame(playerName.getText());
			}
		});
		
		return joinButton;
	}

	
	/**
	 * Constructs an exit button.
	 * @return constructed button
	 */
	private Button getBackButton() {
		Button exitButton = new Button("BACK");
		exitButton.setMinSize(100, 50);
		
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
	 * Constructs an exit button.
	 * @return constructed button
	 */
	private Button getSettingsButton() {
		Button settingsButton = new Button("SETTINGS");
		settingsButton.setMinSize(100, 50);
		
		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				System.out.println("Settings");
				PaperSoccer.getMainWindow().showSettingsWindow();
			
			}
		});
		
		return settingsButton;
	}
	
	/**
	 * Adds start and exit buttons to the window.
	 * @return HBox with a buttons
	 */
	private HBox addSettingsBackButton() {
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(getSettingsButton(), getBackButton());
		
		return buttonBox;
	}
	
	/**
	 * Adds labels for text fields to grid.
	 * @param grid grid to place labels
	 */
	private void addPlayerLabel(GridPane grid) {
		Label player = new Label("Player's name:");
		
		player.setTextFill(Color.WHITE);
		
		grid.add(player, 0, 0);
	}
	
	/**
	 * Adds text fields to grid. 
	 * @param grid grid to place text fields
	 */
	private void addPlayerTextField(GridPane grid) {
		grid.add(playerName, 1, 0);

		playerName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
				if(newValue.matches("^(?=\\s*\\S).*$")){
					correctName.set(true);
					joinButton.setDisable(false);
					
					if(correctWidth.getText()=="" && correctHeight.getText()=="")
						createButton.setDisable(false);
				} else {
					correctName.set(false);
					joinButton.setDisable(true);
					createButton.setDisable(true);
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

						if(correctName.get() && correctHeight.getText()=="")
							createButton.setDisable(false);
					}
					else{
						correctWidth.setText("Incorrect value");
						createButton.setDisable(true);
					}
				} catch (Exception e){
					correctWidth.setText("Incorrect value");
					createButton.setDisable(true);
				}
			}
		});
		boardHeight.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
				try{
					if(!newValue.isEmpty() && Board.isValidHeight(Integer.parseInt(newValue))){
						correctHeight.setText("");
						
						if(correctName.get() && correctWidth.getText()=="")
							createButton.setDisable(false);
					}
					else{
						correctHeight.setText("Incorrect value");
						createButton.setDisable(true);
					}
				} catch (Exception e){
					correctHeight.setText("Incorrect value");
					createButton.setDisable(true);
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
		grid.setMinSize(300, 400);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(insets);
		
		addPlayerLabel(grid);
		addPlayerTextField(grid);
		addBoardSize(grid);
		grid.add(getCreateButton(), 0, 9);
		grid.add(getJoinButton(), 1, 9);
		
		return grid;
	}

	/**
	 * Begin new game for two players. Run controller, and
	 * display appropriate views.
	 *
	 * @param player name of the host player
	 * @param width width of the field
	 * @param height width of the board
	 *
	 * @author ljk
	 */
	private void createNetworkGame(final String player, final int width, final int height) {
		final Player host = new TwoPlayersGameWindow(playerName.getText(), Color.BLUE, Color.RED, true);
		final Player guest = new TwoPlayersGameWindow(playerName.getText(), Color.BLUE, Color.RED, true);
		

		new WaitForOpponentDialog().show();
		
		
		//TODO add connection with server and waiting for opponent
		
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