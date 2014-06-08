package model;

import helpers.GameResult;
import helpers.Move;
import helpers.Player;

public class Game {
	private Board board;
	private Player host;
	private Player guest;
	private Player currentPlayer;
	private Player winner;
	private Player loser;
	private int width;
	private int height;
	
	private int hostScore;
	private int guestScore;
	
	public Game(Player host, Player guest, int width, int height) {
		this.host = host;
		this.guest = guest;
		this.width = width;
		this.height = height;
		this.board = new Board(width, height);
		this.currentPlayer = host;
	}
	
	/**
	 *  Function responsible for returning if move is valid;
	 *  @param move to validate
	 * @throws IllegalMove 
	 */
	public boolean isValidMove(Move move) throws IllegalMove {
		if(move == null) throw new IllegalMove();
		if(move.start.x != board.getCurrentX() || move.start.y != board.getCurrentY())
			return false;
		return board.canMoveTo(move.end.x, move.end.y);
	}
	
	/**
	 *  Function responsible for returning Player who can move now;
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Function responsible for announcing end of the game
	 */
	public boolean isGameOver() {
		return board.isGameOver();
	}
	
	/**
	 * Function responsible for registering moves for players and
	 * swapping them if there is no bounce
	 * @param move to register
	 * @throws IllegalMove 
	 */
	public void registerMove(Move move) throws IllegalMove {
		if (!isValidMove(move))
			throw new IllegalMove();
		if(!moveTo(move))
			swapPlayers();
		if(board.isGameOver())
			registerResults();
	}
	
	private void registerResults() {
		if(board.getCurrentY() == height / 2 + 1)
			winner = host;
		else if (board.getCurrentY() == -height / 2 - 1)
			winner = guest;
		else {
			winner = currentPlayer;
			System.out.println("Zostales zapedzony w kozi rog");
		}
		loser = winner == host ? guest : host;
		currentPlayer = loser;
		
		if (winner == host) ++hostScore;
		else ++guestScore;
	}

	public GameResult getResult(Player player) {		
		return new GameResult(winner, player == host ? hostScore : guestScore, player == host ? guestScore : hostScore);
	}
	
	/**
	 * Prepares new board for next match.
	 */
	public void prepareRematch() {
		board = new Board(width, height);
	}

	/**
	 * Function responsible to notify board about move
	 * and returning if there is bounce
	 * @param move to register
	 * 
	 */
	private boolean moveTo(Move move) {
		try {
			return board.moveTo(move.end.x, move.end.y);
		} catch (IllegalMove e) {}
		return false;
	}

	/**
	 *  Function responsible for swapping Players;
	 */
	private void swapPlayers() {
		currentPlayer = currentPlayer == host ? guest : host;
	}
}
