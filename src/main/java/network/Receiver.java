package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by mateusz on 17.05.2014.
 */
public class Receiver {
	private Socket socket;
	public BufferedReader fromServer;
	Receiver(Socket socket){
		this.socket = socket;
		try{
			fromServer = new BufferedReader( new InputStreamReader(socket.getInputStream()));
		} catch (Exception e){
			System.out.println("Some kind of error");
		}
	}
}
