package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import static org.junit.Assert.*;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.decks.Card;

/**
 * @author Alec Thompson
 * 
 */
public class CardTest {
	//
	// @Test
	// public void createFileNameDefaultTest() {
	// String filename = Card.createFileName("default");
	//
	// assertEquals("./images/default.jpg", filename);
	// }
	//
	// @Test
	// public void createFileNameValueTest() {
	// String filename = Card.createFileName(1);
	//
	// assertEquals("./images/1.jpg", filename);
	// }
	//
	// @Test
	// public void getFilePathExistsTest() {
	// File filePath = Card.getFilePath(1);
	//
	// assertTrue(new File("./images/1.jpg").equals(filePath));
	// }
	//
	// @Test
	// public void getFilePathNotExistTest() {
	// File filePath = Card.getFilePath(2);
	//
	// assertTrue(new File("./images/default.jpg").equals(filePath));
	// }

	@Test
	public void getValueTest() {
		Card card = new Card(1);

		assertEquals(1, card.getValue());
	}

	@Test
	public void getIconExistsTest() throws IOException {
		Image expected = null;
		Card card = new Card(1);

		assertEquals(expected, card);
	}

	@Test
	public void getIconDefaultTest() throws IOException {
		Image expected = null;
		Card card = new Card(2);

		assertEquals(expected, card);
	}
}
