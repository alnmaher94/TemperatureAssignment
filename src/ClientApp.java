import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ClientApp implements ActionListener{
	
	private JButton button;
	private JLabel label;
	private Client client;
	private JOptionPane optionPane;
	private JTextField intervalTF;
	private JTextField portTextField;
	private ClientThread thread;
	private JTextField ipTextField;
	private JLabel maxValue;
	private JLabel minValue;
	private JLabel avgValue;
	private Vector<Temperature> v;
	
	ClientApp(){
		
		//
		
		while(!popup()){}
		
		this.v = new Vector<Temperature>();
		
		JFrame frame = new JFrame("Temperature Application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		JPanel centerPanel = new JPanel(new GridLayout(3,2));
		
		JLabel maxLabel = new JLabel("Max: ");
		centerPanel.add(maxLabel);
		maxValue = new JLabel("");
		centerPanel.add(maxValue);
		
		JLabel minLabel = new JLabel("Min: ");
		centerPanel.add(minLabel);
		minValue = new JLabel("");
		centerPanel.add(minValue);
		
		JLabel avgLabel = new JLabel("Average: ");
		centerPanel.add(avgLabel);
		avgValue = new JLabel("");
		centerPanel.add(avgValue);
		
		contentPane.add(centerPanel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main (String[] args){
		new ClientApp();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(button)){
			System.out.println("Button Clicked!!");
			Temperature t = client.getTemperature();
			label.setText("Current Temperature: " + t.getTemperature());
		}
	}
	
	private boolean popup(){
		
		//IP Address PopUp
		JPanel popupPanel = new JPanel();
		popupPanel.setLayout(new GridLayout(3,2));
		
		JLabel ipLabel = new JLabel("IP Address: ");
		popupPanel.add(ipLabel);
		ipTextField = new JTextField();
		ipTextField.setPreferredSize(new Dimension(100,20));
		popupPanel.add(ipTextField);
		
		JLabel portLabel = new JLabel("Port Number: ");
		popupPanel.add(portLabel);
		portTextField = new CustomTextField();
		portTextField.setPreferredSize(new Dimension(100,20));
		popupPanel.add(portTextField);
		
		JLabel intervalLabel = new JLabel("Interval: ");
		popupPanel.add(intervalLabel);
		JPanel panel = new JPanel(new FlowLayout());
		intervalTF = new CustomTextField();
		intervalTF.setPreferredSize(new Dimension(30,20));	
		JLabel secsLabel = new JLabel(" seconds");		
		panel.add(intervalTF);
		panel.add(secsLabel);
		popupPanel.add(panel);
	
		Object[] options = { "Ok", "Cancel" };
		int result = JOptionPane.showOptionDialog(null,popupPanel, "Select Server"
													, JOptionPane.OK_OPTION, JOptionPane.CANCEL_OPTION,
													null, options, options[0]);
		
		if(result == JOptionPane.OK_OPTION){
			String ip = ipTextField.getText().trim().toString();
			String portString = portTextField.getText().trim().toString();
			String intervalString = intervalTF.getText().trim().toString();
	
			if(ip.equals("") || portString.equals("") || intervalString.equals("")){
				JOptionPane.showMessageDialog(null, "Fields cannot be left blank");
				return false;
			}
			
			int port = Integer.parseInt(portString);
			int interval = Integer.parseInt(intervalString);
			
			try {
				client = new Client(ip,port);	
				thread = new ClientThread(client,interval,this);
				thread.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Could not Connect to Server");
				return false;
			}
			
			return true;
		}else{
			System.exit(0);
			return false;
		}
		
	}

	
	public void addData(Temperature t){
		float avg = 0;;
		v.addElement(t);
		maxValue.setText(Collections.max(v).getTemperature()+"");
		minValue.setText(Collections.min(v).getTemperature() + "");
		if(!v.isEmpty()){
			for(Temperature tem : v){
				avg += tem.getTemperature();
			}
			avg = avg/v.size();
			avgValue.setText(avg + "");
		}
	}
}
