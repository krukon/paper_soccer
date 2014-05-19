package helpers;

/**
 * Interface representing a player (human or computer).
 * 
 * @author jakub
 *
 */

public interface Player {
	
	/**
	 * Starts new game on the field with given width and height.
	 * @param width width of the field
	 * @param height height of the field
	 * @param topGoal true if player's goal is the top one
	 */
	void startNewGame(int width, int height, boolean topGoal);
	
	/**
	 * Notifies player about game result.
	 * @param result result of the current game
	 */
	void finishGame(GameResult result);

	/**
	 * Notifies player that he must wait for opponent in network game.
	 */
	void waitForOpponent();

	/**
	 * @return player's next move
	 */
	Move getNextMove();
	
	/**
	 * Registers move of the opponent.
	 * @param move move made by opponent
	 */
	void registerMove(Move move);
	
	/**
	 * @return name of the player
	 */
	String getName();
}
