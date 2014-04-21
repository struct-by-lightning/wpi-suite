/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Struct-By-Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.DeckModel;

/**
 * @author lisabatbouta
 * 
 */
public class AddDeckControllerTest {

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddDeckController#getInstance()}
	 * . See if each instance is the same.
	 */
	@Test
	public void testGetInstance() {
		AddDeckController gc = AddDeckController.getInstance();
		assertEquals(gc.hashCode(), AddDeckController.getInstance().hashCode());
	}

	}


