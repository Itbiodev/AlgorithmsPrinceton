import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

	private final int x;
	private final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void draw() {
		StdDraw.point(x,y);
	}
	public void drawTo(Point that) {
		StdDraw.line(this.x, this.y, that.x, that.y);
	}
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public int compareTo(Point that) {
		if (this.y < that.y) 	  return -1;
		else if (this.y > that.y) return +1;
		else if (this.x < that.x) return -1;
		else if (this.x > that.x) return +1;
		else 					  return 0;
 	}
	public double slopeTo(Point that) {
		if (this.x != that.x && this.y == that.y) 			  return 0;
		else if (this.x == that.x && (this.y - that.y != 0) ) return Double.POSITIVE_INFINITY;
		else if (this.x == that.x && this.y == that.y) 		  return Double.NEGATIVE_INFINITY;
		else    
			return (double) (that.y-this.y)/ (double)(that.x-this.x);
	}
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new SlopeComparator(this);
    }

    private class SlopeComparator implements Comparator<Point> {

        private final Point point;

        SlopeComparator(Point point) {
            this.point = point;
        }

        @Override
        public int compare(Point p1, Point p2) {
            double slope1 = p1.slopeTo(point);
            double slope2 = p2.slopeTo(point);
            return slope1 == slope2 ? 0 : (slope1 > slope2 ? 1 : -1);
        }
    }
}