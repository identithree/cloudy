package dev.quinnlane.cloudy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainView {
    @FXML
    public Label label;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        label.setText(String.format("Hello, JavaFX %s!%nRunning on Java %s", javafxVersion, javaVersion));
    }
}
