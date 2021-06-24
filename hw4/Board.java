import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;


public class Board
{
    private final int[][] board;
    private final int dim;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col).
    public Board(int[][] tiles)
    {
        dim = tiles.length;
        board = new int[dim][dim];
        for (int i = 0; i < dim; i++)
        {   board[i] = tiles[i].clone();    }
    }

    // string representation of this board
    public String toString()
    {
        StringBuffer aString = new StringBuffer();
        aString.append(dim + "\n");
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {   aString.append(" " + board[i][j]);  }
            aString.append("\n");
        }
        return aString.toString();
    }

    // board dimension n
    public int dimension()
    {   return dim;     }

    // number of tiles out of place
    public int hamming()
    {
        int hammingNum = 0;
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                if (board[i][j] != i*dim+j+1 && i*dim+j+1 < dim*dim) hammingNum += 1; 
            }
        }
        return hammingNum;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int manhattanNum = 0;
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                int steps = 0;
                if (board[i][j] == i*dim+j+1)
                    continue;
                else
                {
                    int x = (board[i][j]-1)/dim;
                    int y = (board[i][j]-1)%dim;
                    if (x < 0 || y < 0) continue;
                    int xDispersion = Math.abs(x-i);
                    int yDispersion = Math.abs(y-j);
                    steps += xDispersion + yDispersion;
                }
                manhattanNum += steps;
            }
        }
        return manhattanNum;
    }

    // is this board the goal board?
    public boolean isGoal()
    {   return this.hamming() == 0;    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this)  return true;
        else if (y == null) return false;
        else if (y.getClass() != this.getClass())   return false;

        Board that = (Board) y;
        if (this.dim != that.dim)   return false;
        
        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                if (that.board[i][j] != this.board[i][j])   return false;
            }
        }
        return true;
    }

    // all neighbouring boards
    public Iterable<Board> neighbors()
    {   
        Queue<Board> boardQueue = new Queue<Board>();
        int[][] tempBlock = new int[dim][dim];
        for (int i = 0; i < dim; i++)   tempBlock[i] = board[i];

        for (int i = 0; i < dim; i++)
        {
            for (int j = 0; j < dim; j++)
            {
                if (board[i][j] == 0)
                {
                    for (int dx = -1; dx <= 1; dx++)
                    {
                        for (int dy = -1; dy <= 1; dy++)
                        {
                            if ((dx != 0 || dy != 0) && (dx == 0 || dy ==0) && i+dx >= 0 && i+dx < dim && j+dy >= 0 && j+dy < dim)
                            {
                                tempBlock[i][j] = tempBlock[i+dx][j+dy];
                                tempBlock[i+dx][j+dy] = 0;
                                boardQueue.enqueue(new Board(tempBlock));
                                tempBlock[i+dx][j+dy] = tempBlock[i][j];
                                tempBlock[i][j] = 0;
                            }
                        }
                    }
                }
            }
        }
        return boardQueue;
    }

    // a board that is obtained by exchanging any pair of tiles.
    // note here a twin of a board just means to change any pair of blocks on the same row except blanck one.
    public Board twin()
    {   
        int[][] tempBlock = new int[dim][dim];
        for (int i = 0; i < dim; i++) tempBlock[i] = board[i].clone();
        int tmp = 0;
        int rowNum = 0;
        if (board[0][0] == 0 || board[0][1] == 0) rowNum = 1;
        tmp = tempBlock[rowNum][0];
        tempBlock[rowNum][0] = tempBlock[rowNum][1];
        tempBlock[rowNum][1] = tmp;
        Board tempBoard = new Board(tempBlock);
        return tempBoard;
    }

    public static void main(String[] args) {
        int[][] tiles = {{1,0,3},{4,2,5},{7,8,6}};
        Board board = new Board(tiles);
        System.out.println(board.toString());
        //System.out.println(board.hamming());
        //System.out.println(board.manhattan());
        //System.out.println(board.isGoal());
        System.out.println(board.twin().toString());
        Iterable<Board> neighbours = board.neighbors();
        for (Board b : neighbours)
            System.out.println(b.toString());
    }
}
