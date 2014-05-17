package ui;

import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

import java.util.ArrayList;
import java.util.List;

import controller.PaperSoccer;
import controller.PaperSoccerController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Window displaying the field of the game. Responsible
 * for interaction with user. Can communicate with Controller
 * through implemented Player interface.
 * 
 * @author krukon, cianciara
 */
public class GameWindow extends BorderPane implements Player {

	// UI private fields:

	private Group root;
	private Shape ball;
	private double pixelWidth = 400;
	private double pixelHeight = 600;
	private double gridSize;
    private Shape currentHighlightPoint = null;
	private final double errorMargin = 0.2;

	// Player private fields:

	private List<Move> moves;
	private int fieldWidth;
	private int fieldHeight;	
	private Color playerColor;
	private Color opponentColor;
	
	//Player protected fields:
	
	protected Player firstPlayer = this;
	protected Player secondPlayer;
	protected boolean isHost = true;
	protected boolean gameOver;
	protected PaperSoccerController controller;
	protected Point head;
	protected Point click;
	protected String playerName;
	protected final Object syncMove = new Object();
	protected Label currentPlayer;

	/**
	 * Construct a view for the game
	 * 
	 * @param playerName the name of the player
	 */
	public GameWindow(String playerName, Color playerColor, Color opponentColor) {
		if (playerName == null || playerName.length() == 0) playerName = "PLAYER";
		
		this.playerName = playerName;
		this.playerColor = playerColor;
		this.opponentColor = opponentColor;
		
		currentPlayer = new Label();
		currentPlayer.setTextFill(playerColor);
		currentPlayer.setFont(Font.font(30));
		setAlignment(currentPlayer, Pos.CENTER);
		setTop(currentPlayer);
		
		root = new Group();
		this.setCenter(root);
		root.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
				double mouseX = e.getX();
				double mouseY = e.getY();
				Integer relativeX = getRelativeX(mouseX);
				Integer relativeY = getRelativeY(mouseY);
				if(relativeX!=null && relativeY!=null && isPointInField(relativeX, relativeY)){
					root.getChildren().remove(currentHighlightPoint);
					currentHighlightPoint = new Circle(translateX(relativeX), translateY(relativeY), 4, Color.BLACK);
					root.getChildren().add(currentHighlightPoint);
				}
				else {
					root.getChildren().remove(currentHighlightPoint);
				}
            }
        });
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if(getRelativeX(e.getX())!=null && getRelativeY(e.getY())!=null){
					System.out.println(getRelativeX(e.getX())+" "+ getRelativeY(e.getY()));
					click = new Point(getRelativeX(e.getX()), getRelativeY(e.getY()));
				} else {
					click = null;
				}
				if (!gameOver && click != null) {
					synchronized (syncMove) { syncMove.notifyAll(); }
				}
			}
		});
	}

	/**
	 * Prepare window to start a new game on a specified field.
	 * 
	 * @param width the width of the field
	 * @param height the height of the field
	 * 
	 * @author krukon
	 */
	@Override
	public void startNewGame(int width, int height) {
		gameOver = false;
		moves = new ArrayList<>();
		head = new Point(0, 0);
		fieldWidth = width;
		fieldHeight = height;
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				prepareWindow();
			}
		});
	}

	/**
	 * Notify player about the end of the game.
	 * 
	 * @param result the result of the game
	 * 
	 * @author krukon
	 */
	@Override
	public void finishGame(final GameResult result) {
		gameOver = true;
		Platform.runLater(new Runnable() {
	
			@Override
			public void run() {
				new GameResultDialog(result, controller).show();
			}
		});
	}

	/**
	 * Request player to get his next move.
	 * 
	 * @return move made by player
	 * 
	 * @author krukon
	 */
	@Override
	public Move getNextMove() {
		try {
			synchronized (syncMove) { syncMove.wait(); }
			return new Move(head, click, this);
		} catch (Exception e) {
		} finally {
			click = null;
		}
		return null;
	}

	/**
	 * Draw specified move on the field.
	 * 
	 * @param move the move to be drawn
	 * 
	 * @author krukon
	 */
	@Override
	public void registerMove(final Move move) {
		moves.add(move);
		head = move.end;
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				drawMove(move);
			}
		});
	}
	
	/**
	 * Registers controller with which player communicates.
	 * @param controller the controller to register
	 */
	public void registerController(final PaperSoccerController controller) {
		this.controller = controller;
	}
	
	/**
	 * Become a spectator of the game between two players.
	 * 
	 * @author krukon
	 */
	public void watchGameBetween(Player first, Player second) {
		firstPlayer = first;
		secondPlayer = second;
	}

	/**
	 * Get player's name
	 * 
	 * @author krukon
	 */
	@Override
	public String getName() {
		return playerName;
	}

	/**
	 * Draw all lines representing moves registered by the player
	 * 
	 * @author krukon
	 */
	private void drawAllMoves() {
		if (moves != null)
			for (Move move : moves)
				drawMove(move);
	}
	
	/**
	 * Draw the pointer showing current position of the ball
	 */
	private void drawBall() {
		if (head != null) {
			root.getChildren().remove(ball);
			ball = new Circle(translateX(head.x), translateY(head.y), 4, Color.GOLD);
			root.getChildren().add(ball);
		}
	}

	/**
	 * Draw the bounds of the field.
	 * 
	 * @param gc GraphicsContext to draw on
	 * 
	 * @author krukon
	 */
	private void drawBounds(GraphicsContext gc) {
		int x = fieldWidth / 2,
				y = fieldHeight / 2;
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2.5);
		double cornersX[] = { -x, -1, -1, 1, 1, x, x, 1, 1, -1, -1, -x };
		double cornersY[] = { y, y, y + 1, y + 1, y, y, -y, -y, -y - 1, -y - 1, -y, -y};
		for (int i = 0; i < 12; i++) {
			cornersX[i] = translateX((int) cornersX[i]);
			cornersY[i] = translateY((int) cornersY[i]);
		}
		gc.strokePolygon(cornersX, cornersY, 12);
		gc.setFill(Color.WHITE);
		gc.fillOval(translateX(0) - 4, translateY(0) - 4, 8, 8);
	}

	/**
	 * Draw a grid under the field
	 * 
	 * @param gc GraphicsContext to draw on
	 * 
	 * @author krukon
	 */
	private void drawGrid(GraphicsContext gc) {
		int maxX = fieldWidth / 2,
				maxY = fieldHeight / 2;
		gc.setStroke(Color.rgb(255, 255, 255, 0.3));
		for (int x = -maxX - 1; x <= maxX + 1; x++)
			strokeLine(gc, x, -maxY - 1, x, maxY + 1);
		for (int y = -maxY - 1; y <= maxY + 1; y++)
			strokeLine(gc, -maxX - 1, y, maxX + 1, y);
	}

	/**
	 * Draw a line from (x1, y1) to (x2, y2) on the field
	 * 
	 * @param color color of the line
	 * 
	 * @author cianciara
	 */
	private void drawLine(Color color, int x1, int y1, int x2, int y2) {
		Line line = new Line(translateX(x1), translateY(y1), translateX(x2), translateY(y2));
		line.setStroke(color);
		line.setStrokeWidth(2.5);
		root.getChildren().add(line);
	}

	/**
	 * Draw a line from start to end on the field
	 * @param color color of the line
	 * @param start starting point of the line
	 * @param end ending point of the line
	 * 
	 * @author krukon
	 */
	private void drawLine(Color color, Point start, Point end) {
		drawLine(color, start.x, start.y, end.x, end.y);
		drawBall();
	}

	/**
	 * Draw a line on the field representing specified move
	 * 
	 * @param move the move to be drawn
	 * 
	 * @author krukon
	 */
	private void drawMove(Move move) {
		drawLine(move.player == firstPlayer ? playerColor : opponentColor, move.start, move.end);
	}

	/**
	 * Calculate the dimensions of the field, and draw
	 * it on the window.
	 * 
	 * @author krukon
	 */
	private void prepareWindow() {
		pixelWidth = getScene().getWidth();
		pixelHeight = getScene().getHeight();
		gridSize = Math.min(pixelHeight / (fieldHeight + 4), pixelWidth / (fieldWidth + 4));
		Canvas canvas = new Canvas(pixelWidth, pixelHeight);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		drawGrid(gc);
		drawBounds(gc);
		root.getChildren().removeAll();
		root.getChildren().clear();
		root.getChildren().add(canvas);
		colorGoals();
		drawBall();
	}

	private void colorGoals() {
		int y = fieldHeight / 2;
		Color upper = isHost ? opponentColor : playerColor,
			lower = isHost ? playerColor : opponentColor;
		drawLine(upper, -1, y, -1, y + 1);
		drawLine(upper, -1, y + 1, 1, y + 1);
		drawLine(upper, 1, y + 1, 1, y);
		drawLine(lower, -1, -y, -1, -y - 1);
		drawLine(lower, -1, -y - 1, 1, -y - 1);
		drawLine(lower, 1, -y - 1, 1, -y);
	}

	/**
	 * Draw a line from (x1, y1) to (x2, y2) on the canvas.
	 * Coordinates are associated with the field.
	 * 
	 * @param gc GraphicsContext to draw on
	 * 
	 * @author krukon
	 */
	private void strokeLine(GraphicsContext gc, int x1, int y1, int x2, int y2) {
		gc.strokeLine(translateX(x1), translateY(y1), translateX(x2), translateY(y2));
	}

	/**
	 * Translate x coordinate into pixel offset value on the window
	 * @param x x coordinate
	 * @return pixel position of x
	 * 
	 * @author cianciara
	 */
	private double translateX(int x) {
		return (pixelWidth / 2) + gridSize * x;
	}

	/**
	 * Translate y coordinate into pixel offset value on the window
	 * @param y y coordinate
	 * @return pixel position of y
	 * 
	 * @author cianciara
	 */
	private double translateY(int y) {
		return (pixelHeight / 2) - gridSize * y;
	}
    /**
     * Translate pixel offset value on the window into x coordinate (with (0, 0) point in center)
     * @param x x pixel offset
     * @return coordinate if possible, if not null
     *
     * @author cianciara
     */
    private Integer getRelativeX(double x){
        double result = (x-translateX(0))/gridSize;
		double low = Math.floor(result), high = Math.ceil(result);
		if(Math.abs(low-result)<errorMargin){
			return (int)low;
		} else if(Math.abs(high-result)<errorMargin){
			return (int)high;
		} else return null;
	}

    /**
     * Translate pixel offset value on the window into y coordinate (with (0, 0) point in center)
     * @param y y pixel offset
     * @return coordinate if possible, if not null
     *
     * @author cianciara
     */
    private Integer getRelativeY(double y){
      	double result = (y-translateY(0))/gridSize;
		double low = (Math.floor(result)), high = (Math.ceil(result));
		if(Math.abs(low-result)<errorMargin){
			return -(int)low;
		} else if(Math.abs(high-result)<errorMargin){
			return -(int)high;
		} else return null;
    }
	
	/**
	 * Checks if point with given coordinates is part of field
	 * @params x y point coordinates
	 * @return true if point is part of field
	 *
	 * @author cianciara
	 */
	private boolean isPointInField(int x, int y){
		return (Math.abs(x) <= fieldWidth/2 && Math.abs(y) <= fieldHeight/2) || (Math.abs(x) <= 1 && Math.abs(y) == fieldHeight/2 + 1);
	}
}
