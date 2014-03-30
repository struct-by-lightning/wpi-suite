package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import sun.util.calendar.Gregorian;
import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;

public class PlanningPokerModel extends RegularAbstractModel<PlanningPokerModel>{

        private String gameName, description, deckType;
        private boolean isFinished, isLive;
        private Gregorian startDate, endDate;
        public PlanningPokerModel(String gameName, String description,
                        boolean isFinished, boolean isLive, Gregorian startDate,
                        Gregorian endDate) {
                super();
                this.gameName = gameName;
                this.setDescription(description);
                this.setFinished(isFinished);
                this.setLive(isLive);
                this.setStartDate(startDate);
                this.setEndDate(endDate);
        }

	@Override
	public String toJSON() {
		PlanningPokerSerializer pps = new PlanningPokerSerializer();
		return pps.serialize(this, null, null).getAsString();
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return this.gameName;
	}

	@Override
	public void setID(String toSet) {
		gameName = toSet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getPrimaryKey() {
		return "gameName";
	}

	public String getDeckType() {
		return deckType;
	}

	public void setDeckType(String deckType) {
		this.deckType = deckType;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public Gregorian getStartDate() {
		return startDate;
	}

	public void setStartDate(Gregorian startDate) {
		this.startDate = startDate;
	}

	public Gregorian getEndDate() {
		return endDate;
	}

	public void setEndDate(Gregorian endDate) {
		this.endDate = endDate;
	}
}
