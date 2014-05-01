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

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.Deck;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @version $Revision: 1.0 $
 * @author sfmailand
 */
public class AddDeckController {

	private static AddDeckController instance = null;
	private final AddDeckRequestObserver observer;

	/**
	 * Construct an AddDeckController for the given model, view
	 * pair
	 */
	private AddDeckController() {
		observer = new AddDeckRequestObserver(this);
	}

	/**
	 * 
	 * @return the instance of the AddDeckController or creates one
	 *         if it does not exist.
	 */
	public static AddDeckController getInstance() {
		if (instance == null) {
			instance = new AddDeckController();
		}

		return instance;
	}

	/**
	 * This method adds a PlanningPokerGame to the server.
	 * 
	 * @param newDeck is the PlanningPokerGame to be added to the server.
	 */
	public void AddDeck(Deck newDeck) {
		final Request request = Network.getInstance().makeRequest(
				"planningpoker/deck", HttpMethod.PUT); // PUT ==
																	// create
		request.setBody(newDeck.toJSON()); // put the new
											// Deck in
											// the body of the
											// request
		request.addObserver(observer); // add an observer to process the
										// response
		request.send();
	}
}
