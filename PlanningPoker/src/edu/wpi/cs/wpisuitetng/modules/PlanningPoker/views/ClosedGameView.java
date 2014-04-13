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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.RequirementManager;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * @author Legion
 * 
 */
public class ClosedGameView extends JPanel {
	public static void open(PlanningPokerGame game) {
		ClosedGameView view = new ClosedGameView(game);
		MainView.getController().addCloseableTab(game.getGameName(), view);
	}

	private PlanningPokerGame game;
	private ArrayList<Requirement> requirements;

	private ClosedGameView(PlanningPokerGame game) {
		System.out.println("ClsoedGameView(" + game + ")");
		this.game = game;
		this.requirements = game.getRequirements();
		initComponents();
		initForGame();
	}

	private void initForGame() {
		// Listener which updates the requirement displayed based on what is
		// selected in the tree.
		this.requirementList
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent ev) {
						JList list;
						list = (JList) ev.getSource();
						selected = requirements.get(list
								.getSelectedIndex());
						requirementNameLabel.setText(selected.getName());
						requirementDescriptionLabel.setText(selected
								.getDescription());
						updateEstimateTotal(selected);
					}
				});
		// Populate the list with each requirement.
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (Requirement r : this.requirements) {
			System.out.println("r: " + r);
			model.addElement(r.getName());
		}
		this.requirementList.setModel(model);

		// Initially select the first item in the tree.
		this.requirementList.setSelectedIndex(0);

		// Show the name of the game.
		this.gameNameLabel.setText(game.getGameName());

		// Show the deadline of the game if there is one.
		this.gameDeadlineDateLabel.setText("Game is Finished");
		

	}

	private void updateEstimateTotal(Requirement selected) {
		this.estimateNumberBox.setText("" + selected.getEstimate());
	}
	
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		splitPane = new javax.swing.JSplitPane();
		leftSplitPanel = new javax.swing.JPanel();
		requirementsLabelPanel = new javax.swing.JPanel();
		requirementsLabel = new javax.swing.JLabel();
		requirementListScrollPane = new javax.swing.JScrollPane();
		requirementList = new javax.swing.JList();
		rightSplitPanel = new javax.swing.JPanel();
		gameTitlePanel = new javax.swing.JPanel();
		gameNameLabel = new javax.swing.JLabel();
		gameDeadlineDateLabel = new javax.swing.JLabel();
		rowSplitPanel = new javax.swing.JPanel();
		topRowRequirementPanel = new javax.swing.JPanel();
		requirementPanel = new javax.swing.JPanel();
		requirementNamePanel = new javax.swing.JPanel();
		requirementNameLabel = new javax.swing.JLabel();
		requirementDescriptionLabelPanel = new javax.swing.JPanel();
		requirementDescriptionLabel = new javax.swing.JLabel();
		rightBlankPanel = new javax.swing.JPanel();
		instructionsLabel = new javax.swing.JLabel();
		estimateCenteringPanel = new javax.swing.JPanel();
		estimatePanel = new javax.swing.JPanel();
		estimateTitlePanel = new javax.swing.JPanel();
		estimateTitleLabel = new javax.swing.JLabel();
		estimateNumberPanel = new javax.swing.JPanel();
		estimateNumberBox = new javax.swing.JTextField();
		
		setLayout(new java.awt.BorderLayout());

		requirementsLabelPanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		requirementsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		requirementsLabel.setText("Requirements");

		javax.swing.GroupLayout requirementsLabelPanelLayout = new javax.swing.GroupLayout(
				requirementsLabelPanel);
		requirementsLabelPanel.setLayout(requirementsLabelPanelLayout);
		requirementsLabelPanelLayout.setHorizontalGroup(requirementsLabelPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						requirementsLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(requirementsLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()));
		requirementsLabelPanelLayout.setVerticalGroup(requirementsLabelPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						requirementsLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(requirementsLabel)
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		requirementListScrollPane.setViewportView(requirementList);

		javax.swing.GroupLayout leftSplitPanelLayout = new javax.swing.GroupLayout(leftSplitPanel);
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
										.addComponent(requirementsLabelPanel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(requirementListScrollPane,
												javax.swing.GroupLayout.PREFERRED_SIZE, 0,
												Short.MAX_VALUE)).addContainerGap()));
		leftSplitPanelLayout.setVerticalGroup(leftSplitPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				leftSplitPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(requirementsLabelPanel,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(requirementListScrollPane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
						.addContainerGap()));

		splitPane.setLeftComponent(leftSplitPanel);

		gameTitlePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153,
				153, 153)));

		gameNameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		gameNameLabel.setText("game.getGameName()");

		gameDeadlineDateLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		gameDeadlineDateLabel.setText("game.getDeadlineDate()");

		javax.swing.GroupLayout gameTitlePanelLayout = new javax.swing.GroupLayout(gameTitlePanel);
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

		rowSplitPanel.setLayout(new java.awt.GridLayout(2, 1));

		topRowRequirementPanel.setLayout(new java.awt.GridLayout(1, 2));

		requirementNamePanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		requirementNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementNameLabel.setText("game.getRequirements.get(LIST SELECTION NUM).name()");

		javax.swing.GroupLayout requirementNamePanelLayout = new javax.swing.GroupLayout(
				requirementNamePanel);
		requirementNamePanel.setLayout(requirementNamePanelLayout);
		requirementNamePanelLayout.setHorizontalGroup(requirementNamePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						requirementNamePanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(requirementNameLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()));
		requirementNamePanelLayout.setVerticalGroup(requirementNamePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				requirementNamePanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(requirementNameLabel)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		requirementDescriptionLabelPanel.setBackground(new java.awt.Color(255, 255, 255));
		requirementDescriptionLabelPanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		requirementDescriptionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementDescriptionLabel
				.setText("game.getRequirements.get(LIST SELECTION NUM).description()");
		requirementDescriptionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

		javax.swing.GroupLayout requirementDescriptionLabelPanelLayout = new javax.swing.GroupLayout(
				requirementDescriptionLabelPanel);
		requirementDescriptionLabelPanel.setLayout(requirementDescriptionLabelPanelLayout);
		requirementDescriptionLabelPanelLayout
				.setHorizontalGroup(requirementDescriptionLabelPanelLayout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						requirementDescriptionLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(requirementDescriptionLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()));
		requirementDescriptionLabelPanelLayout
				.setVerticalGroup(requirementDescriptionLabelPanelLayout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						requirementDescriptionLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(requirementDescriptionLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
								.addContainerGap()));

		javax.swing.GroupLayout requirementPanelLayout = new javax.swing.GroupLayout(
				requirementPanel);
		requirementPanel.setLayout(requirementPanelLayout);
		requirementPanelLayout.setHorizontalGroup(requirementPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(requirementNamePanel, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(requirementDescriptionLabelPanel,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE));
		requirementPanelLayout.setVerticalGroup(requirementPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				requirementPanelLayout
						.createSequentialGroup()
						.addComponent(requirementNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(requirementDescriptionLabelPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		topRowRequirementPanel.add(requirementPanel);
		
		instructionsLabel.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
		instructionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		instructionsLabel
				.setText("<html>Below is a list of statistics for this requirement</html>");

		estimateCenteringPanel.setLayout(new java.awt.GridBagLayout());

		estimateTitlePanel.setBorder(new javax.swing.border.SoftBevelBorder(
				javax.swing.border.BevelBorder.RAISED));

		estimateTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		estimateTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateTitleLabel.setText("<html>Your estimate</html>");

		javax.swing.GroupLayout estimateTitlePanelLayout = new javax.swing.GroupLayout(
				estimateTitlePanel);
		estimateTitlePanel.setLayout(estimateTitlePanelLayout);
		estimateTitlePanelLayout.setHorizontalGroup(estimateTitlePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				estimateTitlePanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(estimateTitleLabel).addContainerGap()));
		estimateTitlePanelLayout.setVerticalGroup(estimateTitlePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				estimateTitlePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(estimateTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		estimateNumberPanel.setBorder(new javax.swing.border.SoftBevelBorder(
				javax.swing.border.BevelBorder.RAISED));

		estimateNumberBox.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
		estimateNumberBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateNumberBox.setText("#");
	    estimateNumberBox.addKeyListener(new KeyAdapter() {
	        public void keyTyped(KeyEvent e) {
	          char c = e.getKeyChar();
	          if (!((c >= '0') && (c <= '9') ||
	             (c == KeyEvent.VK_BACK_SPACE) ||
	             (c == KeyEvent.VK_DELETE))) {
	            getToolkit().beep();
	            e.consume();
	            System.out.println("Please enter a number");
	          }
	          else if (estimateNumberBox.getText().length()>=3) {
		          getToolkit().beep();
		          e.consume();
	        	  System.out.println("Character Limited exceeded");
	          }
	        }
	      });
	    
		javax.swing.GroupLayout estimateNumberPanelLayout = new javax.swing.GroupLayout(
				estimateNumberPanel);
		estimateNumberPanel.setLayout(estimateNumberPanelLayout);
		estimateNumberPanelLayout.setHorizontalGroup(estimateNumberPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				estimateNumberPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(estimateNumberBox, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		estimateNumberPanelLayout.setVerticalGroup(estimateNumberPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				estimateNumberPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(estimateNumberBox, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		javax.swing.GroupLayout estimatePanelLayout = new javax.swing.GroupLayout(estimatePanel);
		estimatePanel.setLayout(estimatePanelLayout);
		estimatePanelLayout.setHorizontalGroup(estimatePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				estimatePanelLayout
						.createSequentialGroup()
						.addGap(0, 0, Short.MAX_VALUE)
						.addGroup(
								estimatePanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(estimateTitlePanel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(estimateNumberPanel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))));
		estimatePanelLayout.setVerticalGroup(estimatePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				estimatePanelLayout
						.createSequentialGroup()
						.addComponent(estimateTitlePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(estimateNumberPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)));

		estimateCenteringPanel.add(estimatePanel, new java.awt.GridBagConstraints());

//		javax.swing.GroupLayout rightBlankPanelLayout = new javax.swing.GroupLayout(rightBlankPanel);
//		rightBlankPanel.setLayout(rightBlankPanelLayout);
//		rightBlankPanelLayout
//				.setHorizontalGroup(rightBlankPanelLayout
//						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//						.addGroup(
//								rightBlankPanelLayout
//										.createSequentialGroup()
//										.addContainerGap()
//										.addGroup(
//												rightBlankPanelLayout
//														.createParallelGroup(
//																javax.swing.GroupLayout.Alignment.LEADING)
//														.addGroup(
//																javax.swing.GroupLayout.Alignment.TRAILING,
//																rightBlankPanelLayout
//																		.createSequentialGroup()
//																		.addComponent(
//																				instructionsLabel,
//																				javax.swing.GroupLayout.DEFAULT_SIZE,
//																				598,
//																				Short.MAX_VALUE)
//																		.addContainerGap()))));
//														.addComponent(
//																estimateCenteringPanel,
//																javax.swing.GroupLayout.Alignment.TRAILING,
//																javax.swing.GroupLayout.DEFAULT_SIZE,
//																javax.swing.GroupLayout.DEFAULT_SIZE,
//																Short.MAX_VALUE))));
//		rightBlankPanelLayout.setVerticalGroup(rightBlankPanelLayout.createParallelGroup(
//				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
//				javax.swing.GroupLayout.Alignment.TRAILING,
//				rightBlankPanelLayout
//						.createSequentialGroup()
//						.addComponent(estimateCenteringPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
//								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//						.addComponent(instructionsLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
//								javax.swing.GroupLayout.DEFAULT_SIZE,
//								javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
//
//		topRowRequirementPanel.add(rightBlankPanel);

		rowSplitPanel.add(topRowRequirementPanel);
		rowSplitPanel.add(estimateCenteringPanel);
		JButton submitButton = new JButton("Submit");
		rowSplitPanel.add(submitButton);
		submitButton.setFont(new java.awt.Font("Tahoma", 0, 48));
		submitButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				List<Requirement> req = RequirementModel.getInstance().getRequirements();
				int n = 0;
				for(int i = 0; i < req.size(); i++) {
					if(req.get(i).getName().equals(selected.getName())) {
						n = i;
						break;
					}
				}
				System.out.println(RequirementModel.getInstance().getRequirements().get(n).getName()+" set estimate to "+ estimateNumberBox.getText());
				Requirement req2set = RequirementModel.getInstance().getRequirement(n);
				req2set.setEstimate(Integer.parseInt(estimateNumberBox.getText()));
				UpdateRequirementController.getInstance().updateRequirement(req2set);
				//GetRequirementsController.getInstance().retrieveRequirements();
			}
		});
		if(!ConfigManager.getConfig().getUserName().equals(game.getModerator())) {
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
			estimateTitleLabel.setEnabled(false);
		}
		javax.swing.GroupLayout rightSplitPanelLayout = new javax.swing.GroupLayout(rightSplitPanel);
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
										.addComponent(rowSplitPanel,
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
						.addComponent(rowSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		//rowSplitPanel.add(estimateCenteringPanel);
		splitPane.setRightComponent(rightSplitPanel);

		add(splitPane, java.awt.BorderLayout.CENTER);
	}

	private javax.swing.JPanel estimateCenteringPanel;
	private javax.swing.JTextField estimateNumberBox;
	private javax.swing.JPanel estimateNumberPanel;
	private javax.swing.JPanel estimatePanel;
	private javax.swing.JLabel estimateTitleLabel;
	private javax.swing.JPanel estimateTitlePanel;
	private javax.swing.JLabel gameDeadlineDateLabel;
	private javax.swing.JLabel gameNameLabel;
	private javax.swing.JPanel gameTitlePanel;
	private javax.swing.JLabel instructionsLabel;
	private javax.swing.JPanel leftSplitPanel;
	private javax.swing.JLabel requirementDescriptionLabel;
	private javax.swing.JPanel requirementDescriptionLabelPanel;
	private javax.swing.JList requirementList;
	private javax.swing.JScrollPane requirementListScrollPane;
	private javax.swing.JLabel requirementNameLabel;
	private javax.swing.JPanel requirementNamePanel;
	private javax.swing.JPanel requirementPanel;
	private javax.swing.JLabel requirementsLabel;
	private javax.swing.JPanel requirementsLabelPanel;
	private javax.swing.JPanel rightBlankPanel;
	private javax.swing.JPanel rightSplitPanel;
	private javax.swing.JPanel rowSplitPanel;
	private javax.swing.JSplitPane splitPane;
	private javax.swing.JPanel topRowRequirementPanel;
	private Requirement selected;

}
