package controller;	

import javafx.application.Platform;
import ui.MainWindow;


/**
 * Class serving basic tools to control user interface.
 * 
 * @author jakub
 *
 */

public class PaperSoccer {
	private static MainWindow mainWindow;
	public static final String sync = "SYNCHRONIZING STRING"; //object on which thread can synchronize, probably should be replaced
	
	public static void main(String[] args) throws InterruptedException {
		synchronized (sync) {
			newGame(args);
			sync.wait(); //waits until main window construction is finished
		}
		addMainMenu();
	}
	
	public static MainWindow getMainWindow() {
		return mainWindow;
	}
	
	/**
	 * Registers main window of the game.
	 * @param window window to register
	 */
	public static void registerWindowGame(MainWindow window) {
		mainWindow = window;
	}
	
	/**
	 * Starts new JavaFX thread and creates main window.
	 * @param args method main's args
	 */
	private static void newGame(final String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainWindow.startNewGame(args);
			}	
		}).start();
	}
	
	/**
	 * Adds menu to main window.
	 */
	private static void addMainMenu() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Run new task - menu.");
				mainWindow.showMenu();
			}
		});
	}
}
