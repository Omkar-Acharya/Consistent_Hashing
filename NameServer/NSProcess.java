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
		return this.prePort;
	}

	public int getSuccessor()
	{
		return this.succPort;
	}

	void setPredecessor(int port)
	{
		//call this when predecessor is changed
		this.prePort = port;
		System.out.println("Predecessor's port is: "+this.prePort);
	}

	void setSuccessor(int port)
	{
		//call this when successor is changed
		this.succPort = port;
		System.out.println("Successor's port is: "+this.succPort);
	}

	public String lookup()
	{
		return "";
	}

	public String insert()
	{

		return "";
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
