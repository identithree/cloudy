package dev.quinnlane.cloudy.common.unit.units;

import dev.quinnlane.cloudy.common.Constants;
import dev.quinnlane.cloudy.common.unit.Unit;
import dev.quinnlane.cloudy.common.unit.UnitLocales;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * The Speed class represents a unit of speed. It allows defining a speed value in a specific locale
 * (e.g., SI, CUSTOMARY) and provides conversion and precision handling mechanisms.
 * The class supports defining speed in either SI or Customary systems.
 * <p>
 * This class extends {@link Unit}, providing capabilities for specific speed-related unit transformations,
 * including conversion to customary format.
 */
public class Speed extends Unit<Integer> {
	private static final UnitLocales MIXED_UNITS = UnitLocales.SI;
	private static final UnitStrings UNIT_STRINGS = new UnitStrings("km/h", "mph", MIXED_UNITS);

	/**
	 * Constructs an instance of the Speed class with the specified value and locale.
	 * It uses {@link Constants#DEFAULT_PRECISION} for the precision value.
	 *
	 * @param value         The value associated with the unit.
	 * @param locale        The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 */
	public Speed(@NotNull Integer value, @NotNull UnitLocales locale) {
		this(value, locale, Constants.DEFAULT_PRECISION);
	}

	/**
	 * Constructs an instance of the Speed class with the specified value, locale,
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
	public Speed(@NotNull Integer value, @NotNull UnitLocales locale, @NotNull DecimalFormat precision) {
		super(value, locale, MIXED_UNITS, UNIT_STRINGS, precision);
	}

	/**
	 * Constructs an instance of the Speed class with the specified value and locale
	 * and overrides the {@link #MIXED_UNITS} value.
	 * It uses {@link Constants#DEFAULT_PRECISION} for the precision value.
	 *
	 * @param value         The value associated with the unit.
	 * @param locale        The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 * @param mixedOverride The units that the MIXED locale should specify instead of the default
	 */
	protected Speed(@NotNull Integer value, @NotNull UnitLocales locale, @NotNull UnitLocales mixedOverride) {
		this(value, locale, mixedOverride, Constants.DEFAULT_PRECISION);
	}

	/**
	 * Constructs an instance of the Speed class with the specified value, locale, and
	 * precision, along with overriding the {@link #MIXED_UNITS} value.
	 *
	 * @param value         The value associated with the unit.
	 * @param locale        The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 * @param precision     A DecimalFormat object that sets the level of numeric precision used for the unit's value representation.
	 * @param mixedOverride The units that the MIXED locale should specify instead of the default
	 *
	 * @throws IllegalArgumentException If the <code>unitsForMixed</code> parameter is set to
	 *                                  {@link UnitLocales#MIXED} the program will throw an
	 *                                  IllegalArgumentException as that combination does not
	 *                                  make sense
	 */
	protected Speed(@NotNull Integer value, @NotNull UnitLocales locale, @NotNull UnitLocales mixedOverride, @NotNull DecimalFormat precision) {
		super(value, locale, mixedOverride, new UnitStrings("km/h", "mph", mixedOverride), precision);
	}

	/**
	 * Converts and returns the value of this unit converted to the customary unit system format.
	 *
	 * @return The unit's value in customary format, converted from SI.
	 */
	@Override
	protected Integer asCustomary() {
		return Math.round(this.value / 1.609f);
	}
}
