import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BootstrapServerThread extends Thread {
	ServerSocket sersocket;
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String inputString = null;
	BSProcess bsProcess = new BSProcess();

	BootstrapServerThread(ServerSocket sersocket)
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

				//NameServerList nameserverlist = new NameServerList();
				if(input.readUTF() == "enter")
				{
					String id = input.readUTF();
					String result = bsProcess.enter(BootStrapServer.keyVal, id);
					
					if(result.contains("false"))
					{
						//send successor port.
						String successorPort = String.valueOf((bsProcess.getSuccessor()));
						output.writeUTF(successorPort);
						
					}
					else
					{
						output.writeUTF(result);
						output.flush();
						output.writeUTF(String.valueOf(BootStrapServer.port));
						output.flush();
						output.writeUTF(String.valueOf(bsProcess.getSuccessor()));
					}
				}


			}
			catch (IOException ex)
			{
				Logger.getLogger(BootstrapServerThread.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
