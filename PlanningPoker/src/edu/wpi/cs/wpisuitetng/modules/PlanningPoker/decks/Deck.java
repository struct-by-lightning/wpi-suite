/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.decks;

import java.util.Set;

/**
 * @author Alec Thompson
 * 
 * A deck of planning poker cards
 */
public class Deck {
	/**	Name of the deck */
	private String name;
	private Set<Card> cardSet;
	
	/**
	 * Generates the default planning poker deck
	 */
	public Deck() {
		this.name = "default";
		this.addCardArray(new int[] {1, 2, 3, 5, 8, 13});
	}
	
	/**
	 * Generates a planning poker deck with the given name, including the
	 * given array of cards.
	 * 
	 * @param name		The name of the deck
	 * @param cardArray	Array of cards to be placed into the deck
	 */
	public Deck(String name, int cardArray[]) {
		this.name = name;
		this.addCardArray(cardArray);
	}
	
	/**
	 * Adds the entered card into the deck.  No duplicate cards can be
	 * entered.
	 * 
	 * @param value	The value of the new card to be added
	 */
	public void addCard(int value) {
		cardSet.add(new Card(value));
	}
	
	/**
	 * Adds the given array of cards to the deck. No duplicate cards can be
	 * entered.
	 * 
	 * @param cardArray
	 */
	public void addCardArray(int cardArray[]) {
		for(int i = 0; i < cardArray.length; i++) {
			cardSet.add(new Card(cardArray[i]));
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the cardSet
	 */
	public Set<Card> getCardSet() {
		return cardSet;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
