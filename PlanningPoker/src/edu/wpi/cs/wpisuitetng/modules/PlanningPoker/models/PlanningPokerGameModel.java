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





/**
 * A singleton class used to read from or add to the set of planning poker games
 * stored in the database.
 *
 * @author bbiletch, atrose
 * @version $Revision: 1.0 $
 */
public class PlanningPokerGameModel {

	// Dictionary mapping each game's name to it's instance.
	private static HashMap<String, PlanningPokerGame> planningPokerGamesDict = new HashMap<String, PlanningPokerGame>();

	/**
	 * Adds a single PlanningPokerGame to the project's set of games.
	 *
	 * @param newGame
	 *            The PlanningPokerGame to be added to the list of
	 *            PlanningPokerGames in the project
	 */
	public static void addPlanningPokerGame(PlanningPokerGame newGame) {
		// Add the planning poker game to the local dictionary.
		PlanningPokerGameModel.planningPokerGamesDict.put(
				newGame.getGameName(), newGame);
	}
	
	/**
	 * Method addPlanningPokerGames.
	 * @param games PlanningPokerGame[]
	 */
	public static void addMultiplePlanningPokerGames(PlanningPokerGame[] games) {
		for (PlanningPokerGame game : games) {
			PlanningPokerGameModel.addPlanningPokerGame(game);
		}
	}

	/**
	 * Returns the PlanningPokerGame with the given name.
	 *
	 * @param name
	 *            The name of the PlanningPokerGame to be returned.
	
	
	 * @return The PlanningPokerGame instance with the name provided. * @throws NotFoundException
	 *             If no game with the given name is found. */
	public static PlanningPokerGame getPlanningPokerGame(String name) {
		PlanningPokerGame toReturn = PlanningPokerGameModel.planningPokerGamesDict
				.get(name);

		return toReturn;
	}

	/**
	 * Returns the list of the PlanningPokerGames
	 *
	
	 * @return the PlanningPokerGames held within the PlanningPokerGameModel. */
	public static ArrayList<PlanningPokerGame> getAllPlanningPokerGames() {
		return new ArrayList<PlanningPokerGame>(
				PlanningPokerGameModel.planningPokerGamesDict.values());
	}

	/**
	 * Method getSize.
	
	 * @return int */
	public static int getSize() {
		return PlanningPokerGameModel.planningPokerGamesDict.size();
	}

	public static void emptyModel() {
		PlanningPokerGameModel.planningPokerGamesDict.clear();
	}

}