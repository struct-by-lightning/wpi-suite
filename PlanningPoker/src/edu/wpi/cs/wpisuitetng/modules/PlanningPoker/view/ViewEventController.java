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

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.MainView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ToolbarView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.iterations.IterationOverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.iterations.IterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.overview.OverviewTable;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.overview.OverviewTreePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.requirements.RequirementPanel;

/**
 * Provides an interface for interaction with the main GUI elements
 * All actions on GUI elements should be conducted through this controller.
 * @version $Revision: 1.0 $
 * @author lhnguyenduc (hlong290494)
 */

public class ViewEventController {
	private static ViewEventController instance = null;
	private MainView main = null;
	private ToolbarView toolbar = null;
	private NewGameTab newGameTab = null;
	private StoriesBoardPanel stories = null;
	private ArrayList<RequirementPanel> listOfEditingPanels = new ArrayList<RequirementPanel>();
	private ArrayList<IterationPanel> listOfIterationPanels = new ArrayList<IterationPanel>();
	private IterationOverviewPanel iterationOverview;
}