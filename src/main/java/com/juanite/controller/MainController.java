package com.juanite.controller;

import java.io.IOException;

import com.juanite.App;
import com.juanite.util.AppData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class MainController {

    @FXML
    public Label lbl_chrono;
    @FXML
    public Button btn_startStop;
    @FXML
    public Button btn_countdown;
    @FXML
    public Button btn_reset;

    private Timeline timeline;
    private int seconds = 0;

    public void initialize() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (AppData.isIsRunning()) {
                            seconds++;
                            refreshLabel();
                        }
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void goToCountdown() throws IOException {
        if(!AppData.isIsRunning()) {
            timeline.stop();
            timeline = null;
            App.setRoot("countdown");
        }
    }

    @FXML
    private void startStopChrono() {
        if(!AppData.isIsRunning()) {
            AppData.setIsRunning(true);
            btn_startStop.setText("STOP");
            timeline.play();
        } else {
            AppData.setIsRunning(false);
            btn_startStop.setText("START");
            timeline.pause();
        }
    }

    @FXML
    private void resetChrono() {
        AppData.setIsRunning(false);
        btn_startStop.setText("START");
        seconds = 0;
        refreshLabel();
        timeline.pause();
    }

    private void refreshLabel() {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
        lbl_chrono.setText(formattedTime);
    }
}
