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
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * List of projects pulled from the server.
 * 
 * Add methods only add projects to the local instance, as the projects already
 * exist on the server.
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 22, 2014
 */
public class ProjectModel extends AbstractListModel<Project> {

	/** the singleton instance of the ProjectModel */
	private static ProjectModel instance = null;
	/**
	 * Returns the singleton instance of the ProjectModel, or creates one if it
	 * does not yet exist.
	 * 
	 * @return the singleton instance of the ProjectModel
	 */
	public static ProjectModel getInstance() {
		if (instance == null)
			instance = new ProjectModel();
		return instance;
	}

	/** The list of all projects on the server */
	private List<Project> projects;

	/** Constructs an empty project model */
	private ProjectModel() {
		projects = new ArrayList<Project>();
	}

	/**
	 * Adds a single Project to the list of Projects
	 * 
	 * @param newProject
	 *            the Project to be added to the list of Projects
	 */
	public void addProject(Project newProject) {
		projects.add(newProject);
	}

	/**
	 * Adds the given array of Projects to the list
	 * 
	 * @param Projects
	 *            the array of Projects to add
	 */
	public void addProjects(Project[] projects) {
		for (Project p : projects) {
			this.projects.add(p);
		}
		this.fireIntervalAdded(this, 0, Math.max(getSize() - 1, 0));
	}

	/**
	 * Removes all Projectss from this model
	 * 
	 * NOTE: Once does not simply construct a new instance of the model. Other
	 * classes reference it, so we must manually remove each Project from the
	 * model.
	 */
	public void emptyModel() {
		int oldSize = getSize();
		Iterator<Project> iterator = projects.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		this.fireIntervalRemoved(this, 0, Math.max(oldSize - 1, 0));
	}

	/**
	 * Take an index and finds the Project at the given index in the list of
	 * Projects.
	 * 
	 * @param index
	 *            the index to retrieve from
	 * @return the Project at the given index
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Project getElementAt(int index) {
		return projects.get(projects.size() - 1 - index);
	}

	/**
	 * Gets the Project with the given name
	 * 
	 * @param name
	 *            the name of the Project to be returned
	 * @return the Project with the given name, or null if it is not found
	 */
	public Project getProject(String name) {
		// iterate through the list of Projects until name is found
		for (Project p : projects) {
			if (p.getName().equals(name))
				return p;
		}
		return null;
	}

	/**
	 * Returns the list of Projects
	 * 
	 * @return the Projects held within the ProjectModel
	 */
	public List<Project> getProjects() {
		return projects;
	}

	/**
	 * Provides the number of elements in the list of Projects
	 * 
	 * @return the number Projects in the list of Projects
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return projects.size();
	}

	/**
	 * Removes the Project with the given name
	 * 
	 * @param name
	 *            The name of the Project to be removed from the list of
	 *            Projects.
	 */
	public void removeProject(String name) {
		// iterate through the list of Users until id is found
		for (int i = 0; i < projects.size(); i++) {
			if (projects.get(i).getName().equals(name)) {
				projects.remove(i);
				break;
			}
		}
	}
}
