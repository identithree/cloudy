package dev.quinnlane.cloudy.common.datagen;

import dev.quinnlane.cloudy.common.datatypes.OrderedPair;

/**
 * An enumeration representing predefined data configurations with associated value ranges
 * and properties indicating whether the associated values are floating-point numbers.
 * <p>
 * Each data preset includes a specific value range and a flag denoting its numeric type.
 * The presets serve as convenient templates for commonly used data configurations.
 */
public enum DataPresets {
	TEMPERATURE_COLD(new OrderedPair<>(-6d, 2d), true),
	TEMPERATURE_COOL(new OrderedPair<>(2d, 12d), true),
	TEMPERATURE_WARM(new OrderedPair<>(12d, 20d), true),
	TEMPERATURE_HOT(new OrderedPair<>(20d, 32d), true),
	HUMIDITY_LOW(new OrderedPair<>(0d, 30d), false),
	HUMIDITY_MEDIUM(new OrderedPair<>(30d, 70d), false),
	HUMIDITY_HIGH(new OrderedPair<>(70d, 100d), false),
	WIND_DIRECTION(new OrderedPair<>(0d, 360d), false, "#.##"),
	WIND_SPEED_SLOW(new OrderedPair<>(0d, 6d), false),
	WIND_SPEED_MEDIUM(new OrderedPair<>(7d, 15d), false),
	WIND_SPEED_FAST(new OrderedPair<>(16d, 27d), false),
	PRESSURE_LOW(new OrderedPair<>(989.634, 1009.144), true, "#.###"),
	PRESSURE_NORMAL(new OrderedPair<>(1009.144, 1022.689), true, "#.###"),
	PRESSURE_HIGH(new OrderedPair<>(1022.689, 1036.234), true, "#.###"),
	UV_INDEX_LOW(new OrderedPair<>(0d, 2d), false),
	UV_INDEX_MEDIUM(new OrderedPair<>(2d, 5d), false),
	UV_INDEX_HIGH(new OrderedPair<>(5d, 7d), false),
	UV_INDEX_VERY_HIGH(new OrderedPair<>(7d, 11d), false),
	UV_INDEX_EXTREME(new OrderedPair<>(11d, 13d), false),
	AIR_QUALITY_GOOD(new OrderedPair<>(0d, 50d), false),
	AIR_QUALITY_MODERATE(new OrderedPair<>(50d, 100d), false),
	AIR_QUALITY_UNHEALTHY_FOR_SENSITIVE_GROUPS(new OrderedPair<>(100d, 150d), false),
	AIR_QUALITY_UNHEALTHY(new OrderedPair<>(150d, 200d), false),
	AIR_QUALITY_VERY_UNHEALTHY(new OrderedPair<>(200d, 300d), false),
	AIR_QUALITY_HAZARDOUS(new OrderedPair<>(300d, 500d), false),
	AIR_QUALITY_EXTREMELY_HAZARDOUS(new OrderedPair<>(500d, 1000d), false),
	PRECIPITATION_CHANCE_LOW(new OrderedPair<>(1d, 25d), false),
	PRECIPITATION_CHANCE_MEDIUM(new OrderedPair<>(25d, 50d), false),
	PRECIPITATION_CHANCE_HIGH(new OrderedPair<>(50d, 75d), false),
	PRECIPITATION_CHANCE_CERTAIN(new OrderedPair<>(75d, 100d), false),
	PRECIPITATION_AMOUNT_LOW(new OrderedPair<>(0d, 0.1), true, "#.##"),
	PRECIPITATION_AMOUNT_MEDIUM(new OrderedPair<>(0.1, 0.5), true, "#.##"),
	PRECIPITATION_AMOUNT_HIGH(new OrderedPair<>(0.5, 1d), true, "#.##"),
	CLOUD_COVER_LOW(new OrderedPair<>(0d, 25d), false),
	CLOUD_COVER_MEDIUM(new OrderedPair<>(25d, 75d), false),
	CLOUD_COVER_HIGH(new OrderedPair<>(75d, 100d), false),
	SUNRISE_HOUR(new OrderedPair<>(5d, 7d), false),
	SUNSET_HOUR(new OrderedPair<>(19d, 21d), false),
	TIME_MINUTE(new OrderedPair<>(0d, 60d), false);

	/**
	 * A constant representing the default precision format for numeric values.
	 * This format is used to determine how numerical values are rounded
	 * and displayed, particularly when working with floating-point numbers.
	 * The default format "#.#" ensures that numbers are rounded to one
	 * decimal place.
	 */
	private static final String DEFAULT_PRECISION = "#.#";

	/**
	 * Represents a pair of double values defining a range for a specific preset.
	 * The range indicates the minimum and maximum bounds associated
	 * with the particular data preset configuration.
	 * <p>
	 * When {@link #isFloatingPoint} is set to true, the upper bound is exclusive,
	 * but when set to false, the upper bound is inclusive.
	 */
	private final OrderedPair<Double, Double> range;

	/**
	 * Indicates whether the values associated with this data preset are floating-point numbers.
	 * This variable is used to identify if the range values for the preset should be treated
	 * as precise decimal values (floating-point) or as whole numbers.
	 */
	private final boolean isFloatingPoint;

	/**
	 * Represents the precision associated with this data preset.
	 * This field defines the level of detail or granularity used in the values
	 * represented by the preset, typically formatted as a string (e.g., number of decimal places).
	 * <p>
	 * The precision value is set during object construction and is immutable.
	 */
	private final String precision;

	/**
	 * Constructs a new DataPresets instance with the specified range and floating-point property,
	 * using the default precision.
	 *
	 * @param range           An OrderedPair of Double values representing the range of values
	 *                        (minimum and maximum) associated with this data preset.
	 * @param isFloatingPoint A boolean indicating whether the values in this data preset are
	 *                        floating-point numbers.
	 */
	DataPresets(OrderedPair<Double, Double> range, boolean isFloatingPoint) {
		this(range, isFloatingPoint, DEFAULT_PRECISION);
	}

	/**
	 * Constructs a new DataPresets instance with the specified range, floating-point property,
	 * and precision.
	 *
	 * @param range           An OrderedPair of Double values representing the range of values
	 *                        (minimum and maximum) associated with this data preset.
	 * @param isFloatingPoint A boolean indicating whether the values in this data preset are
	 *                        floating-point numbers.
	 * @param precision       A String representing the numerical precision or format for the
	 *                        floating-point values.
	 */
	DataPresets(OrderedPair<Double, Double> range, boolean isFloatingPoint, String precision) {
		this.range = range;
		this.isFloatingPoint = isFloatingPoint;
		this.precision = precision;
	}

	/**
	 * Retrieves the minimum value from the range associated with this data preset.
	 *
	 * @return The minimum value of the range as a double.
	 */
	public double getMinimum() {
		return this.range.x();
	}

	/**
	 * Retrieves the maximum value from the range associated with this data preset.
	 *
	 * @return The maximum value of the range as a double.
	 */
	public double getMaximum() {
		return this.range.y();
	}

	/**
	 * Determines if the values associated with this data preset are floating-point numbers.
	 *
	 * @return {@code true} if the values are floating-point numbers; {@code false} otherwise.
	 */
	public boolean isFloatingPoint() {
		return this.isFloatingPoint;
	}

	/**
	 * Retrieves the precision associated with this data preset.
	 *
	 * @return The precision as a string.
	 */
	public String getPrecision() {
		return this.precision;
	}
}
