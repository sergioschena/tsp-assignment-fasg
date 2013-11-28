package tsp.model;

// classe che modella un arco
public class Edge {
	
	City depart;
	City arrive;
	
	int length;
	
	public Edge(City depart, City arrive, int length){
		this.depart = depart;
		this.arrive = arrive;
		this.length = length;
	}
	
	public City getDepart() {
		return depart;
	}

	public City getArrive() {
		return arrive;
	}

	public int getLength() {
		return length;
	}

	public boolean equals(Object arg0) {
		
		Edge e = (Edge)(arg0);
		
		if((depart==e.depart && arrive==e.arrive) || 
				(depart==e.arrive && arrive==e.depart))
			return true;
		
		return false;
		
	}
	
	public int hashCode(){
		
		City c1 = depart, c2 = arrive;
		
		if(arrive.city<depart.city){
			c1 = arrive;
			c2 = depart;
		}
		
		return c1.city * 131 + c2.city * 13;
	}
	
	
	
}
