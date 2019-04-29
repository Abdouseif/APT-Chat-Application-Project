package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Server extends Thread{

	private final int ServerPort;
	
	private ArrayList<ServerCommHandler> ServerHandlerList = new ArrayList<>();
	
	public Server(int port)
	{
		this.ServerPort = port;
		
	}

	public ArrayList<ServerCommHandler> getServerHanldesList()
	{
		return ServerHandlerList;
	}
	
	public void removeHandler(ServerCommHandler Handler)
	{
		ServerHandlerList.remove(Handler);
	}
	
	
	@Override
	public void run()
	{
		try {
			ServerSocket serverSocket = new ServerSocket(ServerPort); // Server Socket Creation
			while (true) {
				System.out.println(" Waiting to accept new user ");
				Socket UserSocket = serverSocket.accept(); // Server Socket accept any client socket connection
				System.out.println("Accepted connection to User " + UserSocket);
				ServerCommHandler Handler = new ServerCommHandler(this,UserSocket); // Handler Object ( Thread for each client )
				ServerHandlerList.add(Handler);
				Handler.start(); // start each client thread
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
