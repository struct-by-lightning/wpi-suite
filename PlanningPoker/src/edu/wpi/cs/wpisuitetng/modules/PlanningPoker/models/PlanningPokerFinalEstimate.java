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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonParser;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

/**
 * @author Friscis
 * @author swconley
 * @author mamora
 *
 */
public class PlanningPokerFinalEstimate extends RegularAbstractModel<PlanningPokerFinalEstimate>{
	private String gameName;
	private int estimate;
	private int requirementID;
	private boolean hasEstimate;
	
	public PlanningPokerFinalEstimate(String gameName, int requirementID) {
		hasEstimate = false;
		if(gameName != null){
			this.gameName = gameName.replace(':', ';');
		}
		
		else{
			this.gameName = null;
		}
		this.estimate = 0;
		this.requirementID = requirementID;
	}
	
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return "{\"id\":\"" + gameName + ":" + requirementID + "\",\"final\":\"" + estimate + "\"}";
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
		Scanner sc = new Scanner(toSet);
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
	
	public boolean hasEstimate() {
		return hasEstimate;
	}
	
	public int getEstimate() {
		return estimate;
	}
	
	public void setEstimate(int estimate) {
		this.estimate = estimate;
		this.hasEstimate = (estimate != 0);
	}
	
	/**
	 * This method makes a JSON string into a votemodel.
	 * It is currently fairly brittle, but should work if the standards for usernames and gamenames don't change.
	 * @param json assumes a JSON string generated from a model of this type
	
	 * @return the object form of the JSON */
	public static PlanningPokerFinalEstimate fromJSON(String json) {
		Scanner scTemp = new Scanner(json);
		System.out.println("This is the json" + json);
		// skip the boilerplate
		scTemp.useDelimiter("\\\"?[:,{}]\\\"?");
		scTemp.next();
		// get the gameName
		String retGameName = scTemp.next();
		
		// get the requirement ID
		Integer retRequirementID = Integer.parseInt(scTemp.next());
		
		scTemp.next();
		// get and format the vote
		int retEstimate = Integer.parseInt(scTemp.next());
		
		 PlanningPokerFinalEstimate est = new PlanningPokerFinalEstimate(retGameName, retRequirementID);
		 est.setEstimate(retEstimate);
		 System.out.println("This is the final estimate that has been parsed:" + est);
		 return est;
	}
	
	/**
	 * Reconstruct an array of PlanningPokerVotes from a JSON array
	 * 
	 * @param jsonArr
	 *            The JSON array to use
	
	 * @return An array of reconstructed PlanningPokerGames */
	public static PlanningPokerFinalEstimate[] fromJsonArray(String jsonArr) {
		JsonArray array = new JsonParser().parse(jsonArr).getAsJsonArray();
		System.out.println(array);
		List<PlanningPokerFinalEstimate> ppnes = new ArrayList<PlanningPokerFinalEstimate>();

		for (JsonElement json : array) {
			ppnes.add(fromJSON(json.toString()));
		}

		return ppnes.toArray(new PlanningPokerFinalEstimate[0]);
	}
	
}
