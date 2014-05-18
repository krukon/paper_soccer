package network;

/**
 * Class representing a network opponent of host.
 * 
 * @author jakub
 */

import javax.management.RuntimeErrorException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

public class NetworkGuestPlayer implements Player {
	private String playerName;
	
	public NetworkGuestPlayer(String playerName) {
		this.playerName = playerName;
		
	}

	/**
	 * Sends information to server to start new game.
	 * @param width board width
	 * @param height board height
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void startNewGame(int width, int height) {
		JSONObject message = new JSONObject();
		message.put("type", "start_new_game");
		message.put("data", null);		
	}

	/**
	 * Sends information to server that game has finished.
	 * @param result game results
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void finishGame(GameResult result) {
		JSONObject message = new JSONObject();
		message.put("type", "finish_game");
		message.put("type", null);
	}

	/**
	 * Requests player for next move. Gets encoded in JSON move form server.
	 *  @return decoded move
	 */
	@Override
	public Move getNextMove() {
		String move = "{\"type\":\"get_next_move\",\"data\":{\"start\":{\"x\":11,\"y\":22},\"end\":{\"x\":33,\"y\":44}}}";
		
		JSONObject task = (JSONObject) ((JSONObject) JSONValue.parse(move));
		if (!task.get("type").toString().equals("get_next_move")) throw new RuntimeException("Invalid JSON expression");
		
		JSONObject moveObj = (JSONObject) task.get("data"); 
		JSONObject start = (JSONObject) moveObj.get("start");
		JSONObject end = (JSONObject) moveObj.get("end");
		
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

		JSONObject message = new JSONObject();
		message.put("type", "register_move");
		message.put("data", jMove);
		System.out.println(message);
	}

	/**
	 * Get player's name
	 */
	@Override
	public String getName() {
		return playerName;
	}
	
	@Deprecated
	public static void main(String[] args) {
		NetworkGuestPlayer p = new NetworkGuestPlayer("x");
		Move m = new Move(new Point(1, 2), new Point(3, 4), p);
		p.registerMove(m);
		p.registerMove(p.getNextMove());
	}

}
