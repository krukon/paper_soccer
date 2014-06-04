package ui;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import controller.PaperSoccer;
import helpers.GameResult;
import helpers.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class GameWindowOnline extends GameWindow implements Player {
	private String gameID;

	// Chat things
	private ListView listView;
	private TextField textField;
	private BorderPane borderPane;
	private ObservableList<String> chatItems = FXCollections
			.observableArrayList();
	private double chatHeight = 100;
	private double textFieldWidth = 300;
	private double buttonWidth = PaperSoccer.WIDTH - textFieldWidth - 50;
	private Insets insets = new Insets(25, 25, 25, 25);

	public GameWindowOnline(String playerName, Color playerColor,
			Color opponentColor) {
		super(playerName, playerColor, opponentColor);
		this.setBottom(addChatWindow());
		pixelHeight = 450;
	}

	public void registerGameID(String gameID) {
		this.gameID = gameID;
	}

	@Override
	public void finishGame(final GameResult result) {
		gameOver = true;
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				new GameResultDialogOnline(result, controller, gameID).show();
			}
		});
	}

	public BorderPane addChatWindow() {
//		listView = new ListView();
//		listView.setMaxHeight(chatHeight);
//		listView.setMinWidth(PaperSoccer.WIDTH - 50);
//		listView.setItems(chatItems);

		textField = new TextField();
		textField.setMinWidth(textFieldWidth);
		Button btn = new Button();
		btn.setMinWidth(buttonWidth);
		btn.setText("SEND");
		btn.setDefaultButton(true);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				System.out.println("chat event " + event);
				JSONObject json = new JSONObject();
				JSONObject data = new JSONObject();
				data.put("message", textField.getText());
				data.put("username", playerName);
				data.put("id", gameID);
				json.put("type", "chat");
				json.put("data", data);
				PaperSoccer.server.send(json);
				textField.clear();
			}
		});

		borderPane = new BorderPane();
		HBox bottom = new HBox(textField, btn);
		borderPane.setPadding(insets);
		//borderPane.setCenter(listView);
		borderPane.setBottom(bottom);
		return borderPane;
	}
	
	public void startChat() {
		Thread chatThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					BufferedReader chat = PaperSoccer.server.subscribeToChat();
					while (true) {
						if (!chat.ready()) {
							Thread.yield();
							continue;
						}
						String rawJson = chat.readLine();
						System.out.println("Waiting for chat message");
					
						JSONObject json = (JSONObject) JSONValue.parse(rawJson);
						JSONObject data = (JSONObject) json.get("data");
						final String message = data.get("message").toString();
						final String username = data.get("username").toString();
						
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								chatItems.add(username + ": " + message);
								listView.scrollTo(chatItems.size() - 1);
							}
						});
					}
				} catch (IOException e) {
					System.err.println("Could not connect to chat");
					return;
				}
			}
		}, "Chat Thread");
		PaperSoccer.getMainWindow().registerChatThread(chatThread);
		chatThread.setDaemon(true);
		chatThread.start();
	}
}
