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

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author friscis
 * @author swconley
 *
 */
public class AddPlanningPokerFinalEstimateRequestObserver implements RequestObserver{
	
	private AddPlanningPokerFinalEstimateController controller;

	/**
	 * Constructs the observer given an AddPlanningPokerVoteController
	 * 
	 * @param controller
	 *            the controller used to add PlanningPokerVotes
	 */
	public AddPlanningPokerFinalEstimateRequestObserver(
			AddPlanningPokerFinalEstimateController controller) {
		this.controller = controller;
	}

	/**
	 * Parse the PlanningPokerVote that was received from the server then pass
	 * them to the controller.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();

	}

	/**
	 * Takes an action if the response results in an error. Specifically,
	 * outputs that the request failed.
	 * 
	 * @param iReq
	 *            IRequest
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err
				.println("The request to add a PlanningPokerFinalEstimate failed. Response error!");
	}

	/**
	 * Takes an action if the response fails. Specifically, outputs that the
	 * request failed.
	 * 
	 * @param iReq
	 *            IRequest
	 * @param exception
	 *            Exception
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest,
	 *      Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err
				.println("The request to add a PlanningPokerFinalEstimate failed. Not able to connect!");
	}
}
