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

import org.junit.Test;

/**
 * Description
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 22, 2014
 */
public class MailerTest {

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

}
