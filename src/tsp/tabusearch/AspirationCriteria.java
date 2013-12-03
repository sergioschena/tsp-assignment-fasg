package tsp.tabusearch;

import tsp.model.Solution;

public interface AspirationCriteria {
	
	public boolean isSatisfiedBy(Solution s, Move m);
	
	public void setBestSolution(Solution s);
	
}
