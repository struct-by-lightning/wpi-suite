package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.PlanningPoker;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.ClosableTabComponent;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.CreateGameView;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.SeeOpenGameView;

public class MainViewController {
	
	private JToolBar toolbar;
	private JTabbedPane tabPane;
	private JTree gameTree;
	
	public MainViewController(JTree gameTree, JTabbedPane tabPane, JToolBar toolbar) {
		this.gameTree = gameTree;
		this.tabPane = tabPane;
		this.toolbar = toolbar;
		
		this.gameTree.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {refreshGameTree();}
			public void ancestorMoved(AncestorEvent event) {refreshGameTree();}
			public void ancestorRemoved(AncestorEvent event) {refreshGameTree();}
		});
		
		this.gameTree.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		        int selRow = getGameTree().getRowForLocation(e.getX(), e.getY());
		        TreePath selPath = getGameTree().getPathForLocation(e.getX(), e.getY());
		        if(selRow != -1) {
		        	if(e.getClickCount() == 2) {
		        		DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
		        		String gameName = (String)node.getUserObject();
		        		GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
		        		
		                gameWasDoubleClicked(PlanningPokerGameModel.getPlanningPokerGame(gameName));
		        	}
		        }
		    }
		});
	}
	
	public void activateView() {
		PlanningPoker.updateComponents(this.toolbar, this.tabPane);
	}
	
	public void addCloseableTab(String tabName, JPanel tabPanel) {
		this.tabPane.addTab(tabName, tabPanel);
		this.tabPane.setTabComponentAt(this.tabPane.indexOfComponent(tabPanel), new ClosableTabComponent(this.tabPane));			
		this.tabPane.setSelectedComponent(tabPanel);
	}
	
	public void createGameButtonClicked() {
		CreateGameView.getController().activateView(); 
	}

	public void refreshGameTree() {
		DefaultTreeModel model = (DefaultTreeModel) this.gameTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		DefaultMutableTreeNode newGames = new DefaultMutableTreeNode("New");
		DefaultMutableTreeNode openGames = new DefaultMutableTreeNode("Open");
		DefaultMutableTreeNode finishedGames = new DefaultMutableTreeNode("Finished");

		GetPlanningPokerGamesController.getInstance().retrievePlanningPokerGames();
		for (PlanningPokerGame game : PlanningPokerGameModel.getPlanningPokerGames()) {
			DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(game.getGameName());

			// Has the game started voting?
			if (game.isLive()) {
				openGames.add(nodeToAdd);
			}

			// Has the game ended?
			else if (game.isFinished()) {
				finishedGames.add(nodeToAdd);
			}

			else {
				// The game must be new.
				newGames.add(nodeToAdd);
			}

		}

		root.removeAllChildren();

		if (!newGames.isLeaf()) {
			root.add(newGames);
		}

		if (!openGames.isLeaf()) {
			root.add(openGames);
		}

		if (!finishedGames.isLeaf()) {
			root.add(finishedGames);
		}

		model.reload(root);

		for (int i = 0; i < this.gameTree.getRowCount(); i++) {
			this.gameTree.expandRow(i);
		}
	}

	public void gameWasDoubleClicked(PlanningPokerGame game) {
		SeeOpenGameView.getController().activateView(game);
		
		
	}
	
	private JTree getGameTree() {
		return this.gameTree;
	}

}
