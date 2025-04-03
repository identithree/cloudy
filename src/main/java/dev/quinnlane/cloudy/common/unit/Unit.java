package dev.quinnlane.cloudy.common.unit;

import dev.quinnlane.cloudy.common.Constants;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * The {@code Unit} class serves as an abstract representation for a unit of measurement.
 * It provides a flexible way to manage values in different measurement systems, such as
 * the International System of Units (SI), customary (imperial), and mixed modes.
 * <p>
 * The main responsibilities of the {@code Unit} class include:
 * <ul>
 *     <li>Storing a unit value, which is represented generically for flexibility.</li>
 *     <li>Providing locale-based transformations of the value and unit string.</li>
 *     <li>Formatting the value based on configurable precision settings.</li>
 * </ul>
 *
 * @param <T> The type of the value being represented by the unit. It enables managing
 *            values in different data formats, such as {@code Double}, {@code Integer}, etc.
 */

public abstract class Unit<T> {
	/**
	 * A utility class designed to store and retrieve unit strings based on specified locales.
	 * It facilitates the handling of different formats for units, such as SI, customary, or mixed.
	 */
	public static class UnitStrings {
		/**
		 * A string representing the unit in the International System of Units (SI) format.
		 * It is used to describe the standardized SI representation of a unit.
		 */
		private final String si;
		/**
		 * A string representing the unit in the customary system format.
		 * This field is used to describe the representation of a unit
		 * as per the customary or imperial system of measurement.
		 */
		private final String customary;
		/**
		 * Represents the default unit locale to be used in mixed mode.
		 * This field specifies whether SI or customary units should be applied
		 * when the unit locale is set to mixed.
		 * <p>
		 * It determines what unit type (e.g., SI or customary) will take precedence
		 * in scenarios where mixed-mode operations are being used.
		 */
		private final UnitLocales unitsForMixed;

		/**
		 * Constructs an instance of UnitStrings to store unit representations
		 * for SI, customary, and mixed unit locales.
		 *
		 * @param si A string representing the unit in the International System of Units (SI) format
		 * @param customary A string representing the unit in the customary system format
		 * @param unitsForMixed The default unit locale to be used for mixed mode; defines
		 *                      whether the SI or customary unit should take precedence
		 *
		 * @throws IllegalArgumentException If the <code>unitsForMixed</code> parameter is set to
		 *                                  {@link UnitLocales#MIXED} the program will throw an
		 *                                  IllegalArgumentException as that combination does not
		 *                                  make sense
		 */
		public UnitStrings(String si, String customary, UnitLocales unitsForMixed) {
			if (unitsForMixed == UnitLocales.MIXED) throw new IllegalArgumentException("The units to use for mixed mode cannot be mixed!");

			this.si = si;
			this.customary = customary;
			this.unitsForMixed = unitsForMixed;
		}

		/**
		 * Retrieves the unit string associated with the specified unit locale.
		 *
		 * @param locale The unit locale, which determines the format of the returned unit string.
		 *               It can be one of the following:
		 *               <ul>
		 *               	<li>{@link UnitLocales#SI}: Returns the unit string in SI format.</li>
		 *               	<li>{@link UnitLocales#CUSTOMARY}: Returns the unit string in customary format.</li>
		 *               	<li>{@link UnitLocales#MIXED}: Returns the unit string based on the default unit locale for mixed mode defined by {@code unitsForMixed}.</li>
		 *               </ul>
		 *
		 * @return The unit string corresponding to the provided locale. If the locale is
		 *         {@link UnitLocales#MIXED}, the return value depends on the value of
		 *         {@code unitsForMixed}, which determines whether the SI or customary string
		 *         takes precedence.
		 */
		public String get(@NotNull UnitLocales locale) {
			return switch (locale) {
				case SI -> this.si;
				case CUSTOMARY -> this.customary;
				case MIXED -> this.unitsForMixed.equals(UnitLocales.CUSTOMARY) ? this.customary : this.si;
			};
		}
	}

	/**
	 * Represents the core value associated with the unit.
	 * This value is defined with a generic type to allow flexibility for various data types.
	 * Its internal representation is always in SI units, however, the display output can and
	 * will be altered by the class {@link #locale} field.
	 * <p>
	 * The value serves as the primary data container for the unit instance and is
	 * immutable after initialization.
	 */
	protected final T value;
	/**
	 * Represents the unit locale used for determining the format
	 * in which the unit is represented or handled.
	 * <p>
	 * This field specifies whether the unit should conform to one of the following:
	 * <ul>
	 *     <li>{@link UnitLocales#SI}: The International System of Units format (Global Standard).</li>
	 *     <li>{@link UnitLocales#CUSTOMARY}: The customary or imperial system format (United States).</li>
	 *     <li>{@link UnitLocales#MIXED}: A mixed-mode format that alternates based on the context (Canada and UK).</li>
	 * </ul>
	 * <p>
	 * The {@code locale} value plays a crucial role in methods such as
	 * <ul>
	 *     <li>{@link #getValue()}, where the locale determines the format in which the value is retrieved.</li>
	 *     <li>{@link #getUnitString()}, where the locale determines the corresponding unit string.</li>
	 * </ul>
	 * <p>
	 * It is initially set via the constructor and can be modified through {@link #setLocale(UnitLocales)}.
	 */
	protected UnitLocales locale;
	/**
	 * Represents the default unit locale to be used in mixed mode.
	 * This field specifies whether SI or customary units should be applied
	 * when the unit locale is set to mixed.
	 * <p>
	 * It determines what unit type (e.g., SI or customary) will take precedence
	 * in scenarios where mixed-mode operations are being used.
	 */
	protected final UnitLocales unitsForMixed;
	/**
	 * Stores unit strings for different measurement system formats (SI, customary)
	 * and provides the ability to retrieve these strings based on the current unit
	 * locale or mixed-mode configuration.
	 */
	protected final UnitStrings unitStrings;
	/**
	 * A {@link DecimalFormat} instance that defines the precision used for formatting numeric values
	 * in this unit. It specifies the number of significant digits or decimal places for the
	 * unit's value representation.
	 */
	protected final DecimalFormat precision;

	/**
	 * Constructs an instance of the Unit class with the specified value, locale,
	 * units for mixed mode, and unit strings.
	 * It uses {@link Constants#DEFAULT_PRECISION} for the precision value.
	 *
	 * @param value The value associated with the unit.
	 * @param locale The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 * @param unitsForMixed Specifies which unit system (SI or CUSTOMARY) to prioritize when the locale is set to MIXED.
	 *                      Using MIXED for this parameter will throw an IllegalArgumentException.
	 * @param unitStrings The object that provides the unit string mappings for different locales.
	 */
	public Unit(@NotNull T value, @NotNull UnitLocales locale, @NotNull UnitLocales unitsForMixed, @NotNull UnitStrings unitStrings) {
		this(value, locale, unitsForMixed, unitStrings, Constants.DEFAULT_PRECISION);
	}

	/**
	 * Constructs an instance of the Unit class with the specified value, locale,
	 * units for mixed mode, unit strings, and precision.
	 *
	 * @param value The value associated with the unit.
	 * @param locale The initial locale of the unit, which determines how the value is represented (e.g., SI, CUSTOMARY, MIXED).
	 * @param unitsForMixed Specifies which unit system (SI or CUSTOMARY) to prioritize when the locale is set to MIXED.
	 *                      Using MIXED for this parameter will throw an IllegalArgumentException.
	 * @param unitStrings The object that provides the unit string mappings for different locales.
	 * @param precision A DecimalFormat object that sets the level of numeric precision used for the unit's value representation.
	 *
	 * @throws IllegalArgumentException If the <code>unitsForMixed</code> parameter is set to
	 *                                  {@link UnitLocales#MIXED} the program will throw an
	 *                                  IllegalArgumentException as that combination does not
	 *                                  make sense
	 */
	public Unit(@NotNull T value, @NotNull UnitLocales locale, @NotNull UnitLocales unitsForMixed, @NotNull UnitStrings unitStrings, @NotNull DecimalFormat precision) {
		if (unitsForMixed == UnitLocales.MIXED) throw new IllegalArgumentException("The units to use for mixed mode cannot be mixed!");

		this.value = value;
		this.locale = locale;
		this.unitsForMixed = unitsForMixed;
		this.unitStrings = unitStrings;
		this.precision = precision;
	}

	/**
	 * Returns the value of the unit in the International System of Units (SI) format.
	 *
	 * @return The unit's value in SI format.
	 */
	protected T asSI() {
		return this.value;
	}

	/**
	 * Converts and returns the value of this unit converted to the customary unit system format.
	 *
	 * @return The unit's value in customary format, converted from SI.
	 */
	protected abstract T asCustomary();

	/**
	 * Converts and returns the value of this unit in a mixed mode, based on the specified
	 * priority of unit systems. If the {@code unitsForMixed} field is set to {@code UnitLocales.CUSTOMARY},
	 * it prioritizes the customary representation. Otherwise, it defaults to the SI representation.
	 *
	 * @return The value of the unit in mixed mode, either in customary or SI format based on the
	 *         {@code unitsForMixed} field.
	 */
	protected T asMixed() {
		return this.unitsForMixed.equals(UnitLocales.CUSTOMARY) ? this.asCustomary() : this.asSI();
	}

	/**
	 * Retrieves the value of the unit using the current locale setting.
	 *
	 * @return The value of the unit as per the active locale configuration.
	 */
	public T getValue() {
		return getValue(this.locale);
	}

	/**
	 * Returns the value of the unit based on the specified locale.
	 *
	 * @param locale The locale that determines how the value is represented. It can be:
	 *               <ul>
	 *                   <li>{@code SI} for the International System of Units.</li>
	 *                   <li>{@code CUSTOMARY} for the customary (imperial) unit system.</li>
	 *                   <li>{@code MIXED} for a mixed mode representation.</li>
	 *               </ul>
	 *
	 * @return The value of the unit in the format corresponding to the specified locale.
	 */
	public T getValue(@NotNull UnitLocales locale) {
		return switch (locale) {
			case SI -> this.asSI();
			case CUSTOMARY -> this.asCustomary();
			case MIXED -> this.asMixed();
		};
	}

	/**
	 * Retrieves the current locale of the unit, which determines how the unit's
	 * value is represented (e.g., SI, CUSTOMARY, MIXED).
	 *
	 * @return The current locale of the unit as an instance of {@link UnitLocales}.
	 */
	public UnitLocales getLocale() {
		return this.locale;
	}

	/**
	 * Sets the locale of the unit, which determines how the value and unit strings are represented.
	 *
	 * @param locale The desired locale for the unit. It can be:
	 *               <ul>
	 *                   <li>{@code SI} for the International System of Units.</li>
	 *                   <li>{@code CUSTOMARY} for the customary (imperial) unit system.</li>
	 *                   <li>{@code MIXED} for a mixed mode representation.</li>
	 *               </ul>
	 */
	public void setLocale(UnitLocales locale) {
		this.locale = locale;
	}

	/**
	 * Retrieves the unit string based on the current locale of the unit.
	 *
	 * @return The unit string corresponding to the current locale, which can be
	 *         represented in SI, customary, or mixed mode based on the locale setting.
	 */
	public String getUnitString() {
		return this.getUnitString(this.locale);
	}

	/**
	 * Retrieves the unit string associated with the specified locale.
	 *
	 * @param locale The locale that determines the format of the returned unit string.
	 *               It can be:
	 *               <ul>
	 *                   <li>{@code SI} for the International System of Units.</li>
	 *                   <li>{@code CUSTOMARY} for the customary (imperial) unit system.</li>
	 *                   <li>{@code MIXED} for a mixed mode representation.</li>
	 *               </ul>
	 *
	 * @return The unit string corresponding to the specified locale. If the locale is {@code MIXED},
	 *         the return value depends on the {@code unitsForMixed} configuration, which prioritizes
	 *         either the SI or customary unit string.
	 */
	public String getUnitString(UnitLocales locale) {
		return this.unitStrings.get(locale);
	}

	/**
	 * Returns a string representation of the unit, combining the formatted value
	 * with its associated unit string based on the current locale configuration.
	 *
	 * @return A formatted string representing the unit's value and unit string.
	 */
	@Override
	public String toString() {
		return String.format("%s %s", this.precision.format(this.getValue()), this.getUnitString());
	}
}
