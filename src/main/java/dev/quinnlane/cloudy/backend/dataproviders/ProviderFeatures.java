package dev.quinnlane.cloudy.backend.dataproviders;

/**
 * ProviderFeatures is an enumeration that defines a set of features supported by a provider.
 * Each feature is associated with a description that specifies the functionality or data type
 * that the feature supports.
 * <p>
 * Features in this enum are designed primarily for weather-related data
 * and include attributes such as temperature, precipitation, wind, and other atmospheric conditions.
 */
public enum ProviderFeatures {
	/**
	 * Represents a feature supporting live temperature readouts.
	 * This feature allows real-time access to temperature information,
	 * typically reflecting the current atmospheric or ambient temperature.
	 */
	LIVE_TEMPERATURE("Supports temperature readouts"),
	/**
	 * Represents a feature that supports high temperature readouts.
	 * This feature provides access to data related to high temperature
	 * measurements, which typically indicate the maximum temperature
	 * recorded or forecasted within a specific time frame or region.
	 */
	HIGH_TEMPERATURE("Supports high temperature readouts"),
	/**
	 * Represents a feature that supports low temperature readouts.
	 * This feature provides access to data related to low temperature
	 * measurements, which typically indicate the minimum temperature
	 * recorded or forecasted within a specific time frame or region.
	 */
	LOW_TEMPERATURE("Supports low temperature readouts"),
	/**
	 * Represents a feature that supports pressure readouts.
	 * This feature allows access to atmospheric pressure data,
	 * typically measured in units such as hPa, mmHg, or inHg,
	 * which can be used for meteorological analysis or forecasting.
	 */
	PRESSURE("Supports pressure readouts"),
	/**
	 * Represents a feature related to precipitation readouts in the associated provider.
	 * This feature includes support for accessing or providing information regarding
	 * precipitation levels, such as rain or snow, from the service.
	 * It is part of the available features described in the ProviderFeatures class.
	 */
	PRECIPITATION("Supports precipitation readouts"),
	/**
	 * Enumeration constant representing the feature of providing precipitation
	 * probability readouts. This can be used to determine the likelihood of
	 * precipitation occurring within a specified timeframe or location.
	 */
	PRECIPITATION_PROBABILITY("Supports precipitation probability readouts"),
	/**
	 * Represents the feature for retrieving sunrise times.
	 * This enumeration constant is used to indicate the availability
	 * of sunrise time data as a feature provided by the service.
	 */
	SUNRISE("Supports sunrise times"),
	/**
	 * Represents the feature related to the sunset time information.
	 * This enumeration constant is a part of the ProviderFeatures class and provides
	 * the ability to handle or describe the sunset time data.
	 */
	SUNSET("Supports sunset times"),
	/**
	 * Represents a feature that supports humidity readouts.
	 * Used to indicate the capability of providing humidity-related data.
	 */
	HUMIDITY("Supports humidity readouts"),
	/**
	 * Represents the feature for wind speed readouts.
	 * Provides information regarding the measurement or reporting of wind speed.
	 */
	WIND_SPEED("Supports wind speed readouts"),
	/**
	 * Represents the wind direction measured in degrees.
	 * The value is typically within the range of 0 to 360,
	 * where 0 or 360 indicates north, 90 indicates east,
	 * 180 indicates south, and 270 indicates west.
	 */
	WIND_DIRECTION_DEGREES("Reports wind direction in degrees"),
	/**
	 * A constant representing wind direction that is reported in only four cardinal directions:
	 * North, East, South, and West.
	 * This simplifies wind direction data by limiting it to the
	 * primary cardinal points.
	 */
	WIND_DIRECTION_CARDINALS_4("Only reports wind direction in cardinal directions"),
	/**
	 * WIND_DIRECTION_CARDINALS_8 represents the feature that reports wind direction
	 * using an 8-point compass system, including both cardinal and ordinal directions
	 * (e.g., N, NE, E, etc.).
	 */
	WIND_DIRECTION_CARDINALS_8("Reports wind direction in cardinal and ordinal directions"),
	/**
	 * Represents the wind direction in 16 cardinal, ordinal, and half-wind directions (e.g., N, NNE, NE, ENE, etc.).
	 * This provides a finer granularity compared to 4 or 8 directions.
	 */
	WIND_DIRECTION_CARDINALS_16("Reports wind direction in cardinal, ordinal, and half-wind directions"),
	/**
	 * Represents a wind direction feature with 32 points of the compass,
	 * including cardinal, ordinal, half-wind, and quarter-wind directions.
	 * Allows for detailed and precise reporting of a wind direction.
	 */
	WIND_DIRECTION_CARDINALS_32("Reports wind direction in cardinal and ordinal, along with half- and quarter-wind directions");

	/**
	 * A descriptive text explaining the specific functionality or data type supported
	 * by the provider feature represented by this enumeration constant.
	 */
	private final String description;

	/**
	 * Constructs a new ProviderFeatures enumeration constant with the specified description.
	 *
	 * @param description a String that represents the description of the feature provided by the enumeration constant
	 */
	ProviderFeatures(String description) {
		this.description = description;
	}

	/**
	 * Returns the description associated with this enumeration constant.
	 *
	 * @return a string representation of the description for the feature
	 */
	@Override
	public String toString() {
		return this.description;
	}
}
