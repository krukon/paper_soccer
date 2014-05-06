package ui;
import java.lang.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * Created by mateusz on 06.05.2014.
 */
public class DrawField extends Application {
    public static String windowName;
    public static int height, width;
    private Stage stage;
    private Scene scene;
    //private double xZero, yZero;
    private double pixelHeight = 600, pixelWidth = 400;
    private double crateSize;
    private Group root;

    public static void main(String[] args) {
        DrawField.width = 8;
        DrawField.height = 10;
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        stage = primaryStage;
        root = new Group();
        stage.setTitle(windowName);
        scene = new Scene(root, pixelWidth, pixelHeight, Color.GREEN);
        stage.setScene(scene);
        //xZero = width/2;
        //yZero = height/2;
        crateSize = Math.min(pixelHeight/((double)(DrawField.height+4)), pixelWidth/((double)(DrawField.width+4)));
        System.out.println(crateSize);
        stage.show();

        this.drawField();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                pixelWidth = newSceneWidth.doubleValue();
                crateSize = Math.min(pixelHeight/((double)(DrawField.height+4)), pixelWidth/((double)(DrawField.width+4)));
                root.getChildren().removeAll();
                root.getChildren().clear();
                drawField();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                pixelHeight = newSceneHeight.doubleValue();
                crateSize = Math.min(pixelHeight/((double)(DrawField.height+4)), pixelWidth/((double)(DrawField.width+4)));
                root.getChildren().removeAll();
                root.getChildren().clear();
                drawField();
            }
        });
    }

    private double translateX(int x) { //return pixel x dim
        return (pixelWidth/2)+crateSize*x;
    }

    /**
     *
     * @param y
     * @return pixel y dim
     */
    private double translateY(int y) {
        return (pixelHeight/2)-crateSize*y;
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        Line line = new Line(translateX(x1), translateY(y1), translateX(x2), translateY(y2));
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(2.5);
        root.getChildren().add(line);
        System.out.println(translateX(x1)+" " + translateY(y1)+" "+ translateX(x2)+ " "+ translateY(y2));
    }

    // drawField
    private void drawField() {
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

    private void drawPoints() {
        for(int i = -1; i<=width+1; i++){
            for(int j = -1; j<=height+1; j++){
                drawPoint(-width/2+i, -height/2+j);
            }
        }
    }

    // drawPoint
    private void drawPoint(int x, int y) {
        Line line = new Line(translateX(x), translateY(y), translateX(x), translateY(y));
        line.setStroke(Color.WHITE);
        if(x==0 && y==0) line.setStrokeWidth(4.0);
        else line.setStrokeWidth(2.0);
        root.getChildren().add(line);
    }
}
