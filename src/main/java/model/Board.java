package model;

/**
 * Class representing the field of the game
 * 
 * @author krukon
 *
 */
public class Board {
	
	/**
	 * Enum representing directions of movements on the field.
	 */
	public static enum Direction {
		N, NE, E, SE, S, SW, W, NW
	}
	
	private int width;
	private int height;
	private int field[][];
	
	public Board(int width, int height) {
		this.width = validSize(width) / 2;
		this.height = validSize(height) / 2;
		field = new int[2 * this.width + 1][2 * this.height + 1];
		boundField();
	}
	
	/**
	 * @return full height of the field
	 */
	public int getHeight() {
		return height * 2;
	}
	
	/**
	 * @return full width of the field
	 */
	public int getWidth() {
		return width * 2;
	}

	/**
	 * Sets bounds of the field, so that moves going out of
	 * the bounds will be considered illegal.
	 */
	private void boundField() {
		for (int y = -height; y <= height; y++) {
			setConnection(-width, y, Direction.N);
			setConnection(-width, y, Direction.NW);
			setConnection(-width, y, Direction.W);
			setConnection(-width, y, Direction.SW);
			setConnection(-width, y, Direction.S);
			setConnection(width, y, Direction.N);
			setConnection(width, y, Direction.NE);
			setConnection(width, y, Direction.E);
			setConnection(width, y, Direction.SE);
			setConnection(width, y, Direction.S);
		}
		for (int x = -width; x <= width; x++) {
			if (x != 0 && x != 1) {
				setConnection(x, height, Direction.W);
				setConnection(x, height, Direction.NW);
				setConnection(x, -height, Direction.W);
				setConnection(x, -height, Direction.SW);
			}
			if (x != 0) {
				setConnection(x, height, Direction.N);
				setConnection(x, -height, Direction.S);
			}
			if (x != -1 && x != 0) {
				setConnection(x, height, Direction.E);
				setConnection(x, height, Direction.NE);
				setConnection(x, -height, Direction.E);
				setConnection(x, -height, Direction.SE);
			}
		}
	}

	/**
	 * Get index of field array representing this coordinate
	 * 
	 * @param x x coordinate
	 * @return correct field index for x
	 */
	private int getX(int x) {
		return width + x;
	}

	/**
	 * Get index of field array representing this coordinate
	 * 
	 * @param y y coordinate
	 * @return correct field index for y
	 */
	private int getY(int y) {
		return height + y;
	}

	/**
	 * Check if point (x, y) belongs to the field
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if the point is in the field bounds
	 */
	private boolean isInBounds(int x, int y) {
		return Math.abs(x) <= width && Math.abs(y) <= height;
	}

	/**
	 * Mark a connection from point (x, y)
	 * 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param dir direction from the point
	 * @return true if the operation was successful
	 */
	private boolean setConnection(int x, int y, Direction dir) {
		if (!isInBounds(x, y))
			return false;
		field[getX(x)][getY(y)] |= 1 << dir.ordinal();
		return true;
	}

	/**
	 * Returns valid dimension for the filed.
	 * Correct dimension should be an even integer
	 * between 4 and 100
	 * 
	 * @param size dimension to be validated
	 * @return validated dimension of the field
	 */
	private int validSize(int size) {
		return Math.min(Math.max(4, (size / 2) * 2), 100);
	}
}
