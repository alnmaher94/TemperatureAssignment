package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Date;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Server.Temperature;

public class ClientApp extends JFrame implements ComponentListener,ChangeListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Client client;
	private JTextField intervalTF;
	private JTextField portTextField;
	private ClientThread thread;
	private JTextField ipTextField;
	private JLabel maxValue;
	private JLabel minValue;
	private JLabel avgValue;
	private JLabel lastValue;
	private JLabel firstValue;
	private Vector<Temperature> v ;
	private CustomCanvas c;
	private JSlider slider;
	private JTextField rangeTF;
	private JPanel sliderPanel;
	private int range;
	
	ClientApp(){
		
		super("Temperature Application");
		this.range = 10;
		this.v = new Vector<Temperature>();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.addComponentListener(this);
		
		//Side Panel Labels
		JPanel sidePanel = new JPanel(new GridLayout(5,2));
		sidePanel.setBackground(new Color(33,33,33));
		
		JLabel maxLabel = new JLabel("Max: ");
		maxLabel.setForeground(Color.white);
		sidePanel.add(maxLabel);
		maxValue = new JLabel("");
		maxValue.setForeground(Color.red);
		sidePanel.add(maxValue);
		
		JLabel avgLabel = new JLabel("Average: ");
		avgLabel.setForeground(Color.white);
		sidePanel.add(avgLabel);
		avgValue = new JLabel("");
		avgValue.setForeground(Color.yellow);
		sidePanel.add(avgValue);
		
		JLabel minLabel = new JLabel("Min: ");
		minLabel.setForeground(Color.white);
		sidePanel.add(minLabel);
		minValue = new JLabel("");
		minValue.setForeground(Color.green);
		sidePanel.add(minValue);
		
		JLabel lastReading = new JLabel("Last Reading: ");
		lastReading.setForeground(Color.white);
		sidePanel.add(lastReading);
		lastValue = new JLabel("");
		lastValue.setForeground(Color.white);
		sidePanel.add(lastValue);
		
		JLabel firstReading = new JLabel("First Reading: ");
		firstReading.setForeground(Color.white);
		sidePanel.add(firstReading);
		firstValue = new JLabel("");
		firstValue.setForeground(Color.white);
		sidePanel.add(firstValue);
		
		//Canvas
		c = new CustomCanvas();
		c.setPreferredSize(new Dimension(1000,500));
	
		//Range Slider
		sliderPanel = new JPanel();
		sliderPanel.setBackground(new Color(33,33,33));
		slider = new JSlider();
		slider.setMaximum(50);
		slider.setMinimum(5);
		slider.setValue(10);
		slider.addChangeListener(this);
		rangeTF = new JTextField();
		rangeTF.setEditable(false);
		rangeTF.setPreferredSize(new Dimension(20, 20));
		rangeTF.setText(range + "");
		sliderPanel.add(slider);
		sliderPanel.add(rangeTF);
		
		contentPane.add(sidePanel,BorderLayout.WEST);
		contentPane.add(c, BorderLayout.CENTER);
		contentPane.add(sliderPanel,BorderLayout.SOUTH);
		
		//Launch PopUp
		while(!popup()){}
		
		this.pack();
		this.setVisible(true);
	}
	
	public static void main (String[] args){
		new ClientApp();
	}


	
	private boolean popup(){ //IP Address PopUp
			
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
		
		if(result == JOptionPane.OK_OPTION){ //Ensure correct settings entered
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
		maxValue.setText(Collections.max(v).getTemperature()+" deg");
		minValue.setText(Collections.min(v).getTemperature() + " deg");
		if(!v.isEmpty()){
			for(Temperature tem : v){
				avg += tem.getTemperature();
			}
			avg = avg/v.size();
			avgValue.setText(avg + " deg");
			
			Date d = v.get(v.size() - 1).getSampleDate();
			lastValue.setText(d.toString());
			
			d = v.get(0).getSampleDate();
			firstValue.setText(d.toString());
		}
				
		updateCanvas();
		sliderPanel.repaint();
	}
	
	private void updateCanvas(){
		c.drawGraph(v,range);
	}

	@Override
	public void componentResized(ComponentEvent e) {
			updateCanvas();
	}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(slider)){
			int value = slider.getValue();
			rangeTF.setText(value + "");
			range = value;
			updateCanvas();
		}
	}
}
