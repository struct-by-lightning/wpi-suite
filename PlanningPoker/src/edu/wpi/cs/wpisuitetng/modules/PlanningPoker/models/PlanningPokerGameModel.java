/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.AddPlanningPokerGameController;


/**List of PlanningPokerGames being pulled from the server
 * 
 *
 * @version $Revision: 1.0 $
 * @author justinhess
 */
@SuppressWarnings("serial")
public class PlanningPokerGameModel extends AbstractListModel{
	
	/**
	 * The list in which all the PlanningPokerGames for a single project are contained
	 */
	private List<PlanningPokerGame> PlanningPokerGames;
	
	//the static object to allow the PlanningPokerGame model to be 
	private static PlanningPokerGameModel instance; 

	/**
	 * Constructs an empty list of PlanningPokerGames for the project
	 */
	private PlanningPokerGameModel (){
		PlanningPokerGames = new ArrayList<PlanningPokerGame>();
	}
	
	/**
	
	 * @return the instance of the PlanningPokerGame model singleton. */
	public static PlanningPokerGameModel getInstance()
	{
		if(instance == null)
		{
			instance = new PlanningPokerGameModel();
		}
		
		return instance;
	}
	
	/**
	 * Adds a single PlanningPokerGame to the PlanningPokerGames of the project
	 * 
	 * @param newReq The PlanningPokerGame to be added to the list of PlanningPokerGames in the project
	 */
	public void addPlanningPokerGame(PlanningPokerGame newReq){
		// add the PlanningPokerGame
		PlanningPokerGames.add(newReq);
		try 
		{
			AddPlanningPokerGameController.getInstance().addPlanningPokerGame(newReq);
			//ViewEventController.getInstance().refreshTable();
			//ViewEventController.getInstance().refreshTree();
		}
		catch(Exception e)
		{
			
		}
	}
	/**
	 * Returns the PlanningPokerGame with the given ID
	 * 
	 * @param id The ID number of the PlanningPokerGame to be returned
	
	 * @return the PlanningPokerGame for the id or null if the PlanningPokerGame is not found */
	public PlanningPokerGame getPlanningPokerGame(String name)
	{
		PlanningPokerGame temp = null;
		// iterate through list of PlanningPokerGames until id is found
		for (int i=0; i < this.PlanningPokerGames.size(); i++){
			temp = PlanningPokerGames.get(i);
			if (temp.getID() == name){
				break;
			}
		}
		return temp;
	}
	/**
	 * Removes the PlanningPokerGame with the given ID
	 * 
	 * @param removeId The ID number of the PlanningPokerGame to be removed from the list of PlanningPokerGames in the project
	 */
	public void removePlanningPokerGame(String removeName){
		// iterate through list of PlanningPokerGames until name of project is found
		for (int i=0; i < this.PlanningPokerGames.size(); i++){
			if (PlanningPokerGames.get(i).getID() == removeName){
				// remove the id
				PlanningPokerGames.remove(i);
				break;
			}
		}
		try {
			//ViewEventController.getInstance().refreshTable();
			//ViewEventController.getInstance().refreshTree();
		}
		catch(Exception e) {}
	}

	/**
	 * Provides the number of elements in the list of PlanningPokerGames for the project. This
	 * function is called internally by the JList in NewPlanningPokerGamePanel. Returns elements
	 * in reverse order, so the newest PlanningPokerGame is returned first.
	 * 
	
	
	
	 * @return the number of PlanningPokerGames in the project * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize() * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return PlanningPokerGames.size();
	}

	/**
	 * This function takes an index and finds the PlanningPokerGame in the list of PlanningPokerGames
	 * for the project. Used internally by the JList in NewPlanningPokerGameModel.
	 * 
	 * @param index The index of the PlanningPokerGame to be returned
	
	
	
	 * @return the PlanningPokerGame associated with the provided index * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int) * @see javax.swing.ListModel#getElementAt(int)
	 */
	public PlanningPokerGame getElementAt(int index) {
		return PlanningPokerGames.get(PlanningPokerGames.size() - 1 - index);
	}

	/**
	 * Removes all PlanningPokerGames from this model
	 * 
	 * NOTE: One cannot simply construct a new instance of
	 * the model, because other classes in this module have
	 * references to it. Hence, we manually remove each PlanningPokerGame
	 * from the model.
	 */
	public void emptyModel() {
		int oldSize = getSize();
		Iterator<PlanningPokerGame> iterator = PlanningPokerGames.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
		try{
			//ViewEventController.getInstance().refreshTable();
			//ViewEventController.getInstance().refreshTree();
		}
		catch (Exception e) {}
	}
	
	/**
	 * Adds the given array of PlanningPokerGames to the list
	 * 
	 * @param PlanningPokerGames the array of PlanningPokerGames to add
	 */
	public void addPlanningPokerGames(PlanningPokerGame[] PlanningPokerGames) {
		for (int i = 0; i < PlanningPokerGames.length; i++) {
			this.PlanningPokerGames.add(PlanningPokerGames[i]);
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
		//ViewEventController.getInstance().refreshTable();
		//ViewEventController.getInstance().refreshTree();
	}

	/**
	 * Returns the list of the PlanningPokerGames
	
	 * @return the PlanningPokerGames held within the PlanningPokerGameModel. */
	public List<PlanningPokerGame> getPlanningPokerGames() {
		return PlanningPokerGames;
	}
	
}
