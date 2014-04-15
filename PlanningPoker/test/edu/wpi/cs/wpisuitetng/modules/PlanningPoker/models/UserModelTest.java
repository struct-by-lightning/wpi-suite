/*******************************************************************************
 * Copyright (c) 2013-2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, struct-by-lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * @author Alec Thompson - ajthompson
 * @version Apr 3, 2014
 */
public class UserModelTest {

	@Test
	public void addUserTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test", "tester", "test@test.com", "testword", 15);
		model.addUser(user);
		assertEquals(1, model.getSize());
		model.addUser(user2);
		assertEquals(2, model.getSize());
	}
	
	@Test
	public void getUserTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test", "tester", "test@test.com", "testword", 15);
		model.addUser(user);
		assertEquals(1, model.getSize());
		model.addUser(user2);
		assertEquals(2, model.getSize());
		User returnedUser = model.getUser(8); // gets users by ID
		User returnedUser2 = model.getUser(15); // gets users by ID
		assertEquals("Alec", returnedUser.getName());
		assertEquals("ajthompson", returnedUser.getUsername());
		assertEquals("ajthompson@wpi.edu", returnedUser.getEmail());
		assertEquals(8, returnedUser.getIdNum());
		assertEquals("Test", returnedUser2.getName());
		assertEquals("tester", returnedUser2.getUsername());
		assertEquals("test@test.com", returnedUser2.getEmail());
		assertEquals(15, returnedUser2.getIdNum());
	}
	
	@Test
	public void removeUserTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test", "tester", "test@test.com", "testword", 15);
		model.addUser(user);
		assertEquals(1, model.getSize());
		model.addUser(user2);
		assertEquals(2, model.getSize());
		model.removeUser(2);	// testing removing a non-existent object
		assertEquals(2, model.getSize());
		model.removeUser(15);
		assertEquals(1, model.getSize());
		model.removeUser(8);
		assertEquals(0, model.getSize());
	}
	
	@Test
	public void getSizeTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test2", "tester2", "test2@test.com", "testword2", 15);
		User user3 = new User("Test3", "tester3", "test3@test.com", "testword3", 16);
		User user4 = new User("Test4", "tester4", "test4@test.com", "testword4", 17);
		model.addUser(user);
		assertEquals(1, model.getSize());
		model.addUser(user2);
		assertEquals(2, model.getSize());
		model.addUser(user3);
		assertEquals(3, model.getSize());
		model.addUser(user4);
		assertEquals(4, model.getSize());
		model.removeUser(8);
		assertEquals(3, model.getSize());
		model.removeUser(15);
		assertEquals(2, model.getSize());
		model.removeUser(16);
		assertEquals(1, model.getSize());
		model.removeUser(17);
		assertEquals(0, model.getSize());
	}
	
	@Test
	public void getElementAtTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test", "tester", "test@test.com", "testword", 15);
		model.addUser(user);
		assertEquals(1, model.getSize());
		model.addUser(user2);
		assertEquals(2, model.getSize());
		User returnedUser = model.getElementAt(1);  // older elements have higher indexes
		User returnedUser2 = model.getElementAt(0);	// an inserted element is placed in index 0
		assertEquals("Alec", returnedUser.getName());
		assertEquals("ajthompson", returnedUser.getUsername());
		assertEquals("ajthompson@wpi.edu", returnedUser.getEmail());
		assertEquals(8, returnedUser.getIdNum());
		assertEquals("Test", returnedUser2.getName());
		assertEquals("tester", returnedUser2.getUsername());
		assertEquals("test@test.com", returnedUser2.getEmail());
		assertEquals(15, returnedUser2.getIdNum());
	}
	
	@Test
	public void emptyModelTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test2", "tester2", "test2@test.com", "testword2", 15);
		User user3 = new User("Test3", "tester3", "test3@test.com", "testword3", 16);
		User user4 = new User("Test4", "tester4", "test4@test.com", "testword4", 17);
		model.addUser(user);
		assertEquals(1, model.getSize());
		model.addUser(user2);
		assertEquals(2, model.getSize());
		model.addUser(user3);
		assertEquals(3, model.getSize());
		model.addUser(user4);
		assertEquals(4, model.getSize());
		model.emptyModel();
		assertEquals(0, model.getSize());
	}
	
	@Test
	public void addUsersTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test2", "tester2", "test2@test.com", "testword2", 15);
		User user3 = new User("Test3", "tester3", "test3@test.com", "testword3", 16);
		User user4 = new User("Test4", "tester4", "test4@test.com", "testword4", 17);
		User[] userArray = new User[] {user, user2, user3, user4};
		model.addUsers(userArray);
		assertEquals(4, model.getSize());
	}
	
	@Test
	public void getUsersTest() {
		UserModel model = UserModel.getInstance();
		model.emptyModel(); // make sure this is a fresh model
		assertEquals(0, model.getSize());
		User user = new User("Alec", "ajthompson", "ajthompson@wpi.edu", "password", 8);
		User user2 = new User("Test2", "tester2", "test2@test.com", "testword2", 15);
		User user3 = new User("Test3", "tester3", "test3@test.com", "testword3", 16);
		User user4 = new User("Test4", "tester4", "test4@test.com", "testword4", 17);
		List<User> expectedUsers = new ArrayList<User>();
		expectedUsers.add(user);
		expectedUsers.add(user2);
		expectedUsers.add(user3);
		expectedUsers.add(user4);
		model.addUser(user);
		assertEquals(1, model.getSize());
		model.addUser(user2);
		assertEquals(2, model.getSize());
		model.addUser(user3);
		assertEquals(3, model.getSize());
		model.addUser(user4);
		assertEquals(4, model.getSize());
		assertEquals(expectedUsers, model.getUsers());
	}
}
