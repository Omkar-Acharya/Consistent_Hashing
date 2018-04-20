import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.TreeMap;
import java.util.*;

public class BSProcess
{
	static int prePort=BootStrapServer.port;
	static int succPort=BootStrapServer.port;

	public int getPredecessor()
	{
		return BSProcess.prePort;
	}

	public int getSuccessor()
	{
		return BSProcess.succPort;
	}

	void setPredecessor(int port)
	{
		//call this when predecessory is changed
		BSProcess.prePort = port;
		System.out.println("Predecessor's port is: "+BSProcess.prePort);
	}

	void setSuccessor(int port)
	{
		//call this when successor is changed
		BSProcess.succPort = port;
		System.out.println("Successor's port is: "+BSProcess.succPort);
	}

	public void printKeyVal(TreeMap<Integer, String> keyVal)
	{
			System.out.println("printing bootstrap server TreeMap");
			for(int key :	keyVal.keySet())
			{
				System.out.println(" key: "+key+" value: "+keyVal.get(key));
			}
	}

	public void lookup(int key)
	{
		//Boolean inMyRange =false;
		
		//key out of range
		if(key < 0 || key > 1023)
		{
			System.out.println("Key Not Found");
		}

		if(BootStrapServer.keyVal.containsKey(key))
		{
			String value = "value is"+BootStrapServer.keyVal.get(key);
			value = value+" Server Id's that are traversed: 0";
			System.out.println(value);
		}
		else
		{
			try
			//send to successor
			{
				Socket socket = new Socket("localhost", this.succPort);
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeUTF("lookup");
				dos.flush();
				dos.writeUTF(String.valueOf(key));
				dos.flush();
				dos.writeUTF(String.valueOf(BootStrapServer.serverId));
				dos.flush();
				dos.close();
				socket.close();
			}
			catch(Exception ex)
			{
				System.out.println("ex 0"+ex.getMessage());
			}
		}
	}

	private Boolean IsinMyRange(int key, TreeMap<Integer, String> keyVal) {


		return true;
	}

	public String insert(int deletekey, TreeMap<Integer, String> keyVal)
	{

		return "";
	}

	public String delete(int deletekey, TreeMap<Integer, String> keyVal)
	{

		return "";
	}
	public String enter(TreeMap<Integer, String> keyVal, String id)
	{
		String range="";

		System.out.println("inside enter of BS process");
		
		int firstKey = keyVal.firstKey();
		int lastKey = keyVal.lastKey();
		System.out.println("firstKey is "+firstKey+" last key is "+lastKey);

		if(Integer.parseInt(id)>=firstKey && Integer.parseInt(id)<=lastKey)
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
