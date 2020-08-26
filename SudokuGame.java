import java.util.Random;
import javax.swing.JOptionPane;

/**
 * SudokuGame class
 * 
 * This is the main driver for the game.
 * 
 * The class allows for user interface given the created GUI. The user can determine the difficulty
 * of the game and play the game via this class.
 */
public class SudokuGame
{
    public static void main(String[] args)
    {
        Random randGenerator = new Random();
        // User prompts to create the game based on possible game types.
        Object[] gameMode = {"Manual", "Automatic"};
        String modeMessage = "Please select the game mode: \n\n" + "Automatic creates a random board. \n"
                + "Manual allows you to create your own board.";
        String modeTitle = "Sudoku Game Mode";
        int jOptionMode = JOptionPane.showOptionDialog(null, modeMessage, modeTitle, JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, gameMode, null);
        // Create a game board automatically.
        if (jOptionMode == 1)
        {
            automaticBoardGame(randGenerator);
        }
        // Allow the user to create a manual game board.
        else if (jOptionMode == 0)
        {
            int manualEntry = JOptionPane.showConfirmDialog(null, "Please enter a valid Sudoku game board.", "Sudoku Game",
                    JOptionPane.PLAIN_MESSAGE);
            if (manualEntry == 0)
            {
                SudokuGridGenerator gridGen = new SudokuGridGenerator();
                SudokuManualBoard manualBoard = new SudokuManualBoard(new SudokuBaseGrid(gridGen));
            }
        }
    }

    /**
     * Create a Sudoku board randomly.
     * 
     * @param a random generator used to create the game board.
     */
    private static void automaticBoardGame(Random rand)
    {
        String gameDifficulty = "";
        // Create easy, medium, and hard buttons for the game difficulty.
        Object[] gameType = {"Hard", "Medium", "Easy"};
        String typeMessage = "Please select the game type:";
        String typeTitle = "Sudoku Game Type";
        int jOptionType = JOptionPane.showOptionDialog(null, typeMessage, typeTitle, JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, gameType, null);
        // Create the game based on difficulty chosen by the user.
        if (jOptionType == 2)
        {
            gameDifficulty = "easy";
            automaticBoardGenerator(rand, gameDifficulty);
        }
        if (jOptionType == 1)
        {
            gameDifficulty = "medium";
            automaticBoardGenerator(rand, gameDifficulty);
        }
        if (jOptionType == 0)
        {
            gameDifficulty = "hard";
            automaticBoardGenerator(rand, gameDifficulty);
        }
    }

    /**
     * Fill in a random board automatically for the Sudoku game.
     * 
     * @param rand  a random generator used to create the game board.
     * @param gameDifficulty  the game difficulty chosen by the user.
     */
    private static void automaticBoardGenerator(Random rand, String gameDifficulty)
    {
        // Use the SudokuGridGenerator class to create a board automatically.
        SudokuGridGenerator grid = new SudokuGridGenerator();
        grid.setGameType(gameDifficulty);
        int xCoord = rand.nextInt(SudokuGridGenerator.MAX_NUM);
        int yCoord = rand.nextInt(SudokuGridGenerator.MAX_NUM);
        int randVal = rand.nextInt(SudokuGridGenerator.MAX_NUM) + 1;
        // Populate and complete a Sudoku grid.
        grid.populateFirstBoard(xCoord, yCoord, randVal);
        grid.populateRemainingBoard(grid.getBoard(), 0);
        // Remove random numbers from the grid to create an initial Sudoku board.
        grid.finalizeBoard();
        SudokuBoardFrame boardFrame = new SudokuBoardFrame(new SudokuBaseGrid(grid));
    }

}
