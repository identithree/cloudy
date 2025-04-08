package dev.quinnlane.cloudy.backend.dataproviders;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public record ProviderInformation(ProviderType type, String className, String friendlyName, String owner, URI friendlyURL, Date updatedOn) {
	@Override
	public @NotNull String toString() {
		return String.format("%s - Data provided by %s (Updated on %s)", this.friendlyName, this.owner, new SimpleDateFormat("yyyy-MM-dd").format(this.updatedOn));
	}
}
