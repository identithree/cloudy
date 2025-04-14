package dev.quinnlane.cloudy.frontend.controllers;

import dev.quinnlane.cloudy.common.unit.UnitLocales;
import dev.quinnlane.cloudy.common.unit.units.CompassRose;
import dev.quinnlane.cloudy.common.unit.units.Speed;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;

public class WindPanel extends VBox {
	@FXML private Label speedText;
	@FXML private Label compassText;
	@FXML
	private ImageView needle;

	@FXML
	public void initialize() {
		setSpeed(new Speed(3999, UnitLocales.CUSTOMARY));
		setCompass(CompassRose.EBS);
		setNeedleDirection(CompassRose.EBS);
	}

	public void setSpeed(Speed speed) {
		speedText.setText(speed.toString());
	}

	public void setCompass(CompassRose direction) {
		compassText.setText(direction.getHumanReadable());
	}

	public void setNeedleDirection(CompassRose direction) {
		needle.getTransforms().clear();
		needle.getTransforms().add(new Rotate(direction.toAbsoluteBearing(), 0, 0)); // TODO this shi is fucked up bruhhhhh T_T
	}
}
