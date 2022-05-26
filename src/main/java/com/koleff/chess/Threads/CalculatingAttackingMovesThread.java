package com.koleff.chess.Threads;

public class CalculatingAttackingMovesThread extends Thread{
    /**
     * Fields
     */
    private CalculatingAttackingMovesRunnable runnable;
    private boolean stopRequest;


    public CalculatingAttackingMovesThread(CalculatingAttackingMovesRunnable runnable){
        this.runnable = runnable;
    }
    /**
     * Functions
     */
    public synchronized void requestStop(){
        this.stopRequest = true;
    }

    public synchronized boolean isStopRequested(){
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
