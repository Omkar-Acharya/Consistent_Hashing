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
				System.out.println("conn accepted by bootstrap");

				//NameServerList nameserverlist = new NameServerList();
				String inputcommand = input.readUTF();
				if(inputcommand.equals("enter"))
				{
					System.out.println("inside bootrstrap enter");
					String id = input.readUTF();
					String result = bsProcess.enter(BootStrapServer.keyVal, id);

					if(result.contains("false"))
					{
						//send successor port.
						output.writeUTF("false");
						output.flush();
						String successorPort = String.valueOf((bsProcess.getSuccessor()));
						output.writeUTF(successorPort);

					}
					else
					{
						//taking the port of predecessor from new NS
						String parapreport = input.readUTF();
						
						System.out.println("range to send is: "+result);
						output.writeUTF(result);
						output.flush();
						
						//self as new server port
						output.writeUTF(String.valueOf(BootStrapServer.port));
						output.flush();
						
						//self predecessor as pred of new server
						output.writeUTF(String.valueOf(BSProcess.prePort));
						output.flush();
						
						//change self predecessor as new name server
						bsProcess.setPredecessor(Integer.parseInt(parapreport));				
						
					}
				}
				
				if(inputcommand.equals("chsuccenter"))
				{
					bsProcess.setSuccessor(Integer.parseInt(input.readUTF()));
				}
				
				if(inputcommand.equals("lookup"))
				{
					String flag = input.readUTF();
					
					if(flag.equals("true"))
					{
						String value = input.readUTF();
						String Servertraversed = input.readUTF();
						System.out.println("Value is: "+value+" and Server traversed are: "+Servertraversed);
					}
					else if(flag.equals("false"))
					{
						String value = input.readUTF();
						String Servertraversed = input.readUTF();
						System.out.println("Value is: "+value+" and Server traversed are: "+Servertraversed);
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
