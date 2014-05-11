package controller;

import model.Board;
import helpers.GameResult;
import helpers.Move;
import helpers.Player;


/*
 * Class responsible for controlling information flow between model and views
 * @author wTendera
 */

public class PaperSoccerController {
	Player host;
	Player guest;
	Player currentPlayer;
	int width;
	int height;
	
	/*
	 * TO BE DELETED!
	 */
	private static class Game {
		static boolean isGameOver() {
			return false;
		}
	}
	
	/*
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
		currentPlayer = host;
	}
	
	/*
	 *  Function responsible for registering move for players
	 *  and swapping them
	 */
	private void swapPlayers() {
		currentPlayer = currentPlayer == host ? guest : host;
	}
	
	/*
	 * Function responsible for running a single game
	 */
	public void runGame() {
		host.startNewGame(width, height);
		guest.startNewGame(width, height);
	
		while(!Game.isGameOver()) {
			Move move;
			move = currentPlayer.getNextMove();
			if(/*Board.isValidMove(move)*/ true) {
				host.registerMove(move);
				guest.registerMove(move);
				swapPlayers();
			}
		}
		
		Player loser = currentPlayer;
		Player winner = loser == host ? guest : host;

		GameResult gameResultHost = new GameResult(winner, winner == host ? 1 : 0, winner == guest ? 1 : 0);
		GameResult gameResultGuest = new GameResult(winner, winner == guest ? 1 : 0, winner == host ? 1 : 0);
		
		host.finishGame(gameResultHost);
		guest.finishGame(gameResultGuest);
		
	}
}
