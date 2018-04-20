import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NSProcess {
	static int prePort=0;
	static int succPort=0, nextsuccPort = 0;
	String myid = null;

	public NSProcess(String myid) {
		this.myid = myid;
	}

	public  int getPredecessor()
	{
		return NSProcess.prePort;
	}

	public int getSuccessor()
	{
		return NSProcess.succPort;
	}

	void setPredecessor(int port)
	{
		//call this when predecessor is changed
		NSProcess.prePort = port;
		System.out.println("Predecessor's port is: "+NSProcess.prePort);
	}

	void setSuccessor(int port)
	{
		//call this when successor is changed
		NSProcess.succPort = port;
		System.out.println("Successor's port is: "+NSProcess.succPort);
	}

	public void lookup(String lookupkey, String BSorNSId)
	{
		//Servers traversed
		String serverstraversed = BSorNSId+"#"+NameServer.nameServerId;
		System.out.println("ServerIds Traversed: "+serverstraversed);

		//key out of range
		if((Integer.parseInt(lookupkey) < Integer.parseInt(NameServer.nameServerId)) && (!NameServer.keyVal.containsKey(Integer.parseInt(lookupkey))))
		{
			System.out.println("Inside NS lookup if...if key not found");
			try {
				Socket lookupsocketnotfound = new Socket("localhost", NameServer.bootStrapServerport);
				DataOutputStream dos = new DataOutputStream(lookupsocketnotfound.getOutputStream());
				dos.writeUTF("lookup");
				dos.flush();
				dos.writeUTF("false");
				dos.flush();
				dos.writeUTF("Key Not Found");
				dos.flush();
				dos.writeUTF(serverstraversed);
				dos.flush();
				dos.close();
				lookupsocketnotfound.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if(NameServer.keyVal.containsKey(Integer.parseInt(lookupkey)))
		{
			System.out.println("Inside NS lookup...if key found");
			String value = NameServer.keyVal.get(Integer.parseInt(lookupkey));

			//sending the value and servers traversed to bootstrap server
			try {
				Socket lookupsocketfound = new Socket("localhost", NameServer.bootStrapServerport);
				DataOutputStream dos = new DataOutputStream(lookupsocketfound.getOutputStream());
				dos.writeUTF("lookup");
				dos.flush();
				dos.writeUTF("true");
				dos.flush();
				dos.writeUTF(value);
				dos.flush();
				dos.writeUTF(serverstraversed);
				dos.flush();
				dos.close();
				lookupsocketfound.close();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{
			System.out.println("Inside NS lookup...if key not with this name server...going to successor NS");
			try
			//send lookup key to successor
			{
				Socket sockettosuccessor = new Socket("localhost", NSProcess.succPort);
				DataOutputStream dos = new DataOutputStream(sockettosuccessor.getOutputStream());
				dos.writeUTF("lookup");
				dos.flush();
				dos.writeUTF(lookupkey);
				dos.flush();
				dos.writeUTF(serverstraversed);
				dos.flush();
				dos.close();
				sockettosuccessor.close();
			}
			catch(Exception ex)
			{
				System.out.println("ex 0"+ex.getMessage());
			}
		}
	}

	public void insert(String insertkey, String insertvalue, String BSorNSId)
	{
		System.out.println("Inside NS insert...");
		String serverstraversed = BSorNSId+"#"+NameServer.nameServerId;

		int firstKey = NameServer.keyVal.firstKey();
		int lastKey = NameServer.keyVal.lastKey();

		if(Integer.parseInt(insertkey) < Integer.parseInt(NameServer.nameServerId))
		{
			System.out.println("Inside NS entry....key is to be inserted into this NS");
			NameServer.keyVal.put(Integer.parseInt(insertkey), insertvalue);

			//Send details to Bootstrap after inserting kay value
			try {
				Socket insertkeysocket = new Socket("localhost", NameServer.bootStrapServerport);
				DataOutputStream dos = new DataOutputStream(insertkeysocket.getOutputStream());
				dos.writeUTF("insert");
				dos.flush();
				dos.writeUTF(serverstraversed);
				dos.flush();
				dos.close();
				insertkeysocket.close();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else
		{
			System.out.println("Inside NS insert...key not inserted in this name server...going to successor NS");
			try
			//send lookup key to successor
			{
				Socket sockettosuccessor = new Socket("localhost", NSProcess.succPort);
				DataOutputStream dos = new DataOutputStream(sockettosuccessor.getOutputStream());
				dos.writeUTF("insert");
				dos.flush();
				dos.writeUTF(insertkey);
				dos.flush();
				dos.writeUTF(insertvalue);
				dos.flush();
				dos.writeUTF(serverstraversed);
				dos.flush();
				dos.close();
				sockettosuccessor.close();
			}
			catch(Exception ex)
			{
				System.out.println("ex 0"+ex.getMessage());
			}
		}
	}

	public String delete()
	{

		return "";
	}
	public void printKeyVal()
	{
		System.out.println("printing name server Map");
		for(int key :	NameServer.keyVal.keySet())
		{
			System.out.println(" key: "+key+" value: "+	NameServer.keyVal.get(key));
		}
	}

	public void exit(String command, String id)
	{
			try
			{
				//give my successor range and predecessor ID
				String commandSuccessor = "chPredExit";
				String range ="";
				int predExit = this.getPredecessor();
				int succID=0;
				for(int key : NameServer.keyVal.keySet())
				{
					range = range+key+" "+NameServer.keyVal.get(key);
					range = range+"#";
				}
				Socket sockettosuccessor = new Socket("localhost", NSProcess.succPort);
				DataOutputStream dos = new DataOutputStream(sockettosuccessor.getOutputStream());
				DataInputStream dis = new DataInputStream(sockettosuccessor.getInputStream());
				dos.writeUTF(commandSuccessor);
				dos.flush();
				dos.writeUTF(range);
				dos.flush();
				dos.writeUTF(String.valueOf(predExit));
				dos.flush();
				succID =Integer.parseInt(dis.readUTF());
				dos.close();
				dis.close();
				sockettosuccessor.close();

				//give my predecessor its new successor
				String commandSuccessorExit = "chSuccExit";
				int succExit = this.getSuccessor();

				Socket sockettopred = new Socket("localhost", this.getPredecessor());
				DataOutputStream dospred = new DataOutputStream(sockettopred.getOutputStream());
				dospred.writeUTF(commandSuccessorExit);
				dospred.flush();
				dospred.writeUTF(String.valueOf(succExit));
				dospred.flush();
				dospred.close();
				sockettopred.close();

				System.out.println("Successful exit!");
				System.out.println("Range given to: "+succID);
				System.out.println("Range given is: "+range);

			}
			catch(Exception ex)
			{
					System.out.println("exception is exit"+ex.getMessage());
			}
	}

	public void enter(String command, String id, String ip, int port)
	{
		try
		{
			System.out.println(command+"id: "+id+"ip: "+ip+"port: "+port);
			Socket socket = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis =	new DataInputStream(socket.getInputStream());
			System.out.println("conn established");
			dos.writeUTF(command);
			dos.flush();
			dos.writeUTF(id);
			dos.flush();
			//Sending this port to set the predecossor of this NS successor
			System.out.println("Sending parapredport: "+NameServer.port);
			dos.writeUTF(String.valueOf(NameServer.port));
			dos.flush();


			String range = dis.readUTF();

			if(range.equals("false"))
			{
				System.out.println("inside name process false");
				nextsuccPort = Integer.parseInt(dis.readUTF());

				//closing the socket
				dis.close();
				dos.close();
				socket.close();

				enter(command, id, "localhost", nextsuccPort);
			}
			else
			{
				//take the range
				System.out.println("inside name process true");
				System.out.println("range to send is: "+range);
				String[] keyValues = range.split("#");

				for(String pairs : keyValues)
				{
					String key = pairs.split(" ")[0];
					String value = pairs.split(" ")[1];
					NameServer.keyVal.put(Integer.parseInt(key), value);
				}
				setSuccessor(Integer.parseInt(dis.readUTF()));
				setPredecessor(Integer.parseInt(dis.readUTF()));

				//closing the socket
				dis.close();
				dos.close();
				socket.close();

				//tell current predecessor to change its successor as self port
				Socket socketPred = new Socket("localhost", getPredecessor());
				DataOutputStream dosPred = new DataOutputStream(socketPred.getOutputStream());
				dosPred.writeUTF("chsuccenter");
				dosPred.flush();
				dosPred.writeUTF(String.valueOf(NameServer.port));
				dosPred.flush();
				dosPred.close();
				socketPred.close();

				this.printKeyVal();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Exception is: "+e.getMessage());
		}
	}
}
