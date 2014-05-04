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

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.DefaultListModel;

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
 * @version $Revision: 1.0 $
 * @author lisabatbouta
 */
public class DeckEntityManagerTest {
	MockData db;
	Deck newDeck;
	Deck newDeck2;
	User existingUser;
	Requirement req1;
	Session defaultSession;
	String mockSsid;
	DeckEntityManager manager;
	Requirement req3;
	User bob;
	Requirement goodUpdatedRequirement;
	Session adminSession;
	Project testProject;
	Project otherProject;
	Requirement req2;

	/**
	 * Set up objects and create a mock session for testing
	
	 * @throws Exception */
	@Before
	public void setUp() throws Exception {
		User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "abc123";
		adminSession = new Session(admin, testProject, mockSsid);
		DefaultListModel<Integer> numsInDeck = new DefaultListModel<Integer>();
		numsInDeck.addElement(1);
		numsInDeck.addElement(2);
		numsInDeck.addElement(3);
		numsInDeck.addElement(4);
		DefaultListModel<Integer> numsInDeck2 = new DefaultListModel<Integer>();
		numsInDeck.addElement(2);
		numsInDeck.addElement(5);
		numsInDeck.addElement(7);
		numsInDeck.addElement(20);
		newDeck = new Deck("deck", numsInDeck);
		newDeck2 = new Deck("deck3", numsInDeck2);
		existingUser = new User("joe", "joe", "1234", 2);
		
		defaultSession = new Session(existingUser, testProject, mockSsid);
		

		db = new MockData(new HashSet<Object>());
		
		db.save(newDeck, testProject);
		db.save(existingUser);
		manager = new DeckEntityManager(db);
		
		Network.initNetwork(new MockNetwork());
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration("http://wpisuitetng"));
		IterationModel.getInstance().setBacklog(new Iteration(1, "Backlog"));
		RequirementModel.getInstance().emptyModel();
	}

	/**
	 * Stores a deck and ensures the correct data was stored
	
	 * @throws WPISuiteException */
	@Test
	public void testMakeEntity() throws WPISuiteException {
		Deck created = manager.makeEntity(defaultSession, newDeck2.toJSON());
		assertEquals("deck3", created.getID()); // IDs are unique across projects
		assertEquals("deck3", created.getDeckName());
	}

	/**
	 * Ensures a deck can be retrieved from the database
	 * @throws WPISuiteException */
	@Test
	public void testGetEntity() throws WPISuiteException {
		Deck[] gotten = manager.getEntity(defaultSession, "");
		assertSame(newDeck, gotten[0]);
	}
	
	
	
	/**
	 * gets all decks
	 * @throws WPISuiteException
	 */
	@Test
	public void getAllTest() throws WPISuiteException {
		Deck deckList[] = new Deck[2];
		deckList[0] = newDeck;
		deckList[1] = newDeck2;
		manager.save(defaultSession, newDeck);
		manager.save(defaultSession, newDeck2);
		Deck returneddeckList[] = manager.getAll(defaultSession);
		assertEquals(2, returneddeckList.length);
	}



	 

	/**
	 * Method testDeleteAllWhenEmpty.
	
	 * @throws WPISuiteException */
	@Test
	public void testDeleteAllWhenEmpty() throws WPISuiteException {
		manager.deleteAll(adminSession);
		manager.deleteAll(adminSession);
		// no exceptions
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
	public void updateDeckTest() throws WPISuiteException {
		Deck updatedDeck = manager.update(defaultSession, newDeck.toJSON());
		assertEquals(newDeck.getDeckName(), updatedDeck.getDeckName());
		assertEquals(newDeck.getID(), updatedDeck.getID());
	}



}

