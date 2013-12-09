package Solution_Data_Structure;

import java.util.List;
import java.util.Set;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Edge;
import tsp.model.Solution;

public class Two_level_tree implements Solution {

	private static final int GROUPSIZE = 50;
	List<Segment> segments;
	int size;  //number of segments
	int city_position; //vettore indicizzato dalle città
	
	public Two_level_tree(City[] cities){
		//segments=new LinkedList<Segment>();
		int n=cities.length;
		int k=0;	//indice del vettore di città
		for(int i=0; i<(int)Math.sqrt(n);i++){
			while(k<n){
			Segment s=new Segment(i);
			Client first;
			Client last;
			int j=0;
				while(j<GROUPSIZE){
					s.addClient(cities[k],k);
					j++;
				}
			k++;	
		} //non funziona!!
		}
	}
	@Override
	public City next(City c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public City previous(City c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean between(City a, City b, City c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void flip(City a, City b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Solution getSolutionFromCities(City[] cities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object clone(){
		return this.clone();
	}

	
	@Override
	public City startFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLength(int delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLength(int length) {
		// TODO Auto-generated method stub
	}
	@Override
	public Set<Edge> getEdges() {
		// TODO Auto-generated method stub
		return null;
	}
	

}

