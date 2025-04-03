package dev.quinnlane.cloudy.common.unit.units;

import dev.quinnlane.cloudy.common.Constants;
import dev.quinnlane.cloudy.common.unit.UnitLocales;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * The WindSpeed class extends the {@link Speed} class and represents a unit of speed specifically
 * tailored for wind measurements. This class ensures that default unit settings are
 * appropriately configured for locale-specific requirements.
 * <p>
 * It primarily uses {@link UnitLocales#CUSTOMARY} as the default mixed units setting for
 * wind speed unit representation. The class allows instantiation with a specific value,
 * locale, and optionally a precision configuration for formatted representations.
 */
public class WindSpeed extends Speed {
	protected static final UnitLocales MIXED_UNITS = UnitLocales.CUSTOMARY;

	/**
	 * Constructs an instance of the WindSpeed class with the specified value and locale.
	 * It uses {@link Constants#DEFAULT_PRECISION} for the precision value.
	 *
	 * @param value  The value associated with the unit.
	 * @param locale The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 */
	public WindSpeed(@NotNull Integer value, @NotNull UnitLocales locale) {
		super(value, locale, MIXED_UNITS);
	}

	/**
	 * Constructs an instance of the WindSpeed class with the specified value, locale,
	 * and precision.
	 *
	 * @param value     The value associated with the unit.
	 * @param locale    The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 * @param precision A DecimalFormat object that sets the level of numeric precision used for the unit's value representation.
	 * @throws IllegalArgumentException If the <code>unitsForMixed</code> parameter is set to
	 *                                  {@link UnitLocales#MIXED} the program will throw an
	 *                                  IllegalArgumentException as that combination does not
	 *                                  make sense
	 */
	public WindSpeed(@NotNull Integer value, @NotNull UnitLocales locale, @NotNull DecimalFormat precision) {
		super(value, locale, MIXED_UNITS, precision);
	}
}
