package edu.wpi.cs.wpisuitetng.modules.PlanningPoker;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

public class PlanningPoker implements IJanewayModule {

	
	List<JanewayTabModel> tabs;
	
	@Override
	public String getName() {
		return "PlanningPoker";
	}

	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}
	
	public PlanningPoker() {
	    // Initialize the list of tabs (however, this module has only one tab)
	    tabs = new ArrayList<JanewayTabModel>();

	    // Create a JPanel to hold the toolbar for the tab
	    JPanel toolbarPanel = new JPanel();
	    toolbarPanel.add(new JLabel("PlanningPoker toolbar placeholder")); // add a label with some placeholder text
	    toolbarPanel.setBorder(BorderFactory.createLineBorder(Color.blue, 2)); // add a border so you can see the panel

	    // Create a JPanel to hold the main contents of the tab
	    JPanel mainPanel = new JPanel();
	    mainPanel.add(new JLabel("PlanningPoker placeholder"));
	    mainPanel.setBorder(BorderFactory.createLineBorder(Color.green, 2));

	    // Create a tab model that contains the toolbar panel and the main content panel
	    JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarPanel, mainPanel);

	    // Add the tab to the list of tabs owned by this module
	    tabs.add(tab1);
	}

}
