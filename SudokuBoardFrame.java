import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

/**
 * This class creates the actual GUI that the game will be played on.
 * 
 * The class extends JFrame and creates an editable GUI.
 */
public class SudokuBoardFrame extends JFrame
{
    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 50;

    public static final int FRAME_WIDTH = CELL_SIZE * GRID_SIZE;
    public static final int FRAME_HEIGHT = CELL_SIZE * GRID_SIZE;

    private static final Color INCORRECT_NUMBER = Color.RED;
    private static final Color CORRECT_NUMBER = Color.BLACK;
    private static final Font NUMBER_FONT = new Font("Monospaced", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Monospaced", Font.BOLD, 10);

    // Create the GUI.
    private static final Border MIDDLE_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY);
    private static final Border RIGHT_BOTTOM_EDGE_BORDER = BorderFactory.createMatteBorder(0, 0, 5, 5, Color.BLACK);
    private static final Border BOTTOM_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 5, 0, Color.BLACK),
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
    private static final Border RIGHT_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK),
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    private static final Border LEFT_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, Color.BLACK),
            BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
    private static final Border LEFT_BOTTOM_EDGE_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 5, 0, Color.BLACK),
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
    private static final Border TOP_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(5, 0, 0, 0, Color.BLACK),
            BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
    private static final Border LEFT_TOP_EDGE_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(5, 5, 0, 0, Color.BLACK),
            BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
    private static final Border RIGHT_TOP_EDGE_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(5, 0, 0, 5, Color.BLACK),
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

    private JButton resetGame;
    private JButton solveGame;
    private JButton checkSolution;

    private JFormattedTextField[][] sudokuCells;
    private SudokuBaseGrid baseGrid;
    private Container board;

    /**
     * Create base GUI and initialize all elements on the GUI
     * 
     * @param the baseGrid that will be filled out
     */
    public SudokuBoardFrame(SudokuBaseGrid baseGrid)
    {
        this.baseGrid = baseGrid;
        sudokuCells = new JFormattedTextField[GRID_SIZE][GRID_SIZE];
        board = getContentPane();
        board.setLayout(new GridLayout(GRID_SIZE + 1, GRID_SIZE));

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new SudokuNumberFormatter(integerFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(SudokuGridGenerator.MIN_NUM);
        numberFormatter.setMaximum(SudokuGridGenerator.MAX_NUM);

        // Set the first row to buttons and the remaining squares as cells.
        for (int row = 0; row <= GRID_SIZE; row++)
        {
            for (int col = 0; col < GRID_SIZE; col++)
            {
                if(row == 0)
                {
                    setButtons(row, col);
                }
                else
                {
                    sudokuCells[row - 1][col] = new JFormattedTextField(numberFormatter);
                    board.add(sudokuCells[row - 1][col]);
                    setField(row, col);
                    setFrame(row, col);
                }
            }
        }
        board.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setVisible(true);
    }

    /**
     * Set the buttons for the GUI at the top of the GUI and make them clickable.
     * 
     * @param row  the row index
     * @param col  the column index
     */
    private void setButtons(int row, int col)
    {
        // Create a reset button that clears the board.
        if (row == 0 && col == 0)
        {
            resetGame = new JButton("Reset");
            resetGame.setFont(BUTTON_FONT);
            resetGame.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    for(int i = 1; i <= GRID_SIZE; i++)
                    {
                        for(int j = 0; j < GRID_SIZE; j++)
                        {
                            setField(i, j);
                            sudokuCells[i - 1][j].setForeground(CORRECT_NUMBER);
                        }
                    }
                }
            });
            board.add(resetGame);
        }
        // Create a solver button that solves the game using backtracking in SudokuGridGenerator.
        else if (row == 0 && col == 4)
        {
            solveGame = new JButton("Solve");
            solveGame.setFont(BUTTON_FONT);
            solveGame.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    for(int i = 0; i < GRID_SIZE; i++)
                    {
                        for(int j = 0; j < GRID_SIZE; j++)
                        {
                            sudokuCells[i][j].setForeground(CORRECT_NUMBER);
                            String cellVal = String.valueOf(baseGrid.getSolvedGrid().getBoard()[i][j]);
                            sudokuCells[i][j].setText(cellVal);
//                            solveBoardFrame(baseGrid.getGridGen(), baseGrid.getBaseGrid(), 0);
                        }
                    }
                }
            });
            board.add(solveGame);
        }
        // Create a check button that determines whether the user entered the correct number in their solution.
        else if (row == 0 && col == 8)
        {
            checkSolution = new JButton("Check");
            checkSolution.setFont(BUTTON_FONT);
            checkSolution.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    for(int i = 0; i < GRID_SIZE; i++)
                    {
                        for(int j = 0; j < GRID_SIZE; j++)
                        {
                            if(!sudokuCells[i][j].getText().equals(""))
                            {
                                String cellVal = String.valueOf(baseGrid.getSolvedGrid().getBoard()[i][j]);
                                if(!sudokuCells[i][j].getText().equals(cellVal))
                                {
                                    sudokuCells[i][j].setForeground(INCORRECT_NUMBER);
                                }
                            }
                        }
                    }
                }
            });
            board.add(checkSolution);
        }
        else
        {
            board.add(new JPanel());
        }

    }

    /**
     * Set the actual entry fields so that they can be editable - these are the tiles 
     * in the Sudoku game.
     * 
     * @param row  the row index
     * @param col  the column index
     */
    private void setField(int row, int col)
    {
        if (baseGrid.getBaseGrid()[row - 1][col] == SudokuGridGenerator.EMPTY_SQUARE)
        {
            sudokuCells[row - 1][col].setText("");
            sudokuCells[row - 1][col].setEditable(true);
        }
        else
        {
            sudokuCells[row - 1][col].setText(baseGrid.getBaseGrid()[row - 1][col] + "");
            sudokuCells[row - 1][col].setEditable(false);
            sudokuCells[row - 1][col].setForeground(CORRECT_NUMBER);
        }
        sudokuCells[row - 1][col].setHorizontalAlignment(JTextField.CENTER);
        sudokuCells[row - 1][col].setFont(NUMBER_FONT);
    }

    /**
     * Set the frame of the GUI.
     * 
     * @param row  the row index
     * @param col  the column index
     */
    private void setFrame(int row, int col)
    {
        // Row values are shifted to start at 1 since row 0 contains the buttons.
        if (row == 1 && col == 0)
        {
            sudokuCells[row - 1][col].setBorder(LEFT_TOP_EDGE_BORDER);
        }
        else if (row == 1 && (col + 1) % 3 != 0)
        {
            sudokuCells[row - 1][col].setBorder(TOP_BORDER);
        }
        else if (row == 1 && (col + 1) % 3 == 0)
        {
            sudokuCells[row - 1][col].setBorder(RIGHT_TOP_EDGE_BORDER);
        }
        else if (col == 0 && row % 3 != 0)
        {
            sudokuCells[row - 1][col].setBorder(LEFT_BORDER);
        }
        else if (col == 0 && row % 3 == 0)
        {
            sudokuCells[row - 1][col].setBorder(LEFT_BOTTOM_EDGE_BORDER);
        }
        else if (row % 3 == 0 && (col + 1) % 3 == 0)
        {
            sudokuCells[row - 1][col].setBorder(RIGHT_BOTTOM_EDGE_BORDER);
        }
        else if (row % 3 == 0)
        {
            sudokuCells[row - 1][col].setBorder(BOTTOM_BORDER);
        }
        else if ((col + 1) % 3 == 0)
        {
            sudokuCells[row - 1][col].setBorder(RIGHT_BORDER);
        }
        else
        {
            sudokuCells[row - 1][col].setBorder(MIDDLE_BORDER);
        }
    }
    
//    private boolean solveBoardFrame(SudokuGridGenerator grid, int[][] board, int tracker)
//    {
//        int xCoord = tracker / GRID_SIZE;
//        int yCoord = tracker % GRID_SIZE;
////        System.out.println(board[xCoord][yCoord]);
//        // If all cells have been visited and there were no errors, return true.
//        if (tracker >= GRID_SIZE * GRID_SIZE)
//        {
//            return true;
//        }
//        // If the current cell is not empty, move to the next cell.
//        if (board[xCoord][yCoord] != SudokuGridGenerator.EMPTY_SQUARE)
//        {
//            return solveBoardFrame(grid, board, tracker + 1);
//        }  
//        else
//        {
//            // Attempt to put any value between 1 and 9 in the cell.
//            for (int newVal = 1; newVal <= SudokuGridGenerator.MAX_NUM; newVal++)
//            {
//                // If the current value satisfies all Sudoku preconditions, place it in the cell.
//                if (grid.validGameBoard(xCoord, yCoord, newVal, board))
//                {
//                    board[xCoord][yCoord] = newVal;
//                    sudokuCells[xCoord][yCoord].setText(String.valueOf(newVal));
//                    /*
//                     * Determine whether the value placed in the cell allows for the board to be
//                     * completed. If the board can be completed (returns true), then complete the
//                     * board and return true for the function.
//                     */
//                    if (solveBoardFrame(grid, board, tracker + 1))
//                    {
//                        return true;
//                    }
//                    // If the board cannot be completed with the value, set cell to empty.
//                    else
//                    {
//                        board[xCoord][yCoord] = SudokuGridGenerator.EMPTY_SQUARE;
//                        sudokuCells[xCoord][yCoord].setText(String.valueOf(SudokuGridGenerator.EMPTY_SQUARE));
//                    }
//                }
//            }
//        }
//        sudokuCells[xCoord][yCoord].setText(String.valueOf(SudokuGridGenerator.EMPTY_SQUARE));
//        return false;
//    }
    
    /**
     * Convert the user entered string values into integers.
     * 
     * @return the new board with integer values
     */
    private int[][] boardFrameInteger()
    {
        int[][] gameBoard = new int[GRID_SIZE][GRID_SIZE];
        for(int i = 0; i < GRID_SIZE; i++)
        {
            for(int j = 0; j < GRID_SIZE; j++)
            {
                String cellVal = sudokuCells[i][j].getText();
                if(cellVal.contentEquals(""))
                {
                    cellVal = "0";
                }
                int cellValInt = Integer.parseInt(cellVal);
                gameBoard[i][j] = cellValInt;
            }
        }
        return gameBoard;
    }
    
    /**
     * Override the NumberFormatter to convert the strings into integers
     * in the Sudoku cells
     */
    private class SudokuNumberFormatter extends NumberFormatter
    {
        private static final long serialVersionUID = 1L;

        public SudokuNumberFormatter(NumberFormat format)
        {
            super(format);
        }

        @Override
        public Object stringToValue(String text) throws ParseException
        {
            if ("".equals(text))
            {
                return null;
            }
            return super.stringToValue(text);
        }
    }

}
