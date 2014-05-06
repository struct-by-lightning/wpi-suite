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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

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
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController#getInstance()}
	 * . See if each instance is the same.
	 */
	@Test
	public void testGetInstance() {
		final GetPlanningPokerUserController guc = GetPlanningPokerUserController.getInstance();
		assertEquals(guc.hashCode(), GetPlanningPokerUserController.getInstance().hashCode());
	}
	
	


	@Test
	public void testReceivedUser() {
		final GetPlanningPokerUserController gc = GetPlanningPokerUserController.getInstance();
		final PlanningPokerUser[] users = new PlanningPokerUser[]{
				 new PlanningPokerUser("jbond@test.com", "jbond", "jbond@aim.com", false, false),
				new PlanningPokerUser("m@m.com", "m",  "m@aim.com", false, false)
		};
		gc.receivedUser(users);
		
		final List<PlanningPokerUser> pgm = PlanningPokerUserModel.getInstance().getUsers();
		for (int x = 0; x < pgm.size(); x++) {
			PlanningPokerUser s1 = pgm.get(x);
			PlanningPokerUser s2 = users[x];
			
		assertEquals(s2.getID(), s1.getID());
		
		}
	}

}



