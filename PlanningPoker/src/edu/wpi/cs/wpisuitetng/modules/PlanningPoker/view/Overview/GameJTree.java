/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor: team struct-by-lightning
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview;

import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;

public class GameJTree extends JTree {

	private DefaultTreeModel model;

	public GameJTree(DefaultTreeModel model) {
		super(model);
		this.model = model;

		this.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent event) {
				refresh(event);
			}

			public void ancestorMoved(AncestorEvent event) {
				refresh(event);
			}

			public void ancestorRemoved(AncestorEvent event) {
				refresh(event);
			}

			public void refresh(AncestorEvent event) {
				GameJTree tree = (GameJTree) event.getComponent();
				tree.fireRefresh();
			}
		});

		this.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent event) {
				JTree tree = (JTree) event.getSource();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				String gameName = (String) selectedNode.getUserObject();

				PlanningPokerGame game = PlanningPokerGameModel
						.getPlanningPokerGame(gameName);
			}
		});
	}

	public void fireRefresh() {

		GetPlanningPokerGamesController.getInstance()
				.retrievePlanningPokerGames();

		DefaultMutableTreeNode allGames = new DefaultMutableTreeNode("All");
		DefaultMutableTreeNode newGames = new DefaultMutableTreeNode("New");
		DefaultMutableTreeNode openGames = new DefaultMutableTreeNode("Open");
		DefaultMutableTreeNode finishedGames = new DefaultMutableTreeNode(
				"Finished");

		for (PlanningPokerGame game : PlanningPokerGameModel
				.getPlanningPokerGames()) {
			DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(
					game.getGameName());

			// Has the game started voting?
			if (game.isLive()) {
				openGames.add(nodeToAdd);
				break;
			}

			// Has the game ended?
			if (game.isFinished()) {
				finishedGames.add(nodeToAdd);
				break;
			}

			// The game must be new.
			newGames.add(nodeToAdd);
		}

		if (!newGames.isLeaf()) {
			allGames.add(newGames);
		}

		if (!openGames.isLeaf()) {
			allGames.add(openGames);
		}

		if (!finishedGames.isLeaf()) {
			allGames.add(finishedGames);
		}

		this.model.setRoot(allGames);

		for (int i = 0; i < this.getRowCount(); i++) {
			this.expandRow(i);
		}

		this.getParent().setMinimumSize(
				new Dimension(this.getWidth() + 60, this.getParent()
						.getMinimumSize().height));
		;
	}
}
