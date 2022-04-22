import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {
	private Point[] points;
	private int size;
	private boolean collinear (Point p, Point q, Point r) {
		
		double m1 = p.slopeTo(q); // p->q
		double m2 = p.slopeTo(r); // p->r

		if ( m1 == m2 ) 	 return true;
		else 				 return false;

	}
	public BruteCollinearPoints(Point[] points) {

		if (points == null)
			throw new IllegalArgumentException();
		this.points = points;
	}

	public int numberOfSegments() { return size; }

	public LineSegment[] segments() {

		int N = 0;

		
		Integer[] indicesArray = new Integer[0];

		List<Integer> indicesList = new ArrayList<Integer>(Arrays.asList(indicesArray));	

		for (int i = 0; i < points.length; i++) {
			for (int j = i+1; j < points.length ; j++) {
				for (int k = j+1; k < points.length; k++) {
					if (collinear(points[i], points[j], points[k])) {
						for (int l = k+1; l < points.length; l++) {
							if (collinear(points[i], points[j], points[l])) {
								indicesList.add(i);
								indicesList.add(j);
								indicesList.add(k);
								indicesList.add(l);	
							}
						}
					}		
				}
			}
		}

		indicesArray = indicesList.toArray(indicesArray);
		N = indicesArray.length;
		int k = N/4;
		LineSegment[] segments = new LineSegment[k];
		
		for (int c=0; c < N; c = c+4) {
			for (int i = c; i < c+4; i++) {
           		for (int j = i; j > c && points[indicesArray[j]].compareTo(points[indicesArray[j-1]]) < 0; j--)
               		swap(indicesArray,j,j-1);
        	}
		}
		
		for (int i = 0; i < k; i++)
			segments[i]  = new LineSegment(points[indicesArray[4*i]],points[indicesArray[4*i+3]]);

		size = N;
		return segments;
	}

    private void swap(Integer[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

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