package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
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
public class MainView extends JPanel {

    /**
     * Construct the panel.
     */
    public MainView() {
    	setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    	setLayout(null);
    	
    	JList list = new JList();
    	list.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	list.setBounds(62, 60, 194, 439);
    	add(list);
    	
    	JList list_1 = new JList();
    	list_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	list_1.setBounds(317, 60, 154, 439);
    	add(list_1);
    	
    	JLabel lblUserStories = new JLabel("User Stories");
    	lblUserStories.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblUserStories.setBounds(100, 24, 119, 25);
    	add(lblUserStories);
    	
    	JLabel lblUsers = new JLabel("Users");
    	lblUsers.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblUsers.setBounds(364, 24, 60, 25);
    	add(lblUsers);
    	
    	JLabel lblStorySummary = new JLabel("Story Summary");
    	lblStorySummary.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblStorySummary.setBounds(562, 24, 137, 25);
    	add(lblStorySummary);
    	
    	JTextArea textArea = new JTextArea();
    	textArea.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	textArea.setEditable(false);
    	textArea.setBounds(511, 61, 239, 132);
    	add(textArea);
    	
    	JLabel lblEstimate = new JLabel("Estimate");
    	lblEstimate.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblEstimate.setBounds(511, 223, 89, 25);
    	add(lblEstimate);
    	
    	JComboBox comboBox = new JComboBox();
    	comboBox.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "5", "8", "13", "20", "40", "100"}));
    	comboBox.setBounds(511, 259, 77, 20);
    	add(comboBox);
    	
    	JButton btnSubmit = new JButton("Submit");
    	btnSubmit.setBounds(623, 227, 127, 52);
    	add(btnSubmit);
    	
    	JLabel lblStatistics = new JLabel("Statistics");
    	lblStatistics.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblStatistics.setBounds(586, 301, 89, 25);
    	add(lblStatistics);
    	
    	JTextArea textArea_1 = new JTextArea();
    	textArea_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	textArea_1.setEditable(false);
    	textArea_1.setBounds(511, 338, 239, 161);
    	add(textArea_1);
    }
}