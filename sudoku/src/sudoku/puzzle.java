package sudoku;


public class puzzle {
	final int sudokuSize =9;
	Double xx = Math.sqrt(sudokuSize);
	final int boxSize = xx.intValue();
	
	final int fullOptions= 511;
		
	
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
				
				if (x >0)
				{
					Squares[i][j].setValue(x);
					Squares[i][j].isFinal=true;
					Squares[i][j].setOptions(0);
				}
				else
				{
					Squares[i][j].setValue(511);
					Squares[i][j].isFinal=false;
					Squares[i][j].setOptions(9);
				}
			}
		} // Input sudoku values
		
		printSudoku(Squares);
		
		//first check boxes
		for (int i=0;i<sudokuSize; i++)
		{
			checkBoxes(i);
		}
		
		//second rows
		for (int i=0;i<sudokuSize; i++)
		{
			checkRowsColumns(i,0);
			checkRowsColumns(i,1);
		}
	
		
		System.out.println("After mask in a box,row,column");
		printSudoku(Squares);
		return 0;
	}
	
	public void checkRowsColumns (int num, int rorc)
	{
		//rorc: 0 - row and 1 = column
		int bitMask = 0;
		int count=0;
		
		for (int i=0;i<sudokuSize; i++)
		{
			if (rorc == 0)
			{
				if (Squares[num][i].isFinal)
				{
					bitMask |= 1<< (Squares[num][i].getValue()-1);
					count++;
				}
			} //row check
			else
			{
				if (Squares[i][num].isFinal)
				{
					bitMask |= 1<< (Squares[i][num].getValue()-1);
					count++;
				}
			} // column check
		}//row/Column loop
		
		fixRowColumn(bitMask,count,num, rorc);
	
	}
	
	public void fixRowColumn(int bitMask, int count, int num, int rorc)
	{
		int value=0;
		int opt = 0;
		for (int i=0;i<sudokuSize; i++)
		{
			if (rorc == 0)
			{
				if (Squares[num][i].isFinal == false)
				{
					value = Squares[num][i].getValue();
					value ^= bitMask;
					
					opt = Squares[num][i].getOptions();
					opt -= count;
					
					Squares[num][i].setValue(value);
					Squares[num][i].setOptions(opt);
					
					if (opt == 1)
					{
						Squares[num][i].setIsFinal(true);
					} // resolved a value
				}
			} //row check
			else
			{
				if (Squares[i][num].isFinal == false)
				{
					value = Squares[i][num].getValue();
					value ^= bitMask;
					
					opt = Squares[i][num].getOptions();
					opt -= count;
					
					Squares[i][num].setValue(value);
					Squares[i][num].setOptions(opt);
					if (opt == 1)
					{
						Squares[num][i].setIsFinal(true);
					} // resolved a value

				}
			} // column check
		}//row/Column loop
		
	}
	
	public void checkBoxes(int boxnum)
	{
		int column = (boxnum%boxSize)*boxSize;
		int row =(boxnum/boxSize)*boxSize;
		int bitMask=0;
		int count = 0;

		System.out.println("Checking Box #" + boxnum);
		
		for(int i = row; i< (row+boxSize); i++)
		{
			for(int j = column; j< (column+boxSize); j++)
			{
				if (Squares[i][j].isFinal)
				{
					bitMask |= 1<< (Squares[i][j].getValue()-1);
					count++;
				}
				//System.out.print("[" + i + "," + j+"] \t");
			}
			//System.out.println("");
		}// get the box mask
		System.out.println(Integer.toBinaryString(bitMask));
		
		fixBox(bitMask, count, row,column);
	} //checkBoxes
	

	void fixBox (int bitMask, int options, int row, int column)
	{
		
		for(int i = row; i< (row+boxSize); i++)
		{
			for(int j = column; j< (column+boxSize); j++)
			{
				
				if (Squares[i][j].isFinal == false)
				{
					int q= Squares[i][j].getValue();
					int op= Squares[i][j].getOptions();
				
					//set Bit mask and free options count				
					Squares[i][j].setValue((q ^= bitMask));
							
					Squares[i][j].setOptions((op-= options));
				} //if final
					
			}//for inner
			
		}//for outer
		
	}//void fixBox

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
