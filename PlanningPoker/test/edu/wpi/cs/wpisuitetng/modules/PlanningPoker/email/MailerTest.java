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
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

/**
 * Description
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 22, 2014
 */
public class MailerTest {
	
	@Before
	public void setUp() {
		Network.initNetwork(new MockNetwork());
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration("http://wpisuitetng"));
	}

	@Test
	public final void countRecipientsTest() {
		Mailer m = new Mailer();
		assertEquals(0, m.countRecipients());
		m.addEmail("ajthompson@wpi.edu", true);
		assertEquals(1, m.countRecipients());
		m.addEmail("test@test.com", false);
		assertEquals(1, m.countRecipients());
		m.addEmail("test@test.com", true);
		assertEquals(2, m.countRecipients());
		m.addEmail("a@a.com", true);
		assertEquals(3, m.countRecipients());
	}

	@Test
	public void otherConstructorTest() {
		PlanningPokerGame ppg = new PlanningPokerGame("g1", "d1", "dt1",
				new ArrayList<Integer>(), false, false,
				new GregorianCalendar(), new GregorianCalendar(), "m1");
		ppg.addRequirementId(2);
		RequirementModel.getInstance().addRequirement(new Requirement());
		User admin = new User("admin", "admin", "1234", 27);
		Mailer m = new Mailer("a", "b");
		Mailer m2 = new Mailer(ppg);
		Mailer m3 = new Mailer(ppg, new Requirement[]{ new Requirement()});
	}
}
