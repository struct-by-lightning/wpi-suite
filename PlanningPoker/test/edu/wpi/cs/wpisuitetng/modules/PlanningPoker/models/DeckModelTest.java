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

import org.junit.Test;



/** Implements a test on deck models
 * 
 * @version $Revision: 1.0 $
 * @author lisabatbouta
 */
public class DeckModelTest {
	/**
	 * Test method for {@link
	 * edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.DeckModel()}. See if
	 * each instance is the same.
	 */
	@Test
	public void testGetInstance() {
		final DeckModel guc = DeckModel.getInstance();
		assertEquals(guc.hashCode(), DeckModel.getInstance().hashCode());
	}
	

}
