/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;


/**
 * This panel fills the main content area of the tab for this module. It
 * contains one inner JPanel, the BoardPanel.
 *
 * @author Batyrlan Nurbekov
 * @author Sam Mailand
 * @author Zachary Zapatka
 * @author Miguel Mora
 *
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class MainView extends JTabbedPane {
	private final boolean dragging = false;
	private final Image tabImage = null;
	private final Point currentMouseLocation = null;
	private final int draggedTabIndex = 0;
	private Component lastTab = null;
	private final JPopupMenu popup = new JPopupMenu();
	private final JMenuItem closeAll = new JMenuItem("Close All Tabs");
	private final JMenuItem closeOthers = new JMenuItem("Close Others");


	/**
	 * Method paintComponent is an internal component part of window builder
	 * @param g is referring to graphics
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Are we dragging?
		if(dragging && currentMouseLocation != null && tabImage != null) {
			// Draw the dragged tab
			g.drawImage(tabImage, currentMouseLocation.x, currentMouseLocation.y, this);
		}
	}


	/**
	 * Overridden insertTab function to allow tab to close.
	 *
	
	
	
	
	
	 * @param c Component
	 */
//	public void insertTab(String title, Icon icon, Component component,
//			String tip, int index) {
//		super.insertTab(title, icon, component, tip, index);
//		if (!(component instanceof OverviewPanel) && !(component instanceof IterationOverviewPanel)) {
//			setTabComponentAt(index, new ClosableTabComponent(this));
//		}
//	}


	/**
	 * Method setSelectedComponent allows you to select tabs
	 * i.e. Main view or New Game.
	 * @param c Component which is a tab
	 */
	@Override
	public void setSelectedComponent(Component c){
		lastTab = this.getSelectedComponent();
		super.setSelectedComponent(c);
	}

	/**
	 * Method removeTabAt will remove a tab.
	 * @param i is the location
	 */
	@Override
	public void removeTabAt(int i){
		super.removeTabAt(i);
		try{
			if (lastTab != null){
				setSelectedComponent(lastTab);}
		} catch (IllegalArgumentException e){}
	}

	/**
	 * Method getPopup opens a window that asks if you want to close a tab.
	
	 * @return JPopupMenu the popup window */
	public JPopupMenu getPopup() {
		return popup;
	}
	/**
	 * Method getCloseAll is an internal component part of window builder .
	
	 * @return JMenuItem */
	public JMenuItem getCloseAll() {
		return closeAll;
	}


	/**
	 * Method getCloseOthers is an internal component part of window builder .
	
	 * @return JMenuItem */
	public JMenuItem getCloseOthers() {
		return closeOthers;
	}
}
