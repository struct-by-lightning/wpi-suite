/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
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

import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.FlowLayout;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

public class UserViewOpenGame extends JPanel {
	private JTextField estimateTextField;

	private DefaultMutableTreeNode unanswered;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public UserViewOpenGame() {
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
		JPanel requirements = new JPanel();
		requirements.setMinimumSize(new Dimension(150, 10));
		requirements.setBorder(new LineBorder(Color.LIGHT_GRAY));
		splitPane.setLeftComponent(requirements);
		
		JList list = new JList();
		list.setPreferredSize(new Dimension(150, 10));
		list.setMinimumSize(new Dimension(150, 10));
		list.setMaximumSize(new Dimension(30000, 30000));
		DefaultListModel model = new DefaultListModel();
		model.addElement("red");
		model.addElement("blue");
		requirements.setLayout(new GridLayout(0, 1, 0, 0));
		list.setModel(model);
		
		requirements.add(list);

		/**
		 * panel that contains the information for a particular game
		 */
		JPanel gameContainer = new JPanel();
		splitPane.setRightComponent(gameContainer);
		gameContainer.setLayout(new BorderLayout(0, 0));

		JPanel gameName = new JPanel();
		FlowLayout flowLayout = (FlowLayout) gameName.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		gameName.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gameContainer.add(gameName, BorderLayout.NORTH);

		JLabel lblGameName = new JLabel("GAME_NAME");
		lblGameName.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		gameName.add(lblGameName, BorderLayout.WEST);

		JPanel infoContainer = new JPanel();
		gameContainer.add(infoContainer, BorderLayout.CENTER);
		infoContainer.setLayout(new GridLayout(2, 2, 5, 5));
		
		JPanel description = new JPanel();
		infoContainer.add(description);
		
		JLabel lblReqName = new JLabel("Requirement Name:");
		
		JLabel lblEstimate = new JLabel("Estimate this requirement:");
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		GroupLayout gl_description = new GroupLayout(description);
		gl_description.setHorizontalGroup(
			gl_description.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_description.createSequentialGroup()
					.addGroup(gl_description.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_description.createSequentialGroup()
							.addContainerGap()
							.addComponent(textArea))
						.addGroup(Alignment.LEADING, gl_description.createSequentialGroup()
							.addGap(5)
							.addComponent(lblReqName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_description.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblEstimate)))
					.addContainerGap(48, Short.MAX_VALUE))
		);
		gl_description.setVerticalGroup(
			gl_description.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_description.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_description.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReqName)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEstimate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
					.addContainerGap())
		);
		description.setLayout(gl_description);
		
		JPanel estimate = new JPanel();
		infoContainer.add(estimate);


	}

	public void fireRefresh() {
		GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
		unanswered.removeAllChildren();
//		for(PlanningPokerGame game : PlanningPokerGameModel.getInstance().getPlanningPokerGames()) {
//			unanswered.add(new DefaultMutableTreeNode(game.getGameName()));
//		}
	}
}
