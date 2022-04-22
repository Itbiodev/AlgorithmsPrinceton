import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;


public class PointSET {

    private SET<Point2D> points;

    public PointSET() {
        this.points = new SET<Point2D>();
    }
    public boolean isEmpty() {
        return points.isEmpty();
    }
    public int size() {
        return points.size();
    }
    public void insert(Point2D p) {
        points.add(p);
    }
    public boolean contains(Point2D p) {
        return points.contains(p);
    }
    public void draw() {
        for (Point2D p: points)
            p.draw();
    }
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> inRange = new SET<Point2D>();
        for (Point2D p: points) {
            if (rect.contains(p))
                inRange.add(p);
        }
        return inRange;
    }
    public Point2D nearest(Point2D p) {
        Double min = Double.POSITIVE_INFINITY;
        Point2D r = p;
        double d = 0;
        for (Point2D q: points) {
            d = p.distanceTo(q);
            if (!q.equals(p) && d < min) {
                min = d;
                r = q;
            }
        }
        return r;
    }   
}