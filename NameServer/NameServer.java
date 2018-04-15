import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NameServer {
	@SuppressWarnings("SleepWhileInLoop")
	
	public static Map<Integer, String> keyVal = new HashMap<Integer, String>();	
	
	public static void main(String[] args) throws Exception
	{
		InetAddress localhost = InetAddress.getLocalHost();
		Socket nclientSocket=null;
		int port = 0, bootStrapServerport = 0;
		String bootStrapServerIp=null, nameServerId = null, nameServerIp = localhost.getHostAddress();
		

		//Taking input from File as Command Line parameters
		try {
			Scanner inputFile = new Scanner(new File(args[0]));
			String input = inputFile.nextLine();

			Scanner myscanner = new Scanner(input);

			if(myscanner.hasNext())
			{
				nameServerId = myscanner.next();
			}

			input = inputFile.nextLine();
			myscanner = new Scanner(input);

			if(myscanner.hasNext()){
				port = Integer.parseInt(myscanner.next());
			}

			input = inputFile.nextLine();
			myscanner = new Scanner(input);

			if(myscanner.hasNext()){
				bootStrapServerIp = myscanner.next().toString();
			}

			if(myscanner.hasNext()){
				bootStrapServerport = Integer.parseInt(myscanner.next());
			}

		} catch (FileNotFoundException ex) {
			System.out.println("No File Found!");
		}

		try
		{
			// Create server socket
			ServerSocket serSocket = new ServerSocket(port);
			//Creating thread object for Socket
			NameServerThread nameserverthread = new NameServerThread(serSocket, nameServerId);
			//starting the thread
			nameserverthread.start();
			NSProcess nsprocess = new NSProcess(nameServerId);

			while (true)
			{
				Thread.sleep(513);
				String command = takeInput();
				if(command.contains("enter"))
				{
					nsprocess.enter(command, nameServerId, bootStrapServerIp, bootStrapServerport);
				}
				else
				{
					
				}
			}
		}
		catch (Exception ex) 
		{
			System.out.println("exceptionnn" + ex + " exception " + ex.getMessage());
		}
	}

	public static String takeInput() throws Exception
	{
		System.out.print("Bootstrap Server> ");
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader buffer = new BufferedReader(reader);
		return buffer.readLine();
	}
}
