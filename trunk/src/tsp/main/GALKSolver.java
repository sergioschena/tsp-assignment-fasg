package tsp.main;

import java.util.Map;

import tsp.ga.GeneticHybridSolver;
import tsp.model.CityManager;
import tsp.model.Solution;
import Instances.Instance;
import Solution_Data_Structure.Array_solution;

public class GALKSolver {
	
	Instance tspInstance;	
	CityManager manager;	
	int repetitions = 10;	
	
	long[] exploringTimes;	
	long[] improvingTimes;
	long[] solutionTimes;	
	long[] initialSolutionTimes;	
	long[] explorerTimes;	
	long[] intensifierTimes;	
	
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
	
	private Solution type;

	public GALKSolver(Instance tsp_instance, CityManager manager, Solution type){
		this.tspInstance = tsp_instance;
		this.manager = manager;
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
		
		setParam(populationSize, eliteCount, generationCount, maxGlobalIterations, maxT1, maxY1, maxY2, maxYi, maxLambda);
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
		
		initialSolutionTimes = new long[repetitions];
		solutionTimes = new long[repetitions];
		intensifierTimes = new long[repetitions];
		explorerTimes = new long[repetitions];
		exploringTimes = new long[repetitions];
		tourLengths = new int[repetitions];
		
		minTourLength = Integer.MAX_VALUE;
		maxTourLength = Integer.MIN_VALUE;
		
		for(int i = 0; i<repetitions; i++){
			
			// FIXME: serve???
			//tempo impiegato nella costruzione della prima soluzione
			long start_init_sol = System.currentTimeMillis();
			
			initialSolutionTimes[i] = System.currentTimeMillis() - start_init_sol;
			
			// FIXME: serve???
			//tempo impiegato per creare una soluzione
			long start_sol = System.currentTimeMillis();
			
			solutionTimes[i] = System.currentTimeMillis() - start_sol;
			
			//tempo impiegato per costruire un esploratore/intensificatore
			long start_explorer = System.currentTimeMillis();
			
			GeneticHybridSolver explorer = new GeneticHybridSolver(manager, 15, type);			
			explorerTimes[i] = System.currentTimeMillis() - start_explorer;
			
			//configurazione dell'esploratore
			explorer.setParameters(populationSize, eliteCount, generationCount, maxGlobalIterations, max_t1, max_y1, max_y2, max_yi, max_lambda);			
			
			//tempo impiegato per l'esplorazione
			long start_exploring = System.currentTimeMillis();
			
			Array_solution best_solution = (Array_solution) explorer.explore();
			
			exploringTimes[i] = System.currentTimeMillis() - start_exploring;
			
			//lunghezza della soluzione ottenuta
			tourLengths[i] = best_solution.length();
			
			if(tourLengths[i]<minTourLength){
				minTourLength = tourLengths[i];
				bestSolutionTime = exploringTimes[i];
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

	public long getAVGImprovingTime() {
		if(improvingTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : improvingTimes)
			avg += l;
		
		return avg/(long)improvingTimes.length;
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

	// FIXME: eliminare
	public double getErrorFromOptimum() {
		double avg = (double)getAVGTourLength();
		if(avg<0)
			return -1;
		
		double opt = (double)tspInstance.getOptimum();
		
		double diff = avg - opt;
		
		return diff/opt;
	}

	public long getAVGSolutionTime() {
		if(solutionTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : solutionTimes)
			avg += l;
		
		return avg/(long)solutionTimes.length;
	}

	public long getAVGInitialSolutionTime() {
		if(initialSolutionTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : initialSolutionTimes)
			avg += l;
		
		return avg/(long)initialSolutionTimes.length;
	}

	public long getAVGExplorerConstructionTime() {
		if(explorerTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : explorerTimes)
			avg += l;
		
		return avg/(long)explorerTimes.length;
	}

	public long getAVGIntensifierTime() {
		if(intensifierTimes.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : intensifierTimes)
			avg += l;
		
		return avg/(long)intensifierTimes.length;
	}

	//FIXME: eliminare
	public double getMINErrorFromOptimum() {
		double min = (double)getMINTourLength();
		if(min<0)
			return -1;
		
		double opt = (double)tspInstance.getOptimum();
		
		double diff = min - opt;
		
		return diff/opt;
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