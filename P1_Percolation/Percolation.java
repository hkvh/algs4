import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation Class
 * Assignment 1
 * @author Hendrik Kits van Heyningen
 */
public class Percolation
{
	private int N; 							//the N x N grid size of the percolation
	private WeightedQuickUnionUF cells;		//An array of open cells that uses the quick union functionality
	private WeightedQuickUnionUF cellsNoEnd; //The same thing as cells except does not connect to the end (for isFull to prevent backwash)
	private boolean[] openArray; 				//An array that stores whether each cell site is open or closed
	private final int TOP_ROW_INDEX = 0;		//Const int for index in cells representing the top row 
	private final int BOTTOM_ROW_INDEX;	//Const int for index in cells representing the bottom row 
	
	/**
	 *  Constructor creates N-by-N grid, with all sites blocked
	 * @param N, the size of the grid
	 */
	public Percolation(int N)
	{
		if (N <= 0)
            throw new IllegalArgumentException("N must be greater than 0");
		this.N = N;
		BOTTOM_ROW_INDEX = N*N+1;
		
		//Fill out the arrays with N^2 + 2 objects, saving 0 and N^2 + 1 for the bottom and top rows, respectively
		cells = new WeightedQuickUnionUF(N*N+2);
		cellsNoEnd = new WeightedQuickUnionUF(N*N+1); //no last cell
		openArray = new boolean[N*N+2];
		
		//The bottom and the top are always open, so:
		openArray[BOTTOM_ROW_INDEX] = true;
		openArray[TOP_ROW_INDEX] = true;
	}
	
	/**
	 * Private method maps 2D x,y coords to 1D UnionFind Coords
	 * @param x
	 * @param y
	 * @return
	 */
	private int xyto1D(int x, int y)
	{
		//We want 0 to be the top row, and N^2+1 to be the bottom row
		//Each row increases the index by N with the first row being an effective 0
		int index = (x-1)*N + y;
		return index;
	}
	
	/**
	 * Private method checks if i and j are between 1 and N
	 * @param i
	 * @param j
	 */
	private boolean isvalidIndices(int i, int j)
	{
		if (i < 1 || i > N || j < 1 || j > N)
			throw new IndexOutOfBoundsException("i and j must be integers between 1 and N");
		else
			return true;
	}
	
	/**
	 * Opens a cell located at (i, j), checks if i and j are valid, opens the cell and provides the connection in cells
	 * @param i, the row between 1 and N
	 * @param j, the column between 1 and N
	 */
	public void open(int i, int j) 
	{
		if (isvalidIndices(i, j))
		{
			int currentIndex = xyto1D(i, j); //gets the 1D associated index
			openArray[currentIndex] = true;
			
			//Now we check the 4 neighboring cells and union them if they are open
			//We making sure we're not on an edge row or column in each case (as is relevant for each direction)
			if (i != 1) 
			{
				int upIndex = xyto1D(i-1, j);
				if (openArray[upIndex])
				{
					cells.union(currentIndex, upIndex);
					cellsNoEnd.union(currentIndex, upIndex);
				}
			}
			else //we are at the top row and should connect it to our top row marker
			{
				cells.union(TOP_ROW_INDEX, currentIndex);
				cellsNoEnd.union(TOP_ROW_INDEX, currentIndex);
			}
				
			if (i != N) 
			{
				int downIndex = xyto1D(i+1, j);
				if (openArray[downIndex])
				{
					cells.union(currentIndex, downIndex);
					cellsNoEnd.union(currentIndex, downIndex);
				}
			}
			else //we are at the bottom row and should connect it to our bottom row marker
				cells.union(BOTTOM_ROW_INDEX, currentIndex);
				//Don't want to connect cellsNoEnd to this
			
			if (j != 1) 
			{
				int leftIndex = xyto1D(i, j-1);
				if (openArray[leftIndex])
				{
					cells.union(currentIndex, leftIndex);
					cellsNoEnd.union(currentIndex, leftIndex);
				}
			}
			if (j != N) 
			{
				int rightIndex = xyto1D(i, j+1);
				if (openArray[rightIndex])
				{
					cells.union(currentIndex, rightIndex);
					cellsNoEnd.union(currentIndex, rightIndex);
				}
			}	
		}
		return;
	}
	
	/**
	 * Checks if a given cell at (i, j) is open
	 * @param i
	 * @param j
	 * @return true if cell is open, false otherwise
	 */
	public boolean isOpen(int i, int j) 
	{
		if (isvalidIndices(i, j))
		{
			int index = xyto1D(i, j); //gets the 1D associated index
			if (openArray[index])
				return true;
			else
				return false;
		}
		return false; //if we throw an exception because of the indices this shouldn't be reached
	}
	
	/**
	 * Method that checks if a given cell at i,j is full, i.e. is connected to the top row
	 * @param i
	 * @param j
	 * @return true, if cell is full, false otherwise
	 */
	public boolean isFull(int i, int j) 
	{
		if (isOpen(i, j)) // check if it's open
		{
			int index = xyto1D(i, j); //gets the 1D associated index
			return cellsNoEnd.connected(TOP_ROW_INDEX, index); 
			//checks if it's connected to the top row to see if it's full
			//Note that we use NoEnd to prevent the backwash 
		}
		else //if it's not open, it's clearly not full
			return false;
	}
	
	/**
	 * Method that checks if the whole system percolates by seeing if the
	 */
	public boolean percolates()
	{
		//For percolates we want to use the cells with the end
		return cells.connected(TOP_ROW_INDEX, BOTTOM_ROW_INDEX);
	}

}
