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

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * This contains the list of requirements that are currently
 * in the specific planning poker session that the user can
 * select from to then case their vote
 * @author sfmailand
 *
 */
public class RequirementsPane extends JPanel {

	public RequirementsPane(JPanel infoContainer){
		JPanel requirementsPane = new JPanel();
		requirementsPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(requirementsPane);
		requirementsPane.setLayout(new BorderLayout(0, 0));

		JPanel requirementLabel = new JPanel();
		requirementLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsPane.add(requirementLabel, BorderLayout.NORTH);

		JLabel lblChooseARequirement = new JLabel("Choose a requirement to estimate");
		lblChooseARequirement.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		requirementLabel.add(lblChooseARequirement);

		JPanel requirementSelector = new JPanel();
		requirementSelector.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsPane.add(requirementSelector, BorderLayout.CENTER);
		requirementSelector.setLayout(new BorderLayout(0, 0));

		JList requirementList = new JList();
		requirementList.setModel(new AbstractListModel() {
			String[] values = new String[] {"stuff", "stuff", "stuff", "stuff"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		requirementSelector.add(requirementList);

	}
}
