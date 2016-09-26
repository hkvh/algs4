/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) 
    {
        int dy = that.y - this.y;
        int dx = that.x - this.x;
        
        //Check if dx = 0, which means the slope is infinity, unless dy = 0 as well
        if ((dx == 0) && (dy == 0))
        	return Double.NEGATIVE_INFINITY; //if we have the same point we'll return negative infinity
 
        else if (dx == 0)
        	return Double.POSITIVE_INFINITY; //undefined slopes can be treated as infinite
        
        else if (dy == 0)
        	return new Double(+0.0); //return positive 0 for horizontal line
        
        else // if dy is defined we can compute slope the normal way
        	return (double)dy/dx; //cast as double to avoid int division
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    @Override
	public int compareTo(Point that) 
    {
    	if (that == null) //make sure we don't have null
    		throw new java.lang.NullPointerException();
    	
    	//if both y coordinates are the same, break tie with x coords
        if (y == that.y)
        	return x - that.x;
        else //otherwise just return the difference in y coordinates
        	return y - that.y;
        	
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
        	 @Override
     		public int compare(Point p, Point q) {
                 if (Point.this.slopeTo(p) < Point.this.slopeTo(q))
                     return -1;   
                 else if (Point.this.slopeTo(p) == Point.this.slopeTo(q))
                     return 0;   
                 else 
                     return 1;
             }
        };
    }
    
    /**
     * Returns a string representation of this point.
     * This method is provided for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */    
    @Override
	public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }
    
    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
    	// Test point creation
        Point x = new Point(5, 5);
        Point y = new Point(1, 1);
        Point z = new Point(2, 3);
        Point a = new Point(5, 5);
        Point b = new Point(1, 10);
        Point c = new Point(6, 3);
        
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        StdDraw.setPenRadius(0.001);  // make the points a bit larger
        
        // Test drawing methods
        x.draw();
        y.draw();
        z.draw();
        a.draw();
        b.draw();
        c.draw();
        
        // Test drawTo
        x.drawTo(y);
        z.drawTo(y);
        b.drawTo(c);
        
        // Test slopeTo method
        StdOut.println("Testing slopeTo method:");
        StdOut.println("Slope from (5, 5) to (1, 1) = " + x.slopeTo(y));
        StdOut.println("Slope from (1, 1) to (2, 3) = " + y.slopeTo(z));
        StdOut.println("Slope from (5, 5) to (5, 5) = " + x.slopeTo(a));
        StdOut.println("Slope from (1, 1) to (1, 10) = " + y.slopeTo(b));
        StdOut.println("Slope from (2, 3) to (6, 3) = " + z.slopeTo(c));

        // Test compareTo method
        StdOut.println("\nTesting compareTo method:");
        StdOut.println("(5, 5) compared to (1, 1) = " + x.compareTo(y));
        StdOut.println("(1, 1) compared to (2, 3) = " + y.compareTo(z));
        StdOut.println("(5, 5) compared to (5, 5) = " + x.compareTo(a));
        StdOut.println("(1, 1) compared to (1, 10) = " + y.compareTo(b));
        StdOut.println("(2, 3) compared to (6, 3) = " + z.compareTo(c));
        
        // Test slope comparator
        StdOut.println("\nTesting slope order comparator using (1, 1) as base point:");
        StdOut.println("(5, 5) compared to (1, 1) = " + y.slopeOrder().compare(x, y));
        StdOut.println("(5, 5) compared to (2, 3) = " + y.slopeOrder().compare(x, z));
        StdOut.println("(5, 5) compared to (5, 5) = " + y.slopeOrder().compare(x, a));
        StdOut.println("(5, 5) compared to (1, 10) = " + y.slopeOrder().compare(x, b));
        StdOut.println("(2, 3) compared to (6, 3) = " + y.slopeOrder().compare(z, c));        
    }
}