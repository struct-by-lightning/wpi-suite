package edu.wpi.cs.wpisuitetng.modules.PlanningPoker.model;

public class PlanningPokerResults {
	
	private final double mean;
	private final double median;
	private final double mode;
	private final double standardDeviation;
	private final int max;
	private final int min;
	private final int officialEstimation;
	
	/**
	 * Constructs the results for the Planning Poker session
	 * 
	 * @param mean
	 * @param median
	 * @param mode
	 * @param standardDeviation
	 * @param max
	 * @param min
	 * @param officialEstimation
	 */
	public PlanningPokerResults (double mean, double median, double mode, double standardDeviation, int max, int min, int officialEstimation){
		this.mean = mean;
		this.median = median;
		this.mode = mode;
		this.standardDeviation = standardDeviation;
		this.max = max;
		this.min = min;
		this.officialEstimation = officialEstimation;
	}

	public double getMean() {
		return mean;
	}

	public double getMedian() {
		return median;
	}

	public double getMode() {
		return mode;
	}

	public double getStandaDeviation() {
		return standardDeviation;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public int getOfficialEstimation() {
		return officialEstimation;
	}
}
