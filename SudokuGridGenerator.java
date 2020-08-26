import java.util.*;
import java.awt.Point;

/**
 * SudokuGridGenerator class
 * 
 * This is the underlying data for the Sudoku board. The class generates the
 * Sudoku board using the populateFirstBoard, populateRemainingBoard, and
 * finalizeBoard methods. Additionally, the populateRemainingBoard method can be
 * used as a Sudoku solver.
 * 
 * In order to generate an initial board, a Sudoku board is first completely
 * filled out. The finalizeBoard method then removes numbers in the grid at
 * random and determines whether the removed number is legal (i.e. it still
 * allows for only one Sudoku solution and satisfies all Sudoku grid
 * requirements).
 */
public class SudokuGridGenerator
{
    public static final int MAX_NUM = 9;
    public static final int MIN_NUM = 1;
    public static final int EMPTY_SQUARE = 0;
    public static final int BOARD_SIZE = 9;
    public static final int SQUARE_SIZE = (int) Math.floor(Math.sqrt(BOARD_SIZE));;
    private int[][] board;
    private Random generator;
    private String gameType;

    /**
     * Create a 2D array to hold the Sudoku grid and corresponding numbers. The
     * input string will determine the difficulty of the game through the number of
     * initially empty cells.
     * 
     * @param game the difficulty of the game.
     */
    public SudokuGridGenerator()
    {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        generator = new Random();
        gameType = "";
    }

    /**
     * Set the gameType based on user input.
     * 
     * @param game the difficulty of the game.
     */
    public void setGameType(String game)
    {
        game.toLowerCase();
        gameType = game;
    }

    public int[][] getBoard()
    {
        return board;
    }

    public void resetBoard()
    {
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            for (int j = 0; j < BOARD_SIZE; j++)
            {
                board[i][j] = EMPTY_SQUARE;
            }
        }
    }

    public int squareVal(int row, int col)
    {
        return board[row][col];
    }

    /**
     * Determine whether the game type provided via user input is valid.
     * 
     * @param the game difficulty provided by the user input.
     * @return boolean to determine valid game type.
     */
    public boolean validGameType(String gameMode)
    {
        gameMode.toLowerCase();
        if (gameMode.compareTo("easy") == 0 || gameMode.compareTo("medium") == 0 || gameMode.compareTo("hard") == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Randomly fill the empty game board. Determine the x and y coordinates
     * randomly and fill that cell with a random number between 1 and 9. The numbers
     * must adhere to Sudoku requirements (row, column, and square regulations).
     * Continue to randomly fill numbers until the requirements are broken.
     * 
     * @param xCoord  the initial x coordinate in which the new number will be
     *                placed.
     * @param yCoord  the initial y coordinate in which the new number will be
     *                placed.
     * @param randVal the initial random value that will be placed at the
     *                coordinate.
     * @return a 2D array with the partially filled grid.
     */
    public int[][] populateFirstBoard(int xCoord, int yCoord, int randVal)
    {
        int[][] gameBoard = new int[BOARD_SIZE][BOARD_SIZE];
        // Generate a random number between 1 and 9.
        board[EMPTY_SQUARE][EMPTY_SQUARE] = generator.nextInt(MAX_NUM) + 1;
        // While the row, column, and square preconditions are met, continue to populate
        // the board.
        while (numAvailableRow(board, randVal, xCoord) && numAvailableCol(board, randVal, yCoord)
                && numAvailableSquare(board, randVal, squareRangeRow(xCoord), squareRangeCol(yCoord),
                        squareRangeRow(xCoord) - SQUARE_SIZE, squareRangeCol(yCoord) - SQUARE_SIZE))
        {
            // Only fill the cell if it is currently empty.
            if (board[xCoord][yCoord] == EMPTY_SQUARE)
            {
                board[xCoord][yCoord] = randVal;
            }
            // Generate a new coordinate set and number for the next run.
            xCoord = generator.nextInt(BOARD_SIZE);
            yCoord = generator.nextInt(BOARD_SIZE);
            randVal = generator.nextInt(MAX_NUM) + 1;
        }
        gameBoard = board;
        return gameBoard;
    }

    /**
     * Solve the Sudoku grid recursively by completing the partially filled board.
     * Return true if the board was filled correctly. If the board was unable to be
     * completed, return false. The method utilizes the backtracking algorithm to
     * solve the board.
     * 
     * @param gameBoard the partially filled Sudoku grid.
     * @param tracker   a counter to progress through the grid.
     * @return boolean that determines whether the board was completed.
     */
    public boolean populateRemainingBoard(int[][] gameBoard, int tracker)
    {
        int xCoord = tracker / BOARD_SIZE;
        int yCoord = tracker % BOARD_SIZE;
        // If all cells have been visited and there were no errors, return true.
        if (tracker >= BOARD_SIZE * BOARD_SIZE)
        {
            return true;
        }
        // If the current cell is not empty, move to the next cell.
        if (gameBoard[xCoord][yCoord] != EMPTY_SQUARE)
        {
            return populateRemainingBoard(gameBoard, tracker + 1);
        }
        else
        {
            // Attempt to put any value between 1 and 9 in the cell.
            for (int newVal = 1; newVal <= MAX_NUM; newVal++)
            {
                // If the current value satisfies all Sudoku preconditions, place it in the
                // cell.
                if (numAvailableRow(gameBoard, newVal, xCoord) && numAvailableCol(gameBoard, newVal, yCoord)
                        && numAvailableSquare(gameBoard, newVal, squareRangeRow(xCoord), squareRangeCol(yCoord),
                                squareRangeRow(xCoord) - SQUARE_SIZE, squareRangeCol(yCoord) - SQUARE_SIZE))
                {
                    gameBoard[xCoord][yCoord] = newVal;
                    /*
                     * Determine whether the value placed in the cell allows for the board to be
                     * completed. If the board can be completed (returns true), then complete the
                     * board and return true for the function.
                     */
                    if (populateRemainingBoard(gameBoard, tracker + 1))
                    {
                        return true;
                    }
                    // If the board cannot be completed with the value, set cell to empty.
                    else
                    {
                        gameBoard[xCoord][yCoord] = EMPTY_SQUARE;
                    }
                }
            }
        }
        /*
         * Return false if the cell cannot be completed with any value. If the cell
         * cannot be filled, the board is not valid.
         */
        return false;
    }

    /**
     * If the user manually enters a board, traverse the board to determine whether
     * it is valid and satisfies the row, column, and square preconditions.
     * 
     * @return a boolean of whether the board is valid or not.
     */
    public boolean validGameBoardManual()
    {
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
        {
            int xCoord = i / BOARD_SIZE;
            int yCoord = i % BOARD_SIZE;
            int boardVal = board[xCoord][yCoord];
            if (boardVal != EMPTY_SQUARE)
            {
                // If any precondition is not met, return false.
                if (!numAvailableRowManual(boardVal, xCoord) || !numAvailableColManual(boardVal, yCoord)
                        || !numAvailableSquareManual(boardVal, squareRangeRow(xCoord), squareRangeCol(yCoord),
                                squareRangeRow(xCoord) - SQUARE_SIZE, squareRangeCol(yCoord) - SQUARE_SIZE))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean validGameBoard(int row, int col, int boardVal, int[][] board)
    {
        if (!numAvailableRow(board, boardVal, row) || !numAvailableCol(board, boardVal, col)
                || !numAvailableSquare(board, boardVal, squareRangeRow(row), squareRangeCol(col),
                        squareRangeRow(row) - SQUARE_SIZE, squareRangeCol(col) - SQUARE_SIZE))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Randomly remove cells from the completed game board. The cell can only be
     * removed if, once removed, the board only has one solution and the solution
     * matches the initially filled board. If the cell removal leads to multiple
     * solutions, then place the number back in the cell and try a different cell.
     * The number of removal attempts is determined by the game difficulty.
     */
    public void finalizeBoard()
    {
        int totalSolveAttempts = numsRemoved();
        int[][] boardCopy = new int[BOARD_SIZE][BOARD_SIZE];
        // Store the initial board values in a map.
        Map<Point, Integer> gridMap = new HashMap<Point, Integer>();
        // Make a hard copy of the board and fill the map.
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            for (int j = 0; j < BOARD_SIZE; j++)
            {
                boardCopy[i][j] = board[i][j];
                gridMap.put(new Point(i, j), board[i][j]);
            }
        }
        // Continue attempting to remove cells while the attempts is greater than zero.
        while (totalSolveAttempts > EMPTY_SQUARE)
        {
            // Randomly generate board coordinates.
            int xCoord = generator.nextInt(BOARD_SIZE);
            int yCoord = generator.nextInt(BOARD_SIZE);
            if (board[xCoord][yCoord] != EMPTY_SQUARE)
            {
                // Remove the cell in the copy board.
                boardCopy[xCoord][yCoord] = EMPTY_SQUARE;
                /*
                 * If the board is non unique after removal, then reset the copy board and
                 * reduce solve attempts.
                 */
                if (boardNonUnique(boardCopy, xCoord, yCoord, gridMap, EMPTY_SQUARE))
                {
                    for (int i = 0; i < BOARD_SIZE; i++)
                    {
                        for (int j = 0; j < BOARD_SIZE; j++)
                        {
                            boardCopy[i][j] = board[i][j];
                        }
                    }
                    totalSolveAttempts--;
                }
                /*
                 * Otherwise, set the cell in the actual board to empty and update the copy
                 * board. The copy board needs to be updated because the helper functions solves
                 * the board.
                 */
                else
                {
                    board[xCoord][yCoord] = EMPTY_SQUARE;
                    for (int i = 0; i < BOARD_SIZE; i++)
                    {
                        for (int j = 0; j < BOARD_SIZE; j++)
                        {
                            boardCopy[i][j] = board[i][j];
                        }
                    }

                }
            }

        }
    }

    // Print the board.
    public void printAll()
    {
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            for (int j = 0; j < BOARD_SIZE; j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

    }

    /**
     * Determines whether the number can be placed in the specified column. If the
     * number cannot be placed (is not available), the method will return false.
     * 
     * @param gameBoard a 2D array representing the game grid.
     * @param number    the number being tested in the column.
     * @param col       the column being tested.
     * @return a boolean to determine whether the number can be in the column.
     */
    private boolean numAvailableCol(int[][] gameBoard, int number, int col)
    {
        boolean isAvailable = true;
        for (int j = 0; j < BOARD_SIZE; j++)
        {
            if (gameBoard[j][col] == number)
            {
                isAvailable = false;
                break;
            }
        }
        return isAvailable;
    }

    /**
     * Determines whether the number can be placed in the specified row. If the
     * number cannot be placed (is not available), the method will return false.
     * 
     * @param gameBoard a 2D array representing the game grid.
     * @param number    the number being tested in the row.
     * @param row       the row being tested.
     * @return a boolean to determine whether the number can be in the row.
     */
    private boolean numAvailableRow(int[][] gameBoard, int number, int row)
    {
        boolean isAvailable = true;
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            if (gameBoard[row][i] == number)
            {
                isAvailable = false;
                break;
            }
        }
        return isAvailable;
    }

    /**
     * Determine the upper row limit for the square the tracker is currently in. If
     * the tracker is in square 1, the limit will be 3. If the tracker is in square
     * 2 the limit will be 6. If the tracker is in square 3, the limit will be 9.
     * The method will return an integer that corresponds to the row limit for the
     * square the tracker is currently in. The integer returned will allow us to
     * traverse the square to determine whether numbers are unique in that square.
     * 
     * @param row the row being tested.
     * @return an integer that corresponds to the upper row limit for the square.
     */
    private int squareRangeRow(int row)
    {
        int cellSquare = row / SQUARE_SIZE;
        return (cellSquare * SQUARE_SIZE + SQUARE_SIZE);
    }

    /**
     * Determine the upper column limit for the square the tracker is currently in.
     * If the tracker is in square 1, the limit will be 3. If the tracker is in
     * square 2 the limit will be 6. If the tracker is in square 3, the limit will
     * be 9. The method will return an integer that corresponds to the column limit
     * for the square the tracker is currently in. The integer returned will allow
     * us to traverse the square to determine whether numbers are unique in that
     * square.
     * 
     * @param col the column being tested.
     * @return an integer that corresponds to the upper col limit for the square.
     */
    private int squareRangeCol(int col)
    {
        int cellSquare = col / SQUARE_SIZE;
        return (cellSquare * SQUARE_SIZE + SQUARE_SIZE);
    }

    /**
     * Determines whether the number can be placed in the specified square. If the
     * number cannot be placed (is not available), the method will return false.
     * 
     * @param gameBoard a 2D array representing the game grid.
     * @param valSearch the number being tested in the square.
     * @param maxRow    the upper row limit for the square.
     * @param maxCol    the upper column limit for the square.
     * @param minRow    the lower row limit for the square.
     * @param minCol    the lower column limit for the square.
     * @return a boolean to determine whether the number can be in the square.
     */
    private boolean numAvailableSquare(int[][] gameBoard, int valSearch, int maxRow, int maxCol, int minRow, int minCol)
    {
        for (int i = minRow; i < maxRow; i++)
        {
            for (int j = minCol; j < maxCol; j++)
            {
                if (gameBoard[i][j] == valSearch)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * For manually entered game boards, determine whether the numbers in a column
     * are unique. If they are not unique, the method will return false. The method
     * is different from the non-manual method since the board is not an empty board
     * to begin with.
     * 
     * @param number the number being tested in the column.
     * @param col    the column being tested.
     * @return a boolean to determine whether the number is unique in the column.
     */
    private boolean numAvailableColManual(int number, int col)
    {
        int counter = 0;
        for (int j = 0; j < BOARD_SIZE; j++)
        {
            if (board[j][col] == number)
            {
                counter++;
            }
        }
        if (counter > 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * For manually entered game boards, determine whether the numbers in a row are
     * unique. If they are not unique, the method will return false. The method is
     * different from the non-manual method since the board is not an empty board to
     * begin with.
     * 
     * @param number the number being tested in the column.
     * @param row    the row being tested.
     * @return a boolean to determine whether the number is unique in the row.
     */
    private boolean numAvailableRowManual(int number, int row)
    {
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            if (board[row][i] == number)
            {
                counter++;
            }
        }
        if (counter > 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Determines whether the numbers are unique in the specified square for
     * manually entered boards. If the numbers are not unique (number is not
     * available), the method will return false.
     * 
     * @param valSearch the number being tested in the square.
     * @param maxRow    the upper row limit for the square.
     * @param maxCol    the upper column limit for the square.
     * @param minRow    the lower row limit for the square.
     * @param minCol    the lower column limit for the square.
     * @return a boolean to determine whether the number can be in the square.
     */
    private boolean numAvailableSquareManual(int valSearch, int maxRow, int maxCol, int minRow, int minCol)
    {
        int counter = 0;
        for (int i = minRow; i < maxRow; i++)
        {
            for (int j = minCol; j < maxCol; j++)
            {
                if (board[i][j] == valSearch)
                {
                    counter++;
                }
            }
        }
        if (counter > 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Configure the number of removal attempts for generating a board. The game
     * difficulty determines the number of removal attempts for the board.
     * 
     * @return an integer that determines the number of removal attempts.
     */
    private int numsRemoved()
    {
        if (gameType.compareTo("easy") == 0)
        {
            return 3;
        }
        else if (gameType.compareTo("medium") == 0)
        {
            return 7;
        }
        else
        {
            return 50;
        }
    }

    /**
     * Helper function to determine whether the number removed is valid for board
     * generation. If the removed number can lead to multiple solutions, then the
     * number is invalid. Additionally, if the removed number leads to an unsolvable
     * board, the number is invalid. If the number is valid, then remove it from the
     * board. Invalid operations will return true (the board is not unique), while
     * valid operations will return false.
     * 
     * @param gameBoard a 2D array representing the game grid.
     * @param row       the X coordinate of the number being tested.
     * @param col       the Y coordinate of the number being tested.
     * @param boardMap  a HashMap containing the original board.
     * @param tracker   a counter used to traverse the entire board.
     * @return a boolean to determine whether the removed number is valid.
     */
    private boolean boardNonUnique(int[][] gameBoard, int row, int col, Map<Point, Integer> boardMap, int tracker)
    {
        int xCoord = tracker / BOARD_SIZE;
        int yCoord = tracker % BOARD_SIZE;
        /*
         * If the tracker is at the end of the board, determine whether the completed
         * board is equivalent to the original board.
         */
        if (tracker >= BOARD_SIZE * BOARD_SIZE)
        {
            for (int i = 0; i < BOARD_SIZE; i++)
            {
                for (int j = 0; j < BOARD_SIZE; j++)
                {
                    if (gameBoard[i][j] != boardMap.get(new Point(i, j)))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        // If the current cell is not empty, move to the next cell.
        if (gameBoard[xCoord][yCoord] != EMPTY_SQUARE)
        {
            return boardNonUnique(gameBoard, row, col, boardMap, tracker + 1);
        }
        else
        {
            // Determine what the old value was at the specific coordinate.
            int oldVal = boardMap.get(new Point(xCoord, yCoord));
            if (xCoord == row && yCoord == col)
            {
                /*
                 * If the tracker is at the target coordinate, determine whether any value other
                 * than the original value can be placed in that cell.
                 */
                for (int newVal = 1; newVal <= MAX_NUM; newVal++)
                {
                    if (newVal != oldVal)
                    {
                        // If a value can be placed in the cell, try to complete the board with the new
                        // value.
                        if (numAvailableRow(gameBoard, newVal, xCoord) && numAvailableCol(gameBoard, newVal, yCoord)
                                && numAvailableSquare(gameBoard, newVal, squareRangeRow(xCoord), squareRangeCol(yCoord),
                                        squareRangeRow(xCoord) - SQUARE_SIZE, squareRangeCol(yCoord) - SQUARE_SIZE))
                        {
                            gameBoard[xCoord][yCoord] = newVal;
                            if (populateRemainingBoard(gameBoard, EMPTY_SQUARE))
                            {
                                /*
                                 * If the board can be completed with the new value, determine if it matches the
                                 * original board. At this point, the board is completed, so the tracker will
                                 * continue moving forward until it reaches the end of the grid and then compare
                                 * the board to the original grid.
                                 */
                                return boardNonUnique(gameBoard, row, col, boardMap, tracker + 1);
                            }
                        }
                    }
                }
                /*
                 * If no value can placed in the cell, the board is unique and the cell can be
                 * removed.
                 */
                return false;
            }
            /*
             * If it is not the target coordinate, move onto the next cell until the target
             * coordinate is reached.
             */
            else
            {
                return boardNonUnique(gameBoard, row, col, boardMap, tracker + 1);
            }
        }
    }
}
