/**
 *  * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 */
package edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.buttons;

/**
 *
 */

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.buttons.Exporter;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.overview.OverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.overview.OverviewTable;

/**
 * @author zjzapatka
 */
public class ExportButtonPanel extends ToolbarGroupView{
	
	private final JPanel contentPanel = new JPanel();
	
	JButton export;
	
	DefaultListModel<Requirement> selected = new DefaultListModel<Requirement>();
	
	public ExportButtonPanel(){
		super("");
		
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		
		export = new JButton("<html>Export<br />Requirements</html>");
		
		for(int i: OverviewPanel.getSelectedValues()){
			selected.addElement(RequirementModel.getInstance().getRequirement(i));
		}
		
		//selected = (DefaultListModel<Requirement>) OverviewTable.getRequirements();
		
		export.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Create a file chooser
				final JFileChooser fc = new JFileChooser();
				//In response to a button click:
				int returnVal = fc.showSaveDialog(contentPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Create exporter
					Exporter ex = new Exporter();
					// Export requirements
					ex.exportAsJSON(selected, fc.getSelectedFile().getAbsolutePath());
					System.out.println("Exported all selected requirements\n");
				}
			}
		});
		
		/*
		try {
		    Image img = ImageIO.read(getClass().getResource("export.png"));
		    export.setIcon(new ImageIcon(img));
		    
		} catch (IOException ex) {}
		*/
		contentPanel.add(export);
		contentPanel.setOpaque(false);
		
		this.add(contentPanel);
	}

	/**
	 * Method getExportButton.
	 * @return JButton
	 */
	public JButton getExportButton() {
		return export;
	}
	
}
