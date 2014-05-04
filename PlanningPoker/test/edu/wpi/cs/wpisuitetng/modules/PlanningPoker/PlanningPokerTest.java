package edu.wpi.cs.wpisuitetng.modules.PlanningPoker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.swing.ImageIcon;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views.MainView;

/**
 * Tests for PlanningPoker
 * 
 * @author bbiletch
 * @version $ Revision 1.0 $
 */
public class PlanningPokerTest {
	private final PlanningPoker pp;
	
	public PlanningPokerTest() {
		pp = new PlanningPoker();
	}
	
	@Test
	public void testGetName() {
		assertEquals(pp.getName(), "PlanningPoker");
	}

	@Test
	public void testGetTabs()
	{
		final List<JanewayTabModel> tabs = pp.getTabs();
		
		assertEquals(tabs.size(), 1);
		
		final JanewayTabModel tab = tabs.get(0);
		
		assertEquals(tab.getName(), "PlanningPoker");
		assertTrue(tab.getIcon() instanceof ImageIcon);
		assertEquals(tab.getToolbar(), MainView.getInstance().getToolbarComponent());
		assertEquals(tab.getMainComponent(), MainView.getInstance().getMainComponent());
	}
}
