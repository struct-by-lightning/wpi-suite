/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor: team struct-by-lightning
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
<<<<<<< HEAD
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
=======
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTextPane;

import java.awt.Font;

import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
>>>>>>> upstream/sbl-dev

public class OverviewPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public OverviewPanel() {

		JTree gamesTree = new GameJTree(new DefaultTreeModel(
				new DefaultMutableTreeNode("All")));

<<<<<<< HEAD
=======
		JTextPane txtpnLoggedInAs = new JTextPane();
		txtpnLoggedInAs.setText("Logged in as: "+ConfigManager.getConfig().getUserName());
		txtpnLoggedInAs.setFocusable(false);
		add(txtpnLoggedInAs, BorderLayout.SOUTH);
		
		/**
		 * panel that contains games in a tree structure
		 */
>>>>>>> upstream/sbl-dev
		JPanel yourGames = new JPanel();
		yourGames.setMinimumSize(new Dimension(150, 10));
		yourGames.setBorder(new LineBorder(Color.LIGHT_GRAY));
		yourGames.setLayout(new BorderLayout(0, 0));
		yourGames.add(gamesTree);

		JLabel lblSessionNameCreatedBy = new JLabel(
				"SESSION_NAME created by MODERATOR on DATE_CREATED");
		lblSessionNameCreatedBy
				.setFont(new Font("Lucida Grande", Font.BOLD, 15));

		JPanel gameName = new JPanel();
		gameName.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gameName.add(lblSessionNameCreatedBy);

		JPanel infoContainer = new JPanel();
		infoContainer.setLayout(new GridLayout(2, 2, 5, 5));

		JPanel gameContainer = new JPanel();
		gameContainer.setLayout(new BorderLayout(0, 0));
		gameContainer.add(gameName, BorderLayout.NORTH);
		gameContainer.add(infoContainer, BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setLeftComponent(yourGames);
		splitPane.setRightComponent(gameContainer);

		this.setMinimumSize(new Dimension(800, 600));
		this.setLayout(new BorderLayout(0, 0));
		this.add(splitPane, BorderLayout.CENTER);

		/**
		 * panel that contains the information for a game's requirements
		 */
		RequirementsPane requirementsPane = new RequirementsPane(infoContainer);

		/**
		 * panel that contains information for a user to submit an estimate
		 */
		SubmitPane submitPane = new SubmitPane(infoContainer);

		/**
		 * panel that contains that statistics of a session (if the session has
		 * ended)
		 */
		StatisticsPane statisticsPane = new StatisticsPane(infoContainer);

		/**
		 * panel that contains team estimates (if the session has ended)
		 */
		AllEstimatesPane allEstimatesPane = new AllEstimatesPane(infoContainer);
	}

}
