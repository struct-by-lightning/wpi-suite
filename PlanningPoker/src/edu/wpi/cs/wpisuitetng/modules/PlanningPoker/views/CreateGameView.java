package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import javax.swing.JComponent;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.PlanningPoker;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.CreateGameViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.MainViewController;

public class CreateGameView {
	
	private static JComponent toolbarComponent;
	private static JComponent mainComponent;
	
	private static CreateGameViewLayout layout;
	private static CreateGameViewController controller;

	public static void activate() {
		if (CreateGameView.toolbarComponent == null || CreateGameView.mainComponent == null) {
			CreateGameView.constructGUI();
		}

		
		PlanningPoker.updateComponents(CreateGameView.toolbarComponent, CreateGameView.mainComponent);
	}
	
	public static CreateGameViewController getController() {
		return CreateGameView.controller;
	}

	public static void constructGUI() {
		CreateGameView.layout = new CreateGameViewLayout();
		
		CreateGameView.toolbarComponent = layout.getToolbarComponent();
		CreateGameView.mainComponent = layout.getMainComponent();
		
		CreateGameView.controller = new CreateGameViewController();
	}
}
