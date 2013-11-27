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
}
