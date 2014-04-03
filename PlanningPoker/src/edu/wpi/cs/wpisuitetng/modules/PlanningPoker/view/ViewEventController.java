/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Struct By Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.janeway.email.Mailer;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;

/**
 * Provides an interface for interaction with the main GUI elements
 * All actions on GUI elements should be conducted through this controller.
 * @version $Revision: 1.0 $
 * @author lhnguyenduc (hlong290494)
 */

public class ViewEventController {
	private static ViewEventController instance = null;
	private JTabbedPane mainView = null;
	private ToolbarView toolbarView = null;
	private NewGameTab newGameTab = null;
	private ToolbarPanel toolbarPanel = null;
	private JPanel overviewPanel = null;
	
	/**
	 * Default constructor for ViewEventController.  Is protected to prevent instantiation.
	 */
	private ViewEventController() {}
	
	/**
	 * Returns the singleton instance of the vieweventcontroller.
	 * @return The instance of this controller. 
	 * */

	
	public static ViewEventController getInstance() {
		if (instance == null) {
			instance = new ViewEventController();
		}
		return instance;
	}
	/*
	 *  Setters
	 */
	
	public void setMainView(JTabbedPane main) {
		this.mainView = main;
	}
	
	public void setOverviewPanel(JPanel overviewPanel) {
		this.overviewPanel = overviewPanel;
	}

	public void setToolBarView(ToolbarView toolbarView) {
		this.toolbarView = toolbarView;
		toolbarView.repaint();
	}
	
	public void setToolbarPanel(ToolbarPanel toolbarPanel) {
		this.toolbarPanel = toolbarPanel;
	}

	public void setNewGameTab(NewGameTab newGameTab) {
		this.newGameTab = newGameTab;
	}

	
	public void createNewGame() {
		NewGameTab newGameWindow = new NewGameTab();
		mainView.addTab("New Game", newGameWindow);
		mainView.setTabComponentAt(mainView.indexOfComponent(newGameWindow), new ClosableTabComponent(mainView));			
		mainView.setSelectedComponent(newGameWindow);
	}
	
	public void modifyGame() {
		GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {}
		
		System.out.println(PlanningPokerGameModel.getInstance().getSize());
		//if (!ViewEventController.getInstance().getOverviewTable().getEditFlag()) {
			//ViewEventController.getInstance().createIteration();
	}
	
	/**
	 * This function create a new Planning Poker session game and push it into the database
	 */
	public void createGame() {
			newGameTab.enteredName = newGameTab.sessionName.getText();
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
				
				for(int i =0; i < gameRequirementsModel.getSize(); i++){
					for(int j = 0; j < requirements.size(); j++){
						if((gameRequirementsModel.get(i).toString()).equals(requirements.get(j).toString())){
							System.out.println(requirements.get(j).toString());
							savedRequirements.add(requirements.get(j));
							
						}
					}
				}
				System.out.println(savedRequirements.size());
				
				
				Calendar currentDate = Calendar.getInstance();
				
				if(startCal.before(endCal) && startCal.after(currentDate)){
					PlanningPokerGame game = new PlanningPokerGame(enteredName, "Default description",
							selectedDeckType, savedRequirements, false, false, startCal, endCal);
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
}