import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private int size;
    private int[][] grid;
    private WeightedQuickUnionUF UF;
    private int flatIndex;
    private int openSites = 0;

    public Percolation(int n) {
        //n-by-n blocked grid
        if (n < 1) {
            throw new IllegalArgumentException();
        } else {
            size = n;
        }
        
        //blocked = 0
        //open = 1
        grid = new int[n][n];
        
        UF = new WeightedQuickUnionUF(n*n+1);
    }

    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }
        //if site is not open already, open it and connect it to the already open sites
        if (grid[row-1][col-1] == 0) {

            grid[row-1][col-1] = 1;
            openSites++;
            flatIndex = (row-1)*size + col;

            if (row == 1) {
                UF.union(0, col);
                if (flatIndex + size <= size*size && isOpen(row+1,col))
                    UF.union(flatIndex+size, flatIndex); //down
            } else {
               if (flatIndex - size > 0 && isOpen(row-1,col)) //up 
                   UF.union(flatIndex-size,flatIndex);
               if (flatIndex + size <= size*size && isOpen(row+1,col))
                  UF.union(flatIndex+size, flatIndex); //down
               if ((flatIndex - 1) % size != 0 && isOpen(row, col - 1)) //left
                   UF.union(flatIndex - 1, flatIndex);
               if ((flatIndex + 1) % size != 1 && isOpen(row, col + 1)) //right
                   UF.union(flatIndex + 1, flatIndex);
            }
        } else {
            return;
        }

    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }

        if (grid[row-1][col-1] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull(int row, int col) {
        //Can the given site be connected to a site in the top row?
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }  

        flatIndex = (row-1)*size + col;

        if (UF.find(0) == UF.find(flatIndex)) { 
            return true;
        } else {
            return false;
        }
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        //for site in bottom row check isFull()
        int i = 1;
        while(i <= size && !isFull(size,i)){
            i++;
            continue;
        }
        if (i == size+1) {
           return isFull(size,i-1); 
        } else {
            return isFull(size,i);
        }
    }
}
