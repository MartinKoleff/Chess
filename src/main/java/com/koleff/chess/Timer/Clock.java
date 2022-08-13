package com.koleff.chess.Timer;

import com.google.common.base.Stopwatch;
import com.koleff.chess.Pieces.Colour;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimerTask;

import static com.koleff.chess.BoardAndFEN.ChessBoardController.currentPlayer;
import static com.koleff.chess.BoardAndFEN.ChessBoardController.nextTurnPlayer;

public class Clock implements Serializable {
    //    private long startTime;
//    private long elapsedTime;
    private int[] allowedTime;
    private boolean isRunning;
    private transient Label playerLabel;

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;

    private transient Timeline timeline;
    private LocalTime time;
    private transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss"); //Replace with hours, minutes and seconds (int variables)

    public Clock(Label playerLabel, int... allowedTime) {
        this.allowedTime = allowedTime;
        this.playerLabel = playerLabel;

        try {
            seconds = allowedTime[0];
            minutes = allowedTime[1];
            hours = allowedTime[2];
        } catch (IndexOutOfBoundsException e) {

        }
        setTime();
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> decreaseTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * First time setup
     */
    private void setTime() {
        if (hours / 10 > 0) {
            if (minutes / 10 > 0) {
                if (seconds / 10 > 0) {
                    time = LocalTime.parse(String.format("%d:%d:%d", hours, minutes, seconds));
                    return;
                }
                time = LocalTime.parse(String.format("%d:%d:0%d", hours, minutes, seconds));
                return;
            }
            time = LocalTime.parse(String.format("%d:0%d:0%d", hours, minutes, seconds));
            return;
        } else if (minutes / 10 > 0) {
            if (seconds / 10 > 0) {
                time = LocalTime.parse(String.format("0%d:%d:%d", hours, minutes, seconds));
                return;
            }
            time = LocalTime.parse(String.format("0%d:%d:0%d", hours, minutes, seconds));
            return;
        } else if (seconds / 10 > 0) {
            time = LocalTime.parse(String.format("0%d:0%d:%d", hours, minutes, seconds));
            return;
        } else {
            time = LocalTime.parse(String.format("0%d:0%d:0%d", hours, minutes, seconds));
        }
    }

    @FXML
    public void start() {
        isRunning = true;
//        startTime = System.currentTimeMillis();
        timeline.play();
    }

    @FXML
    public void pause() {
        isRunning = false;
//        elapsedTime = startTime - System.currentTimeMillis();
        timeline.pause();
    }

    private void decreaseTime() {
        time = time.minusSeconds(1);

        //Game over...
        if (time.equals(LocalTime.parse("00:00:00"))) {
            System.out.printf("Game over! Player %s wins!\n", nextTurnPlayer.getPlayerPiecesColor());
            this.pause();
        }

        if (seconds - 1 < 0) {
            if (minutes - 1 < 0) {
                if (hours - 1 < 0) {

                } else {
                    hours--;
                    minutes = 60;
                }
            } else {
                minutes--;
                seconds = 60;
            }
        } else {
            seconds--;
        }
        playerLabel.setText(time.format(dtf)); //String.format("%d:%d:%d", hours, minutes, seconds)
    }

    @FXML
    private void endTimer(ActionEvent event) {
        timeline.stop();
//            startTimerButton.setDisable(false);
        time = LocalTime.parse(String.format("%d:%d:%d", hours, minutes, seconds));
        playerLabel.setText(time.format(dtf));
    }
}

