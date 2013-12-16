package Instances;

import tsp.model.City;
import tsp.model.CityManager;

public class Eil51 implements Instance {

City[] cities = new City[51];
	
	public Eil51(){
		cities[1-1] = new City(1,37,52);
		cities[2-1] = new City(2,49,49);
		cities[3-1] = new City(3,52,64);
		cities[4-1] = new City(4,20,26);
		cities[5-1] = new City(5,40,30);
		cities[6-1] = new City(6,21,47);
		cities[7-1] = new City(7,17,63);
		cities[8-1] = new City(8,31,62);
		cities[9-1] = new City(9,52,33);
		cities[10-1] = new City(10,51,21);
		cities[11-1] = new City(11,42,41);
		cities[12-1] = new City(12,31,32);
		cities[13-1] = new City(13,5,25);
		cities[14-1] = new City(14,12,42);
		cities[15-1] = new City(15,36,16);
		cities[16-1] = new City(16,52,41);
		cities[17-1] = new City(17,27,23);
		cities[18-1] = new City(18,17,33);
		cities[19-1] = new City(19,13,13);
		cities[20-1] = new City(20,57,58);
		cities[21-1] = new City(21,62,42);
		cities[22-1] = new City(22,42,57);
		cities[23-1] = new City(23,16,57);
		cities[24-1] = new City(24,8,52);
		cities[25-1] = new City(25,7,38);
		cities[26-1] = new City(26,27,68);
		cities[27-1] = new City(27,30,48);
		cities[28-1] = new City(28,43,67);
		cities[29-1] = new City(29,58,48);
		cities[30-1] = new City(30,58,27);
		cities[31-1] = new City(31,37,69);
		cities[32-1] = new City(32,38,46);
		cities[33-1] = new City(33,46,10);
		cities[34-1] = new City(34,61,33);
		cities[35-1] = new City(35,62,63);
		cities[36-1] = new City(36,63,69);
		cities[37-1] = new City(37,32,22);
		cities[38-1] = new City(38,45,35);
		cities[39-1] = new City(39,59,15);
		cities[40-1] = new City(40,5,6);
		cities[41-1] = new City(41,10,17);
		cities[42-1] = new City(42,21,10);
		cities[43-1] = new City(43,5,64);
		cities[44-1] = new City(44,30,15);
		cities[45-1] = new City(45,39,10);
		cities[46-1] = new City(46,32,39);
		cities[47-1] = new City(47,25,32);
		cities[48-1] = new City(48,25,55);
		cities[49-1] = new City(49,48,28);
		cities[50-1] = new City(50,56,37);
		cities[51-1] = new City(51,30,40);
		
	}

	@Override
	public int getOptimum() {
		return 426;
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
