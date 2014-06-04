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
	private PipedWriter sessionWriter;
	
	public ServerInquiry(String address, int port) throws UnknownHostException, IOException {
		socket = new Socket(address, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		chatWriter = new PipedWriter();
		gameWriter = new PipedWriter();
		sessionWriter = new PipedWriter();
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
							if (chatWriter != null) {
								chatWriter.write(raw + '\n');
								chatWriter.flush();
							}
						} else if (type.equals("join_game") || type.equals("close_game")) {
							if (sessionWriter != null) {
								sessionWriter.write(raw + '\n');
								sessionWriter.flush();
							}
						} else {
							if (gameWriter != null) {
								System.out.println("Raw " + raw);
								gameWriter.write(raw + '\n');
								gameWriter.flush();
							}
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
		PipedReader pipe = new PipedReader();
		chatWriter = new PipedWriter();
		chatWriter.connect(pipe);
		return new BufferedReader(pipe);
	}
	
	public BufferedReader subscribeToGame() throws IOException {
		PipedReader pipe = new PipedReader();
		gameWriter = new PipedWriter();
		gameWriter.connect(pipe);
		return new BufferedReader(pipe);
	}
	
	public BufferedReader subscribeToSession() throws IOException {
		PipedReader pipe = new PipedReader();
		sessionWriter = new PipedWriter();
		sessionWriter.connect(pipe);
		return new BufferedReader(pipe);
	}
	
	public void unsubcribeFromChat() {
		try {
			chatWriter.close();
		} catch (IOException e) {}
		chatWriter = null;
	}
	
	public void unsubcribeFromGame() {
		try {
			gameWriter.close();
		} catch (IOException e) {}
		gameWriter = null;
	}
	
	public void unsubcribeFromSession() {
		try {
			sessionWriter.close();
		} catch (IOException e) {}
		sessionWriter = null;
	}
	
	@SuppressWarnings("unchecked")
	public void closeGame(String id) {
		JSONObject closeGame = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("id", id);
		closeGame.put("type", "close_game");
		closeGame.put("data", data);
		send(closeGame);
	}
}
