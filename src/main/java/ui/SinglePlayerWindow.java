package ui;

import java.util.concurrent.atomic.AtomicBoolean;

import bots.BotLoader;
import bots.BotLoader.BotLoaderException;
import helpers.Player;
import controller.PaperSoccer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import controller.PaperSoccerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Board;

public class SinglePlayerWindow extends BorderPane {
	private String botLoadingError = "Could not load specified bot.";
	private Text windowTitle = new Text("Enter your names \n and board size");
	private Insets insets = new Insets(25, 25, 25, 25);
	
	private TextField playerOneName;
	private TextField boardWidth;
	private TextField boardHeight;
	private Button startButton;
	private ComboBox<String> comboBox;
	private Label errorMessages;
	private final AtomicBoolean correctName = new AtomicBoolean(true);
	private final Label correctWidth = new Label("");
	private final Label correctHeight = new Label("");


	public SinglePlayerWindow() {
		setPrefSize(PaperSoccer.WIDTH, PaperSoccer.HEIGHT);
		setStyle("-fx-background-image: url('paper_soccer_background.jpg');");
		
		playerOneName = new TextField("Player");
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
			
			//TODO Showing board and size validation
			@Override
			public void handle(ActionEvent event) {				
				try {
					int width = Integer.parseInt(boardWidth.getCharacters().toString());
					int height = Integer.parseInt(boardHeight.getCharacters().toString());
					
					System.out.println("Constructing board " + width + " x " + height);
					startGame(playerOneName.getText(), comboBox.getValue(), width, height);
				} catch (Exception e) {
					System.out.println("Size is not an integer");
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
		Label difficulty = new Label("Difficulty:");
		
		playerOne.setTextFill(Color.WHITE);
		difficulty.setTextFill(Color.WHITE);
		
		grid.add(playerOne, 0, 0);
		grid.add(difficulty, 0, 1);
	}
	
	/**
	 * Adds text fields to grid. 
	 * @param grid grid to place text fields
	 */
	private void addPlayersTextFields(GridPane grid) {
		grid.add(playerOneName, 1, 0);
		
		playerOneName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
				if(newValue.matches("^(?=\\s*\\S).*$")){
					correctName.set(true);
					
					if(correctWidth.getText()=="" && correctHeight.getText()=="")
						startButton.setDisable(false);
				} else {
					correctName.set(false);
					startButton.setDisable(true);
				}
			}
		});

	}

	/**
	 * Adds menu to choose bot difficulty
	 * @param grid grid to place drop-down menu
	 */
	private void addBotDifficultyMenu(GridPane grid) {
		ObservableList<String> options = FXCollections.observableArrayList(BotLoader.getBotsIds());
		comboBox = new ComboBox<>(options);
		comboBox.setValue(options.get(0));
		comboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				errorMessages.setText("");
			}
		});;
		grid.add(comboBox, 1, 1);
		errorMessages = new Label();
		errorMessages.setTextFill(Color.RED);
		grid.add(errorMessages, 0, 2, 2, 1);
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
						
						if(correctName.get() && correctWidth.getText()=="")
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
		
		addPlayersLabels(grid);
		addPlayersTextFields(grid);
		addBotDifficultyMenu(grid);
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
	 */
	private void startGame(final String playerOne, final String bot, final int width, final int height) {
		GameWindow view = new GameWindow(playerOne, Color.BLUE, Color.RED);
		final Player host = view;
		final Player guest;
		try {
			guest = BotLoader.loadBot(bot);
		} catch (BotLoaderException e) {
			errorMessages.setText(botLoadingError);
			return;
		}
		PaperSoccer.getMainWindow().showSinglePlayerGameWindow(view);

		Thread controllerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				PaperSoccerController controller = new PaperSoccerController(host, guest, width, height);
				((GameWindow) host).registerController(controller);
				controller.runGame();
			}
		});
		controllerThread.setDaemon(true);
		controllerThread.start();
	}
}
