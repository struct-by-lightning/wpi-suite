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

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

/**
 * @author sfmailand
 *
 */
public class UserModel extends RegularAbstractModel<UserModel>{

	/**The email of the user*/
	private String userEmail;
	
	/**The username of the user*/
	private String userName;
	
	/**The SMS number of the user*/
	private String numberSMS;
	
	/**The Instant message information of the User*/
	private String instantMessage;
	

	public UserModel(String userEmail, String userName, String numberSMS, String instantMessage){
		super();
		this.userEmail = userEmail;
		this.userName = userName;
		this.numberSMS = numberSMS;
		this.instantMessage = instantMessage;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, UserModel.class);
	}
	
	/**
	 * Returns an instance of a User constructed using the given
	 * Requirement encoded as a JSON string
	 */
	
	public static UserModel fromJson(String json){
		final Gson parser = new Gson();
		return parser.fromJson(json, UserModel.class);
	}
	



	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	/**
	 * @return the numberSMS
	 */
	public String getNumberSMS() {
		return numberSMS;
	}

	/**
	 * @param numberSMS the numberSMS to set
	 */
	public void setNumberSMS(String numberSMS) {
		this.numberSMS = numberSMS;
	}

	/**
	 * @return the instantMessage
	 */
	public String getInstantMessage() {
		return instantMessage;
	}

	/**
	 * @param instantMessage the instantMessage to set
	 */
	public void setInstantMessage(String instantMessage) {
		this.instantMessage = instantMessage;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getID()
	 */
	@Override
	public String getID() {
		return userName;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#setID(java.lang.String)
	 */
	@Override
	public void setID(String toSet) {
		this.userName = toSet;
		
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getPrimaryKey()
	 */
	@Override
	public String getPrimaryKey() {
		return "username";
	}
	
	
	public String getUserName(){
		return userName;
	}
}
