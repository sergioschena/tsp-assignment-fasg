package tsp.tabusearch;

import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;

import LK.LK_Intesifier;
import tsp.ga.KnownInstances;
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
		
		c = KnownInstances.createCities76();
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
		long start = System.currentTimeMillis();
		City[] cities = KnownInstances.createBerlin52();
		CityManager cityManager = new CityManager(cities,15);
		AspirationCriteria aspirationCriteria = BestEverAspirationCriteria.getInstance();
		TSObjectiveFunction obj = new TSObjectiveFunction(cityManager);
		Solution startSol = kNearestCandidate(cityManager,15);
		//Solution startSol = new TSSolution(cities);
		int startTenure = 5;
		int maxIterations = 10;
		int maxNotImprovingIterations = 5;
		TabuSearch ts = new TabuSearch(cityManager, aspirationCriteria, startTenure, maxNotImprovingIterations, maxIterations);
		Solution improved = ts.improve(startSol);
		long end = System.currentTimeMillis();	
		System.out.println("Cost: "+startSol.length()+", tour: "+(TSSolution)startSol);
		System.out.println("Cost: "+improved.length()+", tour: "+(TSSolution)improved);
		System.out.println("Iterations: "+ts.iterations);
		System.out.println((double)((end-start)/1000.0));
	}
	
	private static TSSolution kNearestCandidate(CityManager cityManager, int K) {
		City[] cities = cityManager.getCities();
		City[] sol = new City[cities.length];
		Random rng = new MersenneTwisterRNG();
		
		for(int i=1; i<cities.length; i++ ){
			cities[i].visited = false;
		}
		
		sol[0] = cities[0];
		sol[0].visited = true;
		int currK = K;
		
		for(int i=1; i<cities.length; i++ ){
			City[] bestCurr = cityManager.bestCurrentNearestOf(sol[i-1]);
			
			if(currK > bestCurr.length){
				currK = bestCurr.length;
			}
			
			sol[i] = bestCurr[rng.nextInt(currK)];
			sol[i].visited = true;
		}		
		
		return new TSSolution(sol);
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
	
}
