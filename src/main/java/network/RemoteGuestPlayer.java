package network;

/**
 * Class representing a network opponent of host.
 * Need ServerInquiryController to be constructed
 * before first use.
 * 
 * @author jakub
 */

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import controller.PaperSoccer;
import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

public class RemoteGuestPlayer implements Player {
	private String playerName;
	private BufferedReader gamePipe;
	private ServerInquiry server;
	private String gameID;
	
	
	public RemoteGuestPlayer(String playerName, ServerInquiry server, String gameID) throws IOException {
		this.playerName = playerName;
		this.server = PaperSoccer.server;
		this.gameID = gameID;
		gamePipe = server.subscribeToGame();
	}

	/**
	 * Sends information to server to start new game.
	 * @param width board width
	 * @param height board height
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void startNewGame(int width, int height, boolean topGoal) {
		JSONObject message = new JSONObject();
		message.put("type", "start_game");
		JSONObject data = new JSONObject();
		data.put("id", gameID);
		message.put("data", data);		
		
		server.send(message);
	}

	/**
	 * TODO create game result JSON object
	 * Sends information to server that game has finished.
	 * @param result game results
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void finishGame(GameResult result) {
		JSONObject message = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("id", gameID);
		data.put("host_result", result.getOpponentResult());
		data.put("guest_result", result.getMyResult());
		message.put("type", "finish_game");
		message.put("data", data);
		
		server.send(message);
	}

	/**
	 * Requests player for next move. Gets encoded in JSON move form server.
	 *  @return decoded move
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Move getNextMove() {
		JSONObject request = new JSONObject();
		JSONObject requestNextMoveData = new JSONObject();
		requestNextMoveData.put("id", gameID);
		request.put("type", "request_next_move");
		request.put("data", requestNextMoveData);
		server.send(request);
		
		String response = null;
		try {
			response = gamePipe.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject task = (JSONObject) JSONValue.parse(response);
		if (!task.get("type").toString().equals("get_next_move")) throw new RuntimeException("Invalid JSON expression");
		
		JSONObject data = (JSONObject) task.get("data");
		JSONObject move = (JSONObject) data.get("move"); 
		JSONObject start = (JSONObject) move.get("start");
		JSONObject end = (JSONObject) move.get("end");
		
		Point startPoint = new Point(Integer.parseInt(start.get("x").toString()), Integer.parseInt(start.get("y").toString()));
		Point endPoint = new Point(Integer.parseInt(end.get("x").toString()), Integer.parseInt(end.get("y").toString()));
		
		return new Move(startPoint, endPoint, this);
	}

	/**
	 * Sends specified move to the server. Encodes it in JSON.
	 * @param move to be send to the server
	 */
	//{move:{start: {x:0, y:0}, end: {x:1, y:1}}}
	@Override
	@SuppressWarnings("unchecked")
	public void registerMove(Move move) {
		JSONObject startPoint = new JSONObject();
		startPoint.put("x", new Integer(move.start.x));
		startPoint.put("y", new Integer(move.start.y));
		
		JSONObject endPoint = new JSONObject();
		endPoint.put("x", new Integer(move.end.x));
		endPoint.put("y", new Integer(move.end.y));
		
		JSONObject jMove = new JSONObject();
		jMove.put("start", startPoint);
		jMove.put("end", endPoint);
		
		JSONObject data = new JSONObject();
		data.put("id", gameID);
		data.put("move", jMove);
		data.put("player", move.player == this ? "guest" : "host");

		JSONObject message = new JSONObject();
		message.put("type", "register_move");
		message.put("data", data);
		
		server.send(message);
	}

	/**
	 * Get player's name
	 */
	@Override
	public String getName() {
		return playerName;
	}
}
