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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * A JList Requirement Vote Icon Renderer
 * @author hlong290494
 * Will render a check mark on the requirement you have voted
 */
class RequirementVoteIconRenderer extends DefaultListCellRenderer {
    private List<Requirement> requirements;
    private PlanningPokerFinalEstimate[] finalEsts;
    private LinkedList<PlanningPokerVote> allVotes;
    private HashMap<Integer, Boolean> checkMap = new HashMap<Integer, Boolean>();
    private String gameName;
    private boolean inOpenGameView = false; 
    private JList originalList = null;
    
    private ImageIcon blankIcon;
    private ImageIcon tickIcon;
    private javax.swing.JLabel iconTab;
    
    /** * A constructor for requirements JList in the OpenGameView
     * 
     * @param requirements The List of Requirements name
     * @param finalEsts An array of final estimates the current user os submitted
     */
    RequirementVoteIconRenderer(List<Requirement> requirements, PlanningPokerFinalEstimate[] finalEsts) {
        this.requirements = requirements;
        this.finalEsts = finalEsts;
        
        tickIcon = new ImageIcon(getClass().getResource("accept.png"));
        blankIcon = new ImageIcon(getClass().getResource("blank.png"));
    }
    
    /**
     * A constructor for requirements JList in the OpenGameView
     * 
     * @param requirements The List of Requirements name
     * @param allVotes All the votes that the current user have submitted.
     */
    public RequirementVoteIconRenderer(List<Requirement> requirements,
			LinkedList<PlanningPokerVote> allVotes) {
		this.inOpenGameView = true;
		this.requirements = requirements;
		this.allVotes = allVotes;
		
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

        this.originalList = list;
        label = new JLabel();
        label.setOpaque(true);
        

        // Set up the label
        label.setText(requirementName);

        if (!selected) {
        	label.setBackground(Color.WHITE);
        } else {
        	label.setBackground(javax.swing.UIManager.getDefaults().getColor("List.selectionBackground"));
        }
        
        // Check to see if it's specially checked by the hashmap
        if (index > -1) {
	        if (checkMap.get(index) != null) {
	        	label.setIcon(tickIcon);
	        	
	        	return label;
	        }
        }

        
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
        if (this.inOpenGameView) {
			for(PlanningPokerVote ppv : allVotes) {
				if (ppv.getRequirementID() == currentReqID && ppv.getVote() != 0) {
					label.setIcon(tickIcon);
					break;
				}
			}       	
        } else {
			for(PlanningPokerFinalEstimate ppfe : finalEsts) {
				if (this.gameName.equals(ppfe.getGameName())) {
					if (ppfe.getRequirementID() == currentReqID) { // Final estimate can be 0
						label.setIcon(tickIcon);
						break;
					}
				}
			}
        }
		
        return label;
    }
    
	/** Update the final estimates
	 * 
	 * @param finalEsts An array of final estimates to update from
	 */
    public void updateFinalEstimation(PlanningPokerFinalEstimate[] finalEsts) {
    	this.finalEsts = finalEsts;
    }
    
    /** Set the game name
     * 
     * @param gameName the new name of the game
     */
    public void setGameName(String gameName) {
    	this.gameName = gameName;
    }
    
    public void goCheck(int index) {
    	if (index > -1) {
	    	checkMap.put(index, true);
	    	
	    	if (originalList != null) {
	    		originalList.repaint();
	    	}
    	}
    }
}

