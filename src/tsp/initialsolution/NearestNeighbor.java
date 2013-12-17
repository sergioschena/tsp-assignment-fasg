package tsp.initialsolution;

import tsp.model.*;

import java.util.*;

public class NearestNeighbor implements InitialSolutionGenerator{
	
	private City[] cities;
	private int n;
	private CityManager cm;
	private City [] solution;
	

	public NearestNeighbor(CityManager cm){
		this.cm = cm;
		this.cities=cm.getCities();
		this.n=cities.length;
		solution = new City[n];
		
	}

	//@Override
	public City[] generate() { 
	
		int counter = 0;
		City c = cities[0]; //inizio dalla prima citta'
		c.visit(true);
		solution[counter++] = c;
		
		while (counter<n){
			City[] closer = cm.bestCurrentNearestOf(c); 
			
			solution[counter++]=closer[0];
			closer[0].visit(true);
			c=closer[0];
		}
		
		return solution;
	}
	
	
	//@Override
	public City[] generate(int k) {
		
		int counter = 0;
		City c = cities[0]; //inizio dalla prima citta'
		c.visit(true);
		solution[counter++] = c;
		
		while (counter<n){
			
			City[] kcloser = cm.bestCurrentNearestOf(c);
		
			int l = kcloser.length;
			Random random = new Random();
			int r = random.nextInt(l);
			
			c=kcloser[r];
			solution[counter++]=c;
			c.visit(true);	
		}
		return solution; 
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("Initial solution:\n");
		for(int i = 0; i<n; i++){
			sb.append(solution[i].city + " - ");
		}
		return sb.toString();
	}

}
