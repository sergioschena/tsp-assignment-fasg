package Solution_Data_Structure;

public class Segment {
	private boolean reverse;
	private int size;
	private int ID;
	private Segment next;
	private Segment previous;
	private Client begin;
	private Client end;
	
	public boolean isReverse() {
		return reverse;
	}
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public Segment getNext() {
		return next;
	}
	public void setNext(Segment next) {
		this.next = next;
	}
	public Segment getPrevious() {
		return previous;
	}
	public void setPrevious(Segment previous) {
		this.previous = previous;
	}
	public Client getBegin() {
		return begin;
	}
	public void setBegin(Client begin) {
		this.begin = begin;
	}
	public Client getEnd() {
		return end;
	}
	public void setEnd(Client end) {
		this.end = end;
	}
	

}
