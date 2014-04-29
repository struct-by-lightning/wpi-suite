/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Struct-By-Lightning
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.DatePicker;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * An instance of this class is a JPanel containing the GUI for interacting with
 * a planning poker game which is not yet open for voting.
 *
 * @author Austin Rose (atrose)
 */
public class NewGameView extends javax.swing.JPanel {

	public PlanningPokerGame getGame() {
		return game;
	}

	// Models for the information contained by a few components.
    private final DefaultListModel<String> thisGameRequirementsListModel;
    private final DefaultListModel<String> backlogRequirementsListModel;
    private final DefaultComboBoxModel<String> deckTypeComboBoxModel;

    // Components for the date-picker.
    private JPanel calendarPanel;
    private DatePicker datePicker;

    // The game being viewed/edited.
	private PlanningPokerGame game;

	/**
	 * This method will open up a new tab in the planning poker module with this
	 * UI for viewing a the planning poker game.
	 */
	public static void open(PlanningPokerGame game) {
		final NewGameView view = new NewGameView(game);
		MainView.getInstance().addCloseableTab(game.getGameName(), view);
	}

    public NewGameView(PlanningPokerGame game) {
    	this.game = game;

        // Populate list of requirements which are going to estimated.
        this.thisGameRequirementsListModel = new DefaultListModel<>();
        ArrayList<Requirement> requirements = game.getRequirements();
        for (Requirement req : requirements) {
            this.thisGameRequirementsListModel.addElement(req.getName());
        }

        // Populate list of remaining requirements from the backlog.
        this.backlogRequirementsListModel = new DefaultListModel<>();
        for (Requirement req : RequirementModel.getInstance().getRequirements()) {
        	if (!this.thisGameRequirementsListModel.contains(req.toString())) {
        		this.backlogRequirementsListModel.addElement(req.toString());
        	}
        }

        // Populate list of deck types.
        this.deckTypeComboBoxModel = new DefaultComboBoxModel<String>(new String[] { "Default", "No Deck" });

        initComponents();

        doubleLeftArrowButton.setFocusable(false);
        doubleRightArrowButton.setFocusable(false);
        singleLeftArrowButton.setFocusable(false);
        singleRightArrowButton.setFocusable(false);

        // Set the deck choice correctly for the game.
        this.deckChoiceComboBox.setSelectedItem(game.getDeckType());
        this.deckChoiceClicked(game.getDeckType());

        this.gameNameLabel.setText(game.getGameName());

        this.deadlineCheckbox.setFocusable(false);

		SpinnerDateModel timePickerModel = new SpinnerDateModel();
		timePickerModel.setCalendarField(Calendar.MINUTE);
		DateFormat dateFormat = new SimpleDateFormat("h:mm a");
		timePicker.setFont(dateField.getFont());
		timePicker.setModel(timePickerModel);
		timePicker.setEditor(new JSpinner.DateEditor(timePicker, "h:mm a"));

		calendarPanel = new JPanel(new GridBagLayout());
		calendarPanel.setPreferredSize(new Dimension(350, 220));
		calendarHandlerPanel.add(calendarPanel);

		openCalendarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (datePicker == null) {
					datePicker = new DatePicker(calendarPanel, dateField);
					openCalendarButton.setText("Close date picker");
				} else {
					datePicker.close();
					datePicker = null;
					openCalendarButton.setText("Open date picker");
				}
			}
		});

		dateField.setEditable(false);

		// Unless there's already a date
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (game.hasEndDate()) {
			timePicker.setValue(game.getEndDate().getTime());
			dateField.setText(sdf.format(game.getEndDate().getTime()));
		} else {
			dateField.setText("Use date picker to choose");
		}


		loginPane.setText("Logged in as: " + ConfigManager.getConfig().getUserName());

		this.updateArrowButtons();

		defaultSettingsButton.setEnabled(false);

        // Set the deadline checkbox to be toggled correctly for the game.
        this.deadlineCheckbox.setSelected(game.hasEndDate());
//		if (game.hasEndDate()) {
//			datePicker = new DatePicker(calendarPanel, dateField);
//			openCalendarButton.setText("Close date picker");
//		}
    }

    private void updateControlButtons() {
    	boolean gameHasReqs = !this.thisGameRequirementsListModel.isEmpty();

    	saveChangesButton.setEnabled(gameHasReqs && deadlineIsValid());
    	openGameForVotingButton.setEnabled(gameHasReqs && deadlineIsValid());
    }

    private boolean deadlineIsValid() {
    	if (!deadlineCheckbox.isSelected()) {
    		return true;
    	}

    	// TODO:
    	return true;
    }

    private void updateArrowButtons() {
    	boolean backlogEmpty = this.backlogRequirementsListModel.isEmpty();
    	boolean gameEmtpy = this.thisGameRequirementsListModel.isEmpty();

    	this.doubleLeftArrowButton.setEnabled(!gameEmtpy);
    	this.doubleRightArrowButton.setEnabled(!backlogEmpty);
    	this.singleLeftArrowButton.setEnabled(!gameEmtpy);
    	this.singleRightArrowButton.setEnabled(!backlogEmpty);

    	updateControlButtons();
    }

    private void defaultSettingsButtonClicked() {

    }

    private void openGameForVotingButtonClicked() {
        game.setLive(true);
        game.setFinished(false);
        this.saveChangesButtonClicked();
    }

    private void saveChangesButtonClicked() {

		GetRequirementsController.getInstance().retrieveRequirements();
		while (RequirementModel.getInstance().getRequirements().size() < 1
				|| RequirementModel.getInstance().getRequirements().get(0) == null) {
		}

		ArrayList<Requirement> allReqs = (ArrayList<Requirement>) RequirementModel.getInstance().getRequirements();

		ArrayList<Integer> newReqIDs = new ArrayList<>();

		for (int i = 0; i < thisGameRequirementsListModel.size(); i++) {
			String reqName = thisGameRequirementsListModel.get(i);
			for (Requirement req : allReqs) {
				if (req.getName().equals(reqName)) {
					newReqIDs.add(new Integer(req.getId()));
					break;
				}
			}
		}

		game.setRequirementIds(newReqIDs);

		game.setDeckType((String)deckChoiceComboBox.getSelectedItem());

		GregorianCalendar startCal, endCal;
		startCal = new GregorianCalendar();
		game.setStartDate(startCal);

		String[] endDate = dateField.getText().split("-");
		Date endVal = (Date) timePicker.getValue();

		if (deadlineCheckbox.isSelected()) {
			endCal = new GregorianCalendar(Integer.parseInt(endDate[2]), Integer.parseInt(endDate[1]) - 1, Integer.parseInt(endDate[0]), endVal.getHours(), endVal.getMinutes());

			if (startCal.before(endCal)) {
				game.setEndDate(endCal);
			} else {
				loginPane.setText(loginPane.getText() + " -- INVALID DEADLINE");
				return;
			}
		} else {
			game.clearEndDate();
		}

		UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(game);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Close this tab.
		MainView.getInstance().removeClosableTab();
    }

    private void deckChoiceClicked(String deckName) {
        this.deckValuesLabel.setText(deckName.equals("Default") ? "[0, 1, 1, 2, 3, 5, 8]" : "N/A");
    }

    private void doubleRightArrowButtonClicked() {
        while (!this.backlogRequirementsListModel.isEmpty()) {
            String req = this.backlogRequirementsListModel.remove(0);
            this.thisGameRequirementsListModel.addElement(req);
        }
        updateArrowButtons();
    }

    private void doubleLeftArrowButtonClicked() {
        while (!this.thisGameRequirementsListModel.isEmpty()) {
            String req = this.thisGameRequirementsListModel.remove(0);
            this.backlogRequirementsListModel.addElement(req);
        }
        updateArrowButtons();
    }

    private void singleRightArrowButtonClicked() {

        ArrayList<String> reqsToMove = new ArrayList<>();

        int[] selected = this.backlogRequirementsList.getSelectedIndices();

        for (int i : selected) {
            reqsToMove.add(this.backlogRequirementsListModel.get(i));
        }

        for (String req : reqsToMove) {
            this.backlogRequirementsListModel.removeElement(req);
            this.thisGameRequirementsListModel.addElement(req);
        }

        updateArrowButtons();
    }

    private void singleLeftArrowButtonClicked() {
        ArrayList<String> reqsToMove = new ArrayList<>();

        int[] selected = this.thisGameRequirementsList.getSelectedIndices();

        for (int i : selected) {
            reqsToMove.add(this.thisGameRequirementsListModel.get(i));
        }

        for (String req : reqsToMove) {
            this.thisGameRequirementsListModel.removeElement(req);
            this.backlogRequirementsListModel.addElement(req);
        }

        updateArrowButtons();
    }

    private void deadlineCheckboxToggled() {
        boolean checked = this.deadlineCheckbox.isSelected();

        if (!checked) {
        	openCalendarButton.doClick();
        }

        this.dateLabel.setEnabled(checked);
        this.dateField.setEnabled(checked);
        this.openCalendarButton.setEnabled(checked);
        this.timeLabel.setEnabled(checked);
        this.timePicker.setEnabled(checked);

        updateControlButtons();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        gameNameFieldPanel = new javax.swing.JPanel();
        gameNameLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        defaultSettingsButton = new javax.swing.JButton();
        openGameForVotingButton = new javax.swing.JButton();
        saveChangesButton = new javax.swing.JButton();
        requirementListsPanel = new javax.swing.JPanel();
        backlogReqsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        backlogRequirementsList = new javax.swing.JList();
        arrowButtonsPanel = new javax.swing.JPanel();
        doubleRightArrowButton = new javax.swing.JButton();
        singleRightArrowButton = new javax.swing.JButton();
        singleLeftArrowButton = new javax.swing.JButton();
        doubleLeftArrowButton = new javax.swing.JButton();
        thisGameReqsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        thisGameRequirementsList = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        deadlineCheckbox = new javax.swing.JCheckBox();
        dateLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        openCalendarButton = new javax.swing.JButton();
        dateField = new javax.swing.JTextField();
        timePicker = new javax.swing.JSpinner();
        calendarHandlerPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        deckChoiceComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        deckValuesLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        loginPane = new javax.swing.JTextPane();

        gameNameLabel.setFont(new java.awt.Font("Lucida Grande", 2, 16));
        gameNameFieldPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N
        javax.swing.GroupLayout gameNameFieldPanelLayout = new javax.swing.GroupLayout(gameNameFieldPanel);
        gameNameFieldPanel.setLayout(gameNameFieldPanelLayout);
        gameNameFieldPanelLayout.setHorizontalGroup(
            gameNameFieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameNameFieldPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gameNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gameNameFieldPanelLayout.setVerticalGroup(
            gameNameFieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameNameFieldPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gameNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        defaultSettingsButton.setText("Default settings");
        defaultSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultSettingsButtonActionPerformed(evt);
            }
        });

        openGameForVotingButton.setText("Open game for voting");
        openGameForVotingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openGameForVotingButtonActionPerformed(evt);
            }
        });

        saveChangesButton.setText("Save changes");
        saveChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(openGameForVotingButton)
                .addGap(18, 18, 18)
                .addComponent(saveChangesButton)
                .addGap(18, 18, 18)
                .addComponent(defaultSettingsButton)
                .addContainerGap())
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveChangesButton)
                    .addComponent(defaultSettingsButton)
                    .addComponent(openGameForVotingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        buttonsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {defaultSettingsButton, openGameForVotingButton, saveChangesButton});

        requirementListsPanel.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Backlog requirements");

        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 200));

        backlogRequirementsList.setModel(this.backlogRequirementsListModel);
        jScrollPane1.setViewportView(backlogRequirementsList);

        javax.swing.GroupLayout backlogReqsPanelLayout = new javax.swing.GroupLayout(backlogReqsPanel);
        backlogReqsPanel.setLayout(backlogReqsPanelLayout);
        backlogReqsPanelLayout.setHorizontalGroup(
            backlogReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backlogReqsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backlogReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        backlogReqsPanelLayout.setVerticalGroup(
            backlogReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backlogReqsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        requirementListsPanel.add(backlogReqsPanel, gridBagConstraints);

        arrowButtonsPanel.setLayout(new java.awt.GridBagLayout());

        doubleRightArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        doubleRightArrowButton.setText(">>");
        doubleRightArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleRightArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        arrowButtonsPanel.add(doubleRightArrowButton, gridBagConstraints);

        singleRightArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        singleRightArrowButton.setText(" >");
        singleRightArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleRightArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        arrowButtonsPanel.add(singleRightArrowButton, gridBagConstraints);

        singleLeftArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        singleLeftArrowButton.setText("< ");
        singleLeftArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleLeftArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        arrowButtonsPanel.add(singleLeftArrowButton, gridBagConstraints);

        doubleLeftArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        doubleLeftArrowButton.setText("<<");
        doubleLeftArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleLeftArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        arrowButtonsPanel.add(doubleLeftArrowButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weightx = 0.1;
        requirementListsPanel.add(arrowButtonsPanel, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("This game's requirements");

        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 200));

        thisGameRequirementsList.setModel(this.thisGameRequirementsListModel);
        jScrollPane2.setViewportView(thisGameRequirementsList);

        javax.swing.GroupLayout thisGameReqsPanelLayout = new javax.swing.GroupLayout(thisGameReqsPanel);
        thisGameReqsPanel.setLayout(thisGameReqsPanelLayout);
        thisGameReqsPanelLayout.setHorizontalGroup(
            thisGameReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thisGameReqsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(thisGameReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        thisGameReqsPanelLayout.setVerticalGroup(
            thisGameReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thisGameReqsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        requirementListsPanel.add(thisGameReqsPanel, gridBagConstraints);

        jPanel8.setLayout(new java.awt.GridLayout(1, 3, 10, 0));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Voting deadline", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        deadlineCheckbox.setText("This game has a deadline");
        deadlineCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deadlineCheckboxActionPerformed(evt);
            }
        });

        dateLabel.setText("Date:");

        timeLabel.setText("Time:");

        openCalendarButton.setText("Open date picker");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deadlineCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(timeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(dateField, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openCalendarButton))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(timePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deadlineCheckbox)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateLabel)
                    .addComponent(openCalendarButton)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(timePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel9);

//        javax.swing.GroupLayout calendarPanelLayout = new javax.swing.GroupLayout(calendarHandlerPanel);
//        calendarHandlerPanel.setLayout(calendarPanelLayout);
//        calendarPanelLayout.setHorizontalGroup(
//            calendarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 343, Short.MAX_VALUE)
//        );
//        calendarPanelLayout.setVerticalGroup(
//            calendarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 153, Short.MAX_VALUE)
//        );

        jPanel8.add(calendarHandlerPanel);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game deck", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        jLabel5.setText("Choose deck:");

        deckChoiceComboBox.setModel(this.deckTypeComboBoxModel);
        deckChoiceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deckChoiceComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("Values:");

        deckValuesLabel.setText("[0, 1, 1, 2, 3, 5, 8]");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(deckChoiceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(deckValuesLabel)))
                .addContainerGap(150, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(deckChoiceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(deckValuesLabel))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel11);

        loginPane.setText("Logged in as ...");
        jScrollPane3.setViewportView(loginPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
                    .addComponent(requirementListsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(gameNameFieldPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameNameFieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(requirementListsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buttonsPanel, gameNameFieldPanel});

    }// </editor-fold>

    private void singleRightArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.singleRightArrowButtonClicked();
    }

    private void singleLeftArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.singleLeftArrowButtonClicked();
    }

    private void defaultSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.defaultSettingsButtonClicked();
    }

    private void doubleRightArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.doubleRightArrowButtonClicked();
    }

    private void doubleLeftArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.doubleLeftArrowButtonClicked();
    }

    private void deadlineCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
        this.deadlineCheckboxToggled();
    }

    private void openGameForVotingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.openGameForVotingButtonClicked();
    }

    private void saveChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.saveChangesButtonClicked();
    }

    private void deckChoiceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        String selection = (String)((JComboBox)evt.getSource()).getSelectedItem();
        this.deckChoiceClicked(selection);
    }


    // Variables declaration - do not modify
    private javax.swing.JPanel arrowButtonsPanel;
    private javax.swing.JPanel backlogReqsPanel;
    private javax.swing.JList backlogRequirementsList;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel calendarHandlerPanel;
    private javax.swing.JTextField dateField;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JCheckBox deadlineCheckbox;
    private javax.swing.JComboBox deckChoiceComboBox;
    private javax.swing.JLabel deckValuesLabel;
    private javax.swing.JButton defaultSettingsButton;
    private javax.swing.JButton doubleLeftArrowButton;
    private javax.swing.JButton doubleRightArrowButton;
    private JLabel gameNameLabel;
    private javax.swing.JPanel gameNameFieldPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane loginPane;
    private javax.swing.JButton openCalendarButton;
    private javax.swing.JButton openGameForVotingButton;
    private javax.swing.JPanel requirementListsPanel;
    private javax.swing.JButton saveChangesButton;
    private javax.swing.JButton singleLeftArrowButton;
    private javax.swing.JButton singleRightArrowButton;
    private javax.swing.JPanel thisGameReqsPanel;
    private javax.swing.JList thisGameRequirementsList;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JSpinner timePicker;
    // End of variables declaration
}

