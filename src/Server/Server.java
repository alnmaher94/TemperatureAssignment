package Server;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;

public class Server {

	private static int portNumber = 5050;
	
	private static File setupLogs(){ //Creates a log file and headings.
		String date = Calendar.getInstance().getTime().toString();
		date = date.replaceAll(" ", "-");
		date = date.replaceAll(":", "-");
		System.out.println(date);
		File logs = new File("../logs-"+date+".txt");
		try {
			logs.createNewFile();
			FileWriter fw = new FileWriter(logs);
			fw.write("Connect Time	:Disconnect Time	:IP Adress	:Samples Taken\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not create log file");
			e.printStackTrace();
		}
		
		return logs;
	}
	
	public static void main(String[] args){
		
		File logs = setupLogs();
		
		boolean listening = true;
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(portNumber);
			System.out.println("Server startd on port " + portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(listening){
			Socket clientSocket = null;
			try {
				System.out.println("Waiting for Client...");
				clientSocket = socket.accept();
				System.out.println("Client accepted from IP " + clientSocket.getInetAddress().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ConnectionHandler con = new ConnectionHandler(clientSocket,logs);
			con.start();
		}
		
		 // Server is no longer listening for client connections - time to shut down.
        try 
        {
            System.out.println("Closing down the server socket gracefully.");
            socket.close();
        } 
        catch (IOException e) 
        {
            System.err.println("Could not close server socket. " + e.getMessage());
            
        }
	}
}
