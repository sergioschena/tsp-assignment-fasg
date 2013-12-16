package Initial_Solution;

import tsp.model.*;

import java.util.*;

public class NearestNeighbor implements InitialSolutionGenerator{
	
	private City[] cities;
	private int n;
	private CityManager cm;
	private City [] solution;
	private City[] solutionk;
	

	public NearestNeighbor(CityManager cm){
		this.cm = cm;
		this.cities=cm.getCities();
		this.n=cities.length;
		solution = new City[n];
		solutionk = new City[n];
		
	}

	//@Override
	public City[] generate() { 
	
		int counter = 0;
		boolean jump=false;
		City c = cities[0]; //inizio dalla prima citta'
		c.visit(true);
		solution[counter] = c;
		
		while (counter<n){
			counter++;
			City[] closer = cm.getNearestNew(c); 
			/*
			 * Questo metodo ritorna le k citta' piu' vicine a quella considerata
			 * ma NON considerando quelle che sono gia state visitate !!
			 * per cui quando per una citta' tutte le k + vicine sn gia' state visitate le altre non vengono inserite
			 * infatti il counter arriva a 52 ma riempie solo fino alla 33 il vett solution
			 */
			
			jump=false;
			for(int i=0; i<closer.length && jump==false; i++){
				if (closer[i].visited == false){
					solution[counter]=closer[i];
					closer[i].visit(true);
					c=closer[i];
					jump=true; 
				}
			}
		}
		
		return solution;
	}
	
	
	//@Override
	public City[] generate(int k) { // per NN con k + vicine o per generare k soluzioni?
		
		int counter = 0;
		City c = cities[0]; //inizio dalla prima citta'
		c.visit(true);
		solutionk[counter] = c;
		
		while (counter<n){
			counter++;
			City[] kcloser = cm.getNearestNew(c); //restituisce k citta' + vicine
			/*City[] kcloser = new City[k];
			int j=0;
			
			//leggo il vettore delle citta piu vicine e ne scelgo k tra le non visitate
			for(int i=0; i<closer.length && j<k; i++){ 
				if (closer[i].visited == false){
					kcloser[j]=closer[i];
					j++;
				}*/
			int l = kcloser.length; // =k (?)
			Random random = new Random();
			int r = random.nextInt(l); //generaro un numero random tra 0 (incluso) e l (escluso)
			
			c=kcloser[r];
			solutionk[counter]=c;
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
	
	public String toString(int k){
		StringBuffer sb = new StringBuffer("Initial solution - K nearest:\n");
		for(int i = 0; i<n; i++){
			sb.append(solutionk[i].city + " - ");
		}
		return sb.toString();
	}
	
	

}
