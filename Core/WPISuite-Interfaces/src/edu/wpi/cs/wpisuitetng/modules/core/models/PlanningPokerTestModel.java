package edu.wpi.cs.wpisuitetng.modules.core.models;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

public class PlanningPokerTestModel extends RegularAbstractModel<PlanningPokerTestModel> {
	String data;
	
	public PlanningPokerTestModel(String data) {
		this.data = data;
	}

	@Override
	public String toJSON() {
		return "{ data : \"" + data + "\" }";
	}
	
	public static PlanningPokerTestModel fromJSON(String json) {
		return new PlanningPokerTestModel(json.substring(10, json.length()-3));
	}

	@Override
	public Boolean identify(PlanningPokerTestModel curious) {
		return this.data.equals(curious.data);
	}

	public String getData()
	{
		return data;
	}
}
