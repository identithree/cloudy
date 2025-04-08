package dev.quinnlane.cloudy.backend.dataproviders;

public enum ProviderFeatures {
	LIVE_TEMPERATURE("Supports temperature readouts"),
	HIGH_TEMPERATURE("Supports high temperature readouts"),
	LOW_TEMPERATURE("Supports low temperature readouts"),
	PRESSURE("Supports pressure readouts"),
	PRECIPITATION("Supports precipitation readouts"),
	PRECIPITATION_PROBABILITY("Supports precipitation probability readouts"),
	SUNRISE("Supports sunrise times"),
	SUNSET("Supports sunset times"),
	HUMIDITY("Supports humidity readouts"),
	WIND_SPEED("Supports wind speed readouts"),
	WIND_DIRECTION_DEGREES("Reports wind direction in degrees"),
	WIND_DIRECTION_CARDINALS_4("Only reports wind direction in cardinal directions"),
	WIND_DIRECTION_CARDINALS_8("Reports wind direction in cardinal and ordinal directions"),
	WIND_DIRECTION_CARDINALS_16("Reports wind direction in cardinal, ordinal, and half-wind directions"),
	WIND_DIRECTION_CARDINALS_32("Reports wind direction in cardinal and ordinal, along with half- and quarter-wind directions");

	private final String description;

	ProviderFeatures(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
