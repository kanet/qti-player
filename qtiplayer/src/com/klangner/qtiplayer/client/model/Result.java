package com.klangner.qtiplayer.client.model;

public class Result {

	/** Max points in results */
	private int	maxPoints;
	/** scored points */
	private int score;
	
	/**
	 * constructor
	 */
	public Result(){
	
		this.score = 0;
		this.maxPoints = 0;
	}

	/**
	 * constructor
	 */
	public Result(int score, int max){
	
		this.score = score;
		this.maxPoints = max;
	}

	/**
	 * @return score
	 */
	public int getScore(){
		return score;
	}
	
	/**
	 * @return max points
	 */
	public int getMaxPoints(){
		return maxPoints;
	}
	
	/**
	 * Merge data from another result
	 * @param result
	 */
	public void merge(Result result){
		score += result.getScore();
		maxPoints += result.getMaxPoints();
	}
}
