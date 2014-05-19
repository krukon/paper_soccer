package network;

/**
 * TODO
 * Chat inquiries
 * Check pipes connecting
 * Thread exiting!
 */

/**
 * Class responsible for communication between server 
 * and client classes. It filters incoming inquiries and distributes 
 * tasks among proper classes.
 * 
 * @author jakub
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ServerInquiryController implements Runnable {
	private Socket socket;
	private BufferedReader in;
	
	private NetworkGuestPlayer guestPlayer;
	private BufferedWriter guestPlayerWriter;
	private PipedWriter guestPlayerPipe;
	
	private String serverMessage;
	
	private HashSet<String> guestPlayerInquiries;
	
	public ServerInquiryController(NetworkGuestPlayer guestPlayer, Socket socket) {
		this.guestPlayer = guestPlayer;
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			guestPlayerPipe = new PipedWriter();
			guestPlayerWriter = new BufferedWriter(guestPlayerPipe);
			guestPlayer.connectPipes(guestPlayerPipe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		guestPlayerInquiries = new HashSet<>();
		guestPlayerInquiries.addAll(Arrays.asList("start_new_game", "finish_game", "get_next_move"));
	}
	
	/**
	 * Gets inquiries from server and then distributes them
	 * to proper classes.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				serverMessage = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject task = (JSONObject) JSONValue.parse(serverMessage);
			if (guestPlayerInquiries.contains(task.get("type").toString())) {
				try {
					guestPlayerWriter.write(serverMessage + "\n");
					guestPlayerWriter.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else throw new UnsupportedOperationException();
		}
	}

}
