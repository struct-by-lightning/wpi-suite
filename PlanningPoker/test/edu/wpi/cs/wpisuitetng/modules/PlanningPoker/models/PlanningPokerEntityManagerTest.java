/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Struct-By-Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.GregorianCalendar;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

/**
 * @author lisabatbouta
 *
 */
public class PlanningPokerEntityManagerTest {

	MockData db;
	Deck newDeck;
	Deck newDeck2;
	User existingUser;
	Requirement req1;
	Session defaultSession;
	String mockSsid;
	PlanningPokerEntityManager manager;
	Requirement req3;
	User bob;
	Requirement goodUpdatedRequirement;
	Session adminSession;
	Project testProject;
	Project otherProject;
	Requirement req2;
	PlanningPokerGame game1= new PlanningPokerGame("testGameName",
			"test description", "Default Deck", null, false, false,
			new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
					5, 28), "ajthompson");
	PlanningPokerGame game2= new PlanningPokerGame("test",
			"testdescr", "Default Deck", null, false, false,
			new GregorianCalendar(2014, 3, 20), new GregorianCalendar(2014,
					5, 28), "labatbouta");

	PlanningPokerGame game5 = new PlanningPokerGame ("game",
			"description", "No Deck", null, false, false,
			new GregorianCalendar(2014, 5, 21), new GregorianCalendar(2014,
					5, 30), "miguelmora");
	PlanningPokerGame game10 = new PlanningPokerGame ("game000",
			"descriptionxxx", "No Deck", null, false, false,
			new GregorianCalendar(2015, 1, 21), new GregorianCalendar(2015,
					5, 31), "hi");

	/**
	 * Set up objects and create a mock session for testing

	 * @throws Exception */
	@Before
	public void setUp() throws Exception {
		Network.initNetwork(new MockNetwork());
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration("http://wpisuitetng"));
		IterationModel.getInstance().setBacklog(new Iteration(1, "Backlog"));
		RequirementModel.getInstance().emptyModel();

		User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "abc123";
		adminSession = new Session(admin, testProject, mockSsid);


		existingUser = new User("joe", "joe", "1234", 2);

		defaultSession = new Session(existingUser, testProject, mockSsid);


		db = new MockData(new HashSet<Object>());
		
		db.save(game2, testProject);
		db.save(existingUser);
		manager = new PlanningPokerEntityManager(db);
	}


	/**
	 * Stores a game and ensures the correct data was stored

	 * @throws WPISuiteException */
	@Test
	public void testMakeEntity() throws WPISuiteException {
		PlanningPokerGame created = manager.makeEntity(defaultSession, game1.toJSON());
		assertEquals("testGameName", created.getGameName());
		assertEquals("test description", created.getDescription());
		assertEquals("Default Deck", created.getDeckType());
		assertEquals(false, created.isFinished());
		assertEquals(false, created.isLive());
		assertEquals(new GregorianCalendar(2014, 3, 20), created.getStartDate());
		assertEquals(new GregorianCalendar(2014,
				5, 28), created.getEndDate());
		assertEquals("ajthompson", created.getModerator());

	}

	/**
	 * Ensures a game can be retrieved from the database
	 * @throws WPISuiteException */
	@Test
	public void testGetEntity() throws WPISuiteException {
		PlanningPokerGame[] gotten = manager.getEntity(defaultSession, "");
		assertSame(game2, gotten[0]);

	}

	
		/**
		 * gets all games
		 * @throws WPISuiteException
		 */
		@Test
		public void getAllTest() throws WPISuiteException {
			PlanningPokerGame gameList[] = new PlanningPokerGame[2];
	
			PlanningPokerGame game5 = new PlanningPokerGame ("game",
					"description", "No Deck", null, false, false,
					new GregorianCalendar(2014, 5, 21), new GregorianCalendar(2014,
							5, 30), "miguelmora");
			PlanningPokerGame game10 = new PlanningPokerGame ("game000",
					"descriptionxxx", "No Deck", null, false, false,
					new GregorianCalendar(2015, 1, 21), new GregorianCalendar(2015,
							5, 31), "hi");
			gameList[0] = game5;
			gameList[1] = game10;
			manager.save(defaultSession, game5);
			manager.save(defaultSession, game10);
			PlanningPokerGame returnedGameList[] = manager.getAll(defaultSession);
			assertEquals(3, returnedGameList.length);
		}







	/**
	 * Method testCount.

	 * @throws WPISuiteException */
	@Test
	public void testCount() throws WPISuiteException {
		assertEquals(0, manager.Count());

	}

	
		/**
		 * Method update deck Test.
		
		 * @throws WPISuiteException */
		@Test
		public void updateGameTest() throws WPISuiteException {
	
			PlanningPokerGame updatedFinalEstimate = manager.update(defaultSession, game1.toJSON());
			assertEquals(game1.getGameName(), updatedFinalEstimate.getGameName());
			assertEquals(game1.getID(), updatedFinalEstimate.getID());
		}
		
	/**
	 * Method testDeleteAllWhenEmpty.

	 * @throws WPISuiteException */
	@Test
	public void testDeleteAllWhenEmpty() throws WPISuiteException {
		manager.deleteAll(adminSession);
		manager.deleteAll(adminSession);
	}


}
