import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
    private Node root;
    private final RectHV CONTAINER = new RectHV(0, 0, 1, 1);

    private class Node
    {
        private Point2D point; // sorted by 2d points
        private Node left, right; // left and right subtree
        private int size; // number of nodes in subtree
        private boolean vertical; // later will be used

        public Node(Point2D point, int size, boolean vertical)
        {
            this.point = point;
            this.size = size;
            this.vertical = vertical;
        }
    }

    // initializes an empty tree
    public KdTree()
    {}

    // is the tree empty?
    public boolean isEmpty()
    {   return size() == 0; }

    // number of points in the tree
    public int size()
    {   return size(root);  }

    private int size(Node x)
    {
        if (x == null)  return 0;
        else return x.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null)  throw new IllegalArgumentException();
        root = insert(root, p, true);
    }

    private Node insert(Node x, Point2D p, boolean vertical)
    {
        if (x == null)  return new Node(p, 1, vertical);
        
        // if already in, just return the input node
        if (x.point.x() == p.x() && x.point.y() == p.y())   return x;

        // if not, we proceed to insert into the tree
        if (x.vertical && p.x() < x.point.x() || !x.vertical && p.y() < x.point.y())
            x.left = insert(x.left, p, !x.vertical);
        else
            x.right = insert(x.right, p, !x.vertical);
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    // does the tree contain the point?
    public boolean contains(Point2D p)
    {
        if (p == null)  throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p)
    {
        if (x == null) return false;
        if (x.point.x() == p.x() && x.point.y() == p.y())   return true;
        if (x.vertical && p.x() < x.point.x() || !x.vertical && p.y() < x.point.y())
            return contains(x.left, p);
        else
            return contains(x.right, p);
    }

    // draw all points to standard draw
    public void draw()
    {
        StdDraw.setScale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        CONTAINER.draw();
        draw(root, CONTAINER);
    }

    private void draw(Node x, RectHV rect)
    {
        if (x == null)  return;
        
        // draw the point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        new Point2D(x.point.x(), x.point.y()).draw();

        // get the min and max points of division line
        Point2D min, max;
        if (x.vertical)
        {
            StdDraw.setPenColor(StdDraw.RED);
            min = new Point2D(x.point.x(), rect.ymin());
            max = new Point2D(x.point.x(), rect.ymax());
        }
        else
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            min = new Point2D(rect.xmin(), x.point.y());
            max = new Point2D(rect.xmax(), x.point.y());
        }

        // draw the line
        StdDraw.setPenRadius();
        min.drawTo(max);

        // recursively draw the children
        draw(x.left, leftRect(rect, x));
        draw(x.right, rightRect(rect, x));
    }

    private RectHV leftRect(RectHV rect, Node x)
    {
        if (x.vertical) return new RectHV(rect.xmin(), rect.ymin(), x.point.x(), rect.ymax());
        else    return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.point.y());
    }

    private RectHV rightRect(RectHV rect, Node x)
    {
        if (x.vertical) return new RectHV(x.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        else    return new RectHV(rect.xmin(), x.point.y(), rect.xmax(), rect.ymax());
    }
    
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)   throw new IllegalArgumentException();
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, CONTAINER, rect, queue);
        return queue;
    }

    private void range(Node x, RectHV hrect, RectHV rect, Queue<Point2D> queue)
    {
        if (x == null)  return;

        if (rect.intersects(hrect))
        {
            Point2D p = new Point2D(x.point.x(), x.point.y());
            if (rect.contains(p))   queue.enqueue(p);
            range(x.left, leftRect(hrect, x), rect, queue);
            range(x.right, rightRect(hrect, x), rect, queue);
        }
    }

    // a nearest neighbour in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null)  throw new IllegalArgumentException();
        if (this.isEmpty()) return null;
        return nearest(root, CONTAINER, p, null);
    }

    private Point2D nearest(Node node, RectHV rect, Point2D p, Point2D candidate)
    {
        if (node == null)   return candidate;

        Point2D nearest = candidate;
        double dpn = 0.0; // the distance between the query point and the nearest point
        double drq = 0.0; // the distance between the rectangle and the query point
        RectHV left = null;
        RectHV right = null;

        if (nearest != null)
        {
            dpn = p.distanceTo(nearest);
            drq = rect.distanceTo(p);
        }

        if (nearest == null || dpn > drq)
        {
            Point2D point = new Point2D(node.point.x(), node.point.y());
            if (nearest == null || dpn > p.distanceTo(point))   nearest = point;

            if (node.vertical)
            {
                left = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                right = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());

                if (p.x() < node.point.x())
                {
                    nearest = nearest(node.left, left, p, nearest);
                    nearest = nearest(node.right, right, p, nearest);
                }
                else
                {
                    nearest = nearest(node.right, right, p, nearest);
                    nearest = nearest(node.left, left, p, nearest);
                }
            }
            else
            {
                left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
                right = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());

                if (p.y() < node.point.y())
                {
                    nearest = nearest(node.left, left, p, nearest);
                    nearest = nearest(node.right, right, p, nearest);
                }
                else
                {
                    nearest = nearest(node.right, right, p, nearest);
                    nearest = nearest(node.left, left, p, nearest);
                }
            }
        }
        return nearest;
    }
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        Point2D pt1 = new Point2D(0.1, 0.5);
        Point2D pt2 = new Point2D(0.5, 0.2);
        Point2D pt3 = new Point2D(0.2, 0.3);
        Point2D pt4 = new Point2D(0.6, 0.7);
        RectHV rect = new RectHV(0.0, 0.0, 0.4, 0.6);
        kdtree.insert(pt1);
        kdtree.insert(pt2);
        kdtree.insert(pt3);
        kdtree.insert(pt4);
        rect.draw();
        kdtree.draw();
        System.out.println(kdtree.size());
        for (Point2D p : kdtree.range(rect))
            System.out.println(p.toString());
        

        //System.out.println(kdtree.contains(pt1));
    }
}
