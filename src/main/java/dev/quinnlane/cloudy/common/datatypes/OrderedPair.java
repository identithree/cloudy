package dev.quinnlane.cloudy.common.datatypes;

import org.jetbrains.annotations.NotNull;

public record OrderedPair<T, U>(T x, U y) {
	@Override
	public @NotNull String toString() {
		return String.format("(%s, %s)", this.x.toString(), this.y.toString());
	}
}
