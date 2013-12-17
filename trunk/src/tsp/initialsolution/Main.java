package tsp.initialsolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import Solution_Data_Structure.Array_solution;
import tsp.model.City;
import tsp.model.CityManager;

public class Main {
	
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	
	private static final String FILE_PARAM = "./param.txt";
	private static final String DATA_DIR_PARAM = "DataFileDir";
	
	private static final Map<String, String> paramsMap = new TreeMap<String, String>();
	private static final LinkedList<String> istancesNames = new LinkedList<String>();
	
	public static void main(String[] args) {

    	//lettura file con parametri:
    	readParamFile(args); //oppure si passa arg[0]
    	
    	String dirName = paramsMap.get(DATA_DIR_PARAM);
       	
    	//lettura dei file delle istanze:
    	for(int i=0; i<istancesNames.size(); i++){ //prima istanza e' dirName
    		
    		Instance inst = new Instance(dirName, istancesNames.get(i));
    		
    		int k = 15; //numero citta' vicine da considerare
    		CityManager cm = new CityManager(inst.getCitiesArr(), k);
    		NearestNeighbor nn = new NearestNeighbor(cm);
    		
    		City[] initialSolutionK = nn.generate(k); 
    		cm.clearVisited();
    		
    		System.out.println(nn.toString());
    		
    		Array_solution sol = new Array_solution(initialSolutionK, cm);
    		
    		// risoluzione dell'istanza
    		
    		
    		// fine risoluzione
    		
    		inst.createOptTourFile(sol);
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

}
