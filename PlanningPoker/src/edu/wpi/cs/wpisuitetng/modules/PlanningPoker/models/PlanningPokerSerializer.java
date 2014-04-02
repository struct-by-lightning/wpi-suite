package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class PlanningPokerSerializer implements JsonSerializer<PlanningPokerGame> {

	@Override
	public JsonElement serialize(PlanningPokerGame m, Type t, JsonSerializationContext context) {
		DateFormat df = new SimpleDateFormat("dd MM yyyy");
		JsonObject deflated = new JsonObject();
		JsonArray deflatedReqs = new JsonArray();
		JsonParser jp = new JsonParser();
		
		deflated.addProperty("gameName", m.getID());
		deflated.addProperty("description", m.getDescription());
		deflated.addProperty("deckType", m.getDeckType());
		for(Requirement r : m.getRequirements()) {
			deflatedReqs.add(jp.parse(r.toJSON()));
		}
		deflated.add("requirements", deflatedReqs);
		deflated.addProperty("isFinished", m.isFinished());
		deflated.addProperty("isLive", m.isLive());
		deflated.addProperty("startDate", df.format(m.getStartDate().getTime()));
		deflated.addProperty("endDate", df.format(m.getEndDate().getTime()));
		return deflated;
	}

}
