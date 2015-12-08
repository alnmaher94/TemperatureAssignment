import java.util.Calendar;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextPane;

public class ClientThread extends Thread{
	
	private Client client;
	private int interval;
	private ClientApp app;
	private Vector<Temperature> v;
	
	ClientThread(Client client, int interval, ClientApp app){
		this.client = client;
		this.interval = interval * 1000;
		this.app = app;
		v = new Vector<Temperature>();
	}
	
	public void run(){
		while(true){
			long startTime = Calendar.getInstance().getTimeInMillis();
			
			while(Calendar.getInstance().getTimeInMillis() - startTime < interval){}
			
			Temperature t = client.getTemperature();
			System.out.println("Request Made");

			app.addData(t);
		}
	}
}
