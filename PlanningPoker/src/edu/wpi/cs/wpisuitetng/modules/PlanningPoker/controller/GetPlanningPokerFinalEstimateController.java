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

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @version $Revision: 1.0 $
 * @author friscis
 * @author swconley
 * @author mamora
 */
public class GetPlanningPokerFinalEstimateController {
	private final Network network;
	private final GetPlanningPokerFinalEstimateRequestObserver observer;
	private static GetPlanningPokerFinalEstimateController instance = null;

	/**
	 * Constructs the controller given a PlanningPokerFinalEstimateModel
	 */
	private GetPlanningPokerFinalEstimateController() {
		network = Network.getInstance();
		observer = new GetPlanningPokerFinalEstimateRequestObserver(this);
	}

	/**
	 * 
	 * @return the instance of the GetPlanningPokerFinalEstimateController or
	 *         creates one if it does not exist.
	 */
	public static GetPlanningPokerFinalEstimateController getInstance() {
		if (instance == null) {
			instance = new GetPlanningPokerFinalEstimateController();
		}

		return instance;
	}

	/**
	 * Sends an HTTP request to retrieve all PlanningPokerFinalEstimates
	 * 
	 * @return an array of final estimates for a planning poker session
	 */
	public PlanningPokerFinalEstimate[] retrievePlanningPokerFinalEstimate() {
		final Request request = network.makeRequest(
				"planningpoker/planningpokerfinalestimate", HttpMethod.GET); // GET
																				// ==
																				// read
		request.addObserver(observer); // add an observer to process the
										// response
		request.send(); // send the request

		Object o = request.getResponse();
		for(int i = 0; i < 1000; i++) { //enter a loop until you get a response or it times out
			o = request.getResponse();
			try { //wait a small time between updates
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(o != null) {
				break;
			}
		}
		if (o != null && request.getResponse() != null) {
			System.out.println("response exists");
			final PlanningPokerFinalEstimate[] a = PlanningPokerFinalEstimate
					.fromJsonArray(request.getResponse().getBody()); // return
																		// the
																		// response
			return a;
		} else {
			System.out.println("response is null");
			return new PlanningPokerFinalEstimate[0];
		}
	}

	/**
	 * Retrieves the planning poker final estimates from the server
	 * 
	 * @param gameName
	 *            the gameName of the final estimate to retrieve
	 * @param requirementID
	 *            the requirementID of the vote to retrieve
	 * @return the vote if it exists, Integer.MIN_VALUE otherwise
	 */
	public int retrievePlanningPokerFinalEstimate(String gameName,
			int requirementID) {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokerfinalestimate", HttpMethod.GET); // GET
																				// equals
																				// read
		request.addObserver(observer); // add an observer to process the
										// response
		request.send(); // send the request

		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (request.getResponse() != null
				&& request.getResponse().getStatusCode() == 200) {
			final PlanningPokerFinalEstimate[] a = PlanningPokerFinalEstimate
					.fromJsonArray(request.getResponse().getBody());
			PlanningPokerFinalEstimate ret = new PlanningPokerFinalEstimate(
					null, 0);
			for (PlanningPokerFinalEstimate fe : a) {
				if (fe.getID().equalsIgnoreCase(gameName + ":" + requirementID)) {
					ret = fe;
				}
			}
			return ret.getEstimate();
		} else {
			return Integer.MIN_VALUE;
		}
	}
}
