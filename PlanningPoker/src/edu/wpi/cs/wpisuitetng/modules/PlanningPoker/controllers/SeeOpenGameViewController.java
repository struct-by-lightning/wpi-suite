/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Struct-By-Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers;

import javax.swing.JList;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.MainView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.SeeOpenGameView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class SeeOpenGameViewController {
	
	private JPanel viewGamePanel;
	private static JList<Requirement> selectedRequirement;
	public static Requirement getSelectedRequirement() {
		return selectedRequirement.getSelectedValue();
	}
	public static void setRequirementList(JList<Requirement> toSet) {
		selectedRequirement = toSet;
	}
	public SeeOpenGameViewController(JPanel viewGamePanel) {
		this.viewGamePanel = viewGamePanel;
	}
	
	public void activateView(PlanningPokerGame game) {
		SeeOpenGameView.update();
		MainView.getController().addCloseableTab(game.getGameName(), viewGamePanel);
	}
	
	public void setViewGamePanel(JPanel viewGamePanel) {
		this.viewGamePanel = viewGamePanel;
	}

}
