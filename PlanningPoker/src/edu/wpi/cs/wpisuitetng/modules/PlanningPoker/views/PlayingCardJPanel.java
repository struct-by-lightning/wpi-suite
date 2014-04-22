/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Miguel and Lisa and Christian
 * @version $Revision: 1.0 $
 */
public class PlayingCardJPanel extends JPanel {

	private final int value;
	private boolean selected;
	private Image img;
	private GroupLayout innerCardPanelLayout;
	private GroupLayout thisLayout;

	/**
	 * Inner panels for displaying this card.
	 */
	private final JPanel innerCardPanel;
	private final JLabel cardLabel;

	/**
	 * Constructor for PlayingCardJPanel.
	 * 
	 * @param value
	 *            int
	 * @param selected
	 *            boolean
	 */

	public PlayingCardJPanel(int value, boolean selected) {
		this.value = value;
		this.selected = selected;
		this.updateBorder();
		this.innerCardPanel = new CardImgPanel();
		this.cardLabel = new JLabel();
		this.innerCardPanelLayout = new GroupLayout(innerCardPanel);
		this.thisLayout = new GroupLayout(this);

		setupCardlabel();
		setupInnerCardPanelBorder();
		setupInnerCardLayoutVertical();
		setupInnerCardLayoutHorizontal();
		setupThisLayoutHorizontal();
		setupThisLayoutVertical();

	}
	/**
	 * sets the label format for the cars in the playing card panel
	 */

	private void setupCardlabel() {
		cardLabel.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
		cardLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		cardLabel.setText(Integer.toString(value));

	}
	
	/**
	 * sets the inner card panel border
	 */
	private void setupInnerCardPanelBorder(){
		innerCardPanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));
	}
	/**
	 * sets the horizontal inner card layout
	 */

	private void setupInnerCardLayoutHorizontal() {
		innerCardPanel.setLayout(innerCardPanelLayout);
		innerCardPanelLayout
				.setHorizontalGroup(innerCardPanelLayout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						innerCardPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(cardLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										92, Short.MAX_VALUE).addContainerGap()));

	}

	/**
	 * sets vertical inner card layout
	 */
	private void setupInnerCardLayoutVertical() {
		innerCardPanelLayout
				.setVerticalGroup(innerCardPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								innerCardPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												cardLabel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												131, Short.MAX_VALUE)
										.addContainerGap()));
	}
	
	/**
	 * sets the overall panel horizontal layout
	 */

	private void setupThisLayoutHorizontal() {
		this.setLayout(thisLayout);
		thisLayout.setHorizontalGroup(thisLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				innerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.PREFERRED_SIZE));

	}

	/**
	 * sets the overall vertical layout
	 */
	private void setupThisLayoutVertical() {
		thisLayout.setVerticalGroup(thisLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				innerCardPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.PREFERRED_SIZE));
	}
	
	/**
	 * allows you to toggle
	 */

	public void toggle() {
		this.selected = !this.selected;
		this.updateBorder();
	}

	/**
	 * allows user to select
	 */
	public void select() {
		this.selected = true;
		this.updateBorder();
	}

	/**
	 * allows you to deselect
	 */
	public void deselect() {
		this.selected = false;
		this.updateBorder();
	}

	/** 
	 * updates the border
	 */
	private void updateBorder() {
		Color borderColor = (this.selected ? new Color(204, 255, 204)
				: Color.white);
		this.setBorder(BorderFactory.createLineBorder(borderColor, 10));
	}

	/**
	 * Method getValue.
	 * 
	 * @return int
	 */
	public int getValue() {
		return (this.selected ? value : 0);
	}

}
