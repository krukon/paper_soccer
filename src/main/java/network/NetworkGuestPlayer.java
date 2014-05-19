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
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import helpers.GameResult;
import helpers.Move;
import helpers.Player;
import helpers.Point;

public class NetworkGuestPlayer implements Player {
	private String playerName;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in; //needs ServerInquiryController instance created first
	
	
	public NetworkGuestPlayer(String playerName, Socket socket) {
		this.playerName = playerName;
		this.socket = socket;
		try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Connects decorated by BufferedReader PipedReader with ServerInquiryController
	 * thread's PipedWriter
	 * @param src source PipedWriter to be connected
	 * @throws IOException
	 */
	public void connectPipes(PipedWriter src) throws IOException {
		in = new BufferedReader(new PipedReader(src));
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
		
		out.println(message);
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
		message.put("data", null);
		
		out.println(message);
	}

	/**
	 * Requests player for next move. Gets encoded in JSON move form server.
	 *  @return decoded move
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Move getNextMove() {
		JSONObject request = new JSONObject();
		request.put("type", "request_next_move");
		request.put("data", null);
		out.println(request);
		
		String move = null;
		try {
			move = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject task = (JSONObject) JSONValue.parse(move);
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
		
		out.println(message);
	}

	/**
	 * Get player's name
	 */
	@Override
	public String getName() {
		return playerName;
	}
	
	@Deprecated
	private void createGame() throws IOException {
		out.println("{\"type\":\"create_game\",\"data\":{\"host_name\":\"Kuba\",\"width\":8,\"height\":10}}");
		System.out.println(in.readLine());
		out.println("{\"type\":\"chat\",\"data\":{\"id\":0,\"message\":\"Wiadomosc od Kuby\",\"username\":\"Kuba\"}}");
		System.out.println(in.readLine());
	}
	
	@Deprecated
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		NetworkGuestPlayer p = new NetworkGuestPlayer("x", new Socket("77.253.12.69", 1444));
		p.createGame();
	}

}
