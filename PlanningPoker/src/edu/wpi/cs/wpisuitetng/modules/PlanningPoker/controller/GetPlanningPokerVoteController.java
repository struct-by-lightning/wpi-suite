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

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
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

	private GetPlanningPokerVoteRequestObserver observer;
	private static GetPlanningPokerVoteController instance;
	private boolean ready;

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
	 * Sends an HTTP request to retrieve all PlanningPokerGames
	 */
	public void retrievePlanningPokerVote() {
		final Request request = Network.getInstance().makeRequest("planningpoker/planningpokervote", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.send(); // send the request
	}
	/**
	 * Method retrievePlanningPokerVote.
	 * @param gameName String this the name of the game
	 * @param userName String this is the user name of the player
	 * @param requirementID int this is the unique id for the requirement
	 * @return int returns the vote value of the player
 	 */
	public int retrievePlanningPokerVote(String gameName, String userName, int requirementID) {
		setReady(false);
		gameName = gameName.toLowerCase();
		userName = userName.toLowerCase();
		final Request request = Network.getInstance().makeRequest("planningpoker/planningpokervote", HttpMethod.GET); // GET == read
		request.addObserver(observer); // add an observer to process the response
		request.setBody(new PlanningPokerVote(gameName, userName, 0, requirementID).toJSON());
		request.send(); // send the request
		
		while(!isReady()) {
		}

		if(request.getResponse() != null && request.getResponse().getStatusCode() == 200) {
			return PlanningPokerVote.fromJSON(request.getBody()).getVote();
		}
		else {
			return Integer.MIN_VALUE;
		}
	}
	
	public synchronized void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public synchronized boolean isReady() {
		return ready;
	}
}
