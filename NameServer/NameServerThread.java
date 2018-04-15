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
	NSProcess nsprocess;
	int td=0;

	NameServerThread(ServerSocket sersocket, String nameServerId)
	{
		//Initialize the socket, DataInputStream, DataOutputStream for object
		try
		{	
			this.sersocket = sersocket;
			this.nsprocess = new NSProcess(nameServerId);
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
				
				if(input.readUTF() == "enter"){
					
					String id = this.input.readUTF();
					//is in my range?					
						//if yes send true
							//else send false										
					String range="";
					if(NameServer.keyVal.containsKey(Integer.parseInt(id)))
					{	for(int key : NameServer.keyVal.keySet())
						{
							if(key<=Integer.parseInt(id)&& key!=0)
							{
								String value = NameServer.keyVal.get(key);
								range = range+String.valueOf(key)+" "+value+"#";	
								output.writeUTF(range);
								output.flush();
							}
						}
					}
					else
					{
						output.writeUTF("false");
						output.flush();
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
