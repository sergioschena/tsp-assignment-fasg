package Initial_Solution;

import tsp.model.*;
import java.util.*;

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
		boolean jump=false;
		CityManager cm = new CityManager(cities);
		City c = cities[0]; //inizio dalla prima citta'
		c.visit(true);
		solution[counter] = c;
		
		while (counter<n){
			counter++;
			City[] closer = cm.getNearest(c); 
			jump=false;
			//leggo il vettore delle citta piu vicine e scelgo la prima non visitata come prossima citta da visitare
			for(int i=0; i<closer.length && jump==false; i++){
				if (closer[i].visited == false){
					solution[counter]=closer[i];
					closer[i].visit(true);
					c=closer[i];
					jump=true; 
				}
			}
		}
		
		
		return null; //TODO deve return Solution (Clonable) ???
	}

	@Override
	public Solution generate(int k) {
		
		int counter = 0;
		boolean jump=false;
		CityManager cm = new CityManager(cities);
		City c = cities[0]; //inizio dalla prima citta'
		c.visit(true);
		solution[counter] = c;
		
		while (counter<n){
			counter++;
			City[] closer = cm.getNearest(c); 
			City[] kcloser = new City[k];
			int j=0;
			
			//leggo il vettore delle citta piu vicine e ne scelgo k tra le non visitate
			for(int i=0; i<closer.length && j<k; i++){ 
				if (closer[i].visited == false){
					kcloser[j]=closer[i];
					j++;
				}
			}
			
			int l = kcloser.length;
			Random random = new Random();
			int r = random.nextInt(l); //generaro un numero random tra 0 (incluso) e l (escluso)
			solution[counter]=kcloser[r];
			kcloser[r].visit(true);
			c=kcloser[r];
		}
		
		return null; //ritornare Solution (Clonable)
	}

}
