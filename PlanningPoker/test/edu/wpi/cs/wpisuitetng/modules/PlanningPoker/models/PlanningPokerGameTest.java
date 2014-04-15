/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: team struct-by-lightning
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the PlanningPokerGame class
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 15, 2014
 */
public class PlanningPokerGameTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public final void getGameNameTest() {
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", null, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertTrue(test.getGameName().equals("test game name"));
	}

	@Test
	public final void getDescriptionTest() {
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", null, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertTrue(test.getDescription().equals("test description"));
	}

	@Test
	public final void getDeckTypeTest() {
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", null, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertTrue(test.getDeckType().equals("fibonacci"));
	}

	@Test
	public final void getRequirementIdsTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertTrue(test.getRequirementIds().contains(1));
		assertTrue(test.getRequirementIds().contains(2));
		assertTrue(test.getRequirementIds().contains(3));
		assertTrue(test.getRequirementIds().contains(4));
		assertTrue(test.getRequirementIds().contains(5));
	}

	@Test
	public final void getIsFinishedTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		PlanningPokerGame test2 = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, true, true,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertFalse(test.isFinished());
		assertTrue(test2.isFinished());
	}

	@Test
	public final void getIsLiveTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		PlanningPokerGame test2 = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, true, true,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertFalse(test.isLive());
		assertTrue(test2.isLive());
	}

	@Test
	public final void getStartDateTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertEquals(2014, test.getStartDate().get(GregorianCalendar.YEAR));
		assertEquals(3, test.getStartDate().get(GregorianCalendar.MONTH));
		assertEquals(20, test.getStartDate()
				.get(GregorianCalendar.DAY_OF_MONTH));
	}

	@Test
	public final void getEndDateTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertEquals(2014, test.getEndDate().get(GregorianCalendar.YEAR));
		assertEquals(5, test.getEndDate().get(GregorianCalendar.MONTH));
		assertEquals(28, test.getEndDate().get(GregorianCalendar.DAY_OF_MONTH));
	}

	@Test
	public final void getModeratorTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("test game name",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertTrue(test.getModerator().equals("ajthompson"));
	}

	/**
	 * The constructor should convert ";"s in the game name to ":"s
	 */
	@Test
	public final void gameNameColonConversionTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("2014-05-28 11;08;15",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertTrue(test.getGameName().equals("2014-05-28 11:08:15"));
	}

	@Test
	public final void getDeckValuesTest() {
		// TODO currently based on the mock data returned by the method
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("2014-05-28 11;08;15",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");

		assertTrue(test.getDeckValues().contains(1));
		assertTrue(test.getDeckValues().contains(2));
		assertTrue(test.getDeckValues().contains(3));
		assertTrue(test.getDeckValues().contains(5));
		assertTrue(test.getDeckValues().contains(8));
	}

	@Test
	public final void hasEndDateTest() {
		// TODO currently based on the mock data returned by the method
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("2014-05-28 11;08;15",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");
		PlanningPokerGame test2 = new PlanningPokerGame("2014-05-28 11;08;15",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(9999,
						5, 28), "ajthompson");

		assertEquals(true, test.hasEndDate());
		assertEquals(false, test2.hasEndDate());
	}

	@Test
	public final void toJsonTest() {
		List<Integer> testIds = new ArrayList<Integer>() {
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(5);
			}
		};
		PlanningPokerGame test = new PlanningPokerGame("2014-05-28 11;08;15",
				"test description", "fibonacci", testIds, false, false,
				new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
						5, 28), "ajthompson");
		PlanningPokerGame nulltest = new PlanningPokerGame("null test", null,
				"fibonacci", null, false, false, new GregorianCalendar(2014, 3,
						20), new GregorianCalendar(2014, 5, 28), "ajthompson");

		String testString = test.toJSON();
		String nullString = nulltest.toJSON();

		assertTrue(testString.contains("gameName"));
		assertTrue(testString.contains("moderator"));
		assertTrue(testString.contains("description"));
		assertTrue(testString.contains("deckType"));
		assertTrue(testString.contains("rquirements"));
		assertTrue(testString.contains("isFinished"));
		assertTrue(testString.contains("isLive"));
		assertTrue(testString.contains("startDate"));
		assertTrue(testString.contains("endDate"));
	}
}
