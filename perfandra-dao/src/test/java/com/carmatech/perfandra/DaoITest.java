package com.carmatech.perfandra;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoITest {
	private static final Logger LOGGER = LoggerFactory.getLogger(DaoITest.class);

	@Test
	public void test() throws Exception {
		LOGGER.info("Testing against the Cassandra instance started by cassandra-maven-plugin.");
	}
}
