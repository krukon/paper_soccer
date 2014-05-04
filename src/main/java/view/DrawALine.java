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
				x2 = event.getSceneX();
				y2 = event.getSceneY();

				root.getChildren().add(new Line(x1, y1 , x2, y2));
				//x1 = x2;
				//y1 = y2;
			}

        	
		});
        
        root.getChildren().add(new Line(10, 10, 10, 390));
        root.getChildren().add(new Line(10, 390, 290, 390));
        root.getChildren().add(new Line(290, 390, 290, 10));
        root.getChildren().add(new Line(290, 10, 10, 10));
        
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.show();
    }
}
