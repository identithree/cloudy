package dev.quinnlane.cloudy.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HumidityPill extends VBox {
	@FXML
	private Label humidity;

	@FXML
	public void initialize() {
		setHumidity(12);
	}

	public void setHumidity(int humidity) {
		this.humidity.setText(humidity + "%");
	}
}
