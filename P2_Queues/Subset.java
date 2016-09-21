import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Subset Class to test RandomizedQueue
 * Assignment 2
 * @author Hendrik Kits van Heyningen
 */
public class Subset 
{
	public static void main(String [] args)
	{
		//Parse our command line arguments to get k
        int k = 0;
        try 
        {
            k = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
            System.exit(1);
        }
        
        RandomizedQueue<String> rQ = new RandomizedQueue<String>();
        String [] userInputs = StdIn.readAllStrings();
        
        //Loop over all input strings and enqueue them into our random queue
        for (String string : userInputs)
        	rQ.enqueue(string);
        
        //To print a random subset of k of them, dequeue k things from random queue
        for (int i = 0; i < k; i++)
        	StdOut.println(rQ.dequeue());
	}
}
