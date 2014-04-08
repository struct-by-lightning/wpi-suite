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

import java.util.Scanner;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

/**
 * @author rbkillea
 */

public class PlanningPokerVoteModel extends RegularAbstractModel<PlanningPokerVoteModel>{
	String gameName;
	String userName;
	int vote;
	int requirementID;
	
	PlanningPokerVoteModel(String gameName, String userName, int vote, int requirementID) {
		this.gameName = gameName;
		this.userName = userName.toLowerCase();
		this.vote = vote;
		this.requirementID = requirementID;
	}
	/**
	 * in ppvotemodel, this does not extend easily to the standard one given to us by a gson object
	 * @return the JSON string with id being the primary key concat of gamename and username as well as vote
	 */
	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return "{\n  \"id\": \"" + gameName + ":" + userName + ":" + requirementID + "\"\n  \"vote\": \"" + vote + "\"\n}";
	}
	/**
	 * gives the cannonical styling (as would appear in the JSON) of the primary key
	 * @return the string gameName:userName
	 */
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return gameName + ":" + userName + ":" + requirementID;
	}
	
	@Override
	public void setID(String toSet) {
		Scanner scTemp = new Scanner(toSet);
		scTemp.useDelimiter(":");
		gameName = scTemp.next();
		userName = scTemp.next();
		requirementID = scTemp.nextInt();
	}
	
	@Override
	public String getPrimaryKey() {
		return "id";
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String toSet) {
		userName = toSet.toLowerCase();
	}
	/**
	 * This method makes a JSON string into a votemodel.
	 * It is currently fairly brittle, but should work if the standards for usernames and gamenames don't change.
	 * @param json assumes a JSON string generated from a model of this type
	 * @return the object form of the JSON
	 */
	static PlanningPokerVoteModel fromJSON(String json) {
		Scanner scTemp = new Scanner(json);
		// skip the boilerplate
		scTemp.useDelimiter(": \"");
		scTemp.next();
		// get the gameName
		scTemp.useDelimiter(":");
		String retGameName = scTemp.next();
		// check if the gameName is null
		if(retGameName.equals("null"))
			retGameName = null;
		
		// get the requirement ID
		Integer retRequirementID = scTemp.nextInt();
		// get the userName
		scTemp.useDelimiter("\"\n  \"vote\": \"");
		String retUserName = scTemp.next();
		// check if the userName is null
		if(retUserName.equals("null"))
			retUserName = null;
		// get and format the vote
		scTemp.useDelimiter("\"\n}");
		int retVote = Integer.valueOf(scTemp.next());
		return new PlanningPokerVoteModel(retGameName, retUserName, retVote, retRequirementID);
	}
}
