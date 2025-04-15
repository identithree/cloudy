package dev.quinnlane.cloudy.frontend.controllers;

import dev.quinnlane.cloudy.backend.dataproviders.DataProvider;
import dev.quinnlane.cloudy.common.unit.UnitLocales;
import dev.quinnlane.cloudy.common.unit.units.Speed;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainView extends AnchorPane {
	@FXML
	public TemperaturePanel temperaturePanelController;
	@FXML
	public WindPanel windPanelController;
	@FXML
	public SunsetPill sunsetPillController;
	@FXML
	public OverTimePanel overTimePanelController;
	@FXML
	public LocationPanel locationPanelController;
	@FXML
	public HumidityPill humidityPillController;

	@FXML
	public void initialize() {
	}

	public void updateFromBackend(DataProvider provider) {
		// TODO: Fetch temperature from call `Cloudy.getBackend().getProvider().getCurrentTemperature()`.
		temperaturePanelController.setTemperatureRead(provider.getCurrentTemperature());

//		var wind = Cloudy.getBackend().getProvider().getWind();
		windPanelController.setSpeed(new Speed(10000, UnitLocales.CUSTOMARY));
	}
}
