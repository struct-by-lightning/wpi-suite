package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.views;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;

import javax.swing.DefaultListModel;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.Deck;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.DeckEntityManager;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.DeckModel;
import edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models.PlanningPokerGame;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.iterations.IterationModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

public class CreateDeckViewTest {
	MockData db;
	Deck newDeck;
	Deck newDeck2;
	User existingUser;
	Requirement req1;
	Session defaultSession;
	String mockSsid;
	DeckEntityManager manager;
	User bob;
	Requirement goodUpdatedRequirement;
	Session adminSession;
	Project testProject;
	Project otherProject;
	Requirement req2;
	PlanningPokerGame ppg;

	/**
	 * Set up objects and create a mock session for testing
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		ppg = new PlanningPokerGame("g1", "d1", "dt1",
				new ArrayList<Integer>(), false, false,
				new GregorianCalendar(), new GregorianCalendar(), "m1");
		User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "abc123";
		adminSession = new Session(admin, testProject, mockSsid);
		DefaultListModel<Integer> numsInDeck = new DefaultListModel<Integer>();
		numsInDeck.addElement(1);
		numsInDeck.addElement(2);
		numsInDeck.addElement(3);
		numsInDeck.addElement(4);
		DefaultListModel<Integer> numsInDeck2 = new DefaultListModel<Integer>();
		numsInDeck.addElement(2);
		numsInDeck.addElement(5);
		numsInDeck.addElement(7);
		numsInDeck.addElement(20);
		newDeck = new Deck("deck", numsInDeck);
		newDeck2 = new Deck("deck3", numsInDeck2);
		existingUser = new User("joe", "joe", "1234", 2);

		defaultSession = new Session(existingUser, testProject, mockSsid);

		db = new MockData(new HashSet<Object>());

		db.save(newDeck, testProject);
		db.save(existingUser);
		manager = new DeckEntityManager(db);

		Network.initNetwork(new MockNetwork());
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration("http://wpisuitetng"));
		IterationModel.getInstance().setBacklog(new Iteration(1, "Backlog"));
		RequirementModel.getInstance().emptyModel();
		DeckModel.getInstance().addDeck(newDeck);
		CreateDeckView.openNewTab();
	}
	@Test
	public void dummyTest() {
		
	}
}
