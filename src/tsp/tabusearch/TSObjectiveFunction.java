package tsp.tabusearch;

import java.util.Collection;

import tsp.model.City;

public class TSObjectiveFunction {
	//TODO: change Solution interface according to this implementation
	int[][] distances;
	int nodes;
	
	public TSObjectiveFunction(Collection<City> cities){
		this((City[]) cities.toArray());
	}
	
	public TSObjectiveFunction(City[] cities){
		nodes = cities.length;
		distances = new int[nodes][nodes];
		for(int i = 0; i<nodes; i++){
			distances[i][i] = 0;
			for(int j = i+1; j<nodes; j++){
				distances[i][j] = TSObjectiveFunction.distance(cities[i], cities[j]);
				distances[j][i] = distances[i][j];
			}
		}
	}
	
	public int cost(City a, City b) {
		return distances[a.getCity()-1][b.getCity()-1];
	}
	
	private static int distance(City a, City b){
		int xd = (int) (a.getX() - b.getX());
		int yd = (int) (a.getY() - b.getY());
		return (int) Math.sqrt(xd*xd + yd*yd);
	}
	
	public int evaluate(TSSolution s){
		if(s.length() != Integer.MAX_VALUE)
			return s.length();

		int val = 0;
		City start = s.startFrom();
		City act = start;
		do{
			val += cost(act,s.next(act));
			act = s.next(act);
		}while(!act.equals(start));
		
		s.setLength(val);
		
		return val;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer("Cost Matrix:\n");
		for(int i = 0; i<nodes; i++){
			sb.append("[ ");
			for(int j = 0; j<nodes; j++){
				sb.append(distances[i][j]+ " - ");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}

}
