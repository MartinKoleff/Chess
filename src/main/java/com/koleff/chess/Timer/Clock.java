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

/**
 * Currently in development... (Coming soon)
 */
public class Clock implements Serializable {
    //    private long startTime;
//    private long elapsedTime;
    private int[] allowedTime;
    private boolean isRunning;
    private Label playerLabel;

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;

    private Timeline timeline;
    private LocalTime time;
    //private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss"); //Replace with hours, minutes and seconds (int variables)

    public Clock(Label playerLabel, int... allowedTime) {
        this.allowedTime = allowedTime;
        this.playerLabel = playerLabel;

        try{
            seconds = allowedTime[0];
            minutes = allowedTime[1];
            hours = allowedTime[2];
        }catch (IndexOutOfBoundsException e){

        }

        time = LocalTime.parse(String.format("%d:%d:%d", hours, minutes, seconds)); //when time is 1 digit -> exception...
        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> decreaseTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
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

        //time equals 0 check...
//        if(){
//
//        }

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
        }else{
            seconds--;
        }
        playerLabel.setText(String.format("%d:%d:%d", hours, minutes, seconds)); //time.format(dtf)
        System.out.println(hours + ":" + minutes + ":" + seconds);
    }

    @FXML
    private void endTimer(ActionEvent event) {
        timeline.stop();
//            startTimerButton.setDisable(false);
        time = LocalTime.parse(String.format("%d:%d:%d", hours, minutes, seconds));
        playerLabel.setText(String.format("%d:%d:%d", hours, minutes, seconds));
    }
}


//  if(seconds + 1 == 60){
//            if(minutes + 1 == 60){
//                if(hours + 1 == 24){
//                    hours = 0;
//
//                    //Game over?
//                }else {
//                    hours += 1;
//                }
//                minutes = 0;
//            }else {
//                minutes += 1;
//            }
//            seconds = 0;
//        }else {
//            seconds += 1;
//        }