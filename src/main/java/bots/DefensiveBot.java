package bots;

import java.util.Stack;
import java.util.concurrent.Callable;

import model.BotBoard;
import model.Board.Direction;
import model.IllegalMove;
import helpers.Point;

public final class DefensiveBot extends StrategyBot {
	public static final String name ="Defensive bot";

	private Stack<Point> currentMoves = new Stack<>();
	private Stack<Point> bestMoves = new Stack<>();
	
	@Override
	public String getName() {
		return name;
	}

	public DefensiveBot() {
		super();
		addStrategy(1, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeDefensiveStrategy();
				return null;
			}
		});
		addStrategy(0.00, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeOffensiveStrategy();
				return null;
			}
		});
		addStrategy(0.00, new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				computeRandomStrategy();
				return null;
			}
		});
	}
	
	private void computeDefensiveStrategy() {
		System.out.println("entered");
		currentMoves.clear();
		bestMoves.clear();
		currentMoves.push(board.getCurrent());
		backTrackDefensive();
		System.out.println("ended");
		for(int i = 0; i < bestMoves.size(); i++)
			nextMoves.add(bestMoves.get(i));
	}
	
	private void backTrackDefensive() {
		//System.out.println("btD" + currentMoves.lastElement().x + ", " + currentMoves.lastElement().y);
		if(board.availableMovesCount(currentMoves.get(currentMoves.size() - 1)) == 0) {
			if((currentMoves.size() > bestMoves.size()) && (bestMoves.size() == 0 || isAcceptableMove(currentMoves.get(currentMoves.size() - 1)))) {
				bestMoves.clear();
				copyStacks();
			}
		} else {
			for(Direction dir : Direction.values()) {
				Point target = dir.moveFrom(currentMoves.get(currentMoves.size() - 1));
				if(board.canMoveTo(target.x, target.y)) {
					try {
						board.moveTo(target.x, target.y);
					} catch (IllegalMove e) {}
					currentMoves.push(target);
					backTrackDefensive();
					try {
						board.moveBack();
					} catch (IllegalMove e) {}
				}
			}
		}
		
		currentMoves.pop();
	}
	
	private void copyStacks() {
		for(int i = currentMoves.size() - 1; i >= 0; i--)
			bestMoves.push(currentMoves.get(i));
	}
	
	private void computeOffensiveStrategy() {
		currentMoves.clear();
		bestMoves.clear();
		currentMoves.push(board.getCurrent());
		backTrackOffensive();
		for(int i = 0; i < bestMoves.size(); i++)
			nextMoves.add(bestMoves.get(i));
	}
	
	private int getDistance(Point target) {
		return (target.x)*(target.x) + (target.y - getOpponentGoalY())*(target.y - getOpponentGoalY());
	}


	private void backTrackOffensive() {
		if(board.availableMovesCount(currentMoves.get(currentMoves.size() - 1)) == 0) {
			Point best = bestMoves.get(bestMoves.size() - 1);
			Point curr = currentMoves.get(currentMoves.size() - 1);
			if((getDistance(curr) < getDistance(best)) && (bestMoves.size() == 0 || isAcceptableMove(currentMoves.get(currentMoves.size() - 1)))) {
				bestMoves.clear();
				copyStacks();
			}
		} else {
			for(Direction dir : Direction.values()) {
				Point target = dir.moveFrom(currentMoves.get(currentMoves.size() - 1));
				if(board.canMoveTo(target.x, target.y)) {
					try {
						board.moveTo(target.x, target.y);
					} catch (IllegalMove e) {}
					currentMoves.push(target);
					backTrackOffensive();
					try {
						board.moveBack();
					} catch (IllegalMove e) {}
				}
			}
		}
		currentMoves.pop();
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
		nextMoves.add(target);
	}
}
