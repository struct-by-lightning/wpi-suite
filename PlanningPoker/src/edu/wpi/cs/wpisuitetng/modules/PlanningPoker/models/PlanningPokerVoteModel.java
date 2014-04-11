/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerVoteController;

/**
 * A singleton class used to read from or add to the set of planning poker Votes
 * stored in the database.
 *
 * @author lhnguyenduc and cwalker
 */
public class PlanningPokerVoteModel {

	// Dictionary mapping each Vote's name to it's instance.
	private static HashMap<String, PlanningPokerVote> planningPokerVotesDict = new HashMap<String, PlanningPokerVote>();

	/**
	 * Adds a single PlanningPokerVote to the project's set of Votes.
	 *
	 * @param newVote
	 *            The PlanningPokerVote to be added to the list of
	 *            PlanningPokerVotes in the project
	 */
	public static void addPlanningPokerVote(PlanningPokerVote newVote) {

		// Add the planning poker Vote to the local dictionary. The key is "username"
		PlanningPokerVoteModel.planningPokerVotesDict.put(newVote.getUserName(), newVote);

		// Trigger a request to the database to add the Vote.
		AddPlanningPokerVoteController.getInstance().addPlanningPokerVote(newVote);
	}
	
	public static void addPlanningPokerVotes(PlanningPokerVote[] Votes) {
		for (PlanningPokerVote Vote : Votes) {
			PlanningPokerVoteModel.addPlanningPokerVote(Vote);
		}
	}

	/**
	 * Returns the PlanningPokerVote by a given username.
	 *
	 * @param name
	 *            The name of the username who is in the voting process of a specific requirement.
	 * @return The PlanningPokerVote instance with the name provided.
	 * @throws NotFoundException
	 *             If no Vote with the given name is found.
	 */
	public static PlanningPokerVote getPlanningPokerVote(String name) {
		PlanningPokerVote toReturn = PlanningPokerVoteModel.planningPokerVotesDict
				.get(name);

		return toReturn;
	}

	/**
	 * Returns the list of the PlanningPokerVotes
	 *
	 * @return the PlanningPokerVotes held within the PlanningPokerVoteModel.
	 */
	public static ArrayList<PlanningPokerVote> getPlanningPokerVotes() {
		return new ArrayList<PlanningPokerVote>(PlanningPokerVoteModel.planningPokerVotesDict.values());
	}

	public static int getSize() {
		return PlanningPokerVoteModel.planningPokerVotesDict.size();
	}

	public static void emptyModel() {
		PlanningPokerVoteModel.planningPokerVotesDict.clear();
	}

}