package dev.quinnlane.cloudy.common.datagen;

import dev.quinnlane.cloudy.common.configuration.MockConfiguration;
import dev.quinnlane.cloudy.common.datatypes.OrderedPair;
import dev.quinnlane.cloudy.common.unit.units.PrecipitationAmount;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Class responsible for generating mock data values, primarily numerical and precipitation data,
 * based on configurable presets and parameters.
 */
public class MockDataGenerator {
	/**
	 * Generates a numerical data value based on the provided data preset and its precision.
	 *
	 * @param preset the data preset configuration containing precision and other information; must not be null
	 * @return the generated numerical data value based on the given preset
	 */
	public static Number generateData(@NotNull DataPresets preset) {
		return generateData(preset, preset.getPrecision());
	}

	/**
	 * Generates a random number based on the given data preset and precision.
	 * The method can generate either a floating-point number or an integer
	 * depending on the provided preset configuration.
	 *
	 * @param preset    the data preset that specifies the range and type of the number
	 * @param precision the format string defining the precision for floating-point numbers
	 * @return a randomly generated number, either a floating-point or an integer,
	 *         based on the provided preset and precision
	 */
	public static Number generateData(@NotNull DataPresets preset, String precision) {
		Random random = new Random();

		if (preset.isFloatingPoint()) return Double.parseDouble(new DecimalFormat(precision).format(random.nextDouble(preset.getMinimum(), preset.getMaximum())));
		return random.nextInt((int) preset.getMinimum(), (int) preset.getMaximum() + 1);
	}

	@Contract(" -> new")
	public static @NotNull OrderedPair<Integer, PrecipitationAmount> generatePrecipitationData() {
		if (MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRECIPITATING) {
			int chance = (int) generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRECIPITATION_CHANCE);
			PrecipitationAmount amount = new PrecipitationAmount((Double) generateData(MockConfiguration.DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRECIPITATION_AMOUNT), MockConfiguration.CUSTOMIZATION__LOCALE);
			return new OrderedPair<>(chance, amount);
		} else {
			PrecipitationAmount amount = new PrecipitationAmount(0d, MockConfiguration.CUSTOMIZATION__LOCALE);
			return new OrderedPair<>(0, amount);
		}
	}
}
