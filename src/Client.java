import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client{

	private Socket socket  = null;
	private ObjectOutputStream os = null;
	private ObjectInputStream is = null;
	
	public Client(String serverIP, int serverPort) throws Exception{
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.out.println("Connecting to server...");
			
		
		return true;
	}
	
	public Temperature getTemperature(){
		String theTempCommand = "GetTemperature";
		Temperature theTemperature;
		System.out.println("Sending Command (" + theTempCommand + ") to the server...");
		this.send(theTempCommand);
		
		theTemperature = (Temperature) receive();
		System.out.println("Server responded with " + theTemperature.getTemperature() + " " + theTemperature.getSampleNumber());
		return theTemperature;
	}
	
	private void send(Object o){
		
		try {
			System.out.println("Sending object " + o);
			this.os.writeObject(o);
			this.os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return o;
		
	}
	
}
