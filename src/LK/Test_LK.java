package LK;

import tsp.model.*;
import tsp.tabusearch.TSSolution;

public class Test_LK {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		City cities[] = createCities();
		
		CityManager manager = new CityManager(cities);
		
		LK_Intesifier intensifier = new LK_Intesifier(manager);
		
		intensifier.setParam(10, 10, 10, 10, 50);
		
		TSSolution solution = new TSSolution(cities);
		
		printDistances(manager);
		
		System.out.println(solution + " -> " + evaluate(solution, manager, cities[0]));
		
		long start = System.currentTimeMillis();
		
		TSSolution best_sol = (TSSolution)intensifier.improve(solution);
		
		long end = System.currentTimeMillis();
		double secs = (end - start) / 1000.0;
		
		System.out.println("Time = "+secs);
		
		System.out.println(best_sol + " -> " + evaluate(best_sol, manager, cities[0]));

	}
	
	public static City[] createCities(){
		
		City cities[] = new City[10];
		
		
		cities[0] = new City(1, 79, 28);
		cities[1] = new City(2, 36, 23);
		cities[2] = new City(3, 42, 76);
		cities[3] = new City(4, 46, 64);
		cities[4] = new City(5, 72, 56);
		cities[5] = new City(6, 91, 85);
		cities[6] = new City(7, 55, 74);
		cities[7] = new City(8, 15, 38);
		cities[8] = new City(9, 83, 4);
		cities[9] = new City(10, 32, 42);
		
		
		/* soluzione ottima
		cities[7] = new City(8, 79, 28);
		cities[9] = new City(10, 36, 23);
		cities[3] = new City(4, 42, 76);
		cities[2] = new City(3, 46, 64);
		cities[6] = new City(7, 72, 56);
		cities[5] = new City(6, 91, 85);
		cities[4] = new City(5, 55, 74);
		cities[0] = new City(1, 15, 38);
		cities[8] = new City(9, 83, 4);
		cities[1] = new City(2, 32, 42);
		*/
		
		return cities;
	}
	
	public static int evaluate(Solution sol, CityManager manager, City c){
		
		int n = manager.n;
		
		City a = c;
		
		int cost = 0;
		
		for(int i = 0; i<n; i++){
			
			City b = sol.next(a);
			
			int cost_ab = manager.cost(a, b);
			
			cost += cost_ab;
			
			a = b;
			
		}
		
		return cost;
	}
	
	public static void printDistances(CityManager manager){
		int n = manager.n;
		
		City[] cities = manager.getCities();
		
		for(int i = 1; i<=n; i++){
			String str = "city "+i+": | ";
			for(int j = 1; j<=n; j++){
				str = str + manager.cost(cities[i-1], cities[j-1])+ " | ";
			}
			System.out.println(str);
		}
		
	}

}
