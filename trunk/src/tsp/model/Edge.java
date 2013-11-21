package tsp.model;

// classe che modella un arco
public class Edge {
	
	City depart;
	City arrive;
	
	int length;
	
	int squared_length;
	
	public Edge(City depart, City arrive){
		this.depart = depart;
		this.arrive = arrive;
		
		double sqx = this.depart.getX() - this.arrive.getX();
		double sqy = this.depart.getY() - this.arrive.getY();
		
		double sq = sqx*sqx + sqy*sqy;
		squared_length = (int)(sq);
		
		length = (int) Math.sqrt(sq);
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
	
	public int getSquaredLength() {
		return squared_length;
	}

	public boolean equals(Object arg0) {
		
		Edge e = (Edge)(arg0);
		
		if((depart==e.depart && arrive==e.arrive) || 
				(depart==e.arrive && arrive==e.depart))
			return true;
		
		return false;
		
	}
}
