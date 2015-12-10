package Client;
import java.util.Calendar;

import Server.Temperature;

public class ClientThread extends Thread{
	
	private Client client;
	private int interval;
	private ClientApp app;
	
	ClientThread(Client client, int interval, ClientApp app){
		this.client = client;
		this.interval = interval * 1000;
		this.app = app;
	}
	
	public void run(){
		while(true){			
			long startTime = Calendar.getInstance().getTimeInMillis();	
			
			Temperature t = client.getTemperature();
			System.out.println("Request for Temperature Made");

			app.addData(t);
			
			while(Calendar.getInstance().getTimeInMillis() - startTime < interval){}	
		}
	}
}
