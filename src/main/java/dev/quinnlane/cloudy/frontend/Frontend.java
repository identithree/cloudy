package dev.quinnlane.cloudy.frontend;

import dev.quinnlane.cloudy.Cloudy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class Frontend extends Application {
	private static final String TITLE = "Cloudy - v" + Cloudy.getVersion();
	public static final Logger logger = LogManager.getFormatterLogger("Cloudy > Frontend");

	public Frontend() {
		logger.trace("Frontend class constructor called from thread '%s'", Thread.currentThread().getName());
		if (Cloudy.getFrontend() != null) {
			logger.debug("The frontend has already been initialized! Skipping constructor logic...");
			return;
		}

		logger.info("Initializing frontend...");
		new Thread(() -> launch(Cloudy.getArguments()), "Cloudy JavaFX Initializer").start();
		logger.info("Frontend has been initialized!");
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/MainView.fxml")));

		stage.setTitle(TITLE);
		stage.setScene(new Scene(root));
		stage.show();
	}
}
