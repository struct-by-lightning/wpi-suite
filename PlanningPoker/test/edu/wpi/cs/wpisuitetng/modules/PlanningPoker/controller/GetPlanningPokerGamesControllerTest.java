/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.mockobjects.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;

/**
 * @author darkd_000
 *
 */
public class GetPlanningPokerGamesControllerTest {

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		GetPlanningPokerGamesController gc = GetPlanningPokerGamesController.getInstance();
		assertEquals(gc.hashCode(), GetPlanningPokerGamesController.getInstance().hashCode());
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController#receivedPlanningPokerGames(edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame[])}.
	 */
	@Test
	public void testReceivedPlanningPokerGames() {
		MockNetwork s = new MockNetwork();
		GetPlanningPokerGamesController gc = GetPlanningPokerGamesController.getInstance();
		PlanningPokerGame[] games = new PlanningPokerGame[]{
				new PlanningPokerGame("g1", "d1", "dt1", new ArrayList<Integer>(), false, false, new GregorianCalendar(), new GregorianCalendar(), "m1"),
				new PlanningPokerGame("g2", "d2", "dt2", new ArrayList<Integer>(), false, false, new GregorianCalendar(), new GregorianCalendar(), "m2")
		};
		gc.receivedPlanningPokerGames(games);
		
		ArrayList<PlanningPokerGame> pgm = PlanningPokerGameModel.getPlanningPokerGames();
		assertTrue(pgm.size() == games.length);
		for (int x = 0; x < pgm.size(); x++) {
			PlanningPokerGame s1 = pgm.get(pgm.size()-x-1);
			PlanningPokerGame s2 = games[x];
			
		assertEquals(s2.getID(), s1.getID());
		}
	}

}
