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
import java.util.Scanner;
import java.util.logging.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

/**
 * @version $Revision: 1.0 $
 * @author Friscis
 * @author swconley
 * @author mamora
 */
public class PlanningPokerFinalEstimate extends RegularAbstractModel<PlanningPokerFinalEstimate>{
	private String gameName;
	private int estimate;
	private int requirementID;
	private boolean hasEstimate;
	
	/** A constructor for the PlanningPokerFinalEstimate class
	 * 
	 * @param gameName the name of the game for this final estimate
	 * @param requirementID the ID of the requirement this estimate is for
	 */
	public PlanningPokerFinalEstimate(String gameName, int requirementID) {
		hasEstimate = false;
		if(gameName != null){
			this.gameName = gameName.replace(':', ';');
		}
		
		else{
			this.gameName = null;
		}
		estimate = 0;
		this.requirementID = requirementID;
	}
	
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		final JsonObject deflated = new JsonObject();
		deflated.addProperty("gameName", gameName);
		deflated.addProperty("requirementID", requirementID);
		deflated.addProperty("estimate", estimate);
		return deflated.toString();
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getID()
	 */
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return gameName + ":" + requirementID;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#setID(java.lang.String)
	 */
	@Override
	public void setID(String toSet) {
		// TODO Auto-generated method stub
		final Scanner sc = new Scanner(toSet);
		sc.useDelimiter("\\s*:\\s*");
		gameName = sc.next();
		requirementID = sc.nextInt();
		estimate = sc.nextInt();
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getPrimaryKey()
	 */
	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}
	
	public String getGameName() {
		return gameName;
	}
	
	public int getRequirementID() {
		return requirementID;
	}
	
	/**  This function is used to see if there is an estimate or not
	 * @return whether there is an estimate or not
	 */
	public boolean hasEstimate() {
		return hasEstimate;
	}
	
	public int getEstimate() {
		return estimate;
	}
	
	public void setEstimate(int estimate) {
		this.estimate = estimate;
		hasEstimate = (estimate != 0);
	}
	
	/**
	 * This method makes a JSON string into a votemodel. It is currently fairly
	 * brittle, but should work if the standards for usernames and gamenames
	 * don't change.
	 * 
	 * @param json
	 *            assumes a JSON string generated from a model of this type
	 * 
	 * @return the object form of the JSON
	 */
	public static PlanningPokerFinalEstimate fromJSON(JsonObject json) {
		if (!json.has("gameName")) {
			throw new JsonParseException(
					"The serialized PlanningPokerFinalEstimate did not contain the required gameName field.");
		}

		if (!json.has("requirementID")) {
			throw new JsonParseException(
					"The serialized PlanningPokerFinalEstimate did not contain the required requirementID field.");
		}
		
		if (!json.has("estimate")) {
			throw new JsonParseException(
					"The serialized PlanningPokerFinalEstimate did not contain the required estimate field.");
		}
		
		String gName = json.get("gameName").getAsString();
		int reqId;
		int estimate;
		
		try {
			reqId = json.get("requirementID").getAsInt();
		} catch (java.lang.ClassCastException e) {
			System.out.println("requirementID field is false");
		}
		
		try {
			estimate = json.get("estimate").getAsInt();
		} catch (java.lang.ClassCastException e) {
			System.out.println("estimate field is false");
		}
		
		return null;
	}
	
	/**
	 * Reconstruct an array of PlanningPokerVotes from a JSON array
	 * 
	 * @param jsonArr
	 *            The JSON array to use
	
	 * @return An array of reconstructed PlanningPokerGames */
	public static PlanningPokerFinalEstimate[] fromJsonArray(String jsonArr) {
		final JsonArray array = new JsonParser().parse(jsonArr).getAsJsonArray();
		//System.out.println(array);
		final List<PlanningPokerFinalEstimate> ppnes = new ArrayList<PlanningPokerFinalEstimate>();

		for (JsonElement json : array) {
			ppnes.add(fromJSON(json.getAsJsonObject()));
		}

		return ppnes.toArray(new PlanningPokerFinalEstimate[0]);
	}
	
}
