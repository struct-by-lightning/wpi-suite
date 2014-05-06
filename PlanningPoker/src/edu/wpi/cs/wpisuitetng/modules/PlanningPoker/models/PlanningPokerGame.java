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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.DefaultListModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetDeckController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * A model to store the information for a planning poker game
 * 
 * @author Miguel
 * @version $Revision: 1.0 $
 */
public class PlanningPokerGame extends RegularAbstractModel<PlanningPokerGame> {
	/** Name for the game (primary key) */
	private String gameName;

	/** Description for the game */
	private String description;

	/** Selected deck */
	private String deckType;

	/** Requirement IDs associated with this game */
	private List<Integer> requirementIds;

	/** Whether the game is finished */
	private boolean isFinished;

	/** Whether the game is currently active */
	private boolean isLive;

	/** The date the game was started */
	private GregorianCalendar startDate;

	/** The date the game ended or is scheduled to end */
	private GregorianCalendar endDate;

	/** The moderator of the planning poker game */
	private String moderator;

	private boolean isArchived;

	/**
	 * Constructor for the game
	 * 
	 * @param gameName
	 *            Name of the game
	 * @param description
	 *            Description for the game
	 * @param deckType
	 *            Selected deck
	 * @param requirementsIds
	 *            Requirement IDs associated with this game
	 * @param isFinished
	 *            Whether the game is finished
	 * @param isLive
	 *            Whether the game is currently active
	 * @param startDate
	 *            The date the game was started
	 * @param endDate
	 *            The date the game was started
	 * @param moderator
	 *            String
	 */
	public PlanningPokerGame(String gameName, String description,
			String deckType, List<Integer> requirementsIds, boolean isFinished,
			boolean isLive, GregorianCalendar startDate,
			GregorianCalendar endDate, String moderator) {

		requirementIds = new ArrayList<Integer>();
		if (gameName != null) {
			this.gameName = gameName.replace(';', ':');
		}
		this.setDescription(description);
		this.setDeckType(deckType);
		if (requirementsIds != null) {
			for (Integer r : requirementsIds) {
				this.addRequirementId(r);
			}
		}
		this.setFinished(isFinished);
		this.setLive(isLive);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setModerator(moderator);
		isArchived = false;
	}

	/**
	 * @return A list of the requirements which are associated with this
	 *         planning poker game.
	 **/
	public List<Requirement> getRequirements() {

		// Make sure requirements have been loaded from the database.
		// Need to make this wait rather than potentially infinite-loop! - Ryan
		GetRequirementsController.getInstance().retrieveRequirements();
		for(int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (RequirementModel.getInstance().getRequirements().size() > 1
					|| RequirementModel.getInstance().getRequirements().get(0) == null) {
				break;
			}
		}

		final List<Requirement> toReturn = new ArrayList<Requirement>();

		for (int id : requirementIds) {
			toReturn.add(RequirementModel.getInstance().getRequirement(id));
		}

		return toReturn;
	}

	/**
	 * Method getDeckValues.
	 * 
	 * @return ArrayList<Integer>
	 */
	public List<Integer> getDeckValues() {

		GetDeckController.getInstance().retrieveDeck();
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
		}
		// TODO This method returns mock data, and needs to be correctly implemented.
		DefaultListModel<Integer> deckNumbers = DeckModel.getInstance().getDeck(deckType).getDeckNumbers();
		
		ArrayList<Integer> deckValues = new ArrayList<Integer>();
		
		Integer[] deckArray = new Integer[deckNumbers.size()];
		
		deckNumbers.copyInto(deckArray);
		
		for(int i = 0; i < deckArray.length; i++){
			deckValues.add(deckArray[i]);
		}
		
		return deckValues;

	}

	/**
	 * Method hasEndDate.
	 * 
	 * @return boolean
	 */
	public boolean hasEndDate() {
		// TODO This method returns mock data, and needs to be correctly
		// implemented.
		if (endDate.get(Calendar.YEAR) == 9999) {
			return false;
		}
		return true;
	}
	
	/** Clears the end date on this planning poker game
	 */
	public void clearEndDate() {
		this.endDate = new GregorianCalendar(9999, 11, 18);
	}

	/**
	 * Method getSelectedCardIndices.
	 * 
	 * @param user
	 *            Object
	 * @param selectedRequirement
	 *            Requirement
	 * 
	 * @return ArrayList<Integer>
	 */
	public List<Integer> getSelectedCardIndices(Object user,
			Requirement selectedRequirement) {
		// TODO This method returns mock data, and needs to be correctly
		// implemented.

		return new ArrayList<Integer>();
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		final PlanningPokerSerializer pps = new PlanningPokerSerializer();
		return pps.serialize(this, null, null).toString();
	}

	/**
	 * Reconstruct a PlanningPokerGame from its JSON representation
	 * 
	 * @param json
	 *            The JSON to use
	 * 
	 * @return The reconstructed PlanningPokerGame
	 */
	public static PlanningPokerGame fromJSON(String json) {
		final PlanningPokerDeserializer ppd = new PlanningPokerDeserializer();
		return ppd.deserialize(new JsonParser().parse(json), null, null);
	}

	/**
	 * Reconstruct an array of PlanningPokerGames from a JSON array
	 * 
	 * @param jsonArr
	 *            The JSON array to use
	 * 
	 * @return An array of reconstructed PlanningPokerGames
	 */
	public static PlanningPokerGame[] fromJsonArray(String jsonArr) {
		final PlanningPokerDeserializer ppd = new PlanningPokerDeserializer();
		final JsonArray array = new JsonParser().parse(jsonArr)
				.getAsJsonArray();
		final List<PlanningPokerGame> ppgs = new ArrayList<PlanningPokerGame>();

		for (JsonElement json : array) {
			ppgs.add(ppd.deserialize(json, null, null));
		}

		return ppgs.toArray(new PlanningPokerGame[0]);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getID()
	 */
	@Override
	public String getID() {
		return gameName;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#setID(java.lang.String)
	 */
	@Override
	public void setID(String toSet) {
		gameName = toSet;
	}

	/**
	 * Method getModerator.
	 * 
	 * @return String
	 */
	public String getModerator() {
		return moderator;
	}

	/**
	 * Get the game's name
	 * 
	 * 
	 * @return The game's name
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * Set the game's name
	 * 
	 * @param toSet
	 *            The new name for the game
	 */
	public void setGameName(String toSet) {
		gameName = toSet;
	}

	/**
	 * Get the game's description
	 * 
	 * 
	 * @return The game's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the game's description
	 * 
	 * @param description
	 *            The new description for the game
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getPrimaryKey()
	 */
	@Override
	public String getPrimaryKey() {
		return "gameName";
	}

	/**
	 * Get the selected deck for the game
	 * 
	 * 
	 * @return The selected deck
	 */
	public String getDeckType() {
		return deckType;
	}

	/**
	 * Select a new deck for the game
	 * 
	 * @param deckType
	 *            The new deck to select
	 */
	public void setDeckType(String deckType) {
		this.deckType = deckType;
	}

	/**
	 * Get the game's associated requirement IDs
	 * 
	 * 
	 * @return A list of the game's requirement IDs
	 */
	public List<Integer> getRequirementIds() {
		return requirementIds;
	}

	public void setRequirementIds(List<Integer> requirementIds) {
		this.requirementIds = requirementIds;
	}

	/**
	 * Add a requirement to the game
	 * 
	 * @param requirementID
	 *            The requirement to add
	 */
	public void addRequirementId(Integer requirementID) {
		requirementIds.add(requirementID);
	}

	/**
	 * Remove a requirement from the game
	 * 
	 * @param requirementID
	 *            The requirement to remove
	 * 
	 * @return {@code true} if the game had the specified requirement
	 */
	public boolean removeRequirement(Integer requirementID) {
		return requirementIds.remove(requirementID);
	}

	/**
	 * Get whether the game is finished
	 * 
	 * 
	 * @return {@code true} if the game is finished
	 */
	public boolean isFinished() {
		return isFinished;
	}

	/**
	 * Set whether the game is finished
	 * 
	 * @param isFinished
	 *            {@code true} if the game should be finished
	 */
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	/**
	 * Get whether the game is live
	 * 
	 * 
	 * @return {@code true} if the game is live
	 */
	public boolean isLive() {
		return isLive;
	}

	/**
	 * Method setModerator.
	 * 
	 * @param moderator
	 *            String
	 */
	public void setModerator(String moderator) {
		this.moderator = moderator;
	}

	/**
	 * Set whether the game is live
	 * 
	 * @param isLive
	 *            {@code true} if the game should be live
	 */
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	/**
	 * Get the start date of the game
	 * 
	 * 
	 * @return The start date of the game
	 */
	public GregorianCalendar getStartDate() {
		return startDate;
	}

	/**
	 * Set the start date of the game
	 * 
	 * @param startDate
	 *            The new start date for the game
	 */
	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * Get the end date of the game
	 * 
	 * 
	 * @return The end date of the game
	 */
	public GregorianCalendar getEndDate() {
		return endDate;
	}

	/**
	 * Set the end date of the game
	 * 
	 * @param endDate
	 *            The new end date for the game
	 */
	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public boolean isArchived() {
		return isArchived;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PlanningPokerGame) {
			// game name cannot be null
			if (this.gameName.equals(((PlanningPokerGame) o).gameName)) {
				// other values cannot be null
				if (this.description != null
						&& !this.description
								.equals(((PlanningPokerGame) o).description))
					return false;
				if (this.deckType != null
						&& !this.deckType
								.equals(((PlanningPokerGame) o).deckType))
					return false;
				if (this.requirementIds != null
						&& !this.requirementIds
								.equals(((PlanningPokerGame) o).requirementIds))
					return false;
				if (this.isFinished != ((PlanningPokerGame) o).isFinished)
					return false;
				if (this.isLive != ((PlanningPokerGame) o).isLive)
					return false;
				if (this.startDate != null
						&& !this.startDate
								.equals(((PlanningPokerGame) o).startDate))
					return false;
				if (this.endDate != null
						&& !this.endDate
								.equals(((PlanningPokerGame) o).endDate))
					return false;
				if (this.moderator != null
						&& !this.moderator
								.equals(((PlanningPokerGame) o).moderator))
					return false;
				if (this.isArchived != ((PlanningPokerGame) o).isArchived)
					return false;
				return true;
			}
			return false;
		}
		return false;
	}

}
