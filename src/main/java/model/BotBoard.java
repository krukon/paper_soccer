package model;

import java.util.ArrayList;
import java.util.List;
import helpers.Point;

public class BotBoard extends Board {
	
	private List<Point> moves;

	public BotBoard(int width, int height) {
		super(width, height);
		moves = new ArrayList<>();
	}
	
	public int availableMovesCount(Point target) {
		if (!isInBounds(target.x, target.y))
			return 0;
		int val = field[getX(target.x)][getY(target.y)], result = 0;
		for (int i = 0; i < 8; i++)
			result += (val & (1 << i)) == 0 ? 1 : 0;
		return result;
	}
	
	public boolean isLockingMove(Point start, Point end) {
		return availableMovesCount(end) == 1;
	}
	
	@Override
	public boolean moveTo(int x, int y) throws IllegalMove {
		int x2 = headX, y2 = headY;
		boolean result = super.moveTo(x, y);
		moves.add(new Point(x2, y2));
		return result;
	}
	
	public void moveBack() throws IllegalMove {
		try {
			Point newHead = moves.remove(moves.size() - 1);
			Direction dir = Direction.getDirection(headX, headY, newHead.x, newHead.y);
			resetConnection(headX, headY, dir);
			resetConnection(newHead.x, newHead.y, dir.opposite());
			headX = newHead.x;
			headY = newHead.y;
			gameOver = false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalMove();
		}
	}
	
	/**
	 * Unmark a connection from point (x, y)
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param dir direction from the point
	 * @return true if the operation was successful
	 */
	protected boolean resetConnection(int x, int y, Direction dir) {
		if (!isInBounds(x, y))
			return false;
		field[getX(x)][getY(y)] &= ((1 << 8) - 1) - (1 << dir.ordinal());
		return true;
	}

}
