/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import static org.junit.Assert.*;



import org.junit.Test;



/**
 * @author friscis, swconley
 *
 * @version $Revision: 1.0 $
 */
public class StatisticsTester {
	/**
	 * Method testAll1.
	 */
	@Test
	public void testAll1() {
		double[] nums = {1.0, 2.0, 3.0, 4.0, 5.0};
		assertEquals(3.0, StatisticsInfo.mean(nums), .001);
		assertEquals(3.0, StatisticsInfo.median(nums), .001);
		assertEquals(1.0, StatisticsInfo.mode(nums), .001);
		assertEquals(1.581, StatisticsInfo.StdDev(nums), .001);
		assertEquals(5.0, StatisticsInfo.max(nums), .001);
		assertEquals(1.0, StatisticsInfo.min(nums), .001);
	}
	/**
	 * Method testAll2.
	 */
	@Test
	public void testAll2() {
		double[] nums = {48.6, 20.9, 8, 20.9, 11.40, 4.26, 71.45, 85.99, 12};
		assertEquals(31.5, StatisticsInfo.mean(nums), .001);
		assertEquals(20.9, StatisticsInfo.median(nums), .001);
		assertEquals(20.9, StatisticsInfo.mode(nums), .001);
		assertEquals(29.918, StatisticsInfo.StdDev(nums), .001);
		assertEquals(85.99, StatisticsInfo.max(nums), .001);
		assertEquals(4.26, StatisticsInfo.min(nums), .001);
	}
}
