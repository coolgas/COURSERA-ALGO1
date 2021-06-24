
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final int dimension;
    private final WeightedQuickUnionUF wquf;
    private int[] siteStats1D; // the 1d version of siteStats 1 for blocked 0 for open

    // the constructor
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Invalid input of n");

        dimension = n;
        siteStats1D = new int[n*n];
        wquf = new WeightedQuickUnionUF((n*n)+2);

        if (dimension > 1) {
            // this loop is to add the virtual site
            for (int i = 0; i < dimension; i++) {
                wquf.union(i, dimension*dimension);
                wquf.union(dimension*dimension-1-i, dimension*dimension+1);
            }
        }           
        
        for (int i = 0; i < (n*n); i++) {
            siteStats1D[i] = 1;
        }
    }

    private boolean isOutOfIndex(int row, int col) {
        return row < 1 || row > dimension || col < 1 || col > dimension;
    }
    
    // this function transforms the index of 2d array to that of the 1d array
    private int toIndex(int row, int col) {
        return (row-1)*dimension+col-1;
    }

    // this function returns the 1d indice of neighbourhoods of the assigned point
    // the -1 append at the end has no real meaning
    private int[] neigbourIndex(int row, int col) {
        int[] coordinates = new int[4]; 
        if (dimension > 1) {
            if (row-1 > 0 && row+1 <= dimension && col-1 > 0 && col+1 <= dimension) {
                coordinates[0] = toIndex(row+1, col);
                coordinates[1] = toIndex(row-1, col);
                coordinates[2] = toIndex(row, col+1);
                coordinates[3] = toIndex(row, col-1);
            } else if (row-1 <= 0 && col-1 <= 0) {
                coordinates[0] = toIndex(row+1, col);
                coordinates[1] = toIndex(row, col+1);
                coordinates[2] = -1;
                coordinates[3] = -1;
            } else if (row-1 <= 0 && col+1 > dimension) {
                coordinates[0] = toIndex(row, col-1);
                coordinates[1] = toIndex(row+1, col);
                coordinates[2] = -1;
                coordinates[3] = -1;
            } else if (row+1 > dimension && col-1 <= 0) {
                coordinates[0] = toIndex(row, col+1);
                coordinates[1] = toIndex(row-1, col);
                coordinates[2] = -1;
                coordinates[3] = -1;
            } else if (row+1 > dimension && col+1 > dimension) {
                coordinates[0] = toIndex(row, col-1);
                coordinates[1] = toIndex(row-1, col);
                coordinates[2] = -1;
                coordinates[3] = -1;
            } else if (row-1 <= 0 && col-1 > 0 && col+1 <= dimension) {
                coordinates[0] = toIndex(row, col-1);
                coordinates[1] = toIndex(row, col+1);
                coordinates[2] = toIndex(row+1, col);
                coordinates[3] = -1;
            } else if (col-1 <= 0 && row-1 > 0 && row+1 <= dimension) {
                coordinates[0] = toIndex(row-1, col);
                coordinates[1] = toIndex(row+1, col);
                coordinates[2] = toIndex(row, col+1);
                coordinates[3] = -1;
            } else if (row+1 > dimension && col-1 > 0 && col+1 <= dimension) {
                coordinates[0] = toIndex(row, col-1);
                coordinates[1] = toIndex(row, col+1);
                coordinates[2] = toIndex(row-1, col);
                coordinates[3] = -1;
            } else if (col+1 > dimension && row-1 > 0 && row+1 <= dimension) {
                coordinates[0] = toIndex(row-1, col);
                coordinates[1] = toIndex(row+1, col);
                coordinates[2] = toIndex(row, col-1);
                coordinates[3] = -1;
            }
        } else {
            coordinates[0] = 0;
            coordinates[1] = -1;
            coordinates[2] = -1;
            coordinates[3] = -1;
        }
        return coordinates;
    }

    // open the assigned site
    public void open(int row, int col) {
        if (isOutOfIndex(row, col)) throw new IllegalArgumentException("Invalid input of row or col");
            
        int[] indices = neigbourIndex(row, col);
        int len = indices.length;

        if (siteStats1D[(row-1)*dimension+col-1] == 0)
            return;
        
        siteStats1D[(row-1)*dimension+col-1] = 0;
        
        for (int i = 0; i < len; i++) {
            if (indices[i] >= 0 && siteStats1D[indices[i]] == 0) {
                wquf.union(indices[i], (row-1)*dimension+col-1);
            }
        }   
    }

    // is the assigned site open?
    public boolean isOpen(int row, int col) {
        if (isOutOfIndex(row, col)) throw new IllegalArgumentException("Invalid input of row or col");     
        return siteStats1D[(row-1)*dimension+col-1] == 0;
    }

    // is the assigned site full?
    public boolean isFull(int row, int col) {
        int index = (row-1)*dimension+col-1;
        
        if (index < 0 || index > (dimension*dimension-1)) throw new IllegalArgumentException("Invalid input of row or col");

        if (!isOpen(row, col)) return false;

        return wquf.find(index) == wquf.find(dimension*dimension);  
    }

    public int numberOfOpenSites() {
        int num = 0;
        for (int i = 0; i < dimension*dimension; i++) {
            if (siteStats1D[i] == 0)
                num += 1;
            else
                continue;
        }
        return num;
    }

    public boolean percolates() {
        if (dimension > 1) {
            return wquf.find(dimension*dimension) == wquf.find(dimension*dimension + 1);
        } else if (siteStats1D[0] != 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        // indended as empty
    }
}