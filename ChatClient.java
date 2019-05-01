
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class ChatClient extends JPanel {
 
	private final String Address;
	private final int port;
	private Socket socket;
	private static int counter=0;
	private static String name;
	private String username;
	private String Password;
	private static OutputStream outputToServer;
	private static InputStream inputFromServer;
	static BufferedReader reader;
	static boolean SuccessLogin;
	static PrintWriter writer;
	private static JList <String> onlineUsers;
	private static DefaultListModel <String> onlinelistmodel;
	
	

	public ChatClient(String Address, int port) {
		this.Address = Address;
		this.port = port;
		this.name="";
		ChatClient newch=this;
		onlinelistmodel = new DefaultListModel<>();
		onlineUsers = new JList<>(onlinelistmodel);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(onlineUsers),BorderLayout.CENTER);
		onlineUsers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if(e.getClickCount()>1)
				{
					String login= onlineUsers.getSelectedValue();
					MessageGUI mes=new MessageGUI(newch,login);
					JFrame frameM = new JFrame("Message "+login);
					frameM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frameM.setSize(600,600);
					frameM.getContentPane().add(mes,BorderLayout.CENTER);
					frameM.setVisible(true);
					
					
				}
			}		
		});
		
		
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
                JFrame frame = new JFrame(name+"'s Online Users");
   				 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   				 frame.setSize(400,900);
   				 frame.getContentPane().add(Client,BorderLayout.CENTER);
   				 frame.setVisible(true);
				 NotificationsReciever();
				 ActionsSender();
			     }

		} else
			System.out.println("Connection failed");

	}

///////////////////////////////// Thread for Sending messages and joining  ///////////////////////	

	public static void ActionsSender() {
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
	public static void NotificationsReciever() {
		Thread NotifThread = new Thread() {
			@Override
			public void run() {
				String StatusMessages = "";
				while (true) {
					try {
						while (!(StatusMessages = reader.readLine()).equalsIgnoreCase(null)) {
							if(StatusMessages.equalsIgnoreCase("logout"))
							{
						    System.out.println("You are offline now"); 
							this.stop(); //stop the thread to handle the exception
							
							}
							else if(StatusMessages.startsWith("online"))
							{   String msg= StatusMessages.replaceFirst("online: ", "");
								onlinelistmodel.addElement(msg);
						    }
							else if(StatusMessages.startsWith("offline"))
							{ String msg= StatusMessages.replaceFirst("offline: ", "");
								onlinelistmodel.removeElement(msg);
						    }
							else if (StatusMessages.startsWith("You joined group"))
							{
								GroupGUI li=new GroupGUI(StatusMessages);
								JFrame frameM = new JFrame("Group");
								frameM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
								frameM.setSize(600,600);
								frameM.getContentPane().add(li,BorderLayout.CENTER);
								frameM.setVisible(true);
								System.out.println(StatusMessages);
							}
						
							else if (StatusMessages.startsWith("You left group"))
							{
								
								System.out.println(StatusMessages);
								GroupGUI.recievegroupMessage(StatusMessages);
								
							}
							else if (StatusMessages.startsWith("#"))
							{
								System.out.println(StatusMessages);
								GroupGUI.recievegroupMessage(StatusMessages.substring(StatusMessages.indexOf(":")+1));
								
							}
							else 
							{
							System.out.println(StatusMessages);
							MessageGUI.recievedMessage(StatusMessages);
							}
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
	public boolean login() {
		// get input user name and input by scanner
		
		String login = "";
		String password = "";
		//Scanner myObj = new Scanner(System.in); // Create a Scanner object
		//System.out.println("Enter username");
		//login = myObj.nextLine(); // Read user input

		//System.out.println("Enter password");
		//password = myObj.nextLine(); // Read user input
		LoginGUI li=new LoginGUI();
		JFrame frameM = new JFrame("Login");
		frameM.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameM.setSize(600,600);
		frameM.getContentPane().add(li,BorderLayout.CENTER);
		frameM.setVisible(true);
		while(!LoginGUI.finish()) {
			password="";
			System.out.println("");
		};
		login=LoginGUI.username;
		password=LoginGUI.Password;
		
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
			LoginGUI.logsuccess();
			System.out.println("Login Successfully");
			this.name=login;
		
			return true;
		} else {
			LoginGUI.logfail();
			System.out.println("Login Failed not valid inputs ");
			return false;
		}

	}

/////////////////////////////////////////////////////////////////////////////////////////////
	public boolean connect() {
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
