package Parameters_Testers;

import tsp.model.CityManager;
import Instances.Berlin52;
import Instances.Cities76;
import Instances.Eil51;
import Instances.Pr1002;
import Instances.Pr152;
import Instances.Rat195;

public class MainTest_GA_TS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Pr152 instance = new Pr152();
		
		CityManager manager = new CityManager(instance.getCities(), 15);
		
		GA_TS_Tester tester = new GA_TS_Tester(instance, manager);
		
		tester.setTotalRuns(10);
		
		tester.setParam(10, 5, 25, 1000, 50, 7, 3);
		
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
		
		System.out.println("\n");System.out.println("\n");System.out.println("\n");
		
Rat195 instance195 = new Rat195();
		
		manager = new CityManager(instance195.getCities(), 15);
		
		tester = new GA_TS_Tester(instance195, manager);
		
		tester.setTotalRuns(10);
		
		tester.setParam(10, 5, 25, 1000, 50, 7, 3);
		
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
		
		System.out.println("\n");System.out.println("\n");System.out.println("\n");
		
		
Pr1002 instance1002 = new Pr1002();
		
		 manager = new CityManager(instance1002.getCities(), 15);
		
		 tester = new GA_TS_Tester(instance1002, manager);
		
		tester.setTotalRuns(10);
		
		tester.setParam(10, 5, 25, 1000, 50, 7, 3);
		
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
