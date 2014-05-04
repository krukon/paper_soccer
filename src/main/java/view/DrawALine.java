package view;

/*
 * @author Wiktor Tendera
 * 
 * Class responsible for drawing single line
 * 
 * While making x1 equals x2 after drawing a line it get confused
 * 
 * To correct
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
 
public class DrawALine extends Application {
	private double x1 = 150, y1 = 200, x2 = 0, y2 = 0;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Click on map to draw a line");
        final StackPane root = new StackPane();        
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	
			@Override
			public void handle(MouseEvent event) {
				//toDo get some space around
				x2 = event.getX();
				y2 = event.getY();
				
				//toDo check if move is valid
				Line line = new Line(x1, y1 , x2, y2);
				line.setStroke(Color.DARKGREEN);
				line.setStrokeWidth(2.5);
				
				root.getChildren().add(line);
				x1 = x2;
				y1 = y2;
			}

        	
		});
        
        // test why line is always centered
        // ???????????????????????????
        
        root.getChildren().add(new Line(10, 10, 10, 390));
        root.getChildren().add(new Line(10, 390, 290, 390));
        root.getChildren().add(new Line(290, 390, 290, 10));
        root.getChildren().add(new Line(290, 10, 10, 10));
        
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.show();
    }
}
