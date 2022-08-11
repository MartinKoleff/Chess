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
    private long allowedTime;
    private boolean isRunning;
    private Label playerLabel;

    private Timeline timeline;
    private LocalTime time = LocalTime.parse("00:00:00");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss"); //Replace with hours, minutes and seconds (int variables)

    public Clock(long allowedTime, Label playerLabel) {
        this.allowedTime = allowedTime;
        this.playerLabel = playerLabel;

        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
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

    private void incrementTime() {
        time = time.plusSeconds(1);
        playerLabel.setText(time.format(dtf));
    }

    @FXML
    private void endTimer(ActionEvent event) {
        timeline.stop();
//            startTimerButton.setDisable(false);
        time = LocalTime.parse("00:00:00");
        playerLabel.setText(time.format(dtf));
    }
}

//Check when it goes in...
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
//        timeline.setCycleCount(Animation.INDEFINITE);
//    }
