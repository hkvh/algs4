import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * PointSET Class Assignment 5
 * 
 * @author Hendrik Kits van Heyningen
 */
public class PointSET {

	private SET<Point2D> pointSET;

	/**
	 * Construct an empty set of points
	 */
	public PointSET() {
		pointSET = new SET<Point2D>();
	}

	/**
	 * Is the set empty?
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return pointSET.isEmpty();
	}

	/**
	 * Return the number of points in the set
	 * 
	 * @return the number of points in the set
	 */
	public int size() {
		return pointSET.size();
	}

	/**
	 * Add the point p to the set (if it is not already in the set)
	 * 
	 * @param p, the point to add
	 */
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.NullPointerException();
		pointSET.add(p);
	}

	/**
	 * Does the set contain the point p?
	 * 
	 * @param p, the point to check
	 * @return true if it contains p, false otherwise
	 */
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.NullPointerException();
		return pointSET.contains(p);
	}

	/**
	 * Draw all of the points to standard draw
	 */
	public void draw() {
		for (Point2D p : pointSET) {
			StdDraw.point(p.x(), p.y());
		}
		
	}

	/**
	 * Returns all points in the set that are inside the rectangle
	 * 
	 * @param rect, the range to check
	 * @return an iterator of points in the range
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.NullPointerException();
		SET<Point2D> pointsInRect = new SET<Point2D>();
		for (Point2D p : this.pointSET) {
			if (rect.contains(p))
				pointsInRect.add(p);
		}
		return pointsInRect;
	}

	/**
	 * Finds a nearest neighbor in the set to p; null if set is empty
	 * 
	 * @param p the point to check
	 * @return the neighbor; null if set is empty
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new java.lang.NullPointerException();
		Point2D closestPoint = null;
		for (Point2D q : this.pointSET) {
			if (closestPoint == null) {
				closestPoint = q;
				continue;
			}
			if (p.distanceTo(q) < p.distanceTo(closestPoint)) {
				closestPoint = p;
			}
		}
		return closestPoint;
	}
}
