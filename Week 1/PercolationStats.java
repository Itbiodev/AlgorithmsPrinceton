import java.lang.Math;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;


public class PercolationStats {

    private int T;
    private double[] a;

    public PercolationStats(int n, int trials) {
        if ( n < 1 || trials < 1) { 
            throw new IllegalArgumentException();
        } else {
            T = trials;
        }

        a = new double[T];

        for (int i = 0; i < T; i++) {   
             Percolation p = new Percolation(n);
             while (!p.percolates()) {
               p.open(StdRandom.uniform(n)+1,StdRandom.uniform(n)+1);
            }
            a[i] = (double) p.numberOfOpenSites()/(n*n);
        }
    }
    
    public double mean() {
        return StdStats.mean(a);
    }

    public double stddev() {
        return StdStats.stddev(a);
    }

    public double confidenceLo() {
        return mean()-1.96*stddev()/Math.sqrt(T);    
    }

    public double confidenceHi() {
        return mean()+1.96*stddev()/Math.sqrt(T); 
    }
    
    public static void main(String[] args) {
        int n = 0;
        int trials = 0;
        
        try {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
        }

        PercolationStats ps = new PercolationStats(n, trials);

        StdOut.println("mean                    =  " + ps.mean());
        StdOut.println("stddev                  =  " + ps.stddev());
        StdOut.println("95% confidence interval = "+ "["+ps.confidenceLo()+","+ps.confidenceHi()+"]");
    }
}
