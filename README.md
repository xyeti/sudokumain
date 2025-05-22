# Java Sudoku Solver

A Java program that solves Sudoku puzzles using a backtracking algorithm and various elimination techniques.

## How to Compile and Run

To compile and run this Sudoku solver, you'll need a Java Development Kit (JDK) installed on your system.

**Compilation:**

Open your terminal or command prompt, navigate to the `sudoku/src` directory, and run the following command to compile the Java source files:

```bash
javac sudoku/*.java
```

This will create `.class` files in the `sudoku` directory.

**Running the Program:**

After successful compilation, navigate to the `sudoku/src` directory (if you're not already there) and run the program using the following command:

```bash
java sudoku.SudokuMain <puzzle_input>
```

Replace `<puzzle_input>` with the Sudoku puzzle you want to solve.

## Input Format

The Sudoku puzzle is provided as a series of 81 numbers as command-line arguments. These numbers represent the Sudoku grid row by row, from left to right. Use `0` to represent an empty square.

**Example:**

For the following puzzle:

```
5 3 0 0 7 0 0 0 0
6 0 0 1 9 5 0 0 0
0 9 8 0 0 0 0 6 0
8 0 0 0 6 0 0 0 3
4 0 0 8 0 3 0 0 1
7 0 0 0 2 0 0 0 6
0 6 0 0 0 0 2 8 0
0 0 0 4 1 9 0 0 5
0 0 0 0 8 0 0 7 9
```

The command-line input would be:

```
java sudoku.SudokuMain 5 3 0 0 7 0 0 0 0 6 0 0 1 9 5 0 0 0 0 9 8 0 0 0 0 6 0 8 0 0 0 6 0 0 0 3 4 0 0 8 0 3 0 0 1 7 0 0 0 2 0 0 0 6 0 6 0 0 0 0 2 8 0 0 0 0 4 1 9 0 0 5 0 0 0 0 8 0 0 7 9
```

## Solving Algorithm

The program employs a backtracking algorithm along with several Sudoku-specific solving techniques to find the solution. These techniques include:

*   **Elimination:** Removing impossible candidates for a square based on the values in its row, column, and 3x3 box.
*   **Hidden Singles:** Identifying squares where a specific number is the only possible candidate within its row, column, or 3x3 box.
*   **Naked Pairs/Triples:** Finding sets of two (or three) squares in the same row, column, or box that contain the exact same two (or three) candidates, allowing the removal of those candidates from other squares in that unit.

The solver iteratively applies these techniques. If it reaches a point where no more direct deductions can be made, it will resort to backtracking (making a guess and attempting to solve from there, undoing the guess if it leads to a contradiction).

## Future Improvements

*   **Graphical User Interface (GUI):** Implement a visual interface for entering puzzles and viewing the solving process.
*   **Alternative Input Methods:** Support for reading puzzles from files (e.g., CSV, plain text).
*   **Advanced Solving Techniques:** Incorporate more sophisticated Sudoku solving strategies for harder puzzles.
*   **Difficulty Analysis:** Add functionality to estimate the difficulty of a given puzzle.
