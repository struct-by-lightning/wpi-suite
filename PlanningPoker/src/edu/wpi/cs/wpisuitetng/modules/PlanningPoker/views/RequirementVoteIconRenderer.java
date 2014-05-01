/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor: team struct-by-lightning
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * A JList Requirement Vote Icon Renderer
 * @author hlong290494
 * Will render a check mark on the requirement you have voted
 */
class RequirementVoteIconRenderer extends DefaultListCellRenderer {
    private List<Requirement> requirements;
    private PlanningPokerFinalEstimate[] finalEsts;
    String gameName;
    
    private ImageIcon blankIcon;
    private ImageIcon tickIcon;
    private javax.swing.JLabel iconTab;
    
    RequirementVoteIconRenderer(List<Requirement> requirements, PlanningPokerFinalEstimate[] finalEsts) {
        this.requirements = requirements;
        this.finalEsts = finalEsts;
        
        tickIcon = new ImageIcon(getClass().getResource("accept.png"));
        blankIcon = new ImageIcon(getClass().getResource("blank.png"));
    }

    @Override
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean selected,
        boolean expanded) {

        String requirementName = (String) value;
        int currentReqID = -1;
        JLabel label;
        int thisEstimation = 0;
        
        label = new JLabel();
        label.setOpaque(true);
        
        boolean voteSubmitted = false;
        
        // Get Requirement by string
        for (Requirement r : requirements) {
        	if (r.getName().equals(requirementName)) {		
        		currentReqID = r.getId();
        		break;
        	}
        }
        
        // Assume this requirement hasn't been voted yet
        label.setIcon(blankIcon);
        
        // Loop through to see which requirement has been voted.
		for(PlanningPokerFinalEstimate ppfe : finalEsts) {
			if (this.gameName.equals(ppfe.getGameName())) {
				if (ppfe.getRequirementID() == currentReqID && ppfe.getEstimate() != 0) {
					thisEstimation = ppfe.getEstimate();
					label.setIcon(tickIcon);
					break;
				}
			}
		}
		
        label.setText(requirementName);
        
        System.out.println("Rendering requiremnent \"" + requirementName + "\". It has an ID of " + currentReqID + " and estimation of " + thisEstimation);
        
        if (!selected) {
        	label.setBackground(Color.WHITE);
        } else {
        	label.setBackground(javax.swing.UIManager.getDefaults().getColor("List.selectionBackground"));
        }

        return label;
    }
    
    public void updateFinalEstimation(PlanningPokerFinalEstimate[] finalEsts) {
    	this.finalEsts = finalEsts;
    }
    
    public void setGameName(String gameName) {
    	this.gameName = gameName;
    }
}

