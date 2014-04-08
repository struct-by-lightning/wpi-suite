package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

// CONTROLLER
//public static void newGameButtonClicked()  {
//	// TODO: Implement this from the backend.
//}
//
//public static void gameDoubleClicked(PlanningPokerGame game) {
//	// TODO: Implement this from the backend.
//}
//
//public static void addGameToTree(PlanningPokerGame game) {
//	// TODO: Implement this from the backend.
//}

import javax.swing.JComponent;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.PlanningPoker;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.MainViewController;

public class MainView {
	
	private static JComponent toolbarComponent;
	private static JComponent mainComponent;
	
	private static MainViewLayout layout;
	
	private static MainViewController controller;

	public static void activate() {
		if (MainView.toolbarComponent == null || MainView.mainComponent == null) {
			MainView.constructGUI();
		}

		PlanningPoker.updateComponents(MainView.toolbarComponent, MainView.mainComponent);
	}
	
	public static MainViewController getController() {
		return MainView.controller;
	}

	public static void constructGUI() {
		MainView.layout = new MainViewLayout();
		
		MainView.toolbarComponent = layout.getToolbarComponent();
		MainView.mainComponent = layout.getMainComponent();
		
		MainView.controller = new MainViewController(MainView.layout.getTree(),
													 MainView.layout.getNewGamePanel());
	}
}
