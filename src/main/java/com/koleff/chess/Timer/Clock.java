package com.koleff.chess.Timer;

import com.google.common.base.Stopwatch;

import java.util.TimerTask;

public class Clock {
//    private long startTime;
//    private long elapsedTime;
    private long allowedTime;

    public Stopwatch stopwatch;
    public boolean isRunning;

    public Clock(long allowedTime){
    this.allowedTime = allowedTime;
    }

    public void start(){
        isRunning = true;
//        startTime = System.currentTimeMillis();
        stopwatch.start();
    }

    public void pause(){
        isRunning = false;
//        elapsedTime = startTime - System.currentTimeMillis();
        stopwatch.stop();
    }
}
