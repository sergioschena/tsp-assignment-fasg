package Instances;

import tsp.model.City;

public class Berlin52 implements Instance {

City[] cities = new City[52];
	
	public Berlin52(){
		cities[1-1] = new City(1,565,575);
		cities[2-1] = new City(2,25,185);
		cities[3-1] = new City(3,345,750);
		cities[4-1] = new City(4,945,685);
		cities[5-1] = new City(5,845,655);
		cities[6-1] = new City(6,880,660);
		cities[7-1] = new City(7,25,230);
		cities[8-1] = new City(8,525,1000);
		cities[9-1] = new City(9,580,1175);
		cities[10-1] = new City(10,650,1130);
		cities[11-1] = new City(11,1605,620); 
		cities[12-1] = new City(12,1220,580);
		cities[13-1] = new City(13,1465,200);
		cities[14-1] = new City(14,1530,5);
		cities[15-1] = new City(15,845,680);
		cities[16-1] = new City(16,725,370);
		cities[17-1] = new City(17,145,665);
		cities[18-1] = new City(18,415,635);
		cities[19-1] = new City(19,510,875);
		cities[20-1] = new City(20,560,365);
		cities[21-1] = new City(21,300,465);
		cities[22-1] = new City(22,520,585);
		cities[23-1] = new City(23,480,415);
		cities[24-1] = new City(24,835,625);
		cities[25-1] = new City(25,975,580);
		cities[26-1] = new City(26,1215,245);
		cities[27-1] = new City(27,1320,315);
		cities[28-1] = new City(28,1250,400);
		cities[29-1] = new City(29,660,180);
		cities[30-1] = new City(30,410,250);
		cities[31-1] = new City(31,420,555);
		cities[32-1] = new City(32,575,665);
		cities[33-1] = new City(33,1150,1160);
		cities[34-1] = new City(34,700,580);
		cities[35-1] = new City(35,685,595);
		cities[36-1] = new City(36,685,610);
		cities[37-1] = new City(37,770,610);
		cities[38-1] = new City(38,795,645);
		cities[39-1] = new City(39,720,635);
		cities[40-1] = new City(40,760,650);
		cities[41-1] = new City(41,475,960);
		cities[42-1] = new City(42,95,260);
		cities[43-1] = new City(43,875,920);
		cities[44-1] = new City(44,700,500);
		cities[45-1] = new City(45,555,815);
		cities[46-1] = new City(46,830,485);
		cities[47-1] = new City(47,1170,65);
		cities[48-1] = new City(48,830,610);
		cities[49-1] = new City(49,605,625);
		cities[50-1] = new City(50,595,360);
		cities[51-1] = new City(51,1340,725);
		cities[52-1] = new City(52,1740,245);
	}

	@Override
	public int getOptimum() {
		return 7542;
	}

	@Override
	public City[] getCities() {
		return cities;
	}

}
