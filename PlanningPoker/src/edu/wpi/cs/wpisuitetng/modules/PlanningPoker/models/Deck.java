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
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

/**
 * @version $Revision: 1.0 $
 * @author sfmailand
 */
public class Deck extends RegularAbstractModel<Deck>{

	private String deckName;
	
	private ArrayList<Integer> deckNumbers = new ArrayList<Integer>();
	
	/** Constructor for the deck class
	 * 
	 * @param deckName The name of the deck
	 * @param deckNumbers The numbers the deck containers
	 */
	public Deck(String deckName, ArrayList<Integer> deckNumbers){
		this.deckName = deckName;
		this.deckNumbers = deckNumbers;
	}
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Deck.class);
	}

	/** Creates a new deck from a JSON string
	 * 
	 * @param json The string that contains the deck information
	 * @return the Deck created from the JSON data
	 */
	public static Deck fromJSON(String json){
		final Gson parser = new Gson();
		return parser.fromJson(json, Deck.class);
	}
	
	
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getID()
	 */
	@Override
	public String getID() {
		return deckName;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#setID(java.lang.String)
	 */
	@Override
	public void setID(String deckName) {
		this.deckName = deckName;
		
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getPrimaryKey()
	 */
	@Override
	public String getPrimaryKey() {
		return "deckName";
	}
	/**
	 * @return the deckName
	 */
	public String getDeckName() {
		return deckName;
	}
	/**
	 * @param deckName the deckName to set
	 */
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	/**
	 * @return the deckNumbers
	 */
	public ArrayList<Integer> getDeckNumbers() {
		return deckNumbers;
	}
	/**
	 * @param deckNumbers the deckNumbers to set
	 */
	public void setDeckNumbers(ArrayList<Integer> deckNumbers) {
		this.deckNumbers = deckNumbers;
	}
	
	/** Makes an array of Decks from a string in JSON format
	 * 
	 * @param jsonArr the string that contains the Deck data
	 * @return an array of Decks made from the JSON data
	 */
	public static Deck[] fromJSONArray(String jsonArr) {
		final JsonArray array = new JsonParser().parse(jsonArr).getAsJsonArray();
		final List<Deck> decks = new ArrayList<Deck>();

		for (JsonElement json : array) {
			decks.add(Deck.fromJSON(json.toString()));
		}

		return decks.toArray(new Deck[0]);
	}

	
}
