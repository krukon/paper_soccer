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
	 */
	public boolean isValidMove(Move move) {
		if(move.start.x != board.getHeadX() || move.start.y != board.getHeadY())
			return false;	
		return board.canMoveTo(move.end.x, move.end.y);
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
	 *  Function responsible for returning Player who can move now;
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 *  Function responsible for swapping Players;
	 */
	private void swapPlayers() {
		currentPlayer = currentPlayer == host ? guest : host;
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
	 */
	public void registerMove(Move move) {
		if(!moveTo(move))
			swapPlayers();
		if(board.isGameOver()) {
			loser = currentPlayer;
			winner = loser == host ? guest : host;
		}
	}
	
	public GameResult getResult(Player player) {
		/*
		 * if(!board.gameOver)
		 * 	throw exception
		 */
		return new GameResult(winner, winner == player ? 1 : 0, winner != player ? 1 : 0);
	}
}
