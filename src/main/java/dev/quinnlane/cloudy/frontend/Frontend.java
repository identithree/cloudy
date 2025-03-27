package dev.quinnlane.cloudy.frontend;

import dev.quinnlane.cloudy.Cloudy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Frontend extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/MainView.fxml")));

		stage.setTitle("Cloudy - v" + Cloudy.VERSION);
		stage.setScene(new Scene(root));
		stage.show();
	}
}
