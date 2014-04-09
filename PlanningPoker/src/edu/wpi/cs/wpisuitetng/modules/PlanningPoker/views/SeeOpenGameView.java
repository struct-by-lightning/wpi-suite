package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.MainViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.SeeOpenGameViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview.SubmitPane;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class SeeOpenGameView {
	
	/**
	 * Static members.
	 */
	
	private static SeeOpenGameView singleInstance;
	
	private static SeeOpenGameView getSingleInstance() {
		if (SeeOpenGameView.singleInstance == null)
			SeeOpenGameView.singleInstance = new SeeOpenGameView();
		return SeeOpenGameView.singleInstance;
	}
	
	public static SeeOpenGameViewController getController() {
		return SeeOpenGameView.getSingleInstance().controller;
	}
	
	public static void update() {
		SeeOpenGameView.singleInstance.updateRequirements();
		SeeOpenGameView.singleInstance.initComponents();
		SeeOpenGameView.getController().setViewGamePanel(SeeOpenGameView.singleInstance.viewGamePanel);
	}
	
	/**
	 * Instance members.
	 */
 	
	private SeeOpenGameViewController controller;
	
	private SeeOpenGameView() {
		initRequirements();
		initComponents();
		this.controller = new SeeOpenGameViewController(this.viewGamePanel);
	}

	private void initComponents() {
		viewGamePanel = new JPanel();
		viewGamePanel.setMinimumSize(new Dimension(800, 600));
		viewGamePanel.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		viewGamePanel.add(splitPane, BorderLayout.CENTER);

		JTextPane txtpnLoggedInAs = new JTextPane();
		txtpnLoggedInAs.setText("Logged in as: "+ConfigManager.getConfig().getUserName());
		txtpnLoggedInAs.setFocusable(false);
		viewGamePanel.add(txtpnLoggedInAs, BorderLayout.SOUTH);

		/**
		 * panel that contains games in a tree structure
		 */
		JPanel requirements = new JPanel();
		requirements.setMinimumSize(new Dimension(150, 10));
		requirements.setBorder(new LineBorder(Color.LIGHT_GRAY));
		splitPane.setLeftComponent(requirements);

		updateRequirements();
		
		JList list = new JList();
		list.setPreferredSize(new Dimension(150, 10));
		list.setMinimumSize(new Dimension(150, 10));
		list.setMaximumSize(new Dimension(30000, 30000));
		DefaultListModel model = new DefaultListModel();
		for (Requirement r : reqList){ 
			model.addElement(r.getName());
		}
		requirements.setLayout(new GridLayout(0, 1, 0, 0));
		list.setModel(model);

		requirements.add(list);

		/**
		 * panel that contains the information for a particular game
		 */
		JPanel gameContainer = new JPanel();
		splitPane.setRightComponent(gameContainer);
		gameContainer.setLayout(new BorderLayout(0, 0));

		JPanel gameName = new JPanel();
		FlowLayout flowLayout = (FlowLayout) gameName.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		gameName.setBorder(new LineBorder(Color.LIGHT_GRAY));
		gameContainer.add(gameName, BorderLayout.NORTH);

		JLabel lblGameName = new JLabel("GAME_NAME");
		lblGameName.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		gameName.add(lblGameName, BorderLayout.WEST);

		JPanel infoContainer = new JPanel();
		gameContainer.add(infoContainer, BorderLayout.CENTER);
		infoContainer.setLayout(new GridLayout(2, 2, 5, 5));

		JPanel description = new JPanel();
		infoContainer.add(description);

		JLabel lblReqName = new JLabel("Requirement Name:");

		JLabel lblEstimate = new JLabel("Estimate this requirement:");

		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		GroupLayout glDescription = new GroupLayout(description);
		glDescription.setHorizontalGroup(
			glDescription.createParallelGroup(Alignment.LEADING)
				.addGroup(glDescription.createSequentialGroup()
					.addGroup(glDescription.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, glDescription.createSequentialGroup()
							.addContainerGap()
							.addComponent(textArea))
						.addGroup(Alignment.LEADING, glDescription.createSequentialGroup()
							.addGap(5)
							.addComponent(lblReqName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, glDescription.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblEstimate)))
					.addContainerGap(48, Short.MAX_VALUE))
		);
		glDescription.setVerticalGroup(
			glDescription.createParallelGroup(Alignment.LEADING)
				.addGroup(glDescription.createSequentialGroup()
					.addGap(5)
					.addGroup(glDescription.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReqName)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEstimate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textArea, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
					.addContainerGap())
		);
		description.setLayout(glDescription);

		JPanel estimate = new JPanel();
		infoContainer.add(estimate);
		SubmitPane submitPane = new SubmitPane(infoContainer);
	}

    private JPanel viewGamePanel;
	private JTextField estimateTextField;
	private DefaultMutableTreeNode unanswered;
	private JTextField textField;
	private List<Requirement> reqList = null;
	
	private void initRequirements() {
		GetRequirementsController.getInstance().retrieveRequirements();
		
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			
		}
	}
	
	private void updateRequirements() {
		reqList = new ArrayList<Requirement>();
		for(int id : MainViewController.activeGame.getRequirements()) {
			reqList.add(RequirementModel.getInstance().getRequirement(id));
		}
	}
}
