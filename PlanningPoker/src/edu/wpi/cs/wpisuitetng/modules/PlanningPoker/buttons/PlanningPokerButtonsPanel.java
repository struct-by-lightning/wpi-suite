/*******************************************************************************
 * Copyright (c) 2013-2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, struct-by-lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.buttons;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.MainView;

/**
 * @author Batyr, cgwalker, Francisco
 * @version $Revision: 1.0 $ Sets up toolbar buttons (including the new
 *          requirement image) for the main view and handles the initial
 *          "Create Game Button" click.
 * 
 */
public class PlanningPokerButtonsPanel extends ToolbarGroupView {

	// initialize the main view toolbar buttons
	private final JButton newGameButton = new JButton("<html>Create New Game</html>");
	private final JPanel contentPanel = new JPanel();
	private final JButton prefButton = new JButton("<html>Preferences</html>");
	private final JButton createDeckButton = new JButton("<html>Create New Deck</html>");
	
	public PlanningPokerButtonsPanel() {
		super("");

		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		this.setPreferredWidth(200);

		this.add(contentPanel);
		

		/**
		 *  the action listener for the Preferences Button
		 */
		prefButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.preferencesButtonClicked();
			}
		});		

		prefButton.setHorizontalAlignment(SwingConstants.CENTER);	

		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		this.setPreferredWidth(460);

		newGameButton.setHorizontalAlignment(SwingConstants.CENTER);
		newGameButton.setPreferredSize(new Dimension(150, 50));


		try {
			final Image img = ImageIO.read(getClass().getResource("new_req.png"));
			newGameButton.setIcon(new ImageIcon(img));
			final Image imgPref = ImageIO.read(getClass().getResource("pref.png"));
			prefButton.setIcon(new ImageIcon(imgPref));
		} catch (IOException ex) {
			System.out.print(ex.getMessage());

		}

		/**
		 *  the action listener for the New Game Button
		 */
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.createGameButtonClicked();
			}
		});
		
		
		/**
		 *  the action listener for the Create Deck Button
		 */
		createDeckButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.createDeckButtonClicked();
			}
		});

		contentPanel.add(newGameButton);
		contentPanel.add(prefButton);
		contentPanel.add(createDeckButton);
		contentPanel.setOpaque(true);

		this.add(contentPanel);
	}

	/**
	 * Method getnewGameButton.
	 * 
	 * 
	 * @return JButton
	 */
	public JButton getnewGameButton() {
		return newGameButton;
	}

}
