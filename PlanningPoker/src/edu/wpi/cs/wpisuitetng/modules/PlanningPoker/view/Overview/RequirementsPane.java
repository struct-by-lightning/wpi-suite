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
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
/**
 * This contains the list of requirements that are currently
 * in the specific planning poker session that the user can
 * select from to then case their vote
 * @author sfmailand
 *
 */
public class RequirementsPane extends JPanel {
	
	private JList <Requirement> requirementList;

	/**A list of requirements from the planning poker games that will be put into the JList 'requirementList'*/
	DefaultListModel<Requirement> listOfRequirements = new DefaultListModel<Requirement>();
	
	public RequirementsPane(JPanel infoContainer){
		JPanel requirementsPane = new JPanel();
		requirementsPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(requirementsPane);
		requirementsPane.setLayout(new BorderLayout(0, 0));

		JPanel requirementLabel = new JPanel();
		requirementLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsPane.add(requirementLabel, BorderLayout.NORTH);

		JLabel lblChooseARequirement = new JLabel("Choose a requirement to estimate");
		lblChooseARequirement.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		requirementLabel.add(lblChooseARequirement);

		JPanel requirementSelector = new JPanel();
		requirementSelector.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsPane.add(requirementSelector, BorderLayout.CENTER);
		requirementSelector.setLayout(new BorderLayout(0, 0));

		
			
		requirementList = new JList<Requirement>();
		requirementSelector.add(requirementList);
		
		

	}
	
	public void newGameSelected(PlanningPokerGame ppg) {
		
		List<Integer> reqs = ppg.getRequirements();
		GetRequirementsController.getInstance().retrieveRequirements();
		
		System.out.println(reqs);
		
		listOfRequirements.clear();
		
		for (int q : reqs) {
			listOfRequirements.addElement(RequirementModel.getInstance().getRequirement(q));
		}
		
		requirementList.setModel(listOfRequirements);		
		
	}
}
