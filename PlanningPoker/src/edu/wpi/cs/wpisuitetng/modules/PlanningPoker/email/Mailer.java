package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.email;

/**
 * This class creates a mailer object.  This object has functions for adding 
 * recipients, and sending the email which consists of the following text:
 * 
 * Subject: WPI-Suite: Planning Poker
 * Contents: A Planning Poker session has begun.
 */

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Alec Thompson
 * @author Long Nguyen
 * 
 */
public class Mailer {
	Session session;
	MimeMessage message;

	public Mailer() {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			String from = "struct.by.lightning@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject("WPI-Suite: Planning Poker");
			message.setText("A Planning Poker session has begun.");
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
		}
	}
	
	/**
	 * Alternate constructor that takes an email as a parameter
	 * @param emailAddress Hard-coded email address
	 */
	public Mailer(String emailAddress) {
		session = createSmtpSession();
		session.setDebug(true);
		message = new MimeMessage(session);

		try {
			String from = "struct.by.lightning@gmail.com";

			// set the message to be from struct by lightning
			message.setFrom(new InternetAddress(from));

			// set the header line
			message.setSubject("WPI-Suite: Planning Poker");
			message.setText("A Planning Poker session has begun.");
			this.addEmail(emailAddress);
		} catch (MessagingException mex) {
			System.out.println("Message creation failed");
			mex.printStackTrace();
		}
	}

	/**
	 * Creates an SMTP session, configured for using the Google SMTP server with
	 * the Struct by Lightning gmail account being used to send the emails
	 * 
	 * @return Google SMTP session
	 */
	private Session createSmtpSession() {
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

	/**
	 * Sends the email to the added recipients
	 * 
	 * @return true if the email sends, false otherwise
	 */
	public boolean send() {
		try {
			// send the message
			System.out.println("Ready to send message");
			Transport.send(message);
			System.out.println("Sent message successfully");
			return true;
		} catch (MessagingException mex) {
			System.out.println("Send failed");
			mex.printStackTrace();
			return false;
		}
	}

	/**
	 * Adds a recipient to the Mailer object
	 * 
	 * @param recipient
	 *            The target's email address
	 * @return true if the recipient is added, false otherwise
	 */
	public boolean addEmail(String recipient) {
		try {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					recipient));
			return true;
		} catch (MessagingException mex) {
			System.out.println("Recipient not added");
			mex.printStackTrace();
			return false;
		}
	}

	/**
	 * Adds multiple recipients to the mailer object from an array
	 * 
	 * @param recArr
	 *            Array of email addresses (strings!)
	 * @return true if all addresses were added successfully, false otherwise
	 */
	public boolean addEmailArray(String[] recArr) {
		boolean isSuccess = true;
		for (String s : recArr) {
			isSuccess &= this.addEmail(s);
		}
		return isSuccess;
	}
}
