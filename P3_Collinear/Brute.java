import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Brute Class for finding collinear points
 * Assignment 3
 * @author Hendrik Kits van Heyningen
 */
public class Brute {
	
	public static void main (String args[]) {
		// Set up drawing canvas
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        
      //String filename = args[0]; //get filename from argument
        String filename = "collinear/input56.txt";
        In in = new In(filename);
        int N = in.readInt(); //first item in the file is the size of the file
        
        Point [] points = new Point[N]; //make this the number of points
        
        for (int i = 0; i < N; i++) //loop through all the points and store them
        {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }
        
        //loop through each point and check the slopes trying to find subsets of 4 points that are collinear
        for (int i = 0; i < N; i++)
        {
        	for (int j = i + 1; j < N; j++)
        	{
        		for (int k = j + 1; k < N; k++)
        		{
        			//if the first three points lie on a line, check for a last point, otherwise, don't bother
        			if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]))
        			{
	        			//check for a last (4th) point being on the line
	        			for (int l = k + 1; l < N; l++)
	        			{
	        				//we know the first 3 points are in a line so just need to make sure the fourth is
	        				if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[l]))
	        				{
	        					Point[] segment = {points[i], points[j], points[k], points[l]}; //make
	        					Arrays.sort(segment); //sort it based on the compareTo method
	        					printLine(segment); //print the segment we found
	        					segment[0].drawTo(segment[3]);      					
	        				}
	        			}
	        			
        	
        			}
        		}
        	}
        }
	}
	
    // Method to print collinear segments
    private static void printLine(Point[] segment)
    {
        StdOut.println(segment[0].toString() + " -> " + 
                       segment[1].toString() + " -> " + 
                       segment[2].toString() + " -> " + 
                       segment[3].toString());
    }
}
