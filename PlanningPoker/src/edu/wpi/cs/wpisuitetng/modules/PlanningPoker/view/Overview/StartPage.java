package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class StartPage extends JPanel {
	/**
	 * 
	 * @param infoContainer
	 *            the container class that is to include this StartPage
	 */
	public StartPage(JPanel infoContainer) {
		// creates a new panel and sets it in the infoContainer
		JPanel startPane = new JPanel();
		startPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(startPane);
		startPane.setLayout(new BorderLayout(0, 0));

		// creates a label (title to panel) and adds it to the startPane
		JPanel startTitle = new JPanel();
		startTitle.setBorder(new LineBorder(Color.LIGHT_GRAY));
		startPane.add(startTitle, BorderLayout.NORTH);

		// I heard you like labels, so I put a label inside yo label #swag
		JLabel lblGettingStarted = new JLabel("Getting Started");
		lblGettingStarted.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		startTitle.add(lblGettingStarted);

		JPanel tipsContainer = new JPanel();
		tipsContainer.setBorder(new LineBorder(Color.LIGHT_GRAY));
		startPane.add(tipsContainer, BorderLayout.NORTH);
		tipsContainer.setLayout(new BorderLayout(0, 0));

		tipsContainer
				.add(new JLabel(
						"<html>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</html>"));

	}
}
