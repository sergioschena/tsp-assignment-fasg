package Initial_Solution;

import tsp.model.*;

public class NearestNeighbor implements InitialSolutionGenerator{
	
	private City[] cities;
	private int n;
	private City[] solution;

	public NearestNeighbor(City[] cities){
		this.cities=cities;
		this.n=cities.length;
		solution = new City[n];
	}

	@Override
	public Solution generate() {
		
		int counter = 0;
		CityManager cm = new CityManager(cities);
		City c = cities[0]; //inizio dalla prima citta'
		c.visit(true);
		solution[counter] = c;
		
		
		while (counter<n){
			counter++;
			City[] closer = cm.getNearest(c);
			for(int i=0; i<closer.length; i++){
				if (closer[i].visited == false){
					solution[counter]=closer[i];
					closer[i].visit(true);
					c=closer[i];
					break; //devo andare al while 
				}
			}
		}
		
		
		return null;
	}

	@Override
	public Solution generate(int k) {
		// TODO Auto-generated method stub
		return null;
	}

}
