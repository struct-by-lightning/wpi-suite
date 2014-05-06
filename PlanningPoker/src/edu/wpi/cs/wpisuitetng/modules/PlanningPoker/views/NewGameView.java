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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetDeckController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email.Mailer;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.im.InstantMessenger;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.DeckModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * An instance of this class is a JPanel containing the GUI for interacting with
 * a planning poker game which has not yet been opened for voting.
 * 
 * @author Austin Rose (atrose)
 */
public class NewGameView extends javax.swing.JPanel {

    /**
     * The Planning Poker game which this instance is based on.
     */
    private PlanningPokerGame game;

    /**
     * The list model for the list of requirements for this game.
     */
    private DefaultListModel<String> thisGameRequirementsListModel;

    /**
     * The list model for the list of requirements remaining in the backlog.
     */
    private DefaultListModel<String> backlogRequirementsListModel;

    /**
     * The combo box model for deck type options the user has.
     */
    private DefaultComboBoxModel<String> deckTypeComboBoxModel;

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
     * This is used to determine whether or not the "Save changes" button should
     * be enabled at any given time.
     */
    private boolean changedSinceLastSave = false;

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
        
		GetDeckController.getInstance().retrieveDeck();
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
		this.deckTypeComboBoxModel = new DefaultComboBoxModel<>();
		for (Deck d : DeckModel.getInstance().getDecks()) {
			this.deckTypeComboBoxModel.addElement(d.getDeckName());
		}

        // Run NetBeans-generated UI initialization code.
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
        
        // Make sure the deck values displayed reflect this game's deck type.
        deckChoiceClicked(game.getDeckType());
        
        // Add a document listener to the game description field.
        this.gameDescriptionField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedSinceLastSave = true;
                runErrorChecks();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedSinceLastSave = true;
                runErrorChecks();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changedSinceLastSave = true;
                runErrorChecks();
            }
        });
        
        // Run error validation once before finishing.
        runErrorChecks();
    }
    
    /**
     * This method checks whether or not each component should be enabled, and
     * whether any any text should be displayed.
     */
    private void runErrorChecks() {
        String errorText = "";
        boolean openGameButtonEnabled = true;
        boolean saveChangesButtonEnabled = this.changedSinceLastSave;
        
        // What if there are no requirements in the game?
        if (this.thisGameRequirementsListModel.isEmpty()) {
           openGameButtonEnabled = false;
           saveChangesButtonEnabled = false;
           errorText = "Game must have at least one requirement.";
        }
        
        // What if the date field does not contain a legal date?
        if (!this.datePicker.isEditValid() && this.deadlineCheckbox.isSelected()) {
            openGameButtonEnabled = false;
            saveChangesButtonEnabled = false;
            errorText = "Invalid deadline date given.";
        }
        
        // What if the deadline given has already passed?
        Date today = new Date();
        Date timeGiven = (Date)timePicker.getValue();
        Date deadlineGiven = datePicker.getDate();
        deadlineGiven.setHours(timeGiven.getHours());
        deadlineGiven.setMinutes(timeGiven.getMinutes());
        if (deadlineGiven.before(today) && this.deadlineCheckbox.isSelected()) {
            openGameButtonEnabled = false;
            saveChangesButtonEnabled = false;
            errorText = "Deadline cannot occur in the past.";
        }
        
        // Should the double right arrow button be enabled?
        this.doubleRightArrowButton.setEnabled(!this.backlogRequirementsListModel.isEmpty());
        
        // Should the double left arrow button be enabled?
        this.doubleLeftArrowButton.setEnabled(!this.thisGameRequirementsListModel.isEmpty());
        
        // Should the single right arrow button be enabled?
        this.singleRightArrowButton.setEnabled(this.backlogRequirementsList.getSelectedIndices().length > 0);
        
        // Should the single left arrow button be enabled?
        this.singleLeftArrowButton.setEnabled(this.thisGameRequirementsList.getSelectedIndices().length > 0);

        this.openGameForVotingButton.setEnabled(openGameButtonEnabled);
        this.saveChangesButton.setEnabled(saveChangesButtonEnabled);
        this.errorTextLabel.setText(errorText);
    }
    
    /**
     * @return The Planning Poker game which this instance was constructed for.
     */
    public PlanningPokerGame getGame() {
        return game;
    }
    
    /**
     * This method is called when the user clicks the "Open game for voting" button.
     */
    private void openGameForVotingButtonClicked() {

        // Save any changes the user has made.
        this.saveChangesButtonClicked();
        
        // Make the game live.
        game.setLive(true);
        
        // Update the game in the database.
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

        // Close the tab.
        MainView.getInstance().removeClosableTab();
                        
        // Send out notifications of the game starting. Do this after the tab has already closed, because it is slow.
        mailer = new Mailer(game);
        mailer.addEmailFromUsers(PlanningPokerUserModel.getInstance().getUsers());
        mailer.send();
        im = new InstantMessenger(game);
        im.sendAllMessages(PlanningPokerUserModel.getInstance().getUsers());
    }

    /**
     * This method is called when the user clicks the "Save changes" button.
     */
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
        
        // Construct a GregorianCalendar from the UI components.
        GregorianCalendar endDate = game.getEndDate();
        if (this.deadlineCheckbox.isSelected()) {
            Date baseDate = datePicker.getDate();
            Date timeValues = (Date)timePicker.getValue();
            
            baseDate.setHours(timeValues.getHours());
            baseDate.setMinutes(timeValues.getMinutes());
            
            endDate.setTime(baseDate);
        }
        
        // Update the planning poker game based on changes made to the UI.
        game.setDescription(this.gameDescriptionField.getText());
        game.setDeckType((String)this.deckChoiceComboBox.getSelectedItem());
        game.setRequirementIds(newRequirementIds);
        game.setEndDate(endDate);
        game.setFinished(false);
        game.setLive(false);
       
        // Update the game in the database.
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
        
        // Set the "Save changes" button to be disabled right after it was clicked.
        this.changedSinceLastSave = false;
        this.saveChangesButton.setEnabled(false);
    }

    /**
     * This method is called with an appropriate argument when the user selects
     * a deck type from the drop down menu.
     * @param deckName The deck type the user chose.
     */
    private void deckChoiceClicked(String deckName) {
		Deck selectedDeck = DeckModel.getInstance().getDeck(deckName);
		if (selectedDeck==null || selectedDeck.getDeckNumbers().isEmpty()) {
			this.deckValues.setText("N/A");
		} else {
			this.deckValues.setText(selectedDeck.getDeckNumbers().toString());
		}
    }

    /**
     * This method exists to allow for testing of this UI directly - without actually running Janeway.
     */
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

        gameNameLabelPanel = new javax.swing.JPanel();
        gameNameLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        openGameForVotingButton = new javax.swing.JButton();
        saveChangesButton = new javax.swing.JButton();
        requirementListsPanel = new javax.swing.JPanel();
        backlogRequirementsListPanel = new javax.swing.JPanel();
        backlogRequirementsLabel = new javax.swing.JLabel();
        backlogRequirementsListScrollPane = new javax.swing.JScrollPane();
        backlogRequirementsList = new javax.swing.JList();
        verticalSpaceFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30), new java.awt.Dimension(32767, 30));
        arrowButtonsPanel = new javax.swing.JPanel();
        doubleRightArrowButton = new javax.swing.JButton();
        singleRightArrowButton = new javax.swing.JButton();
        singleLeftArrowButton = new javax.swing.JButton();
        doubleLeftArrowButton = new javax.swing.JButton();
        thisGameReqsPanel = new javax.swing.JPanel();
        thisGameRequirementsLabel = new javax.swing.JLabel();
        thisGameRequirementsListScrollPane = new javax.swing.JScrollPane();
        thisGameRequirementsList = new javax.swing.JList();
        optionsPanel = new javax.swing.JPanel();
        deadlineOptionsPanel = new javax.swing.JPanel();
        deadlineCheckbox = new javax.swing.JCheckBox();
        dateLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        timePicker = new javax.swing.JSpinner();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        blankMiddlePanel = new javax.swing.JPanel();
        deckOptionsPanel = new javax.swing.JPanel();
        chooseDeckLabel = new javax.swing.JLabel();
        deckChoiceComboBox = new javax.swing.JComboBox();
        deckValuesLabel = new javax.swing.JLabel();
        deckValues = new javax.swing.JLabel();
        gameDescriptionFieldPanel = new javax.swing.JPanel();
        gameDescriptionField = new javax.swing.JTextField();
        loggedInAsLabel = new javax.swing.JLabel();
        errorTextLabel = new javax.swing.JLabel();

        gameNameLabelPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        gameNameLabel.setText(this.game.getGameName());
        gameNameLabel.setPreferredSize(new java.awt.Dimension(121, 28));

        javax.swing.GroupLayout gameNameLabelPanelLayout = new javax.swing.GroupLayout(gameNameLabelPanel);
        gameNameLabelPanel.setLayout(gameNameLabelPanelLayout);
        gameNameLabelPanelLayout.setHorizontalGroup(
            gameNameLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameNameLabelPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gameNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gameNameLabelPanelLayout.setVerticalGroup(
            gameNameLabelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameNameLabelPanelLayout.createSequentialGroup()
                .addComponent(gameNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                .addContainerGap())
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(openGameForVotingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveChangesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        requirementListsPanel.setLayout(new java.awt.GridBagLayout());

        backlogRequirementsLabel.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
        backlogRequirementsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        backlogRequirementsLabel.setText("Backlog requirements");

        backlogRequirementsListScrollPane.setPreferredSize(new java.awt.Dimension(300, 200));

        backlogRequirementsList.setModel(this.backlogRequirementsListModel);
        backlogRequirementsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                backlogRequirementsListValueChanged(evt);
            }
        });
        backlogRequirementsListScrollPane.setViewportView(backlogRequirementsList);

        javax.swing.GroupLayout backlogRequirementsListPanelLayout = new javax.swing.GroupLayout(backlogRequirementsListPanel);
        backlogRequirementsListPanel.setLayout(backlogRequirementsListPanelLayout);
        backlogRequirementsListPanelLayout.setHorizontalGroup(
            backlogRequirementsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backlogRequirementsListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backlogRequirementsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backlogRequirementsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(backlogRequirementsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        backlogRequirementsListPanelLayout.setVerticalGroup(
            backlogRequirementsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backlogRequirementsListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backlogRequirementsLabel)
                .addGap(18, 18, 18)
                .addComponent(backlogRequirementsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
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
        requirementListsPanel.add(backlogRequirementsListPanel, gridBagConstraints);
        requirementListsPanel.add(verticalSpaceFiller, new java.awt.GridBagConstraints());

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

        thisGameRequirementsLabel.setFont(new java.awt.Font("Lucida Grande", 3, 13)); // NOI18N
        thisGameRequirementsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thisGameRequirementsLabel.setText("This game's requirements");

        thisGameRequirementsListScrollPane.setPreferredSize(new java.awt.Dimension(300, 200));

        thisGameRequirementsList.setModel(this.thisGameRequirementsListModel);
        thisGameRequirementsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                thisGameRequirementsListValueChanged(evt);
            }
        });
        thisGameRequirementsListScrollPane.setViewportView(thisGameRequirementsList);

        javax.swing.GroupLayout thisGameReqsPanelLayout = new javax.swing.GroupLayout(thisGameReqsPanel);
        thisGameReqsPanel.setLayout(thisGameReqsPanelLayout);
        thisGameReqsPanelLayout.setHorizontalGroup(
            thisGameReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thisGameReqsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(thisGameReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(thisGameRequirementsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(thisGameRequirementsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        thisGameReqsPanelLayout.setVerticalGroup(
            thisGameReqsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(thisGameReqsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(thisGameRequirementsLabel)
                .addGap(18, 18, 18)
                .addComponent(thisGameRequirementsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
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

        optionsPanel.setLayout(new java.awt.GridLayout(1, 3, 10, 0));

        deadlineOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game deadline", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

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
        timePicker.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timePickerStateChanged(evt);
            }
        });

        datePicker.setEnabled(this.game.hasEndDate());
        datePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datePickerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout deadlineOptionsPanelLayout = new javax.swing.GroupLayout(deadlineOptionsPanel);
        deadlineOptionsPanel.setLayout(deadlineOptionsPanelLayout);
        deadlineOptionsPanelLayout.setHorizontalGroup(
            deadlineOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deadlineOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deadlineOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deadlineCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(deadlineOptionsPanelLayout.createSequentialGroup()
                        .addGroup(deadlineOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(timeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(deadlineOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(timePicker))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        deadlineOptionsPanelLayout.setVerticalGroup(
            deadlineOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deadlineOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deadlineCheckbox)
                .addGap(18, 18, 18)
                .addGroup(deadlineOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateLabel)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(deadlineOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel)
                    .addComponent(timePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        optionsPanel.add(deadlineOptionsPanel);

        javax.swing.GroupLayout blankMiddlePanelLayout = new javax.swing.GroupLayout(blankMiddlePanel);
        blankMiddlePanel.setLayout(blankMiddlePanelLayout);
        blankMiddlePanelLayout.setHorizontalGroup(
            blankMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        blankMiddlePanelLayout.setVerticalGroup(
            blankMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        optionsPanel.add(blankMiddlePanel);

        deckOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Game deck", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

        chooseDeckLabel.setText("Choose deck:");

        deckChoiceComboBox.setModel(this.deckTypeComboBoxModel);
        deckChoiceComboBox.setSelectedItem(this.game.getDeckType());
        deckChoiceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deckChoiceComboBoxActionPerformed(evt);
            }
        });

        deckValuesLabel.setText("Values:");

        deckValues.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        deckValues.setText("[0, 1, 1, 2, 3, 5, 8]");

        javax.swing.GroupLayout deckOptionsPanelLayout = new javax.swing.GroupLayout(deckOptionsPanel);
        deckOptionsPanel.setLayout(deckOptionsPanelLayout);
        deckOptionsPanelLayout.setHorizontalGroup(
            deckOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deckOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deckOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deckOptionsPanelLayout.createSequentialGroup()
                        .addComponent(chooseDeckLabel)
                        .addGap(18, 18, 18)
                        .addComponent(deckChoiceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(deckOptionsPanelLayout.createSequentialGroup()
                        .addComponent(deckValuesLabel)
                        .addGap(18, 18, 18)
                        .addComponent(deckValues)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        deckOptionsPanelLayout.setVerticalGroup(
            deckOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deckOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deckOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseDeckLabel)
                    .addComponent(deckChoiceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(deckOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deckValuesLabel)
                    .addComponent(deckValues))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        optionsPanel.add(deckOptionsPanel);

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
            .addComponent(gameDescriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        loggedInAsLabel.setText("Logged in as: " + ConfigManager.getConfig().getUserName());

        errorTextLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        errorTextLabel.setForeground(new java.awt.Color(153, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optionsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1357, Short.MAX_VALUE)
                    .addComponent(requirementListsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gameNameLabelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(gameDescriptionFieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loggedInAsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(errorTextLabel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(gameDescriptionFieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameNameLabelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(requirementListsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(errorTextLabel)
                    .addComponent(loggedInAsLabel))
                .addContainerGap())
        );
    }// </editor-fold>                        

    private void singleRightArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        ArrayList<String> reqsToMove = new ArrayList<>();

        int[] selected = this.backlogRequirementsList.getSelectedIndices();

        for (int i : selected) {
            reqsToMove.add(this.backlogRequirementsListModel.get(i));
        }

        for (String req : reqsToMove) {
            this.backlogRequirementsListModel.removeElement(req);
            this.thisGameRequirementsListModel.addElement(req);
        }
        
        this.changedSinceLastSave = true;
        this.runErrorChecks();
    }                                                      

    private void singleLeftArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        ArrayList<String> reqsToMove = new ArrayList<>();

        int[] selected = this.thisGameRequirementsList.getSelectedIndices();

        for (int i : selected) {
            reqsToMove.add(this.thisGameRequirementsListModel.get(i));
        }

        for (String req : reqsToMove) {
            this.thisGameRequirementsListModel.removeElement(req);
            this.backlogRequirementsListModel.addElement(req);
        }
        
        this.changedSinceLastSave = true;
        this.runErrorChecks();
    }                                                     

    private void doubleRightArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        while (!this.backlogRequirementsListModel.isEmpty()) {
            String req = this.backlogRequirementsListModel.remove(0);
            this.thisGameRequirementsListModel.addElement(req);
        }
        
        this.changedSinceLastSave = true;
        this.runErrorChecks();
    }                                                      

    private void doubleLeftArrowButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        while (!this.thisGameRequirementsListModel.isEmpty()) {
            String req = this.thisGameRequirementsListModel.remove(0);
            this.backlogRequirementsListModel.addElement(req);
        }
        
        this.changedSinceLastSave = true;
        this.runErrorChecks();
    }                                                     

    /**
     * This method is called when the user clicks the "This game has a deadline" checkbox.
     */
    private void deadlineCheckboxActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        boolean checked = this.deadlineCheckbox.isSelected();

        this.dateLabel.setEnabled(checked);
        this.datePicker.setEnabled(checked);
        this.timeLabel.setEnabled(checked);
        this.timePicker.setEnabled(checked);
        
        this.changedSinceLastSave = true;
        this.runErrorChecks();
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
        this.changedSinceLastSave = true;
        this.runErrorChecks();
    }                                                  

    private void gameDescriptionFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        // This method is not needed and for whatever reason I can't make NetBeans delete it.
    }                                                    

    private void datePickerActionPerformed(java.awt.event.ActionEvent evt) {                                           
        this.changedSinceLastSave = true;
        this.runErrorChecks();
    }                                          

    private void backlogRequirementsListValueChanged(javax.swing.event.ListSelectionEvent evt) {                                                     
        this.runErrorChecks();
    }                                                    

    private void thisGameRequirementsListValueChanged(javax.swing.event.ListSelectionEvent evt) {                                                      
        this.runErrorChecks();
    }                                                     

    private void timePickerStateChanged(javax.swing.event.ChangeEvent evt) {                                        
        this.changedSinceLastSave = true;
        this.runErrorChecks();
    }                                       


    // Variables declaration - do not modify                     
    private javax.swing.JPanel arrowButtonsPanel;
    private javax.swing.JLabel backlogRequirementsLabel;
    private javax.swing.JList backlogRequirementsList;
    private javax.swing.JPanel backlogRequirementsListPanel;
    private javax.swing.JScrollPane backlogRequirementsListScrollPane;
    private javax.swing.JPanel blankMiddlePanel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JLabel chooseDeckLabel;
    private javax.swing.JLabel dateLabel;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JCheckBox deadlineCheckbox;
    private javax.swing.JPanel deadlineOptionsPanel;
    private javax.swing.JComboBox deckChoiceComboBox;
    private javax.swing.JPanel deckOptionsPanel;
    private javax.swing.JLabel deckValues;
    private javax.swing.JLabel deckValuesLabel;
    private javax.swing.JButton doubleLeftArrowButton;
    private javax.swing.JButton doubleRightArrowButton;
    private javax.swing.JLabel errorTextLabel;
    private javax.swing.JTextField gameDescriptionField;
    private javax.swing.JPanel gameDescriptionFieldPanel;
    private javax.swing.JLabel gameNameLabel;
    private javax.swing.JPanel gameNameLabelPanel;
    private javax.swing.JLabel loggedInAsLabel;
    private javax.swing.JButton openGameForVotingButton;
    private javax.swing.JPanel optionsPanel;
    private javax.swing.JPanel requirementListsPanel;
    private javax.swing.JButton saveChangesButton;
    private javax.swing.JButton singleLeftArrowButton;
    private javax.swing.JButton singleRightArrowButton;
    private javax.swing.JPanel thisGameReqsPanel;
    private javax.swing.JLabel thisGameRequirementsLabel;
    private javax.swing.JList thisGameRequirementsList;
    private javax.swing.JScrollPane thisGameRequirementsListScrollPane;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JSpinner timePicker;
    private javax.swing.Box.Filler verticalSpaceFiller;
    // End of variables declaration                   
}
