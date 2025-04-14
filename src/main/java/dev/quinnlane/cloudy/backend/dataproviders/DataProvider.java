package dev.quinnlane.cloudy.backend.dataproviders;

import dev.quinnlane.cloudy.backend.Backend;
import dev.quinnlane.cloudy.common.datatypes.DailyForecast;
import dev.quinnlane.cloudy.common.datatypes.HourlyForecast;
import dev.quinnlane.cloudy.common.datatypes.OrderedPair;
import dev.quinnlane.cloudy.common.datatypes.OrderedTriplet;
import dev.quinnlane.cloudy.common.unit.units.CompassRose;
import dev.quinnlane.cloudy.common.unit.units.Pressure;
import dev.quinnlane.cloudy.common.unit.units.Temperature;
import dev.quinnlane.cloudy.common.unit.units.WindSpeed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.time.Instant;
import java.time.temporal.TemporalUnit;

/**
 * The DataProvider interface defines a contract for fetching weather and environmental
 * data from a specific source. It provides methods to get current conditions, forecasts,
 * and metadata about the data provider, as well as utilities for refreshing data and managing updates.
 */
public interface DataProvider {
	/**
	 * Determines if the provided data provider can be refreshed based on its refresh rate limit
	 * and the time elapsed since its last update.
	 *
	 * @param dataProvider the data provider to be checked for refresh eligibility. Must not be null.
	 * @return true if the data provider can be refreshed, otherwise false.
	 */
	static boolean canRefreshData(@NotNull DataProvider dataProvider) {
		Backend.logger.debug("Determining if data provider can be refreshed...");

		if (Instant.now().isBefore(dataProvider.getLastUpdate().plus(dataProvider.getRefreshRateLimit().x(), dataProvider.getRefreshRateLimit().y()))) {
			Backend.logger.error("Failed to refresh %s! It is too early to request a new refresh! Please wait before trying again.", dataProvider.getClass().getSimpleName());
			return false;
		}

		Backend.logger.info("Refreshing data for %s now...", dataProvider.getClass().getSimpleName());
		dataProvider.setLastUpdate(Instant.now());
		return true;
	}

	/**
	 * Retrieves detailed information about the data provider, including metadata
	 * such as type, class name, friendly name, owner, URL, and the last update timestamp.
	 *
	 * @return A {@link ProviderInformation} record encapsulating the provider's metadata.
	 */
	ProviderInformation getProviderInformation();

	/**
	 * Retrieves the set of features supported by the data provider.
	 *
	 * @return An array of {@link ProviderFeatures} enums representing the capabilities and
	 *         functionalities the data provider supports.
	 */
	ProviderFeatures[] getProviderFeatures();

	/**
	 * Retrieves the rate limit for manually refreshing data from the provider.
	 * The rate limit is represented as an {@link OrderedPair}, where the first element indicates the
	 * duration (as a {@link Long}) and the second element specifies the time unit (as a {@link TemporalUnit}).
	 *
	 * @return An {@link OrderedPair} containing the duration and temporal unit that define the refresh rate limit for the provider.
	 */
	OrderedPair<Long, TemporalUnit> getRefreshRateLimit();

	/**
	 * Retrieves the timestamp of the last update from the data provider.
	 *
	 * @return An {@link Instant} representing the last time the data provider was updated.
	 */
	Instant getLastUpdate();

	/**
	 * Sets the timestamp for the last update of the data provider.
	 *
	 * @param lastUpdate the {@link Instant} representing the time of the last update
	 */
	void setLastUpdate(Instant lastUpdate);

	/**
	 * Pulls the latest data from the provider.
	 * <p>
	 * This method makes a new GET request to the website and retrieves the latest data.
	 * This method will be called as the result of three events during an average runtime:
	 * <ul>
	 *     <li>When the user forces a refresh</li>
	 *     <li>When the user changes location (automatically called)</li>
	 *     <li>At a generic periodic interval specified in config</li>
	 * </ul>
	 */
	void refreshData();

	/**
	 * Gets the current temperature at the desired location.
	 *
	 * @return The current temperature reported by the data provider encapsulated in a {@link Temperature} object
	 */
	Temperature getCurrentTemperature();

	/**
	 * Retrieves the relative temperature at the desired location, which may be
	 * a comparison or feel-like temperature derived from the current conditions.
	 *
	 * @return The relative temperature as reported or calculated by the data provider,
	 *         encapsulated in a {@link Temperature} object.
	 */
	Temperature getRelativeTemperature();

	/**
	 * Gets the high temperature for the day at the desired location.
	 *
	 * @return The high temperature for the day reported by the data provider encapsulated in a {@link Temperature} object
	 */
	Temperature getHighTemperature();

	/**
	 * Gets the low temperature for the day at the desired location.
	 *
	 * @return The low temperature for the day reported by the data provider encapsulated in a {@link Temperature} object
	 */
	Temperature getLowTemperature();

	/**
	 * Gets the humidity as a percentage, in the form of an integer
	 *
	 * @return The humidity reported by the data provider as an integer
	 */
	int getHumidity();


	/**
	 * Gets the current wind details at the desired location.
	 * If the DataProvider does not support {@link ProviderFeatures#WIND_DIRECTION_DEGREES}, then
	 * the value of the most precise compass rose wind will be converted to degrees using
	 * {@link CompassRose#toAbsoluteBearing()}.
	 *
	 * @return An {@link OrderedTriplet} containing the wind's degree direction as a {@link Double},
	 *         its compass direction as a {@link CompassRose}, and its speed as a {@link WindSpeed}.
	 */
	OrderedTriplet<Double, CompassRose, WindSpeed> getWind();

	/**
	 * Retrieves the atmospheric pressure at the desired location.
	 *
	 * @return The atmospheric pressure reported by the data provider, encapsulated in a {@link Pressure} object.
	 */
	Pressure getPressure();

	/**
	 * Retrieves the UV index at the desired location.
	 * <p>
	 * The UV index is a standardized international measurement for the level of ultraviolet (UV) radiation exposure
	 * at the Earth's surface. It is commonly used to indicate the level of risk of harm from unprotected sun exposure.
	 * <p>
	 * The interpretation of the UV index is as follows:
	 * <ul>
	 *     <li><strong>0-2:</strong> Low risk. No protection required; you can safely stay outside.</li>
	 *     <li><strong>3-5:</strong> Moderate risk. Take precautions like wearing sunglasses and applying sunscreen.</li>
	 *     <li><strong>6-7:</strong> High risk. Protection is essential; wear a hat, sunglasses, and sunscreen.</li>
	 *     <li><strong>8-10:</strong> Very high risk. Minimize sun exposure during midday hours; use protective clothing and sunscreen.</li>
	 *     <li><strong>11+:</strong> Extreme risk. Avoid sun exposure as much as possible. Apply maximum protection.</li>
	 * </ul>
	 * The index value can theoretically extend to a very high threshold, as defined by {@link Long#MAX_VALUE},
	 * but common values typically range from 0 to around 11+.
	 *
	 * @return The UV index as an integer, where the value ranges from 0 to a Long.MAX_VALUE-defined threshold.
	 *         A higher UV index indicates elevated levels of ultraviolet radiation exposure.
	 */
	@Range(from = 0L, to = Integer.MAX_VALUE)
	int getUVIndex();

	/**
	 * Retrieves the Air Quality Index (AQI) for the desired location.
	 * <p>
	 * The Air Quality Index (AQI) is a standardized measurement used to communicate the level of
	 * air pollution and its potential impact on human health. It aggregates data from key air pollutants,
	 * such as ground-level ozone, particulate matter (PM10 and PM2.5), carbon monoxide, sulfur dioxide,
	 * and nitrogen dioxide, into a single scale. The index provides public guidance on the level of
	 * air quality and the associated health risks.
	 * <p>
	 * The interpretation of AQI levels is as follows:
	 * <ul>
	 *     <li><strong>0-50 (Good):</strong> Air quality is satisfactory, and air pollution poses little or no risk.</li>
	 *     <li><strong>51-100 (Moderate):</strong> Air quality is acceptable; however, some pollutants may be a concern
	 *         for sensitive groups such as individuals with respiratory conditions.</li>
	 *     <li><strong>101-150 (Unhealthy for Sensitive Groups):</strong> Members of sensitive groups may begin to
	 *         experience health effects; the general public is unlikely to be affected.</li>
	 *     <li><strong>151-200 (Unhealthy):</strong> Everyone may begin to experience health effects, and members of
	 *         sensitive groups may experience more serious effects.</li>
	 *     <li><strong>201-300 (Very Unhealthy):</strong> Health alert: everyone may experience more serious health effects.</li>
	 *     <li><strong>301-500 (Hazardous):</strong> Health warning: emergency conditions in which the entire population
	 *         is more likely to be affected. Avoid outdoor activities.</li>
	 * </ul>
	 * Note that while the AQI scale is standardized between 0 and 500, values beyond 500 are possible in extreme pollution conditions,
	 * as defined by some reporting systems. These "beyond index" values represent exceptionally hazardous air quality levels.
	 *
	 * @return The Air Quality Index as a non-negative integer. A higher AQI value indicates more severe air pollution
	 * and an increased risk to health.
	 */
	@Range(from = 0L, to = Integer.MAX_VALUE)
	int getAirQualityIndex();

	/**
	 * Retrieves the sunrise time for the desired location from the data provider.
	 *
	 * @return An {@link Instant} representing the time of sunrise at the specified location.
	 */
	Instant getSunrise();

	/**
	 * Retrieves the time of sunset as an Instant.
	 *
	 * @return an Instant object representing the time of sunset
	 */
	Instant getSunset();

	/**
	 * Retrieves the hourly weather forecast data.
	 *
	 * @return An array of {@link HourlyForecast} records, where each record represents
	 *         detailed weather information for a specific hour, including temperature,
	 *         relative temperature, precipitation, wind, cloud cover, and humidity.
	 */
	HourlyForecast[] getHourlyForecast();

	/**
	 * Retrieves the daily weather forecast data.
	 *
	 * @return An array of {@link DailyForecast} objects, where each object represents
	 *         detailed weather data for a specific day, including temperature, precipitation,
	 *         wind, and other meteorological conditions.
	 */
	DailyForecast[] getDailyForecast();
}
