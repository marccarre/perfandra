package com.carmatech.perfandra;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.cassandraunit.AbstractCassandraUnit4TestCase;
import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.yaml.ClassPathYamlDataSet;
import org.junit.Before;
import org.junit.Test;

import com.carmatech.perfandra.schema.Schema;
import com.carmatech.perfandra.schema.composites.RawMetrics;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.RangeBuilder;

public class DaoTest extends AbstractCassandraUnit4TestCase {
	private static final String MAX = "\uFFFF";
	private static final String MIN = "\u0000";
	private Keyspace keyspace;

	@Before
	public void setUp() {
		AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
				.forCluster("ClusterName")
				.forKeyspace("perfandra")
				.withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE))
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(9171).setMaxConnsPerHost(1).setSeeds("127.0.0.1:9171"))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor()).buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		keyspace = context.getClient();
	}

	@Override
	public DataSet getDataSet() {
		return new ClassPathYamlDataSet("extendedDataSet.yaml");
	}

	@Test
	public void shouldHaveLoadAnExtendDataSet() throws Exception {
		assertThat(getKeyspace(), notNullValue());
		assertThat(getKeyspace().getKeyspaceName(), is("perfandra"));
		/* and query all what you want */

		ColumnList<RawMetrics> all = keyspace.prepareQuery(Schema.CF_RAW_METRICS).getKey("ALL").execute().getResult();
		assertThat(all.size(), is(4));
		assertThat(all.getColumnByName(new RawMetrics("facebook", "usersPerDay")).getStringValue(), is(""));
		assertThat(all.getColumnByName(new RawMetrics("facebook", "clicksPerDay")).getStringValue(), is(""));
		assertThat(all.getColumnByName(new RawMetrics("googleplus", "pageLoadTime")).getStringValue(), is(""));
		assertThat(all.getColumnByName(new RawMetrics("googleplus", "usersPerDay")).getStringValue(), is(""));

		OperationResult<ColumnList<RawMetrics>> query = keyspace.prepareQuery(Schema.CF_RAW_METRICS).getKey("ALL")
				.withColumnRange(new RangeBuilder().setLimit(1).build()).execute();
		ColumnList<RawMetrics> sliceOne = query.getResult();
		assertThat(sliceOne.size(), is(1));
		assertThat(sliceOne.getColumnByIndex(0).getName(), is(new RawMetrics("facebook", "clicksPerDay")));

		ColumnList<RawMetrics> facebookRange = keyspace
				.prepareQuery(Schema.CF_RAW_METRICS)
				.getKey("ALL")
				.withColumnRange(
						new RangeBuilder().setStart(new RawMetrics("facebook"), RawMetrics.SERIALIZER)
								.setEnd(new RawMetrics("facebook\uffff"), RawMetrics.SERIALIZER).setLimit(Integer.MAX_VALUE).build()).execute().getResult();
		assertThat(facebookRange.size(), is(2));
		assertThat(facebookRange.getColumnByName(new RawMetrics("facebook", "usersPerDay")).getStringValue(), is(""));
		assertThat(facebookRange.getColumnByName(new RawMetrics("facebook", "clicksPerDay")).getStringValue(), is(""));

		ColumnList<RawMetrics> facebookWithColumnRange = keyspace.prepareQuery(Schema.CF_RAW_METRICS).getKey("ALL")
				.withColumnRange(RawMetrics.SERIALIZER.buildRange().withPrefix("facebook").greaterThanEquals(MIN).lessThanEquals(MAX).build()).execute()
				.getResult();
		assertThat(facebookWithColumnRange.size(), is(2));
		assertThat(facebookWithColumnRange.getColumnByName(new RawMetrics("facebook", "usersPerDay")).getStringValue(), is(""));
		assertThat(facebookWithColumnRange.getColumnByName(new RawMetrics("facebook", "clicksPerDay")).getStringValue(), is(""));
	}
}