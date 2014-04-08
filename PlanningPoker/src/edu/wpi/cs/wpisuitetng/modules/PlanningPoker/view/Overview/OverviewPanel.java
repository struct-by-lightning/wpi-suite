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
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;

public class OverviewPanel extends JPanel {
	private JTextField estimateTextField;

	private DefaultMutableTreeNode unanswered;

	/**
	 * Create the panel.
	 */
	public OverviewPanel() {
		setMinimumSize(new Dimension(800, 600));
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);

		JTextPane txtpnLoggedInAs = new JTextPane();
		txtpnLoggedInAs.setText("Logged in as: "+ConfigManager.getConfig().getUserName());
		txtpnLoggedInAs.setFocusable(false);
		add(txtpnLoggedInAs, BorderLayout.SOUTH);
		
		/**
		 * panel that contains games in a tree structure
		 */
		JPanel yourGames = new JPanel();
		yourGames.setMinimumSize(new Dimension(200, 10));
		yourGames.setBorder(new LineBorder(Color.LIGHT_GRAY));
		splitPane.setLeftComponent(yourGames);
		yourGames.setLayout(new BorderLayout(0, 0));

		JTree gamesTree = new GameJTree(new DefaultTreeModel(new DefaultMutableTreeNode("All")));
		yourGames.add(gamesTree);

		/**
		 * panel that contains the information for a particular game
		 */
		JPanel gameContainer = new JPanel();
		splitPane.setRightComponent(gameContainer);
		gameContainer.setLayout(new BorderLayout(0, 0));

		

		JPanel infoContainer = new JPanel();
		gameContainer.add(infoContainer, BorderLayout.CENTER);
		infoContainer.setLayout(new GridLayout(1, 1, 5, 5));

		
		
		/**
		 * panel that contains the getting start info
		 */
			StartPage startPane = new StartPage(infoContainer);

		/**
		 * panel that contains information for first time planning poker players
		 */
			AboutPage aboutPane = new AboutPage(infoContainer);
		

	}


}
