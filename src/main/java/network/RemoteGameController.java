package network;

import java.io.IOException;
import java.io.PipedReader;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import helpers.Player;

public class RemoteGameController {
	
	public static class JsonObj {
		
		private StringBuilder builder;
		
		public JsonObj() {
			builder = new StringBuilder();
		}
		
		public JsonObj(String value) {
			super();
			builder.append(value);
		}
		
		public JsonObj add(String key, Object value) {
			if (builder.length() > 0)
				builder.append(", ");
			builder.append("\"" + key + "\": ");
			if (value.getClass() == String.class)
				builder.append("\"" + value + "\"");
			else
				builder.append(value.toString());
			return this;
		}
		
		@Override
		public String toString() {
			return "{" + builder.toString().trim() + "}";
		}
	}

	private Player guest;
	private ServerInquiry server;
	
	public RemoteGameController(ServerInquiry server, Player guest) {
		this.guest = guest;
		this.server = server;
	}
	
	public void joinGame(String id) {
		//TODO server.joinGame(id);
		server.send((new JsonObj()).add("type", "join_game").add("data", new JsonObj().add("id", id)).toString());
		
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		ServerInquiry server = new ServerInquiry("localhost", 1444);
		server.start();
		RemoteGameController controller = new RemoteGameController(server, null);
		controller.joinGame("0");
		//server.send("Hello server!");
		PipedReader game = server.subscribeToGame();
		
		while (true) {
			char buffer[] = new char[1024];
			if (game.ready()) {
				game.read(buffer);
				System.out.println("From game pipe: " + new String(buffer));
				game.close();
			}
			Thread.yield();
		}
	}
}
