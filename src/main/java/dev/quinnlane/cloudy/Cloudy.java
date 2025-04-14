package dev.quinnlane.cloudy;

import dev.quinnlane.cloudy.frontend.Frontend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Cloudy {
	/**
	 * This field stores the arguments that Cloudy was executed with.
	 */
	private static String[] arguments;

	/**
	 * This field stores the instance of the Backend that Cloudy uses during runtime.
	 */
	private static Frontend frontend;

	/**
	 * THis field stores the version of Cloudy that is currently running.
	 */
	private static String VERSION = "X.X.X";

	/**
	 * Initializes a new instance of Log4J for use across this entrypoint class and all common classes.
	 */
	public static final Logger logger = LogManager.getFormatterLogger("Cloudy > Common");

	/**
	 * Retrieves the instance of the Frontend used during runtime.
	 *
	 * @return the current Frontend instance
	 */
	public static Frontend getFrontend() {
		return frontend;
	}

	/**
	 * Retrieves the arguments that the program was executed with.
	 *
	 * @return an array of Strings representing the arguments passed on program startup
	 */
	public static String[] getArguments() {
		return arguments;
	}

	/**
	 * Retrieves the version of the Cloudy application that is currently running.
	 *
	 * @return a String representing the current version of the application
	 */
	public static String getVersion() {
		return VERSION;
	}

	// Entrypoint method
    public static void main(String[] args) {
		// -- Load the program version at runtime from version.properties
		logger.debug("Loading version from version.properties file...");
		String version = VERSION;
		Properties props = new Properties();

		try (InputStream is = Cloudy.class.getResourceAsStream("/gradle/version.properties")) {
			props.load(is);
			version = props.getProperty("version");
			logger.trace("version.properties file reports version as %s", version);
		} catch (IOException e) {
			logger.error("Failed to load version.properties file!", e);
			logger.info("Version information could not be loaded. The interface might look weird, but the app is completely functional in this state.");
		}

		VERSION = version;

		// -- Start Cloudy
		logger.info("Starting Cloudy v%s...", VERSION);
		arguments = args;

		// Initialize backend and frontend
		frontend = new Frontend();

		logger.info("Cloudy has been started!");
    }
}
