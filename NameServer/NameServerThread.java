import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

public class NameServerThread extends Thread {
	ServerSocket sersocket;
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String inputString = null;
	NSProcess nsprocess;

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
			System.out.println(ex.getMessage());
			// Logger.getLogger(BootstrapServerThread.class.getName()).log(Level.SEVERE, null, ex);
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
				
				String inputcommand = input.readUTF();
				if(inputcommand.equals("enter"))
				{
					System.out.println("Inside NameServerThread enter");
					String id = this.input.readUTF();
					//is in my range?
						//if yes send true
							//else send false
					String range="";
					if(NameServer.keyVal.containsKey(Integer.parseInt(id)))
					{
						System.out.println("Inside NameServerThread ID Found");
						for(int key : NameServer.keyVal.keySet())
						{
							if(key<=Integer.parseInt(id)&& key!=0)
							{
								String value = NameServer.keyVal.get(key);
								range = range+String.valueOf(key)+" "+value+"#";
							}
						}
						
						//taking the port of predecessor from new NS
						String parapreport = input.readUTF();
						System.out.println("Parapredport taken");
						
						output.writeUTF(range);
						output.flush();
						
						//self as new server port
						output.writeUTF(String.valueOf(NameServer.port));
						output.flush();
						
						//self predecessor as pred of new server
						String selfpredaspredofns = String.valueOf(NSProcess.prePort);
						System.out.println("Self predecessor as pred of new server: "+selfpredaspredofns);
						output.writeUTF(selfpredaspredofns);
						output.flush();
						
						//change self pred as new name server
						nsprocess.setPredecessor(Integer.parseInt(parapreport));
						
						
						//remove those keys

						Iterator<Map.Entry<Integer,String>> iter = NameServer.keyVal.entrySet().iterator();
						while (iter.hasNext())
						{
			    	Map.Entry<Integer,String> entry = iter.next();
			    	if((entry.getKey()<=Integer.parseInt(id)&& entry.getKey()!=0))
						{
			        iter.remove();
			    	}
						}
						
					}
					else
					{
						output.writeUTF("false");
						output.flush();
						String successorPort = String.valueOf((nsprocess.getSuccessor()));
						output.writeUTF(successorPort);
						output.flush();
					}
				}
				
				if(inputcommand.equals("chsuccenter"))
				{
					nsprocess.setSuccessor(Integer.parseInt(input.readUTF()));
				}

			}
			catch (IOException ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
}
