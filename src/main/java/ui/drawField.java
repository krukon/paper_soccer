package ui;
import java.lang.*;
import java.util.ListIterator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * Created by mateusz on 06.05.2014.
 */
public class drawField extends Application{
    public static String windowName;
    public static int height, width;
    private Stage stage;
    private double xZero, yZero;
    private double pixelHeight = 600, pixelWidth = 400;
    private double crateSize;
    private Group root;
    public static void main(String[] args){
        drawField.width = 8;
        drawField.height = 10;
        launch(args);
    }
    @Override
    public void start(final Stage primaryStage) {
        stage = primaryStage;
        root = new Group();
        stage.setTitle(windowName);
        stage.setScene(new Scene(root, pixelWidth, pixelHeight));
        xZero = width/2;
        yZero = height/2;
        crateSize = Math.min(pixelHeight/((double)(drawField.height+4)), pixelWidth/((double)(drawField.width+2)));
        System.out.println(crateSize);
        stage.show();
        this.field();
    }
    private double translateX(int x){ //return pixel x dim
        return (pixelWidth/2)+crateSize*x;
    }
    private double translateY(int y){ //return pixel y dim
        return (pixelHeight/2)-crateSize*y;
    }
    private void drawLine(int x1, int y1, int x2, int y2){
        Line line = new Line(translateX(x1), translateY(y1), translateX(x2), translateY(y2));
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2.5);
        root.getChildren().add(line);//new Line(translateX(x1), translateY(y1), translateX(x2), translateY(y2)));
        System.out.println(translateX(x1)+" " + translateY(y1)+" "+ translateX(x2)+ " "+ translateY(y2));
    }
    private void field(){
        int x = width / 2;
        int y = height / 2;
        drawLine(-x, y, -1, y);
        drawLine(-1, y, -1, y + 1);
        drawLine(-1, y + 1, 1, y+1);
        drawLine(1, y+1, 1, y);
        drawLine(1, y, x, y);
        drawLine(x, y, x, -y);
        drawLine(x, -y, 1, -y);
        drawLine(1, -y, 1, -y-1);
        drawLine(1, -y-1, -1, -y-1);
        drawLine(-1, -y-1, -1, -y);
        drawLine(-1, -y, -x, -y);
        drawLine(-x, -y, -x, y);
        drawPoints();
    }

    private void drawPoints(){
        for(int i = 0; i<=width; i++){
            for(int j = 0; j<=height; j++){
                Point(-width/2+i, -height/2+j);
            }
        }
    }
    private void Point(int x, int y){
        Line line = new Line(translateX(x), translateY(y), translateX(x), translateY(y));
        line.setStroke(Color.BLUE);
        if(x==0 && y==0) line.setStrokeWidth(4.0);
        else line.setStrokeWidth(2.0);
        root.getChildren().add(line);
    }
}
