/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.ClosableTabComponent;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.MainView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.NewGameTab;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.ToolbarView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview.OverviewPanel;

public class PlanningPoker implements IJanewayModule {
	
	/**
	 * A list of tabs owned by this module
	 */
	List<JanewayTabModel> tabs;
	JTabbedPane pokerTabs;
	NewGameTab newGameWindow;
    int nextTab = 1;
    
    ClosableTabComponent clsblTab;
    
	public PlanningPoker() {
		
		// Initialize the list of tabs (however, this module has only one tab)
	    tabs = new ArrayList<JanewayTabModel>();

	    //Create a JPanel to hold the toolbar for the tab
	    ToolbarView toolbarView = new ToolbarView();
		
	    // Create a JPanel to hold the main contents of the tab
	    JPanel overviewPanel = new OverviewPanel();
	    
	    //The inner tabs of the planning poker module in Janeway
	    pokerTabs = new MainView();

		ViewEventController.getInstance().setMainView(pokerTabs);
		ViewEventController.getInstance().setToolBarView(toolbarView);
		ViewEventController.getInstance().setOverviewPanel(overviewPanel);
	    
	    
	    //pokerTabs.setTabComponentAt(0, new ClosableTabComponent(pokerTabs));
	    /**
	     * Listens to check if the "New Game" button has been clicked and opens the 
	     * new game window in a new tab when it is. It will automatically switch to 
	     * the new tab. Currently only one new game tab can be open at at time
	     */
		
	    // Create a tab model that contains the toolbar panel and the main content panel
	    JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarView, pokerTabs);

	    // Add the tab to the list of tabs owned by this module
	    tabs.add(tab1);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "PlanningPoker";
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		// TODO Auto-generated method stub
		return tabs;
	}

}
