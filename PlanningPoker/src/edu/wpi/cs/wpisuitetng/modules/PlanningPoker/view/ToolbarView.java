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
}