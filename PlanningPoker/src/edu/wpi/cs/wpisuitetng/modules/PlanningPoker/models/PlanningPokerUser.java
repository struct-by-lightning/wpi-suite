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
public class PlanningPokerUser extends RegularAbstractModel<PlanningPokerUser> {
	
	private String email;
	
	private String userName;
	
	private String instantMessage;
	
	private boolean sendSms, sendEmail, sendAim;

	
	
	public PlanningPokerUser(String email, String userName, String instantMessage, boolean sendSms, boolean sendEmail, boolean sendAim) {
		super();
		this.email = email;
		this.userName = userName;
		this.instantMessage = instantMessage;
		this.sendEmail = sendEmail;
		this.sendSms = sendSms;
		this.sendAim = sendAim;
	}

	public String toJSON(){
		return new Gson().toJson(this, PlanningPokerUser.class);
	}

	public static PlanningPokerUser fromJSON(String json){
		final Gson parser = new Gson();
		return parser.fromJson(json, PlanningPokerUser.class);
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
	 * @return the sendSms
	 */
	public boolean isSendSms() {
		return sendSms;
	}

	/**
	 * @param sendSms the sendSms to set
	 */
	public void setSendSms(boolean sendSms) {
		this.sendSms = sendSms;
	}

	/**
	 * @return the sendEmail
	 */
	public boolean isSendEmail() {
		return sendEmail;
	}

	/**
	 * @param sendEmail the sendEmail to set
	 */
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	/**
	 * @return the sendAim
	 */
	public boolean isSendAim() {
		return sendAim;
	}

	/**
	 * @param sendAim the sendAim to set
	 */
	public void setSendAim(boolean sendAim) {
		this.sendAim = sendAim;
	}

	/**
	 * Description
	 * @param body
	 * @return
	 */
	public static PlanningPokerUser[] fromJSONArray(String jsonArr) {
		JsonArray array = new JsonParser().parse(jsonArr).getAsJsonArray();
		List<PlanningPokerUser> planningPokerUsers = new ArrayList<PlanningPokerUser>();

		for (JsonElement json : array) {
			planningPokerUsers.add(PlanningPokerUser.fromJSON(json.toString()));
		}

		return planningPokerUsers.toArray(new PlanningPokerUser[0]);
	}

}
