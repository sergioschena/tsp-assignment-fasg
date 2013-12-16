package Initial_Solution;

import java.io.*;
import java.util.*;

import tsp.model.*;

//Istance contains the name, the number of nodes, the list of the nodes (numbered with their coordinates)
public class Istance {
	
	public String fileName;
	public int N; //number of nodes
	public LinkedList<City> cities_ll = new LinkedList<City>();
	public City[] cities_arr;
	
	
	public Istance(String file){ 
		
		this.fileName = file;
		System.out.println("Apertura file istanze: " + this.fileName);
		
		try{
			File input = new File(fileName);
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
			String line;
			String token;
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);	
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
		    	cities_ll.add(c);
				
	        }
			line = br.readLine();						
			if (!line.toUpperCase().contains("EOF")){
				throw new IllegalArgumentException("Error while reading the input file: EOF Section");
			}
		}
		catch (Exception e) {
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
	
	public LinkedList<City> getCitiesLL(){
		return cities_ll;
	}
	
	public City[] getCitiesArr(){
		
		return cities_arr;
	}

}
