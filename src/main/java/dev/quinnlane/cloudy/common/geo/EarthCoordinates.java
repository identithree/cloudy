package dev.quinnlane.cloudy.common.geo;

import dev.quinnlane.cloudy.common.Constants;
import dev.quinnlane.cloudy.common.datatypes.OrderedPair;
import dev.quinnlane.cloudy.common.datatypes.OrderedTriplet;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

/**
 * The EarthCoordinates class represents geographical coordinates, providing functionalities
 * to handle and convert latitude and longitude values between various formats.
 * Latitude and longitude values can be represented in raw decimal degrees, degrees-minutes-seconds (DMS),
 * or with quadrant labels.
 * The class also supports custom display formatting for precision.
 */
public class EarthCoordinates {
	/**
	 * Represents the formatting style for displaying geographical coordinates.
	 * <p>
	 * This enumeration defines different formats that can be used to represent coordinates
	 * such as latitude and longitude. It is primarily used to control the output style
	 * of the {@link EarthCoordinates} class when presenting its data.
	 * <p>
	 * The available formats are:
	 * <ul>
	 *     <li>{@code RAW}: Displays the raw data without any formatting.</li>
	 *     <li>{@code DECIMAL_DEGREES}: Formats the coordinates as decimal degrees with a degree symbol.</li>
	 *     <li>{@code DECIMAL_DEGREES_WITH_QUADRANT}: Formats the coordinates as decimal degrees with a degree symbol and directional quadrant (e.g., N, S, E, W).</li>
	 *     <li>{@code DEGREES_MINUTES_SECONDS}: Displays the coordinates in degrees, minutes, and seconds (DMS) format, along with the directional quadrant
	 *     (e.g., N, S, E, W).</li>
	 * </ul>
	 */
	public enum DisplayFormat {
		RAW, DECIMAL_DEGREES, DECIMAL_DEGREES_WITH_QUADRANT, DEGREES_MINUTES_SECONDS
	}

	/**
	 * Converts a geographical coordinate from decimal degrees to degrees, minutes, and seconds (DMS) format.
	 * <p>
	 * The method takes a decimal degree value and decomposes it into its respective
	 * degrees, minutes, and seconds components.
	 * The result is returned as an
	 * {@code OrderedTriplet} containing the integer degrees, integer minutes, and
	 * fractional seconds as a {@code BigDecimal}.
	 *
	 * @param decimalDegrees the geographical coordinate in decimal degrees to convert; must not be null
	 * @return an {@code OrderedTriplet} containing the degrees (integer), minutes (integer),
	 *         and seconds (fractional, as BigDecimal) components in DMS format
	 */
	public static @NotNull OrderedTriplet<Integer, Integer, BigDecimal> decimalDegreesToDMS(@NotNull BigDecimal decimalDegrees) {
		final MathContext mathContext = MathContext.DECIMAL64;

		int degrees = decimalDegrees.intValue();
		BigDecimal fractionalDegrees = decimalDegrees.subtract(BigDecimal.valueOf(degrees), mathContext);
		BigDecimal totalMinutes = fractionalDegrees.multiply(BigDecimal.valueOf(60), mathContext);
		int minutes = totalMinutes.intValue();
		BigDecimal seconds = totalMinutes.subtract(BigDecimal.valueOf(minutes), mathContext)
				.multiply(BigDecimal.valueOf(60), mathContext);

		return new OrderedTriplet<>(degrees, minutes, seconds);
	}

	/**
	 * Converts geographical coordinates from degrees, minutes, and seconds (DMS) format
	 * to decimal degrees format.
	 * <p>
	 * The method accepts an {@code OrderedTriplet} containing the DMS representation
	 * of a coordinate, where the first component represents degrees, the second represents
	 * minutes, and the third represents seconds. It then computes the decimal degree equivalent
	 * by combining these components.
	 *
	 * @param degreesMinutesSeconds an {@code OrderedTriplet} representing the DMS value of the coordinate.
	 *                              The first component is an integer for degrees, the second component
	 *                              is an integer for minutes, and the third component is a {@code BigDecimal}
	 *                              for seconds. The input must not be null.
	 * @return a {@code BigDecimal} representing the coordinate value in decimal degrees format.
	 */
	public static @NotNull BigDecimal dmsToDecimalDegrees(@NotNull OrderedTriplet<Integer, Integer, BigDecimal> degreesMinutesSeconds) {
		final MathContext mathContext = MathContext.DECIMAL64;

		BigDecimal degrees = new BigDecimal(degreesMinutesSeconds.x(), mathContext);
		BigDecimal minutesScaled = new BigDecimal(degreesMinutesSeconds.y(), mathContext)
				.divide(BigDecimal.valueOf(60), mathContext);
		BigDecimal secondsScaled = degreesMinutesSeconds.z().divide(BigDecimal.valueOf(3600), mathContext);

		return degrees.add(minutesScaled).add(secondsScaled);
	}

	/**
	 * Represents the latitude coordinate of a geographical location.
	 * <p>
	 * This variable holds the latitude in decimal degrees format as a {@code BigDecimal}.
	 * Latitude values typically range between -90 and 90,
	 * where positive values denote locations in the Northern Hemisphere,
	 * and negative values denote locations in the Southern Hemisphere.
	 * <p>
	 * It is used throughout the {@code EarthCoordinates} class to store, manipulate,
	 * and convert latitude data into various formats such as degrees, minutes, and seconds (DMS).
	 * <p>
	 * This field is immutable and is initialized during the creation of an {@code EarthCoordinates} object.
	 */
	private final BigDecimal latitude;

	/**
	 * Represents the longitude component of a geographical coordinate.
	 * <p>
	 * The longitude value is stored as a {@code BigDecimal}, allowing for high precision
	 * when representing coordinates in decimal degrees format.
	 * <p>
	 * Longitude defines the east-west position of a point on the Earth's surface, with
	 * values ranging from -180° to 180°, where negative values indicate a westward direction,
	 * and positive values indicate an eastward direction.
	 * <p>
	 * This field is immutable and cannot be modified after the object is created.
	 */
	private final BigDecimal longitude;

	/**
	 * Specifies the format used to display the geographical coordinates.
	 * <p>
	 * This variable determines how the latitude and longitude values
	 * are represented when the coordinates are converted to a string
	 * representation or displayed to the user.
	 * <p>
	 * The available formats are defined by the {@link DisplayFormat} enumeration:
	 * <ul>
	 *     <li>{@code RAW}: Displays the raw data without any formatting.</li>
	 *     <li>{@code DECIMAL_DEGREES}: Formats the coordinates as decimal degrees with a degree symbol.</li>
	 *     <li>{@code DECIMAL_DEGREES_WITH_QUADRANT}: Formats the coordinates as decimal degrees with a degree symbol and directional quadrant (e.g., N, S, E, W).</li>
	 *     <li>{@code DEGREES_MINUTES_SECONDS}: Displays the coordinates in degrees, minutes, and seconds (DMS) format, along with the directional quadrant
	 *     (e.g., N, S, E, W).</li>
	 * </ul>
	 *
	 * This field is initialized to {@code DisplayFormat.RAW}.
	 */
	private DisplayFormat displayFormat = DisplayFormat.RAW;

	/**
	 * Defines the precision format for displaying geographic coordinate values.
	 * <p>
	 * This {@link DecimalFormat} instance is used to control the numerical representation
	 * of latitude and longitude in the {@link EarthCoordinates} class. It provides consistency in how
	 * coordinate values are displayed by ensuring that they adhere to a specific precision format.
	 * <p>
	 * The default precision format is initialized with {@link Constants#DEFAULT_PRECISION}, which
	 * rounds numbers to three decimal places and discards trailing zeroes.
	 *
	 * @see Constants#DEFAULT_PRECISION
	 * @see EarthCoordinates#setPrecision(DecimalFormat)
	 */
	private DecimalFormat precision = Constants.DEFAULT_PRECISION;

	/**
	 * Constructs an {@code EarthCoordinates} instance using the provided latitude and longitude
	 * values in String format, converting them to {@code BigDecimal}.
	 *
	 * @param latitude  the latitude coordinate as a String; must represent a valid numerical value
	 * @param longitude the longitude coordinate as a String; must represent a valid numerical value
	 */
	public EarthCoordinates(String latitude, String longitude) {
		this(new BigDecimal(latitude), new BigDecimal(longitude));
	}

	/**
	 * Constructs an {@code EarthCoordinates} instance using the provided latitude and longitude
	 * values in degrees, minutes, and seconds (DMS) format.
	 * <p>
	 * The provided latitude and longitude are specified as {@code OrderedTriplet} values, where:
	 * <ul>
	 *     <li>The first component represents degrees as an {@code Integer}.</li>
	 *     <li>The second component represents minutes as an {@code Integer}.</li>
	 *     <li>The third component represents seconds as a {@code BigDecimal}.</li>
	 * </ul>
	 *
	 * These values are internally converted to decimal degrees format, which is used
	 * to initialize the {@code EarthCoordinates} instance.
	 *
	 * @param latitude an {@code OrderedTriplet} representing the latitude in DMS format.
	 *                 Must not be null.
	 *                 The components are:
	 *                 <ul>
	 *                 	   <li>Degrees as an {@code Integer}.</li>
	 *                 	   <li>Minutes as an {@code Integer}.</li>
	 *                 	   <li>Seconds as a {@code BigDecimal}.</li>
	 *                 </ul>
	 * @param longitude an {@code OrderedTriplet} representing the longitude in DMS format.
	 *                  Must not be null.
	 *                  The components are:
	 *                  <ul>
	 *                      <li>Degrees as an {@code Integer}.</li>
	 *                 	    <li>Minutes as an {@code Integer}.</li>
	 *                 	    <li>Seconds as a {@code BigDecimal}.</li>
	 *                  </ul>
	 */
	public EarthCoordinates(OrderedTriplet<Integer, Integer, BigDecimal> latitude, OrderedTriplet<Integer, Integer, BigDecimal> longitude) {
		this(dmsToDecimalDegrees(latitude), dmsToDecimalDegrees(longitude));
	}

	/**
	 * Constructs an {@code EarthCoordinates} instance using the provided latitude
	 * and longitude coordinates in decimal degrees format.
	 *
	 * @param latitude the latitude coordinate as a {@code BigDecimal}; must not be null
	 * @param longitude the longitude coordinate as a {@code BigDecimal}; must not be null
	 */
	public EarthCoordinates(BigDecimal latitude, BigDecimal longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Determines the geographical quadrant (N, S, E, W) based on the sign of the specified coordinate.
	 * <p>
	 * This method evaluates whether the coordinate under consideration is latitude or longitude
	 * and returns the corresponding directional quadrant character:
	 * <ul>
	 *     <li>For longitude: 'W' for negative values, 'E' for non-negative values.</li>
	 *     <li>For latitude: 'S' for negative values, 'N' for non-negative values.</li>
	 * </ul>
	 *
	 * @param forLongitude a boolean indicating whether the method should determine the quadrant
	 *                    for longitude (true) or latitude (false).
	 * @return the quadrant character ('N', 'S', 'E', or 'W') based on the provided coordinate.
	 */
	private char getQuadrant(boolean forLongitude) {
		if (forLongitude) {
			return longitude.signum() == -1 ? 'W' : 'E';
		} else {
			return latitude.signum() == -1 ? 'S' : 'N';
		}
	}

	/**
	 * Retrieves the latitude component of the geographical coordinates.
	 *
	 * @return the latitude as a {@code BigDecimal}.
	 */
	public BigDecimal getLatitude() {
		return this.latitude;
	}

	/**
	 * Retrieves the longitude component of the geographical coordinates.
	 *
	 * @return the longitude as a {@code BigDecimal}.
	 */
	public BigDecimal getLongitude() {
		return this.longitude;
	}

	/**
	 * Converts the latitude component of the geographical coordinates from decimal degrees
	 * to degrees, minutes, and seconds (DMS) format.
	 * <p>
	 * This method uses the {@code decimalDegreesToDMS} utility to process the latitude stored
	 * in decimal degrees format, decomposing it into its respective degrees, minutes, and
	 * fractional seconds representation.
	 * The result is returned as an {@code OrderedTriplet}.
	 *
	 * @return an {@code OrderedTriplet} where:
	 *		   <ul>
	 *				<li>The first component represents degrees as an {@code Integer}.</li>
	 *				<li>The second component represents minutes as an {@code Integer}.</li>
	 *				<li>The third component represents seconds as a {@code BigDecimal}.</li>
	 *		   </ul>
	 *         The returned values represent the latitude in DMS format.
	 */
	public OrderedTriplet<Integer, Integer, BigDecimal> getLatitudeAsDMS() {
		return decimalDegreesToDMS(this.latitude);
	}

	/**
	 * Converts the longitude component of the geographical coordinates from decimal degrees
	 * to degrees, minutes, and seconds (DMS) format.
	 * <p>
	 * This method uses the {@code decimalDegreesToDMS} utility to process the longitude
	 * stored in decimal degrees format. It decomposes the longitude into its respective degrees,
	 * minutes, and fractional seconds representation.
	 * The result is returned as an {@code OrderedTriplet}.
	 *
	 * @return an {@code OrderedTriplet} where:
	 *		   <ul>
	 *				<li>The first component represents degrees as an {@code Integer}.</li>
	 *				<li>The second component represents minutes as an {@code Integer}.</li>
	 *				<li>The third component represents seconds as a {@code BigDecimal}.</li>
	 *		   </ul>
	 *         The returned values represent the longitude in DMS format.
	 */
	public OrderedTriplet<Integer, Integer, BigDecimal> getLongitudeAsDMS() {
		return decimalDegreesToDMS(this.longitude);
	}

	/**
	 * Retrieves the geographical coordinates as an ordered pair consisting of latitude and longitude.
	 *
	 * @return an {@code OrderedPair} where the first component is the latitude and the second component is the longitude.
	 *         Both components are represented as {@code BigDecimal}.
	 */
	public OrderedPair<BigDecimal, BigDecimal> getAsOrderedPair() {
		return new OrderedPair<>(this.latitude, this.longitude);
	}

	/**
	 * Sets the display format for the geographical coordinates.
	 *
	 * @param displayFormat the {@code DisplayFormat} to define how the coordinates
	 *                      should be represented.
	 *                      Must not be null.
	 */
	public void setDisplayFormat(DisplayFormat displayFormat) {
		this.displayFormat = displayFormat;
	}

	/**
	 * Sets the precision format for displaying numerical values in geographical coordinates.
	 *
	 * @param precision the {@code DecimalFormat} instance to define the precision level
	 *                  for formatting numerical output of coordinates; must not be null
	 */
	public void setPrecision(DecimalFormat precision) {
		this.precision = precision;
	}

	/**
	 * Returns a string representation of the geographical coordinates based on the current display format.
	 * <p>
	 * The format of the string is determined by the value of the {@code displayFormat} field:
	 * <ul>
	 *     <li>If {@code RAW}, it returns the ordered pair representation of latitude and longitude.</li>
	 *     <li>If {@code DECIMAL_DEGREES}, it formats the coordinates in decimal degrees.</li>
	 *     <li>If {@code DECIMAL_DEGREES_WITH_QUADRANT}, it formats the coordinates in decimal degrees along with quadrant indicators.</li>
	 *     <li>If {@code DEGREES_MINUTES_SECONDS}, it formats the coordinates in degrees, minutes, and seconds (DMS).</li>
	 * </ul>
	 *
	 * @return a string representation of the geographical coordinates formatted according to the current display format.
	 */
	@Override
	public String toString() {
		return switch (this.displayFormat) {
			case RAW -> this.getAsOrderedPair().toString();
			case DECIMAL_DEGREES -> formatAsDecimalDegrees();
			case DECIMAL_DEGREES_WITH_QUADRANT -> formatAsDecimalDegreesWithQuadrant();
			case DEGREES_MINUTES_SECONDS -> formatAsDegreesMinutesSeconds();
		};
	}

	/**
	 * Formats the latitude and longitude values of an object into a string representation
	 * in decimal degrees format, appending the degree symbol to each value.
	 *
	 * @return A string representing the latitude and longitude formatted as decimal degrees.
	 */
	private @NotNull String formatAsDecimalDegrees() {
		String latString = String.format("%s%c", precision.format(this.latitude), Constants.DEGREE_SYMBOL);
		String lonString = String.format("%s%c", precision.format(this.longitude), Constants.DEGREE_SYMBOL);
		return new OrderedPair<>(latString, lonString).toString();
	}

	/**
	 * Formats the latitude and longitude values of the current object
	 * into human-readable strings with decimal degrees and quadrant indicators.
	 * Each coordinate value is represented with a degree symbol and
	 * the corresponding cardinal direction (N, S, E, W).
	 *
	 * @return A non-null formatted string representation of the latitude
	 *         and longitude values as an ordered pair in decimal degree notation
	 *         with quadrant indicators.
	 */
	private @NotNull String formatAsDecimalDegreesWithQuadrant() {
		String latString = String.format("%s%c%c",
				precision.format(this.latitude.abs()),
				Constants.DEGREE_SYMBOL,
				this.getQuadrant(false));

		String lonString = String.format("%s%c%c",
				precision.format(this.longitude.abs()),
				Constants.DEGREE_SYMBOL,
				this.getQuadrant(true));

		return new OrderedPair<>(latString, lonString).toString();
	}

	/**
	 * Formats the latitude and longitude values of the current instance into
	 * Degrees, Minutes, and Seconds (DMS) notation.
	 * <p>
	 * Latitude is formatted as a non-directional string, while longitude includes a direction.
	 *
	 * @return A string representation of the formatted latitude and longitude as a DMS
	 *         notation pair in an ordered pair format.
	 */
	private @NotNull String formatAsDegreesMinutesSeconds() {
		String latString = formatDMS(this.getLatitudeAsDMS(), false);
		String lonString = formatDMS(this.getLongitudeAsDMS(), true);
		return new OrderedPair<>(latString, lonString).toString();
	}

	/**
	 * Formats a given DMS (Degrees, Minutes, Seconds) representation along with a quadrant indicator
	 * into a string representation.
	 *
	 * @param dms an OrderedTriplet where x represents degrees, y represents minutes, and z represents seconds
	 * @param isLongitude a boolean indicating if the DMS value is for longitude (true) or latitude (false)
	 * @return a formatted string representing the DMS value followed by the appropriate quadrant indicator
	 */
	private @NotNull String formatDMS(@NotNull OrderedTriplet<Integer, Integer, BigDecimal> dms, boolean isLongitude) {
		return String.format("%d%c %d' %s\"%c",
				dms.x(),
				Constants.DEGREE_SYMBOL,
				dms.y(),
				precision.format(dms.z()),
				this.getQuadrant(isLongitude));
	}
}
