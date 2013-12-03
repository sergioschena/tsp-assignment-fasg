package tsp.tabusearch;

import tsp.model.Solution;

public class BestEverAspirationCriteria implements AspirationCriteria {

	static final BestEverAspirationCriteria instance = new BestEverAspirationCriteria();
	Solution bestSolution = null;
	
	private BestEverAspirationCriteria(){
		
	}
	
	public static BestEverAspirationCriteria getInstance(){
		return instance;
	}
	
	public void setBestSolution(Solution newBest){
		this.bestSolution = newBest;
	}
	
	@Override
	public boolean isSatisfiedBy(Solution s, Move m) {
		if( (s.length() + m.evaluate()) < bestSolution.length() ){
			return true;
		}else{
			return false;
		}
	}

}
