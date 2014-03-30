package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.model;

import java.util.Date;
import java.util.HashMap;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
//import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;


/**
 * @author atrose
 */
public class PlanningPokerGame extends AbstractModel
{
	// A primary key given uniquely to each session.
	private int id;

	// User who is allowed to moderate this game - i.e., whoever created the game.
	private User moderator;

	// All Users who are requested for estimates for this game.
	private User[] players;

	// The requirements this planning poker game is estimating for.
//	private Requirement requirements;

	// The values each player is allowed to choose from for their estimate.
	private int[] default_deck;

	// A mapping of individual players to their estimations, all mapped to each
	// different requirement the team estimates on.
//	private HashMap<Requirement, HashMap<User, Integer>> estimates;

	// A time limit for this session - after which player will be unable to
	// modify their estimates. A null `time_limit` means the session will run
	// until the moderator ends it, or until everyone has responded.
	private int time_limit;

	// The date/time this session was created at.
	private Date date_time_created;

	// Indicates whether this session is still open for users to create/modify
	// their estimates.
	private boolean is_active;

	/**
	 * TODO
	 **/
	// public PlanningPokerGame() {
	// }

	/**
	 * TODO
	 **/
	// public void end_session() {
		// this.is_active = false;
		// this.results = PlanningPokerGameHandler.compute_game_results(this);
	// }

	/**
	 * Save a new estimate from someone to the game.
	 **/
//	public void add_new_estimate(Requirement requirement, User user, int new_estimate) {
//		HashMap inner_map = new HashMap<User, Integer>();
//		inner_map.put(user, new Integer(new_estimate));
//		this.estimates.put(requirement, inner_map);
//	}

	/**
	 * TODO
	 **/
	// private void send_email_notifications(){};

	/* Serializing */

	/**
	 * TODO: Right now this is a copy of the same method from the `User` model.
	 */
	// public String toJSON()
	// {
	// 	String json;

	// 	Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer()).create();

	// 	json = gson.toJson(this, User.class);

	// 	return json;
	// }

	/**
	 * TODO: Right now this is a copy of the same method from the `User` model.
	 */
	// public static User fromJSON(String json) {
	// 	// build the custom serializer/deserializer
	// 	Gson gson;
	// 	GsonBuilder builder = new GsonBuilder();
	// 	builder.registerTypeAdapter(User.class, new UserDeserializer());

	// 	gson = builder.create();

	// 	return gson.fromJson(json, User.class);
	// }


	/* Built-in overrides/overloads */

	/**
	 * Override of toString() to return a JSON string.
	 */
	public String toString()
	{
		return this.toJSON();
	}

	// NOTE: I'm unclear on why we need an `identify()` method in addition to
	// the `equals()` method.
	//
	// @Override
	// public Boolean identify(Object o)
	// {
	// 	Boolean b  = false;

	// 	if(o instanceof User)
	// 		if(((User) o).username.equalsIgnoreCase(this.username))
	// 			b = true;

	// 	if(o instanceof String)
	// 		if(((String) o).equalsIgnoreCase(this.username))
	// 			b = true;
	// 	return b;
	// }

	@Override
	public boolean equals(Object other) {
		if(other instanceof User)
		{
			if( ((PlanningPokerGame)other).id == this.id)
			{
				return true;
			}
		}
		return false;
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
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}
