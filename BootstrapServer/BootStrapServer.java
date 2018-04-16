import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;

public class BootStrapServer

{
	public static Map<Integer, String> keyVal = new TreeMap<Integer, String>();
	public static int port=0;

	public static void main(String[] args)
	{
		int  serverId = 0, key = 0;
		String value = null;

		//Taking File from command-line parameters
		try
		{


			Scanner inputFile = new Scanner(new File(args[0]));
			String input = inputFile.nextLine();

			Scanner myscanner = new Scanner(input);

			if(myscanner.hasNext())
			{
				serverId = Integer.parseInt(myscanner.next());
			}

			input = inputFile.nextLine();
			myscanner = new Scanner(input);

			if(myscanner.hasNext()){
				port = Integer.parseInt(myscanner.next());
			}

			do{
				input = inputFile.nextLine();
				myscanner = new Scanner(input);

				//read the key value pairs and store them
				if(myscanner.hasNext()){
					key = Integer.parseInt(myscanner.next());
				}

				if(myscanner.hasNext()){
					value = myscanner.next().toString();
				}

				//storing the key value in hashmap
				keyVal.put(key, value);

			}while(inputFile.hasNextLine());


		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found!");
		}

		System.out.println("Bootstrap Server Started");

		try
		{
			// Create server socket
			ServerSocket serSocket = new ServerSocket(port);
			//Creating thread object for Socket
			BSProcess process = new BSProcess();
			BootstrapServerThread bootstrapserverthread = new BootstrapServerThread(serSocket);
			//starting the thread
			bootstrapserverthread.start();


			while (true)
			{
				Thread.sleep(513);
				String command = takeInput();
				if(command.contains("lookup"))
				{
					int lookupkey =Integer.parseInt(command.split(" ")[1]);
					System.out.println(process.lookup(lookupkey,keyVal));
				}
				else
					if(command.contains("insert"))
					{
						int insertkey=Integer.parseInt(command.split(" ")[1]);
						System.out.println(process.insert(insertkey,keyVal));
					}
					else
						if(command.contains("delete"))
						{
							int deletekey =Integer.parseInt(command.split(" ")[1]);
							System.out.println(process.delete(deletekey,keyVal));
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
