package com.koleff.chess.Threads;

public class PawnPromotionThread extends Thread {
    private PawnPromotionRunnable runnable;
    private boolean stopRequest;

    public PawnPromotionThread(PawnPromotionRunnable runnable) {
        this.runnable = runnable;
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
    public void start() {
        if (!isStopRequested()) {
            runnable.run();
        } else {
            System.out.printf("Thread %s has stopped.\n", Thread.currentThread().getId());
        }
    }
}