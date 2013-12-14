package Instances;

import tsp.model.City;

//interfaccia di un'instanza del tsp di cui è nota la soluzione ottima
public interface Instance {
	
	public int getOptimum();
	
	public City[] getCities();
	
}