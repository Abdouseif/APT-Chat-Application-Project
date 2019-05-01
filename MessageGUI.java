import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MessageGUI extends JPanel {
	private final ChatClient client;
	private static String login="";
	private static DefaultListModel <String> messagemodel = new DefaultListModel<>();
	private JList <String>messagelist = new JList<>(messagemodel);
	final JTextField infield=new JTextField();
	
	public MessageGUI(ChatClient clients,String logined)
	{
		this.client=clients;
		this.login=logined;
		setLayout(new BorderLayout());
		add(new JScrollPane(messagelist),BorderLayout.CENTER);
		add(infield,BorderLayout.SOUTH);
		infield.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String msginfield=infield.getText();
				msginfield=msginfield.replace("\n", "");
				messagemodel.addElement("You: "+msginfield);
				ChatClient.writer.println("msg "+login+" "+msginfield);
				infield.setText("");
			}});
	}
	public static void recievedMessage(String RecMsg)
	{  
		if(RecMsg.startsWith(login))
		{
		RecMsg=RecMsg.replace("\n", "");
		messagemodel.addElement(RecMsg);
		}
	
	}
	
	
}
