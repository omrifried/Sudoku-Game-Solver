import java.util.Random;
import javax.swing.JOptionPane;

public class SudokuGame
{
    public static void main(String[] args)
    {
        Random randGenerator = new Random();
        Object[] gameMode = {"Manual", "Automatic"};
        String modeMessage = "Please select the game mode: \n\n" + "Automatic creates a random board. \n"
                + "Manual allows you to create your own board.";
        String modeTitle = "Sudoku Game Mode";
        int jOptionMode = JOptionPane.showOptionDialog(null, modeMessage, modeTitle, JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, gameMode, null);
        if (jOptionMode == 1)
        {
            automaticBoardGame(randGenerator);
        }
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

    private static void automaticBoardGame(Random rand)
    {
        String gameDifficulty = "";
        Object[] gameType = {"Hard", "Medium", "Easy"};
        String typeMessage = "Please select the game type:";
        String typeTitle = "Sudoku Game Type";
        int jOptionType = JOptionPane.showOptionDialog(null, typeMessage, typeTitle, JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, gameType, null);
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

    private static void automaticBoardGenerator(Random rand, String gameDifficulty)
    {
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
