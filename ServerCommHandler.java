package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import javafx.concurrent.Worker;

import java.io.*;

public class ServerCommHandler extends Thread {

	private final Socket UserSocket;
	private Server server;
	OutputStream output;
	BufferedReader reader;
	PrintWriter writer;
	private HashSet<String> TopicSet = new HashSet<>();
	private String login = null;

	public ServerCommHandler(Server server, Socket UserSocket) {
		this.server = server;
		this.UserSocket = UserSocket;
	}

	@Override
	public void run() {
		try {
			multiUserHanlder(UserSocket); // Overriding run to do the below method (handler)
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//////////////////////////////////////////////////////////////////
	private void multiUserHanlder(Socket UserSocket) throws Throwable {
		this.output = UserSocket.getOutputStream();
		InputStream input = UserSocket.getInputStream();
		reader = new BufferedReader(new InputStreamReader(UserSocket.getInputStream()));
		writer = new PrintWriter(UserSocket.getOutputStream(),true);

		String temp;
			while (!(temp = reader.readLine()).equalsIgnoreCase(null)) {
				String[] tokens = new String[100];
				tokens = temp.split(" ");
				if (tokens != null && tokens.length > 0) {

					String cmd = tokens[0];
					if (cmd.equalsIgnoreCase("close") || cmd.equalsIgnoreCase("logout")) {
						logouthandle();
						break;
					} else if (cmd.equalsIgnoreCase("login")) {
						loginhandle(tokens, output);
					} else if (cmd.equalsIgnoreCase("join")) {

						joinhandle(tokens);

					} else if (cmd.equalsIgnoreCase("leave")) {

						leavehandle(tokens);

					} else if (cmd.equalsIgnoreCase("msg")) {

						msghandle(tokens);

					} else {
						String message = "unknown " + cmd + "\n";
						output.write(message.getBytes());

					}

				}
			}
			UserSocket.close();
		}
//////////////////////////////////////////////////////////////////
	private void loginhandle(String[] tokens, OutputStream output) throws IOException {
		if (tokens.length > 2) {
			String login = tokens[1];
			String password = tokens[2];

			if ((login.equalsIgnoreCase("ahmed") && password.equalsIgnoreCase("ahmed"))
					|| (login.equalsIgnoreCase("abdo") && password.equalsIgnoreCase("abdo")) || (login.equalsIgnoreCase("bongo") && password.equalsIgnoreCase("bongo"))
					|| (login.equalsIgnoreCase("ismail") && password.equalsIgnoreCase("ismail"))) {
				String msg = "Login Successfully";
				writer.println(msg);
				System.out.println("Login Successfully");
				this.login = login;

				ArrayList<ServerCommHandler> HandlersList = server.getServerHanldesList();

				for (ServerCommHandler Handler : HandlersList) // btetba3 3andy meen l fat7
				{
					String OnlineID = " online " + Handler.getLogin() + "\n";
					if (!Handler.getLogin().equalsIgnoreCase(null)) {
						if (Handler.getLogin() != this.login)
							writer.println(OnlineID);
					}

				}

				String onlineMsg = "online:  " + login + "\n";
				for (ServerCommHandler Handler : HandlersList) // btetba3 3and l nas el ba2ya eny fat7t
				{
					if (!Handler.getLogin().equalsIgnoreCase(this.login))
						Handler.send(onlineMsg);
				}
			} else {
				String msg = "incorrect username or password" + "\n";
				writer.println(msg);
			}

		} else {
			String msg = "enter username and password" + "\n";
			writer.println(msg);
		}
	}

//////////////////////////////////////////////////////////////////
	public String getLogin() {
		return login;
	}

//////////////////////////////////////////////////////////////////
	public void logouthandle() {
		try {

			server.removeHandler(this);
			String offlineMsg = "offline:  " + login + "\n";
			ArrayList<ServerCommHandler> HandlersList = server.getServerHanldesList();

			for (ServerCommHandler Handler : HandlersList) // btetba3 3and l nas el ba2ya eny fat7t
			{
				if (Handler.getLogin() != this.login)
					Handler.send(offlineMsg);
			}
			UserSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//////////////////////////////////////////////////////////////////
	public boolean CheckTopicMember(String topic) {
		return TopicSet.contains(topic);
	}

//////////////////////////////////////////////////////////////////
	public void leavehandle(String[] tokens) {
		if (tokens.length > 1) {
			TopicSet.remove(tokens[1]);
			send("You left group "+ tokens[1]);
			
		}
	}

//////////////////////////////////////////////////////////////////
	public void joinhandle(String[] tokens) {
		if (tokens.length > 1) {
			TopicSet.add(tokens[1]);
			send("You joined group "+ tokens[1]);
		}
	}

//////////////////////////////////////////////////////////////////
	public void msghandle(String[] tokens) {

		if (tokens.length > 1) {
			ArrayList<ServerCommHandler> HandlersList = server.getServerHanldesList();

			boolean UserFound = false;
			boolean isTopic = false;
			if (tokens[1].startsWith("#") && this.CheckTopicMember(tokens[1]))
				isTopic = true;

			for (ServerCommHandler Handler : HandlersList) {
				if (isTopic) {
					if (Handler.CheckTopicMember(tokens[1])) {
						UserFound = true;
						String messageSent = "";
						for (int i = 2; i < tokens.length; i++) {
							messageSent = messageSent + " " + tokens[i];
						}
						messageSent = tokens[1] + " : " + login + ":" + messageSent;
						Handler.send(messageSent);

					}
				} else {
					if ((Handler.getLogin()).equals(tokens[1]) && !(Handler.getLogin().equals(login)) ) {
						UserFound = true;
						String messageSent = "";
						for (int i = 2; i < tokens.length; i++) {
							messageSent = messageSent + " " + tokens[i];
						}
						messageSent = login + ":" + messageSent;
						Handler.send(messageSent);
					}
				}
			}

			if (!UserFound) {
				String messageSent = " User not found ";
				send(messageSent);

			}
		}
	}

//////////////////////////////////////////////////////////////////
	private void send(String msg) {
			msg = msg + "\n\t";
			writer.println(msg);

	}
}
