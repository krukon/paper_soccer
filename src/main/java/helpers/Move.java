package helpers;

/**
 * Class representing a move.
 * 
 * @author jakub
 * 
 */

public class Move {
	public final Point start;
	public final Point end;
	
	public Move(Point start, Point end, Player player) {
		this.start = start;
		this.end = end;
		this.player = player;
	}
}
