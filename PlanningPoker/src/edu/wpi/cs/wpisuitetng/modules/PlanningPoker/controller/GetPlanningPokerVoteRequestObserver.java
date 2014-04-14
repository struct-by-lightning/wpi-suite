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

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * This observer handles responses to requests for all PlanningPokerGames
 *
 * @version $Revision: 1.0 $
 * @author lhnguyenduc
 * @author cgwalker
 */
public class GetPlanningPokerVoteRequestObserver implements RequestObserver {
	public static boolean isError = false;
	private GetPlanningPokerVoteController controller;
	
	/**
	 * Constructs the observer given a GetPlanningPokerGamesController
	 * @param controller the controller used to retrieve PlanningPokerGames
	 */
	public GetPlanningPokerVoteRequestObserver(GetPlanningPokerVoteController controller) {
		this.controller = controller;
	}

	/**
	 * Parse the PlanningPokerGames out of the response body and pass them to the controller
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
	}

	/**
	 * Put an error PlanningPokerGame in the PostBoardPanel if the request fails.
	 * 
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		
	}

}
