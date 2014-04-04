/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.buttons;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
//import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.ViewEventController;

/**
 * @author Batyr, Christian, Francisco 
 * @version $Revision: 1.0 $
 */
public class PlanningPokerButtonsPanel extends ToolbarGroupView{
	
	// initialize the main view toolbar buttons
		private JButton newGameButton = new JButton("<html>New<br />Game</html>");
		
		private final JButton modifyGameButton = new JButton("<html>Modify<br />Game</html>");
		private final JPanel contentPanel = new JPanel();
	
	public PlanningPokerButtonsPanel(){
		super("");
		
		modifyGameButton.setEnabled(false);
		
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		this.setPreferredWidth(350);
		
		//this.modifyGameButton.setSize(200, 200);
		//this.newGameButton.setPreferredSize(new Dimension(200, 200));
		this.newGameButton.setHorizontalAlignment(SwingConstants.CENTER);
		
		try {
		    Image img = ImageIO.read(getClass().getResource("new_req.png"));
		    this.newGameButton.setIcon(new ImageIcon(img));
		    
		    img = ImageIO.read(getClass().getResource("edit.png"));
		    this.modifyGameButton.setIcon(new ImageIcon(img));
		    
		} catch (IOException ex) {
			System.out.print(ex.getMessage());
			
		}
		
		// the action listener for the New Game Button
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					ViewEventController.getInstance().createNewGame();
			}
		});		
		
		//action listener for the Modify Game Button
		modifyGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewEventController.getInstance().modifyGame();
			}
		});
			
		contentPanel.add(newGameButton);
		contentPanel.add(modifyGameButton);
		contentPanel.setOpaque(false);
		

		this.add(contentPanel);
	}
	/**
	 * Method getnewGameButton.
	
	 * @return JButton */
	public JButton getnewGameButton() {
		return newGameButton;
	}

	/**
	 * Method getmodifyGameButton.
	
	 * @return JButton */
	public JButton getmodifyGameButton() {
		return modifyGameButton;
	}

	
}
