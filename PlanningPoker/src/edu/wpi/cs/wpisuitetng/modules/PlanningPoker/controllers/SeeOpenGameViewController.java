package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.MainView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.SeeOpenGameView;

public class SeeOpenGameViewController {
	
	private JPanel viewGamePanel;
	
	public SeeOpenGameViewController(JPanel viewGamePanel) {
		this.viewGamePanel = viewGamePanel;
	}
	
	public void activateView(PlanningPokerGame game) {
		MainView.getController().addCloseableTab(game.getGameName(), viewGamePanel);
	}

}
