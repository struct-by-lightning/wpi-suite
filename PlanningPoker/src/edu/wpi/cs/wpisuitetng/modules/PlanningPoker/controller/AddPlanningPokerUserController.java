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

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller responds by adding a new planning poker game.
 * 
 * @version $Revision: 1.0 $
 * @author Sam Mailand
 * @author Barry
 * @author Alec
 */
public class AddPlanningPokerUserController {

	private static AddPlanningPokerUserController instance = null;
	private final AddPlanningPokerUserRequestObserver observer;

	/**
	 * Construct an AddPlanningPokerUserController for the given model, view
	 * pair
	 */
	private AddPlanningPokerUserController() {
		observer = new AddPlanningPokerUserRequestObserver(this);
	}

	/**
	 * 
	 * @return the instance of the AddPlanningPokerUserController or creates one
	 *         if it does not exist.
	 */
	public static AddPlanningPokerUserController getInstance() {
		if (instance == null) {
			instance = new AddPlanningPokerUserController();
		}

		return instance;
	}

	/**
	 * This method adds a PlanningPokerGame to the server.
	 * 
	 * @param newUser is the PlanningPokerGame to be added to the server.
	 */
	public void AddUser(PlanningPokerUser newUser) {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokeruser", HttpMethod.PUT); // PUT ==
																	// create
		request.setBody(newUser.toJSON()); // put the new
											// PlanningPokerUser in
											// the body of the
											// request
		request.addObserver(observer); // add an observer to process the
										// response
		request.send();
	}
}
