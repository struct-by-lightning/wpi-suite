package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

public class AboutPage extends JPanel {
	/**
	 * 
	 * @param infoContainer
	 *            the container class that is to include this StartPage
	 */
	public AboutPage(JPanel infoContainer) {
		// creates a new panel and sets it in the infoContainer
		JPanel aboutPane = new JPanel();
		aboutPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(aboutPane);
		aboutPane.setLayout(new BorderLayout(0, 0));

		// creates a label (title to panel) and adds it to the aboutPane
		JPanel aboutTitle = new JPanel();
		aboutTitle.setBorder(new LineBorder(Color.LIGHT_GRAY));
		aboutPane.add(aboutTitle, BorderLayout.NORTH);

		// I heard you like labels, so I put a label inside yo label #swag
		JLabel lblAboutPoker = new JLabel("About Planning Poker");
		lblAboutPoker.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		aboutTitle.add(lblAboutPoker);
		
		//adds the text for about planning poker with scrollbars if needed to the container in aboutPane
		JPanel tipsContainer = new JPanel();
		tipsContainer.setBorder(new LineBorder(Color.LIGHT_GRAY));
		aboutPane.add(tipsContainer, BorderLayout.CENTER);
		tipsContainer.setLayout(new BorderLayout(0, 0));
		JTextPane text = new JTextPane();
		text.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		JScrollPane jsp = new JScrollPane(text);
		tipsContainer.add(jsp);

	}
}
