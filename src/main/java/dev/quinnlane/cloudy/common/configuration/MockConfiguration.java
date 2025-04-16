package dev.quinnlane.cloudy.common.configuration;

import dev.quinnlane.cloudy.backend.dataproviders.DataProvider;
import dev.quinnlane.cloudy.backend.dataproviders.providers.MockDataProvider;
import dev.quinnlane.cloudy.common.datagen.DataPresets;
import dev.quinnlane.cloudy.common.unit.UnitLocales;
import dev.quinnlane.cloudy.common.unit.units.CompassRose;

public class MockConfiguration {
	public static final UnitLocales CUSTOMIZATION__LOCALE = UnitLocales.SI;
	public static final CompassRose.Specificity CUSTOMIZATION__MAX_WIND_DIRECTION_SPECIFICITY = CompassRose.Specificity.ORDINAL;
	public static final boolean FUN__EASTER_EGGS = false;
	public static final Class<? extends DataProvider> DATA_PROVIDER__CURRENT_PROVIDER = MockDataProvider.class;
	public static final int DATA_PROVIDER__UPDATE_INTERVAL = 60;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__WARMTH = DataPresets.TEMPERATURE_WARM;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__COLDNESS = DataPresets.TEMPERATURE_COOL;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__HUMIDITY = DataPresets.HUMIDITY_MEDIUM;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__WIND_SPEED = DataPresets.WIND_SPEED_FAST;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRESSURE = DataPresets.PRESSURE_NORMAL;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__UV_INDEX = DataPresets.UV_INDEX_MEDIUM;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__AIR_QUALITY = DataPresets.AIR_QUALITY_GOOD;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRECIPITATION_CHANCE = DataPresets.PRECIPITATION_CHANCE_MEDIUM;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRECIPITATION_AMOUNT = DataPresets.PRECIPITATION_AMOUNT_LOW;
	public static final DataPresets DATA_PROVIDERS__MOCK_DATA_PROVIDER__CLOUD_COVER = DataPresets.AIR_QUALITY_GOOD;
	public static final boolean DATA_PROVIDERS__MOCK_DATA_PROVIDER__PRECIPITATING = true;
	public static final int DATA_PROVIDERS__MOCK_DATA_PROVIDER__HOURLY_FORECAST_COUNT = 12;
	public static final int DATA_PROVIDERS__MOCK_DATA_PROVIDER__DAILY_FORECAST_COUNT = 7;
}
