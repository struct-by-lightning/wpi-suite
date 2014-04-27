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


import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * Controller that sends requests to and extracts users from the GetPlanningPokerUserController controller.
 * @author Alec Thompson
 * 
 * @version $Revision: 1.0 $
 */
public class GetPlanningPokerUserRequestObserver implements RequestObserver {

	private final GetPlanningPokerUserController controller;

	/**
	 * Parse the Users out of the response body and pass them the controller
	 * 
	 * @param controller
	 *            the controller used to retrieve users
	 */
	public GetPlanningPokerUserRequestObserver(GetPlanningPokerUserController controller) {
		this.controller = controller;
	}

	/**
	 * Parse the Users out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Convert the JSON array of Users to a PlanningPokerUser object array
		final PlanningPokerUser[] users = PlanningPokerUser.fromJSONArray(iReq
				.getResponse().getBody());

		// pass these users to the controller
		controller.receivedUser(users);
	}

	/**
	
	 * @param iReq IRequest
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError
	 * (edu.wpi.cs.wpisuitetng.network.models.IRequest) */
	@Override
	public void responseError(IRequest iReq) {
		fail(iReq, null);
	}

	/**
	
	 * @param iReq IRequest
	 * @param exception Exception
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail
	 * (edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception) */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		// Do something suitable for an error condition.
	}

}
