import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

public class PointSET
{
    private SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET()
    {   pointSet = new SET<Point2D>();  }
    
    // is the set empty?
    public boolean isEmpty()
    {   return pointSet.isEmpty();  }

    // number of points in the set
    public int size()
    {   return pointSet.size(); }

    // add the point to the set (if it is not already in the set)
    public void insert (Point2D p)
    {
        if (p == null)  throw new IllegalArgumentException();
        if (!pointSet.contains(p))  pointSet.add(p);
    }

    // does the set contain point p
    public boolean contains(Point2D p)
    {
        if (p == null)  throw new IllegalArgumentException();
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw()
    {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D p : pointSet)  p.draw();
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)  throw new IllegalArgumentException();
        
        Queue<Point2D> pointQueue = new Queue<Point2D>();
        for (Point2D p : pointSet)
        {
            if (rect.contains(p))   pointQueue.enqueue(p);
        }
        return pointQueue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null) throw new IllegalArgumentException();
        if (pointSet.isEmpty()) return null;
        
        double minDistance = 0.0;
        Point2D minPoint = null;
        for (Point2D q : pointSet)
        {
            double distance = q.distanceTo(p);
            if (minPoint == null || distance < minDistance)
            {
                minDistance = distance;
                minPoint = q;
            }
        }
        return minPoint;
    }

    public static void main(String[] args)
    {
        // intended left to be empty
    }
    
}
