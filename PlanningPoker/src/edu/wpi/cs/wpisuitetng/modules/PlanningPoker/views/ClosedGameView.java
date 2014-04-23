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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerFinalEstimateController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerVoteController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerFinalEstimateController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerVoteController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerFinalEstimate;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.UpdateRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * @author ?
 */
public class ClosedGameView extends JPanel {

	/**
	 * Open up a new closeable tab in the planning poker module with a new
	 * instanse of this GUI for viewing and interacting with a closed planning
	 * poker game.
	 *
	 * @param game
	 *            The closed planning poker game to open up a new tab for.
	 */
	public static void open(PlanningPokerGame game) {
		ClosedGameView view = new ClosedGameView(game);
		MainView.getInstance().addCloseableTab(game.getGameName(), view);
	}

	/**
	 * The game which this instance of a closed game view is created from.
	 */
	private PlanningPokerGame game;

	/**
	 * The list of requirements associated with this instance's game.
	 */
	private ArrayList<Requirement> requirements;

	/**
	 * Constructor initializes GUI components, then updates them based on the
	 * given planning poker game.
	 *
	 * @param game
	 *            PlanningPokerGame
	 */
	private ClosedGameView(PlanningPokerGame game) {
		this.game = game;
		this.requirements = this.game.getRequirements();

		initComponents();
		initLogic();
		initForGame();

		// Initially select the first item in the tree.
		this.requirementList.setSelectedIndex(0);
	}
	/**
	 * 
	 * @return A Planning Poker Game of this View
	 */
	public PlanningPokerGame getGame() {
		return game;
	}

	/**
	 * Update components based on the given game.
	 */
	private void initForGame() {
		// Listener which updates the requirement displayed based on what is
		// selected in the tree.
		System.out.println("**Initforgame was called");
		
		this.requirements = game.getRequirements();
		this.requirementList
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent ev) {
						System.out.println("***The listener has been activated***");
						JList list;
						list = (JList) ev.getSource();
						if(list.getSelectedIndex() != -1) {
							selected = requirements.get(list
									.getSelectedIndex());
							currentID = selected.getId();
						}
						if(currentID != previousID) {
							requirementNameLabel.setText(selected.getName());
							requirementDescriptionLabel.setText(selected
									.getDescription());
							updateEstimateTotal(currentID);
							ArrayList<Double> reqVotes = new ArrayList<Double>();
							estimateModel = new DefaultListModel<String>();
							for(PlanningPokerVote v : gameVotes) {
								if(v.getRequirementID() == currentID) {
									reqVotes.add((double)v.getVote());
									estimateModel.addElement("   "+v.getUserName()+": "+v.getVote());
								}
							}
							System.out.println("est mode: "+estimateModel);
							System.out.println("g Votes: "+gameVotes);
							estimates.setModel(estimateModel);
							if(reqVotes.size()!= 0) {
								double[] voteNums = new double[reqVotes.size()];
								for(int i = 0; i< reqVotes.size(); i++) {
									voteNums[i] = (double)reqVotes.get(i);
								}
								mean.setText(meanDef+df.format(Statistics.mean(voteNums)));
								median.setText(medianDef+df.format(Statistics.median(voteNums)));
								mode.setText(modeDef+df.format(Statistics.mode(voteNums)));
								if(reqVotes.size()>1) {
									std.setText(stdDef+df.format(Statistics.StdDev(voteNums)));
								}
								else {
									std.setText(stdDef+"?");
								}
								max.setText(maxDef+df.format(Statistics.max(voteNums)));
								min.setText(minDef+df.format(Statistics.min(voteNums)));
							}
							else {
								mean.setText(meanDef+"?");
								median.setText(medianDef+"?");
								mode.setText(modeDef+"?");
								std.setText(stdDef+"?");
								max.setText(maxDef+"?");
								min.setText(minDef+"?");
							}
							previousID = currentID;
						}
						
						if(!ConfigManager.getConfig().getUserName().equals(game.getModerator())){
							submitButton.setEnabled(false);
							updateButton.setEnabled(false);
							submitButton.setText("Submit");
						}
						else{
							submitButton.setEnabled(true);
						}
					}
					
				});

		// Populate the list with each requirement.
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (Requirement r : this.requirements) {
			model.addElement(r.getName());
		}

		this.requirementList.setModel(model);

		// Show the name of the game.
		this.gameNameLabel.setText(game.getGameName());

		// Show the deadline of the game if there is one.
		this.gameDeadlineDateLabel.setText("Game is Finished");
		if (!ConfigManager.getConfig().getUserName().equals(game.getModerator())) {
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
			estimateTitleLabel.setEnabled(false);
			updateButton.setEnabled(false);
		}
		if(game.isArchived()) {
			updateButton.setEnabled(false);
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
		}

	}

	/**
	 * Add necesary listeners to GUI components.
	 */
	private void initLogic() {

		// Listener which updates the UI each time a requirement is selected
		// from the list of this game's requirements.
		System.out.println("**InitLogic has been called");
//		this.requirementList.addListSelectionListener(new ListSelectionListener() {
//			int currentID = 0;
//			@Override
//			public void valueChanged(ListSelectionEvent ev) {
//				System.out.println("*****Requirement listener called*****");
//				JList list;
//				list = (JList) ev.getSource();
//				if (list.getSelectedIndex() != -1) {
//					selected = requirements.get(list.getSelectedIndex());
//					currentID = selected.getId();
//					requirementNameLabel.setText(selected.getName());
//					requirementDescriptionLabel.setText(selected.getDescription());
//					updateEstimateTotal(currentID);
//				}
//				ArrayList<Double> reqVotes = new ArrayList<Double>();
//				estimateModel = new DefaultListModel<String>();
//				for (PlanningPokerVote v : gameVotes) {
//					if (v.getRequirementID() == currentID) {
//						reqVotes.add((double) v.getVote());
//						estimateModel.addElement("   " + v.getUserName() + ": " + v.getVote());
//					}
//				}
////				System.out.println(estimateModel);
////				System.out.println(gameVotes);
//				estimates.setModel(estimateModel);
//				if (reqVotes.size() != 0) {
//					double[] voteNums = new double[reqVotes.size()];
//					for (int i = 0; i < reqVotes.size(); i++) {
//						voteNums[i] = (double) reqVotes.get(i);
//					}
//					mean.setText(meanDef + df.format(Statistics.mean(voteNums)));
//					median.setText(medianDef + df.format(Statistics.median(voteNums)));
//					mode.setText(modeDef + df.format(Statistics.mode(voteNums)));
//					if (reqVotes.size() > 1) {
//						std.setText(stdDef + df.format(Statistics.StdDev(voteNums)));
//					} else {
//						std.setText(stdDef + "?");
//					}
//					max.setText(maxDef + df.format(Statistics.max(voteNums)));
//					min.setText(minDef + df.format(Statistics.min(voteNums)));
//				} else {
//					mean.setText(meanDef + "?");
//					median.setText(medianDef + "?");
//					mode.setText(modeDef + "?");
//					std.setText(stdDef + "?");
//					max.setText(maxDef + "?");
//					min.setText(minDef + "?");
//				}
//			}
//		});
	}

	/**
	 * Display the correct estimate in the box based on the given requirement.
	 *
	 * @param selected
	 *            The requriement currently being viewed by the user.
	 */
	private void updateEstimateTotal(int selected) {
		PlanningPokerFinalEstimate[] finalEsts = GetPlanningPokerFinalEstimateController.getInstance().retrievePlanningPokerFinalEstimate();
		this.estimateNumberBox.setText("0");
		for(PlanningPokerFinalEstimate ppfe : finalEsts) {
			if(ppfe.getRequirementID() ==  selected && ppfe.getGameName().equals(game.getGameName())) {
				this.estimateNumberBox.setText("" + ppfe.getEstimate());
			}
		}
		enableUpdateButton(finalEsts);
	}
	
	private void enableUpdateButton(PlanningPokerFinalEstimate[] finalEsts) {
		if(game.isArchived()) {
			updateButton.setEnabled(false);
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
		}
		else {
			List<Integer> gameReqIds = new ArrayList<Integer>();
			boolean gameHasEstimates = true;
			for(PlanningPokerFinalEstimate ppfe : finalEsts) {
				if(ppfe.getGameName().equals(game.getGameName())) {
					gameReqIds.add(ppfe.getRequirementID());
					if(!ppfe.hasEstimate()) {
						gameHasEstimates = false;
					}
				}
			}
			System.out.println("The game req ids: " +gameReqIds);
			for(Requirement r : this.requirements){
				System.out.println("the req id from this game: "+r.getId() +"and has a fianl estimate "+ gameReqIds.contains((Integer)r.getId()));
				if(gameReqIds.contains((Integer)r.getId())){
					updateButton.setEnabled(gameHasEstimates);
				}
				else {
					updateButton.setEnabled(false);
					break;
				}
			}
		}
	}
	

	/**
	 * THIS METHOD WAS AUTOMATICALLY GENERATED BY NETBEANS.
	 */
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
		requirementDescriptionLabel = new javax.swing.JTextArea();
		rightBlankPanel = new javax.swing.JPanel();
		instructionsLabel = new javax.swing.JLabel();
		estimateCenteringPanel = new javax.swing.JPanel();
		estimatePanel = new javax.swing.JPanel();
		estimateTitlePanel = new javax.swing.JPanel();
		estimateTitleLabel = new javax.swing.JLabel();
		estimateNumberPanel = new javax.swing.JPanel();
		estimateSubmitPanel = new javax.swing.JPanel();
		estimateNumberBox = new javax.swing.JTextField();
		mean = new javax.swing.JLabel(meanDef);
		median = new javax.swing.JLabel(medianDef);
		mode = new javax.swing.JLabel(modeDef);
		std = new javax.swing.JLabel(stdDef);
		max = new javax.swing.JLabel(maxDef);
		min = new javax.swing.JLabel(minDef);
		stats = new javax.swing.JPanel(new GridLayout(3, 2));

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

		rowSplitPanel.setLayout(new java.awt.GridLayout(2, 2, 15, 15));

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

		requirementDescriptionLabelPanel.setBackground(Color.white);
		requirementDescriptionLabel.setLineWrap(true);
		requirementDescriptionLabel.setEditable(false);
		requirementDescriptionLabel.setWrapStyleWord(true);

		requirementDescriptionLabelPanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		requirementDescriptionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementDescriptionLabel
				.setText("game.getRequirements.get(LIST SELECTION NUM).description()");

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
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		topRowRequirementPanel.add(requirementPanel);

		instructionsLabel.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
		instructionsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		instructionsLabel
				.setText("<html>Below is a list of statistics for this requirement</html>");

		estimateCenteringPanel.setLayout(new java.awt.GridBagLayout());

		estimateTitlePanel.setBorder(new javax.swing.border.SoftBevelBorder(
				javax.swing.border.BevelBorder.RAISED));

		estimateTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		estimateTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateTitleLabel.setText("<html>Final estimate</html>");

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
		estimateNumberBox.setText("0");
		estimateNumberBox.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
					getToolkit().beep();
					e.consume();
					System.out.println("Please enter a number");
				} else if (estimateNumberBox.getText().length() >= 3) {
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
		mean.setFont(new java.awt.Font("Tahoma", 0, 28));
		median.setFont(new java.awt.Font("Tahoma", 0, 28));
		mode.setFont(new java.awt.Font("Tahoma", 0, 28));
		std.setFont(new java.awt.Font("Tahoma", 0, 28));
		max.setFont(new java.awt.Font("Tahoma", 0, 28));
		min.setFont(new java.awt.Font("Tahoma", 0, 28));
		stats.add(mean);
		stats.add(std);
		stats.add(median);
		stats.add(max);
		stats.add(mode);
		stats.add(min);
		// stats.setPreferredSize(new Dimension(stats.getWidth(), 50));
		stats.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		javax.swing.GroupLayout rightBlankPanelLayout = new javax.swing.GroupLayout(rightBlankPanel);
		rightBlankPanel.setLayout(rightBlankPanelLayout);
		rightBlankPanelLayout.setHorizontalGroup(rightBlankPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				rightBlankPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								rightBlankPanelLayout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING).addGroup(
										javax.swing.GroupLayout.Alignment.TRAILING,
										rightBlankPanelLayout
												.createSequentialGroup()
												.addContainerGap()
												.addComponent(stats,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)))));

		rightBlankPanelLayout.setVerticalGroup(rightBlankPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				rightBlankPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(stats, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

		allVotes = GetPlanningPokerVoteController.getInstance().retrievePlanningPokerVote();

		gameVotes = new ArrayList<PlanningPokerVote>();
		for (PlanningPokerVote v : allVotes) {
			if (v.getGameName().equalsIgnoreCase(game.getGameName())) {
				gameVotes.add(v);
			}
		}

		System.out.println("Game votes: " + gameVotes);
		estimates = new javax.swing.JList();
		estimateModel = new DefaultListModel<String>();

		estimates.setModel(estimateModel);

		estimates.setFont(new java.awt.Font("Tahoma", 0, 20));
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(estimates);
		// scroll.setPreferredSize(new Dimension(500, 400));

		// allEstimates = new javax.swing.JPanel();
		// allEstimates.add(scroll);
		scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153,
				153)));

		rowSplitPanel.add(topRowRequirementPanel);
		rowSplitPanel.add(stats);
		rowSplitPanel.add(estimateCenteringPanel);
		rowSplitPanel.add(scroll);

		submitButton = new JButton("Submit");
		updateButton = new JButton("Update All Estimates");
		updateButton.setFont(new java.awt.Font("Tahoma", 0, 28));
		if(game.isArchived()) {
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
		}
		updateButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				PlanningPokerFinalEstimate[] stuff = GetPlanningPokerFinalEstimateController.getInstance().retrievePlanningPokerFinalEstimate();
				System.out.println("These are the current final estimates:" +Arrays.asList(stuff));
				for(PlanningPokerFinalEstimate ppfe : stuff) {
					if(ppfe.getGameName().equals(game.getGameName())) {
						Requirement req2set = RequirementModel.getInstance().getRequirement(ppfe.getRequirementID());
						req2set.setEstimate(ppfe.getEstimate());
						UpdateRequirementController.getInstance().updateRequirement(req2set);
					}
				}
				game.setArchived(true);
				submitButton.setEnabled(false);
				updateButton.setEnabled(false);
				estimateNumberBox.setEnabled(false);
				UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(game);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				GetRequirementsController.getInstance().retrieveRequirements();
			}
		});
		updateButton.setEnabled(false);
		
		javax.swing.GroupLayout estimateSubmitPanelLayout = new javax.swing.GroupLayout(
				estimateSubmitPanel);
		estimateSubmitPanel.setLayout(estimateSubmitPanelLayout);
		estimateSubmitPanelLayout.setHorizontalGroup(estimateSubmitPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				estimateSubmitPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		estimateSubmitPanelLayout.setVerticalGroup(estimateSubmitPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				estimateSubmitPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE,
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
												Short.MAX_VALUE)
										.addComponent(estimateSubmitPanel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(updateButton,
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
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(estimateSubmitPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)));

		estimateCenteringPanel.add(estimatePanel, new java.awt.GridBagConstraints());

		submitButton.setFont(new java.awt.Font("Tahoma", 0, 28));
		submitButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				List<Requirement> req = RequirementModel.getInstance().getRequirements();
				int n = 0;
				for (int i = 0; i < req.size(); i++) {
					if (req.get(i).getName().equals(selected.getName())) {
						n = i;
						break;
					}
				}
				System.out.println(RequirementModel.getInstance().getRequirements().get(n)
						.getName()
						+ " set estimate to " + estimateNumberBox.getText());

				Requirement req2set = RequirementModel.getInstance().getRequirement(n);
				PlanningPokerFinalEstimate ppfe = new PlanningPokerFinalEstimate(game.getGameName(), n);
				ppfe.setEstimate(Integer.parseInt(estimateNumberBox.getText()));
				AddPlanningPokerFinalEstimateController.getInstance().addPlanningPokerFinalEstimate(ppfe);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				PlanningPokerFinalEstimate[] stuff = GetPlanningPokerFinalEstimateController.getInstance().retrievePlanningPokerFinalEstimate();
				System.out.println("These are the current final estimates:" +Arrays.asList(stuff));
				enableUpdateButton(stuff);
				
				submitButton.setEnabled(false);
				submitButton.setText("Submitted");
			}
		});
		if (!ConfigManager.getConfig().getUserName().equals(game.getModerator())) {
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
			estimateTitleLabel.setEnabled(false);
			updateButton.setEnabled(false);
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
		// rowSplitPanel.add(estimateCenteringPanel);
		splitPane.setRightComponent(rightSplitPanel);

		add(splitPane, java.awt.BorderLayout.CENTER);
	}

	/**
	 * THESE DECLARATIONS WERE AUTOMATICALLY GENERATED BY NETBEANS.
	 */
	private javax.swing.JPanel estimateCenteringPanel;
	private javax.swing.JTextField estimateNumberBox;
	private javax.swing.JPanel estimateNumberPanel;
	private javax.swing.JPanel estimateSubmitPanel;
	private javax.swing.JPanel estimatePanel;
	private javax.swing.JLabel estimateTitleLabel;
	private javax.swing.JPanel estimateTitlePanel;
	private javax.swing.JLabel gameDeadlineDateLabel;
	private javax.swing.JLabel gameNameLabel;
	private javax.swing.JPanel gameTitlePanel;
	private javax.swing.JLabel instructionsLabel;
	private javax.swing.JPanel leftSplitPanel;
	private javax.swing.JTextArea requirementDescriptionLabel;
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
	private javax.swing.JLabel mean;
	private javax.swing.JLabel median;
	private javax.swing.JLabel mode;
	private javax.swing.JLabel std;
	private javax.swing.JLabel max;
	private javax.swing.JLabel min;
	private javax.swing.JPanel stats;
	private javax.swing.JList estimates;
	private javax.swing.JPanel allEstimates;
	private DefaultListModel<String> estimateModel;
	private PlanningPokerVote[] allVotes;
	private String meanDef = "    Mean: ";
	private String medianDef = "  Median: ";
	private String modeDef = "    Mode: ";
	private String stdDef = "Std Dev: ";
	private String maxDef = "    Max: ";
	private String minDef = "    Min: ";
	private List<PlanningPokerVote> gameVotes;
	private int currentID;
	private DecimalFormat df = new DecimalFormat("#.###");
	private int previousID = -1;
	private JButton submitButton;
	private JButton updateButton;
}
