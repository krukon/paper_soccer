package bots;

import java.util.Stack;
import java.util.concurrent.Callable;

import model.Board.Direction;
import model.IllegalMove;

import helpers.Point;

public final class DefensiveBot extends StrategyBot {
	public static final String name ="Defensive bot";

	private Stack<Point> currentMoves = new Stack<>();
	private Stack<Point> bestMoves = new Stack<>();
	private boolean isWinningMove;
	
	@Override
	public String getName() {
		return name;
	}

	public DefensiveBot() {
		super();
		addStrategy(0.75, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeOffensiveStrategy();
				return null;
			}
		});
		addStrategy(0.20, new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				computeRandomStrategy();
				return null;
			}
		});
		addStrategy(0.05, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeDefensiveStrategy();
				return null;
			}
		});
	}
	
	private void computeDefensiveStrategy() {
		//System.out.println("Defensive");
		currentMoves.clear();
		bestMoves.clear();
		currentMoves.push(board.getCurrent());
		isWinningMove = false;
		backTrackDefensive();
		//if (isWinningMove)
		//	System.out.println("Winning (" + bestMoves.size() + ") :" + bestMoves);
		for(int i = 0; i < bestMoves.size(); i++)
			nextMoves.add(bestMoves.get(i));
	}
	
	private void backTrackDefensive() {
		Point current = board.getCurrent();
		for(Direction dir : Direction.permuteValues()) {
			Point target = dir.moveFrom(current);
			if(board.canMoveTo(target.x, target.y) && isAcceptableMove(target)) {
				try {
					boolean bounce = board.moveTo(target.x, target.y);
					currentMoves.push(target);
					if (bounce)
						backTrackDefensive();
					else
						copyStacksDefensive();
					try {
						board.moveBack();
					} catch (IllegalMove e) {}
					currentMoves.pop();
				} catch (IllegalMove e) { System.err.println("Moving to gone wrong!" + e.getMessage());  }
			}
		}
	}
	
	private void copyStacksDefensive() {
		if (isWinningMove)
			return;
		if(currentMoves.size() > bestMoves.size() || (currentMoves.size() == bestMoves.size() && rg.nextDouble() < 0.3)) {
			bestMoves.clear();
			for(int i = 1; i < currentMoves.size(); i++)
				bestMoves.push(currentMoves.get(i));
		}
		if (currentMoves.lastElement().y == getOpponentGoalY())
			isWinningMove = true;
	}
	
	private void copyStacksOffensive() {
		if (isWinningMove)
			return;
		int d1 = getDistance(currentMoves.lastElement()), d2;
		if(bestMoves.size() == 0 || d1 < (d2 = getDistance(bestMoves.lastElement())) || (d1 == d2 && rg.nextDouble() < 0.3)) {
			bestMoves.clear();
			for(int i = 1; i < currentMoves.size(); i++)
				bestMoves.push(currentMoves.get(i));
		}
		if (currentMoves.lastElement().y == getOpponentGoalY())
			isWinningMove = true;
	}
	
	private void computeOffensiveStrategy() {
		//System.out.println("Offensive");
		currentMoves.clear();
		bestMoves.clear();
		currentMoves.push(board.getCurrent());
		isWinningMove = false;
		backTrackOffensive();
		//if (isWinningMove)
		//	System.out.println("Winning (" + bestMoves.size() + ") :" + bestMoves);
		for(int i = 0; i < bestMoves.size(); i++)
			nextMoves.add(bestMoves.get(i));
	}
	
	private int getDistance(Point target) {
		return (target.x)*(target.x) + (target.y - getOpponentGoalY())*(target.y - getOpponentGoalY());
	}

	private void backTrackOffensive() {
		Point current = board.getCurrent();
		for(Direction dir : Direction.permuteValues()) {
			Point target = dir.moveFrom(current);
			if(board.canMoveTo(target.x, target.y) && isAcceptableMove(target)) {
				try {
					boolean bounce = board.moveTo(target.x, target.y);
					currentMoves.push(target);
					if (bounce)
						backTrackOffensive();
					else
						copyStacksOffensive();
					try {
						board.moveBack();
					} catch (IllegalMove e) {}
					currentMoves.pop();
				} catch (IllegalMove e) { System.err.println("Moving to gone wrong!" + e.getMessage());  }
			}
		}
	}
	
	
	private boolean isAcceptableMove(Point target) {
		boolean canMove = board.canMoveTo(target.x, target.y),
				ownGoal = target.y == getMyGoalY(),
				isLocking = board.isLockingMove(board.getCurrent(), target);
		if (!canMove || ownGoal || isLocking)
			return false;
		return true;
	}
	
	private void computeRandomStrategy() {
		//System.out.println("Random");
		Point target;
		do {
			target = Direction.values()[rg.nextInt(8)].moveFrom(board.getCurrent());
		} while (!isAcceptableMove(target) && board.availableMovesCount(board.getCurrent()) > 1);
		nextMoves.add(target);
	}
}
