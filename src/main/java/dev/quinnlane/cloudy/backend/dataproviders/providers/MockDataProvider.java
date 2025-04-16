package dev.quinnlane.cloudy.backend.dataproviders.providers;

import dev.quinnlane.cloudy.backend.Backend;
import dev.quinnlane.cloudy.backend.dataproviders.DataProvider;
import dev.quinnlane.cloudy.backend.dataproviders.ProviderFeatures;
import dev.quinnlane.cloudy.backend.dataproviders.ProviderInformation;
import dev.quinnlane.cloudy.backend.dataproviders.ProviderType;
import dev.quinnlane.cloudy.common.configuration.MockConfiguration;
import dev.quinnlane.cloudy.common.datagen.DataPresets;
import dev.quinnlane.cloudy.common.datagen.MockDataGenerator;
import dev.quinnlane.cloudy.common.datatypes.DailyForecast;
import dev.quinnlane.cloudy.common.datatypes.HourlyForecast;
import dev.quinnlane.cloudy.common.datatypes.OrderedPair;
import dev.quinnlane.cloudy.common.datatypes.OrderedTriplet;
import dev.quinnlane.cloudy.common.unit.UnitLocales;
import dev.quinnlane.cloudy.common.unit.units.*;
import org.jetbrains.annotations.Range;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;

public class MockDataProvider implements DataProvider {
	/**
	 * A constant variable that represents the information about a specific provider.
	 * This includes the provider's type, name, description, functionality, documentation URI,
	 * and the date this provider information was created or last updated.
	 */
	public static final ProviderInformation INFORMATION = new ProviderInformation(ProviderType.MOCK, "MockDataProvider", "Mock Data Provider", "pseudo-random numbers", Backend.stringToURI("https://docs.oracle.com/en/java/javase/23/docs/api/index.html"), Backend.stringToLocalDateTime("2025-04-13T17:23:00"));

	/**
	 * An array of {@link ProviderFeatures} constants representing the features provided by the system.
	 * These features typically include various weather-related elements such as temperature, precipitation,
	 * wind information, and solar events.
	 * <p>
	 * Features included:
	 * <ul>
	 *     <li>LIVE_TEMPERATURE: Current temperature reading.</li>
	 *     <li>HIGH_TEMPERATURE: Forecasted or recorded high temperature.</li>
	 *     <li>LOW_TEMPERATURE: Forecasted or recorded low temperature.</li>
	 *     <li>PRECIPITATION: Current or forecasted precipitation level.</li>
	 *     <li>PRECIPITATION_PROBABILITY: Probability of precipitation occurring.</li>
	 *     <li>SUNRISE: Time of sunrise.</li>
	 *     <li>SUNSET: Time of sunset.</li>
	 *     <li>WIND_SPEED: Current or forecasted wind speed.</li>
	 *     <li>WIND_DIRECTION_DEGREES: Wind direction in degrees.</li>
	 *     <li>WIND_DIRECTION_CARDINALS_32: Wind direction in 32 cardinal points.</li>
	 * </ul>
	 */
	public static final ProviderFeatures[] FEATURES = new ProviderFeatures[] {
			ProviderFeatures.LIVE_TEMPERATURE,
			ProviderFeatures.HIGH_TEMPERATURE,
			ProviderFeatures.LOW_TEMPERATURE,
			ProviderFeatures.PRECIPITATION,
			ProviderFeatures.PRECIPITATION_PROBABILITY,
			ProviderFeatures.SUNRISE,
			ProviderFeatures.SUNSET,
			ProviderFeatures.WIND_SPEED,
			ProviderFeatures.WIND_DIRECTION_DEGREES,
			ProviderFeatures.WIND_DIRECTION_CARDINALS_32
	};

	/**
	 * A constant that represents the rate limit configuration for a particular operation.
	 * The rate limit is defined as an ordered pair of a numerical value and the corresponding temporal unit.
	 * The numerical value specifies the maximum number of operations allowed within the given unit of time.
	 * A value of -1 indicates no rate limiting is enforced.
	 * The temporal unit specifies the duration for which the numerical value is applicable.
	 */
	public static final OrderedPair<Long, TemporalUnit> RATE_LIMIT = new OrderedPair<>(-1L, ChronoUnit.SECONDS);

	/**
	 * A constant that represents the timestamp of the last update.
	 * It is initialized to the epoch constant, which represents
	 * the beginning of the Unix epoch time (1970-01-01T00:00:00Z).
	 * This variable is updated every time {@link #refreshData()}
	 * is successfully called and is used to determine if the provider
	 * can be polled for new data.
	 */
	public static Instant LAST_UPDATE = Instant.EPOCH;

	/**
	 * Represents the current temperature reading of a specific environment or system.
	 * This variable is used to store and track the current temperature data,
	 * typically provided by a temperature sensor or other data source.
	 */
	private Temperature currentTemperature;

	/**
	 * Represents the temperature difference relative to a baseline or reference temperature.
	 * This variable is used to store the temperature variation in a context where a comparison
	 * to a standard or expected value is necessary.
	 */
	private Temperature relativeTemperature;

	/**
	 * Represents the high temperature measurement.
	 * This variable is of type Temperature and is used to store
	 * the highest recorded or expected temperature value.
	 */
	private Temperature highTemperature;

	/**
	 * Represents the lowest temperature recorded or allowed in a specific context.
	 * This variable holds a reference to a Temperature object, which provides detailed
	 * information about a specific temperature value. It is used to manage or compare
	 * temperature thresholds where a lower boundary is important.
	 */
	private Temperature lowTemperature;

	/**
	 * Represents the humidity level as a percentage.
	 * The value typically ranges from 0 to 100, where 0 indicates no humidity
	 * and 100 represents maximum saturation.
	 */
	private int humidity;

	/**
	 * Represents the wind conditions using an ordered triplet.
	 * The triplet consists of:
	 * <ol>
	 *     <li>The wind direction in degrees as a Double.</li>
	 *     <li>The compass rose direction as a CompassRose instance.</li>
	 *     <li>The wind speed as a WindSpeed instance.</li>
	 * </ol>
	 * This variable is designed to encapsulate all relevant
	 * information about the wind's state at a given moment.
	 */
	private OrderedTriplet<Double, CompassRose, WindSpeed> wind;

	/**
	 * Represents the current pressure value in a given context.
	 * This variable holds an instance of the Pressure class, which may represent
	 * atmospheric, fluid, or other forms of pressure, depending on the application's use case.
	 * The specific unit of measurement (e.g., Pascals, Bar) and behavior
	 * depend on the implementation of the Pressure class.
	 */
	private Pressure pressure;

	/**
	 * Represents the ultraviolet (UV) index, which is a measure of the
	 * level of UV radiation from the sun. It is typically used to assess
	 * the risk of harm from unprotected sun exposure and is expressed
	 * as an integer value. Higher values indicate greater risk.
	 */
	private int uvIndex;

	/**
	 * Represents the air quality index (AQI) value which is a numerical scale used
	 * to indicate the level of air pollution in a particular area.
	 * <p>
	 * This variable typically corresponds to an integer value that provides
	 * information about how clean or polluted the air currently is, and highlights
	 * possible health effects of the pollution level.
	 */
	private int airQualityIndex;

	/**
	 * Represents the time of sunrise.
	 * This variable holds the moment when the sun rises, expressed as an Instant object.
	 * It is typically used to represent the start of daylight for a particular location and date.
	 */
	private Instant sunrise;

	/**
	 * Represents the time of sunset.
	 * This variable holds the moment when the sun sets, typically expressed as an {@code Instant}.
	 * It can be used to track or calculate durations relative to the sunset time.
	 */
	private Instant sunset;

	/**
	 * An array of HourlyForecast objects representing the weather forecast for
	 * specific hours within a day. Each element in the array contains information
	 * about weather conditions, such as temperature, humidity, precipitation,
	 * and other forecast details for a particular hour.
	 */
	private HourlyForecast[] hourlyForecast;

	/**
	 * Represents an array of DailyForecast objects that stores weather forecast
	 * information for multiple days.
	 * <p>
	 * This variable is used to manage and access daily forecast data typically
	 * in the context of a weather application or service.
	 */
	private DailyForecast[] dailyForecast;

	/**
	 * Retrieves detailed information about the data provider, including metadata
	 * such as type, class name, friendly name, owner, URL, and the last update timestamp.
	 *
	 * @return A {@link ProviderInformation} record encapsulating the provider's metadata.
	 */
	@Override
	public ProviderInformation getProviderInformation() {
		return INFORMATION;
	}

	/**
	 * Retrieves the set of features supported by the data provider.
	 *
	 * @return An array of {@link ProviderFeatures} enums representing the capabilities and
	 * functionalities the data provider supports.
	 */
	@Override
	public ProviderFeatures[] getProviderFeatures() {
		return FEATURES;
	}

	/**
	 * Retrieves the rate limit for manually refreshing data from the provider.
	 * The rate limit is represented as an {@link OrderedPair}, where the first element indicates the
	 * duration (as a {@link Long}) and the second element specifies the time unit (as a {@link TemporalUnit}).
	 *
	 * @return An {@link OrderedPair} containing the duration and temporal unit that define the refresh rate limit for the provider.
	 */
	@Override
	public OrderedPair<Long, TemporalUnit> getRefreshRateLimit() {
		return RATE_LIMIT;
	}

	/**
	 * Retrieves the timestamp of the last update from the data provider.
	 *
	 * @return An {@link Instant} representing the last time the data provider was updated.
	 */
	@Override
	public Instant getLastUpdate() {
		return LAST_UPDATE;
	}

	/**
	 * Sets the timestamp for the last update of the data provider.
	 *
	 * @param lastUpdate the {@link Instant} representing the time of the last update
	 */
	@Override
	public void setLastUpdate(Instant lastUpdate) {
		LAST_UPDATE = lastUpdate;
	}

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
	@Override
	public void refreshData() {
		// Check if data can be refreshed
		if (!DataProvider.canRefreshData(this)) return;

		// Temperature
		this.currentTemperature = new Temperature((Double) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__WARMTH), MockConfiguration.CUSTOMIZATION__LOCALE);
		this.relativeTemperature = new Temperature(this.currentTemperature.getValue(UnitLocales.SI) - 2d, MockConfiguration.CUSTOMIZATION__LOCALE);
		this.highTemperature = new Temperature(this.currentTemperature.getValue(UnitLocales.SI) + 7d, MockConfiguration.CUSTOMIZATION__LOCALE);
		this.lowTemperature = new Temperature((Double) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__COLDNESS), MockConfiguration.CUSTOMIZATION__LOCALE);

		// Humidity
		this.humidity = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__HUMIDITY);

		// Wind
		double windDirection = (int) MockDataGenerator.generateData(DataPresets.WIND_DIRECTION);
		this.wind = new OrderedTriplet<>(windDirection, CompassRose.fromBearing(windDirection, MockConfiguration.CUSTOMIZATION__MAX_WIND_DIRECTION_SPECIFICITY), new WindSpeed((Integer) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__WIND_SPEED), MockConfiguration.CUSTOMIZATION__LOCALE));

		// Pressure
		this.pressure = new Pressure((Double) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRESSURE), MockConfiguration.CUSTOMIZATION__LOCALE);

		// UV Index
		this.uvIndex = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__UV_INDEX);

		// Air Quality Index
		this.airQualityIndex = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__AIR_QUALITY);

		this.sunrise = Instant.now()
				.minus(Calendar.HOUR_OF_DAY, ChronoUnit.HOURS)
				.minus(Calendar.MINUTE, ChronoUnit.MINUTES)
				.minus(Calendar.SECOND, ChronoUnit.SECONDS)
				.plus((Integer) MockDataGenerator.generateData(DataPresets.SUNRISE_HOUR), ChronoUnit.HOURS)
				.plus((Integer) MockDataGenerator.generateData(DataPresets.TIME_MINUTE), ChronoUnit.MINUTES);

		this.sunset = Instant.now()
				.minus(Calendar.HOUR_OF_DAY, ChronoUnit.HOURS)
				.minus(Calendar.MINUTE, ChronoUnit.MINUTES)
				.minus(Calendar.SECOND, ChronoUnit.SECONDS)
				.plus((Integer) MockDataGenerator.generateData(DataPresets.SUNSET_HOUR), ChronoUnit.HOURS)
				.plus((Integer) MockDataGenerator.generateData(DataPresets.TIME_MINUTE), ChronoUnit.MINUTES);

		// Hourly Forecast
		ArrayList<HourlyForecast> hourlyForecastList = new ArrayList<>();
		for (int i = 0; i < MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__HOURLY_FORECAST_COUNT; i++) {
			OrderedPair<Integer, PrecipitationAmount> precipitation = MockDataGenerator.generatePrecipitationData();

			int cloudCover = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__CLOUD_COVER);

			if (i == 0) {
				hourlyForecastList.add(new HourlyForecast(this.currentTemperature, this.relativeTemperature, precipitation, this.wind, cloudCover, this.humidity));
				continue;
			}

			Temperature temperature = new Temperature((Double) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__WARMTH), MockConfiguration.CUSTOMIZATION__LOCALE);
			Temperature relativeTemperature = new Temperature(temperature.getValue(UnitLocales.SI) - 2d, MockConfiguration.CUSTOMIZATION__LOCALE);

			double hourlyWindDirection = (int) MockDataGenerator.generateData(DataPresets.WIND_DIRECTION);
			OrderedTriplet<Double, CompassRose, WindSpeed> wind = new OrderedTriplet<>(hourlyWindDirection, CompassRose.fromBearing(hourlyWindDirection, MockConfiguration.CUSTOMIZATION__MAX_WIND_DIRECTION_SPECIFICITY), new WindSpeed((Integer) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__WIND_SPEED), MockConfiguration.CUSTOMIZATION__LOCALE));

			int humidity = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__HUMIDITY);

			hourlyForecastList.add(new HourlyForecast(temperature, relativeTemperature, precipitation, wind, cloudCover, humidity));
		}
		this.hourlyForecast = hourlyForecastList.toArray(new HourlyForecast[0]);

		// Daily Forecast
		ArrayList<DailyForecast> dailyForecastList = new ArrayList<>();
		for (int i = 0; i < MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__DAILY_FORECAST_COUNT; i++) {
			OrderedPair<Integer, PrecipitationAmount> precipitation = MockDataGenerator.generatePrecipitationData();

			int cloudCover = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__CLOUD_COVER);

			if (i == 0) {
				dailyForecastList.add(new DailyForecast(this.highTemperature, this.lowTemperature, precipitation, this.wind, cloudCover, this.humidity, this.uvIndex, this.sunrise, this.sunset));
				continue;
			}

			Temperature highTemperature = new Temperature((Double) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__WARMTH) + 7d, MockConfiguration.CUSTOMIZATION__LOCALE);
			Temperature lowTemperature = new Temperature((Double) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__COLDNESS), MockConfiguration.CUSTOMIZATION__LOCALE);

			double dailyWindDirection = (int) MockDataGenerator.generateData(DataPresets.WIND_DIRECTION);
			OrderedTriplet<Double, CompassRose, WindSpeed> wind = new OrderedTriplet<>(dailyWindDirection, CompassRose.fromBearing(dailyWindDirection, MockConfiguration.CUSTOMIZATION__MAX_WIND_DIRECTION_SPECIFICITY), new WindSpeed((Integer) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__WIND_SPEED), MockConfiguration.CUSTOMIZATION__LOCALE));

			int humidity = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__HUMIDITY);

			int uvIndex = (int) MockDataGenerator.generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__UV_INDEX);

			Instant sunrise = Instant.now()
					.minus(Calendar.HOUR_OF_DAY, ChronoUnit.HOURS)
					.minus(Calendar.MINUTE, ChronoUnit.MINUTES)
					.minus(Calendar.SECOND, ChronoUnit.SECONDS)
					.plus(i, ChronoUnit.DAYS)
					.plus((Integer) MockDataGenerator.generateData(DataPresets.SUNRISE_HOUR), ChronoUnit.HOURS)
					.plus((Integer) MockDataGenerator.generateData(DataPresets.TIME_MINUTE), ChronoUnit.MINUTES);

			Instant sunset = Instant.now()
					.minus(Calendar.HOUR_OF_DAY, ChronoUnit.HOURS)
					.minus(Calendar.MINUTE, ChronoUnit.MINUTES)
					.minus(Calendar.SECOND, ChronoUnit.SECONDS)
					.plus(i, ChronoUnit.DAYS)
					.plus((Integer) MockDataGenerator.generateData(DataPresets.SUNSET_HOUR), ChronoUnit.HOURS)
					.plus((Integer) MockDataGenerator.generateData(DataPresets.TIME_MINUTE), ChronoUnit.MINUTES);

			dailyForecastList.add(new DailyForecast(highTemperature, lowTemperature, precipitation, wind, cloudCover, humidity, uvIndex, sunrise, sunset));
		}
		this.dailyForecast = dailyForecastList.toArray(new DailyForecast[0]);

		Backend.logger.info("Data has been successfully refreshed for %s.", getClass().getSimpleName());
	}

	/**
	 * Gets the current temperature at the desired location.
	 *
	 * @return The current temperature reported by the data provider encapsulated in a {@link Temperature} object
	 */
	@Override
	public Temperature getCurrentTemperature() {
		return this.currentTemperature;
	}

	/**
	 * Retrieves the relative temperature at the desired location, which may be
	 * a comparison or feel-like temperature derived from the current conditions.
	 *
	 * @return The relative temperature as reported or calculated by the data provider,
	 * encapsulated in a {@link Temperature} object.
	 */
	@Override
	public Temperature getRelativeTemperature() {
		return this.relativeTemperature;
	}

	/**
	 * Gets the high temperature for the day at the desired location.
	 *
	 * @return The high temperature for the day reported by the data provider encapsulated in a {@link Temperature} object
	 */
	@Override
	public Temperature getHighTemperature() {
		return this.highTemperature;
	}

	/**
	 * Gets the low temperature for the day at the desired location.
	 *
	 * @return The low temperature for the day reported by the data provider encapsulated in a {@link Temperature} object
	 */
	@Override
	public Temperature getLowTemperature() {
		return this.lowTemperature;
	}

	/**
	 * Gets the humidity as a percentage, in the form of an integer
	 *
	 * @return The humidity reported by the data provider as an integer
	 */
	@Override
	public int getHumidity() {
		return this.humidity;
	}

	/**
	 * Gets the current wind details at the desired location.
	 * If the DataProvider does not support {@link ProviderFeatures#WIND_DIRECTION_DEGREES}, then
	 * the value of the most precise compass rose wind will be converted to degrees using
	 * {@link CompassRose#toAbsoluteBearing()}.
	 *
	 * @return An {@link OrderedTriplet} containing the wind's degree direction as a {@link Double},
	 * its compass direction as a {@link CompassRose}, and its speed as a {@link WindSpeed}.
	 */
	@Override
	public OrderedTriplet<Double, CompassRose, WindSpeed> getWind() {
		return this.wind;
	}

	/**
	 * Retrieves the atmospheric pressure at the desired location.
	 *
	 * @return The atmospheric pressure reported by the data provider, encapsulated in a {@link Pressure} object.
	 */
	@Override
	public Pressure getPressure() {
		return this.pressure;
	}

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
	 * A higher UV index indicates elevated levels of ultraviolet radiation exposure.
	 */
	@Override
	public @Range(from = 0L, to = Integer.MAX_VALUE) int getUVIndex() {
		return this.uvIndex;
	}

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
	@Override
	public @Range(from = 0L, to = Integer.MAX_VALUE) int getAirQualityIndex() {
		return this.airQualityIndex;
	}

	/**
	 * Retrieves the sunrise time for the desired location from the data provider.
	 *
	 * @return An {@link Instant} representing the time of sunrise at the specified location.
	 */
	@Override
	public Instant getSunrise() {
		return this.sunrise;
	}

	/**
	 * Retrieves the time of sunset as an Instant.
	 *
	 * @return an Instant object representing the time of sunset
	 */
	@Override
	public Instant getSunset() {
		return this.sunset;
	}

	/**
	 * Retrieves the hourly weather forecast data.
	 *
	 * @return An array of {@link HourlyForecast} records, where each record represents
	 * detailed weather information for a specific hour, including temperature,
	 * relative temperature, precipitation, wind, cloud cover, and humidity.
	 */
	@Override
	public HourlyForecast[] getHourlyForecast() {
		return this.hourlyForecast;
	}

	/**
	 * Retrieves the daily weather forecast data.
	 *
	 * @return An array of {@link DailyForecast} objects, where each object represents
	 * detailed weather data for a specific day, including temperature, precipitation,
	 * wind, and other meteorological conditions.
	 */
	@Override
	public DailyForecast[] getDailyForecast() {
		return this.dailyForecast;
	}
}
