package helpers;

/**
 * Interface representing a player (human or computer).
 * @author jakub
 *
 */

public interface Player {
	
	/**
	 * Starts new game on the field with given width and size.
	 * @param width width of the field
	 * @param height height of the field
	 */
	void startNewGame(int width, int height);
	
	/**
	 * @return player's next move
	 */
	Move getNextMove();
	
	/**
	 * Notifies player about game result.
	 * @param result result of the current game
	 */
	void notifyGameResult(GameResult result);
	
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
