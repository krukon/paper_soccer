package bots;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.Callable;

import model.BotBoard;
import model.IllegalMove;
import model.Board.Direction;
import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

public abstract class StrategyBot implements Player {
	private List<Double> strategyProbabilities = new ArrayList<>();
	private List<Callable<?>> strategies = new ArrayList<>();
	private Stack<Point> currentMoves = new Stack<>();
	private Stack<Point> bestMoves = new Stack<>();
	private boolean isWinningMove;
	
	protected int width;
	protected int height;
	protected boolean topGoal;
	protected Random rg = new Random();
	protected Queue<Point> nextMoves = new LinkedList<>();
	protected BotBoard board;

	@Override
	public void startNewGame(int width, int height, boolean topGoal) {
		this.width = width;
		this.height = height;
		this.topGoal = topGoal;
		board = new BotBoard(width, height);
	}

	@Override
	public void finishGame(GameResult result) {
	}

	@Override
	public final Move getNextMove() {
		long before, after;
		before = System.currentTimeMillis();
		while (nextMoves.isEmpty()) {
			chooseStrategy();
		}
		after = System.currentTimeMillis();
		try {
			if (after - before < 200)
				Thread.sleep(200 - after + before);
		} catch (InterruptedException e) {}
		//System.out.println("moving to: " + nextMoves.peek());
		return new Move(board.getCurrent(), nextMoves.poll(), this);
	}

	@Override
	public final void registerMove(Move move) {
		try {
			if (!move.start.equals(board.getCurrent()))
				throw new IllegalMove();
			board.moveTo(move.end.x, move.end.y);
		} catch (IllegalMove e) { }
	}
	
	protected void computeOffensiveStrategy() {
		currentMoves.clear();
		bestMoves.clear();
		currentMoves.push(board.getCurrent());
		isWinningMove = false;
		backTrackOffensive();
		for(int i = 0; i < bestMoves.size(); i++)
			nextMoves.add(bestMoves.get(i));
	}
	
	protected void computeDefensiveStrategy() {
		currentMoves.clear();
		bestMoves.clear();
		currentMoves.push(board.getCurrent());
		isWinningMove = false;
		backTrackDefensive();
		for(int i = 0; i < bestMoves.size(); i++)
			nextMoves.add(bestMoves.get(i));
	}
	
	protected void computeRandomStrategy() {
		Point target;
		do {
			target = Direction.values()[rg.nextInt(8)].moveFrom(board.getCurrent());
		} while (!isAcceptableMove(target) && board.availableMovesCount(board.getCurrent()) > 1);
		nextMoves.add(target);
	}
	
	protected int getMyGoalY() {
		return (topGoal ? 1 : -1) * (board.getHeight() / 2 +1);
	}
	
	protected int getOpponentGoalY() {
		return -getMyGoalY();
	}
	
	protected final void addStrategy(double probability, Callable<?> startegy) {
		strategyProbabilities.add(probability);
		strategies.add(startegy);
	}

	protected final void chooseStrategy() {
		double p = rg.nextDouble(), sum = 0.0;
		for (int i = 0; i < strategies.size(); i++) {
			if (p < (sum += strategyProbabilities.get(i)))
				try {
					strategies.get(i).call();
					return;
				} catch (Exception e) {}
		}
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
		if((bestMoves.size() == 0) || ((currentMoves.size() > bestMoves.size()) || (currentMoves.size() == bestMoves.size() && rg.nextDouble() < 0.3)) &&
				(getDistance(currentMoves.lastElement()) <= getDistance(currentMoves.firstElement()))) {
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
		if(bestMoves.isEmpty() || d1 < (d2 = getDistance(bestMoves.lastElement())) || (d1 == d2 && rg.nextDouble() < 0.3)) {
			bestMoves.clear();
			for(int i = 1; i < currentMoves.size(); i++)
				bestMoves.push(currentMoves.get(i));
		}
		if (currentMoves.lastElement().y == getOpponentGoalY())
			isWinningMove = true;
	}
	
	
	
	private int getDistance(Point target) {
		return (target.x)*(target.x) + (target.y - getOpponentGoalY())*(target.y - getOpponentGoalY());
	}

	private void backTrackOffensive() {
		Point current = board.getCurrent();
		for(Direction dir : Direction.permuteValues()) {
			Point target = dir.moveFrom(current);
			if((board.canMoveTo(target.x, target.y) && (isAcceptableMove(target) || bestMoves.isEmpty()))) {
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
	
}
