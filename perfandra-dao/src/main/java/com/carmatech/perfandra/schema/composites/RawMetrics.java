package com.carmatech.perfandra.schema.composites;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.netflix.astyanax.annotations.Component;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;

public final class RawMetrics implements Comparable<RawMetrics> {
	public static AnnotatedCompositeSerializer<RawMetrics> SERIALIZER = new AnnotatedCompositeSerializer<RawMetrics>(RawMetrics.class);

	@Component(ordinal = 0)
	private String application;

	@Component(ordinal = 1)
	private String event;

	public RawMetrics(final String application, final String event) {
		this(application);
		checkNotNull(event, "Event must NOT be null.");
		this.event = event;
	}

	public RawMetrics(final String application) {
		checkNotNull(application, "Application must NOT be null.");
		this.application = application;
	}

	public RawMetrics() {
		// No-arg constructor for Astyanax.
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("application", application).add("event", event).toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof RawMetrics))
			return false;

		final RawMetrics that = (RawMetrics) o;
		return Objects.equal(this.application, that.application) && Objects.equal(this.event, that.event);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(application, event);
	}

	@Override
	public int compareTo(RawMetrics that) {
		return ComparisonChain.start().compare(this.application, that.application).compare(this.event, that.event).result();
	}
}
