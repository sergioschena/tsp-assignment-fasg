package tsp.model;

// classe che modella un arco
public class Edge {
	
	City depart;
	City arrive;
	
	int length;
	
	protected Edge(City depart, City arrive){
		this.depart = depart;
		this.arrive = arrive;
		
		length = City.distance(depart, arrive);
	}
	
	protected City getDepart() {
		return depart;
	}

	protected City getArrive() {
		return arrive;
	}

	protected int getLength() {
		return length;
	}
}
