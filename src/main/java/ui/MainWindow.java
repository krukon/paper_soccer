package ui;

import controller.PaperSoccer;
import javafx.application.Application;
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
	private final int prefWidth = 400;
	private final int prefHeight = 550;
	
	private Group mainView;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainView = new Group();
		
		Scene scene = new Scene(mainView, prefWidth, prefHeight, Color.GREEN);
		
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

}
