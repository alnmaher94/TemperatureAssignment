package Client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Server.Temperature;

	public class Client{

		private Socket socket  = null;
		private ObjectOutputStream os = null;
		private ObjectInputStream is = null;
		private String serverIP;
		private int serverPort;
		
		public Client(String serverIP, int serverPort) throws Exception{
			this.serverIP = serverIP;
			this.serverPort = serverPort;
			if(!connectToServer(serverIP, serverPort)){
				System.out.println("Error");
			}
	}
	
	private boolean connectToServer(String serverIP, int serverPort) throws Exception{
		
		this.socket  = new Socket(serverIP, serverPort);
		try {		
			this.os = new ObjectOutputStream(socket.getOutputStream());
			this.is = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connecting to server...");
		return true;
	}
	
	public Temperature getTemperature(){
		Temperature t = new Temperature();
		Temperature theTemperature;
		System.out.println("Sending Command (" + t + ") to the server...");
		this.send(t);
		try{
			theTemperature = (Temperature) receive();
			if(theTemperature == null)
				System.out.println("Could not get Object from Server");
			System.out.println("Server responded with " + theTemperature.getTemperature() + " " + theTemperature.getSampleNumber());
			return theTemperature;
		} catch(ClassCastException e){
			e.printStackTrace();
			return null;
		}	
	}
	
	private void send(Object o){
		
		try {
			System.out.println("Sending object " + o);
			this.os.writeObject(o);
			this.os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private Object receive(){
		Object o = null;
	
	
		try {
			System.out.println("About to receive object...");
			o = is.readObject();
			System.out.println("Object received...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return o;
		
	}
	
	public String getServerIP(){
		return this.serverIP;
	}
	public int getServerPort(){
		return this.serverPort;
	}
	
}
