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
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * Description
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 22, 2014
 */
public class GetProjectRequestObserver implements RequestObserver {
	/** the controller associated with this observer */
	private final GetProjectController controller;

	/**
	 * Construct the observer given a GetProjectController
	 * 
	 * @param controller
	 *            the controller used to retrieve Projects
	 */
	public GetProjectRequestObserver(GetProjectController controller) {
		this.controller = controller;
	}

	/**
	 * Parse the Projects out of the response body and pass them to the
	 * controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Convert the JSON array of PlanningPokerGames to a PlanningPokerGame
		// object array
		final Project[] projects = Project
				.fromJsonArray(iReq.getResponse().getBody());

		// Pass these PlanningPokerGames to the controller
		controller.receivedPlanningPokerGames(projects);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to retrieve Projects has encountered an error and had to close.");
	}

/**
	
	 * @param iReq IRequest
	 * @param exception Exception
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng
	 *      .network.models.IRequest, java.lang.Exception) */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve Projects has failed.");
	}

}
