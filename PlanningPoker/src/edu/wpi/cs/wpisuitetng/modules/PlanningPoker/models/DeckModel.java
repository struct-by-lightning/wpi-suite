/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * @version $Revision: 1.0 $
 * @author sfmailand
 */
public class DeckModel extends AbstractListModel<Deck> {

	
	private final List<Deck> decks;
	
	
	private static DeckModel instance = null;
	
	
	private DeckModel(){
		decks = new ArrayList<Deck>();
	}
	
	/** Singleton function
	 * 
	 * @return the single instance of the deck model class
	 */
	public static DeckModel getInstance(){
		if(instance == null){
			instance = new DeckModel();
		}
		return instance;
	}
	
	/** Adds a deck to the this DeckModel
	 * 
	 * @param newDeck the deck to be added
	 */
	public void addDeck(Deck newDeck){
		decks.add(newDeck);
	}
	
	/** Retrieves a deck based on the input ID
	 * 
	 * @param id the ID of the deck to return
	 * @return the deck identified
	 */
	public Deck getDeck(String id){
		for(Deck deck: decks){
			if(deck.getDeckName().equals(id)){
				return deck;
			}
		}
		
		return null;
	}
	
	/** Removes the user with the input id
	 * 
	 * @param id The ID of the user to remove
	 */
	public void removeUser(String id){
		for(int i = 0; i < decks.size(); i++){
			if(decks.get(i).getID().equals(id)){
				decks.remove(i);
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return decks.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Deck getElementAt(int index) {
		return decks.get(decks.size() - 1 -index);
	}
	
	public void emptyModel() {
		final int oldSize = getSize();
		final Iterator<Deck> iterator = decks.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
	}
	
	/** Add the given array of decks to the Deck Model
	 * @param decks the array of decks to be added
	 */
	public void addDecks(Deck[] decks) {
		for (Deck u : decks) {
			this.decks.add(u);
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
	}

	
	public List<Deck> getDecks(){
		return decks;
	}
	
}
