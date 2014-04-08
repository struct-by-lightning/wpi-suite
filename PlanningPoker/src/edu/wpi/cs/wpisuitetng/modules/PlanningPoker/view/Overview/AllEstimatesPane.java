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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.TeamEstimateTableModel;

/**
 * 
 * This is a panel that displays all of the estimates for a planning poker session
 * This means that once a game is completed, all of the votes from the game will show
 * @author sfmailand
 * @author hlong290494
 */
public class AllEstimatesPane {

	public AllEstimatesPane(JPanel infoContainer){
		JPanel teamEstimates = new JPanel();
		teamEstimates.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(teamEstimates);
		teamEstimates.setLayout(new BorderLayout(0, 0));

		JPanel teamEstimatesLabel = new JPanel();
		teamEstimatesLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		teamEstimates.add(teamEstimatesLabel, BorderLayout.NORTH);

		JLabel lblTeamsEstimates = new JLabel("Team's estimates");
		lblTeamsEstimates.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		teamEstimatesLabel.add(lblTeamsEstimates);

		JPanel timerPane = new JPanel();
		timerPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		teamEstimates.add(timerPane, BorderLayout.SOUTH);
		timerPane.setLayout(new BorderLayout(0, 0));

		JButton btnEndVoting = new JButton("End Voting");
		timerPane.add(btnEndVoting, BorderLayout.EAST);

		JLabel lblTimer = new JLabel("TIMER_MINS_LEFT");
		timerPane.add(lblTimer, BorderLayout.WEST);

		JPanel teamResults = new JPanel();
		teamEstimates.add(teamResults, BorderLayout.CENTER);
		teamResults.setLayout(new BorderLayout(0, 0));
	
		
		JTable table = new JTable();
		table.setModel(new TeamEstimateTableModel());
		JScrollPane teamResultsPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
	
		teamResults.add(teamResultsPane, BorderLayout.CENTER);
	}
}
