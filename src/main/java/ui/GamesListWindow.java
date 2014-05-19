package ui;

/**
 * Class representing a network games list window.
 * 
 * @author ljk
 */

import java.util.concurrent.atomic.AtomicBoolean;

import helpers.Player;
import controller.PaperSoccer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import controller.PaperSoccerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Board;

public class GamesListWindow extends BorderPane {
	private Text windowTitle = new Text("Those players are waiting for you!");
	private Insets insets = new Insets(25, 25, 25, 25);
	private String playerName;
	
	public GamesListWindow(String playerName) {
		this.playerName = playerName;
		
		windowTitle.setFill(Color.WHITE);
		
		setPadding(insets);
		setTop(addWindowTitle());
		setBottom(addStartBackButton());
	}
	
	/**
	 * Constructs window title.
	 * @return HBox with a title
	 */
	private HBox addWindowTitle() {
		HBox textBox = new HBox();
		textBox.setAlignment(Pos.CENTER);
		textBox.getChildren().add(windowTitle);
		
		return textBox;
	}
	
	/**
	 * Constructs an exit button.
	 * @return constructed button
	 */
	private Button getBackButton() {
		Button exitButton = new Button("BACK");
		exitButton.setMinSize(100, 50);
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {				
				System.out.println("Back to network game menu");
				PaperSoccer.getMainWindow().showNetworkWindow();
			
			}
		});
		
		return exitButton;
	}
	
	/**
	 * Adds start and exit buttons to the window.
	 * @return HBox with a buttons
	 */
	private HBox addStartBackButton() {
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(getBackButton());
		
		return buttonBox;
	}
	
}
