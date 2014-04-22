/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: team struct-by-lightning
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.mockobjects.MockNetwork;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * The controller responds by adding a new Deck
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 10, 2014
 */
public class AddDeckController {
	/** The singleton instance of the controller */
	private static AddDeckController instance;
	/** The observer for this controller */
	private AddDeckRequestObserver observer;
	private Network network;



	/**
	 * only use setter for tests
	 * @param n1 the n1 to set
	 */
	public void setN1(Network network) {
		this.network = network;
	}

	/**
	 * Returns the singleton instance of the AddDeckController, or creates it if
	 * it does not yet exist.
	 * 
	 * 
	 * @return the singleton instance of the addDeckController
	 */
	public static AddDeckController getInstance() {
		if (instance == null)
			instance = new AddDeckController();
		return instance;
	}

	/** Construct an AddDeckController */
	private AddDeckController() {
		observer = new AddDeckRequestObserver(this);
		network = Network.getInstance();
	}
	/**
	 * adds constructor 
	 * @param isMock checks to see if it is Mock
	 */
	 public AddDeckController(boolean isMock){
		 if (isMock){
			 network = MockNetwork.getInstance();
		 }
		 
	 }

	/**
	 * Method addDeck.
	 * 
	 * @param newDeck
	 *            Deck
	 */
	public void addDeck(Deck newDeck) {
		
		final Request request = network.makeRequest("planningpoker/deck",
				HttpMethod.PUT); // PUT == create
		request.setBody(newDeck.toJSON()); // put the new Deck into the request
		request.addObserver(observer);
		request.send();
	}
}
