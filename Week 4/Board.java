import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class Board {
    
    private final int[][] tiles;
    private final int N;

    public Board (int[][] tiles) {
        this.tiles = tiles;
        this.N = tiles.length;
    }

    public String toString() {
        String grid = "";

        grid += N + "\n";

        for (int i = 0; i < N; i++) {
            for (int j=0; j < N ; j++)
                grid+=tiles[i][j]+" ";
            grid+="\n";
        }

        return grid;
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int hamm = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] != 0 && tiles[i][j] != i*N+j+1) 
                    hamm++;
        return hamm;
    }

    public int manhattan() {
        int man = 0;
        int m = 0, n = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != i*N+j+1) { 
                    m =  (tiles[i][j] - 1) % N;
                    n =  (tiles[i][j] - 1 - m)/N;
                    man += Math.abs(m-j) + Math.abs(n-i);
                }
            }
        }
        return man;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension())
            return false;
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;
        return true;
    }

    public Iterable<Board> neighbors() { // check for correctness

        List<Board> neighbors = new LinkedList<>();
        int zeroRow = 0, zeroCol = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }

        int flatIndex = zeroRow*N+zeroCol; // flatIndex of zero

        if ( flatIndex + N < N*N ) { // down
            int[][] temp1 = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)                  
                    temp1[i][j] = tiles[i][j];
            swap(temp1, zeroRow, zeroCol, zeroRow + 1, zeroCol);
            Board downy = new Board(temp1);
            neighbors.add(downy);
        }
        if ( flatIndex - N >= 0 ) {// up
            int[][] temp2 = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)                  
                    temp2[i][j] = tiles[i][j];
            swap(temp2, zeroRow, zeroCol, zeroRow - 1, zeroCol);
            Board uppy = new Board(temp2);
            neighbors.add(uppy);
        }
        if ( flatIndex%N != 0 ) { // left
            int[][] temp3 = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)                  
                    temp3[i][j] = tiles[i][j];
            swap(temp3, zeroRow, zeroCol, zeroRow, zeroCol - 1);
            Board lefty = new Board(temp3);
            neighbors.add(lefty);
        }
        if ( flatIndex%N != 2 ) {// right
            int[][] temp4 = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)                  
                    temp4[i][j] = tiles[i][j];
            swap(temp4, zeroRow, zeroCol, zeroRow, zeroCol + 1);
            Board righty = new Board(temp4);
            neighbors.add(righty);
        }
        return neighbors; 
    }

    private void swap(int[][] a, int i, int j, int n, int m)
    { int t = a[i][j]; a[i][j] = a[n][m]; a[n][m] = t; }

    //public Board twin() {}

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
           for (int j = 0; j < n; j++)
              tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);
        StdOut.print(initial.toString());
        StdOut.println("Hamming distance: " + initial.hamming());
        StdOut.println("Manhattan distance: " + initial.manhattan());
        Board another = new Board(tiles);
        StdOut.println("Equal to itself? " + initial.equals(another));
        StdOut.println("Is equal to goal? " + initial.isGoal());
        for (Board b : initial.neighbors())
            StdOut.println(b.toString());
        //Solver solver = new Solver(initial);

        //if (!solver.isSolvable())   
        //    StdOut.println("No solution possible");
        //else {
        //    StdOut.println("Minimum number of moves = " + solver.moves());
        //    for (Board board : solver.solution())
       //         StdOut.println(board);
       // }

    }
} 
