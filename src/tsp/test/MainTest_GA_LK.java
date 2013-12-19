package tsp.test;

import tsp.instances.*;
import tsp.model.CityManager;

public class MainTest_GA_LK {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Berlin52 instance = new Berlin52();
		
		//Eil51 instance = new Eil51();
		
		Pr152 instance = new Pr152();
		
		//Rat195 instance = new Rat195();
		
		//Cities76 instance = new Cities76();
		
		//Pr1002 instance = new Pr1002();
		
		//Fl3795 instance = new Fl3795();
		
		CityManager manager = new CityManager(instance.getCities(), 15);
		
		GA_LK_Tester tester = new GA_LK_Tester(instance, manager);
		
		tester.setTotalRuns(5);
		
		tester.setParam(8, 4, 25, 1000, 30, 15, 5, 15, 1000);
		
		System.out.println("Start tests");
		
		tester.updateTests();
		
		System.out.println("Media lunghezza: "+tester.getAVGTourLength());
		System.out.println("MAX lunghezza: "+tester.getMAXTourLength());
		System.out.println("MIN lunghezza: "+tester.getMINTourLength());
		System.out.println("Errore medio: "+tester.getErrorFromOptimum());
		System.out.println("Errore minimo : "+tester.getMINErrorFromOptimum());
		System.out.println("\n");
		System.out.println("Media tempo : "+tester.getAVGExploringTime());
		System.out.println("Tempo soluzione migliore : "+tester.getTimeofBestSolution());
		
		System.out.println("Media tempo costruzione esploratore : "+tester.getAVGExplorerConstructionTime());

	}

}
