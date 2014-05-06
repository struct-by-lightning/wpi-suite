/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributor: team struct-by-lightning
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerUser;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

/**
 * A Mailer manages the sending of emails to email addresses added to the
 * Mailer. The Mailer operates in two modes, normal and debug.
 * 
 * In normal mode, emails are routed through the Google SMTP server via the
 * team's gmail account (struct.by.lightning@gmail.com), and the emails actually
 * reach their recipient.
 * 
 * In debug mode, the emails are instead routed to the Mailtrap.io SMTP server,
 * where they are placed into an inbox accessible accessible to the team. This
 * prevents unnecessary spam during testing.
 * 
 * 
 * 
 * @author Alec Thompson
 * @author Long Nguyen
 * @version $Revision: 1.0 $
 */
public class Mailer {
	private final Session session;
	private final MimeMessage message;
	private Transport transport;

	// test information
	private final String host = "mailtrap.io";
	private final String login = "structbylightning-17dcc3f2b944376c";
	private final String pass = "ce1a24cb171342c3";

	// TODO: Create a wpi-suite email address and mailtrap account that we can
	// give people access to in the documentation.

	private final boolean DEBUG = false;

	public Mailer() {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			// testing
			if (DEBUG) {
				transport = session.getTransport("smtp");
			}
			// release
			final String from = "struct.by.lightning.noreply@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject("Great news ladies and gentlemen!");
			message.setText("Our favorite past time in which we predict the effort in which we must exert our fingers and minds has begun anew! I request that all ye whom have felt the stern blow of Thor's mighty hammer attend the session, so we may reach a general consensus!");
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
		}
	}

	/**
	 * Alternate constructor that takes an email as a parameter
	 * 
	 * @param emailAddress
	 *            Hard-coded email address
	 */

	/**
	 * Alternate constructor that takes an array of email addresses as a
	 * parameter
	 * 
	 * 
	 * @param subject
	 *            the subject of the message
	 * @param text
	 *            the content of the message
	 */

	public Mailer(String subject, String text) {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			// testing
			if (DEBUG) {
				transport = session.getTransport("smtp");
			}
			// release
			final String from = "struct.by.lightning@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject(subject);
			message.setText(text);
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
		}
	}

	/**
	 * Creates an email notification with the name of the game, the requirements
	 * being estimated, and the deadline if the game has one from a given
	 * PlanningPokerGame. If this occurs at a change in state of the game, the
	 * mailer must be constructed AFTER the change in state.
	 * 
	 * @param game
	 *            the given PlanningPokerGame
	 */
	public Mailer(PlanningPokerGame game) {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		// set up the subject
		String subject = "The Planning Poker Game " + game.getGameName()
				+ " Has ";

		if (game.isFinished()) {
			subject += "Ended";
		} else {
			subject += "Started";
		}

		// set up the text
		String text = "A planning poker game has ";

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
			text += "You can now submit estimates on these requirements!"
					+ formatEndTime(game);
		} else {
			text += "You can now view the results in Janeway!";
		}

		try {
			// testing
			transport = session.getTransport("smtp");

			// release
			final String from = "struct.by.lightning.noreply@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject(subject);
			message.setText(text);
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
		}
	}

	/**
	 * Creates an email notification with the name of the game, the requirements
	 * being estimated, and the deadline if the game has one from a given
	 * PlanningPokerGame. If this occurs at a change in state of the game, the
	 * mailer must be constructed AFTER the change in state.
	 * 
	 * This overload accounts for when the server closes a game, and thus does
	 * not have access to the network classes to retrieve requirements
	 * 
	 * @param game
	 *            the given PlanningPokerGame
	 * @param requirements
	 *            the list of requirements manually retrieved from the server
	 */
	public Mailer(PlanningPokerGame game, Requirement[] requirements) {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		// set up the subject
		String subject = "The Planning Poker Game " + game.getGameName()
				+ " Has ";

		if (game.isFinished()) {
			subject += "Ended";
		} else {
			subject += "Started";
		}

		// set up the text
		String text = "A planning poker game has ";

		if (game.isLive()) {
			text += "started.\n\n";
		} else {
			text += "ended.\n\n";
		}

		text += "Requirements:\n";
		// add the requirement names for this games requirements
		for (Requirement req : requirements) {
			if (game.getRequirementIds().contains(req.getId()))
				text += "-" + req.getName() + "\n";
		}
		text += "\n";

		if (game.isLive()) {
			text += "You can now submit estimates on these requirements!"
					+ formatEndTime(game);
		} else {
			text += "You can now view the results in Janeway!";
		}

		System.out.println(subject);
		System.out.println(text);

		try {
			// testing
			transport = session.getTransport("smtp");

			// release
			final String from = "struct.by.lightning.noreply@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject(subject);
			message.setText(text);
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
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
		if (game.getEndDate().get(Calendar.YEAR) != 9999) {
			text += " Make sure to vote before the game closes at "
					+ game.getEndDate().get(Calendar.HOUR) + ":";
			if (game.getEndDate().get(Calendar.MINUTE) < 10) {
				text += "0";
			}
			text += game.getEndDate().get(Calendar.MINUTE) + " ";

			// control whether it is AM or PM
			switch (game.getEndDate().get(Calendar.AM_PM)) {
			case 0:
				text += "AM";
				break;
			case 1:
				text += "PM";
				break;
			}

			text += " on ";

			// control month
			switch (game.getEndDate().get(Calendar.MONTH)) {
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
			text += " " + game.getEndDate().get(Calendar.DATE);

			// control suffix for date
			switch (game.getEndDate().get(Calendar.DATE)) {
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

	/**
	 * Creates an SMTP session, configured for using the Google SMTP server with
	 * the Struct by Lightning gmail account being used to send the emails
	 * 
	 * 
	 * @return Google SMTP session
	 */
	private Session createSmtpSession() {
		Properties props;

		// final version settings
		if (DEBUG) {
			// test with mailtrap
			props = System.getProperties();

			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", login);
			props.put("mail.smtp.password", pass);
			props.put("mail.smtp.port", "2525");
			props.put("mail.smtp.auth", "true");
		} else {
			props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.user", "struct.by.lightning.noreply@gmail.com");
			props.put("mail.smtp.password", "Donthackthis!12358");
		}

		return Session.getDefaultInstance(props);
	}

	/**
	 * Sends the email to the added recipients
	 * 
	 * 
	 * @return true if the email sends, false otherwise
	 */
	public boolean send() {
		try {
			if (message.getAllRecipients() != null) {
				if (DEBUG) {
					transport.connect(host, login, pass);
				} else
					transport.connect("smtp.gmail.com",
							"struct.by.lightning.noreply@gmail.com",
							"Donthackthis!12358");

				// send the message
				System.out.println("Ready to send message");
				// testing
				transport.sendMessage(message, message.getAllRecipients());
				System.out.println("Sent message successfully");

				transport.close();

				return true;
			}
			System.out
					.println("The message has no recipients\nThe email was not sent.");
			return false;
		} catch (MessagingException mex) {
			System.out.println("Send failed");
			mex.printStackTrace();
			return false;
		}
	}

	/**
	 * Counts the number of recipients that have been added to the Mailer
	 * 
	 * @return the number of recipients added to the Mailer
	 */
	public int countRecipients() {
		int count = 0;

		try {
			if (message.getAllRecipients() != null) {
				for (Address a : message.getAllRecipients()) {
					count++;
				}
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * Adds a recipient to the Mailer object
	 * 
	 * @param recipient
	 *            The target's email address
	 * @param pref
	 *            whether the user receives emails or not
	 * @return true if the recipient is added, false otherwise
	 */
	public boolean addEmail(String recipient, boolean pref) {
		// debug prints
		System.out.print("Attempting to add " + recipient);
		System.out.println(" to message");
		System.out.println("length of recipient: " + recipient.length());
		// make sure it isn't an invalid input

		if (recipient != null && recipient != "" && recipient.length() != 0
				&& pref) {

			try {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipient));
				return true;
			} catch (MessagingException mex) {
				System.out.println("Recipient not added");
				mex.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * Method addEmailFromUsers.
	 * 
	 * @param userList
	 *            List<PlanningPokerUser>
	 * 
	 * @return boolean
	 */
	public boolean addEmailFromUsers(List<PlanningPokerUser> userList) {
		try {
			if (message.getAllRecipients() != null)
				// clear the recipients
				message.setRecipient(RecipientType.TO, null);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isSuccess = true;
		for (PlanningPokerUser u : userList) {
			if (u.getEmail() != null)
				isSuccess &= this.addEmail(u.getEmail(), u.canSendEmail());

		}
		return isSuccess;
	}
}
