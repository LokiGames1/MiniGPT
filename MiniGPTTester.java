import java.io.IOException;

public class MiniGPTTester
{
	public static void main (String [] args)
	{
		try
		{
			MiniGPT myBot = new MiniGPT ("RandomText.txt", 7);
			System.out.println (myBot.printSeed());
			myBot.generateText("Output.txt", 100);



		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}