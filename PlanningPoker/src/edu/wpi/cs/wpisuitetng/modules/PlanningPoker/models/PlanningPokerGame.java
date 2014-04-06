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
import java.util.GregorianCalendar;
import java.util.List;

import sun.util.calendar.Gregorian;
import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class PlanningPokerGame extends RegularAbstractModel<PlanningPokerGame>{

        private String gameName, description, deckType;
        private List<Requirement> requirements;
        private boolean isFinished, isLive;
        private GregorianCalendar startDate, endDate;
        public PlanningPokerGame(String gameName, String description,
        		String deckType, List<Requirement> requirements,
        		boolean isFinished, boolean isLive,
        		GregorianCalendar startDate, GregorianCalendar endDate) {
                super();
                
                this.requirements = new ArrayList<Requirement>();
                
                this.gameName = gameName;
                this.setDescription(description);
                this.setDeckType(deckType);
                if(requirements != null) {
	                for(Requirement r : requirements) {
	                	this.addRequirement(r);
	                }
                }
                this.setFinished(isFinished);
                this.setLive(isLive);
                this.setStartDate(startDate);
                this.setEndDate(endDate);
        }

	@Override
	public String toJSON() {
		PlanningPokerSerializer pps = new PlanningPokerSerializer();
		return pps.serialize(this, null, null).toString();
	}
	
	public static PlanningPokerGame fromJSON(String json) {
		PlanningPokerDeserializer ppd = new PlanningPokerDeserializer();
		return ppd.deserialize(new JsonParser().parse(json), null, null);
	}
	
	public static PlanningPokerGame[] fromJsonArray(String jsonArr) {
		PlanningPokerDeserializer ppd = new PlanningPokerDeserializer();
		JsonArray array = new JsonParser().parse(jsonArr).getAsJsonArray();
		List<PlanningPokerGame> ppgs = new ArrayList<PlanningPokerGame>();
		
		for(JsonElement json : array) {
			ppgs.add(ppd.deserialize(json, null, null));
		}
		
		return ppgs.toArray(new PlanningPokerGame[0]);
	}

	@Override
	public String getID() {
		return this.gameName;
	}

	@Override
	public void setID(String toSet) {
		gameName = toSet;
	}

	public String getGameName() {
		return this.gameName;
	}

	public void setGameName(String toSet) {
		gameName = toSet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getPrimaryKey() {
		return "gameName";
	}

	public String getDeckType() {
		return deckType;
	}

	public void setDeckType(String deckType) {
		this.deckType = deckType;
	}
	
	public List<Requirement> getRequirements() {
		return this.requirements;
	}
	
	public void addRequirement(Requirement requirement) {
		this.requirements.add(requirement);
	}
	
	public void removeRequirement(Requirement requirement) {
		this.requirements.remove(requirement);
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public GregorianCalendar getStartDate() {
		return startDate;
	}

	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}

	public GregorianCalendar getEndDate() {
		return endDate;
	}

	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}
}
