package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ServerInquiry {
	
	private final Socket socket;
	private final BufferedReader in;
	private final PrintWriter out;
	private PipedWriter chatWriter;
	private PipedWriter gameWriter;
	private PipedWriter joinGameWriter;
	
	public ServerInquiry(String address, int port) throws UnknownHostException, IOException {
		socket = new Socket(address, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		chatWriter = new PipedWriter();
		gameWriter = new PipedWriter();
		joinGameWriter = new PipedWriter();
	}
	
	public void start() {
		Thread serverThread = (new Thread(new Runnable() {
			
			@Override
			public void run() {
				String raw;
				JSONObject message;
				while(true) {
					try {
						raw = in.readLine();
						message = (JSONObject) JSONValue.parse(raw);
						String type = (String) message.get("type");
						
						System.out.println("Message from server " + raw);
						
						if (type.equals("chat")) {
							chatWriter.write(raw + '\n');
							chatWriter.flush();
						} else if (type.equals("join_game")) {
							joinGameWriter.write(raw + '\n');
							joinGameWriter.flush();
						} else {
							gameWriter.write(raw + '\n');
							gameWriter.flush();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					Thread.yield();
				}
			}
		}));
		serverThread.setDaemon(true);
		serverThread.start();
	}
	
	public synchronized void send(String message) {
		//out.write(message + '\n');
		//out.flush();
		out.println(message);
		//out.flush();
	}
	
	public void send(JSONObject message) {
		send(message.toString());
	}
	
	public BufferedReader subscribeToChat() throws IOException {
		return new BufferedReader(new PipedReader(chatWriter));
	}
	
	public BufferedReader subscribeToGame() throws IOException {
		PipedReader pipe = new PipedReader();
		gameWriter = new PipedWriter();
		gameWriter.connect(pipe);
		return new BufferedReader(pipe);
	}
	
	public BufferedReader subscribeToJoinGame() throws IOException {
		PipedReader pipe = new PipedReader();
		joinGameWriter = new PipedWriter();
		joinGameWriter.connect(pipe);
		return new BufferedReader(pipe);
	}
	
	public void closeGame(String id) {
		JSONObject closeGame = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("id", id);
		closeGame.put("type", "close_game");
		closeGame.put("data", data);
		send(closeGame);
	}
	
	
	public static void main(String[] args) throws IOException {
		ServerInquiry inquiry = new ServerInquiry("localhost", 1444);
		
		inquiry.start();
		inquiry.send("{\"type\":\"create_game\", \"data\": {\"host_name\":\"Kuba\", \"width\":8, \"height\": 10}}");
		//inquiry.send("{\"type\":\"chat\", \"data\": {\"id\":5, \"message\":\"Moja wiadomosc\"}}");
		BufferedReader chat = inquiry.subscribeToChat(), game = inquiry.subscribeToGame();
		System.out.println("From game pipe: " + game.readLine());
		while (true) {
			String x = game.readLine();
			System.out.println("From chat pipe: " + x);
			//chat.close();
			Thread.yield();
		}
		
	}
}
