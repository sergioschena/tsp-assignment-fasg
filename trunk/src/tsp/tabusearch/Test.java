package tsp.tabusearch;

import tsp.model.City;

/** Test class for tabu list features 
 * NOT NEEDED 
 */
public class Test {

	
	
	public static void main(String[] args) {
		
		testTabuList();
		testMoveManager();
		
	}
	
	public static void testTabuList(){
		City[] c = new City[15];
		for(int i=0; i<c.length; i++){
			c[i] = new City(i+1, 2*i, 3*i);
		}
		
		TSTabuList tl = new TSTabuList(new FalseAspCrit(),3);
		TSObjectiveFunction of = new TSObjectiveFunction(c);
		
		Move m1 = new Move2Opt(c[0], c[1], c[2], c[3], of);
		Move m2 = new Move2Opt(c[0], c[1], c[2], c[3], of);
		Move m3 = new Move2Opt(c[2], c[5], c[4], c[3], of);
		Move m4 = new Move2Opt(c[2], c[5], c[4], c[3], of);
		Move m5 = new Move2Opt(c[3], c[8], c[12], c[9], of);
		Move m6 = new Move2Opt(c[3], c[8], c[12], c[9], of);
		Move m7 = new Move2Opt(c[14], c[11], c[12], c[13], of);
		Move m8 = new Move2Opt(c[14], c[11], c[12], c[13], of);
		Move m9 = new Move2Opt(c[10], c[11], c[12], c[13], of);
		Move m10 = new Move2Opt(c[10], c[11], c[12], c[13], of);
		Move m11 = new Move2Opt(c[9], c[5], c[7], c[13], of);
		Move m12 = new Move2Opt(c[9], c[5], c[7], c[13], of);
		
		
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
		System.out.println(m1+": "+tl.isTabu(m1));
		tl.addTabu(m7);
		tl.nextIteration();
		System.out.println(tl);
		System.out.println(m1+": "+tl.isTabu(m1));
		System.out.println(m3+": "+tl.isTabu(m3));
		tl.deleteTabu(m4);
		System.out.println(tl);
		System.out.println(m4+": "+tl.isTabu(m4));
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
		
		
		TSObjectiveFunction of = new TSObjectiveFunction(c);
		TSTabuList tl = new TSTabuList(new FalseAspCrit(),3);
		TSMoveManager mm = new TSMoveManager(tl, of);
		
		System.out.println(of);
		TSSolution start = new TSSolution(c);
		System.out.println(of.evaluate(start));
		
		System.out.println(start+" -> "+of.evaluate(start));
		Move2Opt m = mm.nextMove(start);
		while(m.eval < 0){
			System.out.println(m);
			m.operateOn(start);
			System.out.println(start+" -> "+of.evaluate(start));
			tl.addTabu(m);
			m = mm.nextMove(start);
		}
		

	}
	
	private static class FalseAspCrit implements AspirationCriteria {

		@Override
		public boolean isSatisfiedBy(Move m) {
			return false;
		}
		
	}
	
}
