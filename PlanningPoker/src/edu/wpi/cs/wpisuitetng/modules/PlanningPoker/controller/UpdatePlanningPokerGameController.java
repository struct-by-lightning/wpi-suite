/*******************************************************************************
 * Copyright (c) 2013-2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, struct-by-lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;


import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This controller responds when the user clicks the Update button by
 * adding the contents of the PlanningPokerGame text fields to the model as a new
 * PlanningPokerGame.
 * @version $Revision: 1.0 $
 * @author Barry
 */
public class UpdatePlanningPokerGameController{
	
	private static UpdatePlanningPokerGameController instance = null;
	private final UpdatePlanningPokerGameRequestObserver observer;
	
	/**
	 * Construct an UpdatePlanningPokerGameController for the given model, view pair
	
	
	 */
	private UpdatePlanningPokerGameController() {
		observer = new UpdatePlanningPokerGameRequestObserver(this);
	}
	
	/**
	 * @return the instance of the UpdatePlanningPokerGameController or creates one if it does not
	 * exist. */
	public static UpdatePlanningPokerGameController getInstance()
	{
		if(instance == null)
		{
			instance = new UpdatePlanningPokerGameController();
		}
		
		return instance;
	}

	/**
	 * This method updates a PlanningPokerGame to the server.
	 * @param newPlanningPokerGame is the PlanningPokerGame to be updated to the server.
	 */
	public void updatePlanningPokerGame(PlanningPokerGame newPlanningPokerGame) 
	{
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/planningpokergame", HttpMethod.POST); // POST equals update
		request.setBody(newPlanningPokerGame.toJSON()); // put the new PlanningPokerGame in the body of the request
		request.addObserver(observer); // add an observer to process the response
		request.send(); 
	}
}
