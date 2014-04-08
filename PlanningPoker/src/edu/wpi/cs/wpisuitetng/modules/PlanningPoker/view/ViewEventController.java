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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview.RequirementsPane;

/**
 * Provides an interface for interaction with the main GUI elements
 * All actions on GUI elements should be conducted through this controller.
 * Controls different interactions with the Planning Poker tab.
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
	
	private RequirementsPane reqPane = null;
	
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
	
	public void setReqPane(RequirementsPane reqPane) {
		this.reqPane = reqPane;
	}
	
	public RequirementsPane getReqPane() {
		return this.reqPane;
	}
	
	/**
	 * 
	 * @param main The JTabPane that is the core area of the planning poker tab.
	 */
	public void setMainView(JTabbedPane main) {
		this.mainView = main;
	}
	
	/**
	 * 
	 * @param overviewPanel The overview panel next to the core area of the planning poker tab.
	 */
	
	public void setOverviewPanel(JPanel overviewPanel) {
		this.overviewPanel = overviewPanel;
	}
	
	/**
	 * 
	 * @param toolbarView The toolbar frame containing buttons controlling the planning poker session.
	 */

	public void setToolBarView(ToolbarView toolbarView) {
		this.toolbarView = toolbarView;
		toolbarView.repaint();
	}
	
	/**
	 * 
	 * @param toolbarPanel The area inside the toolbar frame containing buttons controlling the planning poker session.
	 */
	
	public void setToolbarPanel(ToolbarPanel toolbarPanel) {
		this.toolbarPanel = toolbarPanel;
	}
	
	/**
	 * 
	 * @param newGameTab The tab containing a new planning poker session.
	 */

	public void setNewGameTab(NewGameTab newGameTab) {
		this.newGameTab = newGameTab;
	}

	/**
	 * Opens up a new game tab and adds the game window to it.
	 */
	public void createNewGame() {
		NewGameTab newGameWindow = new NewGameTab();
		mainView.addTab("New Game", newGameWindow);
		mainView.setTabComponentAt(mainView.indexOfComponent(newGameWindow), new ClosableTabComponent(mainView));			
		mainView.setSelectedComponent(newGameWindow);
	}
	
	/**
	 * Retrieves all planning poker games with an HTTP request and then waits for a second.
	 */
	public void modifyGame() {
		GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {}
		
		//System.out.println(PlanningPokerGameModel.getInstance().getSize());
		//if (!ViewEventController.getInstance().getOverviewTable().getEditFlag()) {
			//ViewEventController.getInstance().createIteration();
	}
	
	/**
	 * This function creates a new Planning Poker session game and push it into the database
	 */
	public void createGame() {
			
	}
	/**
	 * This function removes the requirements in the right column
	 */
	public void removeFromGame() {
		
	}
}