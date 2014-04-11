package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.UpdatePlanningPokerGameController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.MainViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers.SeeOpenGameViewController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview.SubmitPane;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.Box;

public class SeeOpenGameView {
	
	/**
	 * Static members.
	 * @author friscis fmsanchez@wpi.edu
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
		
		final JList <Requirement> list = new JList<Requirement>();
		list.setPreferredSize(new Dimension(150, 10));
		list.setMinimumSize(new Dimension(150, 10));
		list.setMaximumSize(new Dimension(30000, 30000));
		DefaultListModel<Requirement> model = new DefaultListModel<Requirement>();
		for (Requirement r : reqList){ 
			model.addElement(r);
		}
		requirements.setLayout(new GridLayout(0, 1, 0, 0));
		list.setModel(model);

		
		SeeOpenGameViewController.setRequirementList(list);
		

		requirements.add(list);
		
		/**
		 * Temporary panel to add the buttons
		 */
		JPanel topPanel = new JPanel();

		/**
		 * panel that contains the information for a particular game
		 */
		JPanel gameContainer = new JPanel();
		splitPane.setRightComponent(gameContainer);
		gameContainer.setLayout(new BorderLayout(0, 0));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		JPanel gameName = new JPanel();
		FlowLayout flowLayout = (FlowLayout) gameName.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		gameName.setBorder(null);
		topPanel.add(gameName);

		final JButton startBtn = new JButton("Start Game");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainViewController.activeGame.setLive(true);
				startBtn.setText("Game Started");
				startBtn.setEnabled(false);
				UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(MainViewController.activeGame);
			}
		});
		
		
		
		final JButton endBtn = new JButton("End Voting");
		endBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainViewController.activeGame.setFinished(true);
				endBtn.setText("Voting Ended");
				endBtn.setEnabled(false);
				UpdatePlanningPokerGameController.getInstance().updatePlanningPokerGame(MainViewController.activeGame);
			}
		});
		
		if((MainViewController.activeGame.getModerator().equals(ConfigManager.getConfig().getUserName()))){
			topPanel.add(startBtn);
			if(MainViewController.activeGame.isLive()){
				startBtn.setText("Game Started");
				startBtn.setEnabled(false);
			}
			
			Component horizontalStrut = Box.createHorizontalStrut(20);
			horizontalStrut.setPreferredSize(new Dimension(10, 0));
			topPanel.add(horizontalStrut);
			topPanel.add(endBtn);
		}
		
		
		gameContainer.add(topPanel, BorderLayout.NORTH);

		JLabel lblGameName = new JLabel(MainViewController.activeGame.getGameName());
		lblGameName.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		gameName.add(lblGameName, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setPreferredSize(new Dimension(10, 0));
		topPanel.add(horizontalStrut_1);

		JPanel infoContainer = new JPanel();
		gameContainer.add(infoContainer, BorderLayout.CENTER);
		infoContainer.setLayout(new GridLayout(2, 2, 5, 5));

		JPanel description = new JPanel();
		infoContainer.add(description);

		JLabel lblReqName = new JLabel("Requirement Name:");

		JLabel lblEstimate = new JLabel("Estimate this requirement:");

		requirementNameText = new JTextField();
		requirementNameText.setEditable(false);
		requirementNameText.setColumns(10);

		final JTextArea requirementDescriptionText = new JTextArea();
		requirementDescriptionText.setEditable(false);
		GroupLayout glDescription = new GroupLayout(description);
		glDescription.setHorizontalGroup(
			glDescription.createParallelGroup(Alignment.LEADING)
				.addGroup(glDescription.createSequentialGroup()
					.addGroup(glDescription.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, glDescription.createSequentialGroup()
							.addContainerGap()
							.addComponent(requirementDescriptionText))
						.addGroup(Alignment.LEADING, glDescription.createSequentialGroup()
							.addGap(5)
							.addComponent(lblReqName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(requirementNameText, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE))
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
						.addComponent(requirementNameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEstimate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(requirementDescriptionText, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
					.addContainerGap())
		);
		description.setLayout(glDescription);

		JPanel estimate = new JPanel();
		infoContainer.add(estimate);
		submitPane = new SubmitPane(infoContainer);
		final JPanel containerClone = infoContainer;
		
		   list.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					requirementNameText.setText(list.getSelectedValue().getName());
					requirementDescriptionText.setText(list.getSelectedValue().getDescription());
					((SubmitPane)submitPane).refresh();//refresh the Submit Pane object when a new req is clicked
				}
	        });
	}

    private JPanel viewGamePanel;
	private JTextField estimateTextField;
	private DefaultMutableTreeNode unanswered;
	private JTextField requirementNameText;
	private List<Requirement> reqList = null;
	private JPanel submitPane;
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
