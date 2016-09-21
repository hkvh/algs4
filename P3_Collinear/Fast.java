import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Fast Class for finding 4 collinear points using a sort
 * Assignment 3
 * @author Hendrik Kits van Heyningen
 */
public class Fast {
	
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
        
        //loop through each point and check the slopes to see if four or more have the same slope
        for (int i = 0; i < N; i++)
        {
        	
        	//we make and sort a temporary array by slope order with respect to this point as an "origin"
        	Point [] points2 = points.clone();
        	Arrays.sort(points2,points[i].slopeOrder());
        	
        	//loop through each of these slop-ordered points and look for blocks of three or more points
        	int blockstart = 0; //keep an index for the beginning of a block
        	
        	for (int j = 0; j < N+1; j++) //we got to N+1 because we want to include the edgecase where the last points were collinear
        		//we just need to be careful that we treat the case j = N with care since it isn't a valid index, but it is essentially the first index after the array ends
        	{
        		//we check if we have reached past the final point (j = N) or if the next point is not in the block
        		//both of these cases mean we should consider if the block we have spanned up to but not including this last point is of size 3
        		if (j == N || points[i].slopeOrder().compare(points2[j], points2[blockstart]) != 0) 
        			//if j == N, we should short-circuit before calling points2[j] which would otherwise throw indexoutofbounds
        		{
        			//if this is the case, j is the first point not in the block (or when j = N, we can think of letting N be a placeholder for the point after the array ends)
        			//we need to check if blockstart to j - 1 contains at least 3 points:
        			if (j - blockstart >= 3)
        			{
        				//make an array that contains the 3 or more points in this line block (except the origin)
        				Point [] linblock = new Point[j-blockstart];
        				for (int k = blockstart; k < j; k++)
        					linblock[k-blockstart] = points2[k];
        				
        				//we sort the points in the block by their lexigraphic order (compareTo)
        				Arrays.sort(linblock);
        				
        				//check if the origin is smaller then any point in the block (to ensure we only print each block once)
        				//if not, we won't print it, to avoid printing the N-1 duplicate permutations of a block of size N
        				if (points[i].compareTo(linblock[0]) < 0) //since we sorted linblock, we know the smallest one is at index 0
        					outputLine(points[i],linblock);
        				
        			}
        			
        			//then this wasn't a block of sufficient size so move the blockstart to our current index j
        			blockstart = j;
        		}
        		
        	}
        	
        }
	}
	
	// Method to print and draw a segment from an origin point
    private static void outputLine(Point origin, Point[] points)
    {
        String result = origin.toString();
        
        for (Point p : points)
        {
            result += " -> " + p.toString();
        }
               
        StdOut.println(result);
        origin.drawTo(points[points.length-1]); //draw the line
    }
}
