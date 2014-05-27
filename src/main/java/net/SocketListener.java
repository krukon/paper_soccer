package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;

public class SocketListener {
	
	public final PipedReader chatPipe;
	public final PipedWriter chatWriter;
	
	public SocketListener() {
		chatPipe = new PipedReader();
		chatWriter = new PipedWriter();
		try {
			chatPipe.connect(chatWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		final SocketListener a = new SocketListener();
		Socket sock = new Socket("localhost", 1444);
		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		PrintWriter out = new PrintWriter(sock.getOutputStream());
		out.println("Hello server!");
		out.flush();
	
		
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				String y;
				char buffer[] = new char[1024];
				try {
					while (true) {
						if (a.chatPipe.ready()) {
							a.chatPipe.read(buffer);
							y = new String(buffer);
							System.out.println("From chat pipe: " + y);
						}
					}
				} catch (Exception e) {}
			}
		})).start();
		String x;
		while (true) {
			x = in.readLine();
			a.chatWriter.write(x);
			System.out.println("From server: " + x);
		}
		
	}

}
