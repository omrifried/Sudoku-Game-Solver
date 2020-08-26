/**
 * Create a base grid that has the given underlying Sudoku Grid. The purpose of this class
 * is to create base grid that can be adjusted (played on) without altering the actual solution
 * board (i.e. playing won't change the actual solution board).
 * 
 */
public class SudokuBaseGrid
{
    private SudokuGridGenerator sudokuGrid;
    private int[][] baseGrid;

    /**
     * Create a base grid that has the given underlying Sudoku Grid. 
     * 
     * @param SudokuGridGenerator the Sudoku Grid to use for for this baseGrid.
     */
    public SudokuBaseGrid(SudokuGridGenerator sudokuGridGenerator)
    {
        sudokuGrid = sudokuGridGenerator;
        baseGrid = new int[SudokuGridGenerator.BOARD_SIZE][SudokuGridGenerator.BOARD_SIZE];
        for (int i = 0; i < SudokuGridGenerator.BOARD_SIZE; i++)
        {
            for (int j = 0; j < SudokuGridGenerator.BOARD_SIZE; j++)
            {
                baseGrid[i][j] = sudokuGrid.getBoard()[i][j];
            }
        }
    }
    
    public SudokuGridGenerator getSolvedGrid()
    {
        setSolvedGrid();
        return sudokuGrid;
    }
    
    public SudokuGridGenerator getGridGen()
    {
        return sudokuGrid;
    }
    
    /**
     * Returns a reference to the base grid.
     * 
     * @return the base grid
     */
    public int[][] getBaseGrid()
    {
        return baseGrid;
    }
    
    /**
     * Solve the Sudoku board.
     */
    private void setSolvedGrid()
    {
        sudokuGrid.populateRemainingBoard(sudokuGrid.getBoard(), 0);
    }
}
