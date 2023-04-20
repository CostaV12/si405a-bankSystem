package com.example.javafxwithjdbc.utils;

import javafx.scene.control.Alert;

public class Alerts {

    public static void ShowAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }
}
