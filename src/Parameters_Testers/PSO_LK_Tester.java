package Parameters_Testers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.InitialSolutionGenerator;
import Instances.Instance;
import LK.LK_Intesifier;
import PSO.PSO_Explorer;
import Solution_Data_Structure.Array_solution;
import Two_Level_Tree.TwoLevelTree;

public class PSO_LK_Tester implements Tester {
	
	//istanza su cui si effettua il test
	Instance tsp_instance;
	
	CityManager manager;
	
	int runs_number = 10;
	
	long[] exploring_times;
	
	long[] improving_times;
	
	int[] tour_lengths;
	
	int min_tour_length;
	
	long best_solution_time;
	
	int max_tour_length;
	
	long[] solution_times;
	
	long[] initial_solution_times;
	
	long[] explorer_times;
	
	long[] intensifier_times;
	
//-------------------------------------------------------------------------------------
//	Parametri
//-------------------------------------------------------------------------------------
	
	//Parametri LK
	
	//numero di citt� iniziali da valutare al massimo
	//valori di test: 25, 50, 75, 100
	private int max_t1 = 25;
	
	//numero di archi y1 da valutare al massimo
	//valori di test: 5, 15, 35
	private int max_y1 = 5;
	
	//numero di archi y2 da valutare al massimo
	//valori di test: 5, 15, 35
	private int max_y2 = 5;

	//numero di archi yi da valutare al massimo
	//valori di test: 5, 15, 35
	private int max_yi = 5;
	
	//numero massimo di archi scambiabili a ogni iterazione
	//valori di test: 100, 250, 500, 750
	//inversamente proporzionale al numero di citt� iniziali da valutare
	//			max_lambda*max_t1 = 20000
	private int max_lambda = 100;
	
	
	//Parametri PSO
	
	//numero massimo di iterazioni
	//valori di test: 10, 50, 75, 100
	private int max_iter = 10;
	
	//tempo massimo di esplorazione in ms (default: 3min = )
	private long max_exploring_time = 18000;
	
	//numero di particelle
	//valori di test: 10, 50, 75
	private int num_particles = 10;
	
	//fattore di inerzia
	//valori di test: 0.3, 0.6, 0.9
	private double weight = 0.3;
	
	//coefficiente di apprendimento dalla miglior particella locale
	//valori di test: 1.2, 1.7, 2.2
	private double c1 = 1.2;
	
	//coefficiente di apprendimento dalla miglior particella globale
	//valori di test: 1.5, 2, 2.5
	private double c2 = 1.5;
		
	//coefficiente di mutazione
	//valori di test: 1.75, 2.25, 2.75
	private double c3 = 1.75;
	
//-------------------------------------------------------------------------------------
//	Costruttore e setter
//-------------------------------------------------------------------------------------
	
	public PSO_LK_Tester(Instance tsp_instance, CityManager manager){
		this.tsp_instance = tsp_instance;
		this.manager = manager;
	}
	
	@Override
	public void setTotalRuns(int runs_number) {
		this.runs_number = runs_number;
	}
	
	//metodo per configurare i parametri dell'algoritmo LK
	public void setParamLK(int max_t1, int max_y1, int max_y2, int max_yi, int max_lambda){
		this.max_t1 = max_t1;
		this.max_y1 = max_y1;
		this.max_y2 = max_y2;
		this.max_yi = max_yi;
		this.max_lambda = max_lambda;
	}
	
	//metodo per configurare i parametri dell'algoritmo PSO
	public void setParamPSO(int max_iter, long max_expl_time, int num_partcl, double w,
															double c1, double c2, double c3){
		this.max_iter = max_iter;
		this.max_exploring_time = max_expl_time;
		this.num_particles = num_partcl;
		this.weight = w;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
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
			
			//Array_solution solution = new Array_solution(init_sol, manager);
			
			TwoLevelTree solution = new TwoLevelTree(manager, init_sol, 62);
			
			solution_times[i] = System.currentTimeMillis() - start_sol;
			
			//tempo impiegato per costruire un intensificatore
			long start_intensifier = System.currentTimeMillis();
			
			LK_Intesifier intensifier = new LK_Intesifier(manager);
			
			intensifier_times[i] = System.currentTimeMillis() - start_intensifier;
			
			//configurazione dell'intensificatore
			intensifier.setParam(max_t1, max_y1, max_y2, max_yi, max_lambda);
			
			//tempo impiegato per costruire un esploratore
			long start_explorer = System.currentTimeMillis();
			
			PSO_Explorer explorer = new PSO_Explorer(manager, generator, intensifier,
																	num_particles, solution);
			
			explorer_times[i] = System.currentTimeMillis() - start_explorer;
			
			//configurazione dell'esploratore
			explorer.configExplorer(weight, c1, c2, c3, max_iter, max_exploring_time);
			
			//tempo impiegato per l'esplorazione
			long start_exploring = System.currentTimeMillis();
			
			//Array_solution best_solution = (Array_solution) explorer.explore();
			
			TwoLevelTree best_solution = (TwoLevelTree) explorer.explore();
			
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

class Dummy_generator implements InitialSolutionGenerator{
	
	City[] cities;
	
	Dummy_generator(City[] cities) {
		this.cities = cities;
	}

	@Override
	public City[] generate() {
		Random rnd = new Random();
		
		City[] solution = new City[cities.length];
		
		LinkedList<City> city_list = new LinkedList<City>(Arrays.asList(cities));
		
		for(int i = 0; i<cities.length; i++){
			int index = rnd.nextInt(city_list.size());
			City c = city_list.remove(index);
			solution[i] = c;
		}
		
		return solution;
	}

	@Override
	public City[] generate(int k) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
