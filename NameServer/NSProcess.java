import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NSProcess {
	int prePort=0;
	int succPort=0;
	
	void setPredecessor(int port)
	{		
		//call this when predecessory is changed
		this.prePort = port;	
	}
	
	void setSuccessor(int port)
	{
		//call this when successor is changed
		this.succPort = port;
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

	public void enter(int id, String ip, int port)
	{
		try {
			Socket socket = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis =	new DataInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
