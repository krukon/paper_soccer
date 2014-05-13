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
    public static int maxHeight = 100, maxWidth = 100, minHeight = 4, minWidth = 4;
	public static enum Direction {
		N, NE, E, SE, S, SW, W, NW;

		private static int[] deltaX = {0, 1, 1, 1, 0, -1, -1, -1};
		private static int[] deltaY = {1, 1, 0, -1, -1, -1, 0, 1};

		/**
		 * Get direction of a move from (x1, y1) to (x2, y2)
		 *
		 * @param x1 x coordinate of start point
		 * @param y1 y coordinate of start point
		 * @param x2 x coordinate of end point
		 * @param y2 y coordinate of end point
		 * @return Direction of movement
		 */
		public static Direction getDirection(int x1, int y1, int x2, int y2) {
			for (int i = 0; i < 8; i++)
				if (x2 - x1 == deltaX[i] && y2 - y1 == deltaY[i])
					return Direction.values()[i];
			return null;
		}

		/**
		 * Get opposite direction
		 *
		 * @return opposite direction
		 */
		public Direction opposite() {
			return Direction.values()[(4 + this.ordinal()) % 8];
		}
	}
	
	private int width;
	private int height;
	private int field[][];
	private int headX;
	private int headY;
	private boolean gameOver;
	
	public Board(int width, int height) {
		this.width = width/2;
		this.height = height / 2;
		field = new int[2 * this.width + 1][2 * this.height + 3];
		boundField();
		headX = 0;
		headY = 0;
		gameOver = false;
	}
	
	/**
	 * Check if move to (x, y) is valid
	 *
	 * @param x x coordinate of end point
	 * @param y y coordinate of end point
	 * @return true if the move can be made
	 */
	public boolean canMoveTo(int x, int y) {
		Direction dir = Direction.getDirection(headX, headY, x, y);
		if (gameOver || dir == null || !isInBounds(x, y))
			return false;
		return (field[getX(headX)][getY(headY)] & (1 << dir.ordinal())) == 0;
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
	 * Check if the game is over
	 *
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Mark movement to point (x, y)
	 *
	 * @param x x coordinate of end point
	 * @param y y coordinate of end point
	 * @return true if this move will grant additional move to current player
	 * @throws IllegalMove
	 */
	public boolean moveTo(int x, int y) throws IllegalMove {
		if (!canMoveTo(x, y))
			throw new IllegalMove();
		boolean bounce = wasVisited(x, y);
		Direction dir = Direction.getDirection(headX, headY, x, y);
		setConnection(headX, headY, dir);
		setConnection(x, y, dir.opposite());
		headX = x;
		headY = y;
		if ((Math.abs(headX) <= 1 && Math.abs(headY) == height + 1) || field[getX(headX)][getY(headY)] == (1 << 8) - 1) {
			gameOver = true;
			return false;
		}
		return bounce;
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
		return height + y + 1;
	}

	/**
	 * Check if point (x, y) belongs to the field
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if the point is in the field bounds
	 */
	private boolean isInBounds(int x, int y) {
		return (Math.abs(x) <= width && Math.abs(y) <= height) || (Math.abs(x) <= 1 && Math.abs(y) == height + 1);
	}

	/**
	 * Check if the point (x, y) was visited
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return true if the point was visited
	 */
	private boolean wasVisited(int x, int y) {
		if (!isInBounds(x, y))
			return false;
		return field[getX(x)][getY(y)] > 0;
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
    /**
     * Returns true if dimension of the filed is valid.
     * Correct dimension should be an even integer
     * between values stored in minHeight and maxHeight
     *
     * @param height dimension to be validated
     * @return true if value is valid
     */
    public static boolean isValidHeight(int height){
        return (height >= Board.minHeight && height <= Board.maxHeight && (height % 2) == 0);
    }
    /**
     * Returns true if dimension of the filed is valid.
     * Correct dimension should be an even integer
     * between values stored in minWidth and maxWidth
     *
     * @param width dimension to be validated
     * @return true if value is valid
     */
    public static boolean isValidWidth(int width){
        return (width >= Board.minWidth && width <= Board.maxWidth && (width % 2) == 0);
    }
}
