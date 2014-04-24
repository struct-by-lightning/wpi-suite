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
 * @author sfmailand
 *
 */
public class PlanningPokerUserEntityManager implements EntityManager<PlanningPokerUser> {
	
	Class<PlanningPokerUser> usr = PlanningPokerUser.class;
	Data data;

	
	private static final Logger logger = Logger.getLogger(PlanningPokerUserEntityManager.class.getName());
	
	
	public PlanningPokerUserEntityManager(Data data){
		this.data = data;
	}
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public PlanningPokerUser makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		PlanningPokerUser u = PlanningPokerUser.fromJSON(content);
		

		PlanningPokerUser[] user = getEntity(s, u.getID());
		
		if(user.length == 0 || user[0] == null){
			save(s,u);
		}
		else{
			logger.log(Level.WARNING, "Conflict Exception during PlanningPokerUser creation.");
			throw new ConflictException("A PlanningPokerUser with the given ID already exists. Entity String "  + content);
		}
		
		return u;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public PlanningPokerUser[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
		PlanningPokerUser[] m = new PlanningPokerUser[0];
		
		if(id.equals("")){
			return getAll(s);
		}
		else{
			return data.retrieve(usr, "username", id).toArray(m);
		}
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(edu.wpi.cs.wpisuitetng.Session)
	 */
	@Override
	public PlanningPokerUser[] getAll(Session s) throws WPISuiteException {
		PlanningPokerUser[] ret = new PlanningPokerUser[0];
		ret = data.retrieveAll(new PlanningPokerUser(null, null, null, false, false, false)).toArray(ret);
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public PlanningPokerUser update(Session s, String content) throws WPISuiteException {
		PlanningPokerUser changes = PlanningPokerUser.fromJSON(content);
		
		if(true){
			System.out.println("Started update");
			deleteEntity(s, changes.getID());
			data.save(changes);
			System.out.println("Finsihed update");
			return changes;
		}
		else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng.Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, PlanningPokerUser model) throws WPISuiteException {
		if(data.save(model)){
			logger.log(Level.FINE, "PlanningPokerUser Saved: " + model);
			return;
		}
		else{
			logger.log(Level.WARNING, "PlanningPokerUser save Failure!");
			throw new DatabaseException("Save failure for user");
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		Model m = data.delete(data.retrieve(usr, "username", id).get(0));
		logger.log(Level.INFO, "PlanningPokerUserEntityManager deleting deck < " + id + ">");
		return (m !=null) ? true: false;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(edu.wpi.cs.wpisuitetng.Session, java.lang.String[])
	 */
	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(edu.wpi.cs.wpisuitetng.Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		logger.log(Level.INFO, "PlanningPokerUserEntityManager involking DeleteAll...");
		data.deleteAll(new PlanningPokerUser(null, null, null, false, false, false));
		
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#Count()
	 */
	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPut(edu.wpi.cs.wpisuitetng.Session, java.lang.String[], java.lang.String)
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPost(edu.wpi.cs.wpisuitetng.Session, java.lang.String, java.lang.String)
	 */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
