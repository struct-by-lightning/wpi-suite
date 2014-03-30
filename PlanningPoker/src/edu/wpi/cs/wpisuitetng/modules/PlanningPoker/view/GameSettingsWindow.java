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
	
	public GameSettingsWindow() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{98, 127, 0, 0, 53, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblGameName = new JLabel("DEFAULT NAME");
		GridBagConstraints gbc_lblGameName = new GridBagConstraints();
		gbc_lblGameName.anchor = GridBagConstraints.EAST;
		gbc_lblGameName.insets = new Insets(0, 0, 5, 5);
		gbc_lblGameName.gridx = 0;
		gbc_lblGameName.gridy = 3;
		add(lblGameName, gbc_lblGameName);
		
		nameOfGameTextField = new JTextField();
		GridBagConstraints gbc_nameOfGameTextField = new GridBagConstraints();
		gbc_nameOfGameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameOfGameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_nameOfGameTextField.gridx = 1;
		gbc_nameOfGameTextField.gridy = 3;
		add(nameOfGameTextField, gbc_nameOfGameTextField);
		nameOfGameTextField.setColumns(10);
		
		JButton endGameButton = new JButton("END Game");
		GridBagConstraints gbc_endGameButton = new GridBagConstraints();
		gbc_endGameButton.insets = new Insets(0, 0, 5, 0);
		gbc_endGameButton.gridx = 13;
		gbc_endGameButton.gridy = 3;
		add(endGameButton, gbc_endGameButton);
		
		JLabel lblSelectGameMode = new JLabel("Select Game Mode:");
		GridBagConstraints gbc_lblSelectGameMode = new GridBagConstraints();
		gbc_lblSelectGameMode.anchor = GridBagConstraints.EAST;
		gbc_lblSelectGameMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectGameMode.gridx = 0;
		gbc_lblSelectGameMode.gridy = 5;
		add(lblSelectGameMode, gbc_lblSelectGameMode);
		
		JComboBox<String> gameType = new JComboBox<String>();
		gameType.setModel(new DefaultComboBoxModel<String>(new String[] {"Long Term", "Short Term"}));
		GridBagConstraints gbc_gameType = new GridBagConstraints();
		gbc_gameType.fill = GridBagConstraints.HORIZONTAL;
		gbc_gameType.insets = new Insets(0, 0, 5, 5);
		gbc_gameType.gridx = 1;
		gbc_gameType.gridy = 5;
		add(gameType, gbc_gameType);
		
		JLabel lblDeckType = new JLabel("Deck Type:");
		GridBagConstraints gbc_lblDeckType = new GridBagConstraints();
		gbc_lblDeckType.insets = new Insets(0, 0, 5, 5);
		gbc_lblDeckType.anchor = GridBagConstraints.EAST;
		gbc_lblDeckType.gridx = 0;
		gbc_lblDeckType.gridy = 7;
		add(lblDeckType, gbc_lblDeckType);
		
		JComboBox<String> deckType = new JComboBox<String>();
		deckType.setModel(new DefaultComboBoxModel<String>(new String[] {"DEFAULT"}));
		GridBagConstraints gbc_deckType = new GridBagConstraints();
		gbc_deckType.insets = new Insets(0, 0, 5, 5);
		gbc_deckType.fill = GridBagConstraints.HORIZONTAL;
		gbc_deckType.gridx = 1;
		gbc_deckType.gridy = 7;
		add(deckType, gbc_deckType);
		
		JLabel lblSelectRequirements = new JLabel("Select Requirements");
		GridBagConstraints gbc_lblSelectRequirements = new GridBagConstraints();
		gbc_lblSelectRequirements.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectRequirements.gridx = 0;
		gbc_lblSelectRequirements.gridy = 10;
		add(lblSelectRequirements, gbc_lblSelectRequirements);
		
		JLabel lblRequirementsToBe = new JLabel("Requirements to be Estimated");
		GridBagConstraints gbc_lblRequirementsToBe = new GridBagConstraints();
		gbc_lblRequirementsToBe.gridwidth = 7;
		gbc_lblRequirementsToBe.insets = new Insets(0, 0, 5, 5);
		gbc_lblRequirementsToBe.gridx = 4;
		gbc_lblRequirementsToBe.gridy = 10;
		add(lblRequirementsToBe, gbc_lblRequirementsToBe);
		
		requiermentsToSelect = new JTable();
		GridBagConstraints gbc_requiermentsToSelect = new GridBagConstraints();
		gbc_requiermentsToSelect.gridheight = 5;
		gbc_requiermentsToSelect.gridwidth = 3;
		gbc_requiermentsToSelect.insets = new Insets(0, 0, 5, 5);
		gbc_requiermentsToSelect.fill = GridBagConstraints.BOTH;
		gbc_requiermentsToSelect.gridx = 0;
		gbc_requiermentsToSelect.gridy = 11;
		add(requiermentsToSelect, gbc_requiermentsToSelect);
		
		selectedRequirements = new JTable();
		GridBagConstraints gbc_selectedRequirements = new GridBagConstraints();
		gbc_selectedRequirements.gridheight = 5;
		gbc_selectedRequirements.gridwidth = 8;
		gbc_selectedRequirements.insets = new Insets(0, 0, 5, 5);
		gbc_selectedRequirements.fill = GridBagConstraints.BOTH;
		gbc_selectedRequirements.gridx = 4;
		gbc_selectedRequirements.gridy = 11;
		add(selectedRequirements, gbc_selectedRequirements);
		
		JButton btnAddReq = new JButton("Add");
		GridBagConstraints gbc_btnAddReq = new GridBagConstraints();
		gbc_btnAddReq.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddReq.gridx = 3;
		gbc_btnAddReq.gridy = 12;
		add(btnAddReq, gbc_btnAddReq);
		
		JButton submitButton = new JButton("Submit");
		GridBagConstraints gbc_submitButton = new GridBagConstraints();
		gbc_submitButton.gridx = 13;
		gbc_submitButton.gridy = 16;
		add(submitButton, gbc_submitButton);
		
		
	}
}
