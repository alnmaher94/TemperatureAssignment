package Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread{
	
	private Socket clientSocket;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private TemperatureService temperatureService;

	ConnectionHandler(Socket clientSocket){
		this.clientSocket = clientSocket;
		this.temperatureService = new TemperatureService();
	}
	
	public void run(){
		try {
			this.is = new ObjectInputStream(this.clientSocket.getInputStream());
			this.os = new ObjectOutputStream(this.clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(readCommand()){}
	}
	
	private boolean readCommand(){
		Temperature t;
		try {
			t = (Temperature) is.readObject();
		} catch (Exception e) {
			this.closeSocket();
			System.out.println("Client Socket closed");
			return false;
			
		}
		
		System.out.println("Received command ( " + t + " )");
		
		if(t.getClass().equals(Temperature.class)){
			this.getTemperature();
		} else{
			this.sendError("Invalid command: " + t);
		}
		return true;
	
	}
	
	private void getTemperature(){
		this.temperatureService = new TemperatureService();
		float temp = this.temperatureService.calcTemperature();
		Temperature currentTemp = new Temperature();
		currentTemp.setTemperature(temp);
		this.send(currentTemp);
	}
	
	private void send(Object o){
		try {
			System.out.println("Sending " + o + " to client");
			this.os.reset();
			this.os.writeObject(o);
			this.os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void sendError(String message){
		this.send("Error:" + message); 	
	}
	
	private void closeSocket(){
		
		try {
			this.os.close();
			this.is.close();
			this.clientSocket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
