/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Struct-By-Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;

import javax.swing.AbstractListModel;



/**
 * List of planningPokerUsers pulled from the server.
 * 
 * Add functions only add planningPokerUsers to the local instance, as the planningPokerUsers already
 * exist on the server
 * 
 * @author Alec Thompson
 */
public class PlanningPokerUserModel extends AbstractListModel<PlanningPokerUser> {
	/**
	 * The list in which all the Users for a single project are contained
	 */
	private List<PlanningPokerUser> planningPokerUsers;

	/**
	 * The static object to allow the user model to exist
	 */
	private static PlanningPokerUserModel instance;

	/**
	 * Constructs an empty list of planningPokerUsers for the project
	 */
	private PlanningPokerUserModel() {
		planningPokerUsers = new ArrayList<PlanningPokerUser>();
	}

	/**
	 * 
	 * @return the instance of the PlanningPokerUser model singleton
	 */
	public static PlanningPokerUserModel getInstance() {
		if (instance == null) {
			instance = new PlanningPokerUserModel();
		}

		return instance;
	}

	/**
	 * Adds a single PlanningPokerUser to the list of planningPokerUsers for the project
	 * 
	 * @param newUser
	 *            The PlanningPokerUser to be added to the list of Users in the project
	 */
	public void addUser(PlanningPokerUser newUser) {
		planningPokerUsers.add(newUser);
	}

	/**
	 * Gets the PlanningPokerUser with the given ID
	 * 
	 * @param id
	 *            the ID number of the PlanningPokerUser to be returned
	 * 
	 * @return the PlanningPokerUser for the ID or null if the PlanningPokerUser is not found
	 */
	public PlanningPokerUser getUser(String id) {
		// iterate through the list of Users until id is found
		for (int i = 0; i < this.planningPokerUsers.size(); i++) {
			if (planningPokerUsers.get(i).getID().equals(id));
				return planningPokerUsers.get(i);
		}
		return null;
	}

	/**
	 * Removes the user with the given id
	 * 
	 * @param id
	 *            The id number of the user to be removed from the list of
	 *            planningPokerUsers.
	 */
	public void removeUser(String id) {
		// iterate through the list of Users until id is found
		for (int i = 0; i < this.planningPokerUsers.size(); i++) {
			if (planningPokerUsers.get(i).getID().equals(id)) {
				planningPokerUsers.remove(i);
				break;
			}
		}
	}

	/**
	 * Provides the number of elements in the list of Users for the project.
	 *
	 * @return the number of Users in the project * @see
	 *         javax.swing.ListModel#getSize() * @see
	 *         javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return planningPokerUsers.size();
	}

	/**
	 * This function takes an index and finds the PlanningPokerUser in the list of Users for
	 * the project.
	 * 
	 * @param index
	 *            int
	 * @return the PlanningPokerUser associated with the provided index * @see
	 *         javax.swing.ListModel@getElementAt(int) * @see
	 *         javax.swing.ListModel#getElementAt(int)
	 */
	public PlanningPokerUser getElementAt(int index) {
		return planningPokerUsers.get(planningPokerUsers.size() - 1 - index);
	}

	/**
	 * Removes all Users from this model
	 * 
	 * NOTE: Once does not simply construct a new instance of the model. Other
	 * classes reference it, so we must manually remove each PlanningPokerUser from the
	 * model.
	 */
	public void emptyModel() {
		int oldSize = getSize();
		Iterator<PlanningPokerUser> iterator = planningPokerUsers.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
	}

	/**
	 * Adds the given array of planningPokerUsers to the list
	 * 
	 * @param Users
	 *            the array of Users to add
	 */
	public void addUsers(PlanningPokerUser[] users) {
		for (PlanningPokerUser u : users) {
			this.planningPokerUsers.add(u);
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
	}

	/**
	 * Returns the list of Users
	 * 
	 * @return the Users held within the PlanningPokerUserModel
	 */
	public List<PlanningPokerUser> getUsers() {
		return planningPokerUsers;
	}
}
