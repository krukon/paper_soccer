package helpers;

/**
 * Class representing result of the game.
 * 
 * @author jakub
 * 
 */

public class GameResult {
	final private Player winner;
	final private int myResult;
	final private int opponentResult;
	
	public GameResult(Player winner, int myResult, int opponenResult) {
		this.winner = winner;
		this.myResult = myResult;
		this.opponentResult = opponenResult;
	}
	
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
