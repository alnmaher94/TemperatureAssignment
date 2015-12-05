import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler {
	
	private Socket clientSocket;
	private ObjectInputStream is;
	private ObjectOutputStream os;

	ConnectionHandler(Socket clientSocket){
		this.clientSocket = clientSocket;
	}
	
	public void init(){
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
		String s = null;
		
		try {
			s = (String) is.readObject();
		} catch (Exception e) {
			this.closeSocket();
			System.out.println("Client Socket closed");
			return false;
			
		}
		
		System.out.println("Received command ( " + s + " )");
		
		if(s.equalsIgnoreCase("getTemperature")){
			this.getTemperature();
		} else{
			this.sendError("Invalid command: " + s);
		}
		return true;
	
	}
	
	private void getTemperature(){
		Temperature currentTemp = new Temperature();
		this.send(currentTemp);
	}
	
	private void send(Object o){
		try {
			System.out.println("Sending " + o + " to client");
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