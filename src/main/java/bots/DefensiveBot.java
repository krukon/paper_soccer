package bots;

import java.util.concurrent.Callable;

import model.Board.Direction;

import helpers.Point;

public final class DefensiveBot extends StrategyBot {
	public static final String name ="Defensive bot";
	
	@Override
	public String getName() {
		return name;
	}

	public DefensiveBot() {
		super();
		addStrategy(0.65, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeDefensiveStrategy();
				return null;
			}
		});
		addStrategy(0.35, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeOffensiveStrategy();
				return null;
			}
		});
	}
	
	private void computeDefensiveStrategy() {
		computeRandomStrategy();
	}
	
	private void computeOffensiveStrategy() {
		computeRandomStrategy();
	}

	private boolean isAcceptableMove(Point target) {
		boolean canMove = board.canMoveTo(target.x, target.y),
				ownGoal = target.y == getMyGoalY(),
				isLocking = board.availableMovesCount(board.getCurrent()) == 1 ? false : board.isLockingMove(board.getCurrent(), target);
		if (!canMove || ownGoal || isLocking)
			return false;
		return true;
	}
	
	private void computeRandomStrategy() {
		Point target;
		do {
			target = Direction.values()[rg.nextInt(8)].moveFrom(board.getCurrent());
		} while (!isAcceptableMove(target));
		nextMoves.push(target);
	}
}
