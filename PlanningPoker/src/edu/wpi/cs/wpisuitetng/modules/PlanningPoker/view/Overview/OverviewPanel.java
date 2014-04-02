package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JButton;

import java.awt.FlowLayout;

import javax.swing.JSplitPane;
import javax.swing.JTree;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import java.awt.Font;

import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;

public class OverviewPanel extends JPanel {
	private JTextField estimateTextField;

	private DefaultMutableTreeNode unanswered;

	/**
	 * Create the panel.
	 */
	public OverviewPanel() {
		setMinimumSize(new Dimension(800, 600));
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);

		/**
		 * panel that contains games in a tree structure
		 */
		JPanel yourGames = new JPanel();
		yourGames.setMinimumSize(new Dimension(150, 10));
		yourGames.setBorder(new LineBorder(Color.LIGHT_GRAY));
		splitPane.setLeftComponent(yourGames);
		yourGames.setLayout(new BorderLayout(0, 0));

		JTree gamesTree = new JTree();
		gamesTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Your Games") {
				{
					DefaultMutableTreeNode node_1;
					unanswered = new DefaultMutableTreeNode("Unanswered");
					unanswered.add(new DefaultMutableTreeNode("blue"));
					unanswered.add(new DefaultMutableTreeNode("violet"));
					unanswered.add(new DefaultMutableTreeNode("red"));
					unanswered.add(new DefaultMutableTreeNode("yellow"));
					add(unanswered);
					node_1 = new DefaultMutableTreeNode("Answered");
						node_1.add(new DefaultMutableTreeNode("basketball"));
						node_1.add(new DefaultMutableTreeNode("soccer"));
						node_1.add(new DefaultMutableTreeNode("football"));
						node_1.add(new DefaultMutableTreeNode("hockey"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Finished");
						node_1.add(new DefaultMutableTreeNode("hot dogs"));
						node_1.add(new DefaultMutableTreeNode("pizza"));
						node_1.add(new DefaultMutableTreeNode("ravioli"));
						node_1.add(new DefaultMutableTreeNode("bananas"));
					add(node_1);
				}
			}
		));
		yourGames.add(gamesTree);

		/**
		 * panel that contains the information for a particular game
		 */
		JPanel gameContainer = new JPanel();
		splitPane.setRightComponent(gameContainer);
		gameContainer.setLayout(new BorderLayout(0, 0));

		JPanel gameName = new JPanel();
		gameName.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gameContainer.add(gameName, BorderLayout.NORTH);

		JLabel lblSessionNameCreatedBy = new JLabel("SESSION_NAME created by MODERATOR on DATE_CREATED");
		lblSessionNameCreatedBy.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		gameName.add(lblSessionNameCreatedBy);

		JPanel infoContainer = new JPanel();
		gameContainer.add(infoContainer, BorderLayout.CENTER);
		infoContainer.setLayout(new GridLayout(2, 2, 5, 5));

		/**
		 * panel that contains the information for a game's requirements
		 */
		JPanel requirementsPane = new JPanel();
		requirementsPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(requirementsPane);
		requirementsPane.setLayout(new BorderLayout(0, 0));

		JPanel requirementLabel = new JPanel();
		requirementLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsPane.add(requirementLabel, BorderLayout.NORTH);

		JLabel lblChooseARequirement = new JLabel("Choose a requirement to estimate");
		lblChooseARequirement.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		requirementLabel.add(lblChooseARequirement);

		JPanel requirementSelector = new JPanel();
		requirementSelector.setBorder(new LineBorder(Color.LIGHT_GRAY));
		requirementsPane.add(requirementSelector, BorderLayout.CENTER);
		requirementSelector.setLayout(new BorderLayout(0, 0));

		JList requirementList = new JList();
		requirementList.setModel(new AbstractListModel() {
			String[] values = new String[] {"stuff", "stuff", "stuff", "stuff"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		requirementSelector.add(requirementList);

		/**
		 * panel that contains information for a user to submit an estimate
		 */
		JPanel submitPane = new JPanel();
		submitPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(submitPane);
		submitPane.setLayout(new BorderLayout(0, 0));

		JPanel submitLabel = new JPanel();
		submitLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		submitPane.add(submitLabel, BorderLayout.NORTH);

		JLabel lblSubmitAnEstimate = new JLabel("Submit an estimate");
		lblSubmitAnEstimate.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		submitLabel.add(lblSubmitAnEstimate);

		JPanel submitButton = new JPanel();
		submitButton.setBorder(new LineBorder(Color.LIGHT_GRAY));
		submitPane.add(submitButton, BorderLayout.SOUTH);

		JButton btnSubmitEstimate = new JButton("Submit");
		submitButton.add(btnSubmitEstimate);

		JScrollPane estimateSelector = new JScrollPane();
		submitPane.add(estimateSelector, BorderLayout.CENTER);

		JPanel estimatePanel = new JPanel();
		estimateSelector.setViewportView(estimatePanel);

		JButton btnNewButton_1 = new JButton("1");
		estimatePanel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("5");
		estimatePanel.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("10");
		estimatePanel.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("20");
		estimatePanel.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("50");
		estimatePanel.add(btnNewButton_5);

		JButton btnNewButton_6 = new JButton("80");
		estimatePanel.add(btnNewButton_6);

		JButton btnNewButton_7 = new JButton("100");
		estimatePanel.add(btnNewButton_7);

		try {
		    Image img = ImageIO.read(getClass().getResource("wpiCardFrontWPI_final.png"));

		    btnNewButton_1.setIcon(new ImageIcon(img));
		    btnNewButton_1.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_1.setContentAreaFilled(false);
		    btnNewButton_1.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_1.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_1.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_2.setIcon(new ImageIcon(img));
		    btnNewButton_2.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_2.setContentAreaFilled(false);
		    btnNewButton_2.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_2.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_2.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_3.setIcon(new ImageIcon(img));
		    btnNewButton_3.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_3.setContentAreaFilled(false);
		    btnNewButton_3.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_3.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_3.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_4.setIcon(new ImageIcon(img));
		    btnNewButton_4.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_4.setContentAreaFilled(false);
		    btnNewButton_4.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_4.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_4.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_5.setIcon(new ImageIcon(img));
		    btnNewButton_5.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_5.setContentAreaFilled(false);
		    btnNewButton_5.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_5.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_5.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_6.setIcon(new ImageIcon(img));
		    btnNewButton_6.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_6.setContentAreaFilled(false);
		    btnNewButton_6.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_6.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_6.setFont(new Font("arial",Font.BOLD,23));

		    btnNewButton_7.setIcon(new ImageIcon(img));
		    btnNewButton_7.setBorder(BorderFactory.createEmptyBorder());
		    btnNewButton_7.setContentAreaFilled(false);
		    btnNewButton_7.setHorizontalTextPosition(JButton.CENTER);
		    btnNewButton_7.setVerticalTextPosition(JButton.CENTER);
		    btnNewButton_7.setFont(new Font("arial",Font.BOLD,23));

		} catch (IOException ex) {
			System.out.print(ex.getMessage());
		}

		/**
		 * panel that contains that statistics of a session (if the session has ended)
		 */
		JPanel resultPane = new JPanel();
		resultPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(resultPane);
		resultPane.setLayout(new BorderLayout(0, 0));

		JPanel resultLabel = new JPanel();
		resultLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		resultPane.add(resultLabel, BorderLayout.NORTH);

		JLabel lblGameResults = new JLabel("Game results");
		lblGameResults.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		resultLabel.add(lblGameResults);

		JPanel finalEstimate = new JPanel();
		finalEstimate.setBorder(new LineBorder(Color.LIGHT_GRAY));
		FlowLayout fl_finalEstimate = (FlowLayout) finalEstimate.getLayout();
		fl_finalEstimate.setAlignment(FlowLayout.LEFT);
		resultPane.add(finalEstimate, BorderLayout.SOUTH);

		JLabel lblFinalEstimate = new JLabel("Final estimate:");
		finalEstimate.add(lblFinalEstimate);

		estimateTextField = new JTextField();
		finalEstimate.add(estimateTextField);
		estimateTextField.setColumns(10);

		JButton btnSubmitResults = new JButton("Submit");
		finalEstimate.add(btnSubmitResults);

		JPanel statisticsPane = new JPanel();
		statisticsPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		resultPane.add(statisticsPane, BorderLayout.CENTER);
		statisticsPane.setLayout(new GridLayout(3, 2, 0, 0));

		JPanel meanPane = new JPanel();
		statisticsPane.add(meanPane);

		JLabel lblMean = new JLabel("Mean:");
		meanPane.add(lblMean);

		JPanel stdDevPane = new JPanel();
		statisticsPane.add(stdDevPane);

		JLabel lblStdDev = new JLabel("Std Dev:");
		stdDevPane.add(lblStdDev);

		JPanel minPane = new JPanel();
		statisticsPane.add(minPane);

		JLabel lblMin = new JLabel("Min:");
		minPane.add(lblMin);

		JPanel maxPane = new JPanel();
		statisticsPane.add(maxPane);

		JLabel lblMax = new JLabel("Max:");
		maxPane.add(lblMax);

		JPanel medianPane = new JPanel();
		statisticsPane.add(medianPane);

		JLabel lblMedian = new JLabel("Median:");
		medianPane.add(lblMedian);

		JPanel modePane = new JPanel();
		statisticsPane.add(modePane);

		JLabel lblMode = new JLabel("Mode:");
		modePane.add(lblMode);

		/**
		 * panel that contains team estimates (if the session has ended)
		 */
		JPanel teamEstimates = new JPanel();
		teamEstimates.setBorder(new LineBorder(Color.LIGHT_GRAY));
		infoContainer.add(teamEstimates);
		teamEstimates.setLayout(new BorderLayout(0, 0));

		JPanel teamEstimatesLabel = new JPanel();
		teamEstimatesLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		teamEstimates.add(teamEstimatesLabel, BorderLayout.NORTH);

		JLabel lblTeamsEstimates = new JLabel("Team's estimates");
		lblTeamsEstimates.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		teamEstimatesLabel.add(lblTeamsEstimates);

		JPanel timerPane = new JPanel();
		timerPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
		teamEstimates.add(timerPane, BorderLayout.SOUTH);
		timerPane.setLayout(new BorderLayout(0, 0));

		JButton btnEndVoting = new JButton("End Voting");
		timerPane.add(btnEndVoting, BorderLayout.EAST);

		JLabel lblTimer = new JLabel("TIMER_MINS_LEFT");
		timerPane.add(lblTimer, BorderLayout.WEST);

		JPanel teamResults = new JPanel();
		teamEstimates.add(teamResults, BorderLayout.CENTER);
		teamResults.setLayout(new BorderLayout(0, 0));

		JScrollPane teamResultsPane = new JScrollPane();
		teamResults.add(teamResultsPane, BorderLayout.CENTER);

		JPanel panel_17 = new JPanel();
		teamResultsPane.setViewportView(panel_17);

		JButton btnSomeonesEstimate = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate);

		JButton btnSomeonesEstimate_3 = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate_3);

		JButton btnSomeonesEstimate_2 = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate_2);

		JButton btnSomeonesEstimate_1 = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate_1);

	}

	public void fireRefresh() {
		GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
		unanswered.removeAllChildren();
		for(PlanningPokerGame game : PlanningPokerGameModel.getInstance().getPlanningPokerGames()) {
			unanswered.add(new DefaultMutableTreeNode(game.getGameName()));
		}
	}

}
