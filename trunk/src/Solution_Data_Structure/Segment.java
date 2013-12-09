package Solution_Data_Structure;

import java.util.*;

import tsp.model.City;

public class Segment {
	private static final int LAST = -1;
	private boolean reverse;
	private int size;
	private int ID;
	private Client first;
	private Client last;
	private Segment previous;
	private Segment next;
	
	
	public Segment(int id){
		this.ID=id;
		this.reverse=false;
		this.size=0;
	}
	
	public boolean getReverse() {
		// TODO Auto-generated method stub
		return reverse;
	}

	public void setReverse(boolean i) {
		// TODO Auto-generated method stub
		this.reverse=i;
	}

	public int getID() {
		// TODO Auto-generated method stub
		return this.ID;
	}

	public void addClient(City city, int k) {
		// TODO Auto-generated method stub
		if(k==0){
			Client a=new Client(city,size);
			this.first=a;
			this.size++;
		} else if(k==LAST){
			Client a=new Client(city,size);
			this.last=a;
			this.size++;
			} else {
				Client a=new Client(city,size);
			}
			
	}
		

	
		

	

}