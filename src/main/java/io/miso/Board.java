package io.miso;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Board {
    private static final char WIDTH_SEPARATOR = '-';
    private static final char HEIGHT_SEPARATOR = '|';
    
    private final Colour[][] gameBoard = new Colour[9][9];
    
    public Board() {
        init();
    }
    
    private void init() {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = Colour.NONE;
            }
        }
    }

    public void clearPieceAtCoordinates(int x, int y) {
        gameBoard[x][y] = Colour.NONE;
    }
    
    public void setPieceAtCoordinates(Colour c, int x, int y) {
        gameBoard[x][y] = c;
    }
    
    public boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= gameBoard.length || y < 0 || y >= gameBoard[0].length) {
            return false;
        }
        return true;
    }

    // A method that checks if the position is free
    public boolean isPositionFree(int x, int y) {
        return gameBoard[x][y] == Colour.NONE;
    }
    
    public boolean gameOver(int currentTurn) {
        // Check if either player has won
        return validateWinConditions(Colour.BLACK, currentTurn) || validateWinConditions(Colour.WHITE, currentTurn);
    }
    
    public void printBoard() {
        int gameBoardWidthLength = gameBoard.length * 5 + 3;
        
        for (int i = 0; i < gameBoardWidthLength; i++) {
            System.out.print(WIDTH_SEPARATOR);
            
            if (i == gameBoardWidthLength - 1) {
                System.out.println();
            }
        }
        
        for (int i = 0; i < gameBoard.length; i++) {
            System.out.print(HEIGHT_SEPARATOR + " ");
            for (int j = 0; j < gameBoard[i].length; j++) {
                switch (gameBoard[i][j]) {
                    case WHITE:
                        System.out.printf("W%d.%d ", i, j);
                        break;
                    case BLACK:
                        System.out.printf("B%d.%d ", i, j);
                        break;
                    case NONE:
                    default:
                        System.out.printf("N%d.%d ", i, j);
                        break;
                }
            }
            System.out.println(HEIGHT_SEPARATOR);
        }
        
        for (int i = 0; i < gameBoardWidthLength; i++) {
            System.out.print(WIDTH_SEPARATOR);
            
            if (i == gameBoardWidthLength - 1) {
                System.out.println();
            }
        }
    }
    
    public boolean validateWinConditions(Colour colour, int currentTurn) {
        int winLength = 5;
        
        if (currentTurn < winLength) {
            return false;
        }
        
        // Check rows, columns and diagonals
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == colour) {
                    
                    // Check row
                    if (i + winLength <= gameBoard.length) {
                        boolean win = true;
                        for (int k = 0; k < winLength; k++) {
                            if (gameBoard[i + k][j] != colour) {
                                win = false;
                                break;
                            }
                        }
                        if (win) return true;
                    }
    
                    // Check column
                    if (j + winLength <= gameBoard[i].length) {
                        boolean win = true;
                        for (int k = 0; k < winLength; k++) {
                            if (gameBoard[i][j + k] != colour) {
                                win = false;
                                break;
                            }
                        }
                        if (win) return true;
                    }
    
                    // Check diagonal
                    if (i + winLength <= gameBoard.length && j + winLength <= gameBoard[i].length) {
                        boolean win = true;
                        for (int k = 0; k < winLength; k++) {
                            if (gameBoard[i + k][j + k] != colour) {
                                win = false;
                                break;
                            }
                        }
                        if (win) return true;
                    }
                }
            }
        }
    
        return false;
    }
    
    public List<Integer[]> getValidMoves() {
        List<Integer[]> validMoves = new ArrayList<>();
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == Colour.NONE) {
                    validMoves.add(new Integer[]{i, j});
                }
            }
        }
        return validMoves;
    }
    
    public Colour getPieceAtCoordinates(int x, int y) {
        return gameBoard[x][y];
    }
    
    public Colour[][] getGameBoard() {
        return gameBoard;
    }
    
    public int getBoardSize() {
        return gameBoard.length;
    }
}
