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
import java.util.HashMap;

/**
 * @author sfmailand
 *
 */
public class Users {

	//Dictionary mapping each user's username to it's instance
	private static HashMap<String, UserModel> userModelDict = new HashMap<String, UserModel>();
	
	public static void addUserModel(UserModel newUser){
		Users.userModelDict.put(newUser.getUserName(), newUser);
	}
	
	
	public static void addUserModels(UserModel[] userModels){
		for(UserModel user: userModels){
			Users.addUserModel(user);
		}
	}
	
	public static UserModel getUserModel(String name){
		UserModel toReturn = Users.userModelDict.get(name);
		return toReturn;
	}
	
	
	public static ArrayList<UserModel> getUserModels() {
		return new ArrayList<UserModel>(
				Users.userModelDict.values());
	}
	
	public static int getSize(){
		return Users.userModelDict.size();
	}
	
	public static void emptyModel(){
		Users.userModelDict.clear();
	}
}
