package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.model;

public class PlanningPokerConfiguration {
	private int[] users_participating;
	private PlanningPokerGameTypes gameType;
	private int timeLimit;
	private int userStoryId;
	private int[] estimationOptions;
	private int moderator;
	private boolean isActive;
	
	/**
	 * Constructs the Planning Poker configuration for the session
	 * 
	 * @param users_participating
	 * @param gameType
	 * @param timeLimit
	 * @param userStoryId
	 * @param estimationOptions
	 * @param moderator
	 * @param isActive
	 */
	public PlanningPokerConfiguration(int[] users_participating,
			PlanningPokerGameTypes gameType, int timeLimit, int userStoryId,
			int[] estimationOptions, int moderator, boolean isActive) {
		this.users_participating = users_participating;
		this.gameType = gameType;
		this.timeLimit = timeLimit;
		this.userStoryId = userStoryId;
		this.estimationOptions = estimationOptions;
		this.moderator = moderator;
		this.isActive = isActive;
	}
	
	public int[] getUsers_participating() {
		return users_participating;
	}
	public PlanningPokerGameTypes getGameType() {
		return gameType;
	}
	public int getTimeLimit() {
		return timeLimit;
	}
	public int getUserStoryId() {
		return userStoryId;
	}
	public int[] getEstimationOptions() {
		return estimationOptions;
	}
	public int getModerator() {
		return moderator;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setUsers_participating(int[] users_participating) {
		this.users_participating = users_participating;
	}
	public void setGameType(PlanningPokerGameTypes gameType) {
		this.gameType = gameType;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	public void setUserStoryId(int userStoryId) {
		this.userStoryId = userStoryId;
	}
	public void setEstimationOptions(int[] estimationOptions) {
		this.estimationOptions = estimationOptions;
	}
	public void setModerator(int moderator) {
		this.moderator = moderator;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}	
}
