package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.model;

import java.util.HashMap;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class PlanningPokerSession implements Model {
	
	private final Integer sessionId;
	private final Integer dateTimeStarted;
	private final Integer dateTimeEnded;
	private final HashMap<Integer,Integer> estimationsMade;
	private final PlanningPokerConfiguration config;
	private final PlanningPokerResults results;
	
	/**
	 * Constructs the planning poker session for the given user story
	 * 	
	 * @param sessionId
	 * @param dateTimeStarted
	 * @param dateTimeEnded
	 * @param estimationsMade
	 * @param config
	 * @param results
	 */
	public PlanningPokerSession(Integer sessionId, Integer dateTimeStarted,
			Integer dateTimeEnded, HashMap<Integer, Integer> estimationsMade,
			PlanningPokerConfiguration config, PlanningPokerResults results) {
		super();
		this.sessionId = sessionId;
		this.dateTimeStarted = dateTimeStarted;
		this.dateTimeEnded = dateTimeEnded;
		this.estimationsMade = estimationsMade;
		this.config = config;
		this.results = results;
	}
	
	/**
	 * Returns a JSON-encoded string representation of this session object
	 * 
	 * @return JSON-encoded string representation of this session object
	 */
	@Override
	public String toJSON() {
	    return new Gson().toJson(this, PlanningPokerSession.class);
	}
	
	/**
	 * Returns an instance of PlanningPokerSession constructed using the given
	 * PlanningPokerSession encoded as a JSON string.
	 * 
	 * @param json the json-encoded PlanningPokerSession to deserialize
	 * @return the PlanningPokerSession contained in the given JSON
	 */
	public static PlanningPokerSession fromJSON(String json) {
	    return new Gson().fromJson(json, PlanningPokerSession.class);
	}
	
	/**
	 * Returns an array of PlanningPokerSession parsed from the given JSON-encoded
	 * string.
	 * 
	 * @param json a string containing a JSON-encoded array of PlanningPokerSession
	 * @return an array of PlanningPokerSession deserialzied from the given json string
	 */
	public static PlanningPokerSession[] fromJsonArray(String json) {
	    return new Gson().fromJson(json, PlanningPokerSession[].class);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Permission getPermission(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPermission(Permission p, User u) {
		// TODO Auto-generated method stub

	}

	@Override
	public Project getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProject(Project p) {
		// TODO Auto-generated method stub

	}

	public Integer getSessionId() {
		return sessionId;
	}

	public Integer getDateTimeStarted() {
		return dateTimeStarted;
	}

	public Integer getDateTimeEnded() {
		return dateTimeEnded;
	}

	public HashMap<Integer, Integer> getEstimationsMade() {
		return estimationsMade;
	}

	public PlanningPokerConfiguration getConfig() {
		return config;
	}

	public PlanningPokerResults getResults() {
		return results;
	}

}
