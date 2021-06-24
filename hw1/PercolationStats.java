import edu.princeton.cs.algs4.StdRandom;

import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] thresholds;
    private static double confiConst = 1.96;

    // perform independent trials on n*n grid
    public PercolationStats(int n, int trials) {
        if (n < 0 || trials < 0) throw new IllegalArgumentException("Invalid input of n or trail");

        thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            }
            thresholds[i] = percolation.numberOfOpenSites()/(double) (n*n);
        }            
    }

    // calculating the mean 
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // calculating the standard deviation
    public double stddev() {
       return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        int len = thresholds.length;
        double sqrtTrial = Math.sqrt(len);
        double result = StdStats.mean(thresholds)-(confiConst*StdStats.stddev(thresholds))/sqrtTrial;
        return result;
    }

    public double confidenceHi() {
        int len = thresholds.length;
        double sqrtTrial = Math.sqrt(len);
        double result = StdStats.mean(thresholds)+(confiConst*StdStats.stddev(thresholds))/sqrtTrial;
        return result;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0], 10);
        int trials = Integer.parseInt(args[1], 10);
        PercolationStats percolationstats = new PercolationStats(n, trials);
        double mean = percolationstats.mean();
        double stddev = percolationstats.stddev();
        double confiLow = percolationstats.confidenceLo();
        double confiHigh = percolationstats.confidenceHi();
        System.out.println("mean" + "                    = " + mean);
        System.out.println("stddev" + "                  = " + stddev);
        System.out.println("95% confidence interval" + " = " + "[" + confiLow + ", " + confiHigh + "]");

    }
}