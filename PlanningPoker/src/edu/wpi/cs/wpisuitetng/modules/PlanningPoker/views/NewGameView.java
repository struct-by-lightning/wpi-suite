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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email.Mailer;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.im.InstantMessenger;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class NewGameView extends javax.swing.JPanel {

    /**
     * The Planning Poker game which this instance is based on.
     */
    private PlanningPokerGame game;

    /**
     * The list model for the list of requirements for this game.
     */
    private final DefaultListModel<String> thisGameRequirementsListModel;

    /**
     * The list model for the list of requirements remaining in the backlog.
     */
    private final DefaultListModel<String> backlogRequirementsListModel;

    /**
     * The combo box model for deck type options the user has.
     */
    private final DefaultComboBoxModel<String> deckTypeComboBoxModel;

    /**
     * The mailer used when the game is opened for voting and users need to be
     * notified.
     */
    private Mailer mailer;

    /**
     * The instant messenger used when the game is opened for voting and users
     * need to be notified.
     */
    private InstantMessenger im;

    /**
     * This method will open up a new tab in the planning poker module with this
     * UI for viewing a the planning poker game.
     *
     * @return: The game to construct the new instance around.
     */
    public static void open(PlanningPokerGame game) {
        NewGameView view = new NewGameView(game);
        MainView.getInstance().addCloseableTab(game.getGameName(), view);
    }

    public NewGameView(PlanningPokerGame game) {

        this.game = game;

        // Fetch updated set of requirements from the database.
        GetRequirementsController.getInstance().retrieveRequirements();
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }

        // Get and add the list of emails to the mailer
        GetPlanningPokerUserController.getInstance().retrieveUser();
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }

        // Populate list of requirements associated with this game.
        this.thisGameRequirementsListModel = new DefaultListModel<>();
        ArrayList<Requirement> requirements = (ArrayList<Requirement>) game.getRequirements();
        for (Requirement req : requirements) {
            this.thisGameRequirementsListModel.addElement(req.getName());
        }

        // Populate list of requriements which are remaining in the backlog.
        this.backlogRequirementsListModel = new DefaultListModel<>();
        for (Requirement req : RequirementModel.getInstance().getRequirements()) {
            if (!this.thisGameRequirementsListModel.contains(req.getName())) {
                this.backlogRequirementsListModel.addElement(req.getName());
            }
        }

        // Populate list of deck types.
        this.deckTypeComboBoxModel = new DefaultComboBoxModel<String>(new String[]{"Default", "No Deck"});

        // NETBEANS GO HAM.
        initComponents();
        
        // Set up the date/time pickers.
        SpinnerDateModel timePickerModel = new SpinnerDateModel();
        timePickerModel.setCalendarField(Calendar.MINUTE);
        final String timeFieldFormatString = "h:mm a";
        DateFormat timeFieldFormat = new SimpleDateFormat(timeFieldFormatString);
        timePicker.setModel(timePickerModel);
        timePicker.setEditor(new JSpinner.DateEditor(timePicker, timeFieldFormatString));
        datePicker.setDate(new Date());
            
        // Populate values of date and time picker if needed.
        if (game.hasEndDate()) {
            timePicker.setValue(game.getEndDate().getTime());
            datePicker.setDate(game.getEndDate().getTime());
        }
    }
    
    /**
     * @return The Planning Poker game which this instance was constructed for.
     */
    public PlanningPokerGame getGame() {
        return game;
    }
    
    /**
     * This method is called when the user clicks the "Default settings" button.
     */
    private void defaultSettingsButtonClicked() {
        // TODO
    }
    
    private void openGameForVotingButtonClicked() {

        this.saveChangesButtonClicked();
        
        game.setLive(true);
        game.setFinished(false);
        
        UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(game);
        try {
            Thread.sleep(300);
        } catch (Exception e) {
        }
        
        GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
        try {
            Thread.sleep(300);
        } catch (Exception e) {
        }
        
        mailer = new Mailer(game);
        mailer.send();
        
        im = new InstantMessenger(game);
        im.sendAllMessages(PlanningPokerUserModel.getInstance().getUsers());

        // Close this tab.
        MainView.getInstance().removeClosableTab();
    }

    private void saveChangesButtonClicked() {

        // Get the list of all requirements (so we can check their name/id pairings).
        List<Requirement> allRequirements = RequirementModel.getInstance().getRequirements();
        
        // Construct the new list of requirement IDs for this game.
        ArrayList<Integer> newRequirementIds = new ArrayList<>();
        for (int i = 0; i < thisGameRequirementsListModel.size(); i++) {
            String reqName = thisGameRequirementsListModel.get(i);
            for (Requirement req : allRequirements) {
                if (req.getName().equals(reqName)) {
                    newRequirementIds.add(new Integer(req.getId()));
                    break;
                }
            }
        }
        
        GregorianCalendar endDate = game.getEndDate();
        if (this.deadlineCheckbox.isSelected()) {
            Date baseDate = datePicker.getDate();
            Date timeValues = (Date)timePicker.getValue();
            
            baseDate.setHours(timeValues.getHours());
            baseDate.setMinutes(timeValues.getMinutes());
            
            endDate.setTime(baseDate);
        }
        
        game.setDescription(this.gameDescriptionField.getText());
        game.setDeckType((String)this.deckChoiceComboBox.getSelectedItem());
        game.setRequirementIds(newRequirementIds);
        game.setEndDate(endDate);
        game.setFinished(false);
        game.setLive(false);
       
        UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(game);
        try {
            Thread.sleep(300);
        } catch (Exception e) {
        }
        
        GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
        try {
            Thread.sleep(300);
        } catch (Exception e) {
        }

    }

    private void deckChoiceClicked(String deckName) {
        this.deckValuesLabel.setText(deckName.equals("Default") ? "[0, 1, 1, 2, 3, 5, 8]" : "N/A");
    }

    private void doubleRightArrowButtonClicked() {
        while (!this.backlogRequirementsListModel.isEmpty()) {
            String req = this.backlogRequirementsListModel.remove(0);
            this.thisGameRequirementsListModel.addElement(req);
        }
    }

    private void doubleLeftArrowButtonClicked() {
        while (!this.thisGameRequirementsListModel.isEmpty()) {
            String req = this.thisGameRequirementsListModel.remove(0);
            this.backlogRequirementsListModel.addElement(req);
        }
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
    }

    private void deadlineCheckboxToggled() {
        boolean checked = this.deadlineCheckbox.isSelected();

        this.dateLabel.setEnabled(checked);
        this.datePicker.setEnabled(checked);
        this.timeLabel.setEnabled(checked);
        this.timePicker.setEnabled(checked);

    }

    public static void main(String[] args) {
        ArrayList<Integer> test1reqs = new ArrayList<Integer>(Arrays.asList(0, 1, 2));
        GregorianCalendar test1start = new GregorianCalendar();
        GregorianCalendar test1end = new GregorianCalendar(9999, 11, 18);
        PlanningPokerGame test1game = new PlanningPokerGame("Game Name 1", "Game Description 1", "Default", test1reqs, false, false, test1start, test1end, "NB_FILLER_USERNAME");
        
        ArrayList<Integer> test2reqs = new ArrayList<Integer>(Arrays.asList(0, 1));
        GregorianCalendar test2start = new GregorianCalendar();
        GregorianCalendar test2end = new GregorianCalendar();
        test2end.add(GregorianCalendar.DATE, 3);
        PlanningPokerGame test2game = new PlanningPokerGame("Game Name 2", "Game Description 2", "Default", test2reqs, false, false, test2start, test2end, "NB_FILLER_USERNAME");
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new NewGameView(test1game));
        frame.pack();
        frame.setVisible(true);
        frame.pack();
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
        jLabel4 = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        defaultSettingsButton = new javax.swing.JButton();
        openGameForVotingButton = new javax.swing.JButton();
        saveChangesButton = new javax.swing.JButton();
        requirementListsPanel = new javax.swing.JPanel();
        backlogReqsPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        backlogRequirementsList = new javax.swing.JList();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30), new java.awt.Dimension(32767, 30));
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
        timePicker = new javax.swing.JSpinner();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        calendarHandlerPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        deckChoiceComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        deckValuesLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        loginPane = new javax.swing.JTextPane();
        gameDescriptionFieldPanel = new javax.swing.JPanel();
        gameDescriptionField = new javax.swing.JTextField();

        gameNameFieldPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        jLabel4.setText(this.game.getGameName());
        jLabel4.setPreferredSize(new java.awt.Dimension(121, 28));

        javax.swing.GroupLayout gameNameFieldPanelLayout = new javax.swing.GroupLayout(gameNameFieldPanel);
        gameNameFieldPanel.setLayout(gameNameFieldPanelLayout);
        gameNameFieldPanelLayout.setHorizontalGroup(
            gameNameFieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameNameFieldPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gameNameFieldPanelLayout.setVerticalGroup(
            gameNameFieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameNameFieldPanelLayout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        defaultSettingsButton.setText("Default settings");
        defaultSettingsButton.setEnabled(false);
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

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
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
        requirementListsPanel.add(filler1, new java.awt.GridBagConstraints());

        arrowButtonsPanel.setLayout(new java.awt.GridBagLayout());

        doubleRightArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        doubleRightArrowButton.setText(">>");
        doubleRightArrowButton.setFocusable(false);
        doubleRightArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleRightArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        arrowButtonsPanel.add(doubleRightArrowButton, gridBagConstraints);

        singleRightArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        singleRightArrowButton.setText(" >");
        singleRightArrowButton.setFocusable(false);
        singleRightArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleRightArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        arrowButtonsPanel.add(singleRightArrowButton, gridBagConstraints);

        singleLeftArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        singleLeftArrowButton.setText("< ");
        singleLeftArrowButton.setFocusable(false);
        singleLeftArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleLeftArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        arrowButtonsPanel.add(singleLeftArrowButton, gridBagConstraints);

        doubleLeftArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
        doubleLeftArrowButton.setText("<<");
        doubleLeftArrowButton.setFocusable(false);
        doubleLeftArrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doubleLeftArrowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        arrowButtonsPanel.add(doubleLeftArrowButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weightx = 0.1;
        requirementListsPanel.add(arrowButtonsPanel, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
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

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game deadline", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        deadlineCheckbox.setSelected(game.hasEndDate());
        deadlineCheckbox.setText("This game has a deadline");
        deadlineCheckbox.setFocusable(false);
        deadlineCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deadlineCheckboxActionPerformed(evt);
            }
        });

        dateLabel.setText("Date:");
        dateLabel.setEnabled(this.game.hasEndDate());

        timeLabel.setText("Time:");
        timeLabel.setEnabled(this.game.hasEndDate());

        timePicker.setEnabled(this.game.hasEndDate());

        datePicker.setEnabled(this.game.hasEndDate());
        datePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datePickerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deadlineCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(timeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(timePicker))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(timePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel9);

        javax.swing.GroupLayout calendarHandlerPanelLayout = new javax.swing.GroupLayout(calendarHandlerPanel);
        calendarHandlerPanel.setLayout(calendarHandlerPanelLayout);
        calendarHandlerPanelLayout.setHorizontalGroup(
            calendarHandlerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 343, Short.MAX_VALUE)
        );
        calendarHandlerPanelLayout.setVerticalGroup(
            calendarHandlerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 171, Short.MAX_VALUE)
        );

        jPanel8.add(calendarHandlerPanel);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game deck", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        jLabel5.setText("Choose deck:");

        deckChoiceComboBox.setModel(this.deckTypeComboBoxModel);
        deckChoiceComboBox.setSelectedItem(this.game.getDeckType());
        deckChoiceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deckChoiceComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("Values:");

        deckValuesLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
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
                .addContainerGap(101, Short.MAX_VALUE))
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
                .addContainerGap(74, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel11);

        loginPane.setText("Logged in as: " + ConfigManager.getConfig().getUserName());
        loginPane.setEnabled(false);
        jScrollPane3.setViewportView(loginPane);

        gameDescriptionFieldPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        gameDescriptionField.setText(this.game.getDescription());
        gameDescriptionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameDescriptionFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gameDescriptionFieldPanelLayout = new javax.swing.GroupLayout(gameDescriptionFieldPanel);
        gameDescriptionFieldPanel.setLayout(gameDescriptionFieldPanelLayout);
        gameDescriptionFieldPanelLayout.setHorizontalGroup(
            gameDescriptionFieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameDescriptionFieldPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gameDescriptionField)
                .addContainerGap())
        );
        gameDescriptionFieldPanelLayout.setVerticalGroup(
            gameDescriptionFieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameDescriptionFieldPanelLayout.createSequentialGroup()
                .addComponent(gameDescriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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
                        .addGap(18, 18, 18)
                        .addComponent(gameDescriptionFieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(108, 108, 108)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(gameNameFieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gameDescriptionFieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(requirementListsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
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
        String selection = (String) ((JComboBox) evt.getSource()).getSelectedItem();
        this.deckChoiceClicked(selection);
    }                                                  

    private void gameDescriptionFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        // TODO add your handling code here:
    }                                                    

    private void datePickerActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          


    // Variables declaration - do not modify                     
    private javax.swing.JPanel arrowButtonsPanel;
    private javax.swing.JPanel backlogReqsPanel;
    private javax.swing.JList backlogRequirementsList;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel calendarHandlerPanel;
    private javax.swing.JLabel dateLabel;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JCheckBox deadlineCheckbox;
    private javax.swing.JComboBox deckChoiceComboBox;
    private javax.swing.JLabel deckValuesLabel;
    private javax.swing.JButton defaultSettingsButton;
    private javax.swing.JButton doubleLeftArrowButton;
    private javax.swing.JButton doubleRightArrowButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JTextField gameDescriptionField;
    private javax.swing.JPanel gameDescriptionFieldPanel;
    private javax.swing.JPanel gameNameFieldPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane loginPane;
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
