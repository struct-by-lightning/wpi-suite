package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import javax.swing.*;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 
 * This class is the new/modify game window
 * It will be opened when the "New Game" or "Modify Game" button is pressed
 * @author sfmailand
 *
 */
public class GameSettingsWindow extends JPanel {
	private JTextField nameOfGameTextField;
	private JTable requiermentsToSelect;
	private JTable selectedRequirements;
	
	
	//To be used to make sure the date is printed in the proper format
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	Date date = new Date();
	
	JComboBox<Months> endMonth = new JComboBox<Months>();
	JComboBox<Integer> endDay = new JComboBox<Integer>();
	
	
	public GameSettingsWindow() {
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, endDay, 186, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, endDay, 368, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, endDay, 482, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.NORTH, endMonth, 154, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, endMonth, 368, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, endMonth, 482, SpringLayout.WEST, this);
		setLayout(springLayout);
		
		
		JLabel lblGameName = new JLabel("Session Name");
		springLayout.putConstraint(SpringLayout.NORTH, lblGameName, 96, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblGameName, 28, SpringLayout.WEST, this);
		add(lblGameName);
		
		
		
		nameOfGameTextField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, nameOfGameTextField, 90, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, nameOfGameTextField, 121, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, nameOfGameTextField, 235, SpringLayout.WEST, this);
		
		//Puts current date and time into poker session name
		nameOfGameTextField.setText(dateFormat.format(date));
		
		add(nameOfGameTextField);
		nameOfGameTextField.setColumns(10);
		
		JButton endGameButton = new JButton("END Game");
		springLayout.putConstraint(SpringLayout.NORTH, endGameButton, 90, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, endGameButton, 667, SpringLayout.WEST, this);
		add(endGameButton);
		
		JLabel lblSelectGameMode = new JLabel("Select Game Mode:");
		springLayout.putConstraint(SpringLayout.NORTH, lblSelectGameMode, 159, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblSelectGameMode, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblSelectGameMode, 116, SpringLayout.WEST, this);
		add(lblSelectGameMode);
		
		JComboBox<String> gameType = new JComboBox<String>();
		springLayout.putConstraint(SpringLayout.NORTH, gameType, 154, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, gameType, 121, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, gameType, 235, SpringLayout.WEST, this);
		gameType.setModel(new DefaultComboBoxModel<String>(new String[] {"Long Term", "Short Term"}));
		add(gameType);
		

		endMonth.setModel(new DefaultComboBoxModel<Months>(Months.values()));
		add(endMonth);
		


		endDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {}));
		set31Days();
		add(endDay);
		
		
		
		JLabel lblDeckType = new JLabel("Deck Type:");
		springLayout.putConstraint(SpringLayout.NORTH, lblDeckType, 223, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblDeckType, 46, SpringLayout.WEST, this);
		add(lblDeckType);
		
		JComboBox<String> deckType = new JComboBox<String>();
		springLayout.putConstraint(SpringLayout.NORTH, deckType, 218, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, deckType, 121, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, deckType, 235, SpringLayout.WEST, this);
		deckType.setModel(new DefaultComboBoxModel<String>(new String[] {"DEFAULT"}));
		add(deckType);
		
		JLabel lblSelectRequirements = new JLabel("Select Requirements");
		springLayout.putConstraint(SpringLayout.NORTH, lblSelectRequirements, 310, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblSelectRequirements, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblSelectRequirements, 116, SpringLayout.WEST, this);
		add(lblSelectRequirements);
		
		JLabel lblRequirementsToBe = new JLabel("Requirements to be Estimated");
		springLayout.putConstraint(SpringLayout.NORTH, lblRequirementsToBe, 310, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblRequirementsToBe, 391, SpringLayout.WEST, this);
		add(lblRequirementsToBe);
		
		requiermentsToSelect = new JTable();
		springLayout.putConstraint(SpringLayout.NORTH, requiermentsToSelect, 331, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, requiermentsToSelect, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, requiermentsToSelect, 570, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, requiermentsToSelect, 265, SpringLayout.WEST, this);
		add(requiermentsToSelect);
		
		selectedRequirements = new JTable();
		springLayout.putConstraint(SpringLayout.NORTH, selectedRequirements, 331, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, selectedRequirements, 368, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, selectedRequirements, 570, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, selectedRequirements, 632, SpringLayout.WEST, this);
		add(selectedRequirements);
		
		JButton btnAddReq = new JButton("Add");
		springLayout.putConstraint(SpringLayout.NORTH, btnAddReq, 447, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnAddReq, 270, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, btnAddReq, 363, SpringLayout.WEST, this);
		add(btnAddReq);
		
		JButton btnRemoveReq = new JButton("Remove");
		springLayout.putConstraint(SpringLayout.NORTH, btnRemoveReq, 481, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnRemoveReq, 270, SpringLayout.WEST, this);
		add(btnRemoveReq);
		
		JButton submitButton = new JButton("Submit");
		springLayout.putConstraint(SpringLayout.WEST, submitButton, 0, SpringLayout.WEST, endGameButton);
		springLayout.putConstraint(SpringLayout.SOUTH, submitButton, 0, SpringLayout.SOUTH, requiermentsToSelect);
		add(submitButton);
		
		
		
		endMonth.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				switch((Months)endMonth.getSelectedItem()){
				case JANUARY: set31Days();
								break;
				case FEBRUARY: set28Days();
								break;
				case MARCH: set31Days();
								break;
				case APRIL: set30Days();
								break;
				case MAY: set31Days();
								break;
				case JUNE: set30Days();
								break;
				case JULY: set31Days();
								break;
				case AUGUST: set31Days();
								break;
				case SEPTEMBER: set30Days();
								break;
				case OCTOBER: set31Days();
								break;
				case NOVEMBER: set30Days();
								break;
				case DECEMBER: set31Days();
								break;
				default: set31Days();
							  
				}
		    }
		});
		
		
		
	}
	
	public void set28Days(){
		endDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28}));
	}

	public void set30Days(){
		endDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30}));
	}
	
	public void set31Days(){
		endDay.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31}));
	}
}
