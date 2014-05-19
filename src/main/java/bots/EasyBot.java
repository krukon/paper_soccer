package bots;

import java.util.Random;

import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

import model.Board;
import model.IllegalMove;

public class EasyBot implements Player {
	public static final String name = "Easy Bot";
	
	private Board board;
	private Point head;
	private boolean topGoal;
	private Random rg;

	public EasyBot() {}

	@Override
	public void startNewGame(int width, int height, boolean topGoal) {
		board = new Board(width, height);
		head = new Point(0, 0);
		this.topGoal = topGoal;
		rg = new Random();
	}

	@Override
	public void finishGame(GameResult result) {
		if (result.getWinner() == this)
			System.out.println(getName() + " is the best!");
	}

	@Override
	public Move getNextMove() {
		Point bestMove = null, tmp;
		Point head = new Point(board.getCurrentX(), board.getCurrentY());
		for(int i = 0; i < 8; i++) {
			tmp = Board.Direction.values()[i].moveFrom(head);
			int d1, d2;
			if(board.canMoveTo(tmp.x, tmp.y) && (bestMove == null  ||  (d1 = getDistance(tmp)) < (d2 = getDistance(bestMove)) || (d1 == d2 && rg.nextBoolean())))
				bestMove = tmp;
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) { }
		return new Move(head, bestMove, this);
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
	
	private int getMyGoalY() {
		return (topGoal ? 1 : -1) * (board.getHeight() / 2 +1);
	}
	
	private int getOpponentGoalY() {
		return -getMyGoalY();
	}
	
	private int getDistance(Point target) {
		return (target.x)*(target.x) + (target.y - getOpponentGoalY())*(target.y - getOpponentGoalY());
	}
}
