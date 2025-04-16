package dev.quinnlane.cloudy.backend.dataproviders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * Represents the information of a provider, including its type, class name, friendly name,
 * owner, and an optional URL and timestamp for the last update.
 * <p>
 * This record serves as a container for metadata about a provider.
 * Instances of this class are immutable and can be used to share or store provider-related details.
 *
 * @param type         The type of the provider, as defined by the {@code ProviderType} enum. Indicates the provider's source type.
 * @param className    The fully qualified class name of the provider implementation.
 * @param friendlyName A user-friendly name representing the provider.
 * @param owner        The owner or organization responsible for the provider.
 * @param friendlyURL  An optional URI pointing to a user-accessible resource related to this provider.
 * @param updatedOn    An optional timestamp of when the provider information was last updated.
 */
public record ProviderInformation(ProviderType type, String className, String friendlyName, String owner, @Nullable URI friendlyURL, @Nullable LocalDateTime updatedOn) {
	/**
	 * Returns a string representation of the provider information.
	 * The format includes the friendly name, the owner, and the last updated date.
	 *
	 * @return a string in the format "friendlyName - Data provided by owner (Updated on yyyy-MM-dd)"
	 * 		   or "friendlyName - Data provided by owner" if no date is specified.
	 */
	@Override
	public @NotNull String toString() {
		if (this.updatedOn == null) return String.format("%s - Data provided by %s", this.friendlyName, this.owner);
		return String.format("%s - Data provided by %s (Updated on %s)", this.friendlyName, this.owner, new SimpleDateFormat("yyyy-MM-dd").format(this.updatedOn));
	}
}
