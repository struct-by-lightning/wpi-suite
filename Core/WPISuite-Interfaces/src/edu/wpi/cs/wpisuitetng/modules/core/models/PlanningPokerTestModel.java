package edu.wpi.cs.wpisuitetng.modules.core.models;

import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

public class PlanningPokerTestModel extends RegularAbstractModel<PlanningPokerTestModel> {
	String data;
	public static String primaryKey = "data";
	
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

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void setID(String toSet) {
		// TODO Auto-generated method stub
		
	}
}
