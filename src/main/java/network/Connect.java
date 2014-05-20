package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by mateusz on 17.05.2014.
 */
public class Connect {
	//private static Socket socket;

	public static Socket NewConnection(String hostName, int portNumber){
		Socket socket = null;
		try{
			socket = new Socket(hostName, portNumber);
		} catch (UnknownHostException e){
			System.out.println("Wrong host");
		} catch (IOException e){}
		return socket;
	}
}
