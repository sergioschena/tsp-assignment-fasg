package Initial_Solution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import tsp.model.*;
import Initial_Solution.*;

public class main {
	
	//Perboli's main:
	
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	
	public static void main(String[] args) {
		   	
//      	String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/berlin52.tsp");
//    		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/eil51.tsp");       	
//    		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/eil76.tsp");
//    		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/pr152.tsp");    		
//       	String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/pr1002.tsp");
//   		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/rat195.tsp");
    	
    	//lettura file con parametri:
    	String myFileParam = "ProvaParam.txt";
    	LinkedList<String> istancesnames = readParams(myFileParam); //oppure si passa arg[0]
    	LinkedList<Istance> istances = new LinkedList<Istance>();
       	
    	//lettura dei file delle istanze:
    	for(int i=0; i<istancesnames.size(); i++){
    		Istance ist = new Istance(istancesnames.get(i));
    		istances.push(ist);
    			
    	}
    	
    	//elaborazione di ognuna delle istanze:
    	for(int j=0; j<istances.size(); j++){
    		
    		// ...
    		// ...
    		
    		//prova di generazione di soluzione inziale:
    		Istance ist = istances.get(j);
    		NearestNeighbor n1 = new NearestNeighbor(ist.cities_arr);
    		City[] sol1 = n1.generate();
    		int k=10; //prova
    		City[] sol1k = n1.generate(k);
    		
    	}
    	
    	
        
		
     

	}
	
	 public static LinkedList<String> readParams(String paramfile){
			
		 ParamFile fileStatus = ParamFile.NONE;
		 String myFileParam = paramfile;
		 // TreeMap <String, String> istances = new TreeMap<String, String>() ; //TODO perche mappa?
		 LinkedList<String> istances = new LinkedList<String>();
			
			/*if (args.length > 0) //per acquisizione da args ?
			{
				myFileParam = args[0];
			} */
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
	    							return istances;
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
							//Read the parameters
							// cosa ci serve leggere? numero di iterazioni? 
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
							istances.push(i);
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
			return istances; //TODO ?
		}
	 
	

}
