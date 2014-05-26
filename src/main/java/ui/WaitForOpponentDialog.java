package ui;

/**
 * Class representing a simple dialog box while waiting for opponent.
 * 
 * @author jakub, ljk
 */


import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import network.RemoteGuestPlayer;
import network.ServerInquiry;
import controller.PaperSoccer;
import controller.PaperSoccerController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WaitForOpponentDialog extends Stage {
	private ServerInquiry server = PaperSoccer.server;
	
	public WaitForOpponentDialog(GameWindowOnline host, String hostName, int width, int height) {
		constructView();
        waitForGuest(host, hostName, width, height);
	}
	
	private void waitForGuest(final GameWindowOnline host, final String hostName, final int width, final int height) {
		new Thread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				System.out.println("Wait for guest - begin");
				String guestName, gameID = null;
				BufferedReader reader = null;
				BufferedReader joinReader = null;
				try {
					reader = server.subscribeToGame();
					joinReader = server.subscribeToJoinGame();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject data = new JSONObject();
				data.put("host_name", hostName);
				data.put("width", width);
				data.put("height", height);
				JSONObject message = new JSONObject();
				message.put("type", "create_game");
				message.put("data", data);
				
				server.send(message);
				System.out.println("Wait for guest - waiting for response");
				try {
					String raw = reader.readLine();
					JSONObject createGameResponse = (JSONObject) JSONValue.parse(raw);
					JSONObject createGameData = (JSONObject) createGameResponse.get("data");
					System.out.println("Wait for guest - response " + createGameData.toString());
					gameID = createGameData.get("id").toString();
					host.registerGameID(gameID);
					System.out.println("Wait for guest - after response " + raw);
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(true) {
					try {
						System.out.println("Waiting...");
						String raw = joinReader.readLine();
						System.out.println("From server: " + raw);
						JSONObject joinGameData = (JSONObject) JSONValue.parse(raw);
						if (joinGameData.get("type").toString().equals("join_game")) {
							guestName = ((JSONObject) joinGameData.get("data")).get("guest_name").toString();
							break;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				final String finalGuestName = guestName;
				final String finalGameId = gameID;

				System.out.println("Run game");
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						WaitForOpponentDialog.this.close();
					}
				});
				
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Thread controllerThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							RemoteGuestPlayer guest = new RemoteGuestPlayer(finalGuestName, server, finalGameId);
							PaperSoccerController controller = new PaperSoccerController(host, guest, width, height);
							host.registerController(controller);
							controller.runGame();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				});
				controllerThread.setDaemon(true);
				controllerThread.start();
			}
		}).start();
	}

	private void constructView() {
		initModality(Modality.APPLICATION_MODAL);
		
		Label message = new Label("Waiting for opponent...");
		message.setFont(Font.font(22));
		message.setAlignment(Pos.BASELINE_CENTER);
		
		Button exit = new Button("Back");
		exit.setCancelButton(true);
		exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Back to network menu");
				PaperSoccer.getMainWindow().showNetworkWindow();
				close();
			}
		});
		
		HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setSpacing(40.0);
        hBox.getChildren().addAll(exit);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BASELINE_CENTER);
        vBox.setSpacing(40.0);
        vBox.getChildren().addAll(message, hBox);

        setScene(new Scene(vBox, 300, 150));
	}
}
