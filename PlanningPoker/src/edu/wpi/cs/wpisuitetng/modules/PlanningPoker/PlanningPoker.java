/*******************************************************************************
 * Copyright (c) 2013-2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, struct-by-lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker;

import java.util.ArrayList;
/**
 * Main class that adds the Planning Poker tab to Janeway and activates it.
 * 
 */
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.MainView;

/**
 * An instance of this class is what Janeway requires to display the planning
 * poker module as a tab.
 */
public class PlanningPoker implements IJanewayModule {

	/**
	 * The list of tabs to create for the planning poker module. There is only
	 * one tab to create for the planning poker module.
	 */
	private List<JanewayTabModel> tabs;

	/**
	 * The gui component to display in the toolbar area for the planning poker
	 * module.
	 */
	private JComponent toolbarComponent;

	/**
	 * The gui component to display in the main area for the planning poker
	 * module.
	 */
	private JComponent mainComponent;

	/**
	 * Constructor adds a planning poker tab with the necessary gui components.
	 */
	public PlanningPoker() {

		// Initialize the list of tabs (however, this module has only one tab).
		tabs = new ArrayList<JanewayTabModel>();

		// Initialize the gui component for the toolbar area
		this.toolbarComponent = MainView.getInstance().getToolbarComponent();
		
		// Initialize the gui component for the main area
		this.mainComponent = MainView.getInstance().getMainComponent();

		// Create a tab model that contains the toolbar component and the main
		// component
		JanewayTabModel tab = new JanewayTabModel(this.getName(), new ImageIcon(),
				this.toolbarComponent, this.mainComponent);

		// Add the tab to the list of tabs owned by this module
		tabs.add(tab);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "PlanningPoker";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}
