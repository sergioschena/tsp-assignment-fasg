package Solution_Data_Structure;

import tsp.model.City;

public class Client {
	private Segment parent;
	private City elemento;
	private int pos_rel;
	private Client next;
	private Client previous;
	public Segment getParent() {
		return parent;
	}
	public void setParent(Segment parent) {
		this.parent = parent;
	}
	public City getElemento() {
		return elemento;
	}
	public void setElemento(City elemento) {
		this.elemento = elemento;
	}
	public int getPos_rel() {
		return pos_rel;
	}
	public void setPos_rel(int pos_rel) {
		this.pos_rel = pos_rel;
	}
	public Client getNext() {
		return next;
	}
	public void setNext(Client next) {
		this.next = next;
	}
	public Client getPrevious() {
		return previous;
	}
	public void setPrevious(Client previous) {
		this.previous = previous;
	}

}
