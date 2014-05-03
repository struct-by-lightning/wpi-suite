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
public class PlanningPokerFinalEstimateEntityManagerTest {
	MockData db;
	Deck newDeck;
	Deck newDeck2;
	User existingUser;
	Requirement req1;
	Session defaultSession;
	String mockSsid;
	PlanningPokerFinalEstimateEntityManager manager;
	Requirement req3;
	User bob;
	Requirement goodUpdatedRequirement;
	Session adminSession;
	Project testProject;
	Project otherProject;
	Requirement req2;
	PlanningPokerFinalEstimate pp1 = new PlanningPokerFinalEstimate ("game1", 3);	
	PlanningPokerFinalEstimate pp8;

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
		pp8 = new PlanningPokerFinalEstimate ("game8", 7);

		db = new MockData(new HashSet<Object>());
		//db.save(pp8, testProject);
		db.save(pp1, testProject);
		db.save(existingUser);
		manager = new PlanningPokerFinalEstimateEntityManager(db);
	}
	

	/**
	 * Stores a deck and ensures the correct data was stored
	
	 * @throws WPISuiteException */
	@Test
	public void testMakeEntity() throws WPISuiteException {
	PlanningPokerFinalEstimate created = manager.makeEntity(defaultSession, pp8.toJSON());
	assertEquals("game8", created.getGameName());
		
	}

	/**
	 * Ensures a deck can be retrieved from the database
	 * @throws WPISuiteException */
	@Test
	public void testGetEntity() throws WPISuiteException {
		PlanningPokerFinalEstimate[] gotten = manager.getEntity(defaultSession, "");
		assertSame(pp1, gotten[0]);
		
	}
	
	
	
	

	/**
	 * gets all decks
	 * @throws WPISuiteException
	 */
	@Test
	public void getAllTest() throws WPISuiteException {
		PlanningPokerFinalEstimate finalEstimateList[] = new PlanningPokerFinalEstimate[2];

		PlanningPokerFinalEstimate pp3 = new PlanningPokerFinalEstimate ("game5", 7);
		PlanningPokerFinalEstimate pp2 = new PlanningPokerFinalEstimate ("game2", 5);
		finalEstimateList[0] = pp3;
		finalEstimateList[1] = pp2;
		manager.save(defaultSession, pp3);
		manager.save(defaultSession, pp2);
		PlanningPokerFinalEstimate returnedfinalEstimateList[] = manager.getAll(defaultSession);
		assertEquals(3, returnedfinalEstimateList.length);
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
	public void updateFinalEstimateTest() throws WPISuiteException {
		PlanningPokerFinalEstimate pp1 = new PlanningPokerFinalEstimate ("game1", 1);
		PlanningPokerFinalEstimate ppa = new PlanningPokerFinalEstimate(null, 0);
		PlanningPokerFinalEstimate updatedFinalEstimate = manager.update(defaultSession, pp1.toJSON());
		assertEquals(pp1.getGameName(), updatedFinalEstimate.getGameName());
		assertEquals(pp1.getID(), updatedFinalEstimate.getID());
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
