package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static int portNumber = 5050;
	
	public static void main(String[] args){
		
		boolean listening = true;
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(portNumber);
			System.out.println("Server startd on port " + portNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(listening){
			Socket clientSocket = null;
			try {
				System.out.println("Waiting for Client...");
				clientSocket = socket.accept();
				System.out.println("Client accepted from IP " + clientSocket.getInetAddress().toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ConnectionHandler con = new ConnectionHandler(clientSocket);
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
