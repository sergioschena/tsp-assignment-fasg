package Solution_Data_Structure;

import java.util.*;

import tsp.model.City;
import tsp.model.Solution;

public class Vettore implements Solution {
	
	Map<City,Integer> appoggio; // in questo vettore memorizzo l'associazione tra la cità e la sua posizione nella soluzione
    City[] soluzione;
    int costo;
    public Vettore(){
    	
    }
    
	@Override
	public City next(City c) {
		// TODO Auto-generated method stub
		int p=appoggio.get(c);
		return soluzione[p+1];
	}

	@Override
	public City previous(City c) {
		// TODO Auto-generated method stub
		int p=appoggio.get(c);
		return soluzione[p-1];
	}
	@Override
	public boolean between(City a, City b, City c) {
		// TODO Auto-generated method stub
		boolean f=false;
		int pa=appoggio.get(a);
		int pb=appoggio.get(b);
		int pc=appoggio.get(c);
		if( pa<pb && pb<pc){
			f=true;
		}
		return f;
	}

	@Override
	public void flip(City a, City b) {
		// TODO Auto-generated method stub
		int pa=appoggio.get(a);
		int pb=appoggio.get(b);
		soluzione[pa]=b;
		soluzione[pb]=a;
		appoggio.put(a, pb);
		appoggio.put(b, pa);
		// calcolo la differenza di costo
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return costo;
	}

	
	

	

}
