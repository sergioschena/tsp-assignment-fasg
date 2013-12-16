package Instances;

import tsp.model.City;
import tsp.model.CityManager;

public class Cities10 implements Instance{
	
	City[] cities = new City[10];
	
	public Cities10(){
		cities[0] = new City(1, 79, 28);
		cities[1] = new City(2, 36, 23);
		cities[2] = new City(3, 42, 76);
		cities[3] = new City(4, 46, 64);
		cities[4] = new City(5, 72, 56);
		cities[5] = new City(6, 91, 85);
		cities[6] = new City(7, 55, 74);
		cities[7] = new City(8, 15, 38);
		cities[8] = new City(9, 83, 4);
		cities[9] = new City(10, 32, 42);
	}

	@Override
	public int getOptimum() {
		return 266;
	}

	@Override
	public City[] getCities() {
		return cities;
	}
	
	@Override
	public CityManager getCityManager() {
		return new CityManager(cities);
	}

}
