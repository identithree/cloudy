package dev.quinnlane.cloudy.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class AirQualityPanel extends HBox {
	@FXML
	private Label quality;
	@FXML
	private Circle circle;

	@FXML
	public void initialize() {
		setQuality(10);
	}

	public void setQuality(int quality) {
		this.quality.setText(quality + "");
		circle.setFill(javafx.scene.paint.Color.valueOf("#ff00cc"));
	}
}
