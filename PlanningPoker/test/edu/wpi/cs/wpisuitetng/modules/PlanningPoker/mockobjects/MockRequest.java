/*******************************************************************************
 * Copyright (c) 2013-2014 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder, struct-by-lightning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.mockobjects;

import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This class creates a mock request that is never actually sent to the server.
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 6, 2014
 */
public class MockRequest extends Request {
	protected boolean sent = false;

	public MockRequest(NetworkConfiguration networkConfiguration, String path,
			HttpMethod requestMethod) {
		super(networkConfiguration, path, requestMethod);
	}

	/**
	 * Pretends to send the request by setting sent = true
	 * 
	 * @throws IllegalStateException
	 */
	@Override
	public void send() throws IllegalStateException {
		sent = true;
	}
	
	/**
	 * @return whether the request was sent
	 */
	public boolean isSent() {
		return sent;
	}
}
