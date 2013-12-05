package tsp.tabusearch;

import java.util.Random;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Solution;

/** Test class for tabu list features 
 * NOT NEEDED 
 */
public class Test {
	
	public static void main(String[] args) {
		
		//testTabuList();
		//testMoveManager();
		testTabuSearch();
		//test3Opt();
	}
	
	private static void test3Opt() {
		
		City[] cs = new City[15];
		for(int i=0; i<cs.length; i++){
			cs[i] = new City(i+1, 2*i, 3*i);
		}
		
		City a = cs[1];
		City b = cs[2];
		City c = cs[6];
		City d = cs[5];
		City e = cs[11];
		City f = cs[10];
		
		TSSolution s = new TSSolution(cs);
		TSObjectiveFunction of = new TSObjectiveFunction(new CityManager(cs));
		Move3Opt ms[] = new Move3Opt[6];
		ms[0] = new Move3Opt(a, b, a, d, e, f, of); // KO
		ms[1] = new Move3Opt(a, b, c, b, e, f, of); // OK
		ms[2] = new Move3Opt(a, b, c, d, c, d, of); // OK
		ms[3] = new Move3Opt(a, b, c, d, a, f, of); // OK
		ms[4] = new Move3Opt(a, b, c, d, b, a, of); // KO
		ms[5] = new Move3Opt(a, b, a, d, e, f, of); // KO
		
		for(Move3Opt m : ms){
			boolean c1 = s.between(m.a, m.b, m.c);
			boolean c2 = s.between(m.c, m.e, m.a);
			System.out.println("c1: "+c1+" - c2: "+c2);
		}
	}

	public static void testTabuList(){
		City[] c = new City[15];
		for(int i=0; i<c.length; i++){
			c[i] = new City(i+1, 2*i, 3*i);
		}
		
		TSTabuList tl = new TSTabuList(new FalseAspCrit(),3);
		TSObjectiveFunction of = new TSObjectiveFunction(new CityManager(c));
		
		Move2Opt m1 = new Move2Opt(c[0], c[1], c[2], c[3], of);
		Move2Opt m2 = new Move2Opt(c[0], c[1], c[2], c[3], of);
		Move2Opt m3 = new Move2Opt(c[2], c[5], c[4], c[3], of);
		Move2Opt m4 = new Move2Opt(c[2], c[5], c[4], c[3], of);
		Move2Opt m5 = new Move2Opt(c[3], c[8], c[12], c[9], of);
		Move2Opt m6 = new Move2Opt(c[3], c[8], c[12], c[9], of);
		Move2Opt m7 = new Move2Opt(c[14], c[11], c[12], c[13], of);
		Move2Opt m8 = new Move2Opt(c[14], c[11], c[12], c[13], of);
		Move2Opt m9 = new Move2Opt(c[10], c[11], c[12], c[13], of);
		Move2Opt m10 = new Move2Opt(c[10], c[11], c[12], c[13], of);
		Move2Opt m11 = new Move2Opt(c[9], c[5], c[7], c[13], of);
		Move2Opt m12 = new Move2Opt(c[9], c[5], c[7], c[13], of);
		
		TSSolution sol = new TSSolution(c);
		
		// Test TabuList method
		tl.addTabu(m1);
		tl.nextIteration();
		System.out.println(tl);
		tl.addTabu(m3);
		tl.nextIteration();
		System.out.println(tl);
		tl.addTabu(m5);
		tl.nextIteration();
		System.out.println(tl);
		System.out.println(m1+": "+tl.isTabu(sol,m1));
		tl.addTabu(m7);
		tl.nextIteration();
		System.out.println(tl);
		System.out.println(m1+": "+tl.isTabu(sol,m1));
		System.out.println(m3+": "+tl.isTabu(sol,m3));
		tl.deleteTabu(m4);
		System.out.println(tl);
		System.out.println(m4+": "+tl.isTabu(sol,m4));
		tl.nextIteration();
		System.out.println(tl);
		tl.resetTabuTenure(m5);
		tl.addTabu(m11);
		tl.nextIteration();
		System.out.println(tl);
		tl.nextIteration();
		System.out.println(tl);
		tl.nextIteration();
		System.out.println(tl);
		tl.nextIteration();
		System.out.println(tl);
		tl.nextIteration();
		System.out.println(tl);

	}
	
	public static void testMoveManager(){
		/*
		City[] c = new City[10];
		c[0] = new City(1, 6, 10);
		c[1] = new City(2, 18, 4);
		c[2] = new City(3, 12, 9);
		c[3] = new City(4, 2, 5);
		c[4] = new City(5, 9, 6);
		c[5] = new City(6, 8, 1);
		c[6] = new City(7, 17, 10);
		c[7] = new City(8, 5, 15);
		c[8] = new City(9, 1, 1);
		c[9] = new City(10, 12, 5);
		*/
		
		City[] c = CityGenerator.randomCities(600, 1000, 1000);
		CityManager cm;
		TSObjectiveFunction of;
		TSTabuList tl;
		TSMoveManager mm;
		Solution best,curr;
		Move2Opt m;
		int cnt,nIter,prev;
		
		double secs;		
		long start;
		long end;
		
		//long start = System.currentTimeMillis();
		//cm = new CityManager(c,15);
		//long end = System.currentTimeMillis();
		//System.out.println((end-start)/1000.0);
		
		//start = System.currentTimeMillis();
		//cm = new CityManager(c);
		//end = System.currentTimeMillis();
		//System.out.printlsn((end-start)/1000.0);
		
		/*
		TSObjectiveFunction of = new TSObjectiveFunction(cm);
		TSTabuList tl = new TSTabuList(new FalseAspCrit(),3);
		TSMoveManager mm = new TSMoveManager(tl, of,cm);
		
		TSSolution curr = new TSSolution(c);
		TSSolution best = (TSSolution) curr.clone();
		System.out.println(of.evaluate(curr));
		
		long start = System.currentTimeMillis();
		System.out.println(curr+" -> "+of.evaluate(curr));
		Move2Opt m = mm.nextMoveTrunc(curr);
		int cnt = 5;
		int prev = Integer.MAX_VALUE;
		int nIter = 0;
		while(cnt > 0){
			//System.out.println(m+" "+m.eval);
			m.operateOn(curr);
			if(curr.length() < prev ){
				cnt = 5;
				prev = curr.length();
				best = (TSSolution) curr.clone();
			}else{
				cnt--;
			}
				
			//System.out.println(curr+" -> "+curr.length());
			//System.out.println(" -> "+curr.length());
			tl.addTabu(m);
			tl.nextIteration();			
			m = mm.nextMoveTrunc(curr);
			nIter++;
		}
		long end = System.currentTimeMillis();
		double secs = (end - start) / 1000.0;
		System.out.println("2-opt with TS| "+best+" -> "+best.length());
		System.out.println(secs);
		System.out.println(nIter);
		*/
		
		c = createCities76();
		cm = new CityManager(c,3);
		of = new TSObjectiveFunction(cm);
		//tl = new TSTabuList(new TrueAspCrit(),3);
		BestEverAspirationCriteria ac = BestEverAspirationCriteria.getInstance();
		tl = new TSTabuList(ac,3);
		mm = new TSMoveManager(tl, of,cm);
		
		curr = new TSSolution(c);
		best = (TSSolution) curr.clone();
		ac.setBestSolution(best);
		System.out.println(of.evaluate(curr));
		
		start = System.currentTimeMillis();
		System.out.println(curr+" -> "+of.evaluate(curr));
		m = mm.nextMoveTrunc(curr);
		cnt = 10;
		prev = Integer.MAX_VALUE;
		nIter = 0;
		while(cnt > 0){
			System.out.println(m+" "+m.eval);
			m.operateOn(curr);
			tl.deleteTabu(m);
			if(curr.length() < prev ){
				cnt = 10;
				prev = curr.length();
				best = (TSSolution) curr.clone();
				ac.setBestSolution(best);
			}else{
				cnt--;
			}
				
			//System.out.println(curr+" -> "+curr.length());
			//System.out.println(" -> "+curr.length());
			tl.addTabu(m);
			tl.nextIteration();
			m = mm.nextMoveTrunc(curr);
			nIter++;
		}
		end = System.currentTimeMillis();
		secs = (end - start) / 1000.0;
		System.out.println("2-opt without TS| "+best+" -> "+best.length());
		System.out.println("Elapsed time (sec.): "+secs);
		System.out.println("Iterations: "+nIter);
		
		/*
		TSSolution curr = new TSSolution(c);
		TSSolution best = (TSSolution) curr.clone();
		System.out.println(of.evaluate(curr));
		
		long start = System.currentTimeMillis();
		System.out.println(curr+" -> "+of.evaluate(curr));
		Move2Opt m = mm.nextMove(curr);
		int cnt = 5;
		int prev = Integer.MAX_VALUE;
		while(cnt > 0){
			//System.out.println(m+" "+m.eval);
			m.operateOn(curr);
			if(curr.length() < prev ){
				cnt = 5;
				prev = curr.length();
				best = (TSSolution) curr.clone();
			}else{
				cnt--;
			}
				
			//System.out.println(curr+" -> "+curr.length());
			tl.addTabu(m);
			m = mm.nextMove(curr);
		}
		long end = System.currentTimeMillis();
		double secs = (end - start) / 1000.0;
		System.out.println(best+" -> "+best.length());
		System.out.println(secs);
		*/
		

	}
	
	public static void testTabuSearch(){
		City[] cities = createEil51();
		CityManager cityManager = new CityManager(cities,15);
		AspirationCriteria aspirationCriteria = BestEverAspirationCriteria.getInstance();
		Solution start = new TSSolution(cities);
		int startTenure = 5;
		int maxIterations = 100;
		int maxNotImprovingIterations = 10;
		TabuSearch ts = new TabuSearch(cityManager, aspirationCriteria, startTenure, maxNotImprovingIterations, maxIterations);
		Solution improved = ts.improve(start);
		System.out.println("Cost: "+start.length()+", tour: "+(TSSolution)start);
		System.out.println("Cost: "+improved.length()+", tour: "+(TSSolution)improved);
		System.out.println("Iterations: "+ts.iterations);
		
	}
	
	private static class CityGenerator {
		public static City[] randomCities(int n, double xMax, double yMax){
			City[] ret = new City[n];
			
			for(int i = 0; i < n; i++){
				Random rGen = new Random();
				double x = rGen.nextDouble()*xMax;
				double y = rGen.nextDouble()*yMax;
				ret[i] = new City(i+1, x, y);
			}
			
			return ret;
		}
		
		
	}
	
	private static class FalseAspCrit implements AspirationCriteria {

		@Override
		public boolean isSatisfiedBy(Solution s, Move m) {
			return false;
		}

		@Override
		public void setBestSolution(Solution s) {}
		
	}
	
	private static class TrueAspCrit implements AspirationCriteria {

		@Override
		public boolean isSatisfiedBy(Solution s, Move m) {
			return true;
		}

		@Override
		public void setBestSolution(Solution s) {}
		
	}
	
	private static City[] createCities10(){
		
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
	
	private static City[] createCities20(){
		
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
	
	private static City[] createEil51(){
		
		// opt = 538
			
		City cities[] = new City[51];			
		
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

		
		return cities;
	}
	
	private static City[] createBerlin52(){
		
	// opt = 538
		
		City cities[] = new City[52];			
		
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
		
		return cities;
	}		
	
	private static City[] createCities76(){
	
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
	
	
}
