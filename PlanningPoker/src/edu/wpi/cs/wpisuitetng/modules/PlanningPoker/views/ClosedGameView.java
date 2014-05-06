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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerFinalEstimateController;
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
 * @version $Revision: 1.0 $
 * @author friscis
 * @author swconley
 * @author mamora
 */
public class ClosedGameView extends JPanel {
	RequirementVoteIconRenderer requirementListRenderer;
	/**
	 * Open up a new closeable tab in the planning poker module with a new
	 * instanse of this GUI for viewing and interacting with a closed planning
	 * poker game.
	 *
	 * @param game
	 *            The closed planning poker game to open up a new tab for.
	 */
	public static void open(PlanningPokerGame game) {
		final ClosedGameView view = new ClosedGameView(game);
		MainView.getInstance().addCloseableTab(game.getGameName(), view);
	}

	/**
	 * The game which this instance of a closed game view is created from.
	 */
	private final PlanningPokerGame game;

	/**
	 * The list of requirements associated with this instance's game.
	 */
	private List<Requirement> requirements;

	/**
	 * Constructor initializes GUI components, then updates them based on the
	 * given planning poker game.
	 *
	 * @param game
	 *            PlanningPokerGame
	 */
	private ClosedGameView(PlanningPokerGame game) {
		this.game = game;
		requirements = this.game.getRequirements();

		initComponents();
		initLogic();
		initForGame();

		// Initially select the first item in the tree.
		requirementList.setSelectedIndex(0);
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
		
		requirements = game.getRequirements();
		requirementList
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent ev) {
						Average = 0; // Quick fix for putting mean value in
						System.out.println("***The listener has been activated***");
						final JList list;
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

							final ArrayList<Double> reqVotes = new ArrayList<Double>();

							estimateModel = new TeamEstimateTableModel();
							
							for(PlanningPokerVote v : gameVotes) {
								if(v.getRequirementID() == currentID) {
									reqVotes.add((double)v.getVote());
									estimateModel.addRow(Arrays.asList(v.getUserName(),
											v.getVote()));
								}
							}
							// Estimates
							estimates.setModel(estimateModel);
							
							// Resize the table row height
						    try
						    {
						        for (int row = 0; row < estimates.getRowCount(); row++)
						        {
						            int rowHeight = estimates.getRowHeight();

						            for (int column = 0; column < estimates
						            		.getColumnCount(); column++)
						            {
						                Component comp = estimates.prepareRenderer(
						                		estimates.getCellRenderer(row, column),
						                		row, column);
						                rowHeight = Math.max(rowHeight,
						                		comp.getPreferredSize().height) + 5;
						            }

						            estimates.setRowHeight(row, rowHeight);
						        }
						    }
						    catch(ClassCastException e) {}

						    
						    // Align text center in table
						    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						    centerRenderer.setHorizontalAlignment( JLabel.CENTER );
						    estimates.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
						    estimates.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
						    
							if(reqVotes.size()!= 0) {
								double[] voteNums = new double[reqVotes.size()];
								for(int i = 0; i< reqVotes.size(); i++) {
									voteNums[i] = (double)reqVotes.get(i);
								}
								mean.setText(meanDef+df.format(Statistics.mean(voteNums)));
								median.setText(medianDef+df.format(Statistics.median(voteNums)));
								
								Average = Math.round(Statistics.mean(voteNums));
								mode.setText(modeDef+df.format(Statistics.mode(voteNums)));
								
								if(reqVotes.size()>1) {
									std.setText(stdDef+df.format(Statistics.StdDev(voteNums)));

								}
								else {
									std.setText(stdDef + "N/A");
								}
								max.setText(maxDef + df.format(Statistics.max(voteNums)));
								min.setText(minDef + df.format(Statistics.min(voteNums)));
							}
							else {
								mean.setText(meanDef + "N/A");
								median.setText(medianDef + "N/A");
								mode.setText(modeDef + "N/A");
								std.setText(stdDef + "N/A");
								max.setText(maxDef + "N/A");
								min.setText(minDef + "N/A");
							}
							
							if (estimateNumberBox.getText().equals("0")) {
								estimateNumberBox.setText(""+Average);
							}
							
							previousID = currentID;
						}
						

						if(!ConfigManager.getConfig().getUserName().equals(game.getModerator())){
							submitButton.setEnabled(false);
							submitButton.setToolTipText(null);
							updateButton.setEnabled(false);
							submitButton.setText("Submit");
						}
						else if(!game.isArchived()){
							submitButton.setEnabled(true);
							submitButton.setText("Submit");
							// Add tooltips
							submitButton.setToolTipText("Update this estimate in the Requirement Manager");
						}
					}
					
				});
		
		// Icons for requirement List
		final PlanningPokerFinalEstimate[] finalEsts = GetPlanningPokerFinalEstimateController
				.getInstance().retrievePlanningPokerFinalEstimate();
		
		final DefaultListModel<String> model = new DefaultListModel<String>();
		for (Requirement r : requirements) {
			if(r!= null) {
				model.addElement(r.getName());
			}
		}
		
		requirementListRenderer = new RequirementVoteIconRenderer(requirements, finalEsts);
		requirementListRenderer.setGameName(game.getGameName());
		
		requirementList.setModel(model);
		requirementList.setCellRenderer(requirementListRenderer);

		// Show the name of the game.
		gameNameLabel.setText(game.getGameName());

		// Show the deadline of the game if there is one.
		gameDeadlineDateLabel.setText("Game is Finished");
		if (!ConfigManager.getConfig().getUserName().equals(game.getModerator())) {
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
			estimateTitleLabel.setEnabled(false);
			updateButton.setEnabled(false);
		}
		if(game.isArchived()) {
			updateButton.setEnabled(true);
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
		}

	}

	/**
	 * Add necesary listeners to GUI components.
	 */
	private void initLogic() {
		System.out.println("**InitLogic has been called");
	}

	/**
	 * Display the correct estimate in the box based on the given requirement.
	 *
	 * @param selected
	 *            The requriement currently being viewed by the user.
	 */
	private void updateEstimateTotal(int selected) {
		System.out.println("Update Estimate Total");
		
		final PlanningPokerFinalEstimate[] finalEsts = GetPlanningPokerFinalEstimateController
				.getInstance().retrievePlanningPokerFinalEstimate();
		estimateNumberBox.setText("0");
		for(PlanningPokerFinalEstimate ppfe : finalEsts) {
			if (ppfe.getRequirementID() == selected
					&& ppfe.getGameName().equals(game.getGameName())) {
				estimateNumberBox.setText("" + ppfe.getEstimate());
			}
		}
		enableUpdateButton(finalEsts);
	}
	
	private void enableUpdateButton(PlanningPokerFinalEstimate[] finalEsts) {
		if(game.isArchived()) {
			updateButton.setEnabled(true);
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
		}
		else {
			final List<Integer> gameReqIds = new ArrayList<Integer>();
			boolean gameHasEstimates = true;
			for(PlanningPokerFinalEstimate ppfe : finalEsts) {
				System.out.println("The final estimate game is: "+ppfe.getGameName() +"while the game name is: "+ game.getGameName());
				if(ppfe.getGameName().equals(game.getGameName())) {
					gameReqIds.add(ppfe.getRequirementID());
					if(!ppfe.hasEstimate()) {
						gameHasEstimates = false;
					}
				}
			}
			System.out.println("The game req ids: " + gameReqIds);
			for(Requirement r : requirements){
				System.out.println("the req id from this game: " + r.getId()
						+ "and has a fianl estimate "
						+ gameReqIds.contains((Integer) r.getId()));
				if(gameReqIds.contains((Integer)r.getId())){
					updateButton.setEnabled(gameHasEstimates);
				}
				else {
					
					break;
				}
			}
		}
	}
	

	/**
	 * THIS METHOD WAS AUTOMATICALLY GENERATED BY NETBEANS.
	 */
	private void initComponents() {
		final java.awt.GridBagConstraints gridBagConstraints;

		splitPane = new javax.swing.JSplitPane();
		splitPane.setDividerLocation(200); // Make the left column wider
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

		requirementsLabelPanel.setBorder(null);

		requirementsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		requirementsLabel.setText("Requirements");

		final javax.swing.GroupLayout requirementsLabelPanelLayout = new javax.swing.GroupLayout(
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

		rowSplitPanel.setLayout(new java.awt.GridLayout(2, 2, 15, 15));

		topRowRequirementPanel.setLayout(new java.awt.GridLayout(1, 2));

		requirementNamePanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		requirementNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementNameLabel.setText("game.getRequirements.get(LIST SELECTION NUM).name()");

		final javax.swing.GroupLayout requirementNamePanelLayout = new javax.swing.GroupLayout(
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

		final javax.swing.GroupLayout requirementDescriptionLabelPanelLayout = new javax.swing.GroupLayout(
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

		final javax.swing.GroupLayout requirementPanelLayout = new javax.swing.GroupLayout(
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

		estimateTitlePanel.setBorder(null);

		estimateTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		estimateTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateTitleLabel.setText("<html>Final estimate</html>");

		final javax.swing.GroupLayout estimateTitlePanelLayout = new javax.swing.GroupLayout(
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

		estimateNumberPanel.setBorder(null);

		estimateNumberBox.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
		estimateNumberBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateNumberBox.setText("0");
		estimateNumberBox.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				final char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
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

		final javax.swing.GroupLayout estimateNumberPanelLayout = new javax.swing.GroupLayout(
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
		mean.setFont(new java.awt.Font("Tahoma", 0, 16));
		median.setFont(new java.awt.Font("Tahoma", 0, 16));
		mode.setFont(new java.awt.Font("Tahoma", 0, 16));
		std.setFont(new java.awt.Font("Tahoma", 0, 16));
		max.setFont(new java.awt.Font("Tahoma", 0, 16));
		min.setFont(new java.awt.Font("Tahoma", 0, 16));
		stats.add(mean);
		stats.add(std);
		stats.add(median);
		stats.add(max);
		stats.add(mode);
		stats.add(min);
		// stats.setPreferredSize(new Dimension(stats.getWidth(), 50));
		stats.setBorder(null);

		final javax.swing.GroupLayout rightBlankPanelLayout = new javax.swing.GroupLayout(
				rightBlankPanel);
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
			System.err.println("Vote gameName: " + v.getGameName());
			System.err.println("Game name: " + game.getGameName());
			if (v.getGameName().equalsIgnoreCase(game.getGameName())) {
				gameVotes.add(v);
			}
		}

		System.out.println("Game votes: " + gameVotes);
		
		estimates = new JTable();
		estimateModel = new TeamEstimateTableModel();

		estimates.setModel(estimateModel);

		estimates.setFont(new java.awt.Font("Tahoma", 0, 16));
		
		final JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(estimates);
		// scroll.setPreferredSize(new Dimension(500, 400));

		// allEstimates = new javax.swing.JPanel();
		// allEstimates.add(scroll);
		scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153,
				153)));
		rowSplitPanel.add(topRowRequirementPanel);
		rowSplitPanel.add(scroll);
		
		rowSplitPanel.add(estimateCenteringPanel);
		rowSplitPanel.add(stats);

		submitButton = new JButton("Submit");
		updateButton = new JButton("Open Final Estimate Updater");
		updateButton.setFont(new java.awt.Font("Tahoma", 0, 28));
		updateButton.setEnabled(true);
		if(game.isArchived()) {
			submitButton.setEnabled(true);
			estimateNumberBox.setEnabled(false);
		}
		updateButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
//				final PlanningPokerFinalEstimate[] stuff = GetPlanningPokerFinalEstimateController
//						.getInstance().retrievePlanningPokerFinalEstimate();
//				System.out.println("These are the current final estimates:" + Arrays.asList(stuff));
//				for(PlanningPokerFinalEstimate ppfe : stuff) {
//					if(ppfe.getGameName().equals(game.getGameName())) {
//						Requirement req2set = RequirementModel.getInstance()
//								.getRequirement(ppfe.getRequirementID());
//						req2set.setEstimate(ppfe.getEstimate());
//						UpdateRequirementController.getInstance().updateRequirement(req2set);
//					}
//				}
//				game.setArchived(true);
//				submitButton.setEnabled(false);
//				updateButton.setEnabled(false);
//				estimateNumberBox.setEnabled(false);
//				UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(game);
//				try {
//					Thread.sleep(300);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				GetRequirementsController.getInstance().retrieveRequirements();
				FinalEstimateView.open(game);
			}
		});
		
		final javax.swing.GroupLayout estimateSubmitPanelLayout = new javax.swing.GroupLayout(
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

		final javax.swing.GroupLayout estimatePanelLayout = new javax.swing.GroupLayout(
				estimatePanel);
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
				final List<Requirement> req = RequirementModel.getInstance().getRequirements();
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

				final Requirement req2set = RequirementModel.getInstance().getRequirement(n);
				final PlanningPokerFinalEstimate ppfe = new PlanningPokerFinalEstimate(
						game.getGameName(), n);
				ppfe.setEstimate(Integer.parseInt(estimateNumberBox.getText()));
				AddPlanningPokerFinalEstimateController.getInstance()
						.addPlanningPokerFinalEstimate(ppfe);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				final PlanningPokerFinalEstimate[] stuff = GetPlanningPokerFinalEstimateController
						.getInstance().retrievePlanningPokerFinalEstimate();
				System.out.println("These are the current final estimates:" + Arrays.asList(stuff));
				enableUpdateButton(stuff);
				
				submitButton.setEnabled(false);
				submitButton.setText("Submitted");
				
				// Repaint the panel when a fianl estimate has been submitted
				requirementListRenderer.updateFinalEstimation(stuff);
				requirementList.repaint();
			}
		});
		if (!ConfigManager.getConfig().getUserName().equals(game.getModerator())) {
			submitButton.setEnabled(false);
			estimateNumberBox.setEnabled(false);
			estimateTitleLabel.setEnabled(false);
			updateButton.setEnabled(false);
		}
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
	private javax.swing.JTable estimates;
	private javax.swing.JPanel allEstimates;
	private TeamEstimateTableModel  estimateModel;
	private PlanningPokerVote[] allVotes;
	private final String meanDef = "    Mean: ";
	private final String medianDef = "  Median: ";
	private final String modeDef = "    Mode: ";
	private final String stdDef = "Std Dev: ";
	private final String maxDef = "    Max: ";
	private final String minDef = "    Min: ";
	private List<PlanningPokerVote> gameVotes;
	private int currentID;
	private final DecimalFormat df = new DecimalFormat("#.###");
	private int previousID = -1;
	private JButton submitButton;
	private JButton updateButton;
	private long Average;
}
