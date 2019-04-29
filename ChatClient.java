package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

	private final String Address;
	private final int port;
	private Socket socket;
	private static OutputStream outputToServer;
	private static InputStream inputFromServer;
	static BufferedReader reader;
	static boolean SuccessLogin;
	static PrintWriter writer;

	public ChatClient(String Address, int port) {
		this.Address = Address;
		this.port = port;
	}
/////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		ChatClient Client = new ChatClient("localhost", 8082);
		boolean isConnected = Client.connect();
		if (isConnected) {
			System.out.print("Connected successfully \n");

			SuccessLogin = false;
				SuccessLogin = Client.login(); // login with entered username and password

			if (SuccessLogin) {
				NotificationsReciever();
				ActionsSender();
			}

		} else
			System.out.println("Connection failed");

	}

///////////////////////////////// Thread for Sending messages and joining  ///////////////////////	

	private static void ActionsSender() {
		Thread Actions = new Thread() {
			@Override
			public void run() {
				while (true) {
					String UserInput;
					Scanner myObj = new Scanner(System.in); // Create a Scanner objectSSS
					UserInput = myObj.nextLine(); // Read user input

					ChatClient.writer.println(UserInput);
				}
			}

		};
		Actions.start();
	}

///////////////////////////////// Thread for recieving notifications ///////////////////////	
	private static void NotificationsReciever() {
		Thread NotifThread = new Thread() {
			@Override
			public void run() {
				String StatusMessages = "";
				while (true) {
					try {
						while (!(StatusMessages = reader.readLine()).equalsIgnoreCase(null)) {
							System.out.println(StatusMessages);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		NotifThread.start();
	}

/////////////////////////////////////////////////////////////////////////////////////////////
	private boolean login() {
		// get input user name and input by scanner
		String login = "";
		String password = "";
		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		System.out.println("Enter username");
		login = myObj.nextLine(); // Read user input

		System.out.println("Enter password");
		password = myObj.nextLine(); // Read user input
		String SentToServer = "login " + login + " " + password;
		this.writer.println(SentToServer);
		String ResponseRecieved = "";
		try {
			ResponseRecieved = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (ResponseRecieved.equals("Login Successfully")) {
			System.out.println("Login Successfully");
			return true;
		} else {
			System.out.println("Login Failed not valid inputs ");
			return false;
		}

	}

/////////////////////////////////////////////////////////////////////////////////////////////
	private boolean connect() {
		try {
			this.socket = new Socket(Address, port);
			ChatClient.outputToServer = socket.getOutputStream();
			ChatClient.inputFromServer = socket.getInputStream();

			reader = new BufferedReader(new InputStreamReader(inputFromServer));
			writer = new PrintWriter(socket.getOutputStream(), true);

			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
}
