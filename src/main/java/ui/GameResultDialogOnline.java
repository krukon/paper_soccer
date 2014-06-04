package ui;

import org.json.simple.JSONObject;

import network.ServerInquiry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import helpers.GameResult;
import controller.PaperSoccer;
import controller.PaperSoccerController;

public class GameResultDialogOnline extends GameResultDialog {
	private String gameID;

	public GameResultDialogOnline(GameResult result, PaperSoccerController controller, String gameID) {
		super(result, controller);
		this.gameID = gameID;
	}
	
	protected void addExitButtonListiner() {
		exit.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Sending close_game message to server.");
				
				JSONObject id = new JSONObject();
				id.put("id", gameID);
				JSONObject message = new JSONObject();
				message.put("type", "close_game");
				message.put("data", id);
				
				ServerInquiry server = PaperSoccer.server;
				server.send(message);
				
				System.out.println("Back to main menu");
				PaperSoccer.getMainWindow().showMenu();
				
				server.unsubcribeFromGame();
				server.unsubcribeFromChat();
				server.unsubcribeFromSession();
				
				close();
			}
		});
	}

}
