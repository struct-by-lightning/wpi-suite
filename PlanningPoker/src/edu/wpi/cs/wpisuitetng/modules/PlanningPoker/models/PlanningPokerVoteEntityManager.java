/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.DatabaseException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

public class PlanningPokerVoteEntityManager implements EntityManager<PlanningPokerVoteModel> {

	Class<PlanningPokerVoteModel> ppg = PlanningPokerVoteModel.class;
	Data data;

	private static final Logger logger = Logger.getLogger(PlanningPokerEntityManager.class.getName());

	public PlanningPokerVoteEntityManager(Data data) {
		this.data = data;
	}
	
	@Override
	public PlanningPokerVoteModel makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		PlanningPokerVoteModel p = PlanningPokerVoteModel.fromJSON(content);

		if (getEntity(s, p.getID())[0] == null) {
			save(s, p);
		} else {
			logger.log(Level.WARNING, "Conflict Exception during PlanningPokerVoteModel creation.");
			throw new ConflictException("A PlanningPokerVoteModel with the given ID already exists. Entity String: " + content);
		}

		return p;
	}
	@Override
	public PlanningPokerVoteModel[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		PlanningPokerVoteModel[] m = new PlanningPokerVoteModel[1];
		if(id.equals(""))
		{
			return getAll(s);
		}
		else
		{
			return data.retrieve(ppg, "id", id).toArray(m);
		}
	}

	@Override
	public PlanningPokerVoteModel[] getAll(Session s) throws WPISuiteException {
		PlanningPokerVoteModel[] ret = new PlanningPokerVoteModel[1];
		ret = data.retrieveAll(new PlanningPokerVoteModel(null, null, 0)).toArray(ret);
		return ret;
	}

	@Override
	public PlanningPokerVoteModel update(Session s, String content)
			throws WPISuiteException {
		PlanningPokerVoteModel changes = PlanningPokerVoteModel.fromJSON(content);
		if(changes.gameName != null && changes.userName != null) {
			deleteEntity(s, changes.getID());
			data.save(changes);
			return changes;
		}
		// currently we don't have the ability to deal with updates on more than one entry
		else
			return null;
	}

	@Override
	public void save(Session s, PlanningPokerVoteModel model)
			throws WPISuiteException {
		if(data.save(model))
		{
			logger.log(Level.FINE, "PlanningPokerVoteModel Saved :" + model);

			return ;
		}
		else
		{
			logger.log(Level.WARNING, "PlanningPokerVoteModel Save Failure!");
			throw new DatabaseException("Save failure for PlanningPokerVoteModel.");
		}
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		List<Model> retrieval = data.retrieve(ppg, "id", id);
		for(Model p : retrieval)
			data.delete(p);
		return true;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		logger.log(Level.INFO, "PlanningPokerEntityManager invoking DeleteAll...");
		data.deleteAll(new PlanningPokerVoteModel(null, null, 0));
	}

	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}
}
