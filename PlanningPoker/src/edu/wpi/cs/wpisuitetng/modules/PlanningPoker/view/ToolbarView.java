/*******************************************************************************
 * Copyright (c) 2013-2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, struct-by-lightning
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.buttons.PlanningPokerButtonsPanel;

public class ToolbarView extends DefaultToolbarView {

	PlanningPokerButtonsPanel buttonsPanel = new PlanningPokerButtonsPanel();

	public ToolbarView(boolean visible) {

		this.addGroup(buttonsPanel);

	}
}