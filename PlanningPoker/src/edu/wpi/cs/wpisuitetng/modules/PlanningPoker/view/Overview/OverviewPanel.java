package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class OverviewPanel extends JPanel {
	private JTextField estimateTextField;

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
					node_1 = new DefaultMutableTreeNode("Unanswered");
						node_1.add(new DefaultMutableTreeNode("blue"));
						node_1.add(new DefaultMutableTreeNode("violet"));
						node_1.add(new DefaultMutableTreeNode("red"));
						node_1.add(new DefaultMutableTreeNode("yellow"));
					add(node_1);
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
		
		JButton btnNewButton_1 = new JButton("New button");
		estimatePanel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		estimatePanel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("New button");
		estimatePanel.add(btnNewButton_3);
		
		JButton btnNewButton_5 = new JButton("New button");
		estimatePanel.add(btnNewButton_5);
		
		JButton btnNewButton_4 = new JButton("New button");
		estimatePanel.add(btnNewButton_4);
		
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

}
