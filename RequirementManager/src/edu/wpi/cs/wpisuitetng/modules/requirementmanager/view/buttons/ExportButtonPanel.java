/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.buttons;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.Transaction;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.characteristics.TransactionHistory;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.Exporter;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.Importer;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.ViewEventController;

/**
 * @author Benjamin
 *
 */
public class ExportButtonPanel extends ToolbarGroupView {
	// initialize the main view toolbar buttons	
	private final static JButton exportButton = new JButton("<html>Export Requirements</html>");
	private final JButton importButton = new JButton("<html>Import Requirements</html>");
	private final JPanel contentPanel = new JPanel();
		
		public ExportButtonPanel(){
			super("");
			
			this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
			this.setPreferredWidth(350);
			
			//this.createIterationButton.setSize(200, 200);
			//this.createButton.setPreferredSize(new Dimension(200, 200));
			this.exportButton.setHorizontalAlignment(SwingConstants.CENTER);
			this.importButton.setHorizontalAlignment(SwingConstants.CENTER);
			this.exportButton.setEnabled(false);
			
			/**
			 * Exports the list of selected requirements to a file when btnExport is
			 * pressed
			 */
			exportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Create default list model
					DefaultListModel lm = new DefaultListModel();
//					Requirement req = RequirementModel.getInstance().getRequirements().get(i);	
					for (int i = 0; i < ViewEventController.getInstance().getOverviewTable().getSelectedRows().length; i++) {
						lm.addElement((Requirement)ViewEventController.getInstance().getOverviewTable().getValueAt(ViewEventController.getInstance().getOverviewTable().getSelectedRows()[i], 1));
					}
					if(lm.getSize() == 0) {
						return;
					}
					//lm.addElement((Requirement)ViewEventController.getInstance().getOverviewTable().getValueAt(ViewEventController.getInstance().getOverviewTable().getSelectedRow(),1));
					//Create a file chooser
					final JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new FileNameExtensionFilter("JSON file", "json"));
					//In response to a button click:
					int returnVal = fc.showSaveDialog(contentPanel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String savePath = fc.getSelectedFile().getAbsolutePath();
						if(!savePath.endsWith(".json")) {
							savePath += ".json";
						}
						// Create exporter
						Exporter ex = new Exporter();
						// Export requirements
						ex.exportAsJSON(lm, savePath);
						System.out.println("Exported all selected requirements\n");
					}
				}
			});
			
			try {
			    Image img = ImageIO.read(getClass().getResource("export.png"));
			    importButton.setIcon(new ImageIcon(img));
			    importButton.setToolTipText("Import requirements from a file");
			} catch (IOException ex) {}
			
			/**
			 * Import the list of selected requirements to a file when importButton is
			 * pressed
			 */
			importButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Create default list model
					String str;
					//Create a file chooser
					final JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new FileNameExtensionFilter("JSON file", "json"));
					//In response to a button click:
					int returnVal = fc.showOpenDialog(contentPanel);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						// Create importer
						Importer im = new Importer();
						// Import requirements
						try {
							str = im.importFromJSON(fc.getSelectedFile().getAbsolutePath());
							// Add the requirements
							Requirement[] req = Requirement.fromJsonArray(str);
							for(int i = 0; i < req.length; i++) {
								req[i].setId(ViewEventController.getInstance().getOverviewTable().getRowCount() + i);
								// Prepare the transaction history
								TransactionHistory history = req[i].getHistory();
								Transaction t = new Transaction(ConfigManager.getConfig().getUserName(), System.currentTimeMillis(), "Requirement imported");
								int j = 0;
								while(history.getIterator(j).hasNext()) {
									j++;
								}
								history.getIterator(j).add(t);
								req[i].setHistory(history);
								// Set the status to new
								req[i].setStatus(RequirementStatus.NEW);
								// Set the priority
								req[i].setPriority(RequirementPriority.BLANK);
								// Set the release number
								req[i].setRelease("");
								// Set the estimate
								req[i].setEstimate(0);
								// Put it in the backlog
								req[i].setIteration("Backlog");
								// Add the requirement
								AddRequirementController.getInstance().addRequirement(req[i]);
								try {
									Thread.sleep(150);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							// Because why not
							try {
								Thread.sleep(150);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							GetRequirementsController.getInstance().retrieveRequirements();
							try {
								Thread.sleep(150);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							ViewEventController.getInstance().refreshTree();
							ViewEventController.getInstance().refreshTable();
							//RequirementModel.getInstance().setRequirements(GetRequirementsController.getInstance().);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println("Imported all selected requirements");
					}
				}
			});
			
			try {
			    Image img = ImageIO.read(getClass().getResource("import.png"));
			    exportButton.setIcon(new ImageIcon(img));	
			    exportButton.setToolTipText("Select requirements to export them to a file");
			} catch (IOException ex) {}
			
			contentPanel.add(importButton);
			contentPanel.add(exportButton);
			contentPanel.setOpaque(false);
			
			this.add(contentPanel);
		}
		
		/**
		 * Method getExportButton.
		
		 * @return JButton Returns the export button
		 */
		public static JButton getExportButton() {
			return exportButton;
		}

		/**
		 * Enable / Disable the export button
		 *
		 * @param input true: enable the button, false: disable the button
		 */
		public static void setExportButtonEnabled(boolean input) {
			exportButton.setEnabled(input);
		}
		
		/**
		 * Method getImportButton.
		
		 * @return JButton Returns the export button
		 */
		public JButton getImportButton() {
			return importButton;
		}
}