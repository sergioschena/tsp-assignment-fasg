package tsp.model;

// classe che modella un arco
public class Edge {
	
	City depart;
	City arrive;
	
	int length;
	
	public Edge(City depart, City arrive){
		this.depart = depart;
		this.arrive = arrive;
		
		length = City.distance(depart, arrive);
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
}
