/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.janeway.gui.container;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.janeway.interfaces.ContactChecker;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

/**
 * The main application panel. Holds each modules' tabs, each of which contain a
 * toolbar and main component.
 */
@SuppressWarnings("serial")
public class TabPanel extends JPanel {

	/** The tab pane */
	protected JTabbedPane tabbedPane;
	protected List<IJanewayModule> modules;

	// This is an instance of an interface which Janeway needs to call in
	// Planning Poker, because Janeway can't directly import Planning Poker code
	// because that would create dependency circles.
	public volatile static ContactChecker contactChecker;

	public static ContactChecker getContactChecker() {
		return TabPanel.contactChecker;
	}

	public static void setContactChecker(ContactChecker checker) {
		TabPanel.contactChecker = checker;
	}

	public TabPanel(List<IJanewayModule> modules) {
		this.modules = modules;
		this.tabbedPane = new JTabbedPane();

		// Populate tabs
		addModules();

		// Set the layout and add the tabs to the panel
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		// Listener which fires when Janeway switches to the Planning Poker tab
		// and determines whether Planning Poker will display a contact
		// information prompt or the regular planning poker interface.
		this.tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				JTabbedPane source = (JTabbedPane) e.getSource();
				String tabName = source.getSelectedComponent().getName();

				if (tabName.equals("PlanningPoker")) {
					TabPanel.getContactChecker().verifyContactInfo();
				}

			}
		});
	}

	/**
	 * Changes the selected tab to the tab left of the current tab
	 */
	public void switchToLeftTab() {
		if (tabbedPane.getSelectedIndex() > 0) {
			switchToTab(tabbedPane.getSelectedIndex() - 1);
		}
	}

	/**
	 * Changes the selected tab to the tab right of the current tab
	 */
	public void switchToRightTab() {
		if (tabbedPane.getSelectedIndex() < tabbedPane.getTabCount() - 1) {
			switchToTab(tabbedPane.getSelectedIndex() + 1);
		}
	}

	/**
	 * Return the JTabbedPane holding the module tabs
	 * 
	 * @return
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * Changes the selected tab to the tab with the given index
	 * 
	 * @param tabIndex
	 *            the index of the tab to select
	 */
	private void switchToTab(int tabIndex) {
		try {
			tabbedPane.setSelectedIndex(tabIndex);
		} catch (IndexOutOfBoundsException e) {
			// an invalid tab was requested, do nothing
		}
	}

	/**
	 * Called by the constructor to add a tab for each module
	 */
	private void addModules() {
		for (IJanewayModule ijm : modules) {
			for (JanewayTabModel jtm : ijm.getTabs()) {
				// Create a panel to hold the buttons and tab contents
				JPanel newPanel = new JPanel();
				SpringLayout newPanelLayout = new SpringLayout();
				newPanel.setLayout(newPanelLayout);

				// Constrain the toolbar
				newPanelLayout.putConstraint(SpringLayout.NORTH,
						jtm.getToolbar(), 0, SpringLayout.NORTH, newPanel);
				newPanelLayout.putConstraint(SpringLayout.WEST,
						jtm.getToolbar(), 0, SpringLayout.WEST, newPanel);
				newPanelLayout.putConstraint(SpringLayout.EAST,
						jtm.getToolbar(), 0, SpringLayout.EAST, newPanel);
				newPanelLayout.putConstraint(SpringLayout.SOUTH,
						jtm.getToolbar(), 100, SpringLayout.NORTH,
						jtm.getToolbar());

				// Constrain the main component
				newPanelLayout.putConstraint(SpringLayout.NORTH,
						jtm.getMainComponent(), 0, SpringLayout.SOUTH,
						jtm.getToolbar());
				newPanelLayout.putConstraint(SpringLayout.WEST,
						jtm.getMainComponent(), 0, SpringLayout.WEST, newPanel);
				newPanelLayout.putConstraint(SpringLayout.EAST,
						jtm.getMainComponent(), 0, SpringLayout.EAST, newPanel);
				newPanelLayout
						.putConstraint(SpringLayout.SOUTH,
								jtm.getMainComponent(), 0, SpringLayout.SOUTH,
								newPanel);

				// Add the toolbar and main component to the new panel
				newPanel.add(jtm.getToolbar());
				newPanel.add(jtm.getMainComponent());
				newPanel.setName(jtm.getName());

				// Add a tab to the tabbed pane containing the new panel
				tabbedPane.addTab(jtm.getName(), jtm.getIcon(), newPanel);
			}
		}
	}
}
