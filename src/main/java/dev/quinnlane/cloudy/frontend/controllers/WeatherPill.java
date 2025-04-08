package dev.quinnlane.cloudy.frontend.controllers;

import dev.quinnlane.cloudy.common.Constants;
import dev.quinnlane.cloudy.common.unit.UnitLocales;
import dev.quinnlane.cloudy.common.unit.units.Temperature;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.Date;

public class WeatherPill extends VBox {
	@FXML
	private Label temperature;
	@FXML
	private Image image;
	@FXML
	private Label chance;
	@FXML
	private Label time;

	@FXML
	public void initialize() {
		setTemperature(new Temperature(4.0, UnitLocales.CUSTOMARY));
		setChance(49);
		setTime(new Date(System.currentTimeMillis()));
	}

	public void setTemperature(Temperature temperature) {
		this.temperature.setText(temperature.toString());
	}

	public void setChance(int chance) {
		this.chance.setText(chance + "%");
	}

	public void setTime(Date time) {
		this.time.setText(Constants.TIME_FORMAT.format(time));
	}

	public void setDate(Date time) {
		this.time.setText(Constants.SIMPLE_DATE_FORMAT.format(time));
	}
}
