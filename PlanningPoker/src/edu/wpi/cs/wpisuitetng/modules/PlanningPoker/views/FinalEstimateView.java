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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerFinalEstimateController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

import java.awt.Component;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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
	private JScrollPane scroll;
	private javax.swing.JLabel gameDeadlineDateLabel;
	private javax.swing.JLabel gameNameLabel;
	private javax.swing.JPanel gameTitlePanel;
	private JPanel buttons;
	private JLabel estimatesLabelPanel;
	
	public static void open(PlanningPokerGame game) {
		final FinalEstimateView view = new FinalEstimateView(game);
		MainView.getInstance().addCloseableTab("Update Final Estimates for: "+game.getGameName(), view);
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
		gameTitlePanel = new javax.swing.JPanel();
		gameNameLabel = new javax.swing.JLabel();
		gameDeadlineDateLabel = new javax.swing.JLabel();
		
		splitPane = new javax.swing.JSplitPane();
		splitPane.setAlignmentY(Component.CENTER_ALIGNMENT);
		splitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftSplitPanel = new javax.swing.JPanel();
		rightSplitPanel = new javax.swing.JPanel();
		estimates = new JTable();
		estimateModel = new FinalEstimateTableModel();
		submitOne = new JButton("Submit Estimate: ");
		submitAll = new JButton("Submit all Estimates");
	    scroll = new JScrollPane();
	    buttons = new JPanel(new GridLayout(2,1));
	    estimatesLabelPanel = new JLabel();
		
	    
	    gameTitlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153,
				153, 153)));

		gameNameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		gameNameLabel.setText("game.getGameName()");

		gameDeadlineDateLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		gameDeadlineDateLabel.setText("game.getDeadlineDate()");

		final javax.swing.GroupLayout gameTitlePanelLayout = new javax.swing.GroupLayout(
				gameTitlePanel);
		gameTitlePanel.setLayout(gameTitlePanelLayout);
		gameTitlePanelLayout.setHorizontalGroup(gameTitlePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				gameTitlePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(gameNameLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(gameDeadlineDateLabel).addContainerGap()));
		gameTitlePanelLayout.setVerticalGroup(gameTitlePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				gameTitlePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gameTitlePanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(gameNameLabel)
										.addComponent(gameDeadlineDateLabel))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		// Show the name of the game.
		gameNameLabel.setText(game.getGameName());

		// Show the deadline of the game if there is one.
		gameDeadlineDateLabel.setText("Submit Final Estimates");
		
		submitOne.setFont(new java.awt.Font("Tahoma", 0, 16));
		submitAll.setFont(new java.awt.Font("Tahoma", 0, 16));
		estimates.setFont(new java.awt.Font("Tahoma", 0, 16));
		
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
		
		buttons.add(submitOne);
		buttons.add(submitAll);
		estimatesLabelPanel.setText("Final Estimates");
		
		
		final javax.swing.GroupLayout rightSplitPanelLayout = new javax.swing.GroupLayout(
				rightSplitPanel);
		rightSplitPanel.setLayout(rightSplitPanelLayout);
		rightSplitPanelLayout.setHorizontalGroup(rightSplitPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				rightSplitPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								rightSplitPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(gameTitlePanel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(buttons,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)).addContainerGap()));
		rightSplitPanelLayout.setVerticalGroup(rightSplitPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				rightSplitPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(gameTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(buttons, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		
		final javax.swing.GroupLayout leftSplitPanelLayout = new javax.swing.GroupLayout(
				leftSplitPanel);
		leftSplitPanel.setLayout(leftSplitPanelLayout);
		leftSplitPanelLayout.setHorizontalGroup(leftSplitPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				leftSplitPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								leftSplitPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(estimatesLabelPanel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(scroll,
												javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE,
												Short.MAX_VALUE)).addContainerGap()));
		leftSplitPanelLayout.setVerticalGroup(leftSplitPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				leftSplitPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(estimatesLabelPanel,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(scroll,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		
		
		estimates.setModel(estimateModel);
		//rightSplitPanel.add(scroll);
		leftSplitPanel.add(gameTitlePanel);
		
		submitOne.setEnabled(false);
		
		listSelectionModel = estimates.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent ev) {
        		submitOne.setEnabled(estimates.getSelectedRow() != -1);
        		submitOne.setText("Submit Estimate: "+estimates.getValueAt(estimates.getSelectedRow(), 0));
        	}
        });
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment( JLabel.CENTER );
	    estimates.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	    estimates.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
		scroll.setViewportView(estimates);
	    
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
				submitOne.setText("Submitted the Final Estimate for "+req2set.getName());
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
				gameDeadlineDateLabel.setText("Submitted all Final Estimates for this game");
		        // Close the tab.
		        MainView.getInstance().removeClosableTab();
        	}
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addComponent(splitPane).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addComponent(splitPane).addContainerGap()));
	}
	
}
