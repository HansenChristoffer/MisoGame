package io.miso.entities;

import io.miso.core.Board;

import java.util.Random;

public class AIPlayer implements Player {
    private static final int MAX_DEPTH = 3; // Careful with going too deep. I had to keep it at 2 when developing. But experiment if you'd like!

    private final Random random = new Random();
    private final Colour colour;
    private final Colour opponentColour;

    private int currentTurn = 0;

    public AIPlayer(final Colour colour) {
        this.colour = colour;
        this.opponentColour = colour == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    public Colour getOpponentColour() {
        return opponentColour;
    }

    public Integer[] makeMove(final Board gameBoard, final int currentTurn) {
        this.currentTurn = currentTurn;
        final int boardSize = gameBoard.getBoardSize();

        // 1. Check if AI can win in the next move.
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard.isPositionFree(i, j)) {
                    gameBoard.setPieceAtCoordinates(this.getColour(), i, j);  // Temporarily set AI's piece
                    if (gameBoard.validateWinConditions(this.getColour(), currentTurn)) {  // Check win
                        gameBoard.clearPieceAtCoordinates(i, j);  // Remove temporary piece
                        return new Integer[]{i, j};  // AI wins, return the coordinates
                    }
                    gameBoard.clearPieceAtCoordinates(i, j);  // Remove temporary piece
                }
            }
        }

        // 2. Check if opponent can win in the next move, and block it.
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard.isPositionFree(i, j)) {
                    gameBoard.setPieceAtCoordinates(opponentColour, i, j);  // Temporarily set opponent's piece
                    if (gameBoard.validateWinConditions(opponentColour, currentTurn)) {  // Check win
                        gameBoard.clearPieceAtCoordinates(i, j);  // Remove temporary piece
                        return new Integer[]{i, j};  // AI blocks, return the coordinates
                    }
                    gameBoard.clearPieceAtCoordinates(i, j);  // Remove temporary piece
                }
            }
        }

        // 3. If none of the above, make a random move.
        return getRandomMove(boardSize);
    }

    public Integer[] getRandomMove(final int boardSize) {
        final int x = random.nextInt(boardSize);
        final int y = random.nextInt(boardSize);

        return new Integer[]{x, y};
    }

    public Integer[] makeMinMaxMove(final Board gameBoard, final int currentTurn) {
        this.currentTurn = currentTurn;
        Integer[] bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (final Integer[] move : gameBoard.getValidMoves()) {
            gameBoard.setPieceAtCoordinates(this.getColour(), move[0], move[1]);
            final int score = minimax(gameBoard, 0, false);
            gameBoard.clearPieceAtCoordinates(move[0], move[1]); // undo move

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }


    private int minimax(final Board gameBoard, final int depth, final boolean isMaximizing) {
        if (gameBoard.gameOver(currentTurn) || depth == MAX_DEPTH) { // MAX_DEPTH is the depth limit of the search
            return evaluateBoard(gameBoard); // A function to evaluate the game state
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (final Integer[] move : gameBoard.getValidMoves()) {
                gameBoard.setPieceAtCoordinates(this.getColour(), move[0], move[1]);
                final int score = minimax(gameBoard, depth + 1, false);
                gameBoard.clearPieceAtCoordinates(move[0], move[1]); // undo move
                bestScore = Math.max(score, bestScore);
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (final Integer[] move : gameBoard.getValidMoves()) {
                gameBoard.setPieceAtCoordinates(opponentColour, move[0], move[1]); // opponent is a Player object representing the opponent
                final int score = minimax(gameBoard, depth + 1, true);
                gameBoard.clearPieceAtCoordinates(move[0], move[1]); // undo move
                bestScore = Math.min(score, bestScore);
            }
            return bestScore;
        }
    }

    private int evaluateBoard(final Board gameBoard) {
        int score = 0;

        for (int i = 0; i < gameBoard.getBoardSize(); i++) {
            for (int j = 0; j < gameBoard.getBoardSize(); j++) {
                if (gameBoard.getPieceAtCoordinates(i, j) == this.getColour()) {
                    // AI pieces increase score
                    score++;
                    // Check for potential win conditions to add more to the score
                    score += checkForPotentialWins(gameBoard, i, j, this.getColour());
                } else if (gameBoard.getPieceAtCoordinates(i, j) == this.getOpponentColour()) {
                    // Opponent pieces decrease score
                    score--;
                    // Check for potential blocking opportunities to add more to the score
                    score += checkForPotentialWins(gameBoard, i, j, this.getOpponentColour());
                }
            }
        }

        return score;
    }

    private int checkForPotentialWins(final Board gameBoard, final int x, final int y, final Colour colour) {
        int score = 0;
        final int boardSize = gameBoard.getBoardSize();

        // Directions to check: horizontal, vertical, diagonal from top-left to bottom-right, 
        // diagonal from bottom-left to top-right.
        final int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (final int[] direction : directions) {
            final int dx = direction[0];
            final int dy = direction[1];
            int count = 0;

            // Check for up to 4 pieces in a row in the current direction.
            for (int i = 1; i <= 4; i++) {
                if (x + i * dx >= 0 && x + i * dx < boardSize &&
                        y + i * dy >= 0 && y + i * dy < boardSize &&
                        gameBoard.getPieceAtCoordinates(x + i * dx, y + i * dy) == colour) {
                    count++;
                } else {
                    break;
                }
            }

            // If there are 4 pieces in a row and the space at either end is open, add a high score.
            if ((count == 4) &&
                    (((x - dx >= 0)
                            && (x - dx < boardSize)
                            && (y - dy >= 0)
                            && (y - dy < boardSize)
                            && (gameBoard.getPieceAtCoordinates(x - dx, y - dy) == Colour.NONE)) ||
                            ((x + 5 * dx >= 0)
                                    && (x + 5 * dx < boardSize)
                                    && (y + 5 * dy >= 0)
                                    && (y + 5 * dy < boardSize)
                                    && (gameBoard.getPieceAtCoordinates(x + 5 * dx, y + 5 * dy) == Colour.NONE)))) {
                score += 10000;
            }

            // If there are 3 pieces in a row and the space at both ends is open, add a medium score.
            if (count == 3 &&
                    (x - dx >= 0 && x - dx < boardSize && y - dy >= 0 && y - dy < boardSize && gameBoard.getPieceAtCoordinates(x - dx, y - dy) == Colour.NONE) &&
                    (x + 4 * dx >= 0 && x + 4 * dx < boardSize && y + 4 * dy >= 0 && y + 4 * dy < boardSize && gameBoard.getPieceAtCoordinates(x + 4 * dx, y + 4 * dy) == Colour.NONE)) {
                score += 1000;
            }
        }

        return score;
    }

    @Override
    public String toString() {
        return "" + getColour();
    }
}
