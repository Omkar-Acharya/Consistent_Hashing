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

					int firstKey = NameServer.keyVal.firstKey();
					int lastKey = NameServer.keyVal.lastKey();
					System.out.println("firstKey is "+firstKey+" last key is "+lastKey);
					if(Integer.parseInt(id)>=firstKey && Integer.parseInt(id)<=lastKey)
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

				if(inputcommand.equals("lookup"))
				{
					String lookupkey = input.readUTF();
					String BSorNSservertraversed = input.readUTF();
					nsprocess.lookup(lookupkey, BSorNSservertraversed);
				}

				if(inputcommand.equals("chPredExit"))
				{
					String range = input.readUTF();
					int predExitPort =Integer.parseInt(input.readUTF());
					NSProcess.prePort = predExitPort;
					String[] rangeArray = range.split("#");

					output.writeUTF(NameServer.nameServerId);

					for(String pair : rangeArray)
					{
						int pairKey = Integer.parseInt(pair.split(" ")[0]);
						String pairValue =  pair.split(" ")[1];
						NameServer.keyVal.put(pairKey,pairValue);
					}

					System.out.println("port predecessor is: "+NSProcess.prePort);
					System.out.println("range after exit is:");
					for(int key : NameServer.keyVal.keySet())
					{
						System.out.println(key+": "+NameServer.keyVal.get(key));
					}

				}
				if(inputcommand.equals("chSuccExit"))
				{
					int newSucc =Integer.parseInt(input.readUTF());
					NSProcess.succPort = newSucc;
					System.out.println("on EXIT successor changed to: "+NSProcess.succPort);
				}

				if(inputcommand.equals("insert"))
				{
					String insertkey = input.readUTF();
					String insertvalue = input.readUTF();
					String BSorNSservertraversed = input.readUTF();
					nsprocess.insert(insertkey, insertvalue, BSorNSservertraversed);
				}

				if(inputcommand.equals("tellmeyourid"))
				{
					output.writeUTF(NameServer.nameServerId);
					output.flush();
				}

			}
			catch (IOException ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}
}
