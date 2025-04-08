package dev.quinnlane.cloudy.frontend.controllers;

import dev.quinnlane.cloudy.common.unit.UnitLocales;
import dev.quinnlane.cloudy.common.unit.units.Temperature;
import javafx.fxml.FXML;

public class MainView {
	@FXML
	public TemperaturePanel temperaturePanelController;

	@FXML
	public void initialize() {
		updateFromBackend();
	}

	public void updateFromBackend() {
		// TODO: Fetch temperature from call `Cloudy.getBackend().getProvider().getCurrentTemperature()`.
		temperaturePanelController.setTemperatureRead(new Temperature(6.0, UnitLocales.CUSTOMARY));
	}
}
