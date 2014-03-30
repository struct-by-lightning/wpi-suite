package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

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

public class AustinMockup extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public AustinMockup() {
		setMinimumSize(new Dimension(800, 600));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		add(panel, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("+ Create Game");
		panel.add(btnNewButton);
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setMinimumSize(new Dimension(150, 10));
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
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
		panel_1.add(tree);
		
		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_2.add(panel_3, BorderLayout.NORTH);
		
		JLabel lblSessionnameCreatedBy = new JLabel("SESSION_NAME created by MODERATOR on DATE_CREATED");
		lblSessionnameCreatedBy.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		panel_3.add(lblSessionnameCreatedBy);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new GridLayout(2, 2, 5, 5));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_4.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_5.add(panel_9, BorderLayout.NORTH);
		
		JLabel lblChooseARequirement = new JLabel("Choose a requirement to estimate");
		lblChooseARequirement.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panel_9.add(lblChooseARequirement);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_5.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new BorderLayout(0, 0));
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"stuff", "stuff", "stuff", "stuff"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		panel_10.add(list);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_4.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_6.add(panel_11, BorderLayout.NORTH);
		
		JLabel lblSubmitAnEstimate = new JLabel("Submit an estimate");
		lblSubmitAnEstimate.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panel_11.add(lblSubmitAnEstimate);
		
		JPanel panel_12 = new JPanel();
		panel_12.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_6.add(panel_12, BorderLayout.SOUTH);
		
		JButton btnSubmit = new JButton("Submit");
		panel_12.add(btnSubmit);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_6.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_13 = new JPanel();
		scrollPane.setViewportView(panel_13);
		
		JButton btnNewButton_1 = new JButton("New button");
		panel_13.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		panel_13.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("New button");
		panel_13.add(btnNewButton_3);
		
		JButton btnNewButton_5 = new JButton("New button");
		panel_13.add(btnNewButton_5);
		
		JButton btnNewButton_4 = new JButton("New button");
		panel_13.add(btnNewButton_4);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_4.add(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_14 = new JPanel();
		panel_14.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_7.add(panel_14, BorderLayout.NORTH);
		
		JLabel lblTeamsEstimates = new JLabel("Team's estimates");
		lblTeamsEstimates.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panel_14.add(lblTeamsEstimates);
		
		JPanel panel_15 = new JPanel();
		panel_15.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_7.add(panel_15, BorderLayout.SOUTH);
		panel_15.setLayout(new BorderLayout(0, 0));
		
		JButton btnEndVoting = new JButton("End Voting");
		panel_15.add(btnEndVoting, BorderLayout.EAST);
		
		JLabel lblTimer = new JLabel("TIMER_MINS_LEFT");
		panel_15.add(lblTimer, BorderLayout.WEST);
		
		JPanel panel_16 = new JPanel();
		panel_7.add(panel_16, BorderLayout.CENTER);
		panel_16.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_16.add(scrollPane_1, BorderLayout.CENTER);
		
		JPanel panel_17 = new JPanel();
		scrollPane_1.setViewportView(panel_17);
		
		JButton btnSomeonesEstimate = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate);
		
		JButton btnSomeonesEstimate_3 = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate_3);
		
		JButton btnSomeonesEstimate_2 = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate_2);
		
		JButton btnSomeonesEstimate_1 = new JButton("Someone's estimate");
		panel_17.add(btnSomeonesEstimate_1);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_4.add(panel_8);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_18 = new JPanel();
		panel_18.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_8.add(panel_18, BorderLayout.NORTH);
		
		JLabel lblGameResults = new JLabel("Game results");
		lblGameResults.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panel_18.add(lblGameResults);
		
		JPanel panel_19 = new JPanel();
		panel_19.setBorder(new LineBorder(Color.LIGHT_GRAY));
		FlowLayout flowLayout_1 = (FlowLayout) panel_19.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_8.add(panel_19, BorderLayout.SOUTH);
		
		JLabel lblFinalEstimate = new JLabel("Final estimate:");
		panel_19.add(lblFinalEstimate);
		
		textField = new JTextField();
		panel_19.add(textField);
		textField.setColumns(10);
		
		JButton btnSubmit_1 = new JButton("Submit");
		panel_19.add(btnSubmit_1);
		
		JPanel panel_20 = new JPanel();
		panel_20.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_8.add(panel_20, BorderLayout.CENTER);
		panel_20.setLayout(new GridLayout(3, 2, 0, 0));
		
		JPanel panel_22 = new JPanel();
		panel_20.add(panel_22);
		
		JLabel lblMean = new JLabel("Mean:");
		panel_22.add(lblMean);
		
		JPanel panel_23 = new JPanel();
		panel_20.add(panel_23);
		
		JLabel lblStdDev = new JLabel("Std Dev:");
		panel_23.add(lblStdDev);
		
		JPanel panel_25 = new JPanel();
		panel_20.add(panel_25);
		
		JLabel lblMin = new JLabel("Min:");
		panel_25.add(lblMin);
		
		JPanel panel_24 = new JPanel();
		panel_20.add(panel_24);
		
		JLabel lblMax = new JLabel("Max:");
		panel_24.add(lblMax);
		
		JPanel panel_21 = new JPanel();
		panel_20.add(panel_21);
		
		JLabel lblMedian = new JLabel("Median:");
		panel_21.add(lblMedian);
		
		JPanel panel_26 = new JPanel();
		panel_20.add(panel_26);
		
		JLabel lblMode = new JLabel("Mode:");
		panel_26.add(lblMode);

	}

}
