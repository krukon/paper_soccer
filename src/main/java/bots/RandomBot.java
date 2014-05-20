package bots;

import java.util.Random;

import model.Board;
import model.Board.Direction;
import model.IllegalMove;
import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

public class RandomBot implements Player {
	
	private static final String name = "Random bot";
	
	private Board board;
	private Point head;
	private Random rg;
	private boolean topGoal;

	public RandomBot() {
		rg = new Random();
	}

	@Override
	public void startNewGame(int width, int height, boolean topGoal) {
		board = new Board(width, height);
		head = new Point(0, 0);
		this.topGoal = topGoal;
	}

	@Override
	public void finishGame(GameResult result) {
		if (result.getWinner() == this)
			System.out.println(getName() + " is the best!");
	}

	@Override
	public Move getNextMove() {
		Point target;
		do {
			target = randomDirection();
		} while (!isAcceptableMove(target));
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { }
		return new Move(head, target, this);
	}

	@Override
	public void registerMove(Move move) {
		try {
			if (!move.start.equals(head))
				throw new IllegalMove();
			board.moveTo(move.end.x, move.end.y);
			head = move.end;
		} catch (IllegalMove e) { }
	}

	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Return a movement in a random direction
	 */
	private Point randomDirection() {
		return Direction.values()[rg.nextInt(8)].moveFrom(head);
	}
	
	private boolean isAcceptableMove(Point target) {
		if (target.y == getMyGoalY())
			return false;
		return board.canMoveTo(target.x, target.y);
	}
	
	private int getMyGoalY() {
		return (topGoal ? 1 : -1) * (board.getHeight() / 2 + 1);
	}

}
