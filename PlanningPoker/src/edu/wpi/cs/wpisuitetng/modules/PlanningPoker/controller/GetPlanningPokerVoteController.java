/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller coordinates retrieving all of the planning poker games
 * from the server.
 *
 * @version $Revision: 1.0 $
 * @author lhnguyenduc
 * @author cgwalker
 */
public class GetPlanningPokerVoteController implements ActionListener {

	private final GetPlanningPokerVoteRequestObserver observer;
	private static GetPlanningPokerVoteController instance = null;

	/**
	 * Constructs the controller given a PlanningPokerVoteModel
	 */
	private GetPlanningPokerVoteController() {

		observer = new GetPlanningPokerVoteRequestObserver(this);
	}

	/**
	
	 * @return the instance of the GetPlanningPokerGameController or creates one if it does not
	 * exist. */
	public static GetPlanningPokerVoteController getInstance()
	{
		if(instance == null)
		{
			instance = new GetPlanningPokerVoteController();
		}

		return instance;
	}

	/**
	 * Sends an HTTP request to store a PlanningPokerGame when the
	 * update button is pressed
	 * @param e ActionEvent
	
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.retrievePlanningPokerVote();
	}

	/**
	 * Sends an HTTP request to retrieve all PlanningPokerVotes
	 * @return an array of planning poker votes attained from the server
	 */
	public PlanningPokerVote[] retrievePlanningPokerVote() {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokervote", HttpMethod.GET); // GET equals read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(request.getResponse() != null) {
			final PlanningPokerVote[] a = PlanningPokerVote
					.fromJsonArray(request.getResponse().getBody());
			return a;
		} else {
			return new PlanningPokerVote[0];
		}
	}
	/**
	 * Retrieves the planning poker vote from the server
	 * @param gameName the gameName of the vote to retrieve
	 * @param userName the userName of the vote to retrieve
	 * @param requirementID the requirementID of the vote to retrieve
	 * @return the vote if it exists, Integer.MIN_VALUE otherwise
	 */
	public int retrievePlanningPokerVote(String gameName, String userName, int requirementID) {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokervote", HttpMethod.GET); // GET equals read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(request.getResponse() != null && request.getResponse().getStatusCode() == 200) {
			final PlanningPokerVote[] a = PlanningPokerVote
					.fromJsonArray(request.getResponse().getBody());
			PlanningPokerVote ret = new PlanningPokerVote(null, null, -1, 0);
			for(PlanningPokerVote v : a) {
				if(v.getID().equalsIgnoreCase(gameName + ":" + userName + ":" + requirementID)) {
						ret = v;
				}
			}
			return ret.getVote();
		} else {
			return Integer.MIN_VALUE;
		}
	}
	
	/**
	 * Sends an HTTP request to retrieve all PlanningPokerVotes from a specific user in a specific game
	 *
	 *@param gameName The name of the game votes will be retrieved from
	 *@param userName The name of the user votes will be retrieved from
	 *@return a linked list of all the game user's votes
	 */
	public LinkedList<PlanningPokerVote> retrievePlanningPokerVoteByGameAndUser(String gameName, String userName) {
		LinkedList<PlanningPokerVote> returnData = new LinkedList<PlanningPokerVote>();
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokervote", HttpMethod.GET); // GET equals read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(request.getResponse() != null) {
			final PlanningPokerVote[] a = PlanningPokerVote
					.fromJsonArray(request.getResponse().getBody());
			
			
			for(PlanningPokerVote v : a) {
				if(v.getID().indexOf(gameName + ":" + userName) != -1) {
						returnData.add(v);
				}
			}			
			
			return returnData;
		} else {
			return returnData;
		}
	}
}