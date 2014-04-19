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

/**
 * @author sfmailand, rbkillea
 *
 */
public class UserEntityManager implements EntityManager<UserModel>{

	Class<UserModel> user = UserModel.class;
	Data data;
	
	private static final Logger logger = Logger.getLogger(UserEntityManager.class.getName());
	
	/**
	 * Constructor for UserEntityManager
	 * @param data Data
	 */
	
	public UserEntityManager(Data data){
		this.data = data;
	}
	
	
	/**
	 * Method makeEntity.
	 * @param s Session
	 * @param content String
	 * @return User * @throws BadRequestException * @throws ConflictException * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(Session, String) */

	@Override
	public UserModel makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		UserModel user;
		user = UserModel.fromJson(content);
		
		UserModel[] existing = getEntity(s, user.getID());
		if(existing.length == 0 || existing[0] == null){
			save(s,user);
		}
		else{
			logger.log(Level.WARNING, "Conflict Exception during User creation.");
			throw new ConflictException("A PlanningPokerGame with the given ID already exists. Entity String: " + content);
		}
		return user;
	}

	/**
	 * Method getEntity.
	 * @param s Session
	 * @param id String
	 * @return PlanningPokerGame[] * @throws NotFoundException * @throws WPISuiteException * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(Session, String) */
	@Override
	public UserModel[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		UserModel[] m = new UserModel[0];
		if(id.equals("")){
			return getAll(s);
		}
		else{
			UserModel [] rv = data.retrieve(user, "userName", id).toArray(m);
			return rv;
		}
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(edu.wpi.cs.wpisuitetng.Session)
	 */
	@Override
	public UserModel[] getAll(Session s) throws WPISuiteException {
		UserModel[] ret = new UserModel[0];
		ret = data.retrieveAll(new UserModel(null, null, null, null), s.getProject()).toArray(ret);
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public UserModel update(Session s, String content) throws WPISuiteException {
		UserModel changes = UserModel.fromJson(content);
		if(true){
			System.out.println("Started update.");
			deleteEntity(s, changes.getID());
			data.save(changes);
			System.out.println("Finished update.");
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
	public void save(Session s, UserModel model) throws WPISuiteException {
		if(data.save(model)){
			logger.log(Level.FINE, "User Saved: " + model);
			
			return ;
		}
		else{
			logger.log(Level.WARNING, "User Save Failure!");
			throw new DatabaseException("Save fair for User");
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO Auto-generated method stub
		return false;
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
		logger.log(Level.INFO, "UserEntity invoking DeleteAll...");
		data.deleteAll(new UserModel(null,null,null,null), s.getProject());
		
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
