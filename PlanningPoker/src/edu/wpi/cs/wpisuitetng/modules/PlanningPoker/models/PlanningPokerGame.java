package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.models;

import java.util.ArrayList;
import java.util.List;

import sun.util.calendar.Gregorian;
import edu.wpi.cs.wpisuitetng.modules.RegularAbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementmanager.models.Requirement;

public class PlanningPokerGame extends RegularAbstractModel<PlanningPokerGame>{

        private String gameName, description, deckType;
        private List<Requirement> requirements;
        private boolean isFinished, isLive;
        private Gregorian startDate, endDate;
        public PlanningPokerGame(String gameName, String description,
        		String deckType, List<Requirement> requirements,
        		boolean isFinished, boolean isLive, Gregorian startDate,
        		Gregorian endDate) {
                super();
                this.requirements = new ArrayList<Requirement>();
                
                this.gameName = gameName;
                this.setDescription(description);
                this.setDeckType(deckType);
                for(Requirement r : requirements) {
                	this.addRequirement(r);
                }
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
	
	public List<Requirement> getRequirements() {
		return this.requirements;
	}
	
	public void addRequirement(Requirement requirement) {
		this.requirements.add(requirement);
	}
	
	public void removeRequirement(Requirement requirement) {
		this.requirements.remove(requirement);
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
