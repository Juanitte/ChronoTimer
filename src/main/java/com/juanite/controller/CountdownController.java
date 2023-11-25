package com.juanite.controller;

import com.juanite.App;
import com.juanite.util.Validator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

public class CountdownController {

    @FXML
    public Label lbl_countdown;
    @FXML
    public Button btn_pause;
    @FXML
    public Button btn_start;
    @FXML
    public Button btn_chrono;
    @FXML
    public Button btn_stop;
    @FXML
    public TextField txt_countdown;
    @FXML
    public Slider slider_volume;
    @FXML
    public ImageView img_volume;

    private int remainingTime;

    private Thread countdownThread;
    private volatile boolean isRunning;
    private MediaPlayer mediaPlayer;

    /**
     * Method that gets called whenever the GUI is loaded.
     */
    @FXML
    private void initialize() {
        slider_volume.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue());
            }
        });
    }

    /**
     * Method that starts the countdown timer based on the text-field input. It also plays a mp3 file.
     */
    @FXML
    private void startCountdown() {
        if (!isRunning) {
            if (Validator.isValid(txt_countdown.getText())) {
                String[] timeParts = txt_countdown.getText().split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);

                remainingTime = hours * 3600 + minutes * 60 + seconds;
            } else {
                return;
            }

            isRunning = true;

            btn_pause.setVisible(true);
            btn_stop.setVisible(true);
            btn_start.setVisible(false);
            txt_countdown.setVisible(false);
            btn_chrono.setVisible(false);
            slider_volume.setVisible(true);
            img_volume.setVisible(true);

            playMP3("victory.mp3");

            countdownThread = new Thread(() -> {
                while (remainingTime > 0 && isRunning) {
                    Platform.runLater(() -> {
                        refreshLabel();
                    });

                    try {
                        Thread.sleep(1000); // Wait for 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    remainingTime--;
                }

                Platform.runLater(() -> {
                    isRunning = false;
                    refreshLabel();
                });
            });

            countdownThread.start();
        }
    }

    /**
     * Method that pauses the countdown timer or resumes it if already paused. It doesn't affect the music, the mp3 file will continue playing.
     */
    @FXML
    private void pauseCountdown() {
        if (isRunning) {
            isRunning = false;
            btn_pause.setText("RESUME");
        } else {
            isRunning = true;
            btn_pause.setText("PAUSE");

            if (mediaPlayer == null) {
                playMP3("victory.mp3");
            }

            countdownThread = new Thread(() -> {
                while (remainingTime > 0 && isRunning) {
                    Platform.runLater(() -> {
                        refreshLabel();
                    });

                    try {
                        Thread.sleep(1000); // Wait for 1 second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    remainingTime--;
                }

                Platform.runLater(() -> {
                    isRunning = false;
                    refreshLabel();
                });
            });

            countdownThread.start();
        }
    }

    /**
     * Method that stops the countdown timer as well as the playing mp3 file.
     */
    @FXML
    private void stopCountdown() {
        isRunning = false;
        btn_pause.setVisible(false);
        btn_stop.setVisible(false);
        btn_start.setVisible(true);
        txt_countdown.setVisible(true);
        btn_chrono.setVisible(true);
        slider_volume.setVisible(false);
        img_volume.setVisible(false);
        remainingTime = 0;
        stopMP3();
        refreshLabel();
    }

    /**
     * Method that plays a mp3 file
     * @param fileName - the name of the mp3 file to play (must be allocated in the resources folder).
     */
    private void playMP3(String fileName) {
        File file = new File("src/main/resources/com/juanite/" + fileName);
        Media media = new Media(file.toURI().toString());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(mediaPlayer.getStartTime()));
        mediaPlayer.volumeProperty().bindBidirectional(slider_volume.valueProperty());

        mediaPlayer.play();
    }

    /**
     * Method that stops the mp3 file from playing.
     */
    private void stopMP3() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Method that switches to the chronometer screen.
     * @throws IOException
     */
    @FXML
    private void goToChrono() throws IOException {
        isRunning = false;
        App.setRoot("main");
    }

    /**
     * Method that refreshes the timer with new data.
     */
    private void refreshLabel() {
        int hours = remainingTime / 3600;
        int minutes = (remainingTime % 3600) / 60;
        int remainingSeconds = remainingTime % 60;
        if(remainingSeconds < 0){
            remainingSeconds = 0;
        }

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
        lbl_countdown.setText(formattedTime);
    }
}