import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;


public class Solver
{
    private boolean isSolvable;
    private SearchNode finalNode; 
    private Board initial;

    // find a solution to the initial board (using A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null)    throw new IllegalArgumentException();
        this.initial = initial;
    
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        SearchNode initialNode = new SearchNode(initial, 0, null);
        SearchNode twinInitialNode = new SearchNode(initial.twin(), 0, null);
        pq.insert(initialNode);
        pq.insert(twinInitialNode);

        while (!pq.isEmpty())
        {
            SearchNode curNode = pq.delMin();
            if (curNode.board.isGoal())
            {
                SearchNode tmpNode = curNode;
                // going back to see if this board is solvable
                while (tmpNode.ancestor != null)    tmpNode = tmpNode.ancestor;
                isSolvable = tmpNode.board.equals(initial);
                finalNode = curNode;
                break;
            }         
            // add neighbours to the PQ
            Iterable<Board> neigbours = curNode.board.neighbors();
            SearchNode preNode = curNode.ancestor;
            Board preBoard = (preNode == null) ? null : preNode.board;
            for (Board b : neigbours)
            {   
                if (b.equals(preBoard)) continue;
                SearchNode neighNode = new SearchNode(b, curNode.moves+1, curNode);
                pq.insert(neighNode);
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable()
    {   return isSolvable;   }

    // min number moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (!isSolvable) return -1;
        else return finalNode.moves;
    }

    // sequence of boards in a shortest solution; null is unsolvable
    public Iterable<Board> solution()
    {
        if (!isSolvable) return null;

        Stack<Board> solutionStack = new Stack<Board>();
        SearchNode tmpNode = finalNode;
        while (tmpNode.ancestor != null)
        {
            solutionStack.push(tmpNode.board);
            tmpNode = tmpNode.ancestor;
        }
        solutionStack.push(initial);
        return solutionStack;
    }
    
    private class SearchNode implements Comparable<SearchNode>
    {
        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode ancestor; // the previous search node

        public SearchNode(Board board, int moves, SearchNode ancestor)
        {
            this.board = board;
            this.moves = moves;
            this.ancestor = ancestor;
            this.priority = board.manhattan() + this.moves;
        }

        public int compareTo(SearchNode that)
        {
            if (this.priority > that.priority)  return 1;
            else if (this.priority == that.priority)    return 0;
            else return -1;
        }
    }

    public static void main(String[] args) 
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++) tiles[i][j] = in.readInt();
        }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable) StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimun number of moves = " + solver.moves());
            for (Board b : solver.solution())
                StdOut.println(b);
        }
    }
}
