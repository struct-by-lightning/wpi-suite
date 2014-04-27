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
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * This observer handles responses to requests for all PlanningPokerGames
 *
 * @version $Revision: 1.0 $
 */
public class GetPlanningPokerGamesRequestObserver implements RequestObserver {
	
	private final GetPlanningPokerGamesController controller;
	
	/**
	 * Constructs the observer given a GetPlanningPokerGamesController
	 * @param controller the controller used to retrieve PlanningPokerGames
	 */
	public GetPlanningPokerGamesRequestObserver(GetPlanningPokerGamesController controller) {
		this.controller = controller;
	}

	/**
	 * Parse the PlanningPokerGames out of the response body and pass them to the controller
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Convert the JSON array of PlanningPokerGames to a PlanningPokerGame object array
		final PlanningPokerGame[] PlanningPokerGames = PlanningPokerGame.fromJsonArray(iReq.getResponse().getBody());
		
		// Pass these PlanningPokerGames to the controller
		controller.receivedPlanningPokerGames(PlanningPokerGames);
		
		GetPlanningPokerGamesController.waitingOnRequest = false;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		fail(iReq, null);
		GetPlanningPokerGamesController.waitingOnRequest = false;
	}

	/**
	 * Put an error PlanningPokerGame in the PostBoardPanel if the request fails.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		GetPlanningPokerGamesController.waitingOnRequest = false;
	}

}
