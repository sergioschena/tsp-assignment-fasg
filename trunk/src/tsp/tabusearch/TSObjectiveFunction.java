package tsp.tabusearch;

import tsp.model.City;

public class TSObjectiveFunction {
	
	int[][] distances;
	int nodes;
	
	public int cost(City a, City b) {
		return distances[a.getCity()][b.getCity()];
	}
	
	

}
