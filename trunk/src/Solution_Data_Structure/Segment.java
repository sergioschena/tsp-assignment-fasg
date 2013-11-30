package Solution_Data_Structure;

import java.util.*;

import tsp.model.City;

public class Segment {
	private int reverse;
	private int size;
	private int ID;
	LinkedList<Client> client;
	
	public Segment(int id, int size){
		this.size=size;
		this.ID=id;
		this.client=new LinkedList<Client>();
		this.reverse=1;
	}
	
	void addClient(Client c){
		this.client.add(c);
	}
	
	// metodo che mi dice se la città è presente nel segmento
	Client isCity(City c){
		for(Client i:client){
			if(i.getCity().getCity()==c.getCity()){
				return i;
			}
		}
		return null;
	}

	public int getReverse() {
		// TODO Auto-generated method stub
		return reverse;
	}

	public void setReverse(int i) {
		// TODO Auto-generated method stub
		this.reverse=i;
	}
	

	

}
