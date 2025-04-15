package dev.quinnlane.cloudy.frontend.controllers;

import dev.quinnlane.cloudy.common.unit.units.Temperature;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

public class TemperaturePanel extends HBox implements UIPanel {
	@FXML private Label temperatureRead;
	@FXML private Image weatherImage;

	@FXML
	public void initialize() {
		System.out.println(weatherImage);
	}

	public void setTemperatureRead(Temperature temperature) {
		this.temperatureRead.setText(temperature.toString());
	}

	public void setWeatherImage() {
		// TODO: get and set the weather conditions image
		//this.weatherImage = weatherImage;
	}
}
