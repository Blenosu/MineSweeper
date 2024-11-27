package Programs;

import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
	
	// Grid Stats
    static class Grid {
        private final int rows, columns, mines;
        private final char[][] board;
        private final int[][] mineBoard;
        private final boolean[][] revealed;
        private boolean gameLost = false;
        

        // ANSI color codes    // Bold
        public static final String blackb = "\033[1;30m";  // BLACK BOLD
        public static final String redb = "\033[1;31m";    // RED BOLD
        public static final String greenb = "\033[1;32m";  // GREEN BOLD
        public static final String yellowb = "\033[1;33m"; // YELLOW BOLD
        public static final String blueb = "\033[1;34m";   // BLUE BOLD
        public static final String purpleb = "\033[1;35m"; // PURPLE BOLD
        public static final String cyanb = "\033[1;36m";   // CYAN BOLD
        public static final String whiteb = "\033[1;37m";  // WHITE BOLD
        
        public static final String reset = "\u001B[0m";
        public static final String red = "\u001B[31m";
        private static final String green = "\u001B[32m";
        private static final String yellow = "\u001B[33m";
        private static final String blue = "\u001B[34m";
        private static final String cyan = "\u001B[36m";
        private static final String white = "\u001B[37m";
        // Ignore as this is Aesthetic
        
        
        // Initialize Grid
        public Grid(int rows, int cols, int mines) {
            this.rows = rows;
            this.columns = cols;
            this.mines = mines;
            board = new char[rows][cols];
            mineBoard = new int[rows][cols];
            revealed = new boolean[rows][cols];
            
            // Run the Methods to create game
            makeBoard();
            addMines();
            calculateNum();
            
        }
        
        // Create a Board to Play on
        private void makeBoard() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    board[i][j] = ' ';
                    mineBoard[i][j] = 0;
                    revealed[i][j] = false;
                }
            }
        }
        
        
        // Adding Mines to board + Continuing the game
        private void addMines() {
            Random random = new Random();
            int placedMines = 0;
            

            while (placedMines < mines) {
                int ro = random.nextInt(rows);
                int co = random.nextInt(columns);
                
                // Place Mines with -1
                if (mineBoard[ro][co] != -1) { // Not already a mine
                    mineBoard[ro][co] = -1; // Place a mine
                    placedMines++;
                }
            }
        }
        
        
        // Calculate the number of mines around
        private void calculateNum() {
        	for (int i = 0; i < rows; i++) {
        		for (int j = 0; j < columns; j++) {
        			if (mineBoard[i][j] == -1) continue;
                        mineBoard[i][j] = countCloseMines(i, j);
                    }
        		}
        	}
        
        
        // Count the Mines around cell
        private int countCloseMines(int row, int col) {
            int count = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int r = row + i, c = col + j;
                    if (r >= 0 && r < rows && c >= 0 && c < columns && mineBoard[r][c] == -1) {
                        count++;
                    }
                }
            }
            return count;
        }

        
        // Checks if Cell contains a Mine if not reveals it
        public void revealCell(int row, int col) {
        	
        	// default if nothing should happen // or errors
        	if (row < 0 || row >= rows || col < 0 || col >= columns || revealed[row][col]) return;

        	revealed[row][col] = true;
        		
        		// Mine is present if the row and column is considered -1
                if (mineBoard[row][col] == -1) {
                    gameLost = true;
                    return;
                }

                board[row][col] = (mineBoard[row][col] == 0) ? ' ' : (char) ('0' + mineBoard[row][col]);
                
                // Mine is not present oif the row and column is considered 0
                if (mineBoard[row][col] == 0) {
                	
                    // Show Close Cells near revealed cell
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            revealCell(row + i, col + j); // Recursion
                        }
                    }
                }
            }
        
        
        // Game has been Lost
        public boolean LGame () {
            return gameLost;
        }
        
        
        // Game has been Won
        public boolean WGame () {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (!revealed[i][j] && mineBoard[i][j] != -1) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        
        
        // Show the Board
        public void showBoard() {
            System.out.print(yellowb + "[ 0 ]" + reset); // Initiate Outside Number of Coordinates
            for (int i = 1; i < columns; i++) {
                System.out.print(yellowb + "[ " + i + " ]" + reset);
            }
            System.out.println();

            for (int i = 1; i < rows; i++) {
                System.out.print(yellowb + "[ " + i + " ]" + reset);
                for (int j = 1; j < columns; j++) {
                	
                    if (revealed[i][j]) {
                    	
                        if (mineBoard[i][j] == -1) {
                            System.out.print(red + "[ * ]" + reset); // Active Mine

                        } else if (mineBoard[i][j] == 0) {
                            System.out.print("[ _ ]"); // Empty cells
                            
                        } else {
                            System.out.print(getColorForNumber(mineBoard[i][j]) + "[ " + mineBoard[i][j] + " ]" + reset);
                        }
                        
                    } else {
                        System.out.print(blackb +"[ _ ]" + reset); // Hidden cells
                    }
                    
                }
                System.out.println();
            }
        }
        
        
        
        private String getColorForNumber(int number) {
            switch (number) {
                case 1: return cyan;
                case 2: return green;
                case 3: return red;
                case 4: return purpleb;
                case 5: return yellow;
                case 6: return blue;
                default: return white;
            }
        }
    }
    
    
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Starting MineSweeper ( 10 Mines )! Loading... ");
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
            Grid grid = new Grid(10, 10, 10); // Rows, Columns, Mines // Rows Columns 9 = 8
            

            while (true) {
                grid.showBoard();
                System.out.println();
                System.out.print("Enter row and column to reveal (ex. (r) 0 (c) 0 ) : ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                

                grid.revealCell(row, col);
                

                if (grid.LGame()) {
                    System.out.println("Mine has been hit! Game Over!");
                    break;
                }
                

                if (grid.WGame()) {
                    System.out.println("Board has been cleared! Congrats!");
                    break;
                }
            }
            

            scanner.close();
        }
    }   