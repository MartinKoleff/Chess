package com.koleff.chess.MediatorAndThreads;

import com.koleff.chess.CoordinatesAndMoves.Moves;
import com.koleff.chess.Pieces.Piece;

public class CalculatingAttackingMovesRunnable<T extends Piece> extends Moves implements Runnable {
    /**
     * Fields
     */
    private T piece;
    private boolean stopRequest = false;

    public CalculatingAttackingMovesRunnable(T piece) {
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
