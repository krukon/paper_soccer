package ui;

import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

/**
 * Window displaying the field of the game. Responsible
 * for interaction with user. Can communicate with Controller
 * through implemented Player interface.
 * 
 * @author krukon, cianciara
 */
public class GameWindow extends Application implements Player {
	private static String windowName = "Paper Soccer";
	private static Player player;

	// UI private fields:

	private Stage stage;
	private Scene scene;
	private Group root;
	private Shape ball;
	private double pixelWidth = 400;
	private double pixelHeight = 600;
	private double gridSize;
    private final int addedToHeight = 2;
    private final int addedToWidth = 2;
    private Point lastUnderMouse = null;
    private Shape currentHighlightPoint = null;

	// Player private fields:

	private List<Move> moves;
	private boolean gameOver;
	private Point head;
	private Point click;
	private String playerName;
	private final Object syncMove = new Object();
	private int fieldWidth;
	private int fieldHeight;

	/**
	 * @deprecated
	 */
	public static void main(String[] args) {
		/*
		 * Example to show the logic of communication between
		 * Controller and this View. JavaFX thread needs some
		 * time in the beginning to start the window, and to
		 * register itself under GameWindow.player. The new
		 * thread is to mock Controller's behavior by sending
		 * moves to View and requesting next move from the View.
		 * 
		 * TO BE DELETED!
		 * 
		 */
		
		(new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(4000);
					player.startNewGame(8, 10);
					Thread.sleep(2000);
					player.registerMove(new Move(new Point(0,0), new Point(1,1)));
					Thread.sleep(2000);
					player.registerMove(new Move(new Point(1,1), new Point(1,2)));
					Thread.sleep(2000);
					player.registerMove(new Move(new Point(1,2), new Point(0,3)));
					Thread.sleep(2000);
					player.registerMove(new Move(new Point(0,3), new Point(0,2)));
					Thread.sleep(2000);
					player.registerMove(new Move(new Point(0,2), new Point(-1,1)));
				} catch (InterruptedException e) { }

				final Move x = player.getNextMove();

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						player.registerMove(x);
					}
				});
			}
		})).start();
		launch(args);

	}

	/**
	 * @deprecated
	 */
	@Override
	public void start(final Stage primaryStage) {
		player = this;
		stage = primaryStage;
		root = new Group();
		scene = new Scene(root, pixelWidth, pixelHeight, Color.GREEN);
		stage.setTitle(windowName);
		stage.setScene(scene);
		stage.show();

		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				pixelWidth = newSceneWidth.doubleValue();
				prepareWindow();
				drawAllMoves();
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				pixelHeight = newSceneHeight.doubleValue();
				prepareWindow();
				drawAllMoves();
			}
		});
		root.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                try{
                    double mouseX = e.getX();
                    double mouseY = e.getY();
                    if(lastUnderMouse!=null && (lastUnderMouse.x!=getRelativeX(mouseX) || lastUnderMouse.y!=getRelativeY(mouseY))){
                        root.getChildren().remove(currentHighlightPoint);
                        currentHighlightPoint = new Circle(translateX(getRelativeX(mouseX)), translateY(getRelativeY(mouseY)), 4, Color.BLACK);
                        lastUnderMouse = new Point(getRelativeX(e.getX()), getRelativeY(e.getY()));
                        root.getChildren().add(currentHighlightPoint);
                    }
                    else if(lastUnderMouse==null){
                        getRelativeY(mouseY);
                        getRelativeX(mouseX);
                        lastUnderMouse = new Point(getRelativeX(mouseX), getRelativeY(mouseY));
                        currentHighlightPoint = new Circle(translateX(getRelativeX(mouseX)), translateY(getRelativeY(mouseY)), 4, Color.BLACK);
                        root.getChildren().add(currentHighlightPoint);
                    }
                } catch (Exception wrongPosition){
                    if(lastUnderMouse!=null){
                        root.getChildren().remove(currentHighlightPoint);
                    }
                }
            }
        });
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override

			public void handle(MouseEvent e) {
				// TODO Recover click's x and y coordinates from position of the mouse
                try{
                    click = new Point(getRelativeX(e.getX()), getRelativeY(e.getY()));
                    System.out.println(getRelativeX(e.getX()) + " " + getRelativeY(e.getY()));
                } catch (Exception wrongClick){
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
				// TODO Redraw field; Cleaning
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
				// TODO Display game result
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
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Notify player about his move
			}
		});
		try {
			synchronized (syncMove) { syncMove.wait(); }
			return new Move(head, click);
		} catch (Exception e) {
		} finally {
			click = null;
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// TODO Notify player about the end of his turn
				}
			});
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
		drawLine(Color.BLUE, move.start, move.end);
	}

	/**
	 * Calculate the dimensions of the field, and draw
	 * it on the window.
	 * 
	 * @author krukon
	 */
	private void prepareWindow() {
		gridSize = Math.min(pixelHeight / (fieldHeight + 4), pixelWidth / (fieldWidth + 4));
		Canvas canvas = new Canvas(pixelWidth, pixelHeight);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		drawGrid(gc);
		drawBounds(gc);
		root.getChildren().removeAll();
		root.getChildren().clear();
		root.getChildren().add(canvas);
		drawBall();
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
     * @return coordinate if possible, if not throws exception
     *
     * @author cianciara
     */
    private int getRelativeX(double x) throws Exception{
        for(int i = 0; i<=fieldWidth+2; i++){
            if(x <= ((double)i*gridSize + gridSize*0.4 + translateX(-(fieldWidth+2)/2)) &&
                    x >=((double)i*gridSize - gridSize*0.4 + translateX(-(fieldWidth+2)/2))){
                return (i-(fieldWidth+addedToWidth)/2);
            }
        }
        throw new Exception();
    }
    /**
     * Translate pixel offset value on the window into y coordinate (with (0, 0) point in center)
     * @param y y pixel offset
     * @return coordinate if possible, if not throws exception
     *
     * @author cianciara
     */
    private int getRelativeY(double y) throws Exception{
        for(int i = 0; i<=fieldHeight+2; i++){
            if(y <= ((double)i*gridSize + gridSize*0.4 + translateY((fieldHeight + 2) / 2)) &&
                    y >=((double)i*gridSize - gridSize*0.4)+translateY((fieldHeight + 2) / 2)){
                return -(i-(fieldHeight+addedToHeight)/2);
            }
        }
        throw new Exception();
    }
}
