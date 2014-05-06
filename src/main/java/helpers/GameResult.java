package helpers;

/**
 * Class representing result of the game.
 * 
 * @author jakub
 * 
 */

public class GameResult {
	private final Player winner;
	private final int myResult;
	private final int opponentResult;
	
	public GameResult(Player winner, int myResult, int opponentResult) {
		this.winner = winner;
		this.myResult = myResult;
		this.opponentResult = opponentResult;
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
