package com.juanite.controller;

import com.juanite.App;
import com.juanite.util.Validator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @FXML
    private void startCountdown() {
        if (!isRunning) {
            if(Validator.isValid(txt_countdown.getText())) {

            }

            remainingTime = remainingTime / 1000; // Convertir a segundos
            isRunning = true;

            countdownThread = new Thread(() -> {
                while (remainingTime > 0 && isRunning) {
                    Platform.runLater(() -> {
                        actualizarEtiqueta();
                    });

                    try {
                        Thread.sleep(1000); // Esperar 1 segundo
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
        isRunning = false;
    }

    @FXML
    private void stopCountdown() {
        isRunning = false;
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

        String tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundosRestantes);
        lbl_countdown.setText(tiempoFormateado);
    }
}