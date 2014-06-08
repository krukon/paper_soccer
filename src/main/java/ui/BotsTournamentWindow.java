package ui;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import bots.BotLoader;
import bots.BotLoader.BotLoaderException;
import controller.PaperSoccer;
import controller.PaperSoccerController;

import helpers.Player;

public class BotsTournamentWindow extends BorderPane {
	private String botLoadingError = "Could not load specified bot.";
	private Text windowTitle = new Text("");
	private Insets insets = new Insets(25, 25, 25, 25);
	
	private TextField boardWidth;
	private TextField boardHeight;
	private Button startButton;
	private ComboBox<String> botOne;
	private ComboBox<String> botTwo;
	private Label botOneErrorMessages;
	private Label botTwoErrorMessages;
	private final AtomicBoolean correctName = new AtomicBoolean(true);
	private final Label correctWidth = new Label("");
	private final Label correctHeight = new Label("");


	public BotsTournamentWindow() {
		setPrefSize(PaperSoccer.WIDTH, PaperSoccer.HEIGHT);
		setStyle("-fx-background-image: url('paper_soccer_background.jpg');");
		
		boardWidth = new TextField("8");
		boardHeight = new TextField("10");
		
		windowTitle.setFill(Color.WHITE);
		
		setPadding(insets);
		setTop(getWindowTitle());
		setCenter(getGridPane());
		
		setBottom(getStartBackButton());
	}
	
	/**
	 * Constructs window title.
	 * @return HBox with a title
	 */
	private HBox getWindowTitle() {
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
		startButton.setDefaultButton(true);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			//TODO Showing board and size validation
			@Override
			public void handle(ActionEvent event) {				
				try {
					int width = Integer.parseInt(boardWidth.getCharacters().toString());
					int height = Integer.parseInt(boardHeight.getCharacters().toString());
					
					System.out.println("Constructing board " + width + " x " + height);
					startGame(botOne.getValue(), botTwo.getValue(), width, height);
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
	private HBox getStartBackButton() {
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
	private void addBotsLabels(GridPane grid) {
		Label botOneLabel = new Label("First bot:");
		Label botTwoLabel = new Label("Second bot:");
		
		botOneLabel.setTextFill(Color.WHITE);
		botTwoLabel.setTextFill(Color.WHITE);
		
		grid.add(botOneLabel, 0, 0);
		grid.add(botTwoLabel, 0, 2);
	}

	/**
	 * Adds menu to choose bot difficulty
	 * @param grid grid to place drop-down menu
	 */
	private void addBotsComboBoxes(GridPane grid) {
		ObservableList<String> options1 = FXCollections.observableArrayList(BotLoader.getBotsIds());
		ObservableList<String> options2 = FXCollections.observableArrayList(BotLoader.getBotsIds());
		botOne = new ComboBox<>(options1);
		botTwo = new ComboBox<>(options2);
		botOne.setValue(options1.get(0));
		botTwo.setValue(options2.get(0));
		botOne.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				botOneErrorMessages.setText("");
			}
		});
		botTwo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				botTwoErrorMessages.setText("");
			}
		});
		grid.add(botOne, 1, 0);
		grid.add(botTwo, 1, 2);
		botOneErrorMessages = new Label();
		botOneErrorMessages.setTextFill(Color.RED);
		botTwoErrorMessages = new Label();
		botTwoErrorMessages.setTextFill(Color.RED);
		grid.add(botOneErrorMessages, 0, 1, 2, 1);
		grid.add(botTwoErrorMessages, 0, 3, 2, 1);
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
	private GridPane getGridPane() {
		GridPane grid = new GridPane();
		grid.setMinSize(300, 400);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(insets);
		//grid.setStyle("-fx-background-image: url('paper_soccer_background.jpg'); -fx-background-repeat: stretch;");
		
		addBotsLabels(grid);
		addBotsComboBoxes(grid);
		addBoardSize(grid);
		
		return grid;
	}

	/**
	 * Begin new game with two bots. Run controller, and
	 * display window following the game state.
	 *
	 * @param botOneName name of the first bot
	 * @param botTwoName name of the second bot
	 * @param width width of the field
	 * @param height width of the board
	 *
	 */
	private void startGame(final String botOneName, final String botTwoName, final int width, final int height) {
		final GameWindow view = new GameWindow("", Color.BLUE, Color.RED);
		view.registerNames(botOneName, botTwoName);
		try {
			final Player host = BotLoader.loadBot(botOneName);
			final Player guest = BotLoader.loadBot(botTwoName);
			
			PaperSoccer.getMainWindow().showSinglePlayerGameWindow(view);

			Thread controllerThread = new Thread(new Runnable() {

				@Override
				public void run() {
					PaperSoccerController controller = new PaperSoccerController(host, guest, width, height);
					((GameWindow) view).registerController(controller);
					controller.subscribe(view);
					view.watchGameBetween(host, guest);
					controller.runGame();
				}
			});
			PaperSoccer.getMainWindow().registerControllerThread(controllerThread);
			controllerThread.setDaemon(true);
			controllerThread.start();
			
		} catch (BotLoaderException e) {
			if (e.botName == botOneName)
				botOneErrorMessages.setText(botLoadingError);
			if (e.botName == botTwoName)
				botTwoErrorMessages.setText(botLoadingError);
		}
	}
}
