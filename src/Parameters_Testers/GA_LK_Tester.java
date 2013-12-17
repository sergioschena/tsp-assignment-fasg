package Parameters_Testers;

import tsp.ga.GeneticHybridSolver;
import tsp.model.City;
import tsp.model.CityManager;
import Instances.Instance;
import Solution_Data_Structure.Array_solution;

public class GA_LK_Tester implements Tester {
	
	//istanza su cui si effettua il test
	Instance tsp_instance;
	
	CityManager manager;
	
	int runs_number = 10;
	
	long[] exploring_times;
	
	long[] improving_times;
	
	int[] tour_lengths;
	
	int min_tour_length = Integer.MAX_VALUE;
	
	long best_solution_time;
	
	int max_tour_length = 0;
	
	long[] solution_times;
	
	long[] initial_solution_times;
	
	long[] explorer_times;
	
	long[] intensifier_times;
	
//-------------------------------------------------------------------------------------
//	Parametri
//-------------------------------------------------------------------------------------
	
	private int populationSize;
	private int eliteCount;
	private int generationCount;
	
	private int maxGlobalIterations;
	
	private int max_t1;	
	private int max_y1;	
	private int max_y2;
	private int max_yi;
	private int max_lambda;	
//-------------------------------------------------------------------------------------
//	Costruttore e setter
//-------------------------------------------------------------------------------------
	
	public GA_LK_Tester(Instance tsp_instance, CityManager manager){
		this.tsp_instance = tsp_instance;
		this.manager = manager;
	}
	
	@Override
	public void setTotalRuns(int runs_number) {
		this.runs_number = runs_number;
	}
	
	//metodo per configurare i parametri dell'algoritmo LK
	public void setParam(int populationSize, int eliteCount, int generationCount, int maxGlobalIterations, int max_t1, int max_y1, int max_y2, int max_yi, int max_lambda){
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
	
//-------------------------------------------------------------------------------------
//	Metodi
//-------------------------------------------------------------------------------------
	
	@Override
	public void updateTests() {
		
		City[] cities = manager.getCities();
		
		initial_solution_times = new long[runs_number];
		solution_times = new long[runs_number];
		intensifier_times = new long[runs_number];
		explorer_times = new long[runs_number];
		exploring_times = new long[runs_number];
		tour_lengths = new int[runs_number];
		
		min_tour_length = Integer.MAX_VALUE;
		max_tour_length = Integer.MIN_VALUE;
		
		//TODO come fare a calcolare il tempo impegato da improve() ?
		
		for(int i = 0; i<runs_number; i++){
			
			Dummy_generator generator = new Dummy_generator(cities);
			
			//tempo impiegato nella costruzione della prima soluzione
			long start_init_sol = System.currentTimeMillis();
			
			City[] init_sol = generator.generate();
			
			initial_solution_times[i] = System.currentTimeMillis() - start_init_sol;
			
			//tempo impiegato per creare una soluzione
			long start_sol = System.currentTimeMillis();
			
			Array_solution solution = new Array_solution(init_sol, manager);
			
			//TwoLevelTree solution = new TwoLevelTree(manager, init_sol, 32);
			
			solution_times[i] = System.currentTimeMillis() - start_sol;
			
			//tempo impiegato per costruire un esploratore/intensificatore
			long start_explorer = System.currentTimeMillis();
			
			GeneticHybridSolver explorer = new GeneticHybridSolver(manager, 15, solution);
			
			explorer_times[i] = System.currentTimeMillis() - start_explorer;
			
			//configurazione dell'esploratore
			//explorer.setParameters(populationSize, eliteCount, generationCount, maxGlobalIterations, 
			//		maxIntensifierIterations, maxIntensifierNotImprovingIterations, startTenure);
			explorer.setParameters(populationSize, eliteCount, generationCount, maxGlobalIterations, max_t1, max_y1, max_y2, max_yi, max_lambda);			
			
			//tempo impiegato per l'esplorazione
			long start_exploring = System.currentTimeMillis();
			
			//TSSolution best_solution = (TSSolution) explorer.explore();
			Array_solution best_solution = (Array_solution) explorer.explore(); 
			
			//TwoLevelTree best_solution = (TwoLevelTree) explorer.explore();
			
			exploring_times[i] = System.currentTimeMillis() - start_exploring;
			
			//lunghezza della soluzione ottenuta
			tour_lengths[i] = best_solution.length();
			
			if(tour_lengths[i]<min_tour_length){
				min_tour_length = tour_lengths[i];
				best_solution_time = exploring_times[i];
			}
			
			if(tour_lengths[i]>max_tour_length)
				max_tour_length = tour_lengths[i];
			
		}
		
		

	}
	
	@Override
	public long getAVGExploringTime() {
		if(exploring_times.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : exploring_times)
			avg += l;
		
		return avg/(long)exploring_times.length;
	}

	@Override
	public long getAVGImprovingTime() {
		if(improving_times.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : improving_times)
			avg += l;
		
		return avg/(long)improving_times.length;
	}

	@Override
	public int getAVGTourLength() {
		if(tour_lengths.length==0)
			return -1;
		
		int avg = 0;
		for(Integer l : tour_lengths)
			avg += l;
		
		return avg/tour_lengths.length;
	}

	@Override
	public int getMINTourLength() {
		if(tour_lengths.length==0)
			return -1;
		
		return min_tour_length;
	}

	@Override
	public int getMAXTourLength() {
		if(tour_lengths.length==0)
			return -1;
		
		return max_tour_length;
	}

	@Override
	public double getErrorFromOptimum() {
		double avg = (double)getAVGTourLength();
		if(avg<0)
			return -1;
		
		double opt = (double)tsp_instance.getOptimum();
		
		double diff = avg - opt;
		
		return diff/opt;
	}

	@Override
	public long getAVGSolutionTime() {
		if(solution_times.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : solution_times)
			avg += l;
		
		return avg/(long)solution_times.length;
	}

	@Override
	public long getAVGInitialSolutionTime() {
		if(initial_solution_times.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : initial_solution_times)
			avg += l;
		
		return avg/(long)initial_solution_times.length;
	}

	@Override
	public long getAVGExplorerConstructionTime() {
		if(explorer_times.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : explorer_times)
			avg += l;
		
		return avg/(long)explorer_times.length;
	}

	@Override
	public long getAVGIntensifierTime() {
		if(intensifier_times.length==0)
			return -1;
		
		long avg = 0;
		for(Long l : intensifier_times)
			avg += l;
		
		return avg/(long)intensifier_times.length;
	}

	@Override
	public double getMINErrorFromOptimum() {
		double min = (double)getMINTourLength();
		if(min<0)
			return -1;
		
		double opt = (double)tsp_instance.getOptimum();
		
		double diff = min - opt;
		
		return diff/opt;
	}

	@Override
	public long getTimeofBestSolution() {
		if(exploring_times.length==0)
			return -1;
		
		return best_solution_time;
	}

}