package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class BotBoardTest {

	@Test
	public void movingBack() {
		BotBoard board = new BotBoard(8, 10);
		boolean thrown = false;
		try {
			board.moveTo(0, 1);
			assertEquals(0, board.headX);
			assertEquals(1, board.headY);
			assertEquals(1, board.field[board.getX(0)][board.getY(0)]);
			assertEquals(1 << 4,board.field[board.getX(0)][board.getY(1)]);
			board.moveBack();
			assertEquals(0, board.headX);
			assertEquals(0, board.headY);
			assertEquals(0, board.field[board.getX(0)][board.getY(0)]);
			assertEquals(0, board.field[board.getX(0)][board.getY(1)]);
		} catch (IllegalMove e) {
			e.printStackTrace();
			thrown = true;
		}
		assertFalse(thrown);
	}
	
	@Test
	public void movingBackLongMoves() {
		BotBoard board = new BotBoard(8, 10);
		boolean thrown = false;
		try {
			board.moveTo(0, 1);
			board.moveTo(1, 1);
			board.moveTo(2, 0);
			board.moveTo(1, 0);
			board.moveTo(0, 1);
			board.moveTo(-1, 1);
			board.moveBack();
			assertEquals(0, board.headX);
			assertEquals(1, board.headY);
			board.moveBack();
			assertEquals(1, board.headX);
			assertEquals(0, board.headY);
			board.moveBack();
			assertEquals(2, board.headX);
			assertEquals(0, board.headY);
			board.moveBack();
			assertEquals(1, board.headX);
			assertEquals(1, board.headY);
			board.moveBack();
			assertEquals(0, board.headX);
			assertEquals(1, board.headY);
			board.moveBack();
			assertEquals(0, board.headX);
			assertEquals(0, board.headY);

			assertEquals(0, board.field[board.getX(0)][board.getY(0)]);
			assertEquals(0, board.field[board.getX(0)][board.getY(1)]);
			assertEquals(0, board.field[board.getX(1)][board.getY(1)]);
			assertEquals(0, board.field[board.getX(2)][board.getY(0)]);
			assertEquals(0, board.field[board.getX(1)][board.getY(0)]);
			assertEquals(0, board.field[board.getX(-1)][board.getY(1)]);
		} catch (IllegalMove e) {
			e.printStackTrace();
			thrown = true;
		}
		assertFalse(thrown);
	}
	
	

}
