package dev.quinnlane.cloudy.common;

import dev.quinnlane.cloudy.common.unit.Unit;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * The Constants class serves as a central repository for constant values and preset configurations
 * that are commonly used throughout the application.
 * <p>
 * It provides predefined symbols, formatting instances, and other static utility values, ensuring
 * consistency and reusability in various parts of the program.
 */
public class Constants {
	/**
	 * Contains a copy of the degree symbol character for use in and around the program.
	 */
	public static final char DEGREE_SYMBOL = '°';
	/**
	 * A predefined {@link DecimalFormat} instance used as the default precision format
	 * for displaying numeric values. This format rounds numbers to three decimal places.
	 * <p>
	 * It is primarily used in the {@link Unit} class to format the representation of unit
	 * values, ensuring consistency and clarity in output. The format pattern is defined as
	 * "#.###", which limits the precision to three significant decimal places and discards
	 * trailing zeroes.
	 */
	public static final DecimalFormat DEFAULT_PRECISION = new DecimalFormat("#.###");
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mma");
}
