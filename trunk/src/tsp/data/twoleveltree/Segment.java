package tsp.data.twoleveltree;

public class Segment {
	
	//id del segmento
	int segment_id;
	
	//numero di sequenza del segmento
	int seq_number;
	
	//reverse bit per indicare in che direzione attraversare le città
	//	false: visita dal primo all'ultimo (dal precedente al successivo nei client)
	//	true : visita dall'ultimo al primo (dal successivo al precedente nei client)
	boolean reverse;
	
	//puntatore al segmento successivo
	Segment next;
	
	//puntatore al segmento precedente
	Segment prev;
	
	//puntatore alla prima città del segmento
	Client first;
	
	//puntatore all'ultima città del segmento
	Client last;
	
	//numero di client nel segmento
	int client_num;
	
	public Segment(int id){
		segment_id = id;
	}

	public boolean equals(Object obj) {
		Segment s = (Segment)obj;
		return s.segment_id == segment_id;
	}

	public Segment getNext() {
		if(reverse)
			return prev;
		return next;
	}

	public Segment getPrev() {
		if(reverse)
			return next;
		return prev;
	}

	public Client getFirst() {
		if(reverse)
			return last;
		return first;
	}

	public Client getLast() {
		if(reverse)
			return first;
		return last;
	}

	public void setNext(Segment next) {
		if(reverse)
			this.prev = next;
		else
			this.next = next;
	}

	public void setPrev(Segment prev) {
		if(reverse)
			this.next = prev;
		else
			this.prev = prev;
	}

	public void setFirst(Client first) {
		if(reverse)
			this.last = first;
		else
			this.first = first;
	}

	public void setLast(Client last) {
		if(reverse)
			this.first = last;
		else
			this.last = last;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Segmento "+segment_id+" (seq "+
													seq_number+") (R = "+reverse+")");
		sb.append(" (next = "+getNext().segment_id+"; prev = "+getPrev().segment_id+"):");
		Client c = getFirst();
		for(int i = 0; i < client_num; i++){
			sb.append(" "+c.city.city+" (seq "+c.seq_number+") - ");
			c = c.getNext();
		}
		return sb.toString();
	}

}
