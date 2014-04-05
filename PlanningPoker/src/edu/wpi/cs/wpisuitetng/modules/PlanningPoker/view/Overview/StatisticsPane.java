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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * @author sfmailand
 *
 */
public class StatisticsPane {

	JTextField estimateTextField;
	
	public StatisticsPane(JPanel infoContainer){
		JPanel resultPane = new JPanel();
		resultPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(resultPane);
		resultPane.setLayout(new BorderLayout(0, 0));

		JPanel resultLabel = new JPanel();
		resultLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		resultPane.add(resultLabel, BorderLayout.NORTH);

		JLabel lblGameResults = new JLabel("Game results");
		lblGameResults.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		resultLabel.add(lblGameResults);

		JPanel finalEstimate = new JPanel();
		finalEstimate.setBorder(new LineBorder(Color.LIGHT_GRAY));
		FlowLayout fl_finalEstimate = (FlowLayout) finalEstimate.getLayout();
		fl_finalEstimate.setAlignment(FlowLayout.LEFT);
		resultPane.add(finalEstimate, BorderLayout.SOUTH);

		JLabel lblFinalEstimate = new JLabel("Final estimate:");
		finalEstimate.add(lblFinalEstimate);

		estimateTextField = new JTextField();
		finalEstimate.add(estimateTextField);
		estimateTextField.setColumns(10);

		JButton btnSubmitResults = new JButton("Submit");
		finalEstimate.add(btnSubmitResults);

		JPanel statisticsPane = new JPanel();
		statisticsPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		resultPane.add(statisticsPane, BorderLayout.CENTER);
		statisticsPane.setLayout(new GridLayout(3, 2, 0, 0));

		JPanel meanPane = new JPanel();
		statisticsPane.add(meanPane);

		JLabel lblMean = new JLabel("Mean:");
		meanPane.add(lblMean);

		JPanel stdDevPane = new JPanel();
		statisticsPane.add(stdDevPane);

		JLabel lblStdDev = new JLabel("Std Dev:");
		stdDevPane.add(lblStdDev);

		JPanel minPane = new JPanel();
		statisticsPane.add(minPane);

		JLabel lblMin = new JLabel("Min:");
		minPane.add(lblMin);

		JPanel maxPane = new JPanel();
		statisticsPane.add(maxPane);

		JLabel lblMax = new JLabel("Max:");
		maxPane.add(lblMax);

		JPanel medianPane = new JPanel();
		statisticsPane.add(medianPane);

		JLabel lblMedian = new JLabel("Median:");
		medianPane.add(lblMedian);

		JPanel modePane = new JPanel();
		statisticsPane.add(modePane);

		JLabel lblMode = new JLabel("Mode:");
		modePane.add(lblMode);

	}
}
