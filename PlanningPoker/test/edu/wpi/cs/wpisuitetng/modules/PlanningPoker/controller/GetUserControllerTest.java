/*******************************************************************************
 /*******************************************************************************
 * Copyright (c) 2013-2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, struct-by-lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;

/**
 * Class to test the user controller's functionality.
 *
 * @author Lisa Batbouta
 * @version Apr 20, 2014
 */


public class GetUserControllerTest {

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController#getInstance()}.
	 * See if each instance is the same.
	 */
	@Test
	public void testGetInstance() {
		GetPlanningPokerUserController guc = GetPlanningPokerUserController.getInstance();
		assertEquals(guc.hashCode(), GetPlanningPokerUserController.getInstance().hashCode());
	}
	



	@Test
	public void testReceivedUser() {
		GetPlanningPokerUserController gc = GetPlanningPokerUserController.getInstance();
		PlanningPokerUser[] users = new PlanningPokerUser[]{
				 new PlanningPokerUser("jbond@test.com", "jbond", "1111111111", "jbond@aim.com"),
				new PlanningPokerUser("m@m.com", "m", "2222222222", "m@aim.com")
		};
		gc.receivedUser(users);
		
		List<PlanningPokerUser> pgm = PlanningPokerUserModel.getInstance().getUsers();
		assertTrue(pgm.size() == users.length);
		for (int x = 0; x < pgm.size(); x++) {
			PlanningPokerUser s1 = pgm.get(x);
			PlanningPokerUser s2 = users[x];
			
		assertEquals(s2.getID(), s1.getID());
		
		}
	}

}



