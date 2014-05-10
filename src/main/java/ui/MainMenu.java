package ui;

import controller.PaperSoccer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


public class MainMenu extends BorderPane {	
	private final int prefWidth = 400;
	private final int prefHeight = 550;
	
	public MainMenu() {
		setCenter(addGridPane());
	}
	
	private GridPane addGridPane() {
		GridPane mainMenu = new GridPane();
		mainMenu.setPrefSize(prefWidth, prefHeight);
		mainMenu.setAlignment(Pos.CENTER);
		mainMenu.setStyle("-fx-background-color: GREEN;");
		
		mainMenu.add(generateTitleLabel(), 0, 0);
		mainMenu.add(generateTwoPlayersButton(), 0, 1);
		mainMenu.add(generateExitButton(), 0, 6);
		
		return mainMenu;
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
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
		
		return buttonBox;

	}

	private HBox generateTwoPlayersButton() {
		Button play = new Button("TWO PLAYERS");
		play.setMinSize(150,60);
		
		play.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Two players");
				PaperSoccer.getMainWindow().showTwoPlayersWindow();
				
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
