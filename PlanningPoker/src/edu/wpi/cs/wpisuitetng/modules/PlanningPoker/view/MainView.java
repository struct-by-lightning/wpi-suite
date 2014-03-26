package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
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

import edu.wpi.cs.wpisuitetng.modules.core.models.PlanningPokerTestModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

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
	
	JLabel lblUserStories, lblUsers, lblStorySummary;
	
	JTextArea textArea;
	
	JLabel lblEstimate;
	
	JComboBox comboBox;
	
	JLabel lblStatistics;
	
	JTextArea statistics_text;
	
	JButton btnSubmit;
	private JTable userStoryTable;
	private JTable usersTable;
    public MainView() {
    	setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
    	setLayout(null);
    	
    	lblUserStories = new JLabel("User Stories");
    	lblUserStories.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblUserStories.setBounds(100, 24, 119, 25);
    	add(lblUserStories);
    	
    	lblUsers = new JLabel("Users");
    	lblUsers.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblUsers.setBounds(364, 24, 60, 25);
    	add(lblUsers);
    	
    	lblStorySummary = new JLabel("Story Summary");
    	lblStorySummary.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblStorySummary.setBounds(562, 24, 137, 25);
    	add(lblStorySummary);
    	
    	textArea = new JTextArea();
    	textArea.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	textArea.setEditable(false);
    	textArea.setBounds(511, 61, 239, 132);
    	add(textArea);
    	
    	lblEstimate = new JLabel("Estimate");
    	lblEstimate.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblEstimate.setBounds(511, 223, 89, 25);
    	add(lblEstimate);
    	
    	//Changed to final because I couldn't grab the data otherwise
    	comboBox = new JComboBox();
    	comboBox.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "5", "8", "13", "20", "40", "100"}));
    	comboBox.setBounds(511, 259, 77, 20);
    	add(comboBox);
    	
    	btnSubmit = new JButton("Submit");
    	btnSubmit.setBounds(623, 227, 127, 52);
    	btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlanningPokerTestModel message = new PlanningPokerTestModel("Test Data");
				
				Request request = Network.getInstance().makeRequest("planningpoker/planningpokertestmodel", HttpMethod.PUT);
				request.setBody(message.toJSON());
				request.addObserver(new RequestObserver() {

					@Override
					public void responseSuccess(IRequest iReq) {
						JOptionPane.showMessageDialog(new JFrame("Result..."), "Success!");
					}

					@Override
					public void responseError(IRequest iReq) {
						JOptionPane.showMessageDialog(new JFrame("Result..."), "Error!");
					}

					@Override
					public void fail(IRequest iReq, Exception exception) {
						JOptionPane.showMessageDialog(new JFrame("Result..."), "Fail!");
					}
					
				});
				request.send();
			}
    	});
    	add(btnSubmit);
    	
    	JButton btnGet = new JButton("Get");
    	btnGet.setBounds(823, 227, 127, 52);
    	btnGet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Request request = Network.getInstance().makeRequest("planningpoker/planningpokertestmodel", HttpMethod.GET);
				//request.setBody(message.toJSON());
				request.addObserver(new RequestObserver() {

					@Override
					public void responseSuccess(IRequest iReq) {
						JOptionPane.showMessageDialog(new JFrame("Result..."), "Success!\n\nResponse: "+iReq.getResponse().getBody());
					}

					@Override
					public void responseError(IRequest iReq) {
						JOptionPane.showMessageDialog(new JFrame("Result..."), "Error!");
					}

					@Override
					public void fail(IRequest iReq, Exception exception) {
						JOptionPane.showMessageDialog(new JFrame("Result..."), "Fail!");
					}
					
				});
				request.send();
			}
    	});
    	add(btnGet);
    	
    	lblStatistics = new JLabel("Statistics");
    	lblStatistics.setFont(new Font("Arial", Font.PLAIN, 20));
    	lblStatistics.setBounds(586, 301, 89, 25);
    	add(lblStatistics);
    	
    	statistics_text = new JTextArea();
    	statistics_text.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
    	statistics_text.setEditable(false);
    	statistics_text.setBounds(511, 338, 239, 161);
    	add(statistics_text);
    	
    	userStoryTable = new JTable();
    	userStoryTable.setBounds(32, 60, 251, 439);
    	add(userStoryTable);
    	
    	usersTable = new JTable();
    	usersTable.setBounds(330, 61, 137, 439);
    	add(usersTable);
    	
    	
    	
    	//When the "Submit button is pushed"
    	btnSubmit.addActionListener(new ActionListener() {
    		  public void actionPerformed(ActionEvent evt) {
    			//Grabs the selected answer from the drop down and prints them out in the console
    		    String selectedValue = (String)comboBox.getSelectedItem();
    		    System.out.println("SELECTED: " + selectedValue);
    		    statistics_text.setText("");
    		    statistics_text.append(selectedValue);
    		  }
    		});
    }
}