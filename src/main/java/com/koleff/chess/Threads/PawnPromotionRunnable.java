package com.koleff.chess.Threads;

import com.koleff.chess.Pieces.Pawn;

public class PawnPromotionRunnable implements Runnable {
    /**
     * Fields
     */
    private Pawn pawn;
    private String coordinates;
    private boolean stopRequest = false;

    public PawnPromotionRunnable(Pawn pawn, String coordinates) {
        this.pawn = pawn;
        this.coordinates = coordinates;
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
            pawn.promote(coordinates);
        } else {
            System.out.println("Runnable has stopped.");
        }
    }
}
