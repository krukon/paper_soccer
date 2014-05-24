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
							chatWriter.write(data);
							chatWriter.flush();
						} else {
							gameWriter.write(data);
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
	
	public PipedReader subscribeToChat() throws IOException {
		return new PipedReader(chatWriter);
	}
	
	public PipedReader subscribeToGame() throws IOException {
		return new PipedReader(gameWriter);
	}
	
	
	
	public static void main(String[] args) throws IOException {
		ServerInquiry inquiry = new ServerInquiry("localhost", 1444);
		
		inquiry.start();
		inquiry.send("Hello server!");
		PipedReader chat = inquiry.subscribeToChat();
		
		while (true) {
			char buffer[] = new char[1024];
			if (chat.ready()) {
				chat.read(buffer);
				System.out.println("From chat pipe: " + new String(buffer));
				chat.close();
			}
			Thread.yield();
		}
		
	}
}
