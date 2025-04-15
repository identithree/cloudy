package dev.quinnlane.cloudy.frontend.controllers;

import dev.quinnlane.cloudy.backend.dataproviders.DataProvider;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainView extends AnchorPane implements UIPanel {
	@FXML
	public TemperaturePanel temperaturePanelController;
	@FXML
	public WindPanel windPanelController;
	@FXML
	public SunsetPill sunsetPillController;
	@FXML
	public OverTimePanel overTimePanelController;
	//	@FXML public LocationPanel locationPanelController;
	@FXML
	public HumidityPill humidityPillController;

	@FXML
	public void initialize() {
	}

	public void updateFromBackend(DataProvider provider) {
		temperaturePanelController.setTemperatureRead(provider.getCurrentTemperature());

//		sunsetPillController.setSunrise(provider.getSunrise());
//		sunsetPillController.setSunset(provider.getSunset());

		var wind = provider.getWind();
		if (wind != null) {
			windPanelController.setSpeed(wind.z());
		}
	}
}
