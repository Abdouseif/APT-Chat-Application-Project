import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


	
public class GroupGUI extends JPanel{
	
	private static DefaultListModel <String> messagemodel = new DefaultListModel<>();
	private JList <String>messagelist = new JList<>(messagemodel);
	final JTextField infield1=new JTextField();
	public static String groupName;
	
	
	public static String Messagegeneral;


public GroupGUI( String s)
{
	
	setLayout(new BorderLayout());
	add(new JScrollPane(messagelist),BorderLayout.CENTER);
	add(infield1,BorderLayout.SOUTH);
	groupName=s.substring(s.lastIndexOf("#")+1);
	messagemodel.addElement(s);
	messagemodel.addElement(groupName);
	
	infield1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
 
			    	String msginfield=infield1.getText();
			    	
			    	infield1.setText("");
			    	if(msginfield.startsWith("leave"))
			    	{    String l=msginfield + " #"+groupName;
			    		ChatClient.writer.println(l);
			    	}
			    	else
			    	{
			    	msginfield=msginfield.replace("\n", "");
					String M= "msg #"+groupName+" "+msginfield;
					ChatClient.writer.println(M);
			    	}
				
				
			}});
}
public static void recievegroupMessage(String RecMsg)
{  

	RecMsg=RecMsg.replace("\n", "");
	messagemodel.addElement(RecMsg);
	}
}