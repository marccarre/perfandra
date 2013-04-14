package com.carmatech.perfandra.schema;

import com.carmatech.perfandra.schema.composites.RawMetrics;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;

public final class Schema {
	private Schema() {
		// Constant placeholder, do NOT instantiate.
	}

	public static ColumnFamily<String, RawMetrics> CF_RAW_METRICS = 
			new ColumnFamily<String, RawMetrics>("RawMetrics", StringSerializer.get(), RawMetrics.SERIALIZER);
}
