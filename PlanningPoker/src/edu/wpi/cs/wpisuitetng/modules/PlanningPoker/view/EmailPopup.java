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
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import java.util.List;

import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;

/**
 * If the user does not have an email address, creates a popup window that
 * prompts them to input an email address to receive notifications.
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 8, 2014
 */
public class EmailPopup {
	/**
	 * The singleton instance of the email popup
	 */
	private static EmailPopup instance = null;

	/**
	 * Creates a singleton instance of the email popup if it does not already
	 * exist, and then calls run on it.
	 * 
	 * For use anywhere, with a method path that has its own user of
	 * GetPlanningPokerUserController.
	 */
	public static void checkUserEmail() {
		if (instance == null) {
			instance = new EmailPopup();
		}
		instance.run();
	}

	/**
	 * Creates a singleton instance of the email popup if it does not already
	 * exist, and then calls run on it.
	 * 
	 * This is for use in NewGameTab, to run after a list of users have already
	 * been retrieved, for efficiency reasons.
	 * @param userList List<PlanningPokerUser>
	
	 * @return List<PlanningPokerUser> */
	public static List<PlanningPokerUser> checkUserEmail(List<PlanningPokerUser> userList) {
		if (instance == null) {
			instance = new EmailPopup();
		}
		return instance.run(userList);
	}

	/**
	 * Constructor
	 */
	private EmailPopup() {
	}

	/**
	 * Checks if a user has an email address.
	 * 
	
	 * @param planningPokerUser PlanningPokerUser
	 * @return false if the user's email is null, otherwise true. */
	private boolean hasEmail(PlanningPokerUser planningPokerUser) {
		if (planningPokerUser.getEmail() == null) {
			return false;
		}
		return true;
	}

	/**
	 * Runs the email checker, by retrieving the list of users, finding the
	 * currently logged in user, checking if they have an email address, and
	 * then providing a pop-up prompt if they do not. After the prompt is filled
	 * out, the user on the server is updated.
	 */
	private void run() {
		PlanningPokerUser planningPokerUser;
		String newEmail = null;
		// check if the user has an email already
		planningPokerUser = findUser();
		System.out.println("Config username: "
				+ ConfigManager.getConfig().getUserName());

		if (planningPokerUser != null) {
			if (!this.hasEmail(planningPokerUser)) {
				// get the new email
				newEmail = JOptionPane
						.showInputDialog("Enter an email address for notifications");

				// update the user locally
				planningPokerUser.setEmail(newEmail);

				// update user on server
				UpdatePlanningPokerUserController.getInstance().update(planningPokerUser);
			}
		}
	}

	/**
	 * Runs the email checker, by finding the currently logged in user, checking
	 * if they have an email address, and then providing a pop-up prompt if they
	 * do not. After the prompt is filled out, the user on the server is
	 * updated, as well as the user in the list.
	 * 
	 * For use with a pre-existing GetPlanningPokerUserController/List =
	 * PlanningPokerUserModel.getInstance().getUsers() set.
	 * 
	 * @param userList
	 *            the list of users retrieved from the server.
	
	 * @return the list of users updated with the user's new email address. */
	private List<PlanningPokerUser> run(List<PlanningPokerUser> userList) {
		PlanningPokerUser planningPokerUser;
		String newEmail = null;
		// check if the user has an email already
		planningPokerUser = findUser(userList, ConfigManager.getConfig().getUserName());
		System.out.println("Config username: "
				+ ConfigManager.getConfig().getUserName());

		if (planningPokerUser != null) {
			if (!this.hasEmail(planningPokerUser)) {
				// get the new email
				newEmail = JOptionPane
						.showInputDialog("Enter an email address for notifications");

				// remove the user from the userList
				userList.remove(planningPokerUser);

				// update the user locally
				planningPokerUser.setEmail(newEmail);

				// re-add to list
				userList.add(planningPokerUser);

				// update user on server
				UpdatePlanningPokerUserController.getInstance().update(planningPokerUser);
			}
		}

		return userList;
	}

	/**
	 * Retrieves the list of users from the server, and finds the currently
	 * logged in user, which is then returned.
	 * 
	
	 * @return the currently logged in user. */
	private PlanningPokerUser findUser() {
		GetPlanningPokerUserController.getInstance().retrieveUser();

		try {
			Thread.sleep(150);
		} catch (InterruptedException e2) {
		}

		for (PlanningPokerUser u : PlanningPokerUserModel.getInstance().getUsers()) {
			if (u.getUserName().equals(ConfigManager.getConfig().getUserName())) {
				return u;
			}
		}
		return null; // this shouldn't happen
	}

	/**
	 * Finds the given username in the given list of users from the server. This
	 * is used to find the currently logged in user and return it.
	 * 
	 * @param userList
	 *            The list of users, retrieved from the server
	 * @param username
	 *            The username of the desired user
	
	 * @return the user with the given username */
	private PlanningPokerUser findUser(List<PlanningPokerUser> userList, String username) {
		for (PlanningPokerUser u : userList) {
			if (u.getUserName().equals(username)) {
				return u;
			}
		}
		System.out.println("PlanningPokerUser not found");
		return null; // should never happen
	}
}
