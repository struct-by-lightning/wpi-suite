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

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.ProjectModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller coordinates retrieving all of the projects from the server
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 22, 2014
 */
public class GetProjectController {
	/** The observer that acts on responses to requests */
	private GetProjectRequestObserver observer;
	/** The singleton instance of the controller */
	private static GetProjectController instance = null;

	/**
	 * Gets the singleton instance of the GetProjectController, or creates one
	 * if it does not yet exist.
	 * 
	 * @return the singleton instance of the GetProjectController
	 */
	public static GetProjectController getInstance() {
		if (instance == null) {
			instance = new GetProjectController();
		}
		return instance;
	}
	
	/** Constructs the controller */
	private GetProjectController() {
		observer = new GetProjectRequestObserver(this);
	}
	
	/**
	 * Sends an HTTP request to retrieve all Projects
	 **/
	public void retrievePlanningPokerGames() {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/project", HttpMethod.GET); // GET ==
																	// read
		request.addObserver(observer); // add an observer to process the
										// response
		request.send(); // send the request
	}
	
	/**
	 * Add the given Projects to the local model (they were received
	 * from the core). This method is called by the
	 * GetProjectRequestObserver
	 * 
	 * @param projects
	 *            array of Projects received from the server
	 **/
	public void receivedPlanningPokerGames(
			Project[] projects) {
		/**
		 * Empty the local model to eliminate duplications
		 **/
		ProjectModel.getInstance().emptyModel();

		/**
		 * Make sure the response was not null
		 **/
		if (projects != null) {

			/**
			 * add the PlanningPokerGames to the local model
			 **/
			ProjectModel.getInstance().addProjects(projects);
		}
	}
}
