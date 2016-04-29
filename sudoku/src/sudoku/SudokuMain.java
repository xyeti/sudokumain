
package sudoku;

public class SudokuMain {
	public static int sudokuSize = 9;
	
	public static void main(String[] args)
	{
		
		puzzle sudoku = new puzzle();
		
		int x = sudoku.solvePuzzle(args);
		
		if (x >0)
		{
			System.out.println("Sudoku is solved");
		}
		else
			System.out.println("Sudoku is not yet solved!!!");

	}

}
