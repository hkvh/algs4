import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation Class Assignment 1
 * 
 * @author Hendrik Kits van Heyningen
 */
public class Percolation {
	
	private int N; // the N x N grid size of the percolation
	
	private WeightedQuickUnionUF cells; // An array of open cells that uses the
										// quick union functionality 
										//c ontains 2 extra cells for top row and bottom row
	
	private WeightedQuickUnionUF cellsNoBottomRow; // The same thing as cells except
												// does not connect to the bottom row
												// (for isFull to prevent backwash)
	
	//If the grid percolates, then bottom connects to top, but we wouldn't want isFull to use that path when determinining if a cell connects to the top
	
	private boolean[] openArray; // An array that stores whether each cell site
									// is open or closed
	private final int TOP_ROW_INDEX = 0; // Const int for index in cells
											// representing the top row (above NxN grid)
	private final int BOTTOM_ROW_INDEX; // Const int for index in cells
										// representing the bottom row (below NxN grid)
	private int numOpen;

	/**
	 * Constructor creates N-by-N grid, with all sites blocked
	 * 
	 * @param N, the size of the grid
	 */
	public Percolation(int N) {
		if (N <= 0)
			throw new IllegalArgumentException("N must be greater than 0");
		this.N = N;
		BOTTOM_ROW_INDEX = N * N + 1;

		// Fill out the arrays with N^2 + 2 objects, saving 0 and N^2 + 1 for
		// the bottom and top rows, respectively
		cells = new WeightedQuickUnionUF(N * N + 2);
		cellsNoBottomRow = new WeightedQuickUnionUF(N * N + 1); // no link to the bottom row
		openArray = new boolean[N * N + 2];

		numOpen = 0;

		// The bottom and the top are always opne, so:
		openArray[BOTTOM_ROW_INDEX] = true;
		openArray[TOP_ROW_INDEX] = true;
	}

	/**
	 * Private method maps 2D x,y coords (1-indexed) to 1D UnionFind Coords
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int xyto1D(int x, int y) {
		// We want 0 to be the top row, and N^2+1 to be the bottom row
		// Each row increases the index by N and fist row, first column is index 1
		int index = (x - 1) * N + y;
		return index;
	}

	/**
	 * Private method checks if i and j are between 1 and N
	 * 
	 * @param i
	 * @param j
	 */
	private boolean isvalidIndices(int i, int j) {
		if (i < 1 || i > N || j < 1 || j > N)
			throw new IndexOutOfBoundsException("i and j must be integers between 1 and N");
		else
			return true;
	}

	/**
	 * Opens a cell located at (i, j), checks if i and j are valid, opens the cell and provides the connection in cells
	 * 
	 * @param i, the row between 1 and N
	 * @param j, the column between 1 and N
	 */
	public void open(int i, int j) {
		if (isvalidIndices(i, j)) {
			int currentIndex = xyto1D(i, j); // gets the 1D associated index
			openArray[currentIndex] = true;
			numOpen++;
			// Now we check the 4 neighboring cells and union them if they are open
			// We making sure we're not on an edge row or column in each case
			// (as is relevant for each direction)
			if (i != 1) {
				int upIndex = xyto1D(i - 1, j);
				if (openArray[upIndex]) {
					cells.union(currentIndex, upIndex);
					cellsNoBottomRow.union(currentIndex, upIndex);
				}
				// we are at the top row and should connect it to our top row marker
			} else {
				cells.union(TOP_ROW_INDEX, currentIndex);
				cellsNoBottomRow.union(TOP_ROW_INDEX, currentIndex);
			}

			if (i != N) {
				int downIndex = xyto1D(i + 1, j);
				if (openArray[downIndex]) {
					cells.union(currentIndex, downIndex);
					cellsNoBottomRow.union(currentIndex, downIndex);
				}
			} else // we are at the bottom row and should connect it to our bottom row marker
				cells.union(BOTTOM_ROW_INDEX, currentIndex);
			// Don't want to connect cellsNoBottomRow to this by definition

			if (j != 1) {
				int leftIndex = xyto1D(i, j - 1);
				if (openArray[leftIndex]) {
					cells.union(currentIndex, leftIndex);
					cellsNoBottomRow.union(currentIndex, leftIndex);
				}
			}
			if (j != N) {
				int rightIndex = xyto1D(i, j + 1);
				if (openArray[rightIndex]) {
					cells.union(currentIndex, rightIndex);
					cellsNoBottomRow.union(currentIndex, rightIndex);
				}
			}
		}
		return;
	}

	/**
	 * Checks if a given cell at (i, j) is open
	 * 
	 * @param i
	 * @param j
	 * @return true if cell is open, false otherwise
	 */
	public boolean isOpen(int i, int j) {
		if (isvalidIndices(i, j)) {
			int index = xyto1D(i, j); // gets the 1D associated index
			if (openArray[index])
				return true;
			else
				return false;
		}
		return false; // if we throw an exception because of the indices this
						// shouldn't be reached
	}

	/**
	 * Method that checks if a given cell at i,j is full, i.e. is connected to the top row
	 * 
	 * @param i
	 * @param j
	 * @return true, if cell is full, false otherwise
	 */
	public boolean isFull(int i, int j) {
		// check if it's open
		if (isOpen(i, j)) {
			int index = xyto1D(i, j); // gets the 1D associated index
			return cellsNoBottomRow.connected(TOP_ROW_INDEX, index);
			// checks if it's connected to the top row to see if it's full
			// Note that we use NoEnd to prevent the backwash
		} else // if it's not open, it's clearly not full
			return false;
	}

	/**
	 * Method that checks if the whole system percolates by seeing if the top row connects to the bottom row
	 */
	public boolean percolates() {
		// For percolates we want to use the cells with the end
		return cells.connected(TOP_ROW_INDEX, BOTTOM_ROW_INDEX);
	}
	
	/**
	 * Gets the number of open sites
	 * @return the number of open sites
	 */
	public int numberOfOpenSites() {
		return this.numOpen;
	}

}
