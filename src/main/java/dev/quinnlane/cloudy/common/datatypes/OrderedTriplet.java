package dev.quinnlane.cloudy.common.datatypes;

import org.jetbrains.annotations.NotNull;

public record OrderedTriplet<T, U, V>(T x, U y, V z) {
	@Override
	public @NotNull String toString() {
		return String.format("(%s, %s, %s)", this.x.toString(), this.y.toString(), this.z.toString());
	}
}
