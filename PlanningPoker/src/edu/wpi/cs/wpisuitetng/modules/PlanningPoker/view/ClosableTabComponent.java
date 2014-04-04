/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Struct-By-Lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This class provides the closable tab component with the close button to the left of the tab title
 * @author bnurbekov
 */
@SuppressWarnings("serial")
public class ClosableTabComponent extends JPanel implements ActionListener {
	
	private final JTabbedPane tabbedPane;
	
	/**
	 * Create a closable tab component belonging to the given tabbedPane.
	 * The title is extracted with {@link JTabbedPane#getTitleAt(int)}.
	 * @param tabbedPane  The JTabbedPane this tab component belongs to
	 */
	public ClosableTabComponent(JTabbedPane tabbedPane) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.tabbedPane = tabbedPane;
		setOpaque(false);
		
		//create the label for the tab component 
		final JLabel label = new JLabel() {
			// display the title according to what's set on our JTabbedPane
			@Override
			public String getText() {
				final JTabbedPane tabbedPane = ClosableTabComponent.this.tabbedPane;
				final int index = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
				return index > -1 ? tabbedPane.getTitleAt(index) : "";
			}
		};
		label.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 7));
		add(label);
		
		//create close button
		final JButton closeButton = new JButton("\u2716");
		
		//specify settings for close button 
		closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.addActionListener(this);
		
		//add button to the tab component (not the JTabbedPane itself!)
		add(closeButton);
	}

	/**
	 * Removes tab at the selected index on action event
	 * @param e ActionEvent The event that occurred 
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent) 
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
		// close this tab when close button is clicked
		final int index = tabbedPane.indexOfTabComponent(this);
		if(index > -1) { 
			if (((NewGameTab) (tabbedPane.getComponentAt(index))).isTabEditedByUser){
				// Confirmation dialog
				/*int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(this, "Close this game session?", "Confirmation",dialogButton);
				*/
				 JOptionPane pane = new JOptionPane("Close this planning poker tab?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
				 JDialog d = pane.createDialog(null); 
				 d.setLocation(tabbedPane.getWidth() / 2, tabbedPane.getHeight() / 2);
				 d.setVisible(true);
				 
				 System.out.println("Dialog was closed with button " + pane.getValue());
				 if((int) pane.getValue()==0) { //remove tab at the specified index
					tabbedPane.removeTabAt(index);
				 }
				 d.dispose();
			}
			else {
				tabbedPane.removeTabAt(index);
			}
		}
	}
	
}
