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
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck;

/**
 * Description
 * 
 * @author Alec Thompson - ajthompson
 * @version Apr 10, 2014
 */
public class DeckTest {

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#Deck(java.lang.String)}
	 * .
	 */
	@Test
	public void testDeckString() {
		// set up unordered list of cards in deck
		Integer[] cards = new Integer[] { 3, 1, 13, 21, 5, 8, 2, 1 };

		Deck test = new Deck("test");

		for (Integer i : cards) {
			test.addCard(i);
		}

		// set up expected values and results
		String expectedName = "test";
		String name = test.getDeckName();
		Integer[] expected = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };
		Object[] deckResult = test.getCards().toArray();

		// tests
		assertTrue(expectedName.equals(name));
		// compare the arrays
		assertEquals(expected.length, deckResult.length);
		for (int i = 0; i < Math.min(expected.length, deckResult.length); i++) {
			assertEquals(expected[i], deckResult[i]);
		}
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#Deck(java.lang.String, java.util.ArrayList)}
	 * .
	 */
	@Test
	public void testDeckStringArrayListOfIntegerOrdered() {
		// set up ordered list of cards in deck
		Integer[] cards = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test", cardList);

		// set up expected values and results
		String expectedName = "test";
		String name = test.getDeckName();
		Integer[] expected = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };
		Object[] deckResult = test.getCards().toArray();

		// tests
		assertTrue(expectedName.equals(name));
		// compare the arrays
		assertEquals(expected.length, deckResult.length);
		for (int i = 0; i < Math.min(expected.length, deckResult.length); i++) {
			assertEquals(expected[i], deckResult[i]);
		}
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#Deck(java.lang.String, java.util.ArrayList)}
	 * .
	 */
	@Test
	public void testDeckStringArrayListOfIntegerUnOrdered() {
		// set up unordered list of cards in deck
		Integer[] cards = new Integer[] { 3, 1, 13, 21, 5, 8, 2, 1 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test", cardList);

		// set up expected values and results
		String expectedName = "test";
		String name = test.getDeckName();
		Integer[] expected = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };
		Object[] deckResult = test.getCards().toArray();

		// tests
		assertTrue(expectedName.equals(name));
		// compare the arrays
		assertEquals(expected.length, deckResult.length);
		for (int i = 0; i < Math.min(expected.length, deckResult.length); i++) {
			assertEquals(expected[i], deckResult[i]);
		}
	}

	/**
	 * Tests both addCard and sortDeck, as addCard calls sortDeck to sort the
	 * new item into the correct place
	 * 
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#addCard(java.lang.Integer)}
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#sortDeck()}
	 */
	@Test
	public void testAddCard() {
		// set up ordered list of cards in deck
		Integer[] cards = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test", cardList);

		// add a value to the list
		test.addCard(11);

		// set up expected values and results
		Integer[] expected = new Integer[] { 1, 1, 2, 3, 5, 8, 11, 13, 21 };
		Object[] deckResult = test.getCards().toArray();

		// compare the arrays
		assertEquals(expected.length, deckResult.length);
		for (int i = 0; i < Math.min(expected.length, deckResult.length); i++) {
			assertEquals(expected[i], deckResult[i]);
		}
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#removeCard(java.lang.Integer)}
	 * .
	 */
	@Test
	public void testRemoveCard() {
		// set up ordered list of cards in deck
		Integer[] cards = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test", cardList);

		// remove a value to the list
		test.removeCard(8);

		// set up expected values and results
		Integer[] expected = new Integer[] { 1, 1, 2, 3, 5, 13, 21 };
		Object[] deckResult = test.getCards().toArray();

		// compare the arrays
		assertEquals(expected.length, deckResult.length);
		for (int i = 0; i < Math.min(expected.length, deckResult.length); i++) {
			assertEquals(expected[i], deckResult[i]);
		}
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#getDeckName()}
	 * .
	 */
	@Test
	public void testGetDeckName() {
		// set up ordered list of cards in deck
		Integer[] cards = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test", cardList);

		// set up expected values and results
		String expectedName = "test";
		String name = test.getDeckName();

		// tests
		assertTrue(expectedName.equals(name));
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#getCards()}
	 * .
	 */
	@Test
	public void testGetCards() {
		// set up ordered list of cards in deck
		Integer[] cards = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test", cardList);

		// set up expected values and results
		Integer[] expected = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };
		Object[] deckResult = test.getCards().toArray();

		// compare the arrays
		assertEquals(expected.length, deckResult.length);
		for (int i = 0; i < Math.min(expected.length, deckResult.length); i++) {
			assertEquals(expected[i], deckResult[i]);
		}
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#setDeckName(java.lang.String)}
	 * .
	 */
	@Test
	public void testSetDeckName() {
		// set up ordered list of cards in deck
		Integer[] cards = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test", cardList);

		// set up expected values and results
		String expectedName = "test";
		String name = test.getDeckName();

		// tests
		assertTrue(expectedName.equals(name));

		test.setDeckName("different");

		assertTrue("different".equals(test.getDeckName()));
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.deck.Deck#setCards(java.util.ArrayList)}
	 * .
	 */
	@Test
	public void testSetCards() {
		// set up ordered list of cards in deck
		Integer[] cards = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };

		ArrayList<Integer> cardList = new ArrayList<Integer>();

		for (Integer i : cards) {
			cardList.add(i);
		}

		Deck test = new Deck("test");
		
		test.setCards(cardList);

		// set up expected values and results
		String expectedName = "test";
		String name = test.getDeckName();
		Integer[] expected = new Integer[] { 1, 1, 2, 3, 5, 8, 13, 21 };
		Object[] deckResult = test.getCards().toArray();

		// tests
		assertTrue(expectedName.equals(name));
		// compare the arrays
		assertEquals(expected.length, deckResult.length);
		for (int i = 0; i < Math.min(expected.length, deckResult.length); i++) {
			assertEquals(expected[i], deckResult[i]);
		}
	}

}
