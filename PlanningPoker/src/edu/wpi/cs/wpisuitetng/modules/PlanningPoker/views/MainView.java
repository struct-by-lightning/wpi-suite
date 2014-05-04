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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.TabPanel;
import edu.wpi.cs.wpisuitetng.janeway.interfaces.ContactChecker;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddDeckController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerUserController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUserModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * A singleton class encapsulating the GUI for the two parts of the planning
 * poker UI:
 * 
 * - The component in the Janeway module toolbar area.
 * 
 * - The component in the main dislpay area of the Janeway module.
 * 
 * This class is implemented as a singleton because there should only ever be
 * one instance of each of the main UI components.
 * 
 * @version $Revision: 1.0 $
 * @author Austin Rose (atrose)
 */
public class MainView {
	private List<PlanningPokerGame> games;

	/**
	 * This function adds a new closeable tab to the planning poker module's
	 * inner tab pane.
	 * 
	 * @param tabName
	 *            The title for the tab being added.
	 * @param tabPanel
	 *            The JPanel to display as the newly added tab's content.
	 */
	public void addCloseableTab(String tabName, JPanel tabPanel) {
		this.mainComponent.addTab(tabName, tabPanel);
		this.mainComponent.setTabComponentAt(
				this.mainComponent.indexOfComponent(tabPanel),
				new ClosableTabComponent(this.mainComponent, tabPanel));
		this.mainComponent.setSelectedComponent(tabPanel);

	}

	/**
	 * This function closes the tab which is currently open in the planning
	 * poker module's inner tab pane.
	 * 
	 * TODO: Is there any risk of this closing the should-always-be-open
	 * overview tab?
	 */
	public void removeClosableTab() {

		// TODO: Should really figure out all the right places to call for
		// updating the database, or refreshing the game tree, and whatnot.
		// Should the list of games ever really update other than when the tree
		// is refreshed?
		this.refreshGameTree();

		final Component selected = mainComponent.getSelectedComponent();
		if (selected != null) {
			mainComponent.remove(selected);
		}

		// TODO: Do these do anything?
		// MainView.getInstance().gameTree.repaint();
		// MainView.getInstance().gameTree.getParent().repaint();
	}

	/**
	 * This method brings the game-displaying tree in the main overview tab up
	 * to date with the database.
	 */
	public int refreshGameTree() {

		// Send a request for an updated set of planning poker games and wait
		// until it is processed.
		GetPlanningPokerGamesController.getInstance()
				.retrievePlanningPokerGames();
		while (GetPlanningPokerGamesController.waitingOnRequest) {
			continue;
		}
		games = PlanningPokerGameModel.getPlanningPokerGames();

		// Instantiate each of the folders which may appear in the tree.
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				"All Games");
		final DefaultMutableTreeNode newGames = new DefaultMutableTreeNode(
				"New");
		final DefaultMutableTreeNode openGames = new DefaultMutableTreeNode(
				"Open");
		final DefaultMutableTreeNode finishedGames = new DefaultMutableTreeNode(
				"Finished");

		// Potentially add a node to one of the sub folders for each planning
		// poker game we have.
		for (PlanningPokerGame game : games) {
			DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(
					game.getGameName());

			// Conditions for a game to be "New" and visible to the current
			// user.
			if (!game.isLive()
					&& !game.isFinished()
					&& game.getModerator().equals(
							ConfigManager.getConfig().getUserName())) {
				newGames.add(nodeToAdd);
			}

			// Conditions for a game to be "Open".
			//
			// TODO: This should check that the current user has actually been
			// added to the project as well...
			//
			// Maybe when a planning poker game is created it makes a note of
			// the users associated with the currently open project (add it to
			// the model), and then we can filter against that here.
			else if (game.isLive() && !game.isFinished()) {
				openGames.add(nodeToAdd);
			}

			// Conditions for a game to be "Finished".
			// TODO: Same user-project filtering as above needed here too.
			else if (game.isFinished() && !game.isLive()) {
				finishedGames.add(nodeToAdd);
			}

		}

		// Add the "New" folder to the tree so long as it has any games in it.
		if (!newGames.isLeaf()) {
			root.add(newGames);
		}

		// Add the "Open" folder to the tree so long as it has any games in it.
		if (!openGames.isLeaf()) {
			root.add(openGames);
		}

		// Add the "Closed" folder to the tree so long as it has any games in
		// it.
		if (!finishedGames.isLeaf()) {
			root.add(finishedGames);
		}

		// Get the model for the tree.
		final DefaultTreeModel model = (DefaultTreeModel) gameTree.getModel();

		// Set the model's root node to the newly constructed one.
		model.setRoot(root);
		model.reload(root);

		// Expand the tree's folders.
		for (int i = 0; i < gameTree.getRowCount(); i++) {
			gameTree.expandRow(i);
		}
		
		return 1;
	}

	/**
	 * @return The component that should be displayed in the toolbar area of
	 *         planning poker. This will either be a banner for the contact
	 *         prompt, or the main toolbar for planning poker.
	 */
	public JComponent getToolbarComponent() {
		return cardToolbarComponent;
	}

	/**
	 * @return The component that should be displayed in the main area of
	 *         planning poker. This will either be the form/info for the contact
	 *         information prompt, or the main tabbed pane for planning poker.
	 */
	public JComponent getMainComponent() {
		return cardMainAreaComponent;
	}

	/**
	 * This function is called with the appropriate argument whenever the user
	 * double clicks a game in the tree.
	 * 
	 * @param selectedGame
	 *            The planning poker game which the user has just double clicked
	 *            in the main view's game tree.
	 */
	private static void gameWasDoubleClicked(PlanningPokerGame selectedGame) {
		boolean originalSelectedState = selectedGame.isFinished();
		// Ensure that requirements have been updated from the database before
		// attempting to display them.
		try {
			GetRequirementsController.getInstance().retrieveRequirements();
			GetPlanningPokerGamesController.getInstance()
					.retrievePlanningPokerGames();

			while (true) {
				RequirementModel rm = RequirementModel.getInstance();
				int size = rm.getRequirements().size();
				if (size > 0) {
					if (rm.getRequirements().get(0) != null) {
						break;
					}
				}
			}

			// Make sure the a reply to the GetPlanningPokerGames request has
			// been received
			while (GetPlanningPokerGamesController.waitingOnRequest) {
				continue;
			}

		} catch (Exception e) {
			System.out
					.println("Exception in gameWasDoubleClicked() from retrieveRequirements()");
			e.printStackTrace();
		}

		// Search for selected game tab that already exists.
		// If it is, remove that game tab, recreate one so that the requirements
		// are updated.
		final Component[] tabInstances = mainComponent.getComponents();
		for (Component c : tabInstances) {
			if (c instanceof OpenGameView || c instanceof NewGameView
					|| c instanceof ClosedGameView) {
				String gameName = "";
				if (c instanceof OpenGameView)
					gameName = ((OpenGameView) c).getGame().getID();
				if (c instanceof NewGameView)
					gameName = ((NewGameView) c).getGame().getID();
				if (c instanceof ClosedGameView)
					gameName = ((ClosedGameView) c).getGame().getID();

				if (selectedGame.getID().equals(gameName)) {
					mainComponent.setSelectedComponent(c);
					return;
				}
			}
		}

		// wait for games to be retrieved
		// while (GetPlanningPokerGamesController.waitingOnRequest) {}

		boolean server = PlanningPokerGameModel.getPlanningPokerGame(
				selectedGame.getGameName()).isFinished();
		System.out.println("Client: " + originalSelectedState + " Server: "
				+ server);

		// check if the original state has changed (ie went from open to
		// finished)
		// new to open is irrelevant, as only one person can see it, thus it
		// can't be changed be someone else
		if (originalSelectedState == server) {
			System.err.println("Game was the same");
			// Conditions for a game to be "New"
			if (!selectedGame.isLive() && !selectedGame.isFinished()) {
				NewGameView.open(selectedGame);
			}

			// Conditions for a game to be "Open"
			if (selectedGame.isLive() && !selectedGame.isFinished()) {
				OpenGameView.open(selectedGame);
			}

			// Conditions for a game to be "Finished"
			if (selectedGame.isFinished() && !selectedGame.isLive()) {
				ClosedGameView.open(selectedGame);
			}
		} else {
			System.err.println("Game changed");
			int n = JOptionPane
					.showConfirmDialog(
							null,
							"The game you have selected"
									+ " has closed. Would you like to open the finished game?",
							"", JOptionPane.YES_NO_OPTION);
			System.err.println("Output: " + n);

			if (n == 0) { // yes
				ClosedGameView.open(selectedGame); // this will automatically
													// refresh the tree
			} else { // no, so just refresh the tree
				MainView.getInstance().refreshGameTree();
			}
		}
	}

	/**
	 * This method is called whenever the user clicks the "Create New Game"
	 * button, and opens up a new closeable tab with the UI for creating a new
	 * game.
	 */
	public static void createGameButtonClicked() {
		CreateGameView.openNewTab();
	}
	
	public static void createDeckButtonClicked(){
		CreateDeckView.openNewTab();
	}

	/**
	 * The name which components related to the contact prompt are indexed by in
	 * card layouts.
	 */
	private final String CONTACT_PROMPT_VIEW = "CONTACT_PROMPT_VIEW";

	/**
	 * The name which components related to the main area view are indexed by in
	 * card layouts.
	 */
	private final String MAIN_VIEW = "MAIN_VIEW";

	/**
	 * Triggers the planning poker module to switch from the contact information
	 * prompt to it's main overview - the default GUI for after a user has
	 * already provided contact information.
	 */
	public void switchToMainOverview() {

		// Make sure the game tree is up to date with the database before
		// displaying it.
		this.refreshGameTree();

		// Set the correct active cards on the toolbar and main components.
		final CardLayout toolbar = (CardLayout) cardToolbarComponent
				.getLayout();
		final CardLayout mainArea = (CardLayout) cardMainAreaComponent
				.getLayout();
		toolbar.show(cardToolbarComponent, MAIN_VIEW);
		mainArea.show(cardMainAreaComponent, MAIN_VIEW);

    	
	}

	/**
	 * The single instance of this singleton-pattern class.
	 */
	private static MainView singleInstance;

	/**
	 * @return MainView The single accessible instance of this singleton-pattern
	 *         class.
	 */
	public static MainView getInstance() {
		if (MainView.singleInstance == null) {
			MainView.singleInstance = new MainView();
		}

		return MainView.singleInstance;
	}

	/**
	 * The component filling the toolbar area of the planning poker module. It's
	 * a JPanel with a card layout where the two possible cards are the
	 * "toolbar" for the contact prompt view, and the toolbar for the main view.
	 */
	private JComponent cardToolbarComponent;

	/**
	 * The component filling the main area of the planning poker module. It's a
	 * JPanel with a card layout where the two possible cards are the main area
	 * component for the contact prompt view, and the the main area component
	 * for the main view.
	 */
	private JComponent cardMainAreaComponent;

	/**
	 * Constructor initializes all GUI components and starts the UI on the
	 * contact information prompt. It is a private member so that this class can
	 * be implemented as a singleton.
	 */
	private MainView() {
		initComponents();
		setupCards();
		initLogic();

		final ContactChecker checker = new ContactChecker() {

			@Override
			public void verifyContactInfo() {

				boolean userHasInfo = false;

				GetPlanningPokerUserController.getInstance().retrieveUser();
				try {
					Thread.sleep(150);
				} catch (Exception e) {
					e.printStackTrace();
				}

				final PlanningPokerUser user = PlanningPokerUserModel
						.getInstance().getUser(
								ConfigManager.getConfig().getUserName());

				userHasInfo = (user != null);

				if (userHasInfo) {
					switchToMainOverview();
				}

			}
		};
		TabPanel.setContactChecker(checker);
	}

	public JTabbedPane getTabComponent() {
		return mainComponent;
	}

	/**
	 * Initialize the card layout JPanels which will allow switiching between
	 * the main planning poker view and the prompt for contact information.
	 */
	private void setupCards() {
		// Initialize the toolbar JPanel with a card layout.
		cardToolbarComponent = new JPanel(new CardLayout());

		// Initialize the main area JPanel with a card layout.
		cardMainAreaComponent = new JPanel(new CardLayout());

		// Add the contact prompt view's toolbar.
		cardToolbarComponent.add(new ContactInformationPromptToolbarView(),
				CONTACT_PROMPT_VIEW);

		// Add the contact prompt view's main area.
		cardMainAreaComponent.add(ContactInformationPromptView.getInstance(),
				CONTACT_PROMPT_VIEW);

		// Add the main view's toolbar.
		cardToolbarComponent.add(toolbarComponent, MAIN_VIEW);

		// Add the main view's main area.
		cardMainAreaComponent.add(mainComponent, MAIN_VIEW);

		// Display the contact prompt view first by default.

		final CardLayout toolbar = (CardLayout) cardToolbarComponent
				.getLayout();
		final CardLayout mainArea = (CardLayout) cardMainAreaComponent
				.getLayout();
		toolbar.show(cardToolbarComponent, CONTACT_PROMPT_VIEW);
		mainArea.show(cardMainAreaComponent, CONTACT_PROMPT_VIEW);

	}

	/**
	 * This method adds listeners needed for GUI components.
	 */
	private void initLogic() {

		// Listener which triggers an appropriate call to
		// MainView.gameWasDoubleClicked() when the user double clicks a game in
		// the game tree.
		gameTree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				final int selRow = gameTree.getRowForLocation(e.getX(),
						e.getY());
				final TreePath selPath = gameTree.getPathForLocation(e.getX(),
						e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 2) {
						if (games.size() > 0) {
							final DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
									.getLastPathComponent();
							final String gameName = (String) node
									.getUserObject();

							MainView.gameWasDoubleClicked(PlanningPokerGameModel
									.getPlanningPokerGame(gameName));
						}
					}
				}
			}
			public void mouseExited(MouseEvent e) {
				gameTree.setToolTipText(null);
			}
		});
			gameTree.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				final int selRow = gameTree.getRowForLocation(e.getX(),
						e.getY());
				final TreePath selPath = gameTree.getPathForLocation(e.getX(),
						e.getY());
				if (selRow != -1) {
					if (games.size() > 0) {
						final DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
								.getLastPathComponent();
						final String gameName = (String) node
								.getUserObject();
						try {
							StringBuilder sb = new StringBuilder();
							sb.append("<html>");
							String[] sa = PlanningPokerGameModel.getPlanningPokerGame(gameName).getDescription().split(" ");
							int lineLength = 0;
							for(int i = 0; i < sa.length; i++) {
								lineLength += sa[i].length();
								sb.append(sa[i]);
								sb.append(" ");
								if(lineLength > 40) {
									lineLength  = 0;
									sb.append("<br>");
								}
							}
							sb.append("</html>");
							gameTree.setToolTipText(sb.toString());
						} catch (NullPointerException n) {}
					}
				}
			}
		});

		// This listener updates the overview tab's tree of games before it is
		// displayed.
		mainComponent.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (e.getSource() instanceof JTabbedPane) {
					final JTabbedPane pane = (JTabbedPane) e.getSource();
					if (pane.getSelectedIndex() == 0) {
						refreshGameTree();
					}
				}
			}
		});
	}

	/**
	 * THIS METHOD WAS AUTOMATICALLY GENERATED BY NETBEANS.
	 */
	private void initComponents() {
		toolbarComponent = new ToolbarView(true);

		gameTree = new javax.swing.JTree(
				new DefaultMutableTreeNode("All Games"));

		treeScrollPane = new javax.swing.JScrollPane();
		treeScrollPane.setMinimumSize(new java.awt.Dimension(200, 384));
		treeScrollPane.setViewportView(gameTree);

		createGameButton = new javax.swing.JButton();
		mainComponent = new javax.swing.JTabbedPane();
		overviewTabSplitPane = new javax.swing.JSplitPane();
		overviewTabSplitPane.setDividerLocation(350);
		infoPanel = new javax.swing.JPanel();
		left = new javax.swing.JPanel();
		whatIsTitle = new javax.swing.JLabel();
		leftHorizontalSeparator = new javax.swing.JSeparator();
		whatIsBody = new javax.swing.JLabel();
		right = new javax.swing.JPanel();
		whatIsTitle1 = new javax.swing.JLabel();
		leftHorizontalSeparator1 = new javax.swing.JSeparator();
		whatIsBody1 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jTextField1 = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		jButton2 = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jCheckBox1 = new javax.swing.JCheckBox();
		jPanel5 = new javax.swing.JPanel();
		jCheckBox2 = new javax.swing.JCheckBox();
		jPanel3 = new javax.swing.JPanel();
		jLabel3 = new javax.swing.JLabel();
		jPanel6 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		jPanel10 = new javax.swing.JPanel();
		jPanel8 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jList2 = new javax.swing.JList();
		jPanel13 = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		jPanel11 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jPanel9 = new javax.swing.JPanel();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jButton5 = new javax.swing.JButton();
		jButton6 = new javax.swing.JButton();
		jPanel12 = new javax.swing.JPanel();
		jPanel7 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jList1 = new javax.swing.JList();
		jPanel14 = new javax.swing.JPanel();
		jComboBox1 = new javax.swing.JComboBox();
		jLabel6 = new javax.swing.JLabel();
		jPanel16 = new javax.swing.JPanel();
		jLabel8 = new javax.swing.JLabel();
		jPanel15 = new javax.swing.JPanel();
		jLabel7 = new javax.swing.JLabel();

		createGameButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
		createGameButton.setText("<html>Create a new game</html>");

		mainComponent.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

		overviewTabSplitPane.setLeftComponent(treeScrollPane);

		infoPanel.setLayout(new java.awt.GridLayout(1, 2));

		left.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		whatIsTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		whatIsTitle.setText("<html>Getting started</html>");

		whatIsBody.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
		whatIsBody
				.setText("<html> What does it mean if you click...<br><br> <em><strong>Create New Game:</strong></em><br> This will open the window to create a new game where you can choose the user requirements for the game.<br><br> <em><strong>New folder:</strong></em><br> These games are created but have not been started yet. If you click one of the games in this folder you if you are the moderator you can start the game.<br><br> <em><strong>Open folder:</strong></em><br> These games have been created and started you can estimate each user story. After the user story is estimated it will be marked as completed.<br><br> <em><strong>Closed folder:</strong></em><br> These are closed games. By clicking on the games in this folder you will get the results from this game. If you are the moderator of this game then you should be able to edit results.<br><br> If you are looking for further information refer to Help. </html>");

		final javax.swing.GroupLayout leftLayout = new javax.swing.GroupLayout(
				left);
		left.setLayout(leftLayout);
		leftLayout
				.setHorizontalGroup(leftLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								leftLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												leftLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																whatIsTitle)
														.addComponent(
																leftHorizontalSeparator)
														.addComponent(
																whatIsBody,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																674,
																Short.MAX_VALUE))
										.addContainerGap()));
		leftLayout
				.setVerticalGroup(leftLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								leftLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												whatIsTitle,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												leftHorizontalSeparator,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(
												whatIsBody,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(478, Short.MAX_VALUE)));

		infoPanel.add(left);

		right.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		whatIsTitle1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		whatIsTitle1.setText("<html>What is Planning Poker?</html>");

		whatIsBody1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
		whatIsBody1
				.setText("<html> Planning Poker is a consensus-based tool for software developers to come together and estimate effort of development goals for the team. This is a great tool for agile teams to estimate the user stories they have for a given iteration.<br><br> The idea behind Planning Poker is that team discusses each user story and then goes into the game and then each user goes into the deck and selects the card that represents how effort he or she thinks the task will take. This process can be repeated for any number of user stories in the game.<br><br> During the game all estimates remain private until everyone has chose his or her card. After all estimates are in the Planning Poker game will calculate the Mean, Median, Mode, Minimum, Maximum, and Standard Deviation of the game. These values can be used for the team to continue the discussion and come to a consensus of what the groups estimate is for the user story.<br><br> </html>");

		final javax.swing.GroupLayout rightLayout = new javax.swing.GroupLayout(
				right);
		right.setLayout(rightLayout);
		rightLayout
				.setHorizontalGroup(rightLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								rightLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												rightLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																whatIsTitle1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																674,
																Short.MAX_VALUE)
														.addComponent(
																leftHorizontalSeparator1)
														.addComponent(
																whatIsBody1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																0,
																Short.MAX_VALUE))
										.addContainerGap()));
		rightLayout
				.setVerticalGroup(rightLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								rightLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												whatIsTitle1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												leftHorizontalSeparator1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(
												whatIsBody1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(433, Short.MAX_VALUE)));

		infoPanel.add(right);

		overviewTabSplitPane.setRightComponent(infoPanel);

		mainComponent.addTab("Overview", overviewTabSplitPane);

		jPanel1.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
		jLabel1.setText("Game Name");

		final javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(jLabel1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jTextField1)
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jTextField1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel1))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
		jButton2.setText("CREATE");

		jPanel2.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		jCheckBox1.setText("Start voting immediately");

		final javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel2Layout
						.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addComponent(jCheckBox1)
						.addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jCheckBox1,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));

		jPanel5.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));
		jPanel5.setForeground(new java.awt.Color(204, 204, 204));

		jCheckBox2.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
		jCheckBox2.setText("End voting after a deadline?");

		jPanel3.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
		jLabel3.setText("End time stuff");

		final javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(
				jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel3Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel3)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel3Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel3)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		jPanel6.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
		jLabel5.setText("End date stuff");

		final javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(
				jPanel6);
		jPanel6.setLayout(jPanel6Layout);
		jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel6Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel5)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel6Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel5)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		final javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(
				jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout
				.setHorizontalGroup(jPanel5Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel5Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel5Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																jCheckBox2,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jPanel6,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jPanel3,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		jPanel5Layout
				.setVerticalGroup(jPanel5Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel5Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(jCheckBox2)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												jPanel3,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												jPanel6,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		jPanel10.setLayout(new java.awt.GridBagLayout());

		jList2.setModel(new javax.swing.AbstractListModel() {
			private final String[] strings = { "Item 1", "Item 2", "Item 3",
					"Item 4", "Item 5" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane2.setViewportView(jList2);

		jPanel13.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(102, 102, 102)));

		jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel4.setText("Backlog");

		final javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(
				jPanel13);
		jPanel13.setLayout(jPanel13Layout);
		jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel13Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel4,
								javax.swing.GroupLayout.DEFAULT_SIZE, 440,
								Short.MAX_VALUE).addContainerGap()));
		jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel13Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel4)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		final javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(
				jPanel8);
		jPanel8.setLayout(jPanel8Layout);
		jPanel8Layout
				.setHorizontalGroup(jPanel8Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel8Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel8Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jPanel13,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jScrollPane2))
										.addContainerGap()));
		jPanel8Layout
				.setVerticalGroup(jPanel8Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel8Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jPanel13,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		jPanel10.add(jPanel8, new java.awt.GridBagConstraints());

		jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
				15, 5));

		jPanel9.setLayout(new java.awt.GridBagLayout());

		jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jButton3.setText("<<");
		jButton3.setAlignmentX(0.5F);
		jButton3.setAlignmentY(0.0F);
		jPanel9.add(jButton3, new java.awt.GridBagConstraints());

		jButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jButton4.setText("<");
		jButton4.setAlignmentX(0.5F);
		jButton4.setAlignmentY(0.0F);
		jPanel9.add(jButton4, new java.awt.GridBagConstraints());

		jButton5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jButton5.setText(">");
		jButton5.setAlignmentX(0.5F);
		jButton5.setAlignmentY(0.0F);
		jPanel9.add(jButton5, new java.awt.GridBagConstraints());

		jButton6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
		jButton6.setText(">>");
		jButton6.setAlignmentX(0.5F);
		jButton6.setAlignmentY(0.0F);
		jPanel9.add(jButton6, new java.awt.GridBagConstraints());

		jPanel4.add(jPanel9);

		final javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(
				jPanel11);
		jPanel11.setLayout(jPanel11Layout);
		jPanel11Layout.setHorizontalGroup(jPanel11Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel11Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel4,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		jPanel11Layout.setVerticalGroup(jPanel11Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel11Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jPanel4,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));

		jPanel10.add(jPanel11, new java.awt.GridBagConstraints());

		jPanel7.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(102, 102, 102)));

		jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel2.setText("This Game");

		final javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(
				jPanel7);
		jPanel7.setLayout(jPanel7Layout);
		jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel7Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel2,
								javax.swing.GroupLayout.DEFAULT_SIZE, 440,
								Short.MAX_VALUE).addContainerGap()));
		jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel7Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel2)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		jList1.setModel(new javax.swing.AbstractListModel() {
			private final String[] strings = { "Item 1", "Item 2", "Item 3",
					"Item 4", "Item 5" };

			public int getSize() {
				return strings.length;
			}

			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane1.setViewportView(jList1);

		final javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(
				jPanel12);
		jPanel12.setLayout(jPanel12Layout);
		jPanel12Layout
				.setHorizontalGroup(jPanel12Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel12Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel12Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jPanel7,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jScrollPane1))
										.addContainerGap()));
		jPanel12Layout
				.setVerticalGroup(jPanel12Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel12Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jPanel7,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		jPanel10.add(jPanel12, new java.awt.GridBagConstraints());

		jPanel14.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Item 1", "Item 2", "Item 3", "Item 4" }));

		jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
		jLabel6.setText("Choose a card deck:");

		jPanel16.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		jLabel8.setText("0, 1, 3, 5, 6, 7, 8, 9");

		final javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(
				jPanel16);
		jPanel16.setLayout(jPanel16Layout);
		jPanel16Layout.setHorizontalGroup(jPanel16Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel16Layout
						.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addComponent(jLabel8)
						.addContainerGap()));
		jPanel16Layout.setVerticalGroup(jPanel16Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel16Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel8)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));

		final javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(
				jPanel14);
		jPanel14.setLayout(jPanel14Layout);
		jPanel14Layout
				.setHorizontalGroup(jPanel14Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel14Layout
										.createSequentialGroup()
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												jPanel14Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																jPanel16,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGroup(
																jPanel14Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel6)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jComboBox1,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
		jPanel14Layout
				.setVerticalGroup(jPanel14Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel14Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel14Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jComboBox1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel6))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jPanel16,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		jPanel15.setBackground(Color.white);
		jPanel15.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(204, 204, 204)));

		jLabel7.setText("Logged in as...");

		final javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(
				jPanel15);
		jPanel15.setLayout(jPanel15Layout);
		jPanel15Layout.setHorizontalGroup(jPanel15Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel15Layout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(jLabel7)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		jPanel15Layout.setVerticalGroup(jPanel15Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel15Layout
						.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addComponent(jLabel7)
						.addContainerGap()));

	}

	/**
	 * THESE DECLARATIONS WERE AUTOMATICALLY GENERATED BY NETBEANS.
	 */
	private javax.swing.JButton createGameButton;
	private javax.swing.JPanel infoPanel;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JButton jButton6;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JCheckBox jCheckBox2;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JList jList1;
	private javax.swing.JList jList2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel12;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel14;
	private javax.swing.JPanel jPanel15;
	private javax.swing.JPanel jPanel16;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JPanel left;
	private javax.swing.JSeparator leftHorizontalSeparator;
	private javax.swing.JSeparator leftHorizontalSeparator1;
	private static javax.swing.JTabbedPane mainComponent;
	private javax.swing.JPanel newGameTabPanel;
	private javax.swing.JSplitPane overviewTabSplitPane;
	private javax.swing.JPanel right;
	private ToolbarView toolbarComponent;
	private javax.swing.JTree gameTree;
	private javax.swing.JScrollPane treeScrollPane;
	private javax.swing.JLabel whatIsBody;
	private javax.swing.JLabel whatIsBody1;
	private javax.swing.JLabel whatIsTitle;
	private javax.swing.JLabel whatIsTitle1;

	/**
	 * Description
	 */
	public static void preferencesButtonClicked() {
		// Loop and check to see if the preferences tab is already opened.
		Component[] tabInstances = mainComponent.getComponents();
		boolean alreadyOpened = false;

		for (Component c : tabInstances) {
			if (c instanceof PreferencesView) { // Preferences already open,
												// select it and return
				alreadyOpened = true;
				mainComponent.setSelectedComponent(c);
				return;
			}
		}

		// If not exist, open it
		PreferencesView.openNewTab();
	}
}
