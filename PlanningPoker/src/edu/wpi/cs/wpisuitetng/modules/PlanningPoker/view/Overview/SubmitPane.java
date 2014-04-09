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
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerVoteController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.MainViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.SeeOpenGameViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;

/**
 * The GUI where the user can cast his vote.
 * @author sfmailand
 *
 */
public class SubmitPane extends JPanel{

	static int selectedValue;
	static PlanningPokerVote ppv;
	public SubmitPane(JPanel infoContainer){
		JPanel submitPane = new JPanel();
		submitPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(submitPane);
		submitPane.setLayout(new BorderLayout(0, 0));

		JPanel submitLabel = new JPanel();
		submitLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		submitPane.add(submitLabel, BorderLayout.NORTH);

		JLabel lblSubmitAnEstimate = new JLabel("Submit an estimate");
		lblSubmitAnEstimate.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		submitLabel.add(lblSubmitAnEstimate);

		JPanel submitButton = new JPanel();
		submitButton.setBorder(new LineBorder(Color.LIGHT_GRAY));
		submitPane.add(submitButton, BorderLayout.SOUTH);

		JButton btnSubmitEstimate = new JButton("Submit");
		btnSubmitEstimate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
	    		AddPlanningPokerVoteController.getInstance().addPlanningPokerVote(ppv);
			}
		});
		submitButton.add(btnSubmitEstimate);

		JScrollPane estimateSelector = new JScrollPane();
		submitPane.add(estimateSelector, BorderLayout.CENTER);

		JPanel estimatePanel = new JPanel();
		estimateSelector.setViewportView(estimatePanel);
		
		// Adding card faces
		LinkedList<Integer> cardFace= new LinkedList<Integer>();
		cardFace.add(1);
		cardFace.add(5);
		cardFace.add(10);
		cardFace.add(20);
		cardFace.add(50);
		cardFace.add(80);
		cardFace.add(101);
		// Button objects list
		LinkedList<JButton> cardBtns= new LinkedList<JButton>();
		for (Integer value: cardFace) {
			cardBtns.add(new JButton(value.toString()));
			// Add a listener
			
		}
		// Add the button to the panel
		try {
			Image img = ImageIO.read(getClass().getResource("wpiCardFrontWPI.png"));
			
			for (final JButton btn : cardBtns) {
				btn.setIcon(new ImageIcon(img));
				btn.setBorder(BorderFactory.createEmptyBorder());
				btn.setContentAreaFilled(false);
			    btn.setHorizontalTextPosition(JButton.CENTER);
			    btn.setVerticalTextPosition(JButton.CENTER);
			    btn.setFont(new Font("arial",Font.BOLD,23));
			    // Add a listener
				btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
			    		// Vote value
			    		int selectedValue = Integer.parseInt(btn.getText());
			    		
			    		// Requirement ID
			    		// @TODO: Get selected requirement ID
			    		int requirementID = SeeOpenGameViewController.getSelectedRequirement().getId();
			    		
			    		// Game name
			    		String gameName = MainViewController.activeGame.getGameName();
			    		
			    		// User name
			    		String userName = ConfigManager.getConfig().getUserName();
			    		
			    		// Vote
			    		ppv = new PlanningPokerVote(gameName, userName, selectedValue, requirementID);
			    		
			    		//Log
			    		System.out.println("User " + userName + " voted " + selectedValue + " for requirement" + requirementID + " in game " + gameName);
			    	}
			    });
			    // Add the button to the panel
			    estimatePanel.add(btn);
			}
		} catch (IOException ex) {
			System.out.print(ex.getMessage());
		}


	}
	
}
