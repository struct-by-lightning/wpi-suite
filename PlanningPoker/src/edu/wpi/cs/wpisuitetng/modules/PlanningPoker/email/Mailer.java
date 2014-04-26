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

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
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
	private Session session;
	private MimeMessage message;
	private Transport transport;

	// test information
	private String host = "mailtrap.io";
	private String login = "structbylightning-17dcc3f2b944376c";
	private String pass = "ce1a24cb171342c3";

	// TODO: Create a wpi-suite email address and mailtrap account that we can
	// give people access to in the documentation.

	private final boolean DEBUG = false;

	public Mailer() {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			// testing
			if (DEBUG)
				transport = session.getTransport("smtp");
			// release
			String from = "struct.by.lightning@gmail.com";

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
	public Mailer(String emailAddress) {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			// testing
			if (DEBUG)
				transport = session.getTransport("smtp");
			// release
			String from = "struct.by.lightning@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject("Great news ladies and gentlemen!");
			message.setText("Our favorite past time in which we predict the effort in which we must exert our fingers and minds has begun anew! I request that all ye whom have felt the stern blow of Thor's mighty hammer attend the session, so we may reach a general consensus!");
			this.addEmail(emailAddress);
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
		}
	}

	/**
	 * Alternate constructor that takes an array of email addresses as a
	 * parameter
	 * 
	 * 
	 * @param emailArray
	 *            String[]
	 */
	public Mailer(String[] emailArray) {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			if (DEBUG)
				transport = session.getTransport("smtp");
			String from = "struct.by.lightning@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject("Great news ladies and gentlemen!");
			message.setText("Our favorite past time in which we predict the effort in which we must exert our fingers and minds has begun anew! I request that all ye whom have felt the stern blow of Thor's mighty hammer attend the session, so we may reach a general consensus!");
			this.addEmailArray(emailArray);
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
		}
	}

	public Mailer(String subject, String text) {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			// testing
			if (DEBUG)
				transport = session.getTransport("smtp");
			// release
			String from = "struct.by.lightning@gmail.com";

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
		String text = ConfigManager.getConfig().getUserName()
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
			text += "You can now submit estimates on these requirements!"
					+ formatEndTime(game);
		} else {
			text += "You can now view the results in Janeway!";
		}

		System.out.println(subject);
		System.out.println(text);

		try {
			// testing
			if (DEBUG)
				transport = session.getTransport("smtp");
			// release
			String from = "struct.by.lightning@gmail.com";

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
		if (game.getEndDate().get(GregorianCalendar.YEAR) != 9999) {
			text += " Make sure to vote before the game closes at "
					+ game.getEndDate().get(GregorianCalendar.HOUR) + ":";
			if (game.getEndDate().get(GregorianCalendar.MINUTE) < 10)
				text += "0";
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

	/**
	 * Creates an SMTP session, configured for using the Google SMTP server with
	 * the Struct by Lightning gmail account being used to send the emails
	 * 
	 * 
	 * @return Google SMTP session
	 */
	private Session createSmtpSession() {
		// final version settings
		if (DEBUG) {
			// test with mailtrap
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", login);
			props.put("mail.smtp.password", pass);
			props.put("mail.smtp.port", "2525");
			props.put("mail.smtp.auth", "true");

			return Session.getDefaultInstance(props);
		} else {
			final Properties props = new Properties();
			props.setProperty("mail.smtp.host", "smtp.gmail.com");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.port", "" + 587);
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.transport.protocol", "smtp");

			return Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {

						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									"struct.by.lightning@gmail.com",
									"Donthackthis!12358");
						}
					});
		}
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
				if (DEBUG)
					transport.connect(host, login, pass);
				// send the message
				System.out.println("Ready to send message");
				// testing
				if (DEBUG) {
					transport.sendMessage(message, message.getAllRecipients());
				} else { // release
					Transport.send(message);
				}
				System.out.println("Sent message successfully");
				if (DEBUG)
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
	 * 
	 * @return true if the recipient is added, false otherwise
	 */
	public boolean addEmail(String recipient) {
		// debug prints
		System.out.print("Attempting to add " + recipient);
		System.out.println(" to message");
		System.out.println("length of recipient: " + recipient.length());
		// make sure it isn't an invalid input
		if (recipient != null && recipient != "" && recipient.length() != 0) {
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
	 * Adds multiple recipients to the mailer object from an array
	 * 
	 * @param recArr
	 *            Array of email addresses (strings!)
	 * 
	 * @return true if all addresses were added successfully, false otherwise
	 */
	public boolean addEmailArray(String[] recArr) {
		boolean isSuccess = true;
		for (String s : recArr) {
			isSuccess &= this.addEmail(s);
		}
		return isSuccess;
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
		boolean isSuccess = true;
		for (PlanningPokerUser u : userList) {
			if (u.getEmail() != null)
				isSuccess &= this.addEmail(u.getEmail());
		}
		return isSuccess;
	}
}
