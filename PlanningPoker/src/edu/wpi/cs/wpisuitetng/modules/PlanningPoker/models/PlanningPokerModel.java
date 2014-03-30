package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import sun.util.calendar.Gregorian;
import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

public class PlanningPokerModel extends RegularAbstractModel<PlanningPokerModel>{

	private String gameName, description;
	private boolean isFinished, isLive;
	private Gregorian startDate, endDate;
	private enum deckType {
		Fibonacci
	};
	
	public PlanningPokerModel(String gameName, String description,
			boolean isFinished, boolean isLive, Gregorian startDate,
			Gregorian endDate) {
		super();
		this.gameName = gameName;
		this.description = description;
		this.isFinished = isFinished;
		this.isLive = isLive;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean identify(PlanningPokerModel curious) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}
}
