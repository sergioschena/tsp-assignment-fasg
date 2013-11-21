package Initial_Solution;

import java.io.*;
import java.util.*;
import tsp.model.*;

//Istance contains the name, the number of nodes, the list of the nodes (numbered and with their coordinates)
public class Istance {
	
	String name;
	int N; 
	LinkedList<City> cities = new LinkedList<City>();
	
	public Istance(String name, int n){ 
		this.name=name;
		this.N=n; //TODO: lettura dal file - name si puo togliere?
	}
	
	public String getName(){
		return name;
	}
	
	public int getN(){
		return N;
	}
	
	public LinkedList<City> InstanceGenerator(String fileName) throws IOException{ //TODO: riceve il file name?
		
		FileReader f;
	    f=new FileReader(fileName);
		BufferedReader b;
	    b=new BufferedReader(f);
	    String s = null;
	    while( s.equals("NODE_COORD_SECTION")!=true ){ //read rows of input file till this one
	    	s=b.readLine();
	    }
	    
	    StringTokenizer st;
	    int a, x, y;
	    
	    for(int i=0; i<N; i++){ //read following N rows and save into a City and put them into a list
	    	s=b.readLine();
	    	st = new StringTokenizer(s);
	    	a = Integer.parseInt(st.toString());
	    	st.nextToken();
	    	x = Integer.parseInt(st.toString());
	    	st.nextToken();
	    	y = Integer.parseInt(st.toString());
	    	
	    	City c = new City(a, x, y);
	    	cities.add(c);
	    	
	    }
	    
	    return cities;
			
	}

}
