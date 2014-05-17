package bots;

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

	public EasyBot() {}

	@Override
	public void startNewGame(int width, int height) {
		board = new Board(width, height);
		head = new Point(0, 0);
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
			if(board.canMoveTo(tmp.x, tmp.y) && (bestMove == null  ||  (getDistance(bestMove) > getDistance(bestMove))))
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
	
	
	private int getDistance(Point target) {
		return (target.x)^2 + (target.y - board.getHeight()/2)^2;
	}
}
