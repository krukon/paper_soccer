package ui;

import controller.PaperSoccer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class representing main window of the game.
 * 
 * @author jakub
 *
 */

public class MainWindow extends Application {
	private enum GameState {
		SINGLE_PLAYER, BOTS_TOURNAMENT, TWO_PLAYERS_OFFLINE, TWO_PLAYERS_ONLINE, OTHER
	}
	
	private Group mainView;
	private GameState state;
	private Thread controllerThread;
	private Thread chatThread;
	private String gameID;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainView = new Group();
		
		Scene scene = new Scene(mainView, PaperSoccer.WIDTH, PaperSoccer.HEIGHT, Color.GREEN);
		
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if (state == GameState.OTHER) return;
				
				System.out.println("Button pressed " + event.getCode());

				if (event.getCode() == KeyCode.ESCAPE) {
					try {
						if (state == GameState.TWO_PLAYERS_ONLINE) {
							unsubscribeFromGame();
						}
						else {
							controllerThread.stop();
						}
					} catch (Exception e) {}
					
					System.out.println("Escape pressed - back to main menu");
					PaperSoccer.getMainWindow().showMenu();
				}
			}
		});
		
		primaryStage.setTitle("Paper soccer");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		PaperSoccer.registerWindowGame(this);
		synchronized (PaperSoccer.sync) { PaperSoccer.sync.notifyAll(); } //notifies PaperSoccer that window construction is finished
	}
	
	public void unsubscribeFromGame() {
		PaperSoccer.server.unsubcribeFromChat();
		PaperSoccer.server.unsubcribeFromGame();
		PaperSoccer.server.unsubcribeFromSession();
		controllerThread.interrupt();
		chatThread.interrupt();
		PaperSoccer.server.closeGame(gameID);
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				PaperSoccer.getMainWindow().showMenu();
			}
		});
	}
	
	/**
	 * Starts new window.
	 * @param args PaperSoccer's main method args
	 */
	public static void startNewGame(final String[] args) {
		launch(args);
	}
	
	/**
	 * Shows the menu.
	 */
	public void showMenu() {
		state = GameState.OTHER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(new MainMenu());
	}
	
	/**
	 * Shows two players window.
	 */
	public void showTwoPlayersWindow() {
		state = GameState.OTHER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(new TwoPlayersWindow());
	}
	
	public void showTwoPlayersGameWindow(GameWindow host, GameWindow guest) {
		state = GameState.TWO_PLAYERS_OFFLINE;
		
		mainView.getChildren().clear();
		mainView.getChildren().addAll(host, guest);
		host.setVisible(false);
	}
	
	public void changeTopGameWindow(GameWindow window) {
		if (mainView.getChildren().get(1) != window) {
			mainView.getChildren().get(0).setVisible(true);
			mainView.getChildren().remove(0);
			mainView.getChildren().add(window);
			mainView.getChildren().get(0).setVisible(false);
		}
	}
	
	public void showSinglePlayerWindow() {
		state = GameState.OTHER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(new SinglePlayerWindow());
	}
	
	public void showSinglePlayerGameWindow(GameWindow host) {
		state = GameState.SINGLE_PLAYER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(host);
	}
	
	public void showBotsTournametWindow() {
		state = GameState.OTHER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(new BotsTournamentWindow());
	}
	
	public void showBotsTournametGameWindow(GameWindow spectator) {
		state = GameState.BOTS_TOURNAMENT;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(spectator);
	}

	public void showNetworkWindow() {
		state = GameState.OTHER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(new NetworkWindow());
	}
	

	public void showSettingsWindow() {
		state = GameState.OTHER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(new SettingsWindow());
	}

	public void joinNetworkGame(String player) {
		state = GameState.OTHER;
		
		mainView.getChildren().clear();
		mainView.getChildren().add(new GamesListWindow(player));
		
	}
	
	public void showHelpWindow() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				new HelpWindow().show();
			}
		});
	}
	
	public void registerOnlineGameState() {
		state = GameState.TWO_PLAYERS_ONLINE;
	}
	
	public void registerControllerThread(Thread controllerThread) {
		this.controllerThread = controllerThread;
	}
	

	public void registerChatThread(Thread chatThread) {
		this.chatThread = chatThread;
	}
	
	public void registerGameID(String gameID) {
		this.gameID = gameID;
	}
}
