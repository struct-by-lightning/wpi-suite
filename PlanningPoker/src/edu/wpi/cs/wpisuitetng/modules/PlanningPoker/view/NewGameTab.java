/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;


/**
 * @author sfmailand
 * @author hlong290494
 * 
 * This creates a tab for New games to be made. 
 */

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JTextField;

import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.SpinnerDateModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email.Mailer;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

import java.awt.Insets;

/**
 * Implements the new game tab for planning poker module
 * 
 * @author Struct-by-lightning
 */
public class NewGameTab extends JPanel {
	private JTextField sessionName;
	private NewGameTab thisPanel;
	final String defaultCalendarText = "Click Calendar to set date";
	/**
	 * Error label that will show the reason why a game cannot be created
	 */
	JLabel createGameErrorText;
	
	//indicates whether the user edited the new game tab
	/*
	***THE FOLLOWING CODE NEEDS TO BE ADDED TO EVERY USER ACTION LISTENER:***
	isTabEditedByUser = true;
	*/
	public boolean isTabEditedByUser; 
	
	/**
	 * A dropdown box that contains the default deck to choose.
	 */
	JComboBox<String> deckType = new JComboBox<String>();
	String selectedDeckType = new String();
	/**
	 * A list contains of available requirements to add to the session
	 */
	JList<Requirement> selectedRequirements = new JList<Requirement>();

	/**
	 * A JList that contains the requirements that are to be estimated in the current planning poker session
	 */
	JList<Requirement> allRequirements = new JList<Requirement>();

	/**
	 * These two lists contain the lists representing what the user has selected
	 * These lists are then put into the JList swing component, which uses them
	 * to display what the user will see in the GUI
	 * 
	 * listOfRequirementsToAdd --> the list of requirements that the user wants added to the game
	 * 
	 * listOfAllRequirements -> the list of all the requirements in the requirement manager
	 */
	DefaultListModel<Requirement> listOfRequirementsToAdd= new DefaultListModel<Requirement>();
	DefaultListModel<Requirement> listOfAllRequirements= new DefaultListModel<Requirement>();
	
	
	/**
	 * The list of requirements that will actually be saved to the game
	 * will be the same as 'listOfRequirementsToAdd' once the game is
	 * in the process of being created
	 */
	List<Requirement> gameRequirementsList = new ArrayList<Requirement>();

	JSpinner startTime, endTime;
	String enteredName = new String();
	GregorianCalendar startCalendar;
	GregorianCalendar endCalendar;
	
	JButton btnCreateGame;
	JLabel lblGameCreated;

	/**
	 * Create the new game panel.
	 */
	public NewGameTab() {
		isTabEditedByUser = false;
		
		thisPanel = this;
		setBorder(new LineBorder(Color.DARK_GRAY));
		setLayout(new BorderLayout(0, 0));

		JPanel titlePanel = new JPanel();
		FlowLayout fl_titlePanel = (FlowLayout) titlePanel.getLayout();
		fl_titlePanel.setAlignment(FlowLayout.LEFT);
		titlePanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		add(titlePanel, BorderLayout.NORTH);

		JPanel namePane = new JPanel();
		titlePanel.add(namePane);

		JLabel lblName = new JLabel("Name:");
		namePane.add(lblName);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));

		sessionName = new JTextField();
		namePane.add(sessionName);
		sessionName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		final Date date = new Date();
		sessionName.setText(dateFormat.format(date));
		sessionName.setColumns(50);

		JPanel createGamePane = new JPanel();
		titlePanel.add(createGamePane);
		createGamePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		btnCreateGame = new JButton("CREATE GAME");
		createGamePane.add(btnCreateGame);

		JButton btnResetGame = new JButton("RESET GAME");
		createGamePane.add(btnResetGame);
		
		createGameErrorText = new JLabel("");
		titlePanel.add(createGameErrorText);
		
		JLabel label = new JLabel("");
		titlePanel.add(label);
		
		lblGameCreated = new JLabel("Session Created!");
		titlePanel.add(lblGameCreated);
		lblGameCreated.setVisible(false);

		JPanel settingsPanel = new JPanel();
		settingsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		add(settingsPanel, BorderLayout.SOUTH);
		settingsPanel.setLayout(new BorderLayout(0, 0));

		JPanel configPanel = new JPanel();
		configPanel.setBorder(new LineBorder(Color.DARK_GRAY));
		settingsPanel.add(configPanel, BorderLayout.NORTH);

		JLabel lblConfigureGameSettings = new JLabel("Configure game settings");
		configPanel.add(lblConfigureGameSettings);

		JPanel calendarOverview = new JPanel();
		calendarOverview.setBorder(new LineBorder(Color.LIGHT_GRAY));
		settingsPanel.add(calendarOverview, BorderLayout.CENTER);
		calendarOverview.setLayout(new GridLayout(1, 3, 0, 0));

		JPanel calendarOne = new JPanel();
		calendarOverview.add(calendarOne);

		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);
		SpinnerDateModel model_2 = new SpinnerDateModel();
		model_2.setCalendarField(Calendar.MINUTE);

		JLabel lblStart = new JLabel("Start Date:");

		final JTextField startDateText = new JTextField(13);
		startDateText.setText(defaultCalendarText);
		startDateText.setEditable(false);
		startDateText.setMinimumSize(new Dimension (startDateText.getPreferredSize().width, startDateText.getPreferredSize().height));
		JButton calendarButton = new JButton("Calendar");
		final JPanel startPanel = new JPanel(new GridBagLayout());
		startPanel.setPreferredSize(new Dimension(350, 220));
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 5, 5);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		startPanel.add(new JLabel("Start Time:"), constraints);
		final GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.insets = new Insets(0, 0, 5, 5);
		constraints2.fill = GridBagConstraints.HORIZONTAL;
		constraints2.gridx = 1;
		constraints2.gridy = 0;
		startTime= new JSpinner();
		startTime.setModel(model);
		startTime.setEditor(new JSpinner.DateEditor(startTime, "h:mm a"));
		startPanel.add(startTime, constraints2);
		
		JLabel lblrequired = new JLabel("*");
		lblrequired.setForeground(Color.RED);
		GridBagConstraints gbc_lblrequired = new GridBagConstraints();
		gbc_lblrequired.insets = new Insets(0, 0, 5, 0);
		gbc_lblrequired.gridx = 2;
		gbc_lblrequired.gridy = 0;
		startPanel.add(lblrequired, gbc_lblrequired);
		final GridBagConstraints constraints3 = new GridBagConstraints();
		constraints3.insets = new Insets(0, 0, 5, 5);
		constraints3.fill = GridBagConstraints.HORIZONTAL;
		constraints3.gridx = 0;
		constraints3.gridy = 1;
		constraints3.weightx = 0;
		constraints3.anchor = GridBagConstraints.LINE_START;
		startPanel.add(lblStart, constraints3);
		final GridBagConstraints constraints4 = new GridBagConstraints();
		constraints4.insets = new Insets(0, 0, 5, 5);
		constraints4.fill = GridBagConstraints.HORIZONTAL;
		constraints4.weightx = 1;
		constraints4.gridx = 1;
		constraints4.gridy = 1;
		startPanel.add(startDateText, constraints4);
		final GridBagConstraints constraints5 = new GridBagConstraints();
		constraints5.insets = new Insets(0, 0, 5, 0);
		constraints5.fill = GridBagConstraints.HORIZONTAL;
		constraints5.weightx = 0;
		constraints5.gridx = 2;
		constraints5.gridy = 1;
		startPanel.add(calendarButton, constraints5);
		final GridBagConstraints constraints6 = new GridBagConstraints();
		constraints6.insets = new Insets(0, 0, 5, 5);
		constraints6.gridx = 0;
		constraints6.gridy = 2;
		constraints6.weightx = 1;
		constraints6.weighty = 1;
		startPanel.add(new JLabel(), constraints6);
		final GridBagConstraints constraints7 = new GridBagConstraints();
		constraints7.insets = new Insets(0, 0, 0, 5);
		constraints7.gridx = 0;
		constraints7.gridy = 3;
		constraints7.weightx = 1;
		constraints7.weighty = 1;
		startPanel.add(new JLabel(), constraints7);
		constraints.weightx = 0;
		constraints.weighty = 0;
//		final JFrame f = new JFrame();
//		f.getContentPane().add(p);
//		f.pack();
//		f.setVisible(true);
		calendarButton.addActionListener(new ActionListener() {
			
			boolean open = false;
			DatePicker dp;
			public void actionPerformed(ActionEvent ae) {
				isTabEditedByUser = true;
				
				if(!open) {
					dp = new DatePicker(startPanel, constraints7, startDateText);
					open = true;
				}
				else {
					dp.close();
					open = false;
				}
			}
		});

		calendarOne.add(startPanel);

//		textField = new JTextField();
//		panel_18.add(textField);
//		textField.setColumns(10);

		JPanel calendarTwo = new JPanel();
		calendarOverview.add(calendarTwo);

		JLabel lblEndDate = new JLabel("End Date:");

		final JTextField endDateText = new JTextField(13);
		endDateText.setText(defaultCalendarText);
		endDateText.setEditable(false);
		endDateText.setMinimumSize(new Dimension (endDateText.getPreferredSize().width, endDateText.getPreferredSize().height));
		JButton calendarButton_2 = new JButton("Calendar");
		final JPanel endPanel = new JPanel(new GridBagLayout());
		endPanel.setPreferredSize(new Dimension(350, 220));
		final GridBagConstraints constraints8 = new GridBagConstraints();
		constraints8.insets = new Insets(0, 0, 5, 5);
		constraints8.fill = GridBagConstraints.HORIZONTAL;
		constraints8.gridx = 0;
		constraints8.gridy = 0;
		constraints8.anchor = GridBagConstraints.LINE_START;
		endPanel.add(new JLabel("End Time:"), constraints8);
		final GridBagConstraints constraints9 = new GridBagConstraints();
		constraints9.insets = new Insets(0, 0, 5, 5);
		constraints9.fill = GridBagConstraints.HORIZONTAL;
		constraints9.gridx = 1;
		constraints9.gridy = 0;
		//constraints9.weightx = 1;
		endTime= new JSpinner();
		endTime.setModel(model_2);
		endTime.setEditor(new JSpinner.DateEditor(endTime, "h:mm a"));
		endPanel.add(endTime, constraints9);
		
		JLabel lblrequired2 = new JLabel("*Required");
		lblrequired2.setForeground(Color.RED);
		GridBagConstraints gbc_lblrequired2 = new GridBagConstraints();
		gbc_lblrequired2.insets = new Insets(0, 0, 5, 0);
		gbc_lblrequired2.gridx = 2;
		gbc_lblrequired2.gridy = 0;
		endPanel.add(lblrequired2, gbc_lblrequired2);
		final GridBagConstraints constraints10 = new GridBagConstraints();
		constraints10.insets = new Insets(0, 0, 5, 5);
		constraints10.fill = GridBagConstraints.HORIZONTAL;
		constraints10.gridx = 0;
		constraints10.gridy = 1;
		constraints10.weightx = 0;
		constraints10.anchor = GridBagConstraints.LINE_START;
		endPanel.add(lblEndDate, constraints10);
		final GridBagConstraints constraints11 = new GridBagConstraints();
		constraints11.insets = new Insets(0, 0, 5, 5);
		constraints11.fill = GridBagConstraints.HORIZONTAL;
		constraints11.weightx = 1;
		constraints11.gridx = 1;
		constraints11.gridy = 1;
		endPanel.add(endDateText, constraints11);
		final GridBagConstraints constraints12 = new GridBagConstraints();
		constraints12.insets = new Insets(0, 0, 5, 0);
		constraints12.fill = GridBagConstraints.HORIZONTAL;
		constraints12.weightx = 0;
		constraints12.gridx = 2;
		constraints12.gridy = 1;
		endPanel.add(calendarButton_2, constraints12);
		final GridBagConstraints constraints13 = new GridBagConstraints();
		constraints13.insets = new Insets(0, 0, 5, 5);
		constraints13.gridx = 0;
		constraints13.gridy = 2;
		constraints13.weightx = 1;
		constraints13.weighty = 1;
		endPanel.add(new JLabel(), constraints13);
		final GridBagConstraints constraints14 = new GridBagConstraints();
		constraints14.insets = new Insets(0, 0, 0, 5);
		constraints14.gridx = 0;
		constraints14.gridy = 3;
		constraints14.weightx = 1;
		constraints14.weighty = 1;
		endPanel.add(new JLabel(), constraints14);
		constraints14.weightx = 0;
		constraints14.weighty = 0;
//		final JFrame f = new JFrame();
//		f.getContentPane().add(p);
//		f.pack();
//		f.setVisible(true);
		calendarButton_2.addActionListener(new ActionListener() {
			boolean open = false;
			DatePicker dp;
			public void actionPerformed(ActionEvent ae) {
				if(!open) {
					dp = new DatePicker(endPanel, constraints14, endDateText);
					open = true;
				}
				else {
					dp.close();
					open = false;
				}
			}
		});

		calendarTwo.add(endPanel);

		JPanel cardPanel = new JPanel(new GridBagLayout());
		JPanel deckPanel = new JPanel();
		JPanel cardDeckPane = new JPanel();

		calendarOverview.add(deckPanel);
		//JPanel deckDisplayPane = new JPanel();

		//calendarOverview.add(deckDisplayPane);
		JLabel lblCardDeck = new JLabel("Card deck:");
		cardDeckPane.add(lblCardDeck);
		deckType.setModel(new DefaultComboBoxModel<String>(new String[] {"Fibonacci", "Other", "No Deck"}));
		//deckType.setMinimumSize(new Dimension (deckType.getPreferredSize().width, deckType.getPreferredSize().height));
		
		cardDeckPane.add(deckType);
		final JTextField deckOverview = new JTextField();
		deckOverview.setText("1, 1, 2, 3, 5, 8, 13, 21");
		deckOverview.setHorizontalAlignment(WIDTH/2);
		deckOverview.setEditable(false);

		cardPanel.setPreferredSize(new Dimension(350, 220));
		final GridBagConstraints constraints15 = new GridBagConstraints();
		constraints15.fill = GridBagConstraints.HORIZONTAL;
		constraints15.gridx = 0;
		constraints15.gridy = 0;
		cardPanel.add(cardDeckPane,constraints15);
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
		
		/**
		* Handles the combo box listener for deck selection and displays the deck type as a string
		*/
		deckType.addActionListener(new ActionListener () {
		   public void actionPerformed(ActionEvent e) {
			isTabEditedByUser = true;   
		    JComboBox combo = (JComboBox)e.getSource();
		                String selection = (String)combo.getSelectedItem();
		                if(selection.contentEquals("Other")) 
		                {
		                // Replace this with button contents
		                deckOverview.setText("0, 0.5, 1, 2, 3, 5, 8, 13, 20 40, 100, ??");
		                } 
		                
		                else if(selection.contentEquals("Fibonacci")) {
		                // Replace this with button contents
		                deckOverview.setText("1, 1, 2, 3, 5, 8, 13, 21");
		                }
		                else if(selection.contentEquals("No Deck")){
		                	deckOverview.setText("User will be able to enter their own estimation");
		                }
		   }
		});

//		JPanel panel_20 = new JPanel();
//		panel_15.add(panel_20);
//
//
//
//		textField_1 = new JTextField();
//		panel_21.add(textField_1);
//		textField_1.setColumns(10);
//
//		JPanel panel_22 = new JPanel();
//		panel_15.add(panel_22);
//
//		JPanel panel_23 = new JPanel();
//		panel_15.add(panel_23);

		JPanel requirementsPanel = new JPanel();
		requirementsPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		add(requirementsPanel, BorderLayout.CENTER);
		requirementsPanel.setLayout(new BorderLayout(0, 0));

		JPanel requirementsHeader = new JPanel();
		requirementsHeader.setBorder(new LineBorder(Color.DARK_GRAY));
		requirementsPanel.add(requirementsHeader, BorderLayout.NORTH);

		JLabel lblChooseRequirementsTo = new JLabel("Choose requirements to estimate");
		lblChooseRequirementsTo.setFont(new Font("Tahoma", Font.PLAIN, 13));
		requirementsHeader.add(lblChooseRequirementsTo);

		JPanel requirementsSelector = new JPanel();
		requirementsPanel.add(requirementsSelector, BorderLayout.CENTER);
		requirementsSelector.setLayout(new GridLayout(1, 3, 3, 10));

		JPanel projectRequirements = new JPanel();
		projectRequirements.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsSelector.add(projectRequirements);
		projectRequirements.setLayout(new BorderLayout(0, 0));

		JPanel projectHeader = new JPanel();
		projectHeader.setBorder(new LineBorder(Color.LIGHT_GRAY));
		projectRequirements.add(projectHeader, BorderLayout.NORTH);

		JLabel lblAllProjectRequirements = new JLabel("All project requirements");
		projectHeader.add(lblAllProjectRequirements);

		JPanel projectList = new JPanel();
		projectList.setBorder(new LineBorder(Color.LIGHT_GRAY));
		projectRequirements.add(projectList, BorderLayout.CENTER);
		projectList.setLayout(new BorderLayout(0, 0));


		projectList.add(allRequirements);

		JPanel addRemPanel = new JPanel();
		addRemPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsSelector.add(addRemPanel);
		addRemPanel.setLayout(new GridLayout(3, 1, 3, 3));

		JPanel topSpacer = new JPanel();
		addRemPanel.add(topSpacer);

		JPanel buttonsPanel = new JPanel();
		addRemPanel.add(buttonsPanel);
		buttonsPanel.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel topButton = new JPanel();
		buttonsPanel.add(topButton);
		topButton.setLayout(new BorderLayout(0, 0));

		JButton btn_addToGame = new JButton("Add to game -->");
		btn_addToGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isTabEditedByUser = true;
			}
		});
		topButton.add(btn_addToGame, BorderLayout.CENTER);

		JPanel bottomButton = new JPanel();
		buttonsPanel.add(bottomButton);
		bottomButton.setLayout(new BorderLayout(0, 0));

		JButton btn_removeFromGame = new JButton("<-- Remove from game");
		bottomButton.add(btn_removeFromGame, BorderLayout.CENTER);

		JPanel bottomSpacer = new JPanel();
		addRemPanel.add(bottomSpacer);

		JPanel gameRequirements = new JPanel();
		gameRequirements.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsSelector.add(gameRequirements);
		gameRequirements.setLayout(new BorderLayout(0, 0));

		JPanel gameHeader = new JPanel();
		gameHeader.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gameRequirements.add(gameHeader, BorderLayout.NORTH);

		JLabel lblRequirementsToEstimate = new JLabel("Requirements to estimate");
		gameHeader.add(lblRequirementsToEstimate);

		JPanel gameList = new JPanel();
		gameList.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gameRequirements.add(gameList, BorderLayout.CENTER);
		gameList.setLayout(new BorderLayout(0, 0));

		
		/**
		 * Listens to the session name field and
		 * disables "Create Game" button if the
		 * field is empty
		 */
		sessionName.addKeyListener(new KeyListener() {

		    @Override
		    public void keyTyped(KeyEvent arg0) {

		    }

		    @Override
		    public void keyReleased(KeyEvent arg0) {
		    	isTabEditedByUser = true;
		        String currentText = sessionName.getText();
		        if (currentText.equals("")){
		        	btnCreateGame.setEnabled(false);
		        	createGameErrorText.setText("Session needs a name");
		        }
		        else{
		        	btnCreateGame.setEnabled(true);
		        	createGameErrorText.setText("");
		        }
		    }

		    @Override
		    public void keyPressed(KeyEvent arg0) {

		    }
		});
		
		selectedRequirements.setModel(listOfRequirementsToAdd);

		GetRequirementsController.getInstance().retrieveRequirements();

		try {	// We need to sleep for the requirement request to be in
			Thread.sleep(150);
		} catch (InterruptedException e1) {
		}


		/**
		 * Adds list of current requirements in requirement model to the list that will be added to the JList
		 * that will hold the requirements to be added to the game
		 */
		final List<Requirement> requirements = RequirementModel.getInstance().getRequirements();
		// We iterate through the requirements list and add to that JList.
		for (int i = 0; i < requirements.size(); i++) {
			Requirement req = requirements.get(i);
			listOfAllRequirements.addElement(req);
		}

		allRequirements.setModel(listOfAllRequirements);

		gameList.add(selectedRequirements);

		/**
		 * Saves data entered about the game when 'Create Game' button is pressed
		 */
		btnCreateGame.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	isTabEditedByUser = false;
		    	
				enteredName = sessionName.getText();
				selectedDeckType = (String)deckType.getSelectedItem();
				GregorianCalendar startCal, endCal;
				
				//Checks to see if the user set the date to something other than default text
				if(startDateText.getText().equals("Click Calendar to set date") || endDateText.getText().equals("Click Calendar to set date")){
					System.out.println("Please enter a valid date");
				}
				else{
					String[] startDate = startDateText.getText().split("-");
					String[] endDate = endDateText.getText().split("-");
					
					Date startVal = (Date)startTime.getValue();
					Date endVal = (Date)endTime.getValue();
					
					/**
					 * Gregorian Calendars save month values starting at 0, so the months
					 * in both of the below calendar has has 1 subtracted from it, as 
					 * the values are being pulled from the text field, which does
					 * not start at zero
					 */
					startCal = new GregorianCalendar(Integer.parseInt(startDate[2]), Integer.parseInt(startDate[1]) -1, Integer.parseInt(startDate[0]), startVal.getHours(), startVal.getMinutes());
					endCal = new GregorianCalendar(Integer.parseInt(endDate[2]), Integer.parseInt(endDate[1]) -1, Integer.parseInt(endDate[0]), endVal.getHours(), endVal.getMinutes());
					
					System.out.println(startCal.toString()+"\n"+endCal.toString());
					System.out.println(enteredName);
					System.out.println(selectedDeckType);
					
					
					for(int i =0; i < listOfRequirementsToAdd.size(); i++){
						gameRequirementsList.add(listOfRequirementsToAdd.getElementAt(i));
						System.out.println("Requirement Name: " + gameRequirementsList.get(i));
					}

					
					System.out.println(gameRequirementsList.size());

					Calendar currentDate = Calendar.getInstance();
					
					if(startCal.before(endCal) && startCal.after(currentDate)){

						PlanningPokerGame game = new PlanningPokerGame(enteredName, "Default description",
								selectedDeckType, gameRequirementsList, false, false, startCal, endCal);
						AddPlanningPokerGameController.getInstance().addPlanningPokerGame(game);
						lblGameCreated.setVisible(true);
						btnCreateGame.setEnabled(false);
						Mailer m = new Mailer();
						m.addEmail("software-team6@wpi.edu");
						m.send();
					}
					else{
						// Error message when the session name is empty
						if (sessionName.getText().isEmpty()) {
							JOptionPane emptyNameErrorPanel = new JOptionPane("You must enter the session name", JOptionPane.ERROR_MESSAGE);
							JDialog errorDialog = emptyNameErrorPanel.createDialog(null); 
							errorDialog.setLocation(thisPanel.getWidth() / 2, thisPanel.getHeight() / 2);
							errorDialog.setVisible(true);
						}
						System.out.println("Start date is after the end date.");
					}
				}
		    }
		});

		
		/**
		 * Removes selected item from box of all requirements
		 * and adds it to the box of requirements that will be used in the session
		 */
		btn_addToGame.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	isTabEditedByUser = true;
		    	
		    	if(allRequirements.getSelectedIndex() >=0){

		    		System.out.println("Added " + allRequirements.getSelectedValue() + "to selected requirements");
		    		
		    		listOfRequirementsToAdd.addElement(allRequirements.getSelectedValue());
			    	selectedRequirements.setModel(listOfRequirementsToAdd);


			    	listOfAllRequirements.removeElementAt(allRequirements.getSelectedIndex());
			    	allRequirements.setModel(listOfAllRequirements);

		    	}
		    }
		});


		/**
		 * Removes selected item from box of selected requirements for session
		 * and adds it back to the total list of requirements
		 */
		btn_removeFromGame.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	isTabEditedByUser = true;
		    	if(selectedRequirements.getSelectedIndex() >= 0){
		    		
		    		System.out.println("Removed " + selectedRequirements.getSelectedValue() + "to selected requirements");
		    		listOfAllRequirements.addElement(selectedRequirements.getSelectedValue());
			    	allRequirements.setModel(listOfAllRequirements);

			    	listOfRequirementsToAdd.removeElementAt(selectedRequirements.getSelectedIndex());
			    	selectedRequirements.setModel(listOfRequirementsToAdd);
		    	}

		    }
		});
		
		btnResetGame.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	// Reset game name
		    	sessionName.setText(dateFormat.format(date));
		    	
		    	// Reset start and end date
		    	startDateText.setText(defaultCalendarText);
		    	endDateText.setText(defaultCalendarText);
		    	btnCreateGame.setEnabled(true);
	        	createGameErrorText.setText("");
		    	// Reset start and end time
		    	startTime.setEditor(new JSpinner.DateEditor(startTime, "h:mm a"));
		    	// Reset the requirements boxes
		    }
		});		
		
	}
}
