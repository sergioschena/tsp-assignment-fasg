package tsp.test;

import tsp.instances.Berlin52;
import tsp.instances.Cities76;
import tsp.instances.Eil51;
import tsp.instances.Fl3795;
import tsp.instances.Pr1002;
import tsp.instances.Pr152;
import tsp.instances.Rat195;
import tsp.model.CityManager;

public class MainTest_PSO_LK {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Berlin52 instance = new Berlin52();
		
		//Eil51 instance = new Eil51();
		
		//Pr152 instance = new Pr152();
		
		//Rat195 instance = new Rat195();
		
		//Cities76 instance = new Cities76();
		
		Pr1002 instance = new Pr1002();
		
		//Fl3795 instance = new Fl3795();
				
		CityManager manager = new CityManager(instance.getCities());
		
		PSO_LK_Tester tester = new PSO_LK_Tester(instance, manager);
		
		tester.setTotalRuns(10);
		
		tester.setParamLK(50, 15, 5, 15, 750);
		
		tester.setParamPSO(25, 180000, 10, 0.5, 1.5, 2, 2);
		
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
