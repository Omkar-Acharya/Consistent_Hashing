import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.*;
public class BSProcess
{
	int prePort=0;
	int succPort=0;

	public int getPredecessor()
	{
		return this.prePort;
	}

	public int getSuccessor()
	{
		return this.succPort;
	}

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

	public void printKeyVal(Map<Integer, String> keyVal)
	{
			System.out.println("printing bootstrap server Map");
			for(int key :	keyVal.keySet())
			{
				System.out.println(" key: "+key+" value: "+keyVal.get(key));
			}
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
	public String enter(Map<Integer, String> keyVal, String id)
	{
		String range="";

		System.out.println("inside enter of BS process");

		if(keyVal.containsKey(Integer.parseInt(id)))
		{
			System.out.println("key is here");
			for(int key : keyVal.keySet())
			{
				if(key<=Integer.parseInt(id)&& key!=0)
				{
					String value = keyVal.get(key);
					range = range+String.valueOf(key)+" "+value+"#";

				}
			}
			//delete those keyS
			Iterator<Map.Entry<Integer,String>> iter = keyVal.entrySet().iterator();
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
			return "false";
		}
		printKeyVal(keyVal);
		return range;
	}

}
