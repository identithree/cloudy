package dev.quinnlane.cloudy.backend;

import dev.quinnlane.cloudy.Cloudy;
import dev.quinnlane.cloudy.backend.dataproviders.DataProvider;
import dev.quinnlane.cloudy.common.reflection.PackageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Backend {
	public static final Logger logger = LogManager.getFormatterLogger("Cloudy > Backend");

	private static final ArrayList<Class<? extends DataProvider>> providers = new ArrayList<>();

	private DataProvider provider;

	public Backend() {
		logger.trace("Backend class constructor called from thread '%s'", Thread.currentThread().getName());
		if (Cloudy.getBackend() != null) {
			logger.debug("The backend has already been initialized! Skipping constructor logic...");
			return;
		}

		logger.info("Initializing backend...");

		logger.debug("Registering all default data providers...");
		try {
			for (Class<?> clazz : PackageUtils.getClassesInPackage("dev.quinnlane.cloudy.backend.dataproviders.providers")) {
				Backend.registerProvider(clazz.asSubclass(DataProvider.class));
			}
		} catch (Exception e) {
			logger.error("Failed to register all default data providers!", e);
		}

		logger.info("Backend has been initialized!");
	}

	/**
	 * Converts a given string to a URI instance. If the string cannot be
	 * converted (e.g., due to an invalid format), the method logs an error
	 * and returns null.
	 *
	 * @param uri the string representation of the URI to be converted
	 * @return a {@link URI} instance if the conversion is successful, or null if the conversion fails
	 */
	public static @Nullable URI stringToURI(String uri) {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			logger.error("Failed to convert string \"%s\" to URI!", uri, e);
			return null;
		}
	}

	/**
	 * Converts a given string into a Date instance. If the string cannot be parsed
	 * (e.g., due to an invalid format), the method logs an error and returns null.
	 *
	 * @param date the string representation of the date to be converted
	 * @return a {@link Date} instance if the conversion is successful, or null if the conversion fails
	 */
	public static @Nullable Date stringToDate(String date) {
		try {
			return DateFormat.getDateInstance().parse(date);
		} catch (ParseException e) {
			logger.error("Failed to convert string \"%s\" to Date!", date, e);
			return null;
		}
	}

	public static boolean hasProvider(Class<? extends DataProvider> providerClass) {
		return providers.contains(providerClass);
	}

	public static void registerProvider(@NotNull Class<? extends DataProvider> providerClass) {
		if (hasProvider(providerClass)) logger.error("The specified provider is already registered!", new IllegalArgumentException());
		providers.add(providerClass);
		logger.debug("Successfully registered data provider %s!", providerClass.getSimpleName());
	}

	public static void unregisterProvider(@NotNull Class<? extends DataProvider> providerClass) {
		if (!hasProvider(providerClass)) logger.error("The specified provider is not registered!", new IllegalArgumentException());
		providers.remove(providerClass);
		logger.debug("Successfully unregistered data provider %s!", providerClass.getSimpleName());
	}

	public static Class<?> @NotNull [] getAvailableProviders() {
		return providers.toArray(new Class[0]);
	}

	public DataProvider getProvider() {
		if (provider == null) {
			logger.error("No provider has been set! Please set one before retrieving it.", new NullPointerException());
		}
		return provider;
	}

	public void setProvider(@NotNull Class<? extends DataProvider> providerClass) {
		logger.info("Setting active provider to %s...", providerClass.getSimpleName());
		if (!Backend.hasProvider(providerClass)) {
			logger.error("The specified provider is not registered! Please register it before setting it as the active provider.", new IllegalArgumentException());
		}

		try {
			provider = providerClass.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			logger.error("Failed to create an instance of the specified provider!", ex);
			return;
		}

		provider.refreshData();
	}
}
