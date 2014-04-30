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

/**
 * @version $Revision: 1.0 $
 * @author friscis
 * @author swconley
 * @author mamora
 */
public class PlanningPokerFinalEstimateEntityManager implements
		EntityManager<PlanningPokerFinalEstimate> {
	Class<PlanningPokerFinalEstimate> ppg = PlanningPokerFinalEstimate.class;
	Data data;

	private static final Logger logger = Logger
			.getLogger(PlanningPokerEntityManager.class.getName());

	/**
	 * Constructor for PlanningPokerFinalEstimateEntityManager.
	 * @param data Data
	 */
	public PlanningPokerFinalEstimateEntityManager(Data data) {
		this.data = data;
	}
	
	/**
	 * Method makeEntity.
	 * @param s Session
	 * @param content String	
	 * @return PlanningPokerFinalEstimate 
	 * @throws BadRequestException 
	 * @throws ConflictException 
	 * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(Session, String) */
	@Override
	public PlanningPokerFinalEstimate makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		final PlanningPokerFinalEstimate p = PlanningPokerFinalEstimate.fromJSON(content);

		if (getEntity(s, p.getID())[0] == null) {
			save(s, p);
		} else {
			logger.log(Level.WARNING,
					"Conflict Exception during PlanningPokerFinalEstimateModel creation.");
			throw new ConflictException(
					"A PlanningPokerFinalEstimateModel with the given ID already exists. Entity String: "
							+ content);
		}

		return p;
	}
	/**
	 * Method getEntity.
	 * @param s Session
	 * @param id String
	 * @return PlanningPokerFinalEstimate[] * @throws NotFoundException 
	 * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(Session, String) */
	@Override
	public PlanningPokerFinalEstimate[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		final PlanningPokerFinalEstimate[] m = new PlanningPokerFinalEstimate[0];
		if(id.equals(""))
		{
			return getAll(s);
		}
		else
		{
			return data.retrieve(ppg, "id", id).toArray(m);
		}
	}

	/**
	 * Method getAll.
	 * @param s Session
	 * @return PlanningPokerFinalEstimate[] * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(Session) */
	@Override
	public PlanningPokerFinalEstimate[] getAll(Session s) throws WPISuiteException {
		PlanningPokerFinalEstimate[] ret = new PlanningPokerFinalEstimate[0];
		ret = data.retrieveAll(new PlanningPokerFinalEstimate(null, 0)).toArray(ret);
		return ret;
	}

	/**
	 * Method update.
	 * @param s Session
	 * @param content String
	 * @return PlanningPokerFinalEstimate * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(Session, String) */
	@Override
	public PlanningPokerFinalEstimate update(Session s, String content)
			throws WPISuiteException {
		final PlanningPokerFinalEstimate changes = PlanningPokerFinalEstimate.fromJSON(content);
		if(changes.getGameName() != null) {
			deleteEntity(s, changes.getID());
			data.save(changes);
			return changes;
		}
		// currently we don't have the ability to deal with updates on more than one entry
		else {
			return null;
		}
	}

	/**
	 * Method save.
	 * @param s Session
	 * @param model PlanningPokerFinalEstimate
	 * @throws WPISuiteException */
	@Override
	public void save(Session s, PlanningPokerFinalEstimate model)
			throws WPISuiteException {
		if(data.save(model))
		{
			logger.log(Level.FINE, "PlanningPokerFinalEstimateModel Saved :" + model);

			return ;
		}
		else
		{
			logger.log(Level.WARNING, "PlanningPokerFinalEstimateModel Save Failure!");
			throw new DatabaseException("Save failure for PlanningPokerFinalEstimateModel.");
		}
	}

	/**
	 * Method deleteEntity.
	 * @param s Session
	 * @param id String
	 * @return boolean * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(Session, String) */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		final List<Model> retrieval = data.retrieve(ppg, "id", id);
		for(Model p : retrieval)
			data.delete(p);
		return true;
	}

	/**
	 * Method advancedGet.
	 * @param s Session
	 * @param args String[]
	 * @return String * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(Session, String[]) */
	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method deleteAll.
	 * @param s Session
	 * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(Session) */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		logger.log(Level.INFO, "PlanningPokerEntityManager invoking DeleteAll...");
		data.deleteAll(new PlanningPokerFinalEstimate(null, 0));
	}

	/**
	 * Method Count.
	 * 
	 * @return int
	 * @throws WPISuiteException
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count()
	 */
	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Method advancedPut.
	 * @param s Session
	 * @param args String[]
	 * @param content String
	 * @return String 
	 * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPut(Session, String[], String) */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method advancedPost.
	 * @param s Session
	 * @param string String
	 * @param content String
	 * @return String * @throws WPISuiteException 
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPost(Session, String, String) */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}
}
