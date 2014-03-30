package edu.wpi.cs.wpisuitetng.modules.core.entitymanagers;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonSyntaxException;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.DatabaseException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.PlanningPokerTestModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserDeserializer;

public class PlanningPokerEntityManager implements
		EntityManager<PlanningPokerTestModel> {

	Class<PlanningPokerTestModel> pptm = PlanningPokerTestModel.class;
	Data data;

	private static final Logger logger = Logger.getLogger(PlanningPokerEntityManager.class.getName());

	public PlanningPokerEntityManager(Data data) {
		this.data = data;
	}

	@Override
	public PlanningPokerTestModel makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		PlanningPokerTestModel p;
		p = PlanningPokerTestModel.fromJSON(content);

		if (getEntity(s, p.getData())[0] == null) {
			save(s, p);
		} else {
			logger.log(Level.WARNING, "Conflict Exception during PlanningPokerTestModel creation.");
			throw new ConflictException("A PlanningPokerTestModel with the given ID already exists. Entity String: " + content);
		}

		return p;
	}

	@Override
	public PlanningPokerTestModel[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		PlanningPokerTestModel[] m = new PlanningPokerTestModel[1];
		if(id.equalsIgnoreCase(""))
		{
			return getAll(s);
		}
		else
		{
			return data.retrieve(pptm, "data", id).toArray(m);
		}
	}

	@Override
	public PlanningPokerTestModel[] getAll(Session s) throws WPISuiteException {
		PlanningPokerTestModel[] ret = new PlanningPokerTestModel[1];
		ret = data.retrieveAll(new PlanningPokerTestModel("")).toArray(ret);
		return ret;
	}

	@Override
	public PlanningPokerTestModel update(Session s, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Session s, PlanningPokerTestModel model)
			throws WPISuiteException {
		if(data.save(model))
		{
			logger.log(Level.FINE, "PlanningPokerTestModel Saved :" + model);

			return ;
		}
		else
		{
			logger.log(Level.WARNING, "PlanningPokerTestModel Save Failure!");
			throw new DatabaseException("Save failure for PlanningPokerTestModel.");
		}
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
		// TODO Auto-generated method stub
		return 0;
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
