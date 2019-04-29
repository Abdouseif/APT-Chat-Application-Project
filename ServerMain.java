package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.net.*;
import java.lang.Thread;

public class ServerMain {

	public static void main(String[] args) {
		int port = 8082; // Server Port
		Server server = new Server(port);
		
		server.start();	

	}

	
}
