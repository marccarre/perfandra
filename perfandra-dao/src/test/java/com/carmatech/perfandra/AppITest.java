/*******************************************************************************
 * Copyright (c) 2012 Marc Carre <carre.marc@gmail.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * Contributors:
 *     Marc Carre - initial API and implementation
 ******************************************************************************/
package com.carmatech.perfandra;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class AppITest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(AppITest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppITest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppITest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		logger.info("Integration test");
		assertTrue(true);
	}
}
