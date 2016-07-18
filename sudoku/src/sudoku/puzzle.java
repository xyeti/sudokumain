package sudoku;

import java.util.ArrayList;

public class puzzle {
	final int sudokuSize =9;
	final int maxVal = (1<<sudokuSize)-1;
	Double xx = Math.sqrt(sudokuSize);
	final int boxSize = xx.intValue();
	
	final int fullOptions= 511;
	boolean recentUpdates= true;
	boolean updateBox = false;
	boolean updateRow = false;
	boolean updateCol = false;
	boolean puzzleStatus = true;	
	
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
		//printSudoku(Squares);
		int round =0;
		
		while(puzzleStatus != false)
		{
			// 1. Check boxes, rows and columns
			boolean elC = eliminateChoices();
			System.out.println("After elimination: round"+round);
			// 1.5 Check for solution complete
			if (checkSolution())
				return 1;
			
			// 2. Check for Unique value present in box, row, column
			boolean uV = findUniqueValue();
			System.out.println("After Unique value elimination:round"+round);
			if (checkSolution())
				return 1;
					
			// 3 eliminate twins
			boolean fTw = findTwins();
			System.out.println("Value after Twins elimination:round"+round);
			//printSudoku(Squares);
			if (checkSolution())
				return 1;
			
			//4 eliminate triplets
			boolean fTr = findTrips();
			System.out.println("Value after Trips elimination:round"+round);
			//printSudoku(Squares);
			if (checkSolution())
				return 1;

			//Rinse and Repeat		
			round++;
					
			puzzleStatus = (fTr|fTw|uV|elC); 
			
		}//repeat the process till Solution is Solved
				
		System.out.println("Couldnt solve the puzzle");
		return 0;
	}
	private boolean findTrips()
	{
		boolean ret = false;
		boolean ret1 = false;
		boolean ret2 = false;
		recentUpdates = true;
		int count =0;
		
		while(recentUpdates !=false)
		{
			recentUpdates = findBoxTrips();
			ret = eliminateChoices();
			
			recentUpdates = findRowColTrips(0);
			ret1 = eliminateChoices();
			
			recentUpdates = findRowColTrips(1);
			ret2 = eliminateChoices();
			
			recentUpdates = ret|ret1|ret2;
			ret = ret1 = ret2 = false;
			count++;
			
		} // checking for anything changed
		
		if (count>0)
			return true;
		else
			return false;
	}
	
	private boolean findBoxTrips()
	{
		boolean ret = false;
		
		for (int boxnum=0;boxnum<sudokuSize;boxnum++)
		{
			ArrayList<Integer> tripArr = new ArrayList<>();
			
			int column = (boxnum%boxSize)*boxSize;
			int row =(boxnum/boxSize)*boxSize;
	
			//System.out.println("Checking for twin in Box #" + boxnum);
			
			for(int i = row; i< (row+boxSize); i++)
			{
				for(int j = column; j< (column+boxSize); j++)
				{
					if (Squares[i][j].isFinal == false && (Squares[i][j].getOptions() == 3))
					{
						tripArr.add(Squares[i][j].getValue());
					}
				}// col. 
			}// get the box trip 
			
			for (int i=0; i<tripArr.size();i++)
			{
				for (int j=i+1; j<tripArr.size();j++)
				{
					int xxx = tripArr.get(i);
					int yyy = tripArr.get(j);
					if (xxx == yyy)
					{
						int twin = findDoubleBox(xxx,boxnum);
						if (twin>0)
						{
							eliminateValFromBox(xxx,twin,boxnum);
							//tripArr.remove(Integer.valueOf(xxx));
							//tripArr.remove(Integer.valueOf(yyy));
							ret = true;
						}
						
					}//found a double
				}//j+1
				
			}//i
			
		}//Running it through for all boxes
		
		return ret;
	}
	
	private boolean findRowColTrips (int roC)
	{
		boolean ret = false;
		for(int i = 0; i< sudokuSize; i++)
		{
			ArrayList<Integer> tripArr = new ArrayList<>();
			for(int j = 0; j< sudokuSize; j++)
			{
				if (roC == 0)
				{
					if (Squares[i][j].isFinal == false && (Squares[i][j].getOptions() == 3))
					{
						tripArr.add(Squares[i][j].getValue());
					}
				}
				else 
				{
					if (Squares[j][i].isFinal == false && (Squares[j][i].getOptions() == 3))
					{
						tripArr.add(Squares[j][i].getValue());
					}
				}
			}// col. 
			
			for (int ii=0; ii<tripArr.size();ii++)
			{
				for (int jj=ii+1; jj<tripArr.size();jj++)
				{
					int xxx = tripArr.get(ii);
					int yyy = tripArr.get(jj);
					if (xxx == yyy)
					{
						int twin = findDoubleRorC(xxx,i,roC);
						if (twin>0)
						{
							eliminateValFromRoC(xxx,twin,roC,i);
							ret = true;
						}
					}//FoundDouble
				}//j+1
			}//i
			
			
		}//Row
		
		return ret;
	}

	private int findDoubleRorC(int val, int rC, int roC)
	{
		for(int j = 0; j< sudokuSize; j++)
		{
			if (roC == 0)
			{
				if (Squares[rC][j].isFinal == false && (Squares[rC][j].getValue() != val))
				{
					int q = (Squares[rC][j].getValue() & val);
					if (q == Squares[rC][j].getValue())
					{
						return q;
					}
				}
			}
			else
			{
				if (Squares[j][rC].isFinal == false && (Squares[j][rC].getValue() != val))
				{
					int q = (Squares[j][rC].getValue() & val);
					
					if (q == Squares[j][rC].getValue())
						return q;
				}
			}
		}// J
			
		return 0;
	}
	
	private int findDoubleBox(int trip, int box)
	{
		int column = (box%boxSize)*boxSize;
		int row =(box/boxSize)*boxSize;
		
		for(int i = row; i< (row+boxSize); i++)
		{
			for(int j = column; j< (column+boxSize); j++)
			{
				if (Squares[i][j].isFinal == false && (Squares[i][j].getValue() != trip))
				{
					int q = (Squares[i][j].getValue() & trip);
					if (q == Squares[i][j].getValue())
					{
						return q;
					}
				}//find a double to match a triple
			}// J
		}// get the box Double
		return 0;
	}
	
	private boolean findTwins()
	{
		boolean ret = false;
		boolean ret1 = false;
		boolean ret2 = false;
		recentUpdates = true;
		int count =0;
		
		while(recentUpdates !=false)
		{
			recentUpdates = findBoxTwins();
			ret = eliminateChoices();
			
			recentUpdates = findRowColTwins(0);
			ret1 = eliminateChoices();
			
			recentUpdates = findRowColTwins(1);
			ret2 = eliminateChoices();
			
			recentUpdates = ret|ret1|ret2;
			ret = ret1 = ret2 = false;
			count++;
			
		} // checking for anything changed
		
		if (count >0)
			return true;
		else
			return false;
	}
	
	private boolean findRowColTwins (int roC)
	{
		boolean ret = false;
		for(int i = 0; i< sudokuSize; i++)
		{
			ArrayList<Integer> twinArr = new ArrayList<>();
			for(int j = 0; j< sudokuSize; j++)
			{
				if (roC == 0)
				{
					if (Squares[i][j].isFinal == false && (Squares[i][j].getOptions() == 2))
					{
						twinArr.add(Squares[i][j].getValue());
					}
				}
				else 
				{
					if (Squares[j][i].isFinal == false && (Squares[j][i].getOptions() == 2))
					{
						twinArr.add(Squares[j][i].getValue());
					}
				}
			}// col. 
			
			for (int ii=0; ii<twinArr.size();ii++)
			{
				for (int jj=ii+1; jj<twinArr.size();jj++)
				{
					int xxx = twinArr.get(ii);
					int yyy = twinArr.get(jj);
					if (xxx == yyy)
					{
						if (roC == 0)
							eliminateValFromRoC(xxx,0,0,i);
						else
							eliminateValFromRoC(xxx,0,1,i);
						
					//	twinArr.remove(Integer.valueOf(xxx));
					//	twinArr.remove(Integer.valueOf(yyy));
						ret = true;
					}
				}//j+1
			}//i
			
			
		}//Row
		
		return ret;
	}
	
	private boolean findBoxTwins()
	{
		boolean ret = false;
		
		for (int boxnum=0;boxnum<sudokuSize;boxnum++)
		{
			ArrayList<Integer> twinArr = new ArrayList<>();
			//ArrayList<Integer> twinArr = new ArrayList<>();
			
			int column = (boxnum%boxSize)*boxSize;
			int row =(boxnum/boxSize)*boxSize;
	
	//		System.out.println("Checking for twin in Box #" + boxnum);
			
			for(int i = row; i< (row+boxSize); i++)
			{
				for(int j = column; j< (column+boxSize); j++)
				{
					if (Squares[i][j].isFinal == false && (Squares[i][j].getOptions() == 2))
					{
						twinArr.add(Squares[i][j].getValue());
					}
				}// col. 
			}// get the box twin 
			
			for (int i=0; i<twinArr.size();i++)
			{
				for (int j=i+1; j<twinArr.size();j++)
				{
					int xxx = twinArr.get(i);
					int yyy = twinArr.get(j);
					if (xxx == yyy)
					{
						eliminateValFromBox(xxx,0, boxnum);
						twinArr.remove(Integer.valueOf(xxx));
						twinArr.remove(Integer.valueOf(yyy));
						ret = true;
					}
				}//j+1
				
			}//i
			
		}//Running it through for all boxes
		
		return ret;
	}
	
	private void eliminateValFromRoC(int val, int val1, int roC, int rC)
	{
			for(int j = 0; j< sudokuSize; j++)
			{
				if (roC == 0)
				{
					if (Squares[rC][j].isFinal == false && 
							((Squares[rC][j].getValue() != val)&&
									(Squares[rC][j].getValue() != val1)))
					{
						int q = (Squares[rC][j].getValue() & (~val));
						
						Squares[rC][j].setOptions(getValuesCount(q));
						if (getValuesCount(q)== 1)
						{
							Squares[rC][j].setIsFinal(true);
							Squares[rC][j].setValue(getValueFromBits(q));
						}
						else
							Squares[rC][j].setValue(q);
					}
				}
				else
				{
					if (Squares[j][rC].isFinal == false && 
							((Squares[j][rC].getValue() != val)&&
									(Squares[j][rC].getValue() != val1)))
					{
						int q = (Squares[j][rC].getValue() & (~val));
						
						Squares[j][rC].setOptions(getValuesCount(q));
						if (getValuesCount(q)== 1)
						{
							Squares[j][rC].setIsFinal(true);
							Squares[j][rC].setValue(getValueFromBits(q));
						}
						else
							Squares[j][rC].setValue(q);
					}
				}
			}// J
				
	} //eliminate Twin from Row or Col
	
	private void eliminateValFromBox(int val,int val1, int box)
	{
		int column = (box%boxSize)*boxSize;
		int row =(box/boxSize)*boxSize;
		
		for(int i = row; i< (row+boxSize); i++)
		{
			for(int j = column; j< (column+boxSize); j++)
			{
				if (Squares[i][j].isFinal == false && 
						((Squares[i][j].getValue() != val)&&
								((Squares[i][j].getValue() != val1))))
				{
					int q = (Squares[i][j].getValue() & (~val));
					Squares[i][j].setValue(q);
					Squares[i][j].setOptions(getValuesCount(q));
				}
				
			}// J
		}// get the box twin
		
	}//eliminate Twin box
	
	private boolean checkSolution()
	{
		int x =0;
		int y =0;
		int rowCount =0;
		int colCount =0;

		for(int i=0;i<sudokuSize;i++)
		{
			for (int j=0;j<sudokuSize;j++)
			{
				x |= 1 << (Squares[i][j].getValue()-1);
				y |= 1 << (Squares[j][i].getValue()-1);
			}
			if (x ==maxVal)
			{
				rowCount++;
				
			}
			if  (y==maxVal)
			{
				colCount++;
			}
			x=0;
			y=0;

		} //for loop
		
		if ((rowCount == sudokuSize)&&(colCount == sudokuSize))
		{
			printSudoku(Squares);
			return true;
		}
		else
			return false;
		
	}
	
	
	private boolean findUniqueValue()
	{
		boolean rowF = false;
		boolean colF = false;
		boolean boxF = false;
		recentUpdates = true;
		int count=0;
		
		while (recentUpdates != false)
		{
			recentUpdates = findUniqueValueRoC(0);
			//printSudoku(Squares);
			rowF = eliminateChoices();
			//printSudoku(Squares);
					
			recentUpdates = findUniqueValueRoC(1);
			//printSudoku(Squares);
			colF = eliminateChoices();
			//printSudoku(Squares);
			
			recentUpdates = findUniqueValueBox();
			//printSudoku(Squares);
			boxF = eliminateChoices();
			//printSudoku(Squares);
			
			recentUpdates = (rowF | colF | boxF) ;
			rowF = false;
			colF = false;
			boxF = false;
			count++;
		}
		if (count>0)
			return true;
		else
			return false;
	}
		
	private boolean findUniqueValueBox() {
		int val=0;
		boolean uniqueFound = true;
		int count=0;
		
		
		while(uniqueFound != false)
		{
			uniqueFound = false;
			for (int i=0;i<sudokuSize;i++)
			{
				int[] arr = { 0, 0 , 0 , 0, 0, 0 , 0 , 0, 0};
				int column = (i %boxSize)*boxSize;
				int row =(i /boxSize)*boxSize;
				
				for(int j = row; j< (row+boxSize); j++)
				{
					for(int k = column; k< (column+boxSize); k++)
					{
						if (Squares[j][k].isFinal != true)
						{
							val = Squares[j][k].getValue();
							arr = getArrCount(arr, val);
						} // counting options in the box
					}
				}// Get elements already present in Box 
				
				for (int k=0;k<sudokuSize;k++)
				{
					if (arr[k] == 1)
					{
						for(int q = row; q< (row+boxSize); q++)
						{
							for(int r = column; r< (column+boxSize); r++)
							{
							    if ( (Squares[q][r].isFinal != true) && ((Squares[q][r].getValue() & (1<<k)) > 0))
								{
									Squares[q][r].setIsFinal(true);
									Squares[q][r].setValue(k+1);
									Squares[q][r].setOptions(0);
									uniqueFound = true;
									count++;
								} // finding the element in the row and setting it to true
						
							} // looking for unique element in the box
						
						}// unique value in the box loop
						
					} // checking arr w/ elements in box	
							
				} // updating the value in Box
			
			} // Repeat for all boxes
		} // Repeat till all unique boxes are filled
		
		if (count >0)
		{
			System.out.println("Number of uniques found in box= "+count);
			return true;
		}
		else
		{
			return false;
		}
	
	} //findUniqueValueBox

	public boolean findUniqueValueRoC(int rorc)
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
								if ((Squares[i][l].isFinal != true) && ( (Squares[i][l].getValue() & (1<<k)) > 0))
								{
									Squares[i][l].setIsFinal(true);
									Squares[i][l].setValue(k+1);
									Squares[i][l].setOptions(0);
									uniqueFound = true;
									count++;
								} // finding the element in the row and setting it to true
							} //row element finding
							else
							{
								if ((Squares[l][i].isFinal != true) &&((Squares[l][i].getValue() & (1<<k)) > 0))
								{
									Squares[l][i].setIsFinal(true);
									Squares[l][i].setValue(k+1);
									Squares[l][i].setOptions(0);
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
	
	
	
	public boolean eliminateChoices()
	{
		int eliminated = 0;
			
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
			eliminated++;
		}// Loop to build possibility matrix
		
		if (eliminated > 1)
		{
			System.out.println("Eliminated "+eliminated+ "choices");
			return true;
		}
		
		return false;
	} //eliminate choices
	
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
