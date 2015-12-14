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
			
			try{
			Temperature t = client.getTemperature();
		
			System.out.println("Request for Temperature Made");

			app.addData(t);
			} catch(NullPointerException e){
				System.out.println("Attempting to reconnect to server");
				try {
					client = new Client(client.getServerIP(),client.getServerPort());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			while(Calendar.getInstance().getTimeInMillis() - startTime < interval){}	
		}
	}
}
