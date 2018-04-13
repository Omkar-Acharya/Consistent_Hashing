import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NameServerThread extends Thread {
	ServerSocket sersocket;
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String inputString = null;
	//CoordinatorProcess mycommand = new CoordinatorProcess();
	int td=0;

	NameServerThread(ServerSocket sersocket)
	{
		//Initialize the socket, DataInputStream, DataOutputStream for object
		try
		{	
			this.sersocket = sersocket;
		}
		catch (Exception ex)
		{
			Logger.getLogger(BootstrapServerThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void run()
	{
		while(true){
			try
			{
				this.socket = sersocket.accept();
				this.input = new DataInputStream(socket.getInputStream());
				this.output = new DataOutputStream(socket.getOutputStream());
				
			}
			catch (IOException ex)
			{
				Logger.getLogger(BootstrapServerThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
