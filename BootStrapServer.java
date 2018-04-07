import java.io.File;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.util.Scanner;

public class BootStrapServer {
	public static void main(String[] args)
	{
		int port = 0, serverId = 0;

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
				
			}while(inputFile.hasNextLine());
			

		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found!");
		}
	}
}
