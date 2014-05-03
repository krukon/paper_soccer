package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {

	@Test
	public void correctBoardDimensions() {
		Board board = new Board(8, 10);
		assertEquals(8, board.getWidth());
		assertEquals(10, board.getHeight());
	}

	@Test
	public void incorrectBoardDimensions() {
		Board board = new Board(9, 11);
		assertEquals(8, board.getWidth());
		assertEquals(10, board.getHeight());
	}

	@Test
	public void minimalBoardDimensions() {
		Board board = new Board(2, 2);
		assertEquals(4, board.getWidth());
		assertEquals(4, board.getHeight());
	}

	@Test
	public void maximalBoardDimensions() {
		Board board = new Board(100000, 100000);
		assertEquals(100, board.getWidth());
		assertEquals(100, board.getHeight());
	}

}
