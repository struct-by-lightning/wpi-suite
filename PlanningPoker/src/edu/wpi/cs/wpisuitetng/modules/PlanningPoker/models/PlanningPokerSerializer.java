package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.lang.reflect.Type;

import sun.util.calendar.Gregorian;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PlanningPokerSerializer implements JsonSerializer<PlanningPokerModel> {

	@Override
	public JsonElement serialize(PlanningPokerModel m, Type t, JsonSerializationContext context) {
		JsonObject deflated = new JsonObject();
		deflated.addProperty("gameName", m.getID());
		deflated.addProperty("description", m.getDescription());
		deflated.addProperty("deckType", m.getDeckType());
		deflated.addProperty("isFinished", m.isFinished());
		deflated.addProperty("isLive", m.isLive());
		deflated.addProperty("startDate", m.getStartDate().toString());
		deflated.addProperty("endDate", m.getEndDate().toString());
		return deflated;
	}

}
