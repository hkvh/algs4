import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Brute Class for finding collinear points Assignment 3
 * 
 * @author Hendrik Kits van Heyningen
 */
public class BruteCollinearPoints {
	
	//Keep a list of all collinear segments we find
	private List<LineSegment> collinearSegments;

	public BruteCollinearPoints(Point[] points) {
		
		//Check for nulls
		if (points == null)
			throw new java.lang.NullPointerException("The provided points array was null");
		
		for (Point point : points) {
			if (point == null)
				throw new java.lang.NullPointerException("A point in the array was null");
		}
		
		int numPoints = points.length;

		//Sort the points (clone to prevent side effects) and check if any are identical (i.e. compareTo == 0)
		Point[] pointsDup = points.clone();
		Arrays.sort(pointsDup);
		for (int i = 1; i < numPoints; i++) {
			if (pointsDup[i].compareTo(pointsDup[i-1]) == 0)
				throw new java.lang.IllegalArgumentException("There was a repeated point in the array");
		}

		this.collinearSegments = new ArrayList<LineSegment>();

		//loop through each point and check the slopes trying to find subsets of 4 points that are collinear
		for (int i = 0; i < numPoints; i++) {

			for (int j = i + 1; j < numPoints; j++) {

				for (int k = j + 1; k < numPoints; k++) {

					//if the first three points lie on a line, check for a last point, otherwise, don't bother
					if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])) {

						//check for a last (4th) point being on the line
						for (int l = k + 1; l < numPoints; l++) {

							//we know the first 3 points are in a line so just need to make sure the fourth is
							if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])) {

								//Now that we found 4 collinear points make them into an array to sort
								Point[] linePoints = { points[i], points[j], points[k], points[l] };
								Arrays.sort(linePoints); //sort it based on the compareTo method
								
								//The first to the fourth element is the segment we want, the second and third points will be included
								this.collinearSegments.add(new LineSegment(linePoints[0], linePoints[3]));
							}
						}

					}
				}
			}
		}
	}

	/**
	 * Gets the number of segments with 4 collinear points
	 * 
	 * @return the number of segments
	 */
	public int numberOfSegments() {
		return collinearSegments.size();
	}

	/**
	 * Returns all segments that contain 4 collinear points
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
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);

		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}

}
