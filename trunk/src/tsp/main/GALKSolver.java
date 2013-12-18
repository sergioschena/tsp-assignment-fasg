package tsp.main;

import java.util.Map;

import Solution_Data_Structure.Array_solution;
import tsp.ga.GeneticHybridSolver;
import tsp.initialsolution.Instance;
import tsp.model.CityManager;
import tsp.model.Solution;

public class GALKSolver {
	
	private Instance tspInstance;	
	//CityManager manager;	
	private int repetitions = 10;
	private int K = 15;
	
	long[] exploringTimes;
	long[] solutionTimes;		
	long[] explorerTimes;	
	
	int[] tourLengths;	
	int minTourLength = Integer.MAX_VALUE;	
	long bestSolutionTime;
	Solution bestSolution;
	
	int maxTourLength = 0;
	
	// Parametri	
	private int populationSize;
	private int eliteCount;
	private int generationCount;
	
	private int maxGlobalIterations;
	
	private int max_t1;	
	private int max_y1;	
	private int max_y2;
	private int max_yi;
	private int max_lambda;	

	public GALKSolver(Instance tsp_instance, int K){
		this.tspInstance = tsp_instance;
		this.K = K;
		//this.manager = manager;
	}
	
	public void setTotalRuns(int runs_number) {
		this.repetitions = runs_number;
	}
	
	public void setParamByMap(Map<String,String> params){
		int populationSize = (int) Double.parseDouble(params.get("GAPopulationSize"));
		int eliteCount = (int) Double.parseDouble(params.get("GAEliteCount"));
		int generationCount = (int) Double.parseDouble(params.get("GAGenerationCount"));
		int maxGlobalIterations = (int) Double.parseDouble(params.get("GAMaxGlobalIterations"));
		int maxT1 = (int) Double.parseDouble(params.get("LKMaxT1"));
		int maxY1 = (int) Double.parseDouble(params.get("LKMaxY1"));
		int maxY2 = (int) Double.parseDouble(params.get("LKMaxY2"));
		int maxYi = (int) Double.parseDouble(params.get("LKMaxYi"));
		int maxLambda = (int) Double.parseDouble(params.get("LKMaxLambda"));
		
		int repetions = (int) Double.parseDouble(params.get("Repetitions"));
		
		setParam(populationSize, eliteCount, generationCount, maxGlobalIterations, maxT1, maxY1, maxY2, maxYi, maxLambda);
		setTotalRuns(repetions);
	}
	
	public void setParam(int populationSize, int eliteCount, int generationCount, int maxGlobalIterations, 
			int max_t1, int max_y1, int max_y2, int max_yi, int max_lambda){
		this.populationSize = populationSize;
		this.eliteCount = eliteCount;
		this.generationCount = generationCount;
		
		this.maxGlobalIterations = maxGlobalIterations;
		
		this.max_t1 = max_t1;
		this.max_y1 = max_y1;
		this.max_y2 = max_y2;
		this.max_yi = max_yi;
		this.max_lambda = max_lambda;
	}
	
	public void solve() {
		
		solutionTimes = new long[repetitions];		
		explorerTimes = new long[repetitions];
		exploringTimes = new long[repetitions];
		tourLengths = new int[repetitions];
		
		minTourLength = Integer.MAX_VALUE;
		maxTourLength = Integer.MIN_VALUE;
		
		for(int i = 0; i<repetitions; i++){
			
			//tempo impiegato per costruire un esploratore/intensificatore
			long start_time = System.currentTimeMillis();
			
			CityManager manager = new CityManager(tspInstance.getCitiesArr(), K);			
			Solution type = new Array_solution(tspInstance.getCitiesArr(), manager);
			GeneticHybridSolver explorer = new GeneticHybridSolver(manager, K, type);			
			
			explorerTimes[i] = System.currentTimeMillis() - start_time;
			
			//configurazione dell'esploratore
			explorer.setParameters(populationSize, eliteCount, generationCount, maxGlobalIterations, max_t1, max_y1, max_y2, max_yi, max_lambda);			
			
			//tempo impiegato per l'esplorazione
			long start_exploring = System.currentTimeMillis();
			
			Solution best_solution = explorer.explore();
			
			long end_exploring = System.currentTimeMillis();
			exploringTimes[i] = end_exploring - start_exploring;
			solutionTimes[i] = end_exploring - start_time;
			
			//lunghezza della soluzione ottenuta
			tourLengths[i] = best_solution.length();
			
			if(tourLengths[i]<minTourLength){
				minTourLength = tourLengths[i];
				bestSolutionTime = solutionTimes[i];
				bestSolution = best_solution;
				
			}
			
			if(tourLengths[i]>maxTourLength)
				maxTourLength = tourLengths[i];
			
		}
		
	}
	
	public long getAVGExploringTime() {
		if(exploringTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : exploringTimes)
			avg += l;
		
		return avg/(long)exploringTimes.length;
	}

	public int getAVGTourLength() {
		if(tourLengths.length==0)
			return -1;
		
		int avg = 0;
		for(Integer l : tourLengths)
			avg += l;
		
		return avg/tourLengths.length;
	}

	public int getMINTourLength() {
		if(tourLengths.length==0)
			return -1;
		
		return minTourLength;
	}

	public int getMAXTourLength() {
		if(tourLengths.length==0)
			return -1;
		
		return maxTourLength;
	}

	public long getAVGSolutionTime() {
		if(solutionTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : solutionTimes)
			avg += l;
		
		return avg/(long)solutionTimes.length;
	}

	public long getAVGExplorerConstructionTime() {
		if(explorerTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : explorerTimes)
			avg += l;
		
		return avg/(long)explorerTimes.length;
	}

	public long getTimeofBestSolution() {
		if(exploringTimes.length==0)
			return -1;
		
		return bestSolutionTime;
	}
	
	public Solution getBestSolution(){
		return bestSolution;
	}

}