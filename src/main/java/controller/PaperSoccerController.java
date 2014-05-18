package controller;

import model.Game;
import model.IllegalMove;
import helpers.Move;
import helpers.Player;


/**
 * Class responsible for controlling information flow between model and views
 * @author wTendera
 */

public class PaperSoccerController {
	private Player host;
	private Player guest;
	private Player currentPlayer;
	private Game game;
	private int width;
	private int height;
	
	
	/**
	 * @param Player host, who will host game in on-line game
	 * @param Player guest, who will join existing games
	 * @param width of the map
	 * @param height of the map
	 */
	public PaperSoccerController(Player host, Player guest, int width, int height) {
		this.host = host;
		this.guest = guest;
		this.width = width;
		this.height = height;
		game = new Game(host, guest, width, height);
	}
	
	/**
	 * Function responsible for running a single game
	 */
	public void runGame() {
		host.startNewGame(width, height);
		guest.startNewGame(width, height);
	
		while(!game.isGameOver()) {
			currentPlayer = game.getCurrentPlayer();
			Move move = currentPlayer.getNextMove();
			if(game.isValidMove(move))  {
				try {
					game.registerMove(move);
					host.registerMove(move);
					guest.registerMove(move);
				} catch (IllegalMove e) { }
			}
		}
		host.finishGame(game.getResult(host));
		guest.finishGame(game.getResult(guest));		
	}
	
	/**
	 * Prepares game for next match.
	 */
	public void prepareRematch() {
		game.prepareRematch();
	}
}
