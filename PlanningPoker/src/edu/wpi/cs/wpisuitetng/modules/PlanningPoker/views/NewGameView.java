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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerDateModel;
import javax.swing.border.LineBorder;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email.Mailer;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.im.InstantMessenger;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.DatePicker;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.NewGameTab;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * This JPanel houses the GUI and logic for allowing a user to create a new
 * planning poker game.
 * 
 * @author Austin Rose (atrose) and Lisa and Christian
 */
public class NewGameView extends JPanel {
	private final PlanningPokerGame game;
	
	/**
	 * This method will open up a new tab in the planning poker module with this
	 * UI for creating a new planning poker game.
	 */
	public static void open(PlanningPokerGame game) {
		final NewGameView view = new NewGameView(game);
		MainView.getInstance().addCloseableTab(game.getGameName(), view);
	}

	/**
	 * Constructor initializes all GUI components, then fills in the logic
	 * between them.
	 * 
	 * This constructor is private so that this class can only be initialized
	 * through the static "open" method.
	 */
	private NewGameView(PlanningPokerGame game) {
		this.game = game;
		initComponents(game);
		initComponentLogic(game);

		// Fetch updated set of requirements
		GetRequirementsController.getInstance().retrieveRequirements();
		while (game.getRequirements().get(0) == null) {
		}

		/**
		 * Adds list of current requirements in requirement model to the list
		 * that will be added to the JList that will hold the requirements to be
		 * added to the game
		 */
		final List<Requirement> requirements = RequirementModel.getInstance().getRequirements();

		// We iterate through the requirements list and add to that JList.
		for (int i = 0; i < requirements.size(); i++) {
			Requirement req = requirements.get(i);
			if (game.getRequirements().contains(req)) {
				listModelForThisGame.addElement(req);
			} else if (req.getIteration().equals("Backlog")) {
				listModelForBacklog.addElement(req);
				listModelForReseting.addElement(req);
			}
		}
		btnStartVoting.setEnabled(true);
		thisGameRequirementList.setModel(listModelForBacklog);
	}
	/**
	 * Fills in all dynamic data displayed by components, and adds appropriate
	 * listeners.
	 */
	private void initComponentLogic(PlanningPokerGame game) {

		final boolean gameHasDeadline = game.hasEndDate();

		// Get and add the list of emails to the mailer
		GetPlanningPokerUserController.getInstance().retrieveUser();
		try {
			Thread.sleep(150);
		} catch (Exception e) {
		}

		// The "have a deadline" checkbox listener
		deadline.addActionListener(new ActionListener() {

			private final boolean checked = gameHasDeadline;

			public void actionPerformed(ActionEvent ae) {
				viewHasBeenEdited = true;

				final JCheckBox deadlineCheckbox = (JCheckBox) ae.getSource();

				if (deadlineCheckbox.isSelected()) {
					calendarButton_2.setEnabled(true);
					endTime.setEnabled(true);
				} else {
					if (calendarOpen) {
						calendarButton_2.doClick();
					}
					calendarButton_2.setEnabled(false);
					endTime.setEnabled(false);
				}
			}
		});

		calendarButton_2.addActionListener(new ActionListener() {
			private boolean open = false;
			private DatePicker dp;

			/**
			 * action for using the calendar method for enabling it and
			 * selecting a date
			 */
			public void actionPerformed(ActionEvent ae) {
				viewHasBeenEdited = true;
				if (!open) {
					dp = new DatePicker(calendarPanel, constraints14, endDateText);
					open = true;
				} else {
					dp.close();
					open = false;
				}
				calendarOpen = open;
			}
		});

		// TODO:
		// There should be some deck selection logic here.
		deckType.setModel(new DefaultComboBoxModel<String>(new String[] { "Default", "No Deck" }));

		deckType.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				viewHasBeenEdited = true;

				final JComboBox<String> combo = (JComboBox<String>) e.getSource();

				final String selection = (String) combo.getSelectedItem();

				if (selection.contentEquals("Default")) {
					deckOverview.setText("1, 1, 2, 3, 5, 8, 13, 0?");
				}

				else if (selection.contentEquals("No Deck")) {
					deckOverview.setText("PlanningPokerUser will be able to enter their own estimation");
				}
			}

		});

		/**
		 * Listens to the session name field and disables "Create Game" button
		 * if the field is empty.
		 */
		sessionName.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			/**
			 * Enables create button key to work if all fields are properly
			 * filled out else tells user what is still needed.
			 */
			@Override
			public void keyReleased(KeyEvent arg0) {

				viewHasBeenEdited = true;

				final String currentText = sessionName.getText();

				if (currentText.length() < 1) {

					btnStartVoting.setEnabled(false);
					createGameErrorText.setText("Session needs a name");

				} else {

					// Don't enable the "Create Game" button if there are no
					// requirements
					if (listModelForThisGame.size() == 0) {
						btnStartVoting.setEnabled(false);
					} else {
						btnStartVoting.setEnabled(true);
						createGameErrorText.setText("");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {

			}
		});

		backlogRequirementList.setModel(listModelForThisGame);

		// TODO:
		// As per a meeting with Pollice, we need to only select users which
		// have been explicitly added to the project through the web-interface.

		/**
		 * Saves data entered about the game when 'Create Game' button is
		 * pressed
		 */
		btnStartVoting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				enteredName = sessionName.getText();
				selectedDeckType = (String) deckType.getSelectedItem();
				final GregorianCalendar startCal, endCal;

				// Checks to see if the user set the date to something other
				// than default text
				if (endDateText.getText().equals("Click Calendar to set date")
						&& deadline.isSelected()) {
					System.out.println("Please enter a valid date");
				} else {
					// String[] startDate = startDateText.getText().split("-");
					final String[] endDate = endDateText.getText().split("-");

					final Date endVal = (Date) endTime.getValue();

					/**
					 * Gregorian Calendars save month values starting at 0, so
					 * the months in both of the below calendar has has 1
					 * subtracted from it, as the values are being pulled from
					 * the text field, which does not start at zero
					 */
					startCal = new GregorianCalendar();
					if (deadline.isSelected()) {
						endCal = new GregorianCalendar(Integer.parseInt(endDate[2]), Integer
								.parseInt(endDate[1]) - 1, Integer.parseInt(endDate[0]), endVal
								.getHours(), endVal.getMinutes());
					} else {
						endCal = new GregorianCalendar(9999, 11, 18);
					}
					System.out.println(startCal.toString() + "\n" + endCal.toString());
					System.out.println(enteredName);
					System.out.println(selectedDeckType);

					for (int i = 0; i < listModelForThisGame.size(); i++) {
						gameRequirementIDsList.add(listModelForThisGame.getElementAt(i).getId());
						System.out.println("Requirement Name: " + listModelForThisGame.get(i));
					}

					System.out.println(gameRequirementIDsList.size());

					if (startCal.before(endCal)) {

						PlanningPokerGame game;
						if (deadline.isSelected()) {
							game = new PlanningPokerGame(enteredName, "Default description",

							(String) deckType.getSelectedItem(), gameRequirementIDsList, false,
									true, startCal, endCal, ConfigManager.getConfig().getUserName());
						} else {
							game = new PlanningPokerGame(enteredName, "Default description",
									(String) deckType.getSelectedItem(), gameRequirementIDsList,
									false, false, startCal, endCal, ConfigManager.getConfig()
											.getUserName());

						}
						System.out.println("PlanningPokerUser Moderator: "
								+ ConfigManager.getConfig().getUserName());
						System.out.println("Planning Poker Live: " + game.isLive());
						game.setLive(true);
						game.setFinished(false);
						UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(
								game);
						lblGameCreated.setVisible(true);
						btnStartVoting.setEnabled(false);
						mailer = new Mailer(game);
						mailer.send();
						im = new InstantMessenger(game);
						im.sendAllMessages(PlanningPokerUserModel.getInstance().getUsers());

						MainView.getInstance().refreshGameTree();
						MainView.getInstance().removeClosableTab();
					} else {
						// Error message when the session name is empty
						if (sessionName.getText().isEmpty()) {
							btnStartVoting.setEnabled(false);
							final JOptionPane emptyNameErrorPanel = new JOptionPane(
									"You must enter the session name", JOptionPane.ERROR_MESSAGE);
							final JDialog errorDialog = emptyNameErrorPanel.createDialog(null);
							errorDialog.setLocation(thisPanel.getWidth() / 2,
									thisPanel.getHeight() / 2);
							errorDialog.setVisible(true);
							btnStartVoting.setEnabled(false);
						}
						System.out.println("Start date is after the end date.");

						txtpnLoggedInAs.setText(txtpnLoggedInAs.getText() + " -- INVALID DEADLINE");

					}
				}

			}

		});

		/**
		 * Reset the input field after the user changed the data.
		 */

		btnResetGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewHasBeenEdited = true;
				// Reset game name
				sessionName.setText(dateFormat.format(date));

				// Reset start and end date
				// startDateText.setText(defaultCalendarText);
				endDateText.setText(defaultCalendarText);
				btnStartVoting.setEnabled(true);
				createGameErrorText.setText("");
				// Reset start and end time
				endTime.setEditor(new JSpinner.DateEditor(endTime, "h:mm a"));
				// Reset the requirements boxes

				viewHasBeenEdited = true;

				while (listModelForThisGame.getSize() > 0) {
					System.out.println(listModelForThisGame.elementAt(0));
					listModelForBacklog.addElement(listModelForThisGame.remove(0));
				}

				backlogRequirementList.setModel(listModelForThisGame);
				thisGameRequirementList.setModel(listModelForBacklog);

				btn_removeFromGame.setEnabled(false);
				btn_removeAll.setEnabled(false);
			}
		});
	}

	private void initComponents(PlanningPokerGame game) {

		/**
		 * A dropdown box that contains the default deck to choose.
		 */
		deckType = new JComboBox<String>();
		deckType.setEnabled(false);

		backlogRequirementList = new JList<Requirement>();
		backlogRequirementList.setEnabled(false);
		thisGameRequirementList = new JList<Requirement>();
		thisGameRequirementList.setEnabled(false);

		// List models for the two lists of requirements, and one backup to
		// reset from
		listModelForReseting = new DefaultListModel<Requirement>();
		listModelForThisGame = new DefaultListModel<Requirement>();
		listModelForBacklog = new DefaultListModel<Requirement>();

		gameRequirementIDsList = new ArrayList<Integer>();

		this.setBorder(new LineBorder(Color.DARK_GRAY));
		this.setLayout(new BorderLayout(0, 0));

		titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.add(titlePanel, BorderLayout.NORTH);

		namePane = new JPanel();
		namePane.setLayout(new BoxLayout(namePane, BoxLayout.X_AXIS));
		titlePanel.add(namePane);

		lblName = new JLabel("Name:");
		namePane.add(lblName);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));

		sessionName = new JTextField();
		sessionName.setEnabled(false);
		namePane.add(sessionName);
		sessionName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		date = new Date();
		sessionName.setText(game.getGameName());
		sessionName.setColumns(50);

		createGamePane = new JPanel();
		titlePanel.add(createGamePane);
		createGamePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		btnStartVoting = new JButton("Start voting");
		createGamePane.add(btnStartVoting);
		btnStartVoting.setEnabled(false);

		btnResetGame = new JButton("Default settings");
		btnResetGame.setEnabled(false);
		createGamePane.add(btnResetGame);

		createGameErrorText = new JLabel("");
		titlePanel.add(createGameErrorText);

		label = new JLabel("");
		titlePanel.add(label);

		lblGameCreated = new JLabel("Session Created!");
		titlePanel.add(lblGameCreated);
		lblGameCreated.setVisible(false);

		settingsPanel = new JPanel();
		settingsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.add(settingsPanel, BorderLayout.SOUTH);
		settingsPanel.setLayout(new BorderLayout(0, 0));

		configPanel = new JPanel();
		configPanel.setBorder(new LineBorder(Color.DARK_GRAY));
		settingsPanel.add(configPanel, BorderLayout.NORTH);

		lblConfigureGameSettings = new JLabel("Configure game settings");
		configPanel.add(lblConfigureGameSettings);

		calendarOverview = new JPanel();
		calendarOverview.setBorder(new LineBorder(Color.LIGHT_GRAY));
		settingsPanel.add(calendarOverview, BorderLayout.CENTER);
		calendarOverview.setLayout(new GridLayout(1, 2, 0, 0));

		final SpinnerDateModel model_2 = new SpinnerDateModel();
		model_2.setCalendarField(Calendar.MINUTE);

		calendar = new JPanel();
		calendarOverview.add(calendar);

		lblEndDate = new JLabel("End Date:");

		endDateText = new JTextField(13);
		endDateText.setText(defaultCalendarText);
		endDateText.setEditable(false);
		endDateText.setMinimumSize(new Dimension(endDateText.getPreferredSize().width, endDateText
				.getPreferredSize().height));
		calendarButton_2 = new JButton("Calendar");
		calendarButton_2.setEnabled(false);
		endPanel = new JPanel(new GridBagLayout());
		endPanel.setPreferredSize(new Dimension(350, 220));

		final GridBagConstraints constraints1 = new GridBagConstraints();
		constraints1.insets = new Insets(0, 0, 5, 5);
		constraints1.fill = GridBagConstraints.HORIZONTAL;
		constraints1.gridx = 1;
		constraints1.gridy = 0;
		constraints1.gridwidth = 3;
		constraints1.anchor = GridBagConstraints.LINE_START;
		deadline = new JCheckBox("Have a Deadline?");
		deadline.setEnabled(false);
		endPanel.add(deadline, constraints1);

		final GridBagConstraints constraints8 = new GridBagConstraints();
		constraints8.insets = new Insets(0, 0, 5, 5);
		constraints8.fill = GridBagConstraints.HORIZONTAL;
		constraints8.gridx = 0;
		constraints8.gridy = 1;
		constraints8.anchor = GridBagConstraints.LINE_START;
		endPanel.add(new JLabel("End Time:"), constraints8);
		final GridBagConstraints constraints9 = new GridBagConstraints();
		constraints9.insets = new Insets(0, 0, 5, 5);
		constraints9.fill = GridBagConstraints.HORIZONTAL;
		constraints9.gridx = 1;
		constraints9.gridy = 1;
		endTime = new JSpinner();
		endTime.setEnabled(false);
		endTime.setModel(model_2);
		endTime.setEditor(new JSpinner.DateEditor(endTime, "h:mm a"));
		endPanel.add(endTime, constraints9);

		lblrequired2 = new JLabel("*");
		final GridBagConstraints gbc_lblrequired2 = new GridBagConstraints();
		gbc_lblrequired2.insets = new Insets(0, 0, 5, 0);
		gbc_lblrequired2.anchor = GridBagConstraints.LINE_START;
		gbc_lblrequired2.gridx = 2;
		gbc_lblrequired2.gridy = 1;
		endPanel.add(lblrequired2, gbc_lblrequired2);
		final GridBagConstraints constraints10 = new GridBagConstraints();
		constraints10.insets = new Insets(0, 0, 5, 5);
		constraints10.fill = GridBagConstraints.HORIZONTAL;
		constraints10.gridx = 0;
		constraints10.gridy = 2;
		constraints10.weightx = 0;
		constraints10.anchor = GridBagConstraints.LINE_START;
		endPanel.add(lblEndDate, constraints10);
		final GridBagConstraints constraints11 = new GridBagConstraints();
		constraints11.insets = new Insets(0, 0, 5, 5);
		constraints11.fill = GridBagConstraints.HORIZONTAL;
		constraints11.weightx = 1;
		constraints11.gridx = 1;
		constraints11.gridy = 2;
		endPanel.add(endDateText, constraints11);
		final GridBagConstraints constraints12 = new GridBagConstraints();
		constraints12.insets = new Insets(0, 0, 5, 0);
		constraints12.fill = GridBagConstraints.HORIZONTAL;
		constraints12.weightx = 0;
		constraints12.gridx = 2;
		constraints12.gridy = 2;
		calendarButton_2.setEnabled(false);
		endPanel.add(calendarButton_2, constraints12);
		final GridBagConstraints constraints13 = new GridBagConstraints();
		constraints13.insets = new Insets(0, 0, 5, 5);
		constraints13.gridx = 0;
		constraints13.gridy = 3;
		constraints13.weightx = 1;
		constraints13.weighty = 1;
		endPanel.add(new JLabel(), constraints13);
		constraints14 = new GridBagConstraints();
		constraints14.insets = new Insets(0, 0, 0, 5);
		constraints14.gridx = 0;
		constraints14.gridy = 4;
		constraints14.weightx = 1;
		constraints14.weighty = 1;
		endPanel.add(new JLabel(), constraints14);
		constraints14.weightx = 0;
		constraints14.weighty = 0;

		calendarHandler = new JPanel();
		calendarPanel = new JPanel(new GridBagLayout());
		calendarPanel.setPreferredSize(new Dimension(350, 220));
		calendarHandler.add(calendarPanel);
		calendarOverview.add(calendarHandler);

		calendar.add(endPanel);

		cardPanel = new JPanel(new GridBagLayout());
		deckPanel = new JPanel();
		cardDeckPane = new JPanel();

		calendarOverview.add(deckPanel);

		lblCardDeck = new JLabel("Card deck:");
		cardDeckPane.add(lblCardDeck);

		cardDeckPane.add(deckType);
		deckOverview = new JTextField();
		deckOverview.setText("1, 1, 2, 3, 5, 8, 13, 0?");

		deckOverview.setEditable(false);

		cardPanel.setPreferredSize(new Dimension(350, 220));
		final GridBagConstraints constraints15 = new GridBagConstraints();
		constraints15.fill = GridBagConstraints.HORIZONTAL;
		constraints15.gridx = 0;
		constraints15.gridy = 0;
		cardPanel.add(cardDeckPane, constraints15);
		final GridBagConstraints constraints16 = new GridBagConstraints();
		constraints16.fill = GridBagConstraints.HORIZONTAL;
		constraints16.anchor = GridBagConstraints.LINE_END;
		constraints16.gridx = 0;
		constraints16.gridy = 1;
		cardPanel.add(deckOverview, constraints16);
		final GridBagConstraints constraints17 = new GridBagConstraints();
		constraints17.gridx = 0;
		constraints17.gridy = 2;
		constraints17.weightx = 1;
		constraints17.weighty = 1;
		cardPanel.add(new JLabel(), constraints17);
		deckPanel.add(cardPanel);

		requirementsPanel = new JPanel();
		requirementsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		this.add(requirementsPanel, BorderLayout.CENTER);
		requirementsPanel.setLayout(new BorderLayout(0, 0));

		requirementsHeader = new JPanel();
		requirementsHeader.setBorder(new LineBorder(Color.DARK_GRAY));
		requirementsPanel.add(requirementsHeader, BorderLayout.NORTH);

		lblChooseRequirementsTo = new JLabel("Choose requirements to estimate");
		lblChooseRequirementsTo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		requirementsHeader.add(lblChooseRequirementsTo);

		requirementsSelector = new JPanel();
		requirementsPanel.add(requirementsSelector, BorderLayout.CENTER);
		requirementsSelector.setLayout(new BoxLayout(requirementsSelector, BoxLayout.X_AXIS));

		projectRequirements = new JPanel();
		projectRequirements.setBorder(null);
		projectRequirements.setEnabled(false);
		requirementsSelector.add(projectRequirements);
		projectRequirements.setLayout(new BorderLayout(0, 0));

		projectHeader = new JPanel();
		projectHeader.setBorder(null);
		projectRequirements.add(projectHeader, BorderLayout.NORTH);

		lblAllProjectRequirements = new JLabel("All project requirements");
		projectHeader.add(lblAllProjectRequirements);

		projectList = new JPanel();
		projectList.setBorder(new LineBorder(Color.LIGHT_GRAY));
		scroller = new JScrollPane(projectList);
		projectRequirements.add(scroller, BorderLayout.CENTER);
		projectList.setLayout(new BorderLayout(0, 0));

		projectList.add(thisGameRequirementList);

		addRemPanel = new JPanel();
		addRemPanel.setBorder(new LineBorder(new Color(192, 192, 192), 0));
		requirementsSelector.add(addRemPanel);

		topSpacer = new JPanel();

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(4, 1, 4, 4));

		topmostButton = new JPanel();
		buttonsPanel.add(topmostButton);
		topmostButton.setLayout(new BorderLayout(0, 0));

		btn_addAll = new JButton(">>");
		topmostButton.add(btn_addAll, BorderLayout.CENTER);
		btn_addAll.setEnabled(false);

		topButton = new JPanel();
		buttonsPanel.add(topButton);
		topButton.setLayout(new BorderLayout(0, 0));

		btn_addToGame = new JButton(">");
		btn_addToGame.setEnabled(false);

		topButton.add(btn_addToGame, BorderLayout.CENTER);

		bottomButton = new JPanel();
		buttonsPanel.add(bottomButton);
		bottomButton.setLayout(new BorderLayout(0, 0));

		btn_removeFromGame = new JButton("<");
		bottomButton.add(btn_removeFromGame, BorderLayout.CENTER);
		btn_removeFromGame.setEnabled(false);

		bottommostButton = new JPanel();
		buttonsPanel.add(bottommostButton);
		bottommostButton.setLayout(new BorderLayout(0, 0));

		btn_removeAll = new JButton("<<");
		bottommostButton.add(btn_removeAll);
		btn_removeAll.setEnabled(false);

		bottomSpacer = new JPanel();
		final GroupLayout gl_addRemPanel = new GroupLayout(addRemPanel);
		gl_addRemPanel
				.setHorizontalGroup(gl_addRemPanel
						.createParallelGroup(Alignment.LEADING)
						.addComponent(topSpacer, GroupLayout.PREFERRED_SIZE, 49,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(bottomSpacer, GroupLayout.PREFERRED_SIZE, 49,
								GroupLayout.PREFERRED_SIZE));
		gl_addRemPanel.setVerticalGroup(gl_addRemPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_addRemPanel
								.createSequentialGroup()
								.addComponent(topSpacer, GroupLayout.PREFERRED_SIZE, 25,
										GroupLayout.PREFERRED_SIZE)
								.addGap(3)
								.addComponent(buttonsPanel, GroupLayout.PREFERRED_SIZE, 121,
										GroupLayout.PREFERRED_SIZE)
								.addGap(3)
								.addComponent(bottomSpacer, GroupLayout.PREFERRED_SIZE, 25,
										GroupLayout.PREFERRED_SIZE)));
		addRemPanel.setLayout(gl_addRemPanel);

		gameRequirements = new JPanel();
		gameRequirements.setBorder(null);
		gameRequirements.setEnabled(false);
		requirementsSelector.add(gameRequirements);
		gameRequirements.setLayout(new BorderLayout(0, 0));

		gameHeader = new JPanel();
		gameHeader.setBorder(null);
		gameRequirements.add(gameHeader, BorderLayout.NORTH);

		lblRequirementsToEstimate = new JLabel("Requirements to estimate");
		gameHeader.add(lblRequirementsToEstimate);

		gameList = new JPanel();
		gameList.setBorder(new LineBorder(Color.LIGHT_GRAY));
		scroller_2 = new JScrollPane(gameList);
		gameRequirements.add(scroller_2, BorderLayout.CENTER);
		gameList.setLayout(new BorderLayout(0, 0));

		txtpnLoggedInAs = new JTextPane();
		txtpnLoggedInAs.setText("Logged in as: " + ConfigManager.getConfig().getUserName());
		txtpnLoggedInAs.setFocusable(false);
		settingsPanel.add(txtpnLoggedInAs, BorderLayout.SOUTH);

		gameList.add(backlogRequirementList);
	}
	
	/**
	 * 
	 * @return A Planning Poker Game of this View
	 */
	public PlanningPokerGame getGame() {
		return game;
	}

	private final String defaultCalendarText = "Click Calendar to set date";

	private String selectedDeckType;
	private boolean calendarOpen = false;
	private List<Integer> gameRequirementIDsList;
	private Mailer mailer;
	private InstantMessenger im;
	private boolean viewHasBeenEdited;

	private DateFormat dateFormat;
	private Date date;
	private DefaultListModel<Requirement> listModelForThisGame;
	private DefaultListModel<Requirement> listModelForBacklog;
	private DefaultListModel<Requirement> listModelForReseting;
	private JList<Requirement> backlogRequirementList;
	private JList<Requirement> thisGameRequirementList;
	private JSpinner endTime;
	private String enteredName;
	private JButton btnStartVoting;
	private JLabel lblGameCreated;
	private JButton btn_removeFromGame;
	private JButton btn_addToGame;
	private JButton btn_removeAll;
	private JTextField sessionName;
	private NewGameTab thisPanel;
	private JLabel createGameErrorText;
	private JPanel titlePanel;
	private JPanel namePane;
	private JLabel lblName;
	private JPanel createGamePane;
	private JButton btnResetGame;
	private JButton btnExport;
	private JCheckBox startNow;
	private JLabel label;
	private JPanel settingsPanel;
	private JPanel configPanel;
	private JLabel lblConfigureGameSettings;
	private JPanel calendarOverview;
	private JPanel calendar;
	private JLabel lblEndDate;
	private JTextField endDateText;
	private JButton calendarButton_2;
	private JPanel endPanel;
	private JCheckBox deadline;
	private JLabel lblrequired2;
	private JPanel calendarHandler;
	private JPanel calendarPanel;
	private JPanel cardPanel;
	private JPanel deckPanel;
	private JPanel cardDeckPane;
	private JLabel lblCardDeck;
	private JTextField deckOverview;
	private JPanel requirementsPanel;
	private JPanel requirementsHeader;
	private JLabel lblChooseRequirementsTo;
	private JPanel requirementsSelector;
	private JPanel projectRequirements;
	private JPanel projectHeader;
	private JLabel lblAllProjectRequirements;
	private JPanel projectList;
	private JScrollPane scroller;
	private JPanel addRemPanel;
	private JPanel topSpacer;
	private JPanel buttonsPanel;
	private JPanel topmostButton;
	private JButton btn_addAll;
	private JPanel topButton;
	private JPanel bottomButton;
	private JPanel bottommostButton;
	private JPanel bottomSpacer;
	private JPanel gameRequirements;
	private JPanel gameHeader;
	private JLabel lblRequirementsToEstimate;
	private JPanel gameList;
	private JScrollPane scroller_2;
	private JTextPane txtpnLoggedInAs;
	private JComboBox<String> deckType;
	private GridBagConstraints constraints14;
}
