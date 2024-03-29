package tsp.pso;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import tsp.data.twoleveltree.TwoLevelTree;
import tsp.lk.LKIntesifier;
import tsp.model.*;

public class Test_PSO {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		City cities[] = tsp.ga.KnownInstances.createPr1002();
		
		//City cities[] = tsp.ga.KnownInstances.createRat195();
		
		System.out.println("Start manager constuction\n");
		
		CityManager manager = new CityManager(cities);
		
		System.out.println("Start initial solution constuction\n");
		
		Dummy_generator generator = new Dummy_generator(cities);
		
		//Dummy_Intensifier dummy = new Dummy_Intensifier();
		
		System.out.println("Start intensifier constuction\n");
		
		LKIntesifier dummy = new LKIntesifier(manager);
		
		dummy.setParam(100, 15, 5, 15, 750);
		
		System.out.println("Start solution constuction\n");
		
		int groupsize = (int) Math.ceil(Math.sqrt(manager.n));
		
		TwoLevelTree solution_type = new TwoLevelTree(manager, cities, groupsize);
		
		System.out.println("Start explorer constuction\n");
		
		PSOExplorer explorer = new PSOExplorer(manager, generator, dummy, 120, solution_type);
		
		//tempo massimo: 1h
		explorer.configExplorer(0.5, 1.4, 2, 2, 100, 3600000);
		
		System.out.println("Start explore\n");
		
		long start = System.currentTimeMillis();
		
		solution_type = (TwoLevelTree) explorer.explore();
		
		long end = System.currentTimeMillis();
		double secs = (end - start) / 1000.0;
		
		System.out.println(solution_type);
		
		System.out.println("\nTime = "+secs);
		System.out.println("\nLength = "+solution_type.length());
		
		

	}
	
	

}

class Dummy_Intensifier implements Intensifier{

	@Override
	public Solution improve(Solution start) {
		return start;
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
		return null;
	}
	
}


