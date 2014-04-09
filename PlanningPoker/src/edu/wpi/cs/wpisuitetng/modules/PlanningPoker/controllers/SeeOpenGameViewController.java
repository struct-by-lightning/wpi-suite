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
