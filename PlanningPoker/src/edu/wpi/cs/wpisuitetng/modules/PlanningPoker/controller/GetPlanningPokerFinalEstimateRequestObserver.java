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

import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * @version $Revision: 1.0 $
 * @author friscis
 * @author swconley
 * @author mamora
 */
public class GetPlanningPokerFinalEstimateRequestObserver implements RequestObserver{
	public static boolean isError = false;
	private final GetPlanningPokerFinalEstimateController controller;
	
	/**
	 * Constructs the observer given a GetPlanningPokerFinalEstimateController
	 * @param controller the controller used to retrieve PlanningPokerFinalEstimates
	 */
	public GetPlanningPokerFinalEstimateRequestObserver(
			GetPlanningPokerFinalEstimateController controller) {
		this.controller = controller;
	}

	/**
	 * Parse the PlanningPokerFinalEstimate out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		isError = false;
		System.out.println("Success!" + iReq.getResponse().getBody());
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		isError = true;
		System.out.println("Error!" + iReq.getResponse().getBody());
	}

	/**
	 * Put an error PlanningPokerFinalEstimate in the PostBoardPanel if the request fails.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {

	}

}
