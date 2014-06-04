package ui;

import controller.PaperSoccer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Class representing main window of the game.
 * 
 * @author jakub
 *
 */

public class MainWindow extends Application {
	
	private Group mainView;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainView = new Group();
		
		Scene scene = new Scene(mainView, PaperSoccer.WIDTH, PaperSoccer.HEIGHT, Color.GREEN);
		
		primaryStage.setTitle("Paper soccer");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		PaperSoccer.registerWindowGame(this);
		synchronized (PaperSoccer.sync) { PaperSoccer.sync.notifyAll(); } //notifies PaperSoccer that window construction is finished
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
		mainView.getChildren().clear();
		mainView.getChildren().add(new MainMenu());
	}
	
	/**
	 * Shows two players window.
	 */
	public void showTwoPlayersWindow() {
		mainView.getChildren().clear();
		mainView.getChildren().add(new TwoPlayersWindow());
	}
	
	public void showTwoPlayersGameWindow(GameWindow host, GameWindow guest) {
		mainView.getChildren().clear();
		mainView.getChildren().addAll(host, guest);
		changeTopGameWindow(host);
	}
	
	public void changeTopGameWindow(GameWindow window) {
		if (mainView.getChildren().get(1) != window) {
			mainView.getChildren().remove(0);
			mainView.getChildren().add(window);
		}
	}
	
	public void showSinglePlayerWindow() {
		mainView.getChildren().clear();
		mainView.getChildren().add(new SinglePlayerWindow());
	}
	
	public void showSinglePlayerGameWindow(GameWindow host) {
		mainView.getChildren().clear();
		mainView.getChildren().add(host);
	}
	
	public void showBotsTournametWindow() {
		mainView.getChildren().clear();
		mainView.getChildren().add(new BotsTournamentWindow());
	}
	
	public void showBotsTournametGameWindow(GameWindow spectator) {
		mainView.getChildren().clear();
		mainView.getChildren().add(spectator);
	}

	public void showNetworkWindow() {
		mainView.getChildren().clear();
		mainView.getChildren().add(new NetworkWindow());
	}
	

	public void showSettingsWindow() {
		mainView.getChildren().clear();
		mainView.getChildren().add(new SettingsWindow());
	}

	public void joinNetworkGame(String player) {
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
}
