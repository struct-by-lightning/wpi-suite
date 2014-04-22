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

import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;



/**
 * @author friscis
 * @author swconley
 * @author mamora
 *
 */
public class AddPlanningPokerFinalEstimateController{
	private static AddPlanningPokerFinalEstimateController instance;
	private AddPlanningPokerFinalEstimateRequestObserver observer;

	/**
	 * Construct an AddPlanningPokerVoteController for the given model, view
	 * pair
	 */
	private AddPlanningPokerFinalEstimateController() {
		observer = new AddPlanningPokerFinalEstimateRequestObserver(this);
	}

	/**
	 * 
	 * @return the instance of the AddPlanningPokerVoteController or creates one
	 *         if it does not exist.
	 */
	public static AddPlanningPokerFinalEstimateController getInstance() {
		if (instance == null) {
			instance = new AddPlanningPokerFinalEstimateController();
		}

		return instance;
	}

	/**
	 * This method adds a PlanningPokerVote to the server.
	 * 
	 * @param newPlanningPokerFinalEstimate
	 *            is the PlanningPokerVote to be added to the server.
	 */
	public void addPlanningPokerFinalEstimate(PlanningPokerFinalEstimate newPlanningPokerFinalEstimate) {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokerfinalestimate", HttpMethod.POST); // PUT ==
																		// create
		request.setBody(newPlanningPokerFinalEstimate.toJSON()); // put the new
														// PlanningPokerVote in
														// the body of the
														// request
		request.addObserver(observer); // add an observer to process the
										// response
		request.send();
	}
}
