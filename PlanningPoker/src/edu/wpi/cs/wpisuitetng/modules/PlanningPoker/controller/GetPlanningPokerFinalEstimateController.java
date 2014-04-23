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
import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.MockNetwork;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author friscis
 * @author swconley
 * @author mamora
 *
 */
public class GetPlanningPokerFinalEstimateController {
	private Network network;
	private GetPlanningPokerFinalEstimateRequestObserver observer;
	private static GetPlanningPokerFinalEstimateController instance;

	/**
	 * Constructs the controller given a PlanningPokerVoteModel
	 */
	private GetPlanningPokerFinalEstimateController() {
		network = Network.getInstance();
		observer = new GetPlanningPokerFinalEstimateRequestObserver(this);
	}
	
	private GetPlanningPokerFinalEstimateController(boolean isMockNetwork) {
		if(isMockNetwork) {
			network = MockNetwork.getInstance();
		} else {
			network = Network.getInstance();
		}
		observer = new GetPlanningPokerFinalEstimateRequestObserver(this);
	}

	/**
	
	 * @return the instance of the GetPlanningPokerGameController or creates one if it does not
	 * exist. */
	public static GetPlanningPokerFinalEstimateController getInstance()
	{
		if(instance == null)
		{
			instance = new GetPlanningPokerFinalEstimateController();
		}

		return instance;
	}
	
	public static GetPlanningPokerFinalEstimateController getInstance(boolean isMockNetwork)
	{
		if(instance == null)
		{
			instance = new GetPlanningPokerFinalEstimateController(isMockNetwork);
		}

		return instance;
	}

	/**
	 * Sends an HTTP request to store a PlanningPokerGame when the
	 * update button is pressed
	 * @param e ActionEvent
	
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) */
	public void actionPerformed(ActionEvent e) {
		this.retrievePlanningPokerFinalEstimate();
	}

	/**
	 * Sends an HTTP request to retrieve all PlanningPokerGames
	 */
	public PlanningPokerFinalEstimate[] retrievePlanningPokerFinalEstimate() {
		final Request request = network.makeRequest("planningpoker/planningpokerfinalestimate", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
		

		Object o = request.getResponse();
		while(true) {
			o = request.getResponse();
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(o != null) {
				break;
			}
		}
		if(request.getResponse() != null) {
			//return Integer.MIN_VALUE;
			System.out.println("response exists");
			PlanningPokerFinalEstimate[] a = PlanningPokerFinalEstimate.fromJsonArray(request.getResponse().getBody());
			return a;
		} else {
			System.out.println("response is null");
			return new PlanningPokerFinalEstimate[0];
		}
	}
	/**
	 * Retrieves the planning poker vote from the server
	 * @param gameName the gameName of the vote to retrieve
	 * @param userName the userName of the vote to retrieve
	 * @param requirementID the requirementID of the vote to retrieve
	 * @return the vote if it exists, Integer.MIN_VALUE otherwise
	 */
	public int retrievePlanningPokerFinalEstimate(String gameName, int requirementID) {
		final Request request = Network.getInstance().makeRequest("planningpoker/planningpokerfinalestimate" /*+ new PlanningPokerVote(gameName, userName, 0, requirementID).getID()*/, HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(request.getResponse() != null && request.getResponse().getStatusCode() == 200) {
			//return Integer.MIN_VALUE;
			PlanningPokerFinalEstimate[] a = PlanningPokerFinalEstimate.fromJsonArray(request.getResponse().getBody());
			PlanningPokerFinalEstimate ret = new PlanningPokerFinalEstimate(null, 0);
			for(PlanningPokerFinalEstimate fe : a) {
				if(fe.getID().equalsIgnoreCase(gameName + ":"  + requirementID)) {
						ret = fe;
				}
			}
			return ret.getEstimate();
		} else {
			return Integer.MIN_VALUE;
		}
	}
}
