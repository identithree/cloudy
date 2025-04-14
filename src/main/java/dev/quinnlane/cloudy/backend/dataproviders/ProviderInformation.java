package dev.quinnlane.cloudy.backend.dataproviders;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents the detailed information about a provider.
 * <p>
 * This record encapsulates metadata related to a provider, including its type,
 * class name, friendly name, owner, friendly URL, and the last updated date.
 * It provides contextual details to identify and describe the provider.
 *
 * @param type         the type of the provider (e.g., API, WEBSITE, MOCK)
 * @param className    the qualified class name of the provider
 * @param friendlyName a human-readable and user-friendly name of the provider
 * @param owner        the owner or organization responsible for the provider
 * @param friendlyURL  a user-friendly URL for the provider, if applicable
 * @param updatedOn    the date when the provider's information was last updated
 */
public record ProviderInformation(ProviderType type, String className, String friendlyName, String owner, URI friendlyURL, Date updatedOn) {
	/**
	 * Returns a string representation of the provider information.
	 * The format includes the friendly name, the owner, and the last updated date.
	 *
	 * @return a string in the format "friendlyName - Data provided by owner (Updated on yyyy-MM-dd)"
	 */
	@Override
	public @NotNull String toString() {
		return String.format("%s - Data provided by %s (Updated on %s)", this.friendlyName, this.owner, new SimpleDateFormat("yyyy-MM-dd").format(this.updatedOn));
	}
}
