/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Struct-By-Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This class closes open tabs
 * @author bnurbekov
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class ClosableTabComponent extends JPanel implements ActionListener {

	private final JTabbedPane tabbedPane;

	/**
	 * Create a closable tab component belonging to the given tabbedPane.
	 * The title is extracted with {@link JTabbedPane#getTitleAt(int)}.
	 * @param tabbedPane  The JTabbedPane this tab component belongs to
	 * @param typeOfPanelAdded used to identify the icon to display based on the type of Panel
	 */
	public ClosableTabComponent(JTabbedPane tabbedPane, JPanel typeOfPanelAdded) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.tabbedPane = tabbedPane;
		setOpaque(false);
		String iconPath = "";

		if (typeOfPanelAdded instanceof CreateGameView || typeOfPanelAdded instanceof NewGameView) {
			iconPath = "add.png";
		} else {
			if (typeOfPanelAdded instanceof OpenGameView) {
				iconPath = "clock.png";
			} else {
				if (typeOfPanelAdded instanceof ClosedGameView) {
					iconPath = "accept.png";
				} else {
					if (typeOfPanelAdded instanceof PreferencesView) {
						iconPath = "cog.png";
					}
				}
			}
		}
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource(iconPath));
		javax.swing.JLabel iconTab = new javax.swing.JLabel();
		iconTab.setIcon(icon);
		iconTab.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 7));
		add(iconTab);

		//create the label for the tab component 
		final JLabel label = new JLabel() {
			// display the title according to what's set on our JTabbedPane
			@Override
			public String getText() {
				final JTabbedPane tabbedPane = ClosableTabComponent.this.tabbedPane;
				final int index = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
				return index > -1 ? tabbedPane.getTitleAt(index) : "";
			}
		};
		label.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 7));
		add(label);

		//create close button
		final JButton closeButton = new JButton("\u2716");

		//specify settings for close button 
		closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.addActionListener(this);

		//add button to the tab component (not the JTabbedPane itself!)
		add(closeButton);
	}

	/**
	 * Removes tab at the selected index on action event
	 * @param e ActionEvent The event that occurred 

	 * * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)  */
	@Override
	public void actionPerformed(ActionEvent e) {
		// close this tab when close button is clicked
		final int index = tabbedPane.indexOfTabComponent(this);

		if(index > -1) {
			System.out.println(index);
			System.out.println(tabbedPane);
			System.out.println(tabbedPane.getComponentAt(index));

			Component tabView = tabbedPane.getComponentAt(index);

			if (tabView instanceof CreateGameView) {
				if (((CreateGameView) tabView).isViewHasBeenEdited()) {
					//default icon, custom title
					int n = JOptionPane.showConfirmDialog(
							this.getParent().getParent(),
							"Are you sure, you want to close the current tab?",
							"Warning",
							JOptionPane.YES_NO_OPTION);


					if (n == JOptionPane.YES_OPTION) {
						tabbedPane.removeTabAt(index);
					}
				}
				else {
					tabbedPane.removeTabAt(index);
				}
			} else {
				tabbedPane.removeTabAt(index);
			}
		}
	}

}
