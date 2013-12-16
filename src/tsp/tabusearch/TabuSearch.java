package tsp.tabusearch;

import tsp.model.CityManager;
import tsp.model.Intensifier;
import tsp.model.Solution;

public class TabuSearch implements Intensifier {

	static final int DEFAULT_START_TENURE = TSTabuList.DEFAULT_START_TENURE;
	static final int DEFAULT_MAX_ITERATIONS = 1000;
	static final int DEFAULT_MAX_NOT_IMPROVING_ITERATIONS = 10;
	
	CityManager cityManager;
	TSObjectiveFunction objectiveFunction;
	TSTabuList tabuList;
	TSMoveManager moveManager;
	AspirationCriteria aspirationCriteria;
	
	//parameters
	int startTenure;
	int maxNotImprovingIterations;
	int maxIterations;
	
	public int iterations;
	
	public TabuSearch(CityManager cityManager, AspirationCriteria aspirationCriteria, int startTenure, int maxNotImprovingIterations, int maxIterations){
		this.cityManager = cityManager;
		this.aspirationCriteria = aspirationCriteria;
		this.startTenure = (startTenure >= 0 ? startTenure : DEFAULT_START_TENURE);
		this.maxNotImprovingIterations = (maxNotImprovingIterations >= 0 ? maxNotImprovingIterations : DEFAULT_MAX_NOT_IMPROVING_ITERATIONS);
		this.maxIterations = (maxIterations >= 0 ? maxIterations : DEFAULT_MAX_ITERATIONS);
		
		objectiveFunction = new TSObjectiveFunction(cityManager);		
		tabuList = new TSTabuList(aspirationCriteria,startTenure);
		moveManager = new TSMoveManager(tabuList, objectiveFunction, cityManager);
	}
	
	public void setParams(int startTenure, int maxNotImprovingIterations, int maxIterations){
		this.startTenure = (startTenure >= 0 ? startTenure : DEFAULT_START_TENURE);
		this.maxNotImprovingIterations = (maxNotImprovingIterations >= 0 ? maxNotImprovingIterations : DEFAULT_MAX_NOT_IMPROVING_ITERATIONS);
		this.maxIterations = (maxIterations >= 0 ? maxIterations : DEFAULT_MAX_ITERATIONS);
		
		tabuList = new TSTabuList(aspirationCriteria,startTenure);
		moveManager = new TSMoveManager(tabuList, objectiveFunction, cityManager);
	}
	
	@Override
	public Solution improve(Solution start) {		
		Solution best;
		Solution current;
		Move move;
		int iterations;
		int notImprovingIterations;
		int prevLength;
		
		tabuList.initialize();
		moveManager.initialize();
		
		objectiveFunction.evaluate(start);
		current = (Solution) start.clone();
		best = (Solution) current.clone();
		aspirationCriteria.setBestSolution(best);
		prevLength = objectiveFunction.evaluate(best);
		
		for(iterations = 0, notImprovingIterations = 0; iterations < maxIterations && notImprovingIterations <= maxNotImprovingIterations; iterations++, tabuList.nextIteration()){
		
			move = moveManager.nextMoveTrunc3(current);
			//move = moveManager.nextMoveTrunc3DontLook(current);

			if(move == null){
				notImprovingIterations++;
				continue;
			}
			
			move.operateOn(current);

			if(current.length() < prevLength ){
				prevLength = current.length();
				best = (Solution) current.clone();
				aspirationCriteria.setBestSolution(best);
				notImprovingIterations = 0;
			}else{
				notImprovingIterations++;
			}
				
			tabuList.addTabu(move);
		}
		
		this.iterations = iterations;
		
		return best;
	}

}
