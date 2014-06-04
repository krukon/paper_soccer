package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedReader;
import java.net.UnknownHostException;

import javafx.application.Platform;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import ui.GameWindow;
import ui.GameWindowOnline;
import controller.PaperSoccer;
import helpers.Move;
import helpers.Player;
import helpers.Point;

public class RemoteGameController {
	private GameWindowOnline guest;
	private ServerInquiry server;
	private BufferedReader reader;
	public static String gameID;
	private BufferedReader sessionReader;
	
	public RemoteGameController(GameWindowOnline guest, String gameID, BufferedReader reader) {
		this.guest = guest;
		this.server = PaperSoccer.server;
		this.reader = reader;
		RemoteGameController.gameID = gameID;
	}
	
	@SuppressWarnings("unchecked")
	public void runGame() throws IOException {
		BufferedReader reader = server.subscribeToGame();
		sessionReader = server.subscribeToSession();
		
		while (true) {
			
			if (isSessionClosed())
				break;
			
			if (!reader.ready()) {
				Thread.yield();
				continue;
			}
			
			String raw = reader.readLine();
			
			JSONObject message = (JSONObject) JSONValue.parse(raw);
			String type = message.get("type").toString();
			JSONObject data = (JSONObject) message.get("data");
			
			System.out.println("Read from server: " + raw);
			
			if (type.equals("start_game")) {
				int width = Integer.parseInt(data.get("width").toString());
				int height = Integer.parseInt(data.get("height").toString());
				
				guest.startNewGame(width, height, true);
				System.out.println("Starting new game");
			}
			else if (type.equals("finish_game")) {
				System.out.println(data);
				//TODO notify game result
			}
			else if (type.equals("request_next_move")) {
				System.out.println("Waiting for player to make move");
				Move move = guest.getNextMove();

				System.out.println("Got: " + move.start + " - " + move.end);
				JSONObject startPoint = new JSONObject();
				startPoint.put("x", move.start.x);
				startPoint.put("y", move.start.y);
				
				JSONObject endPoint = new JSONObject();
				endPoint.put("x", move.end.x);
				endPoint.put("y", move.end.y);
				
				JSONObject jMove = new JSONObject();
				jMove.put("start", startPoint);
				jMove.put("end", endPoint);
				
				JSONObject moveData = new JSONObject();
				moveData.put("id", gameID);
				moveData.put("move", jMove);
				moveData.put("player", "guest");

				JSONObject moveMessage = new JSONObject();
				moveMessage.put("type", "get_next_move");
				moveMessage.put("data", moveData);
				
				System.out.println("Sending move: " + moveMessage);
				
				server.send(moveMessage);
			}
			else if (type.equals("register_move")) {
				JSONObject dataMove = (JSONObject) data.get("move"); 
				JSONObject start = (JSONObject) dataMove.get("start");
				JSONObject end = (JSONObject) dataMove.get("end");
				
				Point startPoint = new Point(Integer.parseInt(start.get("x").toString()), Integer.parseInt(start.get("y").toString()));
				Point endPoint = new Point(Integer.parseInt(end.get("x").toString()), Integer.parseInt(end.get("y").toString()));
				String player = data.get("player").toString();
				
				Move move = new Move(startPoint, endPoint, player.equals("guest") ? guest : null);
				
				guest.registerMove(move);
			}
			else {
				System.err.println("ERROR: " + raw);
			}
		}
		server.unsubcribeFromGame();
		server.unsubcribeFromSession();
		reader.close();
		sessionReader.close();
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				PaperSoccer.getMainWindow().showMenu();
				
			}
		});
	}

	private boolean isSessionClosed() {
		try {
			if (sessionReader.ready()) {
				String raw;
				raw = sessionReader.readLine();
				
				JSONObject message = (JSONObject) JSONValue.parse(raw);
				String type = message.get("type").toString();
				if (type.equals("close_game"))
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
