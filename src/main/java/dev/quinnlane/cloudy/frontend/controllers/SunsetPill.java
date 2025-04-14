package dev.quinnlane.cloudy.frontend.controllers;

import dev.quinnlane.cloudy.common.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Date;

public class SunsetPill extends VBox {
	@FXML
	private Label sunrise;
	@FXML
	private Label sunset;

	@FXML
	public void initialize() {
		setSunrise(new Date(System.currentTimeMillis()));
		setSunset(new Date(System.currentTimeMillis()));
	}

	public void setSunrise(Date time) {
		this.sunrise.setText(Constants.TIME_FORMAT.format(time));
	}

	public void setSunset(Date time) {
		this.sunset.setText(Constants.TIME_FORMAT.format(time));
	}
}
