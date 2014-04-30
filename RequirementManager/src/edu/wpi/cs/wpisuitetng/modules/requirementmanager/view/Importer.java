/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementmanager.view;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * @author Benjamin
 *
 */
public class Importer {

	public Importer() {
		
	}
	
	/**
	 * Imports the input list of selected requirements from a file as a JSON
	 * @param listOfRequirementsToAdd DefaultListModel<Requirement>
	 * @param filename String
	 * @throws IOException 
	 */
	public String importFromJSON(String filename) throws IOException {
		String str = "";
		StringBuilder sb = new StringBuilder();
		// Prepare file for importing
		BufferedReader br = new BufferedReader(
			new InputStreamReader(new FileInputStream(filename)));
		try {
		    String line;
		    while ((line = br.readLine()) != null) {
		        sb.append(line);
		    }
		} finally {
		    br.close();
		}
		
		str = sb.toString();
		return str;
	}
	
}
