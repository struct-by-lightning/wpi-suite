package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.core.models.UserDeserializer;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.controller.GetRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.RequirementModel;

public class PlanningPokerDeserializer implements JsonDeserializer<PlanningPokerGame> {

	private static final Logger logger = Logger
			.getLogger(UserDeserializer.class.getName());

	@Override
	public PlanningPokerGame deserialize(JsonElement ppmElement, Type ppmType,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject deflated = ppmElement.getAsJsonObject();

		if (!deflated.has("gameName")) {
			throw new JsonParseException(
					"The serialized PlanningPokerModel did not contain the required gameName field.");
		}

		if (!deflated.has("requirements")) {
			throw new JsonParseException(
					"The serialized PlanningPokerModel did not contain the required requirements field.");
		}
		
		if (!deflated.has("isFinished")) {
			throw new JsonParseException(
					"The serialized PlanningPokerModel did not contain the required isFinished field.");
		}
		
		if (!deflated.has("isLive")) {
			throw new JsonParseException(
					"The serialized PlanningPokerModel did not contain the required isLive field.");
		}
		
		if (!deflated.has("startDate")) {
			throw new JsonParseException(
					"The serialized PlanningPokerModel did not contain the required startDate field.");
		}
		
		if (!deflated.has("endDate")) {
			throw new JsonParseException(
					"The serialized PlanningPokerModel did not contain the required endData field.");
		}

		// for all other attributes: instantiate as null, fill in if given.

		String gameName = deflated.get("gameName").getAsString();
		String description = null;
		String deckType = null;
		List<Requirement> requirements = new ArrayList<Requirement>();
		boolean isFinished = false;
		boolean isLive = false;
		GregorianCalendar startDate = null;
		GregorianCalendar endDate = null;

		if (deflated.has("deckType")
				&& !deflated.get("deckType").getAsString().equals("")) {
			deckType = deflated.get("deckType").getAsString();
		}
		
		JsonArray jsonRequirements = null;
		
		try {
			jsonRequirements = deflated.get("requirements").getAsJsonArray();
		} catch (java.lang.ClassCastException e) {
			logger.log(Level.FINER,
					"PlanningPokerModel transmitted with non-array in requirements field");
		}
		
		GetRequirementsController.getInstance().retrieveRequirements();

		try {	// We need to sleep for the requirement request to be in
			Thread.sleep(150);
		} catch (InterruptedException e1) {}

		for(JsonElement jsonRequirement : jsonRequirements) {
			Requirement r = RequirementModel.getInstance().getRequirement(jsonRequirement.getAsInt());
			if(r != null) {
				requirements.add(r);
			}
		}
		
		try {
			isFinished = deflated.get("isFinished").getAsBoolean();
		} catch (java.lang.ClassCastException e) {
			logger.log(Level.FINER,
					"PlanningPokerModel transmitted with String in isFinished field");
		}
		
		try {
			isLive = deflated.get("isLive").getAsBoolean();
		} catch (java.lang.ClassCastException e) {
			logger.log(Level.FINER,
					"PlanningPokerModel transmitted with String in isLive field");
		}

		DateFormat df = new SimpleDateFormat("dd MM yyyy");

		try {
			Date date = df.parse(deflated.get("startDate").getAsString());
			startDate = new GregorianCalendar();
			startDate.setTime(date);
		} catch (java.text.ParseException e) {
			logger.log(Level.FINER,
					"PlanningPokerModel transmitted with String in startDate field");
			startDate = null;
		}
		
		try {
			Date date = df.parse(deflated.get("endDate").getAsString());
			endDate = new GregorianCalendar();
			endDate.setTime(date);
		} catch (java.text.ParseException e) {
			logger.log(Level.FINER,
					"PlanningPokerModel transmitted with String in endDate field");
			endDate = null;
		}

		PlanningPokerGame inflated = new PlanningPokerGame(gameName, description,
				deckType, requirements, isFinished, isLive, startDate, endDate);

		return inflated;
	}

}
