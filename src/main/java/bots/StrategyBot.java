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
			if (after - before < 100)
				Thread.sleep(100 - after + before);
		} catch (InterruptedException e) {}
		System.out.println("moving to: " + nextMoves.poll().x + nextMoves.poll().y);
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
	
}
