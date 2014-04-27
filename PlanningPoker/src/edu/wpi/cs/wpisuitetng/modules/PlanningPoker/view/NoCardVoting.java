/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerVoteController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
//import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.SeeOpenGameViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;

/**
 * @author Miguel
 * @version $Revision: 1.0 $
 */
public class NoCardVoting extends JPanel {

	static int selectedValue;
	static PlanningPokerVote ppv;
	private final JTextField textField;
	private final JPanel estimatePanel = new JPanel();
	private final PlanningPokerGame game;

	/**
	 * Constructor for NoCardVoting.
	 * 
	 * @param infoContainer
	 *            JPanel
	 */
	public NoCardVoting(JPanel infoContainer, PlanningPokerGame initialGame) {
		game = initialGame;
		final JPanel submitPane = new JPanel();
		submitPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(submitPane);
		submitPane.setLayout(new BorderLayout(0, 0));

		final JPanel submitLabel = new JPanel();
		submitLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		submitPane.add(submitLabel, BorderLayout.NORTH);

		final JLabel lblSubmitAnEstimate = new JLabel("Submit an estimate");
		lblSubmitAnEstimate.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		submitLabel.add(lblSubmitAnEstimate);

		final JPanel submitButton = new JPanel();
		submitButton.setBorder(new LineBorder(Color.LIGHT_GRAY));
		submitPane.add(submitButton, BorderLayout.SOUTH);

		final JButton btnSubmitEstimate = new JButton("Submit");
		btnSubmitEstimate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isLive() && game.isFinished()) {
					AddPlanningPokerVoteController.getInstance().addPlanningPokerVote(ppv);
				}
			}
		});
		submitButton.add(btnSubmitEstimate);

		final JScrollPane estimateSelector = new JScrollPane();
		submitPane.add(estimateSelector, BorderLayout.CENTER);

		estimateSelector.setViewportView(estimatePanel);

		textField = new JTextField();
		estimatePanel.add(textField);
		textField.setColumns(10);
		textField.setFont(new Font("Lucida Grande", Font.BOLD, 36));

		// Add the button to the panel
		estimatePanel.add(textField);

	}

	public void refresh() {
		final ActionListener[] act = textField.getActionListeners();
		if (act.length > 0) {
			textField.removeActionListener(act[0]);// remove the previous action
													// listener if it exists
		}
		// Add a listener
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Vote value
				final int selectedValue = Integer.parseInt(textField.getText());

				// Requirement ID
				// @TODO: Get selected requirement ID
				// int requirementID =
				// SeeOpenGameViewController.getSelectedRequirement().getId();

				// Game name
				final String gameName = game.getGameName();

				// User name
				final String userName = ConfigManager.getConfig().getUserName();

				// Vote
				// ppv = new PlanningPokerVote(gameName, userName,
				// selectedValue, requirementID);

				// Log
				// System.out.println("User " + userName + " voted " +
				// selectedValue + " for requirement" + requirementID +
				// " in game " + gameName);
			}
		});
		estimatePanel.repaint();
	}
}
