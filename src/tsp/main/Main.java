package tsp.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import tsp.data.array.ArraySolution;
import tsp.initialsolution.Instance;

public class Main {
	
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	
	private static final String FILE_PARAM = "./param.txt";
	private static final String DATA_DIR_PARAM = "DataFileDir";
	private static final String RESULTS_FILE = "./results.csv";
	
	private static final Map<String, String> paramsMap = new TreeMap<String, String>();
	private static final LinkedList<String> istancesNames = new LinkedList<String>();
	
	public static void main(String[] args) {

    	//lettura file con parametri:
    	readParamFile(args); //oppure si passa arg[0]
    	
    	String dirName = paramsMap.get(DATA_DIR_PARAM);
    	int K = 15; //numero citta' vicine da considerare
    	final File resultsFile = new File(RESULTS_FILE);
		
		BufferedWriter bw = null;
		NumberFormat nf = NumberFormat.getInstance();
		try {
			bw = new BufferedWriter(new FileWriter(resultsFile));
			
			bw.write("Instance;Best sol;Mean sol;Min sol;Max sol;Time Best;Time mean;Best known\n");
			
		} catch (IOException e) {
			System.err.println("Unable to create results file \"" + RESULTS_FILE + "\".");
			e.printStackTrace();
		}
    	//lettura dei file delle istanze:
    	for(int i=0; i<istancesNames.size(); i++){ //prima istanza e' dirName
    		StringBuffer csvLine = new StringBuffer();
    		Instance instance = new Instance(dirName, istancesNames.get(i));
    		
    		paramsMap.put("LKMaxT1", new Integer(getMaxT1Param(instance.N)).toString());
    		
    		// risoluzione dell'istanza    		
    		GALKSolver solver = new GALKSolver(instance, K);
    		solver.setParamByMap(paramsMap);
    		solver.solve();
    		ArraySolution optimum = (ArraySolution) solver.bestSolution;
    		// fine risoluzione    		
    		instance.createOptTourFile(optimum);
    		
    		if(bw != null){
    			csvLine.append(instance.fileName.substring(0, instance.fileName.indexOf(Instance.TSP_EXTENSION)));
    			csvLine.append(";");
    			csvLine.append(solver.getMINTourLength());
    			csvLine.append(";");
    			csvLine.append(solver.getAVGTourLength());
    			csvLine.append(";");
    			csvLine.append(solver.getMINTourLength());
    			csvLine.append(";");
    			csvLine.append(solver.getMAXTourLength());
    			csvLine.append(";");
    			csvLine.append(nf.format(solver.getTimeofBestSolution()/1000.0));
    			csvLine.append(";");
    			csvLine.append(nf.format(solver.getAVGSolutionTime()/1000.0));
    			csvLine.append(";\n");
    			
    			try {
					bw.write(csvLine.toString());
				} catch (IOException e) {
					System.err.println("Unable to write a line in \"" + RESULTS_FILE + "\"");
					e.printStackTrace();
				}
    		}
    		
    		System.out.println("Instance name: "+instance.fileName);
    		System.out.println("Avg length: "+solver.getAVGTourLength());
    		System.out.println("Worst length: "+solver.getMAXTourLength());
    		System.out.println("Best length: "+solver.getMINTourLength());
    		System.out.println("Avg exploring time: "+solver.getAVGExploringTime());
    		System.out.println("Avg explorer construction time: "+nf.format(solver.getAVGExplorerConstructionTime()/1000.0));
    		System.out.println("Best solution solve time: "+nf.format(solver.getTimeofBestSolution()/1000.0));    		
    		
    		System.out.println("\n ------------------------------------------------------------ \n");

    	}
    	
    	if(bw != null){
    		try {
				bw.close();
			} catch (IOException e) {
				System.err.println("Unable to close \"" + RESULTS_FILE + "\"");
				e.printStackTrace();
			}
    	}
	}
	
	 public static void readParamFile(String[] args){
			
		 ParamFile fileStatus = ParamFile.NONE;
		 String myFileParam = "";
			
			if (args.length > 0) {
				myFileParam = args[0];
			}else{
				myFileParam = FILE_PARAM;
			}
			
			try
			{
				File input = new File(myFileParam);
				BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
				String line;
				String token;
				StringTokenizer st;
				while ((line = br.readLine()) != null)
				{   					
					switch (fileStatus)
					{
					case NONE: 
	    					st = new StringTokenizer(line);	
							token = st.nextToken();    							
	    					switch (token.toUpperCase()) 
	    					{
	    						case "PARAMS":
	    							fileStatus = ParamFile.PARAMS;
	    							System.out.println("Reading the parameters");
	    							break;
	    						case "INSTANCES":
	    							fileStatus = ParamFile.INSTANCES;
	    							System.out.println("Reading the instances");
	    							break;
	    						case "EOF":
	    							System.out.println("Close the file");
	    							br.close();
	    							return;
	    						default:
	    							break;
	    					}
						break;
					case PARAMS:
						if (line.toUpperCase().contains("ENDPARAMS"))
						{
							fileStatus = ParamFile.NONE;
							System.out.println("End of parameters");
						}
						else
						{
							st = new StringTokenizer(line);	
							token = st.nextToken();
							String val = st.nextToken();
							
							paramsMap.put(token, val);							
						}
						break;
					case INSTANCES:
						if (line.toUpperCase().contains("ENDINSTANCES"))
						{
							System.out.println("End of instances");								
							fileStatus = ParamFile.NONE;
						}
						else
						{
							String i = line;
							istancesNames.add(i);
						}
						break;
					default: 
						break;
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error "+e.getMessage());
		        System.exit(-1);
			}
		
	 }
	 
	 //Metodo per la selezione del parametro MaxT1
	 
	private static int getMaxT1Param(int n){
		int param;
		
		if(n<100){
			param=10;
		} else if(n<152){
			param=linearInterpolation(100,10,152,30,n);
		} else if(n<195){
			param=30;
		} else if(n<318){
			param=linearInterpolation(195, 30, 318, 50, n);
		} else if(n<417){
			param=50;
		} else if(n<575){
			param=linearInterpolation(417, 50, 575, 70, n);
		} else if(n<724){
			param=70;
		} else if(n<783){
			param=linearInterpolation(724, 70, 783, 90, n);
		} else if(n<1002){
			param=linearInterpolation(783, 90, 1002, 100, n);
		} else {param=100;}
		
		return param;
	}

	private static int linearInterpolation(int xa, int ya, int xb, int yb, int x) {
		
		return (int) ((1.0*(x-xb)/(xa-xb)*ya)-(1.0*(x-xa)/(xa-xb)*yb));
	}
}
