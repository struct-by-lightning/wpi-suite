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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

/**
 * A model to store the information for a planning poker game
 * @author sfmailand, rbkillea
 * @version $Revision: 1.0 $
 */
public class User extends RegularAbstractModel<User> {
	
	private String email;
	
	private String userName;
	
	private String sms;
	
	private String instantMessage;

	
	
	public User(String email, String userName, String sms, String instantMessage) {
		super();
		this.email = email;
		this.userName = userName;
		this.sms = sms;
		this.instantMessage = instantMessage;
	}

	public String toJSON(){
		return new Gson().toJson(this, User.class);
	}

	public static User fromJSON(String json){
		final Gson parser = new Gson();
		return parser.fromJson(json, User.class);
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
	public void setID(String userName) {
		this.userName = userName;
		
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel#getPrimaryKey()
	 */
	@Override
	public String getPrimaryKey() {
		return "username";
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the sms
	 */
	public String getSms() {
		return sms;
	}

	/**
	 * @param sms the sms to set
	 */
	public void setSms(String sms) {
		this.sms = sms;
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

	/**
	 * Description
	 * @param body
	 * @return
	 */
	public static User[] fromJSONArray(String jsonArr) {
		JsonArray array = new JsonParser().parse(jsonArr).getAsJsonArray();
		List<User> users = new ArrayList<User>();

		for (JsonElement json : array) {
			users.add(User.fromJSON(json.toString()));
		}

		return users.toArray(new User[0]);
	}

}
