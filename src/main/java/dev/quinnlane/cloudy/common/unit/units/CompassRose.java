package dev.quinnlane.cloudy.common.unit.units;

import dev.quinnlane.cloudy.common.configuration.MockConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Calendar;

/**
 * The CompassRose enum represents the cardinal, ordinal, and half-wind directions of a compass rose,
 * with each direction having a human-readable name.
 */
public enum CompassRose {
	N("North"),
	NBE("North by East"),
	NNE("North-northeast"),
	NEBN("Northeast by North"),
	NE("Northeast"),
	NEBE("Northeast by East"),
	ENE("East-northeast"),
	EBN("East by North"),
	E("East"),
	EBS("East by South"),
	ESE("East-southeast"),
	SEBE("Southeast by East"),
	SE("Southeast"),
	SEBS("Southeast by South"),
	SSE("South-southeast"),
	SBE("South by East"),
	S("South"),
	SBW("South by West"),
	SSW("South-southwest"),
	SWBS("Southwest by South"),
	SW("Southwest"),
	SWBW("Southwest by West"),
	WSW("West-southwest"),
	WBS("West by South"),
	W("West"),
	WBN("West by North"),
	WNW("West-northwest"),
	NWBW("Northwest by West"),
	NW("Northwest"),
	NWBN("Northwest by North"),
	NNW("North-northwest"),
	NBW("North by West");

	/**
	 * Defines the levels of specificity for compass rose directions.
	 * <p>
	 * This enum represents four levels of direction specificity:
	 * <ul>
	 *     <li>{@code CARDINAL}: Cardinal directions (e.g., North, South, East, West)</li>
	 *     <li>{@code ORDINAL}: Ordinal directions (e.g., Northeast, Northwest, Southeast, Southwest)</li>
	 *     <li>{@code HALF_WIND}: Half-wind directions, which are positioned between cardinal and ordinal directions (e.g., North-northeast, East-southeast)</li>
	 *     <li>{@code QUARTER_WIND}: Quarter-wind directions, which provide finer granularity between half-wind directions</li>
	 * </ul>
	 *
	 * {@code Specificity} is used to describe the level of granularity for a given directional orientation,
	 * with {@code CARDINAL} being the broadest and {@code QUARTER_WIND} being the most specific.
	 *
	 * @see #getSpecificity()
	 */
	public enum Specificity {
		CARDINAL, ORDINAL, HALF_WIND, QUARTER_WIND
	}

	/**
	 * Stores a human-readable sentence representation of the compass rose direction
	 */
	private final String humanReadable;

	/**
	 * Constructs a CompassRose instance with a human-readable string
	 * representing a cardinal or intercardinal direction.
	 *
	 * @param humanReadable A string representing the direction in a human-readable format,
	 *                      such as "North", "Southwest", "East-southeast", "West by North", etc.
	 */
	CompassRose(String humanReadable) {
		this.humanReadable = humanReadable;
	}

	/**
	 * Generates an abbreviation from the string representation of the enum's name.
	 * As these abbreviations always write "B" as lower-case, this appropriately corrects
	 * for that.
	 * <p>
	 * There is an Easter egg that occurs, where if Easter eggs are enabled and the month is
	 * March, then the abbreviation for Southwest by South (normally SWbS) becomes "SXSW®" as
	 * a reference to the film festival.
	 *
	 * @return The formatted abbreviation for the enumeration constant as a string, or if
	 *         conditions are met, the Easter egg.
	 *
	 * @see #getHumanReadable()
	 */
	public String getAbbreviation() {
		if (this.ordinal() == 21 && MockConfiguration.FUN__EASTER_EGGS && Calendar.MONTH == Calendar.MARCH) return "SXSW®";

		String abbreviation = this.name();

		for (int i = 0; i < abbreviation.length(); i++) {
			if (abbreviation.charAt(i) != 'B') break;
			abbreviation = abbreviation.substring(0, i) + Character.toLowerCase(abbreviation.charAt(i)) + abbreviation.substring(i + 1);
		}

		return abbreviation;
	}

	/**
	 * Returns just the human-readable representation of the compass rose direction.
	 * <p>
	 * There is an Easter egg that occurs, where if Easter eggs are enabled and the month is
	 * March, then the human-readable representation for Southwest by South becomes "South by
	 * Southwest®" in reference to the film festival.
	 *
	 * @return The formatted abbreviation for the enumeration constant as a string, or if
	 *         conditions are met, the Easter egg.
	 *
	 * @see #toString()
	 */
	public String getHumanReadable() {
		if (this.ordinal() == 21 && MockConfiguration.FUN__EASTER_EGGS && Calendar.MONTH == Calendar.MARCH) return "South by Southwest®";
		return humanReadable;
	}

	/**
	 * Determines the specificity of an instance based on its ordinal value.
	 * This method categorizes the specificity into four types: CARDINAL, ORDINAL, HALF_WIND, and QUARTER_WIND,
	 * based on modulus operations on the ordinal value of the instance.
	 *
	 * @return The specificity determined for the instance.
	 *
	 * @see Specificity
	 */
	public Specificity getSpecificity() {
		if (this.ordinal() % 8 == 0) return Specificity.CARDINAL;
		if (this.ordinal() % 4 == 0) return Specificity.ORDINAL;
		if (this.ordinal() % 2 == 0) return Specificity.HALF_WIND;
		return Specificity.QUARTER_WIND;
	}

	/**
	 * Converts a direction enum to its corresponding absolute bearing value in degrees.
	 *
	 * @return The absolute bearing value in degrees as a double.
	 */
	public double toAbsoluteBearing() {
		return switch (this) {
			case N -> 0;
			case NBE -> 11.25;
			case NNE -> 22.5;
			case NEBN -> 33.75;
			case NE -> 45;
			case NEBE -> 56.25;
			case ENE -> 67.5;
			case EBN -> 78.75;
			case E -> 90;
			case EBS -> 101.25;
			case ESE -> 112.5;
			case SEBE -> 123.75;
			case SE -> 135;
			case SEBS -> 146.25;
			case SSE -> 157.5;
			case SBE -> 168.75;
			case S -> 180;
			case SBW -> 191.25;
			case SSW -> 202.5;
			case SWBS -> 213.75;
			case SW -> 225;
			case SWBW -> 236.25;
			case WSW -> 247.5;
			case WBS -> 258.75;
			case W -> 270;
			case WBN -> 281.25;
			case WNW -> 292.5;
			case NWBW -> 303.75;
			case NW -> 315;
			case NWBN -> 326.25;
			case NNW -> 337.5;
			case NBW -> 348.75;
		};
	}

	/**
	 * Returns a string representation of the unit, combining its abbreviation and human-readable format.
	 * <p>
	 * Because this method uses the underlying getter methods for the abbreviation and human-readable format,
	 * this method also is afflicted by the "South by Southwest®" Easter egg, if applicable.
	 *
	 * @return A formatted string representing the unit in the form of "abbreviation - human-readable name".
	 *
	 * @see #getAbbreviation()
	 * @see #getHumanReadable()
	 */
	@Override
	public String toString() {
		return String.format("%s - %s", this.getAbbreviation(), this.getHumanReadable());
	}

	/**
	 * Determines the appropriate compass direction from a given bearing with maximum specificity.
	 *
	 * @param bearing    The direction in degrees, where 0 is north, 90 is east, 180 is south, and 270 is west.
	 *                   This does not have to be the absolute bearing of the compass direction,
	 *                   performing this operation does lead to data loss.
	 * @return The corresponding CompassRose direction based on the bearing and specified precision.
	 */
	public static CompassRose fromBearing(double bearing) {
		return fromBearing(bearing, Specificity.QUARTER_WIND);
	}

	/**
	 * Determines the appropriate compass direction from a given bearing and level of specificity.
	 *
	 * @param bearing    The direction in degrees, where 0 is north, 90 is east, 180 is south, and 270 is west.
	 *                   This does not have to be the absolute bearing of the compass direction,
	 *                   performing this operation does lead to data loss.
	 * @param precision  The specified level of precision for the direction (e.g., CARDINAL, ORDINAL, HALF_WIND, QUARTER_WIND).
	 *                   Must not be null.
	 * @return The corresponding CompassRose direction based on the bearing and specified precision.
	 */
	public static CompassRose fromBearing(double bearing, @NotNull Specificity precision) {
		bearing = ((bearing % 360) + 360) % 360;

		int divisions = switch (precision) {
			case CARDINAL -> 4;
			case ORDINAL -> 8;
			case HALF_WIND -> 16;
			case QUARTER_WIND -> 32;
		};

		double segmentSize = 360.0 / divisions;
		int index = (int) Math.round(bearing / segmentSize) % divisions;

		CompassRose[] directions = Arrays.stream(CompassRose.values())
				.filter(direction -> direction.getSpecificity().ordinal() <= precision.ordinal())
				.toArray(CompassRose[]::new);

		return directions[index];
	}
}
