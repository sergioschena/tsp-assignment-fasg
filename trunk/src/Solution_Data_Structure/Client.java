package Solution_Data_Structure;

import tsp.model.City;

public class Client {
	private Segment parent;
	private City elemento;
	private Client next;
	private Client previous;
	
	public Client(City c, Segment s){
		this.parent=s;
		this.elemento=c;
				
	}
	
	City getCity(){
		return this.elemento;
	}

	public Client getNext() {
		// TODO Auto-generated method stub
		if(parent.getReverse()==1){return this.next;}
		if(parent.getReverse()==-1){ return this.previous;}
	return null;
	}

	public void setNext(Client next2) {
		this.next=next2;
		// TODO Auto-generated method stub
		
	}

	public void setPrevious(Client a) {
		// TODO Auto-generated method stub
		this.previous=a;
	}

	public Client getPrevious() {
		// TODO Auto-generated method stub
		if(parent.getReverse()==1){return this.previous;}
		if(parent.getReverse()==-1){return this.next;}
		return null;
	}
	

}
