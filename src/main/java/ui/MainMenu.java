package ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


public class MainMenu extends Application {
	
	public static void main(String[] args) {
		Application.launch(MainMenu.class, args);
		}
	
	@Override
	public void start(Stage stage) throws Exception {
		GridPane grid = new GridPane();
		grid.setPrefSize(400, 550);
		grid.setAlignment(Pos.CENTER);
		grid.add(generateTitleLabel(), 0, 0);
		grid.add(generateTwoPlayersButton(), 0, 1);
		grid.add(generateExitButton(), 0, 6);
		Scene scene = new Scene(grid);
		stage.setTitle("Paper Soccer");
		grid.setStyle("-fx-background-color: GREEN;");
		//grid.setStyle("-fx-background-image: url('menuBackground.jpg');");
		stage.setScene(scene);
		stage.show();
	}
	
	private HBox generateExitButton() {
		Button exit = new Button("EXIT");
		exit.setMinSize(150,60);

		exit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Exit");
				System.exit(0);
			}
		});
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(exit);
		buttonBox.setPadding(new Insets(10, 10, 10, 10));;
		return buttonBox;

	}

	private HBox generateTwoPlayersButton() {
		Button play = new Button("TWO PLAYERS");
		play.setMinSize(150,60);
		
		play.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Play pressed");
				
			}
		});
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(play);
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
		return buttonBox;
	}

	private HBox generateTitleLabel() {
		Label label = new Label("Paper Soccer");
		label.setFont(Font.font("Arial", 40));
		label.setTextFill(Color.WHITE);

		HBox labelBox = new HBox();
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(label);
		labelBox.setPadding(new Insets(10, 10, 100, 10));
		return labelBox;
	}
}
