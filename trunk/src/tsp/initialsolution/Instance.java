package tsp.initialsolution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import tsp.model.City;
import tsp.model.Solution;

/**
 * Istance contains the name, the number of nodes, the list of the nodes (numbered with their coordinates)
 */
public class Instance {
	
	public static final String TSP_EXTENSION = ".tsp";
	public static final String OPT_TOUR_EXTENSION = ".opt.tour";
	
	public String fileName;
	public String directory;
	public int N; //number of nodes
	public City[] cities_arr;
	
	
	public Instance(String dir, String file){ 
		
		if(!dir.endsWith("/")){
			this.directory = dir + "/";
		}else{
			this.directory = dir;
		}
		
		this.fileName = file;
		System.out.println("Opening instance file: " + this.directory + this.fileName);
		
		try{
			File input = new File(directory+fileName);
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
			String line;
			String token;
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();
			StringTokenizer st = new StringTokenizer(line," :\t\n\r\f");	
			token = st.nextToken();
			token = st.nextToken();		
			this.N = Integer.parseInt(token);
			cities_arr = new City[this.N];
			line = br.readLine();
			line = br.readLine();
	        for( int i = 0; i < this.N; i++ )
	        {
				line = br.readLine();        	
				st = new StringTokenizer(line);	
				token = st.nextToken();
				int n = Integer.parseInt(token); 
				token = st.nextToken();
				double x = Double.parseDouble(token);
				token = st.nextToken();
				double y = Double.parseDouble(token); 
				City c = new City(n, x, y);
				cities_arr[i]=c;
				
	        }
			line = br.readLine();						
			if (!line.toUpperCase().contains("EOF")){
				br.close();
				throw new IllegalArgumentException("Error while reading the input file: EOF Section");
			}
			br.close();
			System.out.println("File read successfully");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
	        System.exit(-1);
		}
		
	}
	
	public void createOptTourFile(Solution opt){
		final String tspName = fileName.substring(0, fileName.indexOf(TSP_EXTENSION));
		final File file = new File(directory+tspName+OPT_TOUR_EXTENSION);
		
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(file));
			
			System.out.println("Writing tour file: " + tspName);
			
			bw.write("NAME : "+ tspName + OPT_TOUR_EXTENSION + "\n");
			bw.write("COMMENT : Optimal tour for "+ tspName + TSP_EXTENSION + " (" + opt.length() + ")" + "\n");
			bw.write("TYPE : TOUR\n");
			bw.write("DIMENSION : " + N + "\n");
			bw.write("TOURSECTION\n");
			
			City st = opt.startFrom();
			City act = st;
			do{
				bw.write(act.city + "\n");
				act = opt.next(act);
			}while(!act.equals(st));
			
			bw.write("-1\n");
			bw.write("EOF\n");
			bw.close();
			System.out.println("File written successfully");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
	        System.exit(-1);
		}
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public int getN(){
		return N;
	}
	
	public City[] getCitiesArr(){		
		return cities_arr;
	}

}
