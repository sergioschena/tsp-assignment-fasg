package tsp.data.twoleveltree;

import java.util.HashSet;
import java.util.Set;

import tsp.model.*;

public class TwoLevelTree implements Solution {
	
//---------------------------------------------------------------------------------------
// Attributi
//---------------------------------------------------------------------------------------
	
	//citt� del problema
	City[] cities;
	
	//gestore delle citt�
	CityManager manager;
	
	//nodi che contengono le citt�
	Client[] clients;
	
	//segmenti che contengono i nodi
	Segment[] segments;
	
	//numero di segmenti
	int segments_num;
	
	//set degli archi componenti la soluzione
	HashSet<Edge> edges;
	
	//lunghezza del tour
	int length = 0;
	
	//tolleranza per lo scambio durante un flip = 1/8 * groupsize
	int toll;
	
//---------------------------------------------------------------------------------------
// Parametri
//---------------------------------------------------------------------------------------
	
	//valore che indica il numero medio di citt� all'interno di un segmento
	private int groupsize = 50;
	
//---------------------------------------------------------------------------------------
// Costruttore e Setter
//---------------------------------------------------------------------------------------
	
	public TwoLevelTree(CityManager m, City[] c, int groupsize){
		manager = m;
		cities = manager.getCities();
		
		clients = new Client[manager.n];
		
		this.groupsize = groupsize;
		
		toll = groupsize / 8;
		
		//numero di segmenti = n / groupsize
		segments_num = (int) Math.ceil((double)(manager.n)/(double)(groupsize));
		
		segments = new Segment[segments_num];
		
		//creazione dei client, dei segmenti e degli archi della soluzione
		
		edges = new HashSet<Edge>();
		
		int segment_seq = 0;
		
		int client_seq = 0;
		
		boolean create_new_segment = true;
		
		Client prev_client = null;
		
		Segment prev_segment = null;
		
		int last_client_index = 0;
		
		for(int i = 0; i < manager.n; i++){
			if(create_new_segment){
				//crea un altro segmento
				segments[segment_seq] = new Segment(segment_seq);
				segments[segment_seq].seq_number = segment_seq;
				
				segments[segment_seq].reverse = false;
				
				segments[segment_seq].prev = prev_segment;
				
				if(prev_segment!=null)
					prev_segment.next = segments[segment_seq];
				
				client_seq = 0;
				create_new_segment = false;
			}
			
			//creazione dell'arco
			
			Edge e;
			
			if(i==0)
				e = manager.getEdge(c[c.length-1], c[0]);
			else
				e = manager.getEdge(c[i-1], c[i]);
			
			length += e.getLength();
			
			edges.add(e);
			
			//creazione del client
			
			int city_id = c[i].city-1;
			
			clients[city_id] = new Client(c[i]);
			
			clients[city_id].seq_number = client_seq;
			
			clients[city_id].parent = segments[segment_seq];
			
			clients[city_id].prev = prev_client;
			
			if(prev_client!=null)
				prev_client.next = clients[city_id];
			
			prev_client = clients[city_id];
			
			if(client_seq == 0){
				//il client � il primo del segmento
				segments[segment_seq].first = clients[city_id];
			}
			if(client_seq == groupsize-1 || i == manager.n -1){
				//il client � l'ultimo del segmento
				segments[segment_seq].last = clients[city_id];
				segments[segment_seq].client_num = client_seq+1;
				//bisogna creare un altro segmento
				prev_segment = segments[segment_seq];
				segment_seq++;
				create_new_segment = true;
			}
			last_client_index = city_id;
			client_seq++;
		}
		
		//bisogna collegare l'ultima citt� e l'ultimo segmento con i propri successivi
		clients[last_client_index].next = segments[0].first;
		segments[0].first.prev = clients[last_client_index];
		
		segments[segments.length-1].next = segments[0];
		segments[0].prev = segments[segments.length-1];
	}
	
//---------------------------------------------------------------------------------------
// Metodi
//---------------------------------------------------------------------------------------


	@Override
	public City next(City c) {
		
		//l'id delle citt� corrispondono agli id dei client
		Client current = clients[c.city-1];
		
		if(current.parent.reverse)
			return current.prev.city;
		
		return current.next.city;
	}

	@Override
	public City previous(City c) {
		//l'id delle citt� corrispondono agli id dei client
		Client current = clients[c.city-1];
				
		if(current.parent.reverse)
			return current.next.city;
				
		return current.prev.city;
	}

	@Override
	public boolean between(City a, City b, City c) {
		if(a.equals(b) || c.equals(b) || a.equals(c))
			return false;
		
		Client cl_a = clients[a.city-1];
		Client cl_b = clients[b.city-1];
		Client cl_c = clients[c.city-1];
		
		if(cl_a.parent.equals(cl_c.parent)){
			//a e c si trovano nello stesso segmento
			Segment s = cl_a.parent;
			
			if(s.reverse){
				//bisogna visitare il segmento al contrario
				
				if(cl_a.seq_number > cl_c.seq_number){
					//b deve essere nel segmento e il suo numero di sequenza tra quello
					//di a e c
					return cl_b.parent.equals(s) && cl_b.seq_number < cl_a.seq_number &&
							cl_b.seq_number > cl_c.seq_number;
				}
				
				//b deve essere in un altro segmento oppure il suo numero di sequenza non
				//deve essere compreso tra quelli di a e c
				
				return !cl_b.parent.equals(s) || cl_b.seq_number < cl_a.seq_number ||
														cl_b.seq_number > cl_c.seq_number;
				
			}
			else{
				//bisogna visitare il segmento dal primo verso l'ultimo

				if(cl_a.seq_number < cl_c.seq_number){
					//b deve essere nel segmento e il suo numero di sequenza tra quello
					//di a e c
					return cl_b.parent.equals(s) && cl_b.seq_number > cl_a.seq_number &&
							cl_b.seq_number < cl_c.seq_number;
				}

				//b deve essere in un altro segmento oppure il suo numero di sequenza non
				//deve essere compreso tra quelli di a e c

				return !cl_b.parent.equals(s) || cl_b.seq_number > cl_a.seq_number ||
													 cl_b.seq_number < cl_c.seq_number;
				
			}
			
		}
		else{
			// a e c sono in segmenti diversi
			
			boolean b_is_with_a = cl_b.parent.equals(cl_a.parent);
			boolean b_is_with_c = cl_b.parent.equals(cl_c.parent);
			
			if(!b_is_with_a && !b_is_with_c){
				//b � in un segmento differente da quello di a e quello di c
				
				if(cl_a.parent.seq_number < cl_c.parent.seq_number){
					// il segmento di b deve trovarsi tra quello di a e quello di c
					return cl_b.parent.seq_number > cl_a.parent.seq_number &&
								cl_b.parent.seq_number < cl_c.parent.seq_number;
				}
				
				//il segmento di b deve trovarsi prima di quello di a o dopo quello di c
				return cl_b.parent.seq_number > cl_a.parent.seq_number ||
							cl_b.parent.seq_number < cl_c.parent.seq_number;
			}
			else if(b_is_with_a){
				//b � nello stesso segmento di a
				
				if(cl_a.parent.reverse){
					//b deve avere un numero di sequenza minore di quello di a
					return cl_b.seq_number < cl_a.seq_number;
				}
				
				//b deve avere un numero di sequenza maggiore di quello di a
				return cl_b.seq_number > cl_a.seq_number;
			}
			else{
				//b � nello stesso segmento di c
				
				if(cl_c.parent.reverse){
					//b deve avere un numero di sequenza maggiore di quello di c
					return cl_b.seq_number > cl_c.seq_number;
				}
				
				//b deve avere un numero di sequenza minore di quello di c
				return cl_b.seq_number < cl_c.seq_number;
			}
			
		}
	}

	@Override
	public void flip(City a, City b) {
		
		Client cl_a = clients[a.city-1];
		
		Client cl_b = clients[b.city-1];
		
		Client next_a = cl_a.getNext();
		
		if(next_a.client_id == cl_b.client_id)
			return;
		
		Client next_b = cl_b.getNext();
		
		if(next_b.client_id == cl_a.client_id){
			//bisogna invertire tutti i segmenti
			Segment next_seg = segments[0];
			Segment prev_seg = next_seg.getPrev();
			
			boolean keep_swapping = true;
			
			while(keep_swapping){
				if(next_seg.equals(prev_seg)){
					next_seg.reverse = !next_seg.reverse;
					break;
				}
				
				if(next_seg.getNext().equals(prev_seg))
					keep_swapping = false;
				
				int seq = next_seg.seq_number;
				next_seg.seq_number = prev_seg.seq_number;
				prev_seg.seq_number = seq;
				
				next_seg.reverse = !next_seg.reverse;
				prev_seg.reverse = !prev_seg.reverse;
				
				next_seg = next_seg.getPrev();
				prev_seg = prev_seg.getNext();
				
			}
		}
		else{
			if(next_a.parent.equals(cl_b.parent)){
				//next(a) e b sono nello stesso segmento
				Segment p = next_a.parent;
				
				if(next_a.comesBefore(cl_b)){
					//pu� essere eseguito un flip interno al segmento
					
					boolean next_a_is_first = next_a.equals(p.getFirst());
					boolean b_is_last = cl_b.equals(p.getLast());
					
					if(next_a_is_first && b_is_last){
						//basta invertire la direzione del segmento
						p.reverse = !p.reverse;
						
						p.setNext(next_b.parent);
						p.setPrev(cl_a.parent);
					}
					else{
						//una citt� tra b e next(a) � in mezzo al segmento
						
						int seq_left = next_a.seq_number;
						int seq_right = cl_b.seq_number;
						
						Client left = next_a;
						Client right = cl_b;
						
						if(p.reverse){
							seq_left = cl_b.seq_number;
							seq_right = next_a.seq_number;
							left = cl_b;
							right = next_a;
						}
						
						//scambio interno dei client
						while(seq_left <= seq_right){
							Client tmp = right.next;
							right.next = right.prev;
							right.prev = tmp;
							
							if(seq_left == seq_right)
								break;
							
							right.seq_number = seq_left++;
							
							tmp = left.next;
							left.next = left.prev;
							left.prev = tmp;
							left.seq_number = seq_right--;
							
							left = left.prev;
							right = right.next;
						}
						
						if(next_a_is_first)
							p.setFirst(cl_b);
						
						if(b_is_last)
							p.setLast(next_a);
						
					}
					
					
					
				}
				else{
					//il flip coinvolge altri segmenti
					
					//scambio interno degli estremi del segmento
					
					Client left = cl_b;
					Client next_left = next_b;
					
					Client right = cl_a;
					Client next_right = next_a;
					
					if(p.reverse){
						left = next_a;
						next_left = cl_a;
						right = next_b;
						next_right = cl_b;
					}
					
					//aggiorna i numeri di sequenza dei client da next(b) ad a
					int offset = (p.client_num - next_right.seq_number) - left.seq_number -1;
					
					Client c = next_left;
					
					while(!c.equals(next_right)){
						c.seq_number = c.seq_number + offset;
						c = c.next;
					}
					
					int right_seq_num = next_right.seq_number + offset;
					int left_seq_num = left.seq_number + offset;
					
					//aggiorna la posizione dei client all'inizio del segmento per portarli
					//alla fine
					c = left;
					
					while(right_seq_num < p.client_num){
						Client next_c = c.prev;
						c.prev = c.next;
						c.next = next_c;
						
						c.seq_number = right_seq_num++;
						
						c = next_c;
					}
					
					//aggiorna la posizione dei client alla fine del segmento per portarli
					//all'inizio
					
					c = next_right;
					
					while(left_seq_num >= 0){
						Client next_c = c.next;
						c.next = c.prev;
						c.prev = next_c;
						
						c.seq_number = left_seq_num--;
						
						c = next_c;
					}
					
					c = p.getFirst();
					p.setFirst(p.getLast());
					p.setLast(c);
					
					//scambio dei segmenti
					
					Segment next_seg = p.getNext();
					
					Segment prev_seg = p.getPrev();
					
					p.setNext(prev_seg);
					p.setPrev(next_seg);
					
					boolean keep_swapping = true;
					
					while(keep_swapping){
						
						if(next_seg.equals(p)){
							//c'� un solo segmento nella soluzione
							break;
						}
						
						if(next_seg.equals(prev_seg)){
							next_seg.reverse = !next_seg.reverse;
							break;
						}
						
						if(next_seg.getNext().equals(prev_seg))
							keep_swapping = false;
						
						next_seg.reverse = !next_seg.reverse;
						prev_seg.reverse = !prev_seg.reverse;
						
						int seq = next_seg.seq_number;
						next_seg.seq_number = prev_seg.seq_number;
						prev_seg.seq_number = seq;
						
						next_seg = next_seg.getPrev();
						prev_seg = prev_seg.getNext();
					}
					
					
				}
	
			}
			else{
				//next(a) e b sono in due segmenti diversi
				
				boolean next_a_is_first = next_a.equals(next_a.parent.getFirst());
				boolean b_is_last = cl_b.equals(cl_b.parent.getLast());
				
				if(next_a_is_first && b_is_last){
					//� possibile cambiare solo la direzione dei segmenti tra next(a) e b
					
					Segment next_seg = next_a.parent;
					
					Segment prev_seg = cl_b.parent;
					
					boolean keep_swapping = true;
					
					while(keep_swapping){
						if(next_seg.equals(prev_seg)){
							next_seg.reverse = !next_seg.reverse;
							break;
						}
						
						if(next_seg.getNext().equals(prev_seg))
							keep_swapping = false;
						
						next_seg.reverse = !next_seg.reverse;
						prev_seg.reverse = !prev_seg.reverse;
						
						int seq = next_seg.seq_number;
						next_seg.seq_number = prev_seg.seq_number;
						prev_seg.seq_number = seq;
						
						next_seg = next_seg.getPrev();
						prev_seg = prev_seg.getNext();
					}
					
					
					
					next_a.parent.setNext(next_b.parent);
					next_b.parent.setPrev(next_a.parent);
					cl_b.parent.setPrev(cl_a.parent);
					cl_a.parent.setNext(cl_b.parent);
					
				}
				else{
					//un delle due citt� � in mezzo al proprio segmento
					
					if(!next_a_is_first && !b_is_last){
						//le citt� sono entrambe in mezzo al proprio segmento
						//si pu� valutare di scambiarne le porzioni esterne dei segmenti
						//invece di eseguire uno split
						
						//numero di citt� successive a next(a) nel suo segmento, inclusa next(a)
						int num_next_a = next_a.parent.client_num - next_a.seq_number;
						
						if(next_a.parent.reverse)
							num_next_a = next_a.seq_number +1;
						
						//numero di citt� precedenti a b nel suo segmento, inclusa b
						int num_b = cl_b.seq_number +1;
						
						if(cl_b.parent.reverse)
							num_b = cl_b.parent.client_num - cl_b.seq_number;
						
						//differenza di elementi tra le due porzioni
						int diff = Math.abs(num_next_a - num_b);
						
						if(diff <= toll){
							// si pu� effettuare lo scambio delle porzioni senza fare split
							
							int seq_num_next_a = next_a.seq_number;
							int seq_num_b = cl_b.seq_number;
							
							Segment next_a_seg = next_a.parent;
							Segment b_seg = cl_b.parent;
							
							if(num_b < num_next_a){
								//il numero di client nel segmento di b aumenta
								b_seg.client_num += diff;
								next_a_seg.client_num -= diff;
							}
							else{
								//il numero di client nel segmento di next(a) aumenta
								b_seg.client_num -= diff;
								next_a_seg.client_num += diff;
							}
							
							//scambio delle citt� agli estremi dei due segmenti
							
							Client c = next_a_seg.getLast();
							next_a_seg.setLast(b_seg.getFirst());
							b_seg.setFirst(c);
							
							//scambio dalla porzione di b alla porzione di next(a)
							
							c = cl_b;
							
							if(next_a_seg.reverse){
								//lo scambio deve essere fatto con seq_num decrescente
								
								int new_seq_num = num_b - 1;
								
								while(new_seq_num >= 0){
									Client next_c = c.getPrev();
									Client prev_c = c.getNext();
									c.parent = next_a_seg;
									c.seq_number = new_seq_num--;
									c.setNext(next_c);
									c.setPrev(prev_c);
									
									c = next_c;
								}
								
								//aggiorna il seq_number degli altri client nel segmento
								
								if(diff > 0){
									int offset = diff;
									if(num_next_a>num_b)
										offset = -offset;
									
									c = cl_a;
									
									new_seq_num = c.seq_number + offset;
								
									while(new_seq_num < next_a_seg.client_num){
										c.seq_number = new_seq_num;
										c = c.next;
										new_seq_num++;
									}
								}
								
								
							}
							else{
								//lo scambio deve essere fatto con seq_num crescente
								
								int new_seq_num = seq_num_next_a;
								
								while(new_seq_num < next_a_seg.client_num){
									Client next_c = c.getPrev();
									Client prev_c = c.getNext();
									c.parent = next_a_seg;
									c.seq_number = new_seq_num++;
									c.setNext(next_c);
									c.setPrev(prev_c);
									
									c = next_c;
								}
								
							}
							
							//scambio dalla porzione di next(a) alla porzione di b
							
							c = next_a;
							
							if(b_seg.reverse){
								//lo scambio deve essere fatto con seq_num crescente
								
								int new_seq_num = seq_num_b;
								
								while(new_seq_num < b_seg.client_num){
									Client next_c = c.getPrev();
									Client prev_c = c.getNext();
									c.parent = b_seg;
									c.seq_number = new_seq_num++;
									c.setNext(next_c);
									c.setPrev(prev_c);
									
									c = prev_c;
								}
								
							}
							else{
								//lo scambio deve essere fatto con seq_num decrescente
								
								int new_seq_num = num_next_a - 1;
								
								while(new_seq_num >= 0){
									Client next_c = c.getPrev();
									Client prev_c = c.getNext();
									c.parent = b_seg;
									c.seq_number = new_seq_num--;
									c.setNext(next_c);
									c.setPrev(prev_c);
									
									c = prev_c;
								}
								
								//aggiorna il seq_number degli altri client nel segmento
								
								if(diff > 0){
									int offset = diff;
									if(num_b > num_next_a)
										offset = -offset;
								
									c = next_b;
									
									new_seq_num = c.seq_number + offset;
								
									while(new_seq_num < b_seg.client_num){
										c.seq_number = new_seq_num;
										c = c.next;
										new_seq_num++;
									}
								}
								
							}
							
							//cambio di direzione dei segmenti tra quello di next(a)
							//e quello di b
							
							Segment next_seg = next_a_seg.getNext();
							
							Segment prev_seg = b_seg.getPrev();
							
							
							
							boolean keep_swapping = true;
							
							while(keep_swapping){
								if(next_seg.equals(b_seg))
									//non ci sono segmenti intermedi
									break;
								
								if(next_seg.equals(prev_seg)){
									next_seg.reverse = !next_seg.reverse;
									break;
								}
								
								if(next_seg.getNext().equals(prev_seg))
									keep_swapping = false;
								
								next_seg.reverse = !next_seg.reverse;
								prev_seg.reverse = !prev_seg.reverse;
								
								int seq = next_seg.seq_number;
								next_seg.seq_number = prev_seg.seq_number;
								prev_seg.seq_number = seq;
								
								next_seg = next_seg.getPrev();
								prev_seg = prev_seg.getNext();
							}
							
							next_seg = next_a_seg.getNext();
							prev_seg = b_seg.getPrev();
							
							if(!next_seg.equals(b_seg)){
								next_a_seg.setNext(prev_seg);
								prev_seg.setPrev(next_a_seg);
								b_seg.setPrev(next_seg);
								next_seg.setNext(b_seg);
							}
						}
						else{
							// bisogna eseguire uno split su entrambi i segmenti
							
							if(segments_num==2){
								//soluzione ad hoc per caso di due segmenti
								splitAndMergeOnPrev(next_a);
								splitAndMergeOnPrev(next_b);
							}
							else{
								//split sul segmento di next(a)
								//se il segmento precedente contiene b si �
								//costretti a fare merge sul segmento successivo
								if(next_a.parent.getPrev().equals(cl_b.parent)){
									//bisogna fare merge sul segmento successivo
									splitAndMergeOnNext(next_a);
									
									//lo split sul segmento di b pu� essere eseguito al meglio
									//per bilanciare i segmenti
									splitAndMerge(next_b);
								}
								else{
									//si pu� eseguire il merge migliore dopo lo split del
									//segmento di next(a)
									splitAndMerge(next_a);
									
									//bisogna controllare che dopo il merge il segmento in cui
									//c'� next(a) non sia quello successivo a quello contenente b
									
									if(cl_b.parent.getNext().equals(next_a.parent))
										//bisogna fare merge sul segmento precedente
										splitAndMergeOnPrev(next_b);
									else if(cl_b.parent.equals(next_a.parent))
										splitAndMergeOnNext(next_b);
									else
										//si pu� eseguire il merge migliore
										splitAndMerge(next_b);
								}
							}
							
							//� possibile cambiare solo la direzione dei segmenti tra next(a) 
							//e b
							Segment next_seg = next_a.parent;
							
							Segment prev_seg = cl_b.parent;
							
							boolean keep_swapping = true;
							
							while(keep_swapping){
								if(next_seg.equals(prev_seg)){
									next_seg.reverse = !next_seg.reverse;
									break;
								}
								
								if(next_seg.getNext().equals(prev_seg))
									keep_swapping = false;
								
								next_seg.reverse = !next_seg.reverse;
								prev_seg.reverse = !prev_seg.reverse;
								
								int seq = next_seg.seq_number;
								next_seg.seq_number = prev_seg.seq_number;
								prev_seg.seq_number = seq;
								
								next_seg = next_seg.getPrev();
								prev_seg = prev_seg.getNext();
							}
							
							next_a.parent.setNext(next_b.parent);
							next_b.parent.setPrev(next_a.parent);
							cl_b.parent.setPrev(cl_a.parent);
							cl_a.parent.setNext(cl_b.parent);
							
						}
						
					}
					else{
						//bisogna eseguire uno split su uno dei segmenti
						
						if(!next_a_is_first){
							//bisogna splittare il segmento contenete next(a)
							
							//se il segmento precedente contiene b, che � gi� l'ultimo, si �
							//costretti a fare merge sul segmento successivo
							
							if(next_a.parent.getPrev().equals(cl_b.parent))
								//bisogna fare merge sul segmento successivo
								splitAndMergeOnNext(next_a);
							else
								//si pu� eseguire il merge migliore
								splitAndMerge(next_a);
						}
							
						
						if(!b_is_last){
							//bisogna splittare il segmento contenete b
							
							//se il segmento successivo contiene next(a), che � gi� il primo,
							//si� costretti a fare merge sul segmento precedente
							
							if(cl_b.parent.getNext().equals(next_a.parent))
								//bisogna fare merge sul segmento successivo
								splitAndMergeOnPrev(next_b);
							else
								//si pu� eseguire il merge migliore
								splitAndMerge(next_b);
						}
						
						//� possibile cambiare solo la direzione dei segmenti tra next(a) e b
						
						Segment next_seg = next_a.parent;
						
						Segment prev_seg = cl_b.parent;
						
						boolean keep_swapping = true;
						
						while(keep_swapping){
							if(next_seg.equals(prev_seg)){
								next_seg.reverse = !next_seg.reverse;
								break;
							}
							
							if(next_seg.getNext().equals(prev_seg))
								keep_swapping = false;
							
							next_seg.reverse = !next_seg.reverse;
							prev_seg.reverse = !prev_seg.reverse;
							
							int seq = next_seg.seq_number;
							next_seg.seq_number = prev_seg.seq_number;
							prev_seg.seq_number = seq;
							
							next_seg = next_seg.getPrev();
							prev_seg = prev_seg.getNext();
						}
						
						
						
						next_a.parent.setNext(next_b.parent);
						next_b.parent.setPrev(next_a.parent);
						cl_b.parent.setPrev(cl_a.parent);
						cl_a.parent.setNext(cl_b.parent);
					}
					
				}
			}
			
			cl_a.setNext(cl_b);
			cl_b.setPrev(cl_a);
			
			next_a.setNext(next_b);
			next_b.setPrev(next_a);
			
		}
		
		//arco (a, next(a))
		Edge e1 = manager.getEdge(a, next_a.city);
		//arco (b, next(b))
		Edge e2 = manager.getEdge(b, next_b.city);
		//arco (a,b)
		Edge e3 = manager.getEdge(a, b);
		//arco (next(a),next(b))
		Edge e4 = manager.getEdge(next_a.city, next_b.city);
		
		//aggiorna la lunghezza del tour
		length = length - e1.getLength() - e2.getLength() + e3.getLength() + e4.getLength();
		
		//aggiorna il set di archi
		edges.remove(e1);
		edges.remove(e2);
		edges.add(e3);
		edges.add(e4);

	}
	
	//metodo che divide un segmento rispetto a un suo client, in modo che questo diventi
	//il primo elemento di un segmento. una delle due parti del segmento viene unita a
	//un segmento vicino
	private static void splitAndMerge(Client cut){
		Segment parent = cut.parent;
		
		//numero di citt� alla destra del taglio
		int num_right = parent.client_num - cut.seq_number;
		if(parent.reverse)
			num_right = cut.seq_number + 1;
		
		//numero di citt� alla sinistra del taglio
		int num_left = parent.client_num - num_right;
		
		//segmento successivo
		Segment next_seg = parent.getNext();
		
		//numero di citt� nel segmento successivo dopo il merge con esso
		int num_next = next_seg.client_num + num_right;
		
		//segmento precedente
		Segment prev_seg = parent.getPrev();
		
		//numero di citt� nel segmento precedente dopo il merge con esso
		int num_prev = prev_seg.client_num + num_left;
		
		//si sceglie di ottenere i segmenti pi� corti
		boolean merge_on_next = num_next < num_prev;
		
		if(merge_on_next){
			//parte del segmento originale verr� unito al segmento successivo
			
			if(next_seg.reverse){
				//bisogna spostare i client e spostarli alla fine del segmento
				int new_seq_num = num_next - 1;
				
				Client c = cut;
				
				while(new_seq_num >= next_seg.client_num){
					Client prev_c = c.getPrev();
					Client next_c = c.getNext();
					c.parent = next_seg;
					c.seq_number = new_seq_num--;
					c.setNext(next_c);
					c.setPrev(prev_c);
					
					c = next_c;
				}
			}
			else{
				//bisogna spostare i client all'inizio del segmento successivo e
				//aggiornare il seq_num dei client presenti in esso
				
				int new_seq_num = 0;
				
				Client c = cut;
				
				while(new_seq_num < num_right){
					Client prev_c = c.getPrev();
					Client next_c = c.getNext();
					c.parent = next_seg;
					c.seq_number = new_seq_num++;
					c.setNext(next_c);
					c.setPrev(prev_c);
					
					c = next_c;
				}
				
				new_seq_num = num_right;
				
				c = next_seg.getFirst();
				
				while(new_seq_num < num_next){
					c.seq_number = new_seq_num++;
					c = c.next;
				}
				
			}
			
			parent.setLast(cut.getPrev());
			next_seg.setFirst(cut);
			
			parent.client_num -= num_right;
			next_seg.client_num += num_right;
			
			//bisogna aggiornare i numeri di sequenza del segmento diviso
			if(parent.reverse){
				//i client rimossi erano all'inizio del segmento				
				Client c  = parent.getFirst();
				c.seq_number -= num_right;
				
				while(!c.equals(parent.getLast())){
					c = c.getNext();
					c.seq_number -= num_right;
				}
			}
			
		}
		else{
			//parte del segmento originale verr� unito al segmento precedente
			
			if(!prev_seg.reverse){
				//bisogna spostare i client e spostarli alla fine del segmento
				int new_seq_num = num_prev - 1;
				
				Client c = cut.getPrev();
				
				while(new_seq_num >= prev_seg.client_num){
					Client prev_c = c.getPrev();
					Client next_c = c.getNext();
					c.parent = prev_seg;
					c.seq_number = new_seq_num--;
					c.setNext(next_c);
					c.setPrev(prev_c);
					
					c = prev_c;
				}
			}
			else{
				//bisogna spostare i client all'inizio del segmento precedente e
				//aggiornare il seq_num dei client presenti in esso
				
				int new_seq_num = 0;
				
				Client c = cut.getPrev();
				
				while(new_seq_num < num_left){
					Client prev_c = c.getPrev();
					Client next_c = c.getNext();
					c.parent = prev_seg;
					c.seq_number = new_seq_num++;
					c.setNext(next_c);
					c.setPrev(prev_c);
					
					c = prev_c;
				}
				
				new_seq_num = num_left;
				
				c = prev_seg.first;
				
				while(new_seq_num < num_prev){
					c.seq_number = new_seq_num++;
					c = c.next;
				}
				
			}
			
			parent.setFirst(cut);
			prev_seg.setLast(cut.getPrev());
			
			parent.client_num -= num_left;
			prev_seg.client_num += num_left;
			
			//bisogna aggiornare i numeri di sequenza del segmento diviso
			if(!parent.reverse){
				//i client rimossi erano all'inizio del segmento
				Client c  = parent.getFirst();
				c.seq_number -= num_left;
				
				while(!c.equals(parent.getLast())){
					c = c.getNext();
					c.seq_number -= num_left;
				}
			}
		}
		
	}
	
	//metodo per spostare la porzione di segmento che parte con il client cut al segmento
	//successivo
	private static void splitAndMergeOnNext(Client cut){
		Segment parent = cut.parent;
		
		//numero di citt� alla destra del taglio
		int num_right = parent.client_num - cut.seq_number;
		if(parent.reverse)
			num_right = cut.seq_number + 1;
		
		//segmento successivo
		Segment next_seg = parent.getNext();
		
		//numero di citt� nel segmento successivo dopo il merge con esso
		int num_next = next_seg.client_num + num_right;
		
		if(next_seg.reverse){
			//bisogna spostare i client e spostarli alla fine del segmento
			int new_seq_num = num_next - 1;
			
			Client c = cut;
			
			while(new_seq_num >= next_seg.client_num){
				Client prev_c = c.getPrev();
				Client next_c = c.getNext();
				c.parent = next_seg;
				c.seq_number = new_seq_num--;
				c.setNext(next_c);
				c.setPrev(prev_c);
				
				c = next_c;
			}
		}
		else{
			//bisogna spostare i client all'inizio del segmento successivo e
			//aggiornare il seq_num dei client presenti in esso
			
			int new_seq_num = 0;
			
			Client c = cut;
			
			while(new_seq_num < num_right){
				Client prev_c = c.getPrev();
				Client next_c = c.getNext();
				c.parent = next_seg;
				c.seq_number = new_seq_num++;
				c.setNext(next_c);
				c.setPrev(prev_c);
				
				c = next_c;
			}
			
			new_seq_num = num_right;
			
			c = next_seg.getFirst();
			
			while(new_seq_num < num_next){
				c.seq_number = new_seq_num++;
				c = c.next;
			}
			
		}
		
		parent.setLast(cut.getPrev());
		next_seg.setFirst(cut);
		
		parent.client_num -= num_right;
		next_seg.client_num += num_right;
		
		//bisogna aggiornare i numeri di sequenza del segmento diviso
		if(parent.reverse){
			//i client rimossi erano all'inizio del segmento				
			Client c  = parent.getFirst();
			c.seq_number -= num_right;
			
			while(!c.equals(parent.getLast())){
				c = c.getNext();
				c.seq_number -= num_right;
			}
		}
	}
	
	
	//metodo per spostare la porzione di segmento che parte non contiene client cut al 
	//segmento precedente
	private static void splitAndMergeOnPrev(Client cut){
		Segment parent = cut.parent;
		
		//numero di citt� alla destra del taglio
		int num_right = parent.client_num - cut.seq_number;
		if(parent.reverse)
			num_right = cut.seq_number + 1;
		
		//numero di citt� alla sinistra del taglio
		int num_left = parent.client_num - num_right;
		
		//segmento precedente
		Segment prev_seg = parent.getPrev();
		
		//numero di citt� nel segmento precedente dopo il merge con esso
		int num_prev = prev_seg.client_num + num_left;
		
		if(!prev_seg.reverse){
			//bisogna spostare i client e spostarli alla fine del segmento
			int new_seq_num = num_prev - 1;
			
			Client c = cut.getPrev();
			
			while(new_seq_num >= prev_seg.client_num){
				Client prev_c = c.getPrev();
				Client next_c = c.getNext();
				c.parent = prev_seg;
				c.seq_number = new_seq_num--;
				c.setNext(next_c);
				c.setPrev(prev_c);
				
				c = prev_c;
			}
		}
		else{
			//bisogna spostare i client all'inizio del segmento precedente e
			//aggiornare il seq_num dei client presenti in esso
			
			int new_seq_num = 0;
			
			Client c = cut.getPrev();
			
			while(new_seq_num < num_left){
				Client prev_c = c.getPrev();
				Client next_c = c.getNext();
				c.parent = prev_seg;
				c.seq_number = new_seq_num++;
				c.setNext(next_c);
				c.setPrev(prev_c);
				
				c = prev_c;
			}
			
			new_seq_num = num_left;
			
			c = prev_seg.first;
			
			while(new_seq_num < num_prev){
				c.seq_number = new_seq_num++;
				c = c.next;
			}
			
		}
		
		parent.setFirst(cut);
		prev_seg.setLast(cut.getPrev());
		
		parent.client_num -= num_left;
		prev_seg.client_num += num_left;
		
		//bisogna aggiornare i numeri di sequenza del segmento diviso
		if(!parent.reverse){
			//i client rimossi erano all'inizio del segmento
			Client c  = parent.getFirst();
			c.seq_number -= num_left;
			
			while(!c.equals(parent.getLast())){
				c = c.getNext();
				c.seq_number -= num_left;
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Tour:");
		Client c = segments[0].getFirst();
		for(int i = 0; i < manager.n; i++){
			sb.append(" "+c.city.city+" - ");
			c = c.getNext();
		}
		sb.append("> Length: "+ length);
		sb.append("\n");
		Segment s = segments[0];
		for(int i = 0; i < segments_num; i++){
			sb.append(s+"\n");
			s = s.getNext();
		}
		return sb.toString();
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public Solution getSolutionFromCities(City[] cities) {
		return new TwoLevelTree(manager, cities, groupsize);
	}

	@Override
	public Object clone() {
		return new TwoLevelTree(manager, getCitiesfromSolution(), groupsize);
	}
	
	private City[] getCitiesfromSolution(){
		City[] c = new City[manager.n];
		
		Client client = clients[0];
		
		for(int i = 0; i < manager.n; i++){
			c[i] = client.city;
			client = client.getNext();
		}
		
		return c;
	}
	
	@Override
	public Set<Edge> getEdges() {
		return edges;
	}
	
	
	//disponendo di un city manager non dovrebbe servire una citt� di riferimento
	//oltretutto il tour non ha una vera citt� di inizio
	
	@Override
	public City startFrom() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//questi metodi non dovrebbero servire, l'aggiornamento della lunghezza � interno

	
	@Override
	public void updateLength(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLength(int length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void swap(City a, City b) {
		Client ca = clients[a.city-1];
		Client cb = clients[b.city-1];
		
		
		Client prev_ca = ca.prev;
		Client next_ca = ca.next;
		Segment parent_ca = ca.parent;
		int seq_ca = ca.seq_number;
		
		Client prev_cb = cb.prev;
		Client next_cb = cb.next;
		
		ca.prev = cb.prev;
		ca.next = cb.next;
		ca.parent = cb.parent;
		ca.seq_number = cb.seq_number;
		
		cb.prev = prev_ca;
		cb.next = next_ca;
		cb.parent = parent_ca;
		cb.seq_number = seq_ca;
		
		Edge old_1 = manager.getEdge(ca.city ,next_ca.city);		
		Edge old_2 = manager.getEdge(prev_ca.city,ca.city);
		Edge old_3 = manager.getEdge(cb.city,next_cb.city);		
		Edge old_4 = manager.getEdge(prev_cb.city, cb.city);
					
		Edge new_1= manager.getEdge(ca.city,next_cb.city);
		Edge new_2=manager.getEdge(prev_cb.city, ca.city);
		Edge new_3= manager.getEdge(cb.city,next_ca.city);
		Edge new_4=manager.getEdge(prev_ca.city,cb.city);
	
		edges.remove(old_1);
		edges.remove(old_2);
		edges.remove(old_3);
		edges.remove(old_4);
		edges.add(new_1);
		edges.add(new_2);
		edges.add(new_3);
		edges.add(new_4);
	
		length += -old_1.getLength()-old_2.getLength()-old_3.getLength()
					-old_4.getLength()+new_1.getLength()+new_2.getLength()+
											new_3.getLength()+new_4.getLength();
		
	}



}
