import java.util.Arrays;

public class BruteCollinearPoints {
    private int num = 0;   // the number colinear segments
    private LineSegment[] segments;
 
    public BruteCollinearPoints(Point[] points)
    {
        if (points == null) throw new IllegalArgumentException();
        checkFirst(points);

        int len = points.length;

        LineSegment[] testSegments = new LineSegment[len*len];

        // Instead of modifying the original points array, 
        // we sorting the copy array of point.
        Point[] copyPoints = points.clone();
        Arrays.sort(copyPoints);
        for (int i = 0; i < len-3; i++)
        {
            for (int j = i+1; j < len-2; j++)
            {
                for (int k = j+1; k < len-1; k++)
                {
                    for (int l = k+1; l < len; l++)
                    {
                        Point a = copyPoints[i];
                        Point b = copyPoints[j];
                        Point c = copyPoints[k];
                        Point d = copyPoints[l];
                        if (colinearity(a, b, c, d))
                            testSegments[num++] = new LineSegment(a, d);
                    }
                }
            }
        }
        segments = new LineSegment[num];
        for (int i = 0; i < num; i++ )
        {   segments[i] = testSegments[i];   };
    }

    // return the number of line segments
    public int numberOfSegments()
    {
        return num;
    }

    // return the list of colinear segments
    public LineSegment[] segments()
    {
        return Arrays.copyOf(segments, num);
    }

    // returns ture when the corner cases exist
    private void checkFirst(Point[] points)
    {
        int len = points.length;
        for (int i = 0; i < len; i++)
        {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i+1; j < len; j++)
            {
                if (points[j] == null)
                    throw new IllegalArgumentException();
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }
    }

    // check if the four input points are colinear
    private boolean colinearity(Point p, Point q, Point r, Point s)
    {
        double pqSlope = p.slopeTo(q);
        double qrSlope = q.slopeTo(r);
        double rsSlope = r.slopeTo(s);
        if (pqSlope == Double.NEGATIVE_INFINITY || qrSlope == Double.NEGATIVE_INFINITY || rsSlope == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
        return pqSlope == qrSlope && qrSlope == rsSlope;
    }

    public static void main(String[] args) {
        // Intended to be empty
    } 
}
