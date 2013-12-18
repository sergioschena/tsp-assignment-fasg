package Parameters_Testers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import tsp.model.CityManager;

import Instances.*;

public class GA_LK_ParamTester {

	//Test di diversi parametri dell'algoritmo GA-LK 
	public static void main(String[] args) {
		
		ArrayList<Instance> instances = new ArrayList<Instance>();
		
		instances.add(new Berlin52());
		instances.add(new Eil51());
		
		//instances.add(new Pr1002());
		
		//bisogna effettuare i test sul numero di interne e sulla numerosità della
		//popolazione
		
		//valori di max_t1  = {10, 20, 40, 67, 100}
		ArrayList<Integer> max_iter_values = new ArrayList<Integer>();
		
		max_iter_values.add(10);
		max_iter_values.add(20);
		max_iter_values.add(40);
		max_iter_values.add(67);
		max_iter_values.add(100);
		
		//valori di populationsize = {10, 15, 25, 50}
		ArrayList<Integer> population_size_values = new ArrayList<Integer>();
		
		population_size_values.add(10);
		population_size_values.add(15);
		population_size_values.add(25);
		population_size_values.add(50);
		
		final File resultsFile = new File("./ParamTestResult.csv");
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(resultsFile));
			
		} catch (IOException e) {
			System.err.println("Unable to create results file.");
			e.printStackTrace();
		}
		
		//itera su ogni istanza
		for(int i=0; i<instances.size(); i++){
			
			System.out.println(instances.get(i).getClass());
			
			long start_test = System.currentTimeMillis();
			
			try {
				bw.write(instances.get(i).getClass().toString()+";\n");
				bw.write("PopSize;MaxIter;MinSol;MeanSol;MaxSol;MinTime;MeanTime;MinErr;MeanErr;ExplorerConstrTime\n");
			} catch (IOException e) {
				System.err.println("Unable to write into results file.");
				e.printStackTrace();
			}
			
			CityManager manager = new CityManager(instances.get(i).getCities(), 15);
			
			GA_LK_Tester tester = new GA_LK_Tester(instances.get(i), manager);
			
			tester.setTotalRuns(20);
			
			//iterazione sulla numerosità della popolazione
			for(int j=0; j<population_size_values.size(); j++){
				
				int pop_size = population_size_values.get(j);
				
				int elite_size = pop_size/2;
				
				//iterazione sul numero di città iniziali
				for(int k=0; k<max_iter_values.size(); k++){
					
					int max_t1 = max_iter_values.get(k);
					
					tester.setParam(pop_size, elite_size, 25, 100000, 
														max_t1, 15, 5, 15, 1500);
					
					tester.updateTests();
					
					System.out.println("Population size: "+pop_size);
					System.out.println("Max Iterations: "+max_t1);
					System.out.println("Avg length: "+tester.getAVGTourLength());
		    		System.out.println("Worst length: "+tester.getMAXTourLength());
		    		System.out.println("Best length: "+tester.getMINTourLength());
		    		System.out.println("Mean error: "+tester.getErrorFromOptimum());
		    		System.out.println("Min error: "+tester.getMINErrorFromOptimum());
		    		System.out.println("Mean time: "+tester.getAVGExploringTime());
		    		System.out.println("Best solution time: "+tester.getTimeofBestSolution());
		    		System.out.println("Explorer Construction time: "+
		    										tester.getAVGExplorerConstructionTime());
		    		
		    		System.out.println("\n ------------------------------------------------------------ \n");
					
					StringBuffer csvLine = new StringBuffer();
					
					if(bw != null){
		    			csvLine.append(pop_size);csvLine.append(";");
		    			csvLine.append(max_t1);csvLine.append(";");
		    			csvLine.append(tester.getMINTourLength());csvLine.append(";");
		    			csvLine.append(tester.getAVGTourLength());csvLine.append(";");
		    			csvLine.append(tester.getMAXTourLength());csvLine.append(";");
		    			csvLine.append(tester.getTimeofBestSolution());csvLine.append(";");
		    			csvLine.append(tester.getAVGExploringTime());csvLine.append(";");
		    			csvLine.append(tester.getMINErrorFromOptimum());csvLine.append(";");
		    			csvLine.append(tester.getErrorFromOptimum());csvLine.append(";");
		    			csvLine.append(tester.getAVGExplorerConstructionTime());
		    			csvLine.append(";\n");
		    			try {
							bw.write(csvLine.toString());
						} catch (IOException e) {
							System.err.println("Unable to write a line.");
							e.printStackTrace();
						}
		    		}
					
				}
				
			}
			
			long end_test = System.currentTimeMillis() - start_test;
			
			//nuova istanza
			try {
				bw.write("\nTestTime;"+end_test+";\n\n");
				bw.flush();
			} catch (IOException e) {
				System.err.println("Unable to write into results file.");
				e.printStackTrace();
			}
			
			System.out.println("Fine Test per "+instances.get(i).getClass());
			System.out.println("Tempo test "+end_test);
			
		}
		
		//chiudi il file
		if(bw != null){
    		try {
				bw.close();
			} catch (IOException e) {
				System.err.println("Unable to close.");
				e.printStackTrace();
			}
    	}

	}

}
