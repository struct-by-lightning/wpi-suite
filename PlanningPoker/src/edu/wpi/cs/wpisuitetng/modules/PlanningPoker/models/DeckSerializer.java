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
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck;

/**
 * Description
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 10, 2014
 */
public class DeckSerializer implements JsonSerializer<Deck> {

	
	/**
	 * Method serialize.
	 * @param d Deck
	 * @param t Type
	 * @param context JsonSerializationContext
	
	 * @return JsonElement */
	@Override
	public JsonElement serialize(Deck d, Type t,
			JsonSerializationContext context) {
		final JsonObject deflated = new JsonObject();
		final JsonArray deflatedCards = new JsonArray();

		deflated.addProperty("deckName", d.getDeckName());
		
		if (d.getCards() != null) {
			System.out.println("Cards is not null");
			for (Integer card : d.getCards()) {
				deflatedCards.add(new JsonPrimitive(card));
			}
			deflated.add("cards", deflatedCards);
		}

		return deflated;
	}
}
