package sudoku;
import java.lang.*;

public class puzzle {
	final int sudokuSize =9;
	Square [][] Squares = new Square [sudokuSize][sudokuSize];
	
	
	int solvePuzzle (String [] args)
	{
		System.out.println(args.length);
		
		for (int i=0;i< (args.length)/sudokuSize;i++)
		{
			for(int j=0;j<sudokuSize;j++)
			{
				Squares[i][j] = new Square();
				int x = Integer.parseInt(args[(i*sudokuSize)+j]);
				Squares[i][j].setValue(x);
				if (x >0)
				{
					Squares[i][j].isFinal=true;
				}
			}
		} // Input sudoku values
		
		printSudoku(Squares);
		
		return 0;
	}

	void printSudoku(Square[][] args)
	{
		System.out.println("Sudoku as follows");
		System.out.println("_________________________________________________________________________");
		for (int i=0;i<sudokuSize;i++)
		{
			System.out.print("|");
			for(int j=0;j<sudokuSize;j++)
			{
				System.out.print(args[i][j].getValue()+ "\t|");
			}
			System.out.println("");
		}
		System.out.println("_________________________________________________________________________");
	}
	
	
}
