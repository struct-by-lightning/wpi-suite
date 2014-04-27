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
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.im;

import java.util.GregorianCalendar;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * Description
 * 
 * @author Alec Thompson - ajthompson
 * @author Ryan Killea - rbkillea
 * @version Apr 24, 2014
 */
public class InstantMessenger {
	private ConnectionConfiguration config;
	private XMPPConnection server;
	private Presence presence;
	private String text;
	private PacketListener pl;

	public InstantMessenger(PlanningPokerGame game) {
		config = new ConnectionConfiguration("talk.google.com", 5222,
				"gmail.com");
		server = new XMPPConnection(config);
		presence = new Presence(Presence.Type.available);

		try {
			server.connect();
			server.login("struct.by.lightning@gmail.com", "Donthackthis!12358");
		} catch (XMPPException e) {
			e.printStackTrace();
		}

		server.sendPacket(presence);

		// receive msg
		pl = new PacketListener() {
			@Override
			public void processPacket(Packet p) {
				if (p instanceof Message) {
					Message msg = (Message) p;
					System.out.println(msg.getFrom() + ": " + msg.getBody());
				}
			}
		};
		server.addPacketListener(pl, null);

		// set up the text
		text = ConfigManager.getConfig().getUserName()
				+ ",\n\nA planning poker game has ";

		if (game.isLive()) {
			text += "started.\n\n";
		} else {
			text += "ended.\n\n";
		}

		text += "Requirements:\n";
		// add the requirement names
		for (Requirement r : game.getRequirements()) {
			text += "-" + r.getName() + "\n";
		}
		text += "\n";

		if (game.isLive()) {
			text += "You can now submit estimates on the requirements for "
					+ game.getGameName() + "!" + formatEndTime(game);
		} else {
			text += "You can now view the results of " + game.getGameName()
					+ " in Janeway!";
		}
	}

	/**
	 * Sends a message to the given username
	 * 
	 * @param username
	 *            the username to send the message to
	 */
	public void sendMessage(String username) {
		System.out.println("Sending message to " + username);
		Message msg = new Message(username, Message.Type.chat);
		msg.setBody(text);
		server.sendPacket(msg);
	}

	/**
	 * Sends IM notificartions to all PlanningPokerUsers in the list
	 * 
	 * @param users
	 *            the List of PlanningPokerUsers to notify about a game
	 */
	public void sendAllMessages(List<PlanningPokerUser> users) {
		for (PlanningPokerUser u : users) {
			sendMessage(u.getInstantMessage());
		}
	}

	/**
	 * Formats the deadline for the email, if the game has a legitimate
	 * deadline, and not a placeholder of November 18th, 9999.
	 * 
	 * @param game
	 *            the Planning Poker Game
	 * @return a formatted date notification string.
	 */
	private String formatEndTime(PlanningPokerGame game) {
		String text = "";

		// check if the date is real or a placeholder
		if (game.getEndDate().get(GregorianCalendar.YEAR) != 9999) {
			text += " Make sure to vote before the game closes at "
					+ game.getEndDate().get(GregorianCalendar.HOUR) + ":";
			if (game.getEndDate().get(GregorianCalendar.MINUTE) < 10) {
				text += "0";
			}
			text += game.getEndDate().get(GregorianCalendar.MINUTE) + " ";

			// control whether it is AM or PM
			switch (game.getEndDate().get(GregorianCalendar.AM_PM)) {
			case 0:
				text += "AM";
				break;
			case 1:
				text += "PM";
				break;
			}

			text += " on ";

			// control month
			switch (game.getEndDate().get(GregorianCalendar.MONTH)) {
			case 0:
				text += "January";
				break;
			case 1:
				text += "February";
				break;
			case 2:
				text += "March";
				break;
			case 3:
				text += "April";
				break;
			case 4:
				text += "May";
				break;
			case 5:
				text += "June";
				break;
			case 6:
				text += "July";
				break;
			case 7:
				text += "August";
				break;
			case 8:
				text += "September";
				break;
			case 9:
				text += "October";
				break;
			case 10:
				text += "November";
				break;
			case 11:
				text += "December";
				break;
			}
			text += " " + game.getEndDate().get(GregorianCalendar.DATE);

			// control suffix for date
			switch (game.getEndDate().get(GregorianCalendar.DATE)) {
			case 1:
				text += "st";
				break;
			case 2:
				text += "nd";
				break;
			case 3:
				text += "rd";
				break;
			default:
				text += "th";
				break;
			}

			text += "!";
		}

		return text;
	}
}
