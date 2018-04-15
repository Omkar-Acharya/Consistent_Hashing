import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NSProcess {
	int prePort=0;
	int succPort=0;
	String myid = null;
	
	public NSProcess(String myid) {
		this.myid = myid;
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

	public void enter(String command, String id, String ip, int port)
	{
		try {
			Socket socket = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis =	new DataInputStream(socket.getInputStream());
			
			dos.writeUTF(command);
			dos.flush();
			dos.writeUTF(id);
			dos.flush();
			
			if(dis.readUTF() == "false")
			{
				succPort = Integer.parseInt(dis.readUTF());
				
				enter(command, id, "localhost", succPort);
			}
			else
			{
				//take the range
				String range = dis.readUTF();
				
				String[] keyValues = range.split("#");
				
				for(String pairs : keyValues)
				{
					String key = pairs.split(" ")[0];
					String value = pairs.split(" ")[1];
					NameServer.keyVal.put(Integer.parseInt(key), value);
				}				
				setPredecessor(Integer.parseInt(dis.readUTF()));
				setSuccessor(Integer.parseInt(dis.readUTF()));				
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
