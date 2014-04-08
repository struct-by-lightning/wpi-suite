/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.buttons.PlanningPokerButtonsPanel;

/**
 * This panel contains the refresh button
 * 
 * 
 * @author Batyrlan Nurbekov
 * @author Sam Mailand
 * @author Zachary Zapatka
 * @author Miguel Mora
 *
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends DefaultToolbarView {

    /** The refresh button */
    //private final JButton startTimer;
    PlanningPokerButtonsPanel btnsPanel = new PlanningPokerButtonsPanel();


    /**
     * Construct the panel.
     */
    public ToolbarPanel() {

        // Make this panel transparent, we want to see the JToolbar gradient beneath it
        this.setOpaque(false);
       
        /*// Construct the refresh button and add it to this panel
        startTimer = new JButton("Start Timer");
        startTimer.setHorizontalAlignment(SwingConstants.RIGHT);
        startTimer.setFont(new Font("Arial", Font.PLAIN, 15));
        startTimer.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent clicked) {
                System.out.println("Timer Started");
        	}
        });
        
        
        JLabel lblPlanningPoker = new JLabel("Planning Poker");
        lblPlanningPoker.setHorizontalAlignment(SwingConstants.LEFT);
        lblPlanningPoker.setFont(new Font("Arial", Font.PLAIN, 28));
        add(lblPlanningPoker);
        
        Component rigidArea = Box.createRigidArea(new Dimension(500, 20));
        add(rigidArea);
        add(startTimer);*/
        addGroup(btnsPanel);
    }
    
    public PlanningPokerButtonsPanel getButton(){
    	return btnsPanel;
    }
}