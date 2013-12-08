package tsp.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class CityManager {
	
	private int[][] distances;
	private City[] cities;	
	private City[][] nearest;
	public int n;
	private int maxNeighbors;
	
	public CityManager(City[] cities){
		this(cities,cities.length-1);
	}
	
	public CityManager(City[] cities, int K){
		this.cities = cities.clone();
		this.n = cities.length;
		this.maxNeighbors = K;
		initDistances();
		initNearest();
	}
	
	public int cost(City a, City b) {
		return distances[a.city-1][b.city-1];
	}
	
	public City[] getCities(){
		return cities.clone();
	}
	
	public City getCity(int city){
		return cities[city-1];
	}
	
	//metodo per la creazione di archi tra città
	public Edge getEdge(City a, City b){
		return new Edge(a, b, cost(a, b));
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
		
		//City[] toSort;
		//CityComparator cmp;
		for(City c: cities){
			//cmp = new CityComparator(c.city-1);
			//toSort = cities.clone();
			//Arrays.sort(toSort,cmp);
			//nearest[c.city-1] = toSort;
					
			nearest[c.city-1] = bestNearestOf(c);
		}
	}

	private static int distance(City a, City b){
		double xd = a.getX() - b.getX();
		double yd = a.getY() - b.getY();
		// TSPLIB requires a nearest-int rounding
		return (int) Math.round(Math.sqrt(xd*xd + yd*yd));
	}
	
	private City[] bestNearestOf(City c){
		LinkedList<City> bestNear = new LinkedList<City>();
		
		int cIdx = c.city - 1;
		int dist;
		boolean onlyAdd = true;
		CityComparator cmp;
		
		for(int i=0; i<this.n; i++){
			if(i != cIdx){
				cmp = new CityComparator(c.city-1);
				dist = distances[cIdx][i];
				
				if(onlyAdd){
					onlyAdd = ((bestNear.size() == (maxNeighbors-1)) ? false : true);
					bestNear.add(cities[i]);
					Collections.sort(bestNear, cmp);
				}else if(distances[cIdx][bestNear.getLast().city-1] > dist){
					bestNear.removeLast();
					bestNear.add(cities[i]);
					Collections.sort(bestNear, cmp);
				}
			}
		}
		
		return bestNear.toArray(new City[0]);
	}
	
	public City[] bestCurrentNearestOf(City c){
		LinkedList<City> bestNear = new LinkedList<City>();
		
		int cIdx = c.city - 1;
		int dist;
		boolean onlyAdd = true;
		CityComparator cmp;
		
		for(int i=0; i<this.n; i++){
			if(i != cIdx){
				cmp = new CityComparator(c.city-1);
				
				if(cities[i].visited) continue;
				
				dist = distances[cIdx][i];
				
				if(onlyAdd){
					onlyAdd = ((bestNear.size() == (maxNeighbors-1)) ? false : true);
					bestNear.add(cities[i]);
					Collections.sort(bestNear, cmp);
				}else if(distances[cIdx][bestNear.getLast().city-1] > dist){
					bestNear.removeLast();
					bestNear.add(cities[i]);
					Collections.sort(bestNear, cmp);
				}
			}
		}
		
		return bestNear.toArray(new City[0]);
	}
	
	private class CityComparator implements Comparator<City>{
		int src;
		
		public CityComparator(int src){
			this.src = src;
		}
		
		@Override
		public int compare(City arg0, City arg1) {
			return (distances[src][arg0.city-1] - distances[src][arg1.city-1]);
		}
		
	}	
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer("Cost Matrix:\n");
		for(int i = 0; i<n; i++){
			sb.append((i+1)+"[ ");
			for(int j = 0; j<n; j++){
				sb.append(distances[i][j]+ " - ");
			}
			sb.append("]\n");
		}
		sb.append("Best neighbors: \n");
		for(int i = 0; i<n; i++){
			sb.append((i+1)+"[ ");
			for(int j = 0; j<nearest[i].length; j++){
				sb.append(nearest[i][j].city+ " - ");
			}
			sb.append("]\n");
		}
		
		return sb.toString();
	}

}