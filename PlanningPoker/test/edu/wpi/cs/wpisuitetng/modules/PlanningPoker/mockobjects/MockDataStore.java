/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: team struct-by-lightning
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.mockobjects;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * Description
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 4, 2014
 */
public class MockDataStore implements Data {

	private static MockDataStore instance = null;
	
	public static MockDataStore getInstance() {
		if (instance == null)
			instance = new MockDataStore();
		return instance;
	}
	
	private MockDataStore() {
		
	}
	
	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#save(java.lang.Object)
	 */
	@Override
	public <T> boolean save(T aModel) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#save(java.lang.Object,
	 *      edu.wpi.cs.wpisuitetng.modules.core.models.Project)
	 */
	@Override
	public <T> boolean save(T aModel, Project aProject) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#retrieve(java.lang.Class,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public List<Model> retrieve(Class anObjectQueried, String aFieldName,
			Object theGivenValue) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#retrieve(java.lang.Class,
	 *      java.lang.String, java.lang.Object,
	 *      edu.wpi.cs.wpisuitetng.modules.core.models.Project)
	 */
	@Override
	public List<Model> retrieve(Class anObjectQueried, String aFieldName,
			Object theGivenValue, Project theProject) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#delete(java.lang.Object)
	 */
	@Override
	public <T> T delete(T aTNG) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#update(java.lang.Class,
	 *      java.lang.String, java.lang.Object, java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void update(Class anObjectToBeModified, String fieldName,
			Object uniqueID, String changeField, Object changeValue)
			throws WPISuiteException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#retrieveAll(java.lang.Object)
	 */
	@Override
	public <T> List<T> retrieveAll(T aSample) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#retrieveAll(java.lang.Object,
	 *      edu.wpi.cs.wpisuitetng.modules.core.models.Project)
	 */
	@Override
	public <T> List<Model> retrieveAll(T aSample, Project aProject) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#deleteAll(java.lang.Object)
	 */
	@Override
	public <T> List<T> deleteAll(T aSample) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#deleteAll(java.lang.Object,
	 *      edu.wpi.cs.wpisuitetng.modules.core.models.Project)
	 */
	@Override
	public <T> List<Model> deleteAll(T aSample, Project aProject) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#andRetrieve(java.lang.Class,
	 *      java.lang.String[], java.util.List)
	 */
	@Override
	public List<Model> andRetrieve(Class anObjectQueried,
			String[] aFieldNameList, List<Object> theGivenValueList)
			throws WPISuiteException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#orRetrieve(java.lang.Class,
	 *      java.lang.String[], java.util.List)
	 */
	@Override
	public List<Model> orRetrieve(Class anObjectQueried,
			String[] aFieldNameList, List<Object> theGivenValueList)
			throws WPISuiteException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.database.Data#complexRetrieve(java.lang.Class,
	 *      java.lang.String[], java.util.List, java.lang.Class,
	 *      java.lang.String[], java.util.List)
	 */
	public List<Model> complexRetrieve(Class andanObjectQueried,
			String[] andFieldNameList, List<Object> andGivenValueList,
			Class orAnObjectQueried, String[] orFieldNameList,
			List<Object> orGivenValueList) throws WPISuiteException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return null;
	}

}
