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
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

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

		// adds the text for about planning poker with scrollbars if needed to
		// the container in aboutPane
		JPanel tipsContainer = new JPanel();
		tipsContainer.setBorder(new LineBorder(Color.LIGHT_GRAY));
		aboutPane.add(tipsContainer, BorderLayout.CENTER);
		tipsContainer.setLayout(new BorderLayout(0, 0));
		JTextPane text = new JTextPane();
		text.setEditable(false);
		String[] textBody = { "   Planning Poker is a consensus-based tool for software developers to come together"
				+ "and estimate effort of development goals for the team. This is a great tool for agile "
				+ "teams to estimate the user stories they have for a given iteration.\n\n "
				+ "   The idea behind Planning Poker is that team discusses each user story and then "
				+ "goes into the game and then each user goes into the deck and selects the card "
				+ "that represents how effort he or she thinks the task will take. This process can be "
				+ "repeated for any number of user stories in the game. \n\n"
				+ "   During the game all estimates remain private until everyone has chose his or her "
				+ "card. After all estimates are in the Planning Poker game will calculate the Mean, "
				+ "Median, Mode, Minimum, Maximum, and Standard Deviation of the game. These "
				+ "values can be used for the team to continue the discussion and come to a consensus "
				+ "of what the groups estimate is for the user story." };
		String[] textStyles = { "regular" };

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
		
		SimpleAttributeSet sa = new SimpleAttributeSet();
		StyleConstants.setAlignment(sa, StyleConstants.ALIGN_JUSTIFIED);
		doc.setParagraphAttributes(0, 0, sa, false);

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

	}
}
