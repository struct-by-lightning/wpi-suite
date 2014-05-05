/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Struct-By-Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.sun.awt.AWTUtilities;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerVoteController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerVoteController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email.Mailer;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.im.InstantMessenger;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerVote;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * An instance of this class is a JPanel containing the GUI for interacting with
 * a planning poker game which is open for voting.
 * 
 * @version $Revision: 1.0 $
 * @author Sam Mailand (sfmailand)
 */
@SuppressWarnings("serial")
public class OpenGameView extends JPanel {
	private LinkedList<PlanningPokerVote> allVotes;
	private PlanningPokerVote[] realAllVotes;
	private String username;

	ArrayList<DefaultListModel<String>> usersVotedListModel;
	private int selectedRequirement = 0;

	// TODO: The transition from this screen to the overview tab appears to be
	// the only one which doesn't refresh the tree properly.

	/**
	 * This function is called as appropriate by a listener on the end game
	 * button.
	 */
	private void endGameButtonPressed() {

		// Mark the game as closed.
		game.setFinished(true);
		game.setLive(false);

		closedNotification = new Mailer(game);
		closedNotification.addEmailFromUsers(PlanningPokerUserModel
				.getInstance().getUsers());
		closedNotification.send();
		im = new InstantMessenger(game);
		im.sendAllMessages(PlanningPokerUserModel.getInstance().getUsers());

		// Update the database with the changes.
		UpdatePlanningPokerGameController.getInstance()
				.updatePlanningPokerGame(game);

		// Don't proceed until the changes to this planning poker game have
		// been
		// recognized by the database.
		while (PlanningPokerGameModel.getPlanningPokerGame(game.getGameName())
				.isLive()
				|| !PlanningPokerGameModel.getPlanningPokerGame(
						game.getGameName()).isFinished()) {
			GetPlanningPokerGamesController.getInstance()
					.retrievePlanningPokerGames();

			// wait until the request has been answered before continuing so
			// we
			// don't send literally fifty thousand requests
			while (GetPlanningPokerGamesController.waitingOnRequest) {
				continue;
			}

		}

		// Close this tab.
		MainView.getInstance().removeClosableTab();
	}

	/**
	 * This function will open up a tab in the planning poker module with a new
	 * instance of this UI for interacting with the given open-for-voting
	 * planning poker game.
	 * 
	 * @param game
	 *            A planning poker game which is open for voting.
	 */
	public static void open(PlanningPokerGame game) {
		final OpenGameView view = new OpenGameView(game);
		MainView.getInstance().addCloseableTab(game.getGameName(), view);
	}

	private final PlanningPokerGame game;
	private final List<Requirement> requirements;
	private Requirement currentlySelectedRequirement;
	private static PlanningPokerVote ppv;
	private JTextArea textArea;

	// JPanel subclasses for each card in this game's deck.
	private final List<PlayingCardJPanel> cards;

	// Mailer for this view
	private Mailer closedNotification;

	// instant messenger
	private InstantMessenger im;
	private RequirementVoteIconRenderer requirementListRenderer;

	/**
	 * Constructor runs NetBeans generated UI initialization code and then
	 * updates those components based on the given planning poker game.
	 * 
	 * @param game
	 *            The planning poker game whose data will be displayed on this
	 *            JPanel.
	 */
	private OpenGameView(PlanningPokerGame game) {
		this.username = ConfigManager.getConfig().getUserName();
		this.game = game;

		requirements = game.getRequirements();

		cards = new ArrayList<>();

		// Initialize all GUI components. Netbeans generated code.
		initComponents();

		// Fill components with data from the planning poker game.
		initForGame();

		/*if (!hasOpenedOnce) {
			estimateNumberLabel.setText("?");
			hasOpenedOnce = true;
		}*/

		if (!game.getDeckType().equals("No Deck")) {
			submitButton.setEnabled(false);
		}

		if (!game.getDeckType().equals("No Deck")) {
			submitButton.setEnabled(false);
		}

		initUsers();
	}

	/**
	 * 
	 * @return A Planning Poker Game of this View
	 */
	public PlanningPokerGame getGame() {
		return game;
	}

	/**
	 * Populates the allCardsPanel with cards
	 */
	private void populateWithCards() {
		// Add JPanels for each card available in this game.
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 15, 0, 15);
		for (final Integer cardValue : game.getDeckValues()) {
			PlayingCardJPanel card = new PlayingCardJPanel(
					cardValue.intValue(), false);
			cards.add(card);
			allCardsPanel.add(card, gridBagConstraints);
			card.repaint();
			card.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent evt) {
					PlayingCardJPanel clickedCard = (PlayingCardJPanel) evt
							.getSource();
					clickedCard.toggle();
					updateEstimateTotal();// Vote value

					// Requirement ID
					// @TODO: Get selected requirement ID
					int requirementID = requirements.get(
							requirementList.getSelectedIndex()).getId();

					// Game name
					String gameName = game.getGameName();

					// User name
					String userName = ConfigManager.getConfig().getUserName();

					// Vote
					if (estimateNumberLabel.getText().equals("?")) {
						submitButton.setEnabled(false);
					} else {
						submitButton.setEnabled(true);
						ppv = new PlanningPokerVote(gameName, userName, Integer
								.parseInt(estimateNumberLabel.getText()),
								requirementID);
					}
				}
			});
		}
	}

	/**
	 * Create a list of lists of users that have voted for each requirement
	 */
	private void initUsers() {
		usersVotedListModel = new ArrayList<DefaultListModel<String>>(requirements.size());
		for (int i = 0; i < requirements.size(); i++) {
			usersVotedListModel.add(new DefaultListModel<String>());
			
			Requirement r = requirements.get(i);
			System.err.println("Adding requirement " + r.getName() + " at index " + i);

			for (PlanningPokerVote v : realAllVotes) {
				System.err.println("Vote for requirement " + v.getRequirementID() + " in " + v.getGameName());
				System.err.println("Real Game Name: " + game.getGameName());
				System.err.println("Real Req ID: " +r.getId());
				if (v.getGameName().equals(game.getGameName())
						&& v.getRequirementID() == r.getId()) {
					DefaultListModel<String> temp = usersVotedListModel.get(i);
					if (!temp.contains(v.getUserName()))
						temp.addElement(v.getUserName());
					usersVotedListModel.set(i, temp);
					System.err.println("Added " + v.getUserName());
				}
			}
		}
	}

	/**
	 * Adds a username to the list of usernames who have voted on the
	 * requirement with the given index
	 * 
	 * @param username
	 *            the username of the user who has voted
	 * @param selectedIndex
	 *            the index of the requirement
	 */
	private void addUserName(String username, int selectedIndex) {
		DefaultListModel<String> temp = usersVotedListModel.get(selectedIndex);
		if (!temp.contains(username))
			temp.addElement(username);
		usersVotedListModel.set(selectedIndex, temp);
		refreshUsers(selectedIndex);
	}

	/**
	 * Switches the scroll pane to display the votes for the selected
	 * requirements.
	 * 
	 * @param selectedIndex
	 *            the index of the requirement
	 */
	private void refreshUsers(int selectedIndex) {
		System.err.println("Switching to userList " + selectedIndex);
		alreadyVotedList.clearSelection();
		alreadyVotedList.setModel(usersVotedListModel.get(selectedIndex));
	}

	/**
	 * Populates the allCardsPanel with one card and JTextArea for the user to
	 * enter the estimation manually
	 */
	private void populateWithNoCardDeckPanel() {
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 15, 0, 15);

		textArea = new JTextArea();
		textArea.setDocument(new LimitedDocument(textArea));
		textArea.getDocument().addDocumentListener(new MyDocumentListener());
		final Font bigFont = new Font(null, Font.PLAIN, 32);
		textArea.setFont(bigFont);

		textArea.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		textArea.setColumns(2);
		textArea.setRows(1);
		
		JPanel card = new CardImgPanel();
		
		card.setPreferredSize(new Dimension(120, 170));
		card.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		card.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		card.add(textArea, gbc);
		
		card.repaint();
		allCardsPanel.add(card, gridBagConstraints);
	}

	/**
	 * This class implements the document listener used for the text field that
	 * allows users to manually enter their own estimates.
	 * 
	 * @version $Revision: 1.0 $
	 * @author Benjamin
	 * @author Batyr
	 */
	class MyDocumentListener implements DocumentListener {
		public void insertUpdate(DocumentEvent e) {

			estimateNumberLabel.setText(textArea.getText());
			// Requirement ID
			// @TODO: Get selected requirement ID
			final int requirementID = requirements.get(
					requirementList.getSelectedIndex()).getId();

			// Game name
			final String gameName = game.getGameName();

			// User name
			final String userName = ConfigManager.getConfig().getUserName();

			ppv = new PlanningPokerVote(gameName, userName,
					Integer.parseInt(estimateNumberLabel.getText()),
					requirementID);

			submitButton.setEnabled(true);
		}

		public void removeUpdate(DocumentEvent e) {
			final String tempString = textArea.getText();

			estimateNumberLabel.setText(tempString);

			if (textArea.getText().equals("")) {
				estimateNumberLabel.setText("?");
				submitButton.setEnabled(false);
			} else {
				estimateNumberLabel.setText(textArea.getText());
				// Requirement ID
				// @TODO: Get selected requirement ID
				final int requirementID = requirements.get(
						requirementList.getSelectedIndex()).getId();

				// Game name
				final String gameName = game.getGameName();

				// User name
				final String userName = ConfigManager.getConfig().getUserName();

				ppv = new PlanningPokerVote(gameName, userName,
						Integer.parseInt(estimateNumberLabel.getText()),
						requirementID);
			}

		}

		public void changedUpdate(DocumentEvent e) {
			System.out.println("Changed");
		}
	}

	/**
	 * This class implements the plain document used to prevent the user from
	 * inputing letters and copy-pasting into the manual estimation text field.
	 * 
	 * @version $Revision: 1.0 $
	 * @author Benjamin
	 * @author Batyr
	 */
	class LimitedDocument extends PlainDocument {
		private final int MAX_LENGTH = 3;
		private final JTextArea field;

		/**
		 * This constructor creates the LimitedDocument
		 * 
		 * @param input
		 *            Sets the text area field
		 */
		LimitedDocument(JTextArea input) {
			field = input;
		}

		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str == null || field.getText().length() >= MAX_LENGTH
					|| !(str.matches("^[0-9]+$"))) {
				return;
			}
			super.insertString(offs, str, a);
		}
	}

	/**
	 * Initializes components based on the given planning poker game.
	 * 
	 * @param game
	 *            The planning poker game to define this view.
	 */
	private void initForGame() {
		if (game.getDeckType().equals("No Deck")) {
			populateWithNoCardDeckPanel();
		} else {
			populateWithCards();
		}

		// initialize users from server
		initComponentLogic();
		// set up the users who have voted
		initUsers();

		// Listener which updates the requirement displayed based on what is
		// selected in the tree.

		requirementList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent ev) {
				final JList list;
				list = (JList) ev.getSource();
				selectedRequirement = list.getSelectedIndex();
				final Requirement selected = requirements.get(selectedRequirement);
				currentlySelectedRequirement = selected;
				requirementNameLabel.setText(selected.getName());
				requirementDescriptionLabel.setText(selected.getDescription());
				final int vote = GetPlanningPokerVoteController.getInstance()
						.retrievePlanningPokerVote(game.getGameName(),

						ConfigManager.getConfig().getUserName(),
								selected.getId());
				final String strVote = vote > 0 ? ((Integer) vote).toString()
						: "?";

				refreshUsers(selectedRequirement);

				System.out.println("Retrieved vote: " + vote + ": " + strVote);
				estimateNumberLabel.setText(strVote);
				submitButton.setEnabled(false);
				final int voteNumber = GetPlanningPokerVoteController
						.getInstance().retrievePlanningPokerVote(
								game.getGameName(),
								ConfigManager.getConfig().getUserName(),
								requirements.get(
										requirementList.getSelectedIndex())
										.getId());
				
				if (voteNumber != Integer.MIN_VALUE) {
					estimateNumberLabel.setText("" + voteNumber);
				}
				
				if (voteNumber == -1) {
					estimateNumberLabel.setText("?");
				}

				for (PlayingCardJPanel card : cards) {
					card.deselect();
				}

				submitButton.setEnabled(true);
				submitButton.setText("Submit Vote");

				if (game.getDeckType().equals("No Deck")) {
					textArea.setText("");
				}
			}
		});

		// Populate the list with each requirement.
		final DefaultListModel<String> model = new DefaultListModel<String>();
		for (Requirement r : requirements) {
			System.out.println("r: " + r);
			model.addElement(r.getName());
		}

		// Icons for requirement List
		requirementListRenderer = new RequirementVoteIconRenderer(requirements,
				allVotes);
		requirementListRenderer.setGameName(game.getGameName());

		requirementList.setModel(model);
		requirementList.setCellRenderer(requirementListRenderer);

		requirementList.repaint();
		// Initially select the first item in the tree.
		requirementList.setSelectedIndex(0);

		// Show the name of the game.
		gameNameLabel.setText(game.getGameName());

		// Show the deadline of the game if there is one.
		if (game.hasEndDate()) {
			final SimpleDateFormat fmt = new SimpleDateFormat(
					"'Closes' MMM dd, yyyy 'at' hh:mm a");
			fmt.setCalendar(game.getEndDate());
			final String dateFormatted = fmt
					.format(game.getEndDate().getTime());
			gameDeadlineDateLabel.setText(dateFormatted);
		} else {
			gameDeadlineDateLabel.setText("No Deadline");
		}

	}

	/**
	 * Set each of the cards displayed to be either selected or not selected
	 * based on the votes for the given game and requirement.
	 * 
	 * @param game
	 *            Game whose card selection values are in question.
	 * @param selectedRequirement
	 *            Particular requirement whose card selection values are in
	 *            question.
	 */
	private void updateSelectedCards(PlanningPokerGame game,
			Requirement selectedRequirement) {

		final List<Integer> selectedIndices = game.getSelectedCardIndices(null,
				selectedRequirement);

		for (int i = 0; i < cards.size(); i++) {
			if (selectedIndices.contains(new Integer(i))) {
				cards.get(i).select();
			} else {
				cards.get(i).deselect();
			}
		}
	}

	/**
	 * Updates the display on the estimate sum counter based on the cards which
	 * are currently selected.
	 */
	private void updateEstimateTotal() {
		int total = 0;
		for (PlayingCardJPanel card : cards) {
			total += card.getValue();
		}
		if (total > 0) {
			estimateNumberLabel.setText(Integer.toString(total));
		}

		else {
			final int vote = GetPlanningPokerVoteController.getInstance()
					.retrievePlanningPokerVote(game.getGameName(),
							ConfigManager.getConfig().getUserName(),
							currentlySelectedRequirement.getId());
			final String strVote = vote > 0 ? ((Integer) vote).toString() : "?";
			estimateNumberLabel.setText(strVote);
		}

	}

	/**
	 * Fills in all dynamic data displayed by components, and adds appropriate
	 * listeners.
	 */
	private void initComponentLogic() {
		// Get and add the list of emails to the mailer
		GetPlanningPokerUserController.getInstance().retrieveUser();
		try {
			Thread.sleep(150);
		} catch (Exception e) {
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		splitPane = new javax.swing.JSplitPane();
		splitPane.setDividerLocation(200); // Make the left column wider
		leftSplitPanel = new javax.swing.JPanel();
		requirementListScrollPane = new javax.swing.JScrollPane();
		requirementList = new javax.swing.JList();
		requirementLabel = new javax.swing.JLabel();
		rightSplitPanel = new javax.swing.JPanel();
		gameTitlePanel = new javax.swing.JPanel();
		gameNameLabel = new javax.swing.JLabel();
		btnEndGame = new javax.swing.JButton();
		gameDeadlineDateLabel = new javax.swing.JLabel();
		requirementNamePanel = new javax.swing.JPanel();
		requirementNameLabel = new javax.swing.JLabel();
		requirementDescriptionScrollPane = new javax.swing.JScrollPane();
		requirementDescriptionLabel = new javax.swing.JTextArea();
		alreadyVotedPanel = new javax.swing.JPanel();
		alreadyVotedScrollPane = new javax.swing.JScrollPane();
		alreadyVotedList = new javax.swing.JList();
		estimatePanel = new javax.swing.JPanel();
		submitButton = new javax.swing.JButton();
		estimateDisplayPanel = new javax.swing.JPanel();
		estimateNumberLabel = new javax.swing.JLabel();
		cardsScrollPane = new javax.swing.JScrollPane();
		allCardsPanel = new javax.swing.JPanel();

		requirementListScrollPane.setViewportView(requirementList);

		requirementLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		requirementLabel.setText("Requirements");

		javax.swing.GroupLayout leftSplitPanelLayout = new javax.swing.GroupLayout(
				leftSplitPanel);
		leftSplitPanel.setLayout(leftSplitPanelLayout);
		leftSplitPanelLayout.setHorizontalGroup(leftSplitPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(requirementListScrollPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 91,
						Short.MAX_VALUE)
				.addComponent(requirementLabel,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		leftSplitPanelLayout
				.setVerticalGroup(leftSplitPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								leftSplitPanelLayout
										.createSequentialGroup()
										.addComponent(requirementLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												requirementListScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												667, Short.MAX_VALUE)));

		splitPane.setLeftComponent(leftSplitPanel);

		gameTitlePanel
				.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		gameNameLabel.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
		gameNameLabel.setText("jLabel2");

		btnEndGame.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
		btnEndGame.setText("End Game");

		gameDeadlineDateLabel.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
		gameDeadlineDateLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		gameDeadlineDateLabel.setText("jLabel3");

		javax.swing.GroupLayout gameTitlePanelLayout = new javax.swing.GroupLayout(
				gameTitlePanel);
		gameTitlePanel.setLayout(gameTitlePanelLayout);
		gameTitlePanelLayout
				.setHorizontalGroup(gameTitlePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								gameTitlePanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												gameNameLabel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												gameDeadlineDateLabel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												btnEndGame,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												120,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));
		gameTitlePanelLayout
				.setVerticalGroup(gameTitlePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								gameTitlePanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gameTitlePanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																gameDeadlineDateLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																gameTitlePanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				btnEndGame,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addGap(4,
																				4,
																				4))
														.addComponent(
																gameNameLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																34,
																Short.MAX_VALUE))
										.addContainerGap()));

		requirementNamePanel.setBorder(javax.swing.BorderFactory
				.createEtchedBorder());

		requirementNameLabel.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
		requirementNameLabel.setText("jLabel7");

		javax.swing.GroupLayout requirementNamePanelLayout = new javax.swing.GroupLayout(
				requirementNamePanel);
		requirementNamePanel.setLayout(requirementNamePanelLayout);
		requirementNamePanelLayout
				.setHorizontalGroup(requirementNamePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								requirementNamePanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												requirementNameLabel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addContainerGap()));
		requirementNamePanelLayout.setVerticalGroup(requirementNamePanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(requirementNameLabel,
						javax.swing.GroupLayout.PREFERRED_SIZE, 33,
						javax.swing.GroupLayout.PREFERRED_SIZE));

		requirementDescriptionLabel.setEditable(false);
		requirementDescriptionLabel.setColumns(20);
		requirementDescriptionLabel.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
		requirementDescriptionLabel.setLineWrap(true);
		requirementDescriptionLabel.setRows(5);
		requirementDescriptionLabel.setWrapStyleWord(true);
		requirementDescriptionScrollPane
				.setViewportView(requirementDescriptionLabel);

		alreadyVotedPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(
						javax.swing.BorderFactory.createEtchedBorder(),
						"Already Voted",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 0, 16))); // NOI18N

		alreadyVotedList.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
		alreadyVotedList
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		alreadyVotedScrollPane.setViewportView(alreadyVotedList);

		javax.swing.GroupLayout alreadyVotedPanelLayout = new javax.swing.GroupLayout(
				alreadyVotedPanel);
		alreadyVotedPanel.setLayout(alreadyVotedPanelLayout);
		alreadyVotedPanelLayout
				.setHorizontalGroup(alreadyVotedPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								alreadyVotedPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												alreadyVotedScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												220, Short.MAX_VALUE)
										.addContainerGap()));
		alreadyVotedPanelLayout
				.setVerticalGroup(alreadyVotedPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								alreadyVotedPanelLayout
										.createSequentialGroup()
										.addComponent(
												alreadyVotedScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												261, Short.MAX_VALUE)
										.addContainerGap()));

		estimatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				javax.swing.BorderFactory.createEtchedBorder(),
				"Your Estimate",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 0, 19))); // NOI18N

		submitButton.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
		submitButton.setText("Submit");

		estimateDisplayPanel.setBorder(new javax.swing.border.SoftBevelBorder(
				javax.swing.border.BevelBorder.RAISED));

		estimateNumberLabel.setFont(new java.awt.Font("Tahoma", 0, 50)); // NOI18N
		estimateNumberLabel
				.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateNumberLabel.setText("8");

		javax.swing.GroupLayout estimateDisplayPanelLayout = new javax.swing.GroupLayout(
				estimateDisplayPanel);
		estimateDisplayPanel.setLayout(estimateDisplayPanelLayout);
		estimateDisplayPanelLayout
				.setHorizontalGroup(estimateDisplayPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								estimateDisplayPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												estimateNumberLabel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												152, Short.MAX_VALUE)
										.addContainerGap()));
		estimateDisplayPanelLayout
				.setVerticalGroup(estimateDisplayPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								estimateDisplayPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												estimateNumberLabel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												72, Short.MAX_VALUE)
										.addContainerGap()));

		javax.swing.GroupLayout estimatePanelLayout = new javax.swing.GroupLayout(
				estimatePanel);
		estimatePanel.setLayout(estimatePanelLayout);
		estimatePanelLayout
				.setHorizontalGroup(estimatePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								estimatePanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												estimatePanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																estimateDisplayPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																submitButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		estimatePanelLayout
				.setVerticalGroup(estimatePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								estimatePanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												estimateDisplayPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												submitButton,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												48,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(140, Short.MAX_VALUE)));

		allCardsPanel.setBackground(new java.awt.Color(255, 255, 255));
		allCardsPanel.setLayout(new java.awt.GridBagLayout());
		cardsScrollPane.setViewportView(allCardsPanel);

		javax.swing.GroupLayout rightSplitPanelLayout = new javax.swing.GroupLayout(
				rightSplitPanel);
		rightSplitPanel.setLayout(rightSplitPanelLayout);
		rightSplitPanelLayout
				.setHorizontalGroup(rightSplitPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								rightSplitPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												rightSplitPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																rightSplitPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				estimatePanel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				cardsScrollPane,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				0,
																				Short.MAX_VALUE)
																		.addContainerGap())
														.addGroup(
																rightSplitPanelLayout
																		.createSequentialGroup()
																		.addGroup(
																				rightSplitPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								requirementDescriptionScrollPane,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								570,
																								Short.MAX_VALUE)
																						.addComponent(
																								requirementNamePanel,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				alreadyVotedPanel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addComponent(
																gameTitlePanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))));
		rightSplitPanelLayout
				.setVerticalGroup(rightSplitPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								rightSplitPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												gameTitlePanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												rightSplitPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																rightSplitPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				requirementNamePanel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				requirementDescriptionScrollPane))
														.addComponent(
																alreadyVotedPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												rightSplitPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																estimatePanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																cardsScrollPane,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																321,
																Short.MAX_VALUE))
										.addContainerGap()));

		splitPane.setRightComponent(rightSplitPanel);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addComponent(splitPane).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addComponent(splitPane).addContainerGap()));

		initListeners();
	}// </editor-fold>//GEN-END:initComponents

	private void initListeners() {
		allVotes = GetPlanningPokerVoteController.getInstance()
				.retrievePlanningPokerVoteByGameAndUser(game.getGameName(),
						username);
		
		realAllVotes = GetPlanningPokerVoteController.getInstance().retrievePlanningPokerVote();

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isLive() && !game.isFinished()) {

					// retrieve the games on the server to see if they have
					// changed
					GetPlanningPokerGamesController.getInstance()
							.retrievePlanningPokerGames();

					// wait for the reply
					while (GetPlanningPokerGamesController.waitingOnRequest) {
						continue;
					}

					/*
					 * If a game has a deadline, it is only closed when the
					 * first get request after the deadline has passed is sent.
					 * Send a second request so that the local game is updated
					 * if the game has a deadline
					 */
					if (game.hasEndDate() && game.isLive()
							&& new Date().after(game.getEndDate().getTime())) {
						GetPlanningPokerGamesController.getInstance()
								.retrievePlanningPokerGames();
						while (GetPlanningPokerGamesController.waitingOnRequest) {
							continue;
						}
					}

					if (PlanningPokerGameModel.getPlanningPokerGame(
							game.getGameName()).isLive()
							&& !PlanningPokerGameModel.getPlanningPokerGame(
									game.getGameName()).isFinished()) {

						AddPlanningPokerVoteController.getInstance()
								.addPlanningPokerVote(ppv);

						// List the users first
						/*
						 * List<PlanningPokerUser> userList =
						 * PlanningPokerUserModel.getInstance().getUsers();
						 * 
						 * for(PlanningPokerUser user: userList) {
						 * System.out.println("User " + user.getID()); }
						 * System.out.println("Test output");
						 */
						// Submit button disable
						submitButton.setEnabled(false);
						submitButton.setText("Submitted!");

						// Check it!
						requirementListRenderer.goCheck(requirementList
								.getSelectedIndex());
						
						// add user to the list
						addUserName(username, selectedRequirement);
					} else { // the game has been closed by someone else
						int n = 0;
						if (game.hasEndDate()
								&& new Date()
										.after(game.getEndDate().getTime())) {
							n = JOptionPane
									.showConfirmDialog(
											null,
											"The game you have selected has "
													+ "passed its deadline. Would "
													+ "you like to return to MainView?",
											"", JOptionPane.YES_NO_OPTION);
						} else { // the game was closed by the moderator
							n = JOptionPane.showConfirmDialog(null,
									"The game you have selected has "
											+ "been closed by the "
											+ "moderator. Would you "
											+ "like to return to "
											+ "MainView?", "",
									JOptionPane.YES_NO_OPTION);
						}

						if (n == 0) { // return to mainview
							MainView.getInstance().removeClosableTab();
						} else { // answered no, disable all buttons
							submitButton.setEnabled(false);
							btnEndGame.setToolTipText(null);
							btnEndGame.setEnabled(false);
						}
					}
				}

			}
		});

		/**
		 * Disables endGame button if use is not the moderator of the game
		 */
		if (!ConfigManager.getConfig().getUserName()
				.equals(game.getModerator())) {
			btnEndGame.setToolTipText(null);
			btnEndGame.setEnabled(false);
		}
		/**
		 * Action listener for end button
		 */
		btnEndGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endGameButtonPressed();
			}
		});

	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel allCardsPanel;
	private javax.swing.JPanel noCardVotingPanel;
	private javax.swing.JScrollPane cardsScrollPane;
	private javax.swing.JPanel estimateCenteringPanel;
	private javax.swing.JLabel estimateNumberLabel;
	private javax.swing.JPanel estimateNumberPanel;
	private javax.swing.JPanel estimatePanel;
	private javax.swing.JLabel estimateTitleLabel;
	private javax.swing.JPanel estimateTitlePanel;
	private javax.swing.JLabel gameDeadlineDateLabel;
	private javax.swing.JLabel gameNameLabel;
	private javax.swing.JPanel gameTitlePanel;
	private javax.swing.JLabel instructionsLabel;
	private javax.swing.JPanel leftSplitPanel;
	private javax.swing.JTextArea requirementDescriptionLabel;
	private javax.swing.JPanel requirementDescriptionLabelPanel;
	private javax.swing.JList requirementList;
	private javax.swing.JScrollPane requirementListScrollPane;
	private javax.swing.JLabel requirementNameLabel;
	private javax.swing.JPanel requirementNamePanel;
	private javax.swing.JPanel requirementPanel;
	private javax.swing.JLabel requirementsLabel;
	private javax.swing.JPanel requirementsLabelPanel;
	private javax.swing.JPanel rightBlankPanel;
	private javax.swing.JPanel rightSplitPanel;
	private javax.swing.JPanel rowSplitPanel;
	private javax.swing.JSplitPane splitPane;
	private javax.swing.JPanel topRowRequirementPanel;
	private javax.swing.JButton submitButton;
	private javax.swing.JScrollPane requirementDescriptionScrollPane;
	private JButton btnEndGame;
	private javax.swing.JPanel alreadyVotedPanel;
	private javax.swing.JPanel estimateDisplayPanel;
	private javax.swing.JScrollPane alreadyVotedScrollPane;
	private javax.swing.JList alreadyVotedList;
	private javax.swing.JLabel requirementLabel;
}
