package Two_Level_Tree;

import tsp.model.City;

public class Client {
	
	//id del client
	int client_id;

	//numero di sequenza all'interno del segmento
	int seq_number;
	
	//città del client
	City city;
	
	//segmento di appartenenza
	Segment parent;
	
	//prossimo client
	Client next;
	
	//client precedente
	Client prev;
	
	public Client(City c){
		city = c;
		client_id = city.city;
	}

	@Override
	public boolean equals(Object obj) {
		Client c = (Client) obj;
		return c.client_id == client_id;
	}

	public Client getNext() {
		if(parent.reverse)
			return prev;
		return next;
	}

	public Client getPrev() {
		if(parent.reverse)
			return next;
		return prev;
	}

	public void setNext(Client next) {
		if(parent.reverse)
			prev = next;
		else
			this.next = next;
	}

	public void setPrev(Client prev) {
		if(parent.reverse)
			next = prev;
		else
			this.prev = prev;
	}
	
	//metodo per stabilire se un client è precedente ad un altro in un segmento
	//prima di invocare il metodo bisogna controllare che c e il client chiamante
	//siano contenuti nello stesso segmento
	public boolean comesBefore(Client c){
		if(parent.reverse)
			return c.seq_number < seq_number;
		return seq_number < c.seq_number;
	}

}
