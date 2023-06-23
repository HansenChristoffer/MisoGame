package io.miso.core;

import io.miso.entities.AIPlayer;
import io.miso.entities.Colour;
import io.miso.entities.HumanPlayer;
import io.miso.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameLogic {
    private final Scanner scanner = new Scanner(System.in);

    private final List<Player> players = new ArrayList<>();
    private final Board gameBoard;

    private boolean done = false;
    private Player currentPlayer = null;

    private int currentTurn = 0;

    public GameLogic(final boolean vsAI) {
        this.gameBoard = new Board();

        players.add(new HumanPlayer(Colour.BLACK));
        if (vsAI) {
            players.add(new AIPlayer(Colour.WHITE));
        } else {
            players.add(new HumanPlayer(Colour.WHITE));
        }
    }

    public void cycle() {
        currentPlayer = players.get(0);

        // Print the initial board!
        gameBoard.printBoard();

        while (!done) {
            final Integer[] coordinates;

            if (currentPlayer instanceof AIPlayer) {
                // coordinates = ((AIPlayer) currentPlayer).makeMove(gameBoard, currentTurn); // Basic "AI"
                coordinates = ((AIPlayer) currentPlayer).makeMinMaxMove(gameBoard, currentTurn); // Minimax "AI"
            } else {
                coordinates = inputMoveCoordinates();
            }

            if (coordinates != null && coordinates.length == 2) {
                System.out.println("Player." + currentPlayer.getColour() + " sets piece at [" + coordinates[0] + "." + coordinates[1] + "]!");
                gameBoard.setPieceAtCoordinates(currentPlayer.getColour(), coordinates[0], coordinates[1]);
            } else {
                System.err.println("Something went very wrong with getting coordinates from player!");
                System.exit(-1);
            }

            // Wait 200 ms and then print the board again
            System.out.println("-------------");
            cycleWait(200);
            gameBoard.printBoard();

            // current player has won after the valid move
            final boolean currentPlayerHasWon = gameBoard.validateWinConditions(currentPlayer.getColour(), currentTurn);
            if (currentPlayerHasWon) {
                done = true;

                System.out.println(currentPlayer.toString() + " is the winner!!!");
            } else {
                // At the end wait 200 ms
                cycleWait(200);
                changeCurrentPlayer();
            }

            currentTurn++;
        }
    }

    private void changeCurrentPlayer() {
        if (players.indexOf(currentPlayer) == 0) {
            currentPlayer = players.get(1);
        } else {
            currentPlayer = players.get(0);
        }
    }

    private Integer[] inputMoveCoordinates() {
        System.out.print(currentPlayer + " -> Where to put your piece? [x.y]: ");

        final String input = scanner.nextLine();

        if (input.equalsIgnoreCase("x")) {
            System.out.println("Quitting...");
            System.exit(0);
        }

        final String[] inputArr = input.split("\\.");

        if (inputArr.length == 2) {
            try {
                final int x = Integer.parseInt(inputArr[0]);
                final int y = Integer.parseInt(inputArr[1]);

                if (validateMove(x, y)) {
                    return new Integer[]{x, y};
                } else {
                    System.out.println("Invalid move. Please try again!");
                }
            } catch (final NumberFormatException e) {
                System.out.println("Invalid input. Please enter coordinates as x.y!");
            }
        } else {
            System.out.println("Invalid input. Please enter coordinates as x.y!");
        }

        // Call the method again in case of invalid input
        return inputMoveCoordinates();
    }

    private boolean validateMove(final int x, final int y) {
        // If any of x and y is equal to -1 then input was bad, just return false!
        // check so that the coordinates is even within the board.
        // check so that the coordinate is not taken on the board by either white or black.
        // If not valid move println that it was not a valid move. Then return false.
        // If it was a valid move then return true.

        if (x == -1 || y == -1) {
            System.out.println("Invalid coordinates. Use digits! Example input: 5.3");
            return false;
        }

        if (!gameBoard.isValidPosition(x, y)) {
            System.out.println("Invalid coordinates. They should be within the board!");
            return false;
        }

        if (!gameBoard.isPositionFree(x, y)) {
            System.out.println("The chosen position is already occupied! Choose another one.");
            return false;
        }

        return true;
    }

    private void cycleWait(final Integer ms) {
        try {
            Thread.sleep((ms != null && ms > 0) ? ms : 200);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(e);
        }
    }

    public Board getGameBoard() {
        return gameBoard;
    }
}
