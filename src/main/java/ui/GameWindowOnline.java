package ui;

import helpers.GameResult;
import helpers.Player;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class GameWindowOnline extends GameWindow implements Player {
	private String gameID;

	public GameWindowOnline(String playerName, Color playerColor, Color opponentColor) {
		super(playerName, playerColor, opponentColor);
	}
	
	public void registerGameID(String gameID) {
		this.gameID = gameID;
	}
	
	@Override
	public void finishGame(final GameResult result) {
		gameOver = true;
		Platform.runLater(new Runnable() {
	
			@Override
			public void run() {
				new GameResultDialogOnline(result, controller, gameID).show();
			}
		});
	}
}
