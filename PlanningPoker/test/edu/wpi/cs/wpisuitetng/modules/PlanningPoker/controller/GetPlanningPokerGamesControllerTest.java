/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGameModel;

/** Test for the GetPlanningPokerGames class.
 * 
 * @version $Revision: 1.0 $
 * @author cgwalker, rbkillea
 *
 */
public class GetPlanningPokerGamesControllerTest {

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController#getInstance()}
	 * . See if each instance is the same.
	 */
	@Test
	public void testGetInstance() {
		final GetPlanningPokerGamesController gc = GetPlanningPokerGamesController.getInstance();
		assertEquals(gc.hashCode(), GetPlanningPokerGamesController.getInstance().hashCode());
	}

	/**
	 * Test method for
	 * {@link edu.wpi.cs.wpisuitetng.modules.PlanningPoker.controller.GetPlanningPokerGamesController#receivedPlanningPokerGames(edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame[])}
	 * . Retrieve games and check to see if the lists contain the same elements.
	 * Currently, for some reason, the list is set backwards. This is kind of
	 * bad and should be fixed.
	 */
	@Test
	public void testReceivedPlanningPokerGames() {
		final GetPlanningPokerGamesController gc = GetPlanningPokerGamesController.getInstance();
		final PlanningPokerGame[] games = new PlanningPokerGame[]{
				new PlanningPokerGame("g1", "d1", "dt1",
						new ArrayList<Integer>(), false, false,
						new GregorianCalendar(), new GregorianCalendar(), "m1"),
				new PlanningPokerGame("g2", "d2", "dt2",
						new ArrayList<Integer>(), false, false,
						new GregorianCalendar(), new GregorianCalendar(), "m2")
		};
		gc.receivedPlanningPokerGames(games);
		
		final List<PlanningPokerGame> pgm = PlanningPokerGameModel.getPlanningPokerGames();
		for (int x = 0; x < pgm.size(); x++) {
			PlanningPokerGame s1 = pgm.get(pgm.size() - x - 1);
			PlanningPokerGame s2 = games[x];
			
		assertEquals(s2.getID(), s1.getID());
		}
	}

}
