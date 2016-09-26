import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Fast Class for finding 4 collinear points using a sort Assignment 3
 * 
 * @author Hendrik Kits van Heyningen
 */
public class FastCollinearPoints {

	//Keep a list of all collinear segments we find
	private List<LineSegment> collinearSegments;
	
	public FastCollinearPoints(Point[] points) {


		//Check for nulls
		if (points == null)
			throw new java.lang.NullPointerException("The provided points array was null");
		
		for (Point point : points) {
			if (point == null)
				throw new java.lang.NullPointerException("A point in the array was null");
		}
		
		int numPoints = points.length;
		this.collinearSegments = new ArrayList<LineSegment>();
		
		//Sort the points (clone to prevent side effects) and check if any are identical (i.e. compareTo == 0)
		Point[] pointsDup = points.clone();
		Arrays.sort(pointsDup);
		for (int i = 1; i < numPoints; i++) {
			if (pointsDup[i].compareTo(pointsDup[i-1]) == 0)
				throw new java.lang.IllegalArgumentException("There was a repeated point in the array");
		}
		
		//loop through each point in our duplicate sorted array and check the slopes to see if four or more have the same slope
		//Note that we use pointsDup instead of points because starting with an pre-sorted array prevents us from having to sort linblock later
		//This was a hard-to-find optimization because it only could be exposed by the hidden tests 2a-g that put a lot of points in a line, 
		//But this was making the linblock sort that happened inside the double nested loop use way too many compares
		
		for (int i = 0; i < numPoints; i++) {

			//we make and sort a temporary array by slope order with respect to this point as an "origin"
			Point[] points2 = pointsDup.clone();
			Arrays.sort(points2, pointsDup[i].slopeOrder());

			//Loop through each of these slope-ordered points and look for blocks of three or more points
			
			int blockstart = 0; //keep an index for the beginning of a block
			
			//Start at j = 1 because want to skip points2[0] = pointsDup[i] (since the same point has a slope order of -Inf)
			//<= because we want to include the edgecase where the last points were collinear
			for (int j = 1; j <= numPoints; j++) {  
			//we just need to be careful that we treat the case j = numPoints with care since it isn't a valid index, but it is essentially the first index after the array ends


				//we check if we have reached past the final point (j = numPoints) or if the next point is not in the block
				//both of these cases mean we should consider if the block we have spanned up to but not including this last point is of size 3
				//if j == numPoints, we should short-circuit before calling points2[j] which would otherwise throw indexoutofbounds
				if (j == numPoints || pointsDup[i].slopeOrder().compare(points2[j], points2[blockstart]) != 0) {
					//if this is the case, j is the first point not in the block (or when j = numPoints, we can think of letting j be a placeholder for the point after the array ends)
					//we need to check if blockstart to j - 1 contains at least 3 points:
					if (j - blockstart >= 3) {
						//make an array that contains the 3 or more points in this line block (except the origin)
						Point[] linblock = new Point[j - blockstart];
						for (int k = blockstart; k < j; k++)
							linblock[k - blockstart] = points2[k];

						//check if the origin is smaller then any point in the block (to ensure we only consider each segment once)
						//if not, we won't create a segment from it, to avoid adding the numPoints-1 duplicate permutations of a block of size numPoints
						if (pointsDup[i].compareTo(linblock[0]) < 0) //since we sorted linblock, we know the smallest one is at index 0
							this.collinearSegments.add(new LineSegment(pointsDup[i], linblock[j - blockstart - 1])); //and the largest one is at index j - blockstart - 1
					}

					//then this wasn't a block of sufficient size so move the blockstart to our current index j
					blockstart = j;
				}

			}

		}
	}
	
	/**
	 * Gets the number of segments with 4 or more collinear points
	 * 
	 * @return the number of collinear point segments
	 */
	public int numberOfSegments() {
		return collinearSegments.size();
	}

	/**
	 * Returns all segments that contain 4 or more collinear points
	 * 
	 * @return the segments themselves
	 */
	public LineSegment[] segments() {
		return collinearSegments.toArray(new LineSegment[0]);
	}

	public static void main(String[] args) {
		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];

		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}
		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);

		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();
		// print and draw the line segments
		FastCollinearPoints collinear = new FastCollinearPoints(points);

		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
