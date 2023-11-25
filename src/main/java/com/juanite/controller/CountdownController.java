package com.juanite.controller;

import com.juanite.App;
import com.juanite.util.Validator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    private int remainingTime;

    private Thread countdownThread;
    private volatile boolean isRunning;


    @FXML
    private void startCountdown() {
        if (!isRunning) {
            if (Validator.isValid(txt_countdown.getText())) {
                String[] timeParts = txt_countdown.getText().split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);

                remainingTime = hours * 3600 + minutes * 60 + seconds; // Convert to seconds
            } else {
                // Handle invalid input (you may show an error message)
                return;
            }

            isRunning = true;

            btn_pause.setVisible(true);
            btn_stop.setVisible(true);

            countdownThread = new Thread(() -> {
                while (remainingTime > 0 && isRunning) {
                    Platform.runLater(() -> {
                        actualizarEtiqueta();
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
                    actualizarEtiqueta();
                });
            });

            countdownThread.start();
        }
    }

    @FXML
    private void pauseCountdown() {
        if (isRunning) {
            isRunning = false;
            btn_pause.setText("RESUME");
        } else {


            isRunning = true;
            btn_pause.setText("PAUSE");

            countdownThread = new Thread(() -> {
                while (remainingTime > 0 && isRunning) {
                    Platform.runLater(() -> {
                        actualizarEtiqueta();
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
                    actualizarEtiqueta();
                });
            });

            countdownThread.start();

        }
    }

    @FXML
    private void stopCountdown() {
        isRunning = false;
        btn_pause.setVisible(false);
        btn_stop.setVisible(false);
        remainingTime = 0;
        actualizarEtiqueta();
    }

    @FXML
    private void goToChrono() throws IOException {
        isRunning = false;
        App.setRoot("main");
    }

    private void actualizarEtiqueta() {
        int horas = remainingTime / 3600;
        int minutos = (remainingTime % 3600) / 60;
        int segundosRestantes = remainingTime % 60;
        if(segundosRestantes < 0){
            segundosRestantes = 0;
        }

        String tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundosRestantes);
        lbl_countdown.setText(tiempoFormateado);
    }
}