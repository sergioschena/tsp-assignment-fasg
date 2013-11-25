package tsp.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CityManager {
	
	private int[][] distances;
	private City[] cities;	
	private City[][] nearest;
	private int n;
	
	public CityManager(City[] cities){
		// TODO: cercare di evitare una cazzo di clone
		this.cities = cities.clone();
		this.n = cities.length;
		initDistances();
		initNearest();
	}
	
	public int cost(City a, City b) {
		return distances[a.city-1][b.city-1];
	}
	
	public City[] getNearest(City c){
		return nearest[c.city-1];
	}
	
	public City[] getNearest(int city){
		return nearest[city]; 
	}

	private void initDistances() {
		distances = new int[n][n];
		for(int i = 0; i<n; i++){
			distances[i][i] = 0;
			for(int j = i+1; j<n; j++){
				distances[i][j] = distance(cities[i], cities[j]);
				distances[j][i] = distances[i][j];
			}
		}
	}
	
	private void initNearest() {
		//nearest = new HashMap<Integer,City[]>();
		nearest = new City[n][];
		
		City[] toSort;
		CityComparator cmp;
		for(City c: cities){
			cmp = new CityComparator(c.city);
			toSort = cities.clone();
			Arrays.sort(toSort,cmp);
			nearest[c.city-1] = toSort;
		}
	}

	private static int distance(City a, City b){
		double xd = a.getX() - b.getX();
		double yd = a.getY() - b.getY();
		return (int) Math.sqrt(xd*xd + yd*yd);
	}	
	
	private class CityComparator implements Comparator<City>{
		int src;
		
		public CityComparator(int src){
			this.src = src;
		}
		
		@Override
		public int compare(City arg0, City arg1) {
			int c0 = arg0.city-1;
			int c1 = arg1.city-1;
			return (distances[src][c0] - distances[src][c1]);
		}
		
	}

}