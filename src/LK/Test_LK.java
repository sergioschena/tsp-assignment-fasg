package LK;

import tsp.model.*;
import tsp.tabusearch.TSSolution;

public class Test_LK {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		City cities[] = createCities76();
		
		CityManager manager = new CityManager(cities);
		
		LK_Intesifier intensifier = new LK_Intesifier(manager);
		
		intensifier.setParam(76, 15, 5, 5, 50);
		
		TSSolution solution = new TSSolution(cities);
		
		printDistances(manager);
		
		System.out.println(solution + " -> " + evaluate(solution, manager, cities[0]));
		
		long start = System.currentTimeMillis();
		
		TSSolution best_sol = (TSSolution)intensifier.improve(solution);
		
		long end = System.currentTimeMillis();
		double secs = (end - start) / 1000.0;
		
		System.out.println("Time = "+secs);
		
		System.out.println(best_sol + " -> " + evaluate(best_sol, manager, cities[0]));
		
		System.out.println(evaluate(best_sol, manager, cities[0]));
	}
	
	public static City[] createCities10(){
		
		//opt = 266
		
		City cities[] = new City[10];
		
		
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
		
		
		/* soluzione ottima
		cities[7] = new City(8, 79, 28);
		cities[9] = new City(10, 36, 23);
		cities[3] = new City(4, 42, 76);
		cities[2] = new City(3, 46, 64);
		cities[6] = new City(7, 72, 56);
		cities[5] = new City(6, 91, 85);
		cities[4] = new City(5, 55, 74);
		cities[0] = new City(1, 15, 38);
		cities[8] = new City(9, 83, 4);
		cities[1] = new City(2, 32, 42);
		*/
		
		return cities;
	}
	
public static City[] createCities20(){
		
		City cities[] = new City[20];
		
		
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
		
		cities[10] = new City(11, 88, 17);
		cities[11] = new City(12, 86, 84);
		cities[12] = new City(13, 39, 0);
		cities[13] = new City(14, 7, 70);
		cities[14] = new City(15, 8, 79);
		cities[15] = new City(16, 12, 77);
		cities[16] = new City(17, 82, 90);
		cities[17] = new City(18, 74, 0);
		cities[18] = new City(19, 14, 15);
		cities[19] = new City(20, 45, 47);
		
		return cities;
	}
	
public static City[] createCities76(){
	
	// opt = 538
		
		City cities[] = new City[76];
		
		
		cities[0] = new City(1, 22, 22);
		cities[1] = new City(2, 36, 26);
		cities[2] = new City(3, 21, 45);
		cities[3] = new City(4, 45, 35);
		cities[4] = new City(5, 55, 20);
		cities[5] = new City(6, 33, 34);
		cities[6] = new City(7, 50, 50);
		cities[7] = new City(8, 55, 45);
		cities[8] = new City(9, 26, 59);
		cities[9] = new City(10, 40, 66);
		
		cities[10] = new City(11, 55, 65);
		cities[11] = new City(12, 35, 51);
		cities[12] = new City(13, 62, 35);
		cities[13] = new City(14, 62, 57);
		cities[14] = new City(15, 62, 24);
		cities[15] = new City(16, 21, 36);
		cities[16] = new City(17, 33, 44);
		cities[17] = new City(18, 9, 56);
		cities[18] = new City(19, 62, 48);
		cities[19] = new City(20, 66, 14);
		
		cities[20] = new City(21, 44, 13);
		cities[21] = new City(22, 26, 13);
		cities[22] = new City(23, 11, 28);
		cities[23] = new City(24, 7, 43);
		cities[24] = new City(25, 17, 64);
		cities[25] = new City(26, 41, 46);
		cities[26] = new City(27, 55, 34);
		cities[27] = new City(28, 35, 16);
		cities[28] = new City(29, 52, 26);
		cities[29] = new City(30, 43, 26);
		
		cities[30] = new City(31, 31, 76);
		cities[31] = new City(32, 22, 53);
		cities[32] = new City(33, 26, 29);
		cities[33] = new City(34, 50, 40);
		cities[34] = new City(35, 55, 50);
		cities[35] = new City(36, 54, 10);
		cities[36] = new City(37, 60, 15);
		cities[37] = new City(38, 47, 66);
		cities[38] = new City(39, 30, 60);
		cities[39] = new City(40, 30, 50);
		
		cities[40] = new City(41, 12, 17);
		cities[41] = new City(42, 15, 14);
		cities[42] = new City(43, 16, 19);
		cities[43] = new City(44, 21, 48);
		cities[44] = new City(45, 50, 30);
		cities[45] = new City(46, 51, 42);
		cities[46] = new City(47, 50, 15);
		cities[47] = new City(48, 48, 21);
		cities[48] = new City(49, 12, 38);
		cities[49] = new City(50, 15, 56);
		
		cities[50] = new City(51, 29, 39);
		cities[51] = new City(52, 54, 38);
		cities[52] = new City(53, 55, 57);
		cities[53] = new City(54, 67, 41);
		cities[54] = new City(55, 10, 70);
		cities[55] = new City(56, 6, 25);
		cities[56] = new City(57, 65, 27);
		cities[57] = new City(58, 40, 60);
		cities[58] = new City(59, 70, 64);
		cities[59] = new City(60, 64, 4);
		
		cities[60] = new City(61, 36, 6);
		cities[61] = new City(62, 30, 20);
		cities[62] = new City(63, 20, 30);
		cities[63] = new City(64, 15, 5);
		cities[64] = new City(65, 50, 70);
		cities[65] = new City(66, 57, 72);
		cities[66] = new City(67, 45, 42);
		cities[67] = new City(68, 38, 33);
		cities[68] = new City(69, 50, 4);
		cities[69] = new City(70, 66, 8);
		
		cities[70] = new City(71, 59, 5);
		cities[71] = new City(72, 35, 60);
		cities[72] = new City(73, 27, 24);
		cities[73] = new City(74, 40, 20);
		cities[74] = new City(75, 40, 37);
		cities[75] = new City(76, 40, 40);
		
		return cities;
	}
	
	public static int evaluate(Solution sol, CityManager manager, City c){
		
		int n = manager.n;
		
		City a = c;
		
		int cost = 0;
		
		for(int i = 0; i<n; i++){
			
			City b = sol.next(a);
			
			int cost_ab = manager.cost(a, b);
			
			cost += cost_ab;
			
			a = b;
			
		}
		
		return cost;
	}
	
	public static void printDistances(CityManager manager){
		int n = manager.n;
		
		City[] cities = manager.getCities();
		
		for(int i = 1; i<=n; i++){
			String str = "city "+i+": | ";
			for(int j = 1; j<=n; j++){
				str = str + manager.cost(cities[i-1], cities[j-1])+ " | ";
			}
			System.out.println(str);
		}
		
	}

}
