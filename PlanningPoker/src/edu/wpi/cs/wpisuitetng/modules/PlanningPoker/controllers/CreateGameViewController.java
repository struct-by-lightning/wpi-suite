package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.CreateGameView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.MainView;

public class CreateGameViewController {

	private CreateGameView view;

	public CreateGameViewController(CreateGameView view) {

		this.view = view;

	}

	public void activateView() {
		MainView.getController().addCloseableTab("Create Game", this.view.newCreateGamePanel());
	}

}
