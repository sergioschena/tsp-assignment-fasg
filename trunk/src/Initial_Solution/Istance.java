package Initial_Solution;

import java.io.*;
import java.util.*;

import tsp.model.*;

//Istance contains the name, the number of nodes, the list of the nodes (numbered with their coordinates)
public class Istance {
	
	public String fileName;
	public int N; //number of nodes
	public LinkedList<City> cities_ll = new LinkedList<City>();
	public City[] cities_arr; //serve?
	
	
	public Istance(String fileName){ 
		
		this.fileName = fileName;
		
		//Perboli's lettura file istanze:
		try
		{
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
	        //customers = new double[numCustomers][2];
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
				//customers[i][0] = Double.parseDouble(token); 
				token = st.nextToken();
				double y = Double.parseDouble(token);
				//customers[i][1] = Double.parseDouble(token); 
				City c = new City(n, x, y);
		    	cities_ll.add(c);
				
	        }
			line = br.readLine();						
			if (!line.toUpperCase().contains("EOF"))
			{
				throw new IllegalArgumentException("Error while reading the input file: EOF Section");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
	        System.exit(-1);
		}
		
	}
		
	
		
	/*	//mio :
	
		FileReader f;
	    f=new FileReader(fileName);
		BufferedReader b;
		b=new BufferedReader(f);
		String s = null;
		//TODO lettura del nome istanza - serve?
		for(int i=0; i<4; i++){
			s=b.readLine();
		}//gets the row with N number of cities
		StringTokenizer st = new StringTokenizer(s, ":"); //TODO controllare spazio dopo ":"
		st.nextToken();
    	int n = Integer.parseInt(st.toString());
    	this.N=n;
    	b.close();
	} */
	
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
		return (City[]) cities_ll.toArray();
	}
	
	/*public City[] IstanceGenerator() throws IOException{ 
		
		FileReader f;
	    f=new FileReader(this.fileName);
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
	    	cities_ll.add(c);
	    	
	    }
	    
	    b.close();
	    cities_arr = (City[]) cities_ll.toArray();
	    return cities_arr;
			
	}*/

}
