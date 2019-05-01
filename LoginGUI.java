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


	
public class LoginGUI extends JPanel{
	
	private static DefaultListModel <String> messagemodel = new DefaultListModel<>();
	private JList <String>messagelist = new JList<>(messagemodel);
	final JTextField infield1=new JTextField();
	private static int counter=0;
	public static String username;
	public static String Password;
	public static String Messagegeneral;


public LoginGUI(  )
{
	
	setLayout(new BorderLayout());
	add(new JScrollPane(messagelist),BorderLayout.CENTER);
	add(infield1,BorderLayout.SOUTH);
	messagemodel.addElement("Enter Username: ");
	infield1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(counter==0) 
				{
					
				
				String msginfield=infield1.getText();
				messagemodel.addElement(msginfield);
				username=msginfield;
				infield1.setText("");
				messagemodel.addElement("Enter Password: ");
				counter=counter+1;
				
				}
				else if(counter==1)
				{
			    	String msginfield=infield1.getText();
					msginfield=msginfield.replace("\n", "");
					messagemodel.addElement(msginfield);
					Password=msginfield;
					infield1.setText("");
					counter=counter+1; 
				}
				else  
				{

			    	String msginfield=infield1.getText();
					msginfield=msginfield.replace("\n", "");
					messagemodel.addElement(msginfield);
					Messagegeneral=msginfield;
					infield1.setText("");
					ChatClient.writer.println(msginfield);
					counter=counter+1; 
				}
				
			}});
}
public static boolean finish()
{
	if(counter>1)
	{
		
		return true;
	}
	else 
	{
		return false;
	}
}
public static void logsuccess()
{
	messagemodel.addElement("Logged in successfully");
}
public static void logfail()
{
	messagemodel.addElement("Failed to Login");
}

public static void leftsuccess(String jj)
{
	messagemodel.addElement(jj);
	messagemodel.addElement(" ");
	
}
public static void joinsuccess(String jj)
{
	messagemodel.addElement(jj);
	messagemodel.addElement(" ");
}
}

