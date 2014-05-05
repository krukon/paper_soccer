package sample;
/*
 * @author Mateusz Cianciara
 * 
 * Class responsible for drawing single line
 * 
 * To correct
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class DrawField extends Application {

    private double x0, y0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        DrawField.draw(primaryStage, 10, 8);
    }
    private static void draw(final Stage stage, final int height, final int width){
        final Group root = new Group();

        stage.setTitle("Field");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
        final double x0 = stage.getWidth()/2;
        final double y0 = stage.getHeight()/2;
        double windowHeight =  stage.getHeight();
        double windowWidth =  stage.getWidth();
        double gateStartX = ((width-2)/2)*windowWidth/width;
        double gateEndX = windowWidth-gateStartX;
        double topCornerY = y0-windowHeight/2+windowHeight/10;
        double bottomCornerY = y0+windowHeight/2-windowHeight/10;
        double leftX = x0-windowWidth/2+windowWidth/40;
        double rightX = x0+windowWidth/2-windowWidth/40;
        Canvas canvas = new Canvas(windowWidth, windowHeight);
        Line leftEdge = new Line(leftX, y0-windowHeight/2+windowHeight/10, x0-windowWidth/2+windowWidth/40, y0+windowHeight/2-windowHeight/10);
        Line rightEdge = new Line(rightX, y0-windowHeight/2+windowHeight/10, x0+windowWidth/2-windowWidth/40, y0+windowHeight/2-windowHeight/10);
        root.getChildren().add(leftEdge);
        root.getChildren().add(rightEdge);
        root.getChildren().add(new Line(x0-windowWidth/2+windowWidth/40, y0-windowHeight/2+windowHeight/10, gateStartX, y0-windowHeight/2+windowHeight/10));
        root.getChildren().add(new Line(gateStartX, topCornerY, gateStartX, topCornerY-((bottomCornerY-topCornerY)/(2*height))));
        root.getChildren().add(new Line(gateStartX, topCornerY-((bottomCornerY-topCornerY)/(2*height)), gateEndX, topCornerY-((bottomCornerY-topCornerY)/(2*height))));
        root.getChildren().add(new Line(gateEndX, topCornerY-((bottomCornerY-topCornerY)/(2*height)), gateEndX, topCornerY));
        root.getChildren().add(new Line(gateEndX, topCornerY, x0+windowWidth/2-windowWidth/40, y0-windowHeight/2+windowHeight/10));
        root.getChildren().add(new Line(x0-windowWidth/2+windowWidth/40, y0+windowHeight/2-windowHeight/10, gateStartX, bottomCornerY));
        root.getChildren().add(new Line(gateStartX, bottomCornerY, gateStartX, bottomCornerY+((bottomCornerY-topCornerY)/(height*2)) ));
        root.getChildren().add(new Line(gateStartX, bottomCornerY+((bottomCornerY-topCornerY)/(height*2)), gateEndX, bottomCornerY+((bottomCornerY-topCornerY)/(height*2))));
        root.getChildren().add(new Line(gateEndX, bottomCornerY+((bottomCornerY-topCornerY)/(height*2)), gateEndX, bottomCornerY));
        root.getChildren().add(new Line(gateEndX, bottomCornerY, x0+windowWidth/2-windowWidth/40, y0+windowHeight/2-windowHeight/10));
        for(int i = 0; i<=height; i++){
            root.getChildren().add(new Line(leftX, topCornerY+i*(bottomCornerY-topCornerY)/height, rightX, topCornerY+i*(bottomCornerY-topCornerY)/height));
        }
        for(int i = 0; i<width; i++){
            root.getChildren().add(new Line(leftX+i*(rightX-leftX)/width, topCornerY, leftX+i*(rightX-leftX)/width, bottomCornerY));
        }
        root.getChildren().add(canvas);
    }
}
