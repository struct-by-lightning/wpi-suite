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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerFinalEstimateController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * @author Friscis
 *
 */
public class FinalEstimateView extends JPanel {

	/**
	 * The game which this instance of a closed game view is created from.
	 */
	private final PlanningPokerGame game;
	/**
	 * The list of requirements associated with this instance's game.
	 */
	private HashMap<Integer, Requirement> allReqs = new HashMap<Integer, Requirement>();
	
	private javax.swing.JPanel leftSplitPanel;
	private javax.swing.JPanel rightSplitPanel;
	private javax.swing.JSplitPane splitPane;
	private javax.swing.JTable estimates;
	private FinalEstimateTableModel  estimateModel;
	private JButton submitOne;
	private JButton submitAll;
	private  ListSelectionModel listSelectionModel;
	
	public static void open(PlanningPokerGame game) {
		final FinalEstimateView view = new FinalEstimateView(game);
		MainView.getInstance().addCloseableTab(game.getGameName(), view);
	}
	
	private FinalEstimateView(PlanningPokerGame game) {
		this.game = game;
		List<Requirement> requirements = this.game.getRequirements();
		for(Requirement r : requirements) {
			allReqs.put(r.getId(), r);
		}
		initComponents();
		// Initially select the first item in the tree.
	}
	
	private void initComponents() {
		splitPane = new javax.swing.JSplitPane();
		leftSplitPanel = new javax.swing.JPanel();
		rightSplitPanel = new javax.swing.JPanel();
		estimates = new JTable();
		estimateModel = new FinalEstimateTableModel();
		submitOne = new JButton("Submit this Estimate");
		submitAll = new JButton("Submit all Estimates");
		
		splitPane.setLeftComponent(leftSplitPanel);
		splitPane.setRightComponent(rightSplitPanel);
		
		final PlanningPokerFinalEstimate[] finalEsts = GetPlanningPokerFinalEstimateController
				.getInstance().retrievePlanningPokerFinalEstimate();
		for(PlanningPokerFinalEstimate ppfe : finalEsts) {
			if(ppfe.getGameName().equals(game.getGameName())) {
				estimateModel.addRow(Arrays.asList(allReqs.get(ppfe.getRequirementID()),
						ppfe.getEstimate()));
			}
		}
		
		estimates.setModel(estimateModel);
		rightSplitPanel.add(estimates);
		leftSplitPanel.add(submitOne);
		leftSplitPanel.add(submitAll);
		this.add(splitPane);
		
		submitOne.setEnabled(false);
		
		listSelectionModel = estimates.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent ev) {
        		submitOne.setEnabled(estimates.getSelectedRow() != -1);
        	}
        });
        
        submitOne.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Requirement req2set = (Requirement)estimates.getValueAt(estimates.getSelectedRow(), 0);
				req2set.setEstimate((Integer)estimates.getValueAt(estimates.getSelectedRow(), 1));
				UpdateRequirementController.getInstance().updateRequirement(req2set);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				GetRequirementsController.getInstance().retrieveRequirements();
        	}
        });
        
        submitAll.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		for(int i = 0; i< estimates.getRowCount(); i++) {
	        		Requirement req2set = (Requirement)estimates.getValueAt(i, 0);
					req2set.setEstimate((Integer)estimates.getValueAt(i, 1));
					UpdateRequirementController.getInstance().updateRequirement(req2set);
        		}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				GetRequirementsController.getInstance().retrieveRequirements();
        	}
        });
	}
	
}
