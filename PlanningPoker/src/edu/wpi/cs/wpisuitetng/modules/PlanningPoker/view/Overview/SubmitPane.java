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
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/**
 * The GUI where the user can cast his vote.
 * @author sfmailand
 *
 */
public class SubmitPane extends JPanel{

	
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
		submitButton.add(btnSubmitEstimate);

		JScrollPane estimateSelector = new JScrollPane();
		submitPane.add(estimateSelector, BorderLayout.CENTER);

		JPanel estimatePanel = new JPanel();
		estimateSelector.setViewportView(estimatePanel);

		JButton btnNewButton_1 = new JButton("1");
		estimatePanel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("5");
		estimatePanel.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("10");
		estimatePanel.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("20");
		estimatePanel.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("50");
		estimatePanel.add(btnNewButton_5);

		JButton btnNewButton_6 = new JButton("80");
		estimatePanel.add(btnNewButton_6);

		JButton btnNewButton_7 = new JButton("100");
		estimatePanel.add(btnNewButton_7);

		/**
		 * Displays the cards on the overview page for a given game
		 */
		try {
		    Image img = ImageIO.read(getClass().getResource("wpiCardFrontWPI.png"));

		    btnNewButton_1.setIcon(new ImageIcon(img));
		    btnNewButton_1.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_1.setContentAreaFilled(false);
		    btnNewButton_1.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_1.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_1.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_2.setIcon(new ImageIcon(img));
		    btnNewButton_2.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_2.setContentAreaFilled(false);
		    btnNewButton_2.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_2.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_2.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_3.setIcon(new ImageIcon(img));
		    btnNewButton_3.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_3.setContentAreaFilled(false);
		    btnNewButton_3.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_3.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_3.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_4.setIcon(new ImageIcon(img));
		    btnNewButton_4.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_4.setContentAreaFilled(false);
		    btnNewButton_4.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_4.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_4.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_5.setIcon(new ImageIcon(img));
		    btnNewButton_5.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_5.setContentAreaFilled(false);
		    btnNewButton_5.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_5.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_5.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_6.setIcon(new ImageIcon(img));
		    btnNewButton_6.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_6.setContentAreaFilled(false);
		    btnNewButton_6.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_6.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_6.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_7.setIcon(new ImageIcon(img));
		    btnNewButton_7.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_7.setContentAreaFilled(false);
		    btnNewButton_7.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_7.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_7.setFont(new Font("arial",Font.BOLD,23));

		} catch (IOException ex) {
			System.out.print(ex.getMessage());
		}


	}
}
