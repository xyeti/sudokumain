package sudoku;


public class puzzle {
	final int sudokuSize =9;
	Double xx = Math.sqrt(sudokuSize);
	final int boxSize = xx.intValue();
	
	final int fullOptions= 511;
	boolean recentUpdates= true;
	boolean updateBox = false;
	boolean updateRow = false;
	boolean updateCol = false;
		
	
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
		
		System.out.println("Original puzzle");
		printSudoku(Squares);
		
		
		// 1. Check boxes, rows and columns
		eliminateChoices();
		System.out.println("After First elimination");
		printSudoku(Squares);
		
		// 2. Check for Unique value present in box, row, column
		recentUpdates = findUniqueValue(0);
		eliminateChoices();
		recentUpdates = findUniqueValue(1);
		eliminateChoices();
		System.out.println("After Unique value elimination");
		printSudoku(Squares);
		
/*		
		System.out.println("2 Count follows");
		for (int i=0;i<sudokuSize;i++)
		{
			for (int j=0;j<sudokuSize;j++)
			{
				if (Squares[i][j].isFinal == false && (Squares[i][j].getOptions() == 2))
				{
					System.out.println("row "+i+ "Col "+j +"="+Squares[i][j].getValue());
				}
			}
		}
*/
		
		//System.out.println("After mask in a box,row,column");
		//printSudoku(Squares);
		return 0;
	}
	
	public boolean findUniqueValue(int rorc)
	{
		int val=0;
		boolean uniqueFound = true;
		int count=0;
		
		while (uniqueFound != false)
		{
			uniqueFound = false;
			for(int i=0; i < sudokuSize;i++)
			{
				int[] arr = { 0, 0 , 0 , 0, 0, 0 , 0 , 0, 0};
				
				for (int j=0;j<sudokuSize;j++)
				{
					if (rorc == 0)
					{
						if (Squares[i][j].isFinal != true)
						{
							val = Squares[i][j].getValue();
							arr = getArrCount(arr, val);
						} // counting options in the row
					
					} //row pop
					else
					{
						if (Squares[j][i].isFinal != true)
						{
							val = Squares[j][i].getValue();
							arr = getArrCount(arr, val);
						} // counting options in the row
					
					}//col pop
				} // row
				
				for (int k=0;k<sudokuSize;k++)
				{
					if (arr[k] == 1)
					{
						for(int l=0;l<sudokuSize;l++)
						{
							if(rorc == 0)
							{
								if ( (Squares[i][l].getValue() & (1<<k)) > 0)
								{
									Squares[i][l].setIsFinal(true);
									Squares[i][l].setValue(k+1);
									uniqueFound = true;
									count++;
								} // finding the element in the row and setting it to true
							} //row element finding
							else
							{
								if ( (Squares[l][i].getValue() & (1<<k)) > 0)
								{
									Squares[l][i].setIsFinal(true);
									Squares[l][i].setValue(k+1);
									uniqueFound = true;
									count++;
								} // finding the element in the row and setting it to true
							}//column element finding
							
						} // looking for element in the row
						
					}// unique value in the row
				
				} // going over the row again
						
			}// column
		} //while uniqueFound = false

		if (count >0)
		{
			System.out.println("Number of uniques found in" + rorc + " ="+count);
			return true;
		}
		else
		{
			return false;
		}
	} // findUniqueValue
	
	
	
	public void eliminateChoices()
	{
		
		while (recentUpdates != false)
		{
			recentUpdates = false;
			//first check boxes
			for (int i=0;i<sudokuSize; i++)
			{
				updateBox |= checkBoxes(i);
			}
			
			//second rows
			for (int i=0;i<sudokuSize; i++)
			{
				updateRow |= checkRowsColumns(i,0);
				updateCol |= checkRowsColumns(i,1);
			}
			
			recentUpdates = (updateBox | updateRow | updateCol);
			updateBox = false;
			updateRow = false;
			updateCol = false;
				
		}// Loop to build possibility matrix	
	}
	
	public boolean checkRowsColumns (int num, int rorc)
	{
		//rorc: 0 - row and 1 = column
		int bitMask = 0;
						
		for (int i=0;i<sudokuSize; i++)
		{
			if (rorc == 0)
			{
				if (Squares[num][i].isFinal)
				{
					bitMask |= 1<< (Squares[num][i].getValue()-1);
		
				}
			} //row check
			else
			{
				if (Squares[i][num].isFinal)
				{
					bitMask |= 1<< (Squares[i][num].getValue()-1);

				}
			} // column check
		}//row/Column loop
		
		return fixRowColumn(bitMask,num, rorc);
	}
	
	public boolean fixRowColumn(int bitMask, int num, int rorc)
	{
		int value=0;
		int opt = 0;
		boolean isUpdate = false;
		for (int i=0;i<sudokuSize; i++)
		{
			if (rorc == 0)
			{
				if (Squares[num][i].isFinal == false)
				{
					value = Squares[num][i].getValue();
					value &= ~bitMask;
					Squares[num][i].setValue(value);
					
					opt= getValuesCount(value);
					if (opt == 1)
					{
						Squares[num][i].setIsFinal(true);
						isUpdate = true;
						
						int x = getValueFromBits(value);
						Squares[num][i].setValue(x);
						System.out.println("setting Final for row"+num+"column"+i);
					} // resolved a value	
					
					Squares[num][i].setOptions(opt);
									
				}//isFinal == false
			} //row check
			else
			{
				if (Squares[i][num].isFinal == false)
				{
					value = Squares[i][num].getValue();
					value &= ~bitMask;
					Squares[i][num].setValue(value);
					
					opt = getValuesCount(value);
					if (opt == 1)
					{
						Squares[i][num].setIsFinal(true);
						isUpdate = true;
						
						int x = getValueFromBits(value);
						Squares[i][num].setValue(x);
						
						System.out.println("setting Final for row"+i+"column"+num);
					} // resolved a value

					Squares[i][num].setOptions(opt);
								
				} // isFinal = false
			} // column check
		}//row/Column loop
		
		return isUpdate;
		
	}
	
	public boolean checkBoxes(int boxnum)
	{
		int column = (boxnum%boxSize)*boxSize;
		int row =(boxnum/boxSize)*boxSize;
		int bitMask=0;

//		System.out.println("Checking Box #" + boxnum);
		
		for(int i = row; i< (row+boxSize); i++)
		{
			for(int j = column; j< (column+boxSize); j++)
			{
				if (Squares[i][j].isFinal)
				{
					bitMask |= 1<< (Squares[i][j].getValue()-1);
				}
				//System.out.print("[" + i + "," + j+"] \t");
			}
			//System.out.println("");
		}// get the box mask
		
	 //	System.out.println(Integer.toBinaryString(bitMask));
	//	System.out.println(Integer.toBinaryString(~bitMask));
   //	System.out.println("Count="+count);
		
		return fixBox(bitMask, row,column);
	} //checkBoxes
	

	boolean fixBox (int bitMask, int row, int column)
	{
		int op=0;
		boolean isUpdate=false;
		
		for(int i = row; i< (row+boxSize); i++)
		{
			for(int j = column; j< (column+boxSize); j++)
			{
				
				if (Squares[i][j].isFinal == false)
				{
					int q= Squares[i][j].getValue();
									
					//set Bit mask and free options count				
					Squares[i][j].setValue((q &= ~bitMask));
					
					op= getValuesCount(q);
					
					if (op == 1)
					{
						Squares[i][j].setIsFinal(true);
						isUpdate = true;
						int val= getValueFromBits(q);
						Squares[i][j].setValue(val);
					
	//					System.out.println("setting Final for row"+i+"column"+j);
					}		
					Squares[i][j].setOptions(op);
					
				} //if final
					
			}//for inner
			
		}//for outer
		return isUpdate;
	}//void fixBox
	
	/*
	 * Function that will get number of options per row
	 */
	
	public int[] getArrCount(int[] arr, int val)
	{
		int cnt = 0;
		
		for(int i=0;i<sudokuSize;i++)
		{
			if((val&(1<<i)) > 0 )
			{
				arr[i]++;	
			}
		}
	
		return arr;
	} //getCount
	
	/* 
	 * Function to estimate how many options are possible per square
	 */
	int getValuesCount(int arg)
	{
		int op=0;
		
		for(int i=0;i<sudokuSize;i++)
		{
			if (((arg>>i)&1) == 1)
			{
				op++;
			}
		}
		
		return op;
	}
	
	/*
	 * Function to convert bitmask to value
	 */

	int getValueFromBits(int arg)
	{
		int op=0;
		
		for(int i=0;i<sudokuSize;i++)
		{
			if (((arg>>i)&1) == 1)
			{
				return i+1;
			}
		}
		
		return op;
	}

	/*
	 * Function to print the sudoku map
	 */
	void printSudoku(Square[][] args)
	{
		System.out.println("_________________________________________________________________________");
		for (int i=0;i<sudokuSize;i++)
		{
			System.out.print("|");
			for(int j=0;j<sudokuSize;j++)
			{
				if (args[i][j].isFinal != true)
				{
					System.out.print( "="+ args[i][j].getValue() + "\t|");
				}
				else
				{
					System.out.print( args[i][j].getValue() + "\t|");
				}
							
			}
			System.out.println("");
		}
		System.out.println("_________________________________________________________________________");
	}
	
	
	
}
