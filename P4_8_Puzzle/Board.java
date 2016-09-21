import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Board Class for solving 8 Puzzle
 * Assignment 4
 * @author Hendrik Kits van Heyningen
 */
public class Board 
{
	private int N; 			//size of board (number of rows and columns)
	private int [][] tiles; //contains the 2D array of size NxN
	
	/**
	 *  construct a board from an N-by-N array of blocks 
	 * @param blocks, blocks[i][j] = block in row i, column j
	 */
	public Board(int[][] blocks)
	{
		N = blocks[0].length; //gets N assuming blocks is square
		
		tiles = new int[N][N]; //stores the 2D array as a class variable 
		//Note: coding this as a 2D array copy is very important since we want to pass the array by value
		//Saying tiles = blocks.clone() would not work as desried because in a 2D array
		// we would merely get different references to each of the row arrays, so changing one in the original would change one here
		// This is why it is best to simply copy each element by value
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				tiles[i][j] = blocks[i][j];
	}
	
	/**
	 * Returns the board dimension N
	 * @return N
	 */
	public int dimension()
	{
		return N;
	}
	
	/**
	 * Computes the number of blocks out of place
	 * @return this hamming out-of-place sum
	 */
	public int hamming()
	{
		//make a hamming counter
		int h = 0;
		
		//loop through the block and increase each time a tile is not in the right position
		for (int i = 0; i < N; i++)
		{
			for (int j = 0; j < N; j++) 
			{
				//skip the blank tile
				if (tiles[i][j] != 0)
				{
					//check if our tile is where it should be if the game is finished
					if (tiles[i][j] != i*N + j + 1) 
						h++; //if it isn't increase the counter
				}
			}
		}
		return h;
	}
	
	/**
	 * Computes the sum of distances between blocks and each goal position
	 * @return this Manhattan distance sum
	 */
	public int manhattan()
	{
		//make a manhattan counter
		int m = 0;
		
		//loop through the block and see if it is in the right position
		for (int i = 0; i < N; i++)
		{
			for (int j = 0; j < N; j++) 
			{
				//skip the blank spot
				if (tiles[i][j] != 0)
				{
					//find the i and j where our tile should be
					int goal_i = (tiles[i][j]-1)/N;
					int goal_j = (tiles[i][j]-1)%N;
					
					//increase the counter by the number of offset rows and columns
					m += (Math.abs(goal_i - i) + Math.abs(goal_j - j)); 
					//this is 0 if the tile is in the right place since goal_i = i and goal_j = j;
				}
			}
		}
		return m;
	}
	
	/**
	 *  is this board the goal board?
	 * @return true if the board is in finished order with numbers 1 - N displayed in order
	 */
	public boolean isGoal()
	{
		return manhattan() == 0; //if either function is 0 we know we have the goal board
	}
	
	// a board obtained by exchanging two adjacent blocks in the same row
	public Board twin()
	{
		//row search: find a row that does not have the blank square
		int row = 0;
		//see if first row has it; if it does, use second row
		for (int i = 0; i < N; i++)
		{
			if (tiles[0][i] == 0)
			{
				row = 1;
				break;
			}
		}
		//exchange the first and second block (row, 0) and (row, 1)
		exchange(tiles, row, 0, row, 1);
		
		//make a new board with this swap in place
		Board twin = new Board(tiles);
		
		//undo the swap to restore tiles (but the swapped board is stored in twin)
		exchange(tiles, row, 0, row, 1); 
		return twin;
		
	}
	
	/**
	 * Does this board equal y?
	 * @return false if the tiles in the boards aren't the same, true otherwise
	 */
	@Override
	public boolean equals(Object y)
	{
		if (y == this) 
			return true;
        if (y == null) 
        	return false;
        if (y.getClass() != this.getClass()) 
        	return false;
        Board that = (Board) y; //cast guaranteed to succeed
        
        //check to make sure dimensions are equal
        if (this.N != that.N)
        	return false;
        
        boolean all_equal = true;
        for (int i = 0; i < N; i++) //if any of the 2D array elements are not equal, then not equal
        	for (int j = 0; j < N; j++)
        		all_equal = all_equal && this.tiles[i][j] == that.tiles[i][j];
	
        return all_equal;
	}
	
	/**
	 * Gets all neighbors of a given board (at most 4)
	 * @return A Queue containing the 2-4 neighbors
	 */
	public Iterable<Board> neighbors()
	{
		//find the row and column of the blank spot
		int row0 = 0;
		int col0 = 0;
		
		for (int i = 0; i < N; i++)
		{
			for (int j = 0; j < N; j++)
			{
				if (tiles[i][j] == 0)
				{
					row0 = i;
					col0 = j;
				}
			}
		}
		Queue<Board> neighbors = new Queue<Board>(); //make an iterable queue to hold the neighbors
				
		if (row0 != 0) //then we can exchange a piece from above
		{		                        
			exchange(tiles, row0, col0, row0-1, col0);
			neighbors.enqueue(new Board(tiles));
			
			//undo the swap to reset tiles (since neighbor already enqueued with the swap)
			exchange(tiles, row0, col0, row0-1, col0); 
		}
		if (row0 != N-1) //then we can exchange a piece from below
		{
			exchange(tiles, row0, col0, row0+1, col0);
			neighbors.enqueue(new Board(tiles));
			//undo the swap to reset tiles (since neighbor already enqueued with the swap)
			exchange(tiles, row0, col0, row0+1, col0); 
		}
		if (col0 != 0) //then we can exchange a piece from the left
		{
			exchange(tiles, row0, col0, row0, col0-1);
			neighbors.enqueue(new Board(tiles));
			//undo the swap to reset tiles (since neighbor already enqueued with the swap)
			exchange(tiles, row0, col0, row0, col0-1);
		}
		if (col0 != N-1) //then we can exchange a piece from the right
		{
			exchange(tiles, row0, col0, row0, col0+1);
			neighbors.enqueue(new Board(tiles));
			//undo the swap to reset tiles (since neighbor already enqueued with the swap)
			exchange(tiles, row0, col0, row0, col0+1);
		}
		return neighbors;
		
	}
	/**
	 * A private method that exchanges (i1, j1) with (i2, j2)
	 * @param array the array to exchange from (passed by reference so will change)
	 * @param i1 = row 1
	 * @param j1 = col 1
	 * @param i2 = row 2
	 * @param j2 = col 2
	 */
	private void exchange(int [][] array, int i1, int j1, int i2, int j2)
	{
		int temp = array[i1][j1];
		array[i1][j1] = array[i2][j2];
		array[i2][j2] = temp;
	}
	
	/** String representation of the board
	 * @return The String representation of the board with the board
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		//we want to easily build a mutable string so we use string builder
		StringBuilder s = new StringBuilder(); 
		s.append(N + "\n");
		for (int i = 0; i < N; i++) 
		{
			for (int j = 0; j < N; j++) 
			{ 
				//formats as a decimal integer with width 2
				s.append(String.format("%2d ", tiles[i][j])); 
			}
			s.append("\n"); //add new line fore each row
		}
		return s.toString();
	}
	
	
	
	//
	/** For testing (not in API though, so comment out when done)
	 * 
	 */
	public static void main (String [] args)
	{
		int [][] a = {
				{8, 0, 3},
				{4, 1, 2},
				{7, 6, 5}
		};
		
		int [][] a_ = {
				{8, 1, 3},
				{4, 0, 2},
				{7, 6, 5}
		};
		Board b = new Board(a);
		Board b_ = new Board(a_);
		
		StdOut.println("Board:\n" + b);
		StdOut.println("IsGoal: " + b.isGoal());
		StdOut.println("Equals: " + b.equals(b_));
		StdOut.println("\nTwin:\n"+ b.twin());
		StdOut.println("Manhattan: " + b.manhattan());
		StdOut.println("Hamming: " + b.hamming());
		
		StdOut.println("\nNeighbors:\n");
		for (Board c : b.neighbors())
			StdOut.println(c);
	}
	//
}
