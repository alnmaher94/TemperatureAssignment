package Server;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConnectionHandler extends Thread{
	
	private Socket clientSocket;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private TemperatureService temperatureService;
	private Date connectTime, disconnectTime;
	private int samplesTaken;
	private String ipAddress;
	private File logs;

	ConnectionHandler(Socket clientSocket, File logs){
		this.clientSocket = clientSocket;
		this.temperatureService = new TemperatureService();
		
		this.logs = logs;
		this.connectTime = Calendar.getInstance().getTime();
		this.ipAddress = clientSocket.getInetAddress().toString();
		this.samplesTaken = 0;
	}
	
	public void run(){
		try {
			this.is = new ObjectInputStream(this.clientSocket.getInputStream());
			this.os = new ObjectOutputStream(this.clientSocket.getOutputStream());
		} catch (IOException e) {
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
			disconnectTime = Calendar.getInstance().getTime();

			try {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/YY (hh:mm:ss)");		
				String connect =  format.format(connectTime);
				String disconnect = format.format(disconnectTime);
				FileWriter fw = new FileWriter(logs,true);
				fw.append(connect + " : " + disconnect
								+ " : " + ipAddress + " : " + samplesTaken+"\n");
				fw.flush();
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("Client Socket at" + 
								clientSocket.getInetAddress().toString() 
								+ " closed");
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
			samplesTaken++;
		} catch (IOException e) {
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
			e.printStackTrace();
		}
	
	}

}
