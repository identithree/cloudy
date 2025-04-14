package dev.quinnlane.cloudy.common.datatypes;

import dev.quinnlane.cloudy.common.unit.units.CompassRose;
import dev.quinnlane.cloudy.common.unit.units.PrecipitationAmount;
import dev.quinnlane.cloudy.common.unit.units.Temperature;
import dev.quinnlane.cloudy.common.unit.units.WindSpeed;
import org.jetbrains.annotations.Range;

import java.time.Instant;

/**
 * Represents a daily weather forecast containing various meteorological elements for a given day.
 * This record aggregates high-level weather data such as temperature, precipitation, wind,
 * cloud cover, humidity, UV index, and solar events (sunrise and sunset).
 *
 * @param highTemperature  The highest recorded or predicted temperature for the day, represented as a {@link Temperature}.
 * @param lowTemperature   The lowest recorded or predicted temperature for the day, represented as a {@link Temperature}.
 * @param precipitation    A pair representing the chance of precipitation (as a percentage) and the
 *                         amount of precipitation (e.g., rainfall) as a {@link PrecipitationAmount}.
 *                         The pair contains:
 *                         <ul>
 *                         		<li>An {@link Integer} for the likelihood (%) of precipitation.</li>
 *                         		<li>A {@link PrecipitationAmount} for the volume of precipitation.</li>
 *                         </ul>
 * @param wind             Details about wind conditions provided as a triplet:
 *                         <ul>
 *                         		<li>A {@link Double} representing the wind speed.</li>
 *                         		<li>A {@link CompassRose} enum value representing the wind direction (cardinal and intercardinal points).</li>
 *                         		<li>A {@link WindSpeed} representing the speed of the wind including units.</li>
 *                         </ul>
 * @param cloudCover       The estimated cloud cover percentage, ranging from 0 (completely clear) to 100 (completely overcast).
 * @param humidity         The relative humidity percentage, ranging from 0 to 100.
 * @param uvIndex          The ultraviolet index, an indicator of the sun's UV radiation at the Earth's surface.
 *                         UV index values are constrained by an inclusive range of 0 to {@link Integer#MAX_VALUE}.
 * @param sunrise          The time of sunrise as an {@link Instant}.
 * @param sunset           The time of sunset as an {@link Instant}.
 */
public record DailyForecast(Temperature highTemperature, Temperature lowTemperature, OrderedPair<Integer, PrecipitationAmount> precipitation, OrderedTriplet<Double, CompassRose, WindSpeed> wind, int cloudCover, int humidity, @Range(from = 0L, to = Integer.MAX_VALUE) int uvIndex, Instant sunrise, Instant sunset) {}
