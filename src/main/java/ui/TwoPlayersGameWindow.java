package ui;

/**
 * Window displaying the field of the game in offline 
 * multiplayer mode. Can communicate with Controller through
 * implemented Player interface in parent class.
 * 
 * @author jakub
 */

import java.util.concurrent.atomic.AtomicBoolean;

import helpers.GameResult;
import helpers.Move;
import controller.PaperSoccer;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class TwoPlayersGameWindow extends GameWindow{
	
	private GameWindow opponentWindow;
	private GameWindow playerWindow;
	
	private static AtomicBoolean resultDialogShown;

	public TwoPlayersGameWindow(String playerName, Color playerColor, Color opponentColor) {
		super(playerName, playerColor, opponentColor);
		resultDialogShown = new AtomicBoolean(false);
	}
	
	/**
	 * Registers both players' game windows.
	 * @param opponentWindow opponent's game window
	 * @param playerWindow current player's game window
	 */
	public void registerWindows(GameWindow opponentWindow, GameWindow playerWindow) {
		this.opponentWindow = opponentWindow;
		this.playerWindow = playerWindow;
	}
	
	/**
	 * Requests player to get his next move and 
	 * sends task to keep current player's game window
	 * as a top view layer.
	 */
	@Override
	public Move getNextMove() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				currentPlayer.setText(playerName);
				PaperSoccer.getMainWindow().changeTopGameWindow(playerWindow);
			}
		});
		try {
			synchronized (syncMove) { syncMove.wait(); }
			return new Move(head, click, this);
		} catch (Exception e) {
		} finally {
			click = null;
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					currentPlayer.setText("");
					PaperSoccer.getMainWindow().changeTopGameWindow(opponentWindow);
				}
			});
		}
		return null;
	}
	
	/**
	 * Notify player about the end of the game.
	 * Shows only one game result dialog.
	 * @param result the result of the game
	 */
	@Override
	public void finishGame(final GameResult result) {
		gameOver = true;
		
		synchronized (syncMove) {
			if (!resultDialogShown.get()) {
				resultDialogShown.set(true);
				
				Platform.runLater(new Runnable() {
			
					@Override
					public void run() {
						new GameResultDialog(result, controller).show();
					}
				});
			}
			else resultDialogShown.set(false);
		}
	}
}
