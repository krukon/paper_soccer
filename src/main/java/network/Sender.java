package network;

import helpers.Move;
import org.json.simple.JSONObject;
import java.io.*;
import java.net.*;

/**
 * Created by mateusz on 17.05.2014.
 */
public class Sender {
	private Socket socket;
	private PrintWriter out;
	Sender(Socket socket){
		try{
			this.socket = socket;
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e){
			System.out.println("some kind of error");
		}
	}
	public void Send(Move move){
		JSONObject obj = new JSONObject();
		obj.put("userName", move.player.getName());
		obj.put("moveX", move.end.x);
		obj.put("moveY", move.end.y);
		out.println(obj);
	}
}
