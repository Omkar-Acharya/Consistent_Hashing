import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;

public class BSProcess 
{
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

	public String lookup(int key, Map<Integer, String> keyVal) 
	{
		Boolean inMyRange =false;

		String range="";

		if(keyVal.containsKey(key))
		{
			range = "value is"+keyVal.get(key);
			range = range+" server traversed: 0";
		}
		else
		{
			try
			//send to successor
			{
				Socket socket = new Socket("localhost", this.succPort);
				String messageToSend = "lookup "+key;
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF(messageToSend);
				dos.flush();
				DataInputStream dis =	new DataInputStream(socket.getInputStream());				
				range = dis.readUTF();
				dis.close();
				dos.close();
				socket.close();
			}
			catch(Exception ex)
			{
				System.out.println("ex 0"+ex.getMessage());
			}
		}


		return range;
		// TODO Auto-generated method stub

	}

	private Boolean IsinMyRange(int key, Map<Integer, String> keyVal) {


		return true;
	}

	public String insert(int deletekey, Map<Integer, String> keyVal) 
	{

		return "";
	}

	public String delete(int deletekey, Map<Integer, String> keyVal) 
	{

		return "";
	}

}
