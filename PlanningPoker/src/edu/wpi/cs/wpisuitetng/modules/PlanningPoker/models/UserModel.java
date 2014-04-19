/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;

/**
 * A singleton class used to read from or add to the set of planning poker games
 * stored in the database.
 *
 * @author sfmailand
 * @version $Revision: 1.0 $
 */
public class UserModel {

	// Dictionary mapping each game's name to it's instance.
	private static HashMap<String, User> userDict = new HashMap<String, User>();

	/**
	 * Adds a single User to the project's set of games.
	 *
	 * @param newGame
	 *            The User to be added to the list of
	 *            Users in the project
	 */
	public static void addUser(User newUser) {
		// Add the planning poker game to the local dictionary.
		UserModel.userDict.put(
				newUser.getUserName(), newUser);
	}
	
	/**
	 * Method addUsers.
	 * @param games User[]
	 */
	public static void addUsers(User[] users) {
		for (User user : users) {
			UserModel.addUser(user);
		}
	}

	/**
	 * Returns the User with the given name.
	 *
	 * @param name
	 *            The name of the User to be returned.
	
	
	 * @return The User instance with the name provided. * @throws NotFoundException
	 *             If no game with the given name is found. */
	public static User getUser(String user) {
		User toReturn = UserModel.userDict
				.get(user);

		return toReturn;
	}

	/**
	 * Returns the list of the Users
	 *
	
	 * @return the Users held within the UserModel. */
	public static ArrayList<User> getUsers() {
		return new ArrayList<User>(
				UserModel.userDict.values());
	}

	/**
	 * Method getSize.
	
	 * @return int */
	public static int getSize() {
		return UserModel.userDict.size();
	}

	public static void emptyModel() {
		UserModel.userDict.clear();
	}

}