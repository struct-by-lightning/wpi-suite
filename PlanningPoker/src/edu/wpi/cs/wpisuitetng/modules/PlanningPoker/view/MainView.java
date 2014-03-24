package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import javax.swing.JPanel;

/**
 * This panel fills the main content area of the tab for this module. It
 * contains one inner JPanel, the BoardPanel.
 * 
 * @author Batyrlan Nurbekov
 *
 */
@SuppressWarnings("serial")
public class MainView extends JPanel {

    /** The panel containing the post board */
    private final StoriesBoardPanel boardPanel;

    /**
     * Construct the panel.
     */
    public MainView() {
        // Add the board panel to this view
        boardPanel = new StoriesBoardPanel();
        add(boardPanel);
    }
}