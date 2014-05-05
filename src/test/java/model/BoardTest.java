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

	@Test
	public void movementValidation() {
		Board board = new Board(8, 10);
		assertTrue(board.canMoveTo(1, 1));
		assertTrue(board.canMoveTo(0, 1));
		assertTrue(board.canMoveTo(1, 0));
		assertTrue(board.canMoveTo(-1, 1));

		assertFalse(board.canMoveTo(2, 2));
		assertFalse(board.canMoveTo(1, 100));
		assertFalse(board.canMoveTo(0, 0));
		assertFalse(board.canMoveTo(-100, -100));

		boolean thrown = false;
		try {
			board.moveTo(1, 1);
			assertFalse(board.canMoveTo(0, 0));
		} catch (IllegalMove e) { thrown = true; }
		assertFalse(thrown);
	}

	@Test
	public void makingMoves() {
		Board board = new Board(8, 10);
		boolean thrown = false;
		try {
			assertFalse(board.moveTo(1, 1));
			assertFalse(board.moveTo(1, 0));
			assertTrue(board.moveTo(0, 0));
		} catch (IllegalMove e) { thrown = true; }
		assertFalse(thrown);

		thrown = false;
		try {
			board.moveTo(1, 1);
		} catch (IllegalMove e) { thrown = true; }
		assertTrue(thrown);

		thrown = false;
		try {
			assertFalse(board.moveTo(-1, -1));
		} catch (IllegalMove e) { thrown = true; }
		assertFalse(thrown);
	}

	@Test
	public void boundsOfTheField() {
		Board board = new Board(4, 4);
		boolean thrown = false;
		try {
			assertFalse(board.moveTo(1, 0));
			assertTrue(board.moveTo(2, 0));
			assertFalse(board.canMoveTo(2, 1));
			assertFalse(board.canMoveTo(3, 1));
			assertFalse(board.canMoveTo(3, 0));
			assertFalse(board.canMoveTo(3, -1));
			assertFalse(board.canMoveTo(2, -1));
		} catch (IllegalMove e) { thrown = true; }
		assertFalse(thrown);
	}

	@Test
	public void scoringGoal() {
		Board board = new Board(4, 4);
		boolean thrown = false;
		try {
			board.moveTo(0, 1);
			board.moveTo(1, 2);
			assertFalse(board.isGameOver());
			board.moveTo(0, 3);
			assertTrue(board.isGameOver());
		} catch (IllegalMove e) { thrown = true; }
		assertFalse(thrown);
	}

	@Test
	public void getingStuckInCorner() {
		Board board = new Board(4, 4);
		boolean thrown = false;
		try {
			board.moveTo(0, 1);
			board.moveTo(1, 2);
			assertFalse(board.isGameOver());
			board.moveTo(0, 3);
			assertTrue(board.isGameOver());
		} catch (IllegalMove e) { thrown = true; }
		assertFalse(thrown);
	}

	@Test
	public void freezeGameWhenOver() {
		Board board = new Board(4, 4);
		boolean thrown = false;
		try {
			board.moveTo(0, 1);
			board.moveTo(1, 2);
			board.moveTo(0, 3);
			assertTrue(board.isGameOver());
			assertFalse(board.canMoveTo(0, 2));
		} catch (IllegalMove e) { thrown = true; }
		assertFalse(thrown);
		thrown = false;
		try {
			board.moveTo(0, 2);
		} catch (IllegalMove e) { thrown = true; }
		assertTrue(thrown);
	}
}
