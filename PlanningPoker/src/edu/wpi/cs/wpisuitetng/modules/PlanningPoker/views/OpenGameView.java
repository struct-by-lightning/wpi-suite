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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

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
 * @author Austin Rose (atrose)
 */
@SuppressWarnings("serial")
public class OpenGameView extends JPanel {
	private LinkedList<PlanningPokerVote> allVotes;
	private String username;

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

		// Don't proceed until the changes to this planning poker game have been
		// recognized by the database.
		while (PlanningPokerGameModel.getPlanningPokerGame(game.getGameName())
				.isLive()
				|| !PlanningPokerGameModel.getPlanningPokerGame(
						game.getGameName()).isFinished()) {
			GetPlanningPokerGamesController.getInstance()
			.retrievePlanningPokerGames();
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
	private static boolean hasOpenedOnce = false;

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

		// Long: I do not know why you need to do this?
		/*if (!hasOpenedOnce) {
			estimateNumberLabel.setText("?");
			hasOpenedOnce = true;
		}*/

		if (!game.getDeckType().equals("No Deck")) {
			submitButton.setEnabled(false);
		}
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
					}
					else {
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

		textArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		textArea.setColumns(2);
		textArea.setRows(1);

		allCardsPanel.add(textArea, gridBagConstraints);

		allCardsPanel.setBackground(new Color(232, 232, 232));
	}

	/** This class implements the document listener used for the text field that allows
	 * users to manually enter their own estimates.
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

	/** This class implements the plain document used to prevent the user from inputing
	 * letters and copy-pasting into the manual estimation text field.
	 * 
	 * @version $Revision: 1.0 $
	 * @author Benjamin
	 * @author Batyr
	 */
	class LimitedDocument extends PlainDocument {
		private final int MAX_LENGTH = 3;
		private final JTextArea field;

		/** This constructor creates the LimitedDocument 
		 * 
		 * @param input Sets the text area field
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

		// Listener which updates the requirement displayed based on what is
		// selected in the tree.

		requirementList
		.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent ev) {
				final JList list;
				list = (JList) ev.getSource();
				final Requirement selected = requirements.get(list
						.getSelectedIndex());
				currentlySelectedRequirement = selected;
				requirementNameLabel.setText(selected.getName());
				requirementDescriptionLabel.setText(selected
						.getDescription());
				final int vote = GetPlanningPokerVoteController
						.getInstance()
						.retrievePlanningPokerVote(
								game.getGameName(),
								ConfigManager.getConfig().getUserName(),
								selected.getId());
				final String strVote = vote > 0 ? ((Integer) vote).toString()
						: "?";
				System.out.println("Retrieved vote: " + vote + ": "
						+ strVote);
				estimateNumberLabel.setText(strVote);
				submitButton.setEnabled(false);
				final int voteNumber = GetPlanningPokerVoteController
						.getInstance()
						.retrievePlanningPokerVote(
								game.getGameName(),
								ConfigManager.getConfig().getUserName(),
								requirements.get(
										requirementList
										.getSelectedIndex())
										.getId());

				if (voteNumber != Integer.MIN_VALUE) {
					estimateNumberLabel.setText("" + voteNumber);
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
		requirementListRenderer = new RequirementVoteIconRenderer(requirements, allVotes);
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
			final SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
			fmt.setCalendar(game.getEndDate());
			final String dateFormatted = fmt.format(game.getEndDate().getTime());
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
		allVotes = GetPlanningPokerVoteController.getInstance().retrievePlanningPokerVoteByGameAndUser(game.getGameName(), username);
		final java.awt.GridBagConstraints gridBagConstraints;

		splitPane = new javax.swing.JSplitPane();
		leftSplitPanel = new javax.swing.JPanel();
		requirementsLabelPanel = new javax.swing.JPanel();
		requirementsLabel = new javax.swing.JLabel();
		requirementListScrollPane = new javax.swing.JScrollPane();
		requirementList = new javax.swing.JList();
		rightSplitPanel = new javax.swing.JPanel();
		gameTitlePanel = new javax.swing.JPanel();
		gameNameLabel = new javax.swing.JLabel();
		gameDeadlineDateLabel = new javax.swing.JLabel();
		rowSplitPanel = new javax.swing.JPanel();
		topRowRequirementPanel = new javax.swing.JPanel();
		requirementPanel = new javax.swing.JPanel();
		requirementNamePanel = new javax.swing.JPanel();
		requirementNameLabel = new javax.swing.JLabel();
		requirementDescriptionLabelPanel = new javax.swing.JPanel();
		requirementDescriptionLabel = new javax.swing.JTextArea();
		rightBlankPanel = new javax.swing.JPanel();
		instructionsLabel = new javax.swing.JLabel();
		estimateCenteringPanel = new javax.swing.JPanel();
		estimatePanel = new javax.swing.JPanel();
		estimateTitlePanel = new javax.swing.JPanel();
		estimateTitleLabel = new javax.swing.JLabel();
		estimateNumberPanel = new javax.swing.JPanel();
		estimateNumberLabel = new javax.swing.JLabel();
		cardsScrollPane = new javax.swing.JScrollPane();
		cardsScrollPane.setBackground(Color.gray);
		allCardsPanel = new javax.swing.JPanel();
		allCardsPanel.setBackground(Color.gray);

		submitButton = new javax.swing.JButton();

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isLive() && !game.isFinished()) {
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
					requirementListRenderer.goCheck(requirementList.getSelectedIndex());
				}

			}
		});

		submitButton.setText("Submit Vote");

		setLayout(new java.awt.BorderLayout());

		requirementsLabelPanel.setBorder(null);

		requirementsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementsLabel
		.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		requirementsLabel.setText("Requirements");

		final javax.swing.GroupLayout requirementsLabelPanelLayout = new javax.swing.GroupLayout(
				requirementsLabelPanel);
		requirementsLabelPanel.setLayout(requirementsLabelPanelLayout);
		requirementsLabelPanelLayout
		.setHorizontalGroup(requirementsLabelPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								requirementsLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(
										requirementsLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
										.addContainerGap()));
		requirementsLabelPanelLayout
		.setVerticalGroup(requirementsLabelPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								requirementsLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(requirementsLabel)
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		requirementListScrollPane.setViewportView(requirementList);

		final javax.swing.GroupLayout leftSplitPanelLayout = new javax.swing.GroupLayout(
				leftSplitPanel);
		leftSplitPanel.setLayout(leftSplitPanelLayout);
		leftSplitPanelLayout
		.setHorizontalGroup(leftSplitPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								leftSplitPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										leftSplitPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														requirementsLabelPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addComponent(
																requirementListScrollPane,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																0,
																Short.MAX_VALUE))
																.addContainerGap()));
		leftSplitPanelLayout
		.setVerticalGroup(leftSplitPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								leftSplitPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(
										requirementsLabelPanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(
														requirementListScrollPane,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														624, Short.MAX_VALUE)
														.addContainerGap()));

		splitPane.setLeftComponent(leftSplitPanel);

		gameTitlePanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		gameNameLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		gameNameLabel.setText("game.getGameName()");

		gameDeadlineDateLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		gameDeadlineDateLabel.setText("game.getDeadlineDate()");

		/**
		 * Button for ending a game
		 */
		btnEndGame = new JButton("End Game");

		/**
		 * Disables endGame button if use is not the moderator of the game
		 */
		if (!ConfigManager.getConfig().getUserName()
				.equals(game.getModerator())) {
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

		final javax.swing.GroupLayout gameTitlePanelLayout = new javax.swing.GroupLayout(
				gameTitlePanel);
		gameTitlePanelLayout.setHorizontalGroup(gameTitlePanelLayout
				.createParallelGroup(Alignment.TRAILING).addGroup(
						gameTitlePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(gameNameLabel,
								GroupLayout.DEFAULT_SIZE, 341,
								Short.MAX_VALUE)
								.addGap(18)
								.addComponent(btnEndGame)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(gameDeadlineDateLabel,
										GroupLayout.PREFERRED_SIZE, 160,
										GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));
		gameTitlePanelLayout.setVerticalGroup(gameTitlePanelLayout
				.createParallelGroup(Alignment.LEADING).addGroup(
						gameTitlePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gameTitlePanelLayout
								.createParallelGroup(
										Alignment.BASELINE)
										.addComponent(gameNameLabel)
										.addComponent(
												gameDeadlineDateLabel)
												.addComponent(btnEndGame))
												.addContainerGap(GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)));
		gameTitlePanel.setLayout(gameTitlePanelLayout);

		rowSplitPanel.setLayout(new java.awt.GridLayout(2, 1));

		topRowRequirementPanel.setLayout(new java.awt.GridLayout(1, 2));

		requirementNamePanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		requirementNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementNameLabel
		.setText("game.getRequirements.get(LIST SELECTION NUM).name()");

		final javax.swing.GroupLayout requirementNamePanelLayout = new javax.swing.GroupLayout(
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
				.addGroup(
						requirementNamePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(requirementNameLabel)
						.addContainerGap(
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		requirementDescriptionLabelPanel.setBackground(Color.white);
		requirementDescriptionLabel.setLineWrap(true);
		requirementDescriptionLabel.setEditable(false);
		requirementDescriptionLabel.setWrapStyleWord(true);
		requirementDescriptionLabelPanel.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(153, 153, 153)));

		requirementDescriptionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		requirementDescriptionLabel
		.setText("game.getRequirements.get(LIST SELECTION NUM).description()");

		final javax.swing.GroupLayout requirementDescriptionLabelPanelLayout = new
				javax.swing.GroupLayout(requirementDescriptionLabelPanel);
		requirementDescriptionLabelPanel
		.setLayout(requirementDescriptionLabelPanelLayout);
		requirementDescriptionLabelPanelLayout
		.setHorizontalGroup(requirementDescriptionLabelPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								requirementDescriptionLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(
										requirementDescriptionLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
										.addContainerGap()));
		requirementDescriptionLabelPanelLayout
		.setVerticalGroup(requirementDescriptionLabelPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								requirementDescriptionLabelPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addComponent(
										requirementDescriptionLabel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										230, Short.MAX_VALUE)
										.addContainerGap()));

		final javax.swing.GroupLayout requirementPanelLayout = new javax.swing.GroupLayout(
				requirementPanel);
		requirementPanel.setLayout(requirementPanelLayout);
		requirementPanelLayout.setHorizontalGroup(requirementPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(requirementNamePanel,
						javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(requirementDescriptionLabelPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		requirementPanelLayout
		.setVerticalGroup(requirementPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								requirementPanelLayout
								.createSequentialGroup()
								.addComponent(
										requirementNamePanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														requirementDescriptionLabelPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addContainerGap()));

		topRowRequirementPanel.add(requirementPanel);

		instructionsLabel.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
		instructionsLabel
		.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		if (!game.getDeckType().equals("No Deck")) {
			instructionsLabel
			.setText("<html>Click on one or more cards below to sum up your estimate&nbsp</html>");
		} else {
			instructionsLabel
			.setText("<html>Enter your estimate into the text field below&nbsp</html>");
		}

		estimateCenteringPanel.setLayout(new java.awt.GridBagLayout());

		estimateTitlePanel.setBorder(null);

		estimateTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
		estimateTitleLabel
		.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateTitleLabel.setText("<html>Your estimate</html>");

		final javax.swing.GroupLayout estimateTitlePanelLayout = new javax.swing.GroupLayout(
				estimateTitlePanel);
		estimateTitlePanelLayout.setHorizontalGroup(estimateTitlePanelLayout
				.createParallelGroup(Alignment.TRAILING).addGroup(
						Alignment.LEADING,
						estimateTitlePanelLayout
						.createSequentialGroup()
						.addComponent(estimateTitleLabel,
								GroupLayout.DEFAULT_SIZE, 108,
								Short.MAX_VALUE).addContainerGap()));
		estimateTitlePanelLayout.setVerticalGroup(estimateTitlePanelLayout
				.createParallelGroup(Alignment.LEADING).addGroup(
						estimateTitlePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(estimateTitleLabel)
						.addContainerGap(GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		estimateTitlePanel.setLayout(estimateTitlePanelLayout);

		estimateNumberPanel.setBorder(new javax.swing.border.SoftBevelBorder(
				javax.swing.border.BevelBorder.RAISED));

		estimateNumberLabel.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
		estimateNumberLabel
		.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		estimateNumberLabel.setText("?");

		final javax.swing.GroupLayout estimateNumberPanelLayout = new javax.swing.GroupLayout(
				estimateNumberPanel);
		estimateNumberPanel.setLayout(estimateNumberPanelLayout);
		estimateNumberPanelLayout.setHorizontalGroup(estimateNumberPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						estimateNumberPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(estimateNumberLabel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));
		estimateNumberPanelLayout.setVerticalGroup(estimateNumberPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						estimateNumberPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(estimateNumberLabel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));
		final javax.swing.GroupLayout estimatePanelLayout = new javax.swing.GroupLayout(
				estimatePanel);
		estimatePanel.setLayout(estimatePanelLayout);
		estimatePanelLayout
		.setHorizontalGroup(estimatePanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								estimatePanelLayout
								.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addGroup(
										estimatePanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														estimateTitlePanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addComponent(
																estimateNumberPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
																.addComponent(
																		submitButton,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))));
		estimatePanelLayout
		.setVerticalGroup(estimatePanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								estimatePanelLayout
								.createSequentialGroup()
								.addComponent(
										estimateTitlePanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														estimateNumberPanel,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																submitButton,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)));

		estimateCenteringPanel.add(estimatePanel,
				new java.awt.GridBagConstraints());

		final javax.swing.GroupLayout rightBlankPanelLayout = new javax.swing.GroupLayout(
				rightBlankPanel);
		rightBlankPanel.setLayout(rightBlankPanelLayout);
		rightBlankPanelLayout
		.setHorizontalGroup(rightBlankPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								rightBlankPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										rightBlankPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														rightBlankPanelLayout
														.createSequentialGroup()
														.addComponent(
																instructionsLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																598,
																Short.MAX_VALUE)
																.addContainerGap())
																.addComponent(
																		estimateCenteringPanel,
																		javax.swing.GroupLayout.Alignment.TRAILING,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))));
		rightBlankPanelLayout
		.setVerticalGroup(rightBlankPanelLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								rightBlankPanelLayout
								.createSequentialGroup()
								.addComponent(
										estimateCenteringPanel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(
														instructionsLabel,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addContainerGap()));

		topRowRequirementPanel.add(rightBlankPanel);

		rowSplitPanel.add(topRowRequirementPanel);

		allCardsPanel.setBackground(Color.white);
		allCardsPanel.setLayout(new java.awt.GridBagLayout());
		cardsScrollPane.setViewportView(allCardsPanel);

		rowSplitPanel.add(cardsScrollPane);

		final javax.swing.GroupLayout rightSplitPanelLayout = new javax.swing.GroupLayout(
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
												.addComponent(
														gameTitlePanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addComponent(
																rowSplitPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
																.addContainerGap()));
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
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(
														rowSplitPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
														.addContainerGap()));

		splitPane.setRightComponent(rightSplitPanel);

		add(splitPane, java.awt.BorderLayout.CENTER);
	}// </editor-fold>//GEN-END:initComponents

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
	private JButton btnEndGame;
}
