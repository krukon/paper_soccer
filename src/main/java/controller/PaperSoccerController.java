package controller;

import java.util.ArrayList;
import java.util.List;

import model.Game;
import model.IllegalMove;

import helpers.GameResult;
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
	
	private List<Player> spectators;
	
	
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
		spectators = new ArrayList<>();
		game = new Game(host, guest, width, height);
	}
	
	/**
	 * Function responsible for running a single game
	 */
	public void runGame() {
		host.startNewGame(width, height, false);
		guest.startNewGame(width, height, true);
		notifyStartGame(width, height);
		while(!game.isGameOver()) {
			currentPlayer = game.getCurrentPlayer();
			Move move = currentPlayer.getNextMove();
			if(game.isValidMove(move))  {
				try {
					game.registerMove(move);
					host.registerMove(move);
					guest.registerMove(move);
					notifyMove(move);
				} catch (IllegalMove e) { }
			}
		}
		host.finishGame(game.getResult(host));
		guest.finishGame(game.getResult(guest));
		notifyFinishGame(game.getResult(host));
		
	}
	
	/**
	 * Prepares game for next match.
	 */
	public void prepareRematch() {
		game.prepareRematch();
	}
	
	/**
	 * Subscribe to the controller to become a spectator
	 * of the game. This will allow all spectators to receive
	 * all notifications about the state of the game, such as
	 * start and end of the game, and movements of the players.
	 * 
	 * @param spectator subscriber
	 * @author krukon
	 */
	public void subscribe(Player spectator) {
		spectators.add(spectator);
	}
	
	/**
	 * Unsubscribe from the controller for receiving notifications
	 * about the game.
	 * 
	 * @param spectator subscriber
	 * @author krukon
	 */
	public void unsubscribe(Player spectator) {
		spectators.remove(spectator);
	}

	/**
	 * Notify all spectators about the end of the game.
	 * 
	 * @param result result of the game
	 * @author krukon
	 */
	private void notifyFinishGame(GameResult result) {
		for (Player e : spectators)
			e.finishGame(result);
	}

	/**
	 * Notify all spectators about the beginning of the new game.
	 * 
	 * @param width the width of the field
	 * @param height the height of the field
	 * @author krukon
	 */
	private void notifyStartGame(int width, int height) {
		for (Player e : spectators)
			e.startNewGame(width, height, true);
	}

	/**
	 * Notify all spectators about movement made by a player.
	 * 
	 * @param move movement to be registered
	 * @author krukon
	 */
	private void notifyMove(Move move) {
		for (Player e : spectators)
			e.registerMove(move);
	}
}
