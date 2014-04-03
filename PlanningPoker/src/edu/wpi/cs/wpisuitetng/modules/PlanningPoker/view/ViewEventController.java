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
			
	}
	/**
	 * This function removes the requirements in the right column
	 */
	public void removeFromGame() {
		
	}
}