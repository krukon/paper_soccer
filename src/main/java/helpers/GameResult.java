package helpers;

/**
 * Class representing result of the game.
 * 
 * @author jakub
 * 
 */

public class GameResult {
	private Player winner;
	private int myResult;
	private int opponentResult;
	
	/**
	 * @return the winner of the game
	 */
	public Player getWinner() {
		return winner;
	}
	
	/**
	 * @return result of current player
	 */
	public int getMyResult() {
		return myResult;
	}
	
	/**
	 * @return result of opponent
	 */
	public int getOpponentResult() {
		return opponentResult;
	}

}
