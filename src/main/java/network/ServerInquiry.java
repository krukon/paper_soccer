package network;

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

public class ServerInquiry {
	
	private final Socket socket;
	private final BufferedReader in;
	private final PrintWriter out;
	private PipedWriter chatWriter;
	private PipedWriter gameWriter;
	
	public ServerInquiry(String address, int port) throws UnknownHostException, IOException {
		socket = new Socket(address, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
		chatWriter = new PipedWriter();
		gameWriter = new PipedWriter();
	}
	
	public void start() {
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				String raw;
				JSONObject message;
				while(true) {
					try {
						raw = in.readLine();
						message = (JSONObject) JSONValue.parse(raw);
						String type = (String) message.get("type");
						String data = JSONValue.toJSONString(message.get("data"));
						if (type.equals("chat")) {
							chatWriter.write(data + '\n');
							chatWriter.flush();
						} else {
							gameWriter.write(data + '\n');
							gameWriter.flush();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					Thread.yield();
				}
			}
		})).start();
	}
	
	public void send(String message) {
		out.write(message);
		out.flush();
	}
	
	public BufferedReader subscribeToChat() throws IOException {
		return new BufferedReader(new PipedReader(chatWriter));
	}
	
	public BufferedReader subscribeToGame() throws IOException {
		return new BufferedReader(new PipedReader(gameWriter));
	}
	
	
	
	public static void main(String[] args) throws IOException {
		ServerInquiry inquiry = new ServerInquiry("178.37.109.133", 1444);
		
		inquiry.start();
		inquiry.send("{\"type\":\"create_game\", \"data\": {\"host_name\":\"Kuba\", \"width\":8, \"height\": 10}}");
		inquiry.send("{\"type\":\"chat\", \"data\": {\"id\":5, \"message\":\"Moja wiadomosc\"}}");
		BufferedReader chat = inquiry.subscribeToChat(), game = inquiry.subscribeToGame(),
				game2 = inquiry.subscribeToGame();
		System.out.println("From game pipe: " + game.readLine());
		System.out.println("From game pipe2: " + game2.readLine());
		while (true) {
			String x = chat.readLine();
			System.out.println("From chat pipe: " + x);
			//chat.close();
			Thread.yield();
		}
		
	}
}
