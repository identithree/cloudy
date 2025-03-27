package dev.quinnlane.cloudy;

import dev.quinnlane.cloudy.backend.Backend;
import dev.quinnlane.cloudy.frontend.Frontend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Cloudy {
	/**
	 * Initializes a new instance of Log4J for use during execution
	 */
	public static final Logger logger = LogManager.getFormatterLogger("Cloudy");
	/**
	 * Stores the current version of the application
	 */
	public static final String VERSION;

	// Get the program version from version.propeerties
	static {
		String version;
		Properties props = new Properties();

		try (InputStream is = Cloudy.class.getResourceAsStream("/gradle/version.properties")) {
			props.load(is);
			version = props.getProperty("version");
		} catch (IOException e) {
			logger.error("Failed to load version.properties file!", e);
			version = "X.X.X";
		}

		VERSION = version;
	}

    public static void main(String[] args) {
		logger.info("Starting Cloudy...");
		Frontend.launch(Frontend.class, args);
		logger.info("Cloudy has been started!");
    }
}
