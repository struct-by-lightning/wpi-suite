/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import javax.swing.JToolBar;

/**
 * This is the toolbar for the PostBoard module
 * 
 * 
 * @author Batyrlan Nurbekov
 * @author Sam Mailand
 * @author Zachary Zapatka
 * @author Miguel Mora
 * 
 */
@SuppressWarnings("serial")
public class ToolbarView extends JToolBar {

    /** The panel containing toolbar buttons */
    private final ToolbarPanel toolbarPanel;

    /**
     * Construct this view and all components in it.
     */
    public ToolbarView() {
        // Prevent this toolbar from being moved
        setFloatable(false);

        // Add the panel containing the toolbar buttons
        toolbarPanel = new ToolbarPanel();
        add(toolbarPanel);
    }
    
    
    public ToolbarPanel getToolBarPanel(){
    	return toolbarPanel;
    }
}