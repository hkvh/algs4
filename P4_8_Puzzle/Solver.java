import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
/**
 * Solver Class for solving 8 Puzzle
 * Assignment 4
 * @author Hendrik Kits van Heyningen
 */
public class Solver 
{
	/**
	 * A private class that contains the current Board, the previous node, the Manhattan distance, and the number of moves
	 * @author Hendrik
	 *
	 */
	private class SearchNode
	{
		private Board board;
		private SearchNode previous;
		private int manhattan;
		private int numMoves;
		
		/**
		 * Create a new search node with a given board (assume no previous move)
		 * @param board
		 */
		private SearchNode(Board board)
		{
			this.board = board;
			previous = null;
			manhattan = board.manhattan();
			numMoves = 0;
			
		}
		
		private SearchNode(Board board, SearchNode previous, int numMoves)
		{
			this.board = board;
			this.previous = previous;
			manhattan = board.manhattan();
			this.numMoves = numMoves;
			
		}
		
		/**
		 * Returns the priority of this search node
		 * @return the sum of the number of moves and the manhattan distance
		 */
		private int priority()
		{
			return manhattan + numMoves;
		}
		
		/**
		 * String representaiton of search node
		 */
		@Override
		public String toString()
		{
			//we want to easily build a mutable string so we use string builder
			StringBuilder s = new StringBuilder(); 
			s.append(board + "\n");
			s.append("manhattan: " + manhattan + "\n");
			s.append("numMoves: " + numMoves + "\n");
			s.append("priority: " + priority() + "\n");
			return s.toString();
		}
	}
	
	/**
	 * A comparator for search nodes based on the priority function
	 * @author Hendrik
	 *
	 */
    private class PriorityOrder implements Comparator<SearchNode>
    {
        @Override
		public int compare(SearchNode p, SearchNode q)
        {
            if (p.priority() < q.priority())
                return -1;   
            else if (p.priority() == q.priority())
                return 0;   
            else 
                return 1;
        }
    }
    
    //Class Variables for Solver
    private int moves;
    private SearchNode goal;
    private MinPQ<SearchNode> mPQ;
    private MinPQ<SearchNode> mPQ_t;
    private boolean isSolvable;
    
	/**
	 * Find a solution to the initial board (using the A* algorithm)
	 * @param initial is the original board
	 */
	public Solver(Board initial)
	{
		//put the initial board into a search node and then into a priority cue using priority order
		mPQ = new MinPQ<SearchNode>(new PriorityOrder());
		mPQ.insert(new SearchNode(initial));
		
		//do the same with the twin of the initial board
		mPQ_t = new MinPQ<SearchNode>(new PriorityOrder());
		mPQ_t.insert(new SearchNode(initial.twin()));
		
		//keep looping until we find a solution to this board or the twin
		while (true)
		{
			//if we find the solution then keep track of the search node as goal so we can trace the path backwards
			SearchNode curr = mPQ.delMin();
			SearchNode curr_t = mPQ_t.delMin();

			//Useful for debugging/seeing how the pQ works
			//StdOut.println("Current: " + curr);

			if (curr.board.isGoal())
			{
				goal = curr;
				isSolvable = true;
				moves = goal.numMoves;
				return;
			}
			
			//if the search node from the initial boards' twin's PQ reaches the goal, we know that our initial board has no solution
			if (curr_t.board.isGoal())
			{
				goal = null;
				isSolvable = false;
				moves = -1;
				return;
			}
			
			//Loop through each neighboring board, and add each as a new search node to the priority queue
			for (Board neighbor : curr.board.neighbors())
			{
				//Check if any of them match previous, and don't add those
				if (curr.previous == null || !neighbor.equals(curr.previous.board)) //check for null 
				{
					//Useful for debugging / seeing how the pQ works: 
					//StdOut.println("Neighb: " + new SearchNode(neighbor, curr, curr.numMoves+1));
					mPQ.insert(new SearchNode(neighbor, curr, curr.numMoves+1));
					//adds the new neighbor node with 1 move added
				}	
			}
			
			//do the same thing with the twin board
			for (Board neighbor_t : curr_t.board.neighbors())
			{
				if (curr_t.previous == null || !neighbor_t.equals(curr_t.previous.board))
				{
					mPQ_t.insert(new SearchNode(neighbor_t, curr_t, curr_t.numMoves+1));
				}	
			}
			
		}
	}
	
	/**
	 * Is the initial board solvable?
	 * @return true if the board is solvable, false otherwise
	 */
	public boolean isSolvable()
	{
		//the constructor already concluded whether or not it was solvable
		return isSolvable;
	}
	
	/**
	 * min number of moves to solve initial board; -1 if no solution
	 * @return numMoves if solvable; -1 if no solution
	 */
	public int moves()
	{
		return moves; //recall that we already set this to -1 if no solution
	}
	
	/**
	 * Sequence of boards in a shortest solution; null if no solution
	 * @return a queue containing the boards in order, or null if no solution
	 */
	public Iterable<Board> solution()
	{
		if (moves() == -1)
			return null;
		else
		{
			//we iterate through searchNode going backwards
			//since we are going from goal to initial but want the output from initial to goal, LIFO is desired so we use a stack
			Stack<Board> sB = new Stack<Board>();
			
			SearchNode current = goal;
			//Loop until we finish processing the initial node (the only one with previous == null)
			while (current != null)
			{
				//push the board on the stack
				sB.push(current.board);
				current = current.previous;
			}
			return sB;
		}
	}
	
	/**
	 * Main method to solve 8 puzzle using Solver and Board classes
	 * @param args the puzzle to read in
	 */
	public static void main(String[] args) 
	{ 
		
		In in = new In(args[0]);
		int N = in.readInt(); // create initial board from file 

		int[][] blocks = new int[N][N]; 
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) 
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		
		// solve the puzzle
		Solver solver = new Solver(initial);
		
		// print solution to standard output 
		if (!solver.isSolvable())
			StdOut.println("No solution possible"); 
		else 
		{
			StdOut.println("Minimum number of moves = " + solver.moves() + "\n"); 
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
