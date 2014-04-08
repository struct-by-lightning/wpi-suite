/*******************************************************************************
* Copyright (c) 2012-2014 -- WPI Suite
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* Contributor: team struct-by-lightning
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.decks;

import java.awt.Image;
import java.io.File;

/**
 * @author Alec Thompson
 *
 */
public class Card {
	private int value;
	private Image icon=null;
	
	public Card(int value) {
		this.value = value;
		icon = null;
	}
	
	public static String createFileName(int value) {
		String fileName = "./images/" + value + ".jpg";
		return fileName;
	}
	
	public static String createFileName(String defaultName) {
		String fileName = "./images/" + defaultName + ".jpg";
		return fileName;
	}
	
	public static File getFilePath(int value) {
		File image = new File(createFileName(value));
		if (image.exists())
			return image;
		image = new File(createFileName("default"));
		return image;
	}

	public int getValue() {
		return value;
	}

	public Image getIcon() {
		return icon;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}
}