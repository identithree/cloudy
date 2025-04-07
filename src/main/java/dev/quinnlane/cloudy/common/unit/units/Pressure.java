package dev.quinnlane.cloudy.common.unit.units;

import dev.quinnlane.cloudy.common.Constants;
import dev.quinnlane.cloudy.common.unit.Unit;
import dev.quinnlane.cloudy.common.unit.UnitLocales;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class Pressure extends Unit<Double> {
	private static final UnitLocales MIXED_UNITS = UnitLocales.SI;
	private static final UnitStrings UNIT_STRINGS = new UnitStrings("mb", "in Hg", MIXED_UNITS);

	/**
	 * Constructs an instance of the Pressure class with the specified value and locale.
	 * It uses {@link Constants#DEFAULT_PRECISION} for the precision value.
	 *
	 * @param value         The value associated with the unit.
	 * @param locale        The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 */
	public Pressure(@NotNull Double value, @NotNull UnitLocales locale) {
		this(value, locale, Constants.DEFAULT_PRECISION);
	}

	/**
	 * Constructs an instance of the Pressure class with the specified value, locale,
	 * and precision.
	 *
	 * @param value         The value associated with the unit.
	 * @param locale        The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 * @param precision     A DecimalFormat object that sets the level of numeric precision used for the unit's value representation.
	 *
	 * @throws IllegalArgumentException If the <code>unitsForMixed</code> parameter is set to
	 *                                  {@link UnitLocales#MIXED} the program will throw an
	 *                                  IllegalArgumentException as that combination does not
	 *                                  make sense
	 */
	public Pressure(@NotNull Double value, @NotNull UnitLocales locale, @NotNull DecimalFormat precision) {
		super(value, locale, MIXED_UNITS, UNIT_STRINGS, precision);
	}

	/**
	 * Converts and returns the value of this unit converted to the customary unit system format.
	 *
	 * @return The unit's value in customary format, converted from SI.
	 */
	@Override
	protected Double asCustomary() {
		return this.value / 33.86;
	}
}
