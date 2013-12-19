package tsp.data.array;

import tsp.model.City;

public class Client {
	
	Segment parent;
	City city;
	int Id;
	int seq_number;
	City next;
	City previous;
	
	public Client(City city2, int i) {
		// TODO Auto-generated constructor stub
		this.city=city2;
		this.Id=city2.getCity();
		this.seq_number=i;
	}
	
	public void setNext(City c){
		this.next=c;
	}
	
	public void setPrevious(City c){
		this.previous=c;
	}

}
