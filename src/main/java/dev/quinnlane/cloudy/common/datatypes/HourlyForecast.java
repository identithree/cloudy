package dev.quinnlane.cloudy.common.datatypes;

import dev.quinnlane.cloudy.common.unit.units.CompassRose;
import dev.quinnlane.cloudy.common.unit.units.PrecipitationAmount;
import dev.quinnlane.cloudy.common.unit.units.Temperature;
import dev.quinnlane.cloudy.common.unit.units.WindSpeed;

/**
 * Represents an hourly weather forecast with detailed information about temperature,
 * precipitation, wind conditions, cloud cover, and humidity.
 * <p>
 * This immutable record provides a comprehensive set of weather data for a specific hour.
 * Each aspect of the weather is represented with fine-grained data types designed for
 * clear representation and unit conversions where applicable.
 * <p>
 * The components of the forecast include:
 * <ul>
 *     <li>Temperature: The current ambient temperature.</li>
 *     <li>Relative Temperature: A perceived temperature considering additional factors such as wind chill.</li>
 *     <li>Precipitation: The expected amount and probability of precipitation, represented as an ordered pair of percentage and amount.</li>
 *     <li>Wind: A combination of details such as wind direction, speed, and the associated compass direction.</li>
 *     <li>Cloud Cover: The percentage of cloud cover.</li>
 *     <li>Humidity: The relative humidity percentage.</li>
 * </ul>
 * <p>
 * The record is designed with reusability and extensibility in mind, leveraging other strongly typed classes
 * for unit-based measurements such as Temperature and PrecipitationAmount.
 *
 * @param temperature         The current ambient temperature at the forecasted time.
 * @param relativeTemperature The perceived temperature considering factors such as wind chill.
 * @param precipitation        An ordered pair consisting of the precipitation probability (as an Integer percentage)
 *                             and the precipitation amount (as a PrecipitationAmount).
 * @param wind                An ordered triplet representing the wind's intensity and direction:
 *                            the wind's speed (as a Double), the compass direction (as a CompassRose),
 *                            and the wind speed (as a WindSpeed).
 * @param cloudCover          The percentage of the sky covered by clouds (represented as an Integer, ranging from 0 to 100).
 * @param humidity            The relative humidity as a percentage (represented as an Integer).
 */
public record HourlyForecast(Temperature temperature, Temperature relativeTemperature, OrderedPair<Integer, PrecipitationAmount> precipitation, OrderedTriplet<Double, CompassRose, WindSpeed> wind, int cloudCover, int humidity) {}
