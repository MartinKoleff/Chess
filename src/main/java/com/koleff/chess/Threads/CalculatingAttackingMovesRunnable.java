package com.koleff.chess.Threads;

import com.koleff.chess.CoordinatesAndMoves.Moves;
import com.koleff.chess.Pieces.Piece;

import static com.koleff.chess.Board.ChessBoardController.board;

public class CalculatingAttackingMovesRunnable extends Moves implements Runnable {
    /**
     * Fields
     */
    private Piece piece;
    private boolean stopRequest = false;

    public CalculatingAttackingMovesRunnable(Piece piece) {
        this.piece = piece;
    }

    /**
     * Functions
     */
    public synchronized void requestStop() {
        this.stopRequest = true;
    }

    public synchronized boolean isStopRequested() {
        return this.stopRequest;
    }

    @Override
    public void run() {
        if (!isStopRequested()) {
            if (!isCalculatingAttackingMoves && !isCalculatingCheckmate && !isCalculatingStalemate && !isCalculatingProtection) {
                board.updateBoard();
            }
            piece.move();
        } else {
            System.out.println("Runnable has stopped.");
        }
    }
}
