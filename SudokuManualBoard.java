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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;

public class SudokuManualBoard extends JFrame
{
    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 50;

    public static final int FRAME_WIDTH = CELL_SIZE * GRID_SIZE;
    public static final int FRAME_HEIGHT = CELL_SIZE * GRID_SIZE;

    private static final Color INCORRECT_NUMBER = Color.RED;
    private static final Color CORRECT_NUMBER = Color.BLACK;
    private static final Font NUMBER_FONT = new Font("Monospaced", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Monospaced", Font.BOLD, 10);

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

    private JButton completeGame;

    private JFormattedTextField[][] sudokuCells;
    private SudokuBaseGrid baseGrid;
    private Container board;

    public SudokuManualBoard(SudokuBaseGrid baseGrid)
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
                    sudokuCells[row - 1][col].setText("");
                    sudokuCells[row - 1][col].setEditable(true);
                    sudokuCells[row - 1][col].setForeground(CORRECT_NUMBER);
                    sudokuCells[row - 1][col].setHorizontalAlignment(JTextField.CENTER);
                    sudokuCells[row - 1][col].setFont(NUMBER_FONT);
                    setFrame(row, col);
                }
            }
        }
        board.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku Manual Entry");
        setVisible(true);
    }

    private void setButtons(int row, int col)
    {
        if (row == 0 && col == 4)
        {
            completeGame = new JButton("Finish");
            completeGame.setFont(BUTTON_FONT);
            completeGame.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    if(validBoard())
                    {
                        SudokuGridGenerator grid = setBoard();
                        SudokuBoardFrame boardFrame = new SudokuBoardFrame(new SudokuBaseGrid(grid));
                        setVisible(false);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "The board is invalid. Please enter a valid Sudoku game board.", 
                                "Sudoku Game", JOptionPane.ERROR_MESSAGE);
                        for (int i = 0; i < GRID_SIZE; i++)
                        {
                            for (int j = 0; j < GRID_SIZE; j++)
                            {
                                sudokuCells[row][col].setText("");
                                sudokuCells[row][col].setEditable(true);                            
                            }
                        } 
                    }
                    
                }
            });
            board.add(completeGame);
        }
        else
        {
            board.add(new JPanel());
        }

    }

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
    
    private SudokuGridGenerator setBoard()
    {
        SudokuGridGenerator gridGen = baseGrid.getGridGen();
        int[][] gameGrid = gridGen.getBoard();
        for (int i = 0; i < GRID_SIZE; i++)
        {
            for (int j = 0; j < GRID_SIZE; j++)
            {
                String cellVal = sudokuCells[i][j].getText();
                if (cellVal.equals(""))
                {
                    cellVal = "0";
                }
                int cellValInt = Integer.parseInt(cellVal);
                gameGrid[i][j] =  cellValInt;
            }
        }
        return gridGen;
    }
    
    private boolean validBoard()
    {
        setBoard();
        if (!baseGrid.getGridGen().validGameBoardManual())
        {
            return false;
        } 
        return true;
    }


    private class SudokuNumberFormatter extends NumberFormatter
    {
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
