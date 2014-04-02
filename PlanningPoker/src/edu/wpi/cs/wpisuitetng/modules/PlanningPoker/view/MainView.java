package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view.Overview.OverviewPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.view.iterations.IterationOverviewPanel;

/**
 * This panel fills the main content area of the tab for this module. It
 * contains one inner JPanel, the BoardPanel.
 * 
 * @author Batyrlan Nurbekov
 * @author Sam Mailand
 * @author Zachary Zapatka
 * @author Miguel Mora
 *
 */
@SuppressWarnings("serial")
public class MainView extends JTabbedPane {
	
	private boolean dragging = false;
	private Image tabImage = null;
	private Point currentMouseLocation = null;
	private int draggedTabIndex = 0;
	private OverviewPanel overview = new OverviewPanel();
	private NewGameTab newGame = new NewGameTab();
	private Component lastTab = null;
	private final JPopupMenu popup = new JPopupMenu();
	private JMenuItem closeAll = new JMenuItem("Close All Tabs");
	private JMenuItem closeOthers = new JMenuItem("Close Others");



	/**
	 * Adds main subtab when user goes to RequirementManager
	 */
	public MainView() {
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.addTab("Overview", overview);
		//this.addTab("Iteration Overview", newGame);
	}

		
		


	/**
	 * Method paintComponent.
	 * @param g Graphics
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
	 * Overridden insertTab function to add the closable tab element.
	 * 
	 * @param title	Title of the tab
	 * @param icon	Icon for the tab
	 * @param component	The tab
	 * @param tip	Showing mouse tip when hovering over tab
	 * @param index	Location of the tab
	 */
	@Override
	public void insertTab(String title, Icon icon, Component component,
			String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		if (!(component instanceof OverviewPanel) && !(component instanceof IterationOverviewPanel)) {
			setTabComponentAt(index, new ClosableTabComponent(this));
		}
	}
	
	/**
	 * Method getOverview.
	
	 * @return OverviewPanel */
	public OverviewPanel getOverview() {
		return overview;
	}
	
	/**
	 * Method setSelectedComponent.
	 * @param c Component
	 */
	@Override
	public void setSelectedComponent(Component c){
		this.lastTab = this.getSelectedComponent();
		super.setSelectedComponent(c);
	}

	/**
	 * Method removeTabAt.
	 * @param i int
	 */
	@Override
	public void removeTabAt(int i){
		super.removeTabAt(i);
		try{
			if (this.lastTab != null){
				setSelectedComponent(this.lastTab);}
		} catch (IllegalArgumentException e){}
	}
	
	/**
	 * Method getPopup.
	 * @return JPopupMenu
	 */
	public JPopupMenu getPopup() {
		return popup;
	}
	/**
	 * Method getCloseAll.
	 * @return JMenuItem
	 */
	public JMenuItem getCloseAll() {
		return closeAll;
	}


	/**
	 * Method getCloseOthers.
	 * @return JMenuItem
	 */
	public JMenuItem getCloseOthers() {
		return closeOthers;
	}
}