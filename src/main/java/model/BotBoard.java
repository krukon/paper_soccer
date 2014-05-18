package model;

import helpers.Point;

public class BotBoard extends Board {

	public BotBoard(int width, int height) {
		super(width, height);
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
		boolean result = availableMovesCount(end) == 1;
		if (result)
			System.out.println("Lock: " + start.x +", " +start.y + " - " + end.x + ", " +end.y);
		return result;
		
	}

}
