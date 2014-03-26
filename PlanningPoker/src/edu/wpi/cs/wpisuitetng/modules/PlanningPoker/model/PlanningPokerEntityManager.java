package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.model;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

public class PlanningPokerEntityManager implements
		EntityManager<PlanningPokerSession> {
	
	private Data db;
	
	public PlanningPokerEntityManager(Data db) {
		this.db = db;
	}

	@Override
	public PlanningPokerSession makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		
		final PlanningPokerSession newSession = PlanningPokerSession.fromJSON(content);
		
		if (!db.save(newSession, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return newSession;
	}

	/**
	 * Throws an exception when user tries to retrieve the specific entity of the session
	 */
	@Override
	public PlanningPokerSession[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		throw new WPISuiteException();
	}

	@Override
	public PlanningPokerSession[] getAll(Session s) throws WPISuiteException {
		List<Model> messages = db.retrieveAll(new PlanningPokerSession(null, null, null, null, null, null), s.getProject());
		return messages.toArray(new PlanningPokerSession[0]);
	}

	@Override
	public PlanningPokerSession update(Session s, String content)
			throws WPISuiteException {
		final PlanningPokerSession updatedPlanningPokerSession = PlanningPokerSession.fromJSON(content);
		
		//db.update(PlanningPokerSession.class, "sessionId", updatedPlanningPokerSession.getSessionId(), "results", updatedPlanningPokerSession.getResults());
		throw new WPISuiteException();
	}

	@Override
	public void save(Session s, PlanningPokerSession model)
			throws WPISuiteException {
		db.save(model);
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub

	}

	@Override
	public int Count() throws WPISuiteException {
		//return the number of sessions stored in the database
		return db.retrieveAll(new PlanningPokerSession(null, null, null, null, null, null)).size();
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}
}
