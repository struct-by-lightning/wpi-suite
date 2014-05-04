/**
 * *****************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributor: team
 * struct-by-lightning
 * *****************************************************************************
 */
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerDateModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetDeckController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email.Mailer;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.im.InstantMessenger;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.DeckModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
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
public class CreateGameView extends javax.swing.JPanel {

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
	 * This is used to determine whether or not a popup should warn of unsaved
	 * inputs.
	 */
	private boolean changedSinceOpened = false;

	/**
	 * This method will open up a new tab in the planning poker module with this
	 * UI for creating a new planning poker game.
	 */
	public static void openNewTab() {
		CreateGameView view = new CreateGameView();
		MainView.getInstance().addCloseableTab("Create game", view);
	}

	public boolean hasBeenEdited() {
		return this.changedSinceOpened;
	}

	public CreateGameView() {

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

		this.thisGameRequirementsListModel = new DefaultListModel<>();

		// Populate list of requirements which are remaining in the backlog.
		this.backlogRequirementsListModel = new DefaultListModel<>();
		for (Requirement req : RequirementModel.getInstance().getRequirements()) {
			this.backlogRequirementsListModel.addElement(req.getName());
		}

		// Populate list of deck types.
		this.deckTypeComboBoxModel = new DefaultComboBoxModel<>();
		for (Deck d : DeckModel.getInstance().getDecks()) {
			this.deckTypeComboBoxModel.addElement(d.getDeckName());
		}

		// Run NetBeans-generated UI initialization code.
		initComponents();

		String selectedDeckName = (String) deckChoiceComboBox.getSelectedItem();
		Deck selectedDeck = DeckModel.getInstance().getDeck(selectedDeckName);
		this.deckValues.setText(selectedDeck.getDeckNumbers().toString());

		// Set up the date/time pickers.
		SpinnerDateModel timePickerModel = new SpinnerDateModel();
		timePickerModel.setCalendarField(Calendar.MINUTE);
		final String timeFieldFormatString = "h:mm a";
		DateFormat timeFieldFormat = new SimpleDateFormat(timeFieldFormatString);
		timePicker.setModel(timePickerModel);
		timePicker.setEditor(new JSpinner.DateEditor(timePicker,
				timeFieldFormatString));
		GregorianCalendar tomorrow = new GregorianCalendar();
		tomorrow.add(Calendar.DAY_OF_YEAR, 1);

		// Set the default deadline (if a deadline is enforced) to 24 hours from
		// now.
		datePicker.setDate(tomorrow.getTime());

		// Add change listeners to both the game description and game name text
		// fields.
		for (JTextField field : Arrays.asList(gameDescriptionField,
				gameNameField)) {
			field.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void insertUpdate(DocumentEvent e) {
					changedSinceOpened = true;
					runErrorChecks();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					changedSinceOpened = true;
					runErrorChecks();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					changedSinceOpened = true;
					runErrorChecks();
				}
			});
		}

		// Run error validation once before finishing.
		runErrorChecks();
	}

	/**
	 * This method checks whether or not each component should be enabled, and
	 * whether any any text should be displayed.
	 */
	private void runErrorChecks() {
		String errorText = " ";
		boolean createGameButtonEnabled = true;

		String gameName = this.gameNameField.getText();

		// What if the game name is blank?
		if (gameName.length() == 0) {
			createGameButtonEnabled = false;
			errorText = "Name cannot be blank.";

		}
		else if(PlanningPokerGameModel.getPlanningPokerGame(gameName) != null)
		{
			createGameButtonEnabled = false;
			errorText = "A game already exists with that name.";
		}
		// What if the game name starts or ends with whitespace?
		else {
			String firstCharOfName = gameName.substring(0, 1);
			String lastCharOfName = gameName.substring(gameName.length() - 1,
					gameName.length());

			if (firstCharOfName.trim().length() == 0
					|| lastCharOfName.trim().length() == 0) {
				createGameButtonEnabled = false;
				errorText = "Name cannot begin or end with whitespace.";
			}

		}

		// What if there are no requirements in the game?
		if (this.thisGameRequirementsListModel.isEmpty()) {
			createGameButtonEnabled = false;
			errorText = "Game must have at least one requirement.";
		}

		// What if the date field does not contain a legal date?
		if (!this.datePicker.isEditValid()
				&& this.deadlineCheckbox.isSelected()) {
			createGameButtonEnabled = false;
			errorText = "Invalid deadline date given.";
		}

		// What if the deadline given has already passed?
		Date today = new Date();
		Date timeGiven = (Date) timePicker.getValue();
		Date deadlineGiven = datePicker.getDate();
		deadlineGiven.setHours(timeGiven.getHours());
		deadlineGiven.setMinutes(timeGiven.getMinutes());
		if (deadlineGiven.before(today) && this.deadlineCheckbox.isSelected()) {
			createGameButtonEnabled = false;
			errorText = "Deadline cannot occur in the past.";
		}

		// Should the double right arrow button be enabled?
		this.doubleRightArrowButton
				.setEnabled(!this.backlogRequirementsListModel.isEmpty());

		// Should the double left arrow button be enabled?
		this.doubleLeftArrowButton
				.setEnabled(!this.thisGameRequirementsListModel.isEmpty());

		// Should the single right arrow button be enabled?
		this.singleRightArrowButton.setEnabled(this.backlogRequirementsList
				.getSelectedIndices().length > 0);

		// Should the single left arrow button be enabled?
		this.singleLeftArrowButton.setEnabled(this.thisGameRequirementsList
				.getSelectedIndices().length > 0);

		this.createGameButton.setEnabled(createGameButtonEnabled);
		this.defaultSettingsButton.setEnabled(this.changedSinceOpened);
		this.errorTextLabel.setText(errorText);
	}


	/**
	 * This method is called when the user clicks the "Create Game" button.
	 */
	private void createGameButtonClicked() {

		// The game to construct and add to the database.
		PlanningPokerGame game;

		// Declare each of the parameters necessary for creating a planning
		// poker game.
		String name;
		String description;
		String deckType;
		List<Integer> requirementIds;
		boolean isFinished = false;
		boolean isLive;
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate;
		String moderator = ConfigManager.getConfig().getUserName();

		name = this.gameNameField.getText();

		description = this.gameDescriptionField.getText();

		deckType = (String) this.deckChoiceComboBox.getSelectedItem();

		// Get the list of all requirements (so we can check their name/id
		// pairings).
		List<Requirement> allRequirements = RequirementModel.getInstance()
				.getRequirements();

		// Construct the list of requirement IDs for this game.
		requirementIds = new ArrayList<>();
		for (int i = 0; i < thisGameRequirementsListModel.size(); i++) {
			String reqName = thisGameRequirementsListModel.get(i);
			for (Requirement req : allRequirements) {
				if (req.getName().equals(reqName)) {
					requirementIds.add(new Integer(req.getId()));
					break;
				}
			}
		}

		// Construct the endDate GregorianCalendar from the UI components.
		if (this.deadlineCheckbox.isSelected()) {
			final Date baseDate = datePicker.getDate();
			Date timeValues = (Date) timePicker.getValue();

			baseDate.setHours(timeValues.getHours());
			baseDate.setMinutes(timeValues.getMinutes());

			endDate = new GregorianCalendar() {
				{
					setTime(baseDate);
				}
			};
		} else {
			endDate = new GregorianCalendar(9999, 11, 18);
		}

		isLive = this.openForVotingImmediatelyCheckbox.isSelected();

		game = new PlanningPokerGame(name, description, deckType,
				requirementIds, isFinished, isLive, startDate, endDate,
				moderator);

		// Add the game to the database.
		AddPlanningPokerGameController.getInstance().addPlanningPokerGame(game);
		try {
			Thread.sleep(150);
		} catch (Exception e) {
		}

		// Close the tab.
		MainView.getInstance().removeClosableTab();

		// Send out notifications of the game starting. Do this after the tabe
		// closes because it takes a while. Only send this if the
		// game is open for voting immediately
		if (this.openForVotingImmediatelyCheckbox.isSelected()) {
			mailer = new Mailer(game);
			mailer.addEmailFromUsers(PlanningPokerUserModel.getInstance()
					.getUsers());
			mailer.send();
			im = new InstantMessenger(game);
			im.sendAllMessages(PlanningPokerUserModel.getInstance().getUsers());
		}
	}

	/**
	 * This method is called when the user clicks the "Default settings" button.
	 */
	private void defaultSettingsButtonClicked() {
		// TODO
	}

	/**
	 * This method is called with an appropriate argument when the user selects
	 * a deck type from the drop down menu.
	 * 
	 * @param deckName
	 *            The deck type the user chose.
	 */
	private void deckChoiceClicked(String deckName) {
		Deck selectedDeck = DeckModel.getInstance().getDeck(deckName);
		if (selectedDeck.getDeckNumbers().isEmpty()) {
			this.deckValues.setText("N/A");
		} else {
			this.deckValues.setText(selectedDeck.getDeckNumbers().toString());
		}
	}

	/**
	 * This method exists to allow for testing of this UI directly - without
	 * actually running Janeway.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new CreateGameView());
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
		gameNameField = new javax.swing.JTextField();
		buttonsPanel = new javax.swing.JPanel();
		createGameButton = new javax.swing.JButton();
		defaultSettingsButton = new javax.swing.JButton();
		openForVotingImmediatelyCheckbox = new javax.swing.JCheckBox();
		requirementListsPanel = new javax.swing.JPanel();
		backlogRequirementsListPanel = new javax.swing.JPanel();
		backlogRequirementsLabel = new javax.swing.JLabel();
		backlogRequirementsListScrollPane = new javax.swing.JScrollPane();
		backlogRequirementsList = new javax.swing.JList();
		verticalSpaceFiller = new javax.swing.Box.Filler(
				new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30),
				new java.awt.Dimension(32767, 30));
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

		gameNameLabelPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Game name",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

		gameNameField.setColumns(20);
		gameNameField.setText("Game on "
				+ new SimpleDateFormat("EEEE, MM/dd 'at' hh:mm a")
						.format(new Date()));
		gameNameField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				gameNameFieldActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout gameNameLabelPanelLayout = new javax.swing.GroupLayout(
				gameNameLabelPanel);
		gameNameLabelPanel.setLayout(gameNameLabelPanelLayout);
		gameNameLabelPanelLayout.setHorizontalGroup(gameNameLabelPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						gameNameLabelPanelLayout.createSequentialGroup()
								.addContainerGap().addComponent(gameNameField)
								.addContainerGap()));
		gameNameLabelPanelLayout.setVerticalGroup(gameNameLabelPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						gameNameLabelPanelLayout
								.createSequentialGroup()
								.addComponent(gameNameField,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)));

		createGameButton.setText("Create game");
		createGameButton.setEnabled(false);
		createGameButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createGameButtonActionPerformed(evt);
			}
		});

		defaultSettingsButton.setText("Default settings");
		defaultSettingsButton.setEnabled(false);
		defaultSettingsButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						defaultSettingsButtonActionPerformed(evt);
					}
				});

		openForVotingImmediatelyCheckbox.setSelected(true);
		openForVotingImmediatelyCheckbox.setText("Open for voting immediately");
		openForVotingImmediatelyCheckbox
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						openForVotingImmediatelyCheckboxActionPerformed(evt);
					}
				});

		javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(
				buttonsPanel);
		buttonsPanel.setLayout(buttonsPanelLayout);
		buttonsPanelLayout.setHorizontalGroup(buttonsPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						buttonsPanelLayout
								.createSequentialGroup()
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(createGameButton)
								.addGap(18, 18, 18)
								.addComponent(defaultSettingsButton)
								.addContainerGap())
				.addGroup(
						buttonsPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(openForVotingImmediatelyCheckbox)
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		buttonsPanelLayout
				.setVerticalGroup(buttonsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								buttonsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												buttonsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(
																defaultSettingsButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																createGameButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(
												openForVotingImmediatelyCheckbox)
										.addContainerGap()));

		requirementListsPanel.setLayout(new java.awt.GridBagLayout());

		backlogRequirementsLabel.setFont(new java.awt.Font("Lucida Grande", 3,
				13)); // NOI18N
		backlogRequirementsLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		backlogRequirementsLabel.setText("Backlog requirements");

		backlogRequirementsListScrollPane
				.setPreferredSize(new java.awt.Dimension(300, 200));

		backlogRequirementsList.setModel(this.backlogRequirementsListModel);
		backlogRequirementsList
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						backlogRequirementsListValueChanged(evt);
					}
				});
		backlogRequirementsListScrollPane
				.setViewportView(backlogRequirementsList);

		javax.swing.GroupLayout backlogRequirementsListPanelLayout = new javax.swing.GroupLayout(
				backlogRequirementsListPanel);
		backlogRequirementsListPanel
				.setLayout(backlogRequirementsListPanelLayout);
		backlogRequirementsListPanelLayout
				.setHorizontalGroup(backlogRequirementsListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								backlogRequirementsListPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												backlogRequirementsListPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																backlogRequirementsListScrollPane,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																backlogRequirementsLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		backlogRequirementsListPanelLayout
				.setVerticalGroup(backlogRequirementsListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								backlogRequirementsListPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(backlogRequirementsLabel)
										.addGap(18, 18, 18)
										.addComponent(
												backlogRequirementsListScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												438, Short.MAX_VALUE)
										.addContainerGap()));

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.gridheight = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.2;
		gridBagConstraints.weighty = 1.0;
		requirementListsPanel.add(backlogRequirementsListPanel,
				gridBagConstraints);
		requirementListsPanel.add(verticalSpaceFiller,
				new java.awt.GridBagConstraints());

		arrowButtonsPanel.setLayout(new java.awt.GridBagLayout());

		doubleRightArrowButton.setFont(new java.awt.Font("Courier New", 1, 13)); // NOI18N
		doubleRightArrowButton.setText(">>");
		doubleRightArrowButton.setFocusable(false);
		doubleRightArrowButton
				.addActionListener(new java.awt.event.ActionListener() {
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
		singleRightArrowButton
				.addActionListener(new java.awt.event.ActionListener() {
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
		singleLeftArrowButton
				.addActionListener(new java.awt.event.ActionListener() {
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
		doubleLeftArrowButton
				.addActionListener(new java.awt.event.ActionListener() {
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

		thisGameRequirementsLabel.setFont(new java.awt.Font("Lucida Grande", 3,
				13)); // NOI18N
		thisGameRequirementsLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		thisGameRequirementsLabel.setText("This game's requirements");

		thisGameRequirementsListScrollPane
				.setPreferredSize(new java.awt.Dimension(300, 200));

		thisGameRequirementsList.setModel(this.thisGameRequirementsListModel);
		thisGameRequirementsList
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						thisGameRequirementsListValueChanged(evt);
					}
				});
		thisGameRequirementsListScrollPane
				.setViewportView(thisGameRequirementsList);

		javax.swing.GroupLayout thisGameReqsPanelLayout = new javax.swing.GroupLayout(
				thisGameReqsPanel);
		thisGameReqsPanel.setLayout(thisGameReqsPanelLayout);
		thisGameReqsPanelLayout
				.setHorizontalGroup(thisGameReqsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								thisGameReqsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												thisGameReqsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																thisGameRequirementsListScrollPane,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																thisGameRequirementsLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		thisGameReqsPanelLayout
				.setVerticalGroup(thisGameReqsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								thisGameReqsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(thisGameRequirementsLabel)
										.addGap(18, 18, 18)
										.addComponent(
												thisGameRequirementsListScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												438, Short.MAX_VALUE)
										.addContainerGap()));

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

		deadlineOptionsPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Game deadline",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

		deadlineCheckbox.setText("This game has a deadline");
		deadlineCheckbox.setFocusable(false);
		deadlineCheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deadlineCheckboxActionPerformed(evt);
			}
		});

		dateLabel.setText("Date:");
		dateLabel.setEnabled(false);

		timeLabel.setText("Time:");
		timeLabel.setEnabled(false);

		timePicker.setEnabled(false);
		timePicker.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				timePickerStateChanged(evt);
			}
		});

		datePicker.setEnabled(false);
		datePicker.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				datePickerActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout deadlineOptionsPanelLayout = new javax.swing.GroupLayout(
				deadlineOptionsPanel);
		deadlineOptionsPanel.setLayout(deadlineOptionsPanelLayout);
		deadlineOptionsPanelLayout
				.setHorizontalGroup(deadlineOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								deadlineOptionsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												deadlineOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																deadlineCheckbox,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																deadlineOptionsPanelLayout
																		.createSequentialGroup()
																		.addGroup(
																				deadlineOptionsPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								timeLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								dateLabel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				deadlineOptionsPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								datePicker,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								timePicker))
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		deadlineOptionsPanelLayout
				.setVerticalGroup(deadlineOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								deadlineOptionsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(deadlineCheckbox)
										.addGap(18, 18, 18)
										.addGroup(
												deadlineOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(dateLabel)
														.addComponent(
																datePicker,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												deadlineOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(timeLabel)
														.addComponent(
																timePicker,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		optionsPanel.add(deadlineOptionsPanel);

		blankMiddlePanel.setLayout(new java.awt.GridBagLayout());

		optionsPanel.add(blankMiddlePanel);

		deckOptionsPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Game deck",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

		chooseDeckLabel.setText("Choose deck:");

		deckChoiceComboBox.setModel(this.deckTypeComboBoxModel);
		deckChoiceComboBox
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						deckChoiceComboBoxActionPerformed(evt);
					}
				});

		deckValuesLabel.setText("Values:");

		deckValues.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
		deckValues.setText("[0, 1, 1, 2, 3, 5, 8]");

		javax.swing.GroupLayout deckOptionsPanelLayout = new javax.swing.GroupLayout(
				deckOptionsPanel);
		deckOptionsPanel.setLayout(deckOptionsPanelLayout);
		deckOptionsPanelLayout
				.setHorizontalGroup(deckOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								deckOptionsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												deckOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																deckOptionsPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				chooseDeckLabel)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				deckChoiceComboBox,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																deckOptionsPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				deckValuesLabel)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				deckValues)))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		deckOptionsPanelLayout
				.setVerticalGroup(deckOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								deckOptionsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												deckOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																chooseDeckLabel)
														.addComponent(
																deckChoiceComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												deckOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																deckValuesLabel)
														.addComponent(
																deckValues))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		optionsPanel.add(deckOptionsPanel);

		gameDescriptionFieldPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Game description",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Al Bayan", 0, 10))); // NOI18N

		gameDescriptionField.setText("Game created by "
				+ ConfigManager.getConfig().getUserName() + ".");
		gameDescriptionField
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						gameDescriptionFieldActionPerformed(evt);
					}
				});

		javax.swing.GroupLayout gameDescriptionFieldPanelLayout = new javax.swing.GroupLayout(
				gameDescriptionFieldPanel);
		gameDescriptionFieldPanel.setLayout(gameDescriptionFieldPanelLayout);
		gameDescriptionFieldPanelLayout
				.setHorizontalGroup(gameDescriptionFieldPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								gameDescriptionFieldPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(gameDescriptionField)
										.addContainerGap()));
		gameDescriptionFieldPanelLayout
				.setVerticalGroup(gameDescriptionFieldPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								gameDescriptionFieldPanelLayout
										.createSequentialGroup()
										.addComponent(
												gameDescriptionField,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		loggedInAsLabel.setText("Logged in as: "
				+ ConfigManager.getConfig().getUserName());
		errorTextLabel = new javax.swing.JLabel();

		errorTextLabel.setFont(new Font("Tahoma", Font.PLAIN, 16)); // NOI18N
		errorTextLabel.setForeground(Color.RED);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		layout.setHorizontalGroup(layout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addComponent(
														optionsPanel,
														Alignment.TRAILING,
														GroupLayout.DEFAULT_SIZE,
														901, Short.MAX_VALUE)
												.addComponent(
														requirementListsPanel,
														GroupLayout.DEFAULT_SIZE,
														901, Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		loggedInAsLabel)
																.addGap(0,
																		813,
																		Short.MAX_VALUE))
												.addGroup(
														Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		gameNameLabelPanel,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(18)
																.addComponent(
																		gameDescriptionFieldPanel,
																		GroupLayout.DEFAULT_SIZE,
																		290,
																		Short.MAX_VALUE)
																.addGap(18)
																.addGroup(
																		layout.createParallelGroup(
																				Alignment.LEADING,
																				false)
																				.addComponent(
																						errorTextLabel,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						buttonsPanel,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addComponent(
														buttonsPanel,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(
														layout.createParallelGroup(
																Alignment.TRAILING,
																false)
																.addComponent(
																		gameNameLabelPanel,
																		Alignment.LEADING,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(
																		gameDescriptionFieldPanel,
																		Alignment.LEADING,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)))
								.addGap(2)
								.addComponent(errorTextLabel)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(requirementListsPanel,
										GroupLayout.DEFAULT_SIZE, 320,
										Short.MAX_VALUE)
								.addGap(18)
								.addComponent(optionsPanel,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE).addGap(18)
								.addComponent(loggedInAsLabel)
								.addContainerGap()));
		this.setLayout(layout);
	}// </editor-fold>

	private void singleRightArrowButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		ArrayList<String> reqsToMove = new ArrayList<>();

		int[] selected = this.backlogRequirementsList.getSelectedIndices();

		for (int i : selected) {
			reqsToMove.add(this.backlogRequirementsListModel.get(i));
		}

		for (String req : reqsToMove) {
			this.backlogRequirementsListModel.removeElement(req);
			this.thisGameRequirementsListModel.addElement(req);
		}

		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	private void singleLeftArrowButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		ArrayList<String> reqsToMove = new ArrayList<>();

		int[] selected = this.thisGameRequirementsList.getSelectedIndices();

		for (int i : selected) {
			reqsToMove.add(this.thisGameRequirementsListModel.get(i));
		}

		for (String req : reqsToMove) {
			this.thisGameRequirementsListModel.removeElement(req);
			this.backlogRequirementsListModel.addElement(req);
		}

		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	private void doubleRightArrowButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		while (!this.backlogRequirementsListModel.isEmpty()) {
			String req = this.backlogRequirementsListModel.remove(0);
			this.thisGameRequirementsListModel.addElement(req);
		}

		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	private void doubleLeftArrowButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		while (!this.thisGameRequirementsListModel.isEmpty()) {
			String req = this.thisGameRequirementsListModel.remove(0);
			this.backlogRequirementsListModel.addElement(req);
		}

		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	/**
	 * This method is called when the user clicks the "This game has a deadline"
	 * checkbox.
	 */
	private void deadlineCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
		boolean checked = this.deadlineCheckbox.isSelected();

		this.dateLabel.setEnabled(checked);
		this.datePicker.setEnabled(checked);
		this.timeLabel.setEnabled(checked);
		this.timePicker.setEnabled(checked);

		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	private void createGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
		this.createGameButtonClicked();
	}

	private void defaultSettingsButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		this.defaultSettingsButtonClicked();
	}

	private void deckChoiceComboBoxActionPerformed(
			java.awt.event.ActionEvent evt) {
		String selection = (String) ((JComboBox) evt.getSource())
				.getSelectedItem();
		this.deckChoiceClicked(selection);
		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	private void gameDescriptionFieldActionPerformed(
			java.awt.event.ActionEvent evt) {
		// This method is not needed and for whatever reason I can't make
		// NetBeans delete it.
	}

	private void datePickerActionPerformed(java.awt.event.ActionEvent evt) {
		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	private void backlogRequirementsListValueChanged(
			javax.swing.event.ListSelectionEvent evt) {
		this.runErrorChecks();
	}

	private void thisGameRequirementsListValueChanged(
			javax.swing.event.ListSelectionEvent evt) {
		this.runErrorChecks();
	}

	private void timePickerStateChanged(javax.swing.event.ChangeEvent evt) {
		this.changedSinceOpened = true;
		this.runErrorChecks();
	}

	private void gameNameFieldActionPerformed(java.awt.event.ActionEvent evt) {
		// This method is not needed and for whatever reason I can't make
		// NetBeans delete it.
	}

	private void openForVotingImmediatelyCheckboxActionPerformed(
			java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
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
	private javax.swing.JButton createGameButton;
	private javax.swing.JLabel dateLabel;
	private org.jdesktop.swingx.JXDatePicker datePicker;
	private javax.swing.JCheckBox deadlineCheckbox;
	private javax.swing.JPanel deadlineOptionsPanel;
	private javax.swing.JComboBox deckChoiceComboBox;
	private javax.swing.JPanel deckOptionsPanel;
	private javax.swing.JLabel deckValues;
	private javax.swing.JLabel deckValuesLabel;
	private javax.swing.JButton defaultSettingsButton;
	private javax.swing.JButton doubleLeftArrowButton;
	private javax.swing.JButton doubleRightArrowButton;
	private javax.swing.JLabel errorTextLabel;
	private javax.swing.JTextField gameDescriptionField;
	private javax.swing.JPanel gameDescriptionFieldPanel;
	private javax.swing.JTextField gameNameField;
	private javax.swing.JPanel gameNameLabelPanel;
	private javax.swing.JLabel loggedInAsLabel;
	private javax.swing.JCheckBox openForVotingImmediatelyCheckbox;
	private javax.swing.JPanel optionsPanel;
	private javax.swing.JPanel requirementListsPanel;
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
