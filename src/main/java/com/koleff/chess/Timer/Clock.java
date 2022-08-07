package com.koleff.chess.Timer;

import com.google.common.base.Stopwatch;

import java.util.TimerTask;

/**
 * Currently in development... (Coming soon)
 */
public class Clock {
    private long startTime;
    private long elapsedTime;
    private long allowedTime;

    private Stopwatch stopwatch;

    public Clock(long allowedTime) {
        this.allowedTime = allowedTime;
    }

    public void start() {
        if(startTime != 0L) {
            startTime = System.currentTimeMillis();
        }

        //New thread to check if over allowed time???
        stopwatch.start();
    }

    public void pause() {
        elapsedTime = startTime - System.currentTimeMillis();
        stopwatch.stop();
    }

    public long time(){
        return startTime - System.currentTimeMillis();
    }

    public boolean isRunning(){
        return stopwatch.isRunning();
    }

    @Override
    public String toString(){
        return "Elapsed time: " + time() + " nanoseconds.";
    }
}
