import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats 
{
    private int T;       //number of independent experiments, greater than 0 
    private double[] percThresholds;  //an array that contains values for the fraction of open cells when the system percolated
    
    /**
     * Constructor that performs T independent computational experiments on an N-by-N grid
     * @param N number of rows and columns of grid, greater than 0
     * @param T number of independent experiments, greater than 0 
     */
    public PercolationStats(int N, int T) 
    {
        //Make sure our arguments are positive integers
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("N and T must be greater than 0");
        this.T = T;
        
        percThresholds = new double[T];
        
        //Loop through the T experiments
        for (int i = 0; i < T; i++)
        {
            Percolation perc = new Percolation(N);
            
            int numOpen = 0; //keep a counter to keep track of how many open cells
            
            //Keep opening new cells at random until the system percolates
            while(!perc.percolates())
            {
                //Find a random site to open
                int row = 0;
                int col = 0;
                do
                {
                    row = StdRandom.uniform(1, N+1); //Uniform goes from [a,b) so need [a,b+1) to get [a,b]
                    col = StdRandom.uniform(1, N+1);
                } while (perc.isOpen(row, col)); //as long as we picked one open, keep looping
                perc.open(row, col);
                numOpen++;
            }
            percThresholds[i] = ((double)numOpen)/(N*N);
        }
    }
    
    /**
     * Sample mean of percolation threshold with static method from stdstats
     * @return mean
     */
    public double mean()
    {
        return StdStats.mean(percThresholds);
    }
    
    /**
     * Sample standard deviation of percolation threshold with static method from stdstats
     * @return mean
     */
    public double stddev()
    {
        if (T != 1)
            return StdStats.stddev(percThresholds);
        else //stddev not defined for one experiment, so:
            return Double.NaN;
    }
    
    /**
     * Computes  lower bound of the 95% confidence interval using the given formula
     * @return lower bound of confidence interval
     */
    public double confidenceLo()
    {
        return mean() - 1.96*stddev()/Math.sqrt(T);
        
    }
    /**
     * Computes  higher bound of the 95% confidence interval
     * @return higher bound of confidence interval
     */
    public double confidenceHi()
    {
        return mean() + 1.96*stddev()/Math.sqrt(T);
    }
    
    /**
     * Test client to determine the probability p via Monte Carlo simulations
     * @param args command line arguments that take two integers, N and T
     */
    public static void main(String[] args) 
    {
        //Parse our command line arguments to get N and T
        int commandN = 0;
        int commandT = 0;
        try 
        {
            commandN = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
            System.exit(1);
        }
        try 
        {
            commandT = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[1] + " must be an integer.");
            System.exit(1);
        }
        
        PercolationStats ps = new PercolationStats(commandN, commandT); 
        Out out = new Out(); //Create an output object for printing
        
        out.println("mean\t\t\t= " + ps.mean());
        out.println("stddev\t\t\t= " + ps.stddev());
        out.println("95% confidence interval\t= " + ps.confidenceLo() + ", " + ps.confidenceHi());
        
        
    }
    
}
