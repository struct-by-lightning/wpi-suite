/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor: team struct-by-lightning
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Alec Thompson
 * 
 * @version $Revision: 1.0 $
 */
public class GetPlanningPokerUserController implements ActionListener {

	private GetPlanningPokerUserRequestObserver observer;
	private static GetPlanningPokerUserController instance = null;

	private GetPlanningPokerUserController() {
		observer = new GetPlanningPokerUserRequestObserver(this);
	}

	/**
	 * 
	 * @return the instance of the GetUserEmailsController or creates one if it
	 *         doesn't exist
	 */
	public static GetPlanningPokerUserController getInstance() {
		if (instance == null) {
			instance = new GetPlanningPokerUserController();
		}

		return instance;
	}

	/**
	 * Sends an HTTP request to get a user email when the update button is
	 * pressed
	 * 
	 * @param e
	 *            ActionEvent
	 * 
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Send a request to the core to get this email
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokeruser", HttpMethod.GET);
		request.addObserver(observer);
		request.send();
	}

	/**
	 * Sends an HTTP request to retrieve all user emails
	 */
	public void retrieveUser() {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokeruser", HttpMethod.GET);
		request.addObserver(observer);
		request.send();
	}

	/**
	 * Add the given Users to the local model (they were received from the core
	 * 
	 * 
	 * @param Users
	 *            array of Users received from the server
	 */
	public void receivedUser(PlanningPokerUser[] Users) {

		// make sure the response was not null
		if (Users != null && Users.length != 0) {

			// empty the local model to eliminate duplications
			PlanningPokerUserModel.getInstance().emptyModel();
			
			// add the users to the local model
			PlanningPokerUserModel.getInstance().addUsers(Users);
		}
	}
}
