package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

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
		lblGettingStarted.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		startTitle.add(lblGettingStarted);

		// adds the text for getting started with scrollbars if needed to the
		// container in startPane
		JPanel tipsContainer = new JPanel();
		tipsContainer.setBorder(new LineBorder(Color.LIGHT_GRAY));
		startPane.add(tipsContainer, BorderLayout.CENTER);
		tipsContainer.setLayout(new BorderLayout(0, 0));
		JTextPane text = new JTextPane();
		text.setEditable(false);
		String[] textBody = {
				"Create New Game: ",
				"This will open the window to create a new game where you can "
						+ "choose the user requirements for the game. \n\n",
				"New folder: ",
				"These games are created but have not been started yet. If you click one "
						+ "of the games in this folder you if you are the moderator you can start the game.\n\n",
				"Open folder: ",
				"These games have been created and started you can estimate each "
						+ "user story. After the user story is estimated it will be marked as completed.\n\n",
				"Closed folder: ",
				"These are closed games. By clicking on the games in this folder you "
						+ "will get the results from this game. If you are the moderator of this game then you "
						+ "should be able to edit results. \n\n",
				"If you are looking for further information refer to Help." };
		String[] textStyles = { "bold", "regular", "bold", "regular", "bold",
				"regular", "bold", "regular", "italic" };
		StyledDocument doc = text.getStyledDocument();
		addStylesToDocument(doc);

		try {
			for (int i = 0; i < textBody.length; i++) {
				doc.insertString(doc.getLength(), textBody[i],
						doc.getStyle(textStyles[i]));
			}
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}
		JScrollPane jsp = new JScrollPane(text);

		tipsContainer.add(jsp);
	}

	protected void addStylesToDocument(StyledDocument doc) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");
		StyleConstants.setFontSize(def, 16);
		
		SimpleAttributeSet sa = new SimpleAttributeSet();
		StyleConstants.setAlignment(sa, StyleConstants.ALIGN_JUSTIFIED);
		doc.setParagraphAttributes(0, 0, sa, false);

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

	}
}
