import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints
{
    private int numSegments = 0;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points)
    {
        if (checkFirst(points)) throw new IllegalArgumentException();

        int len = points.length;
        double testSlope;

        Point origin;
        Point[] pointsClone = points.clone();
        Point[] fixedPoints;

        LineSegment[] testSegments = new LineSegment[len*len];

        Arrays.sort(pointsClone);
        fixedPoints = pointsClone.clone();

        // pick out every single point as origin
        for (int i = 0; i < len; i++)
        {
            origin = fixedPoints[i];

            // first sort the array into natural ordering,
            // then sort the array so that points with the same slope with respect to origin is grouped together.
            // notice here the second sort is stable.
            Arrays.sort(pointsClone);
            Arrays.sort(pointsClone, origin.slopeOrder());

            int j = len-1;
            while (j > 0)
            {
                int distance = 0;
                testSlope = origin.slopeTo(pointsClone[j]);
                for (int k = j-1; k > 0; k--)
                {
                    if (origin.slopeTo(pointsClone[k]) == testSlope)
                        distance += 1;
                    else
                        break; 
                }
                distance += 1;
                // once distance is larger or equal to 3,
                // which means we are reaching a potential line segement.
                // but we still need to make sure that these points are aligned in ascending order.
                if (distance >= 3)
                {
                    if (origin.compareTo(pointsClone[j]) < 0 && origin.compareTo(pointsClone[j-distance+1]) < 0)
                    {
                        testSegments[numSegments++] = new LineSegment(origin, pointsClone[j]);
                    }
                }
                j -= distance;
            }
            
        }
        segments = new LineSegment[numSegments];
        for (int i = 0; i < numSegments; i++)
        {   segments[i] = testSegments[i];   }
    }
    
    // check the number of line segements
    public int numberOfSegments()
    {   return numSegments; }

    // return the line segments
    public LineSegment[] segments()
    {   return Arrays.copyOf(segments, numSegments);    }

    // check if the input points follow regulations
    private boolean checkFirst(Point[] points)
    {
        if (points == null) return true;
        int len = points.length;
        for (int i = 0; i < len; i++)
        {
            for (int j = i+1; j < len; j++)
            {
                if (points[i] == null || points[j] == null || points[i].compareTo(points[j]) == 0)
                    return true;
            }
        }
        return false;
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
