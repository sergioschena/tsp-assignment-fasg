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
	
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	
	public static void main(String[] args) {

    	//lettura file con parametri:
    	String myFileParam = "/Users/giuliazago/Documents/workspace/Prova_lettura/src/param.txt";
    	LinkedList<String> istancesnames = readParams(myFileParam); //oppure si passa arg[0]
    	String dirName = istancesnames.getFirst();
    	LinkedList<Istance> istances = new LinkedList<Istance>();
       	
    	//lettura dei file delle istanze:
    	for(int i=1; i<istancesnames.size(); i++){ //prima istanza e' dirName
    		Istance ist = new Istance(dirName + istancesnames.get(i));
    		istances.add(ist);
    			
    	}
    	
    	//elaborazione di ognuna delle istanze:
    	for(int j=0; j<istances.size(); j++){ 
    		
    		//...
    		
    		//generazione della soluzione inziale:
    		Istance ist = istances.get(j);
    		
    		int k=10; //numero citta' vicine da considerare
    		CityManager cm = new CityManager(ist.getCitiesArr(), k);
    		NearestNeighbor nn = new NearestNeighbor(cm);
    		
    		
    		City[] initialSolution = nn.generate(); //genera una soluzione scegliendo la prima tra le k citta' piu' vicine
    		cm.clearVisited();
    		
    		/* Il CityManager se inizializzato con k cerca automaticamente le k citta' piu' vicine con il metodo getNearest.
    		 * E' conveniente creare un solo cm per ogni insieme di istanze.
    		 * Deve essere possibile generare la sol iniziale sia tra le k che tra tutte le citta'
    		 */

    		
    		City[] initialSolutionK = nn.generate(k);  //genera una soluzione scegliendone una random tra le k citta' piu' vicine 
    		
    		//...
    		
    		//print:
    		System.out.println(nn.toString());
    		//System.out.println("Initial solution: " + nn.toString(k));
    		System.out.println(cm.toString());
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
							st = new StringTokenizer(line);	
							token = st.nextToken(); 
							if ( token.equals("DataFileDir")){
								token = st.nextToken();
								String s = token ;
								istances.add(s);
							}
							
							// serve numero di iterazioni? 
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
							istances.add(i);
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