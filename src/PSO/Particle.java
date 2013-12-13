package PSO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tsp.model.*;

class Particle implements Cloneable {
	
	//solutione corrispondente alla particella
	Solution position;
	
	//set di archi che compongono la soluzione
	HashSet<Edge> edge_set;
	
	//insieme di archi che compongono la soluzione
	Edge[] edges;
	
	//set di archi che modificano la posizione della particella
	PSO_Edge[] velocity;
	
	//miglior posizione registrata per la particella
	Particle local_best_particle = null;
	
	//manager delle citt� per ricavarne le informazioni
	CityManager manager;
	
	
	public Particle(Solution position, CityManager manager){
		
		this.manager = manager;
		
		this.position = (Solution) position.clone();
		
		updateEdges();		
		
		//inizializza la velocit�
		initVelocity();
		
		//il primo migliore locale � la particella stessa
		updateLocalBest();
				
	}
	
	//metodo per aggiornare gli archi secondo la posizione attuale
	public void updateEdges(){
		
		edge_set = (HashSet<Edge>) position.getEdges();
		
		edges = (Edge[]) edge_set.toArray();
		
		/* vecchia implementazione
		City c0 = manager.getCities()[0];
				
		int n = manager.n;
		
		edges = new Edge[n];
		
		
		
		edge_set = new HashSet<Edge>();
		
		City ci = c0;
		
		// creazione degli archi della soluzione
		for(int i = 0; i < n-1; i++){
			
			City c_ip1 = position.next(ci);
			
			Edge edge = new Edge(ci, c_ip1, manager.cost(ci, c_ip1));
			
			edges[i] = edge;
			
			edge_set.add(edge);
			
		}
		
		//ultimo arco
		City cn = position.previous(c0);
		
		Edge edge  = new Edge(cn, c0, manager.cost(cn, c0));
		
		edges[n-1] = edge;
		
		edge_set.add(edge);
		*/
	}
	
	//metodo che inizializza la velocit� in maniera randomica
	private void initVelocity(){
		
		HashSet<Edge> added_Edges = new HashSet<Edge>();
		
		City[] cities = manager.getCities();
		
		int n = manager.n;
		
		velocity = new PSO_Edge[n];
		
		for(int i = 0; i < n; i++){
			//cerca due citt� casuale da collegare
			int id0 = (int) Math.floor(n * Math.random());
			
			int id1 = (int) Math.floor(n * Math.random());
			
			City c0 = cities[id0];
			
			City c1 = cities[id1];
			
			Edge edge = manager.getEdge(c0, c1);
			
			if(added_Edges.contains(edge) || id0==id1){
				//cercare un altro arco
				i--;
				continue;
			}
			
			added_Edges.add(edge);
			
			velocity[i] = new PSO_Edge(1, edge);
			
		}
		
	}
	
	
	public int getLength(){
		return position.length();
	}
	
	public int getLocalBestLength(){
		return local_best_particle.position.length();
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		try {
			
			Particle p = (Particle) super.clone();
			p.position = (Solution)position.clone();
			p.velocity = velocity.clone();
			p.edges = edges.clone();
			p.edge_set = (HashSet<Edge>) edge_set.clone();
			p.manager = manager;
			
			p.local_best_particle = new Particle(local_best_particle.position, manager);
			p.local_best_particle.velocity = local_best_particle.velocity.clone();
			return p;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//metodo per aggiornare la soluzione locale migliore con i dati correnti
	@SuppressWarnings("unchecked")
	public void updateLocalBest(){
		Particle local;
		try {
			
			local = (Particle) super.clone();
			local.position = (Solution)position.clone();
			local.velocity = velocity.clone();
			local.edges = edges.clone();
			local.edge_set = (HashSet<Edge>) edge_set.clone();
			local.manager = manager;
			
			local.local_best_particle = null;
			
			local_best_particle = local;
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
	}
	
	//metodo che aggiorna la posizione e la velocit� della particella
	public void update(Particle global_best, double w, double c1, double c2, double c3){
		
		updateVelcity(global_best, w, c1, c2);
		
		updatePosition(c3);
		
	}
	
	//metodo per aggiornare la velocit� della particella
	private void updateVelcity(Particle global_best, double w, double c1, double c2){
		
		ArrayList<PSO_Edge> new_velocity = new ArrayList<PSO_Edge>();
		
		//ogni citt� pu� essere presente nella nuova velocit� al pi� 4 volte
		//FIXME si potrebbe aggiungere un parametro per regolare il numero di volte in cui
		//		una citt� � presente nella velocit�?
		HashMap<City, Integer> city_count = new HashMap<City, Integer>();
		
		
		
		//prendi gli archi presenti nella miglior posizione globale
		PSO_Edge[] global_edges = doSubAndMultiply(global_best, this, c2*Math.random());
		
		//prendi gli archi presenti nella miglior posizione locale
		PSO_Edge[] local_edges = doSubAndMultiply(local_best_particle, this, 
																		  c1*Math.random());
		
		//aggiorna la probabilit� degli archi nella vecchia velocit�
		multiply(velocity, w);
		
		
		//inserimento degli archi presi dalla miglior posizione globale
		insertEdges(global_edges, new_velocity, city_count);
		
		//inserimento degli archi presi dalla miglior posizione locale
		insertEdges(local_edges, new_velocity, city_count);
		
		//inserimento degli archi presi dalla vecchia velocit�
		insertEdges(velocity, new_velocity, city_count);
		
		//la nuova velocit� � completa
		
		//assegna la nuova velocit�
		//FIXME evitare il toArray()?
		velocity = (PSO_Edge[]) new_velocity.toArray();
		
		//FIXME l'ordinamento viene eseguito per ogni particella, conviene?
		//Bisogna ordinare rispetto alla probabilit� bisogna
		Arrays.sort(velocity, new PSO_Edge_Comparator());
		
	}
	
	//metodo che esegue la differenza fra due posizioni e ne ricava gli archi con le relative
	//probabilit�
	private static PSO_Edge[] doSubAndMultiply(Particle best, Particle particle, 
																			double prob){
		
		//l'array differenza conterr� al massimo gli n archi di best
		ArrayList<PSO_Edge> edges = new ArrayList<PSO_Edge>();
		
		for(Edge e : best.edges){
			//l'arco fa parte della posizione di particle?
			if(particle.contains(e))
				//l'arco deve essere scartato
				continue;
			
			//aggiungi l'arco
			edges.add(new PSO_Edge(prob, e));
		}
		
		return (PSO_Edge[]) edges.toArray();
	}
	
	//metodo per controllare se un arco fa parte della posizione della particella
	private boolean contains(Edge e){
		return edge_set.contains(e);
	}
	
	//metodo per moltiplicare un coefficiente a un insieme di archi: l'effetto � moltiplicare
	//la probabilit� di ogni arco con il coefficiente
	private static void multiply(PSO_Edge[] edges, double coeff){

		for(PSO_Edge e : edges)
			e.probability *= coeff;

	}
	
	//metodo per inserire gli archi da una sorgente a una destinazione a partire da un certo
	//indice. tramite la mappa count si controlla che sia possibile aggiungere un ulteriore
	//arco, in quanto mantiene il conteggio delle volte che ogni citt� � presente nell'array
	//di destinazione.
	//il metodo ritorna il prossimo indice per inserire altri archi in dst
	private static void insertEdges(PSO_Edge[] src, ArrayList<PSO_Edge> dst, 
																Map<City, Integer> count){
		
		for(PSO_Edge e : src){
			
			double fate = Math.random();
			
			if(e.probability < fate)
				//l'arco non � stato aggiunto
				continue;
			
			//citt� di partenza
			City depart = e.edge.getDepart();
			int depart_count = 0;
			
			//la citt� di partenza � presente nella nuova velocit�?
			boolean depart_is_present = count.containsKey(depart);
			
			if(depart_is_present){
				//la citt� � gi� presente
				
				depart_count = count.get(depart);
				
				//� possibile inserirla ancora?
				if(depart_count >= 4)
					//non � possibile inserire la citt�, quindi nemmeno l'arco
					continue;
			}
			
			//citt� di arrivo
			City arrive = e.edge.getArrive();
			int arrive_count = 0;
			
			//la citt� di arrivo � presente nella nuova velocit�?
			boolean arrive_is_present = count.containsKey(arrive);
			
			if(arrive_is_present){
				//la citt� � gi� presente
				
				arrive_count = count.get(arrive);
				
				//� possibile inserirla ancora?
				if(arrive_count >= 4)
					//non � possibile inserire la citt�, quindi nemmeno l'arco
					continue;
			}
			
			//a questo punto si pu� inserire l'arco
			
			//si aggiornano le informazioni sulle citt� nella mappa
			if(!depart_is_present)
				count.put(depart, 1);
			else
				count.put(depart, ++depart_count);
			
			if(!arrive_is_present)
				count.put(arrive, 1);
			else
				count.put(arrive, ++arrive_count);
			
			dst.add(e);
		}
		
	}
	
	//metodo per aggiornare la posizione della particella in base alla propria velocit�
	private void updatePosition(double c3){
		
		//set di archi aggiunti nella nuova posizione
		HashSet<Edge> new_edge_set = new HashSet<Edge>();
		
		//mappa degli archi, per poter ricreare la soluzione
		HashMap<City, ArrayList<Edge>> edge_map = new HashMap<City, ArrayList<Edge>>();
		
		//set di citt� gi� visitate 2 volte
		HashSet<City> cities_2_times = new HashSet<City>();
		
		//mappa tra gli estremi dei sottopercorsi che verranno a crearsi aggiungendo archi.
		//l'utilizzo di questa mappa � necessaria per garantire che il tour generato sia
		//hamiltoniano. il set di chiavi della mappa costituisce le citt� visitate una sola
		//volta
		HashMap<City, City> cities_1_times = new HashMap<City, City>();
		
		//set di citt� non ancora visitate
		HashSet<City> cities_0_times = new HashSet<City>();
		
		//riempimento del set di citt� non ancora visitate
		for(City c : manager.getCities())
			cities_0_times.add(c);
		
		//contatore degli archi inseriti, una volta raggiunto n, significa che si ha un tour
		//completo
		int i = 0;
		
		//inserimento degli archi dalla velocit�
		for(PSO_Edge e : velocity){
			
			if(i==manager.n)
				//sono stati aggiunti tutti gli archi
				break;
			
			//controlla che sia possibile inserire l'arco
			if(tryToInsertEdge(e.edge, 1, new_edge_set, i, manager.n, edge_map, 
																	cities_2_times,
																	cities_1_times,
																	cities_0_times))
				//l'arco � stato inserito, quindi incrementa il contatore
				i++;
		}
		
		//probabilit� degli archi della posizione precedente
		double prob = c3 * Math.random();
		
		//inserimento degli archi dalla posizione precedente
		for(Edge e : edges){
			
			if(i==manager.n)
				//sono stati aggiunti tutti gli archi
				break;
			
			//� possibile inserire l'arco?
			if(tryToInsertEdge(e, prob, new_edge_set, i, manager.n, edge_map,
																		cities_2_times,
																		cities_1_times,
																		cities_0_times))
				//arco inserito, incrementa il contatore
				i++;
		}
		
		if(i<manager.n)
			//bisogna inserire altri archi
			completeTour(new_edge_set, edge_map, i, manager, cities_2_times, cities_1_times,
																			  cities_0_times);
		
		//nella mappa degli archi sono presenti tutti gli archi che formano la
		//nuova posizione. bisogna adesso ricostruire la nuova posizione
		
		//citt� che formeranno la nuova posizione
		City[] new_position = new City[manager.n];
		
		//archi che formano la nuova posizione
		Edge[] new_edges = new Edge[manager.n];
		
		Edge edge = new_edge_set.iterator().next();
		
		City city = edge.getDepart();
		
		//estrazione degli archi e delle citt� dalla mappa
		for(int k = 0; k < manager.n; k++){
			
			new_position[k] = city;
			
			new_edges[k] = edge;
			
			ArrayList<Edge> array = edge_map.get(city);
			
			Edge e1 = array.get(0);
			
			Edge e2 = array.get(1);
			
			Edge next_edge = e1;
			
			if(e1.equals(edge))
				next_edge = e2;
			
			City c1 = next_edge.getArrive();
			
			City c2 = next_edge.getDepart();
			
			City next_city = c1;
			
			if(c1.equals(city))
				next_city = c2;
			
			city = next_city;
			
			edge = next_edge;
			
		}
		
		//aggiorna la soluzione
		position = position.getSolutionFromCities(new_position);
		
		edge_set = new_edge_set;
		
		edges = new_edges;
		
	}
	
	private static boolean tryToInsertEdge(Edge e, double prob, Set<Edge> set, 
														  int i, int n,
														  Map<City, ArrayList<Edge>> map,
														  Set<City> set_c2,
														  Map<City,City> map_c1,
														  Set<City> set_c0 ){
		
		double fate = Math.random();
		
		if(prob < fate)
			//l'arco non � inserito
			return false;
		
		if(set.contains(e))
			//l'arco � gi� contenuto: non inserire
			return false;
		
		//citt� di partenza
		City depart = e.getDepart();
		
		if(set_c2.contains(depart))
			//la citt� � gi� presente 2 volte nella velocit�
			return false;

		//la citt� di partenza � presente nella nuova velocit�?
		boolean depart_is_present = set_c0.contains(depart);
		
		//citt� di arrivo
		City arrive = e.getArrive();

		if(set_c2.contains(arrive))
			//la citt� � gi� presente 2 volte nella velocit�
			return false;

		//la citt� di partenza � presente nella nuova velocit�?
		boolean arrive_is_present = set_c0.contains(arrive);
		
		//4 casi:	-citt� di partenza e di arrivo sono entrambi assenti dalla velocit�,  
		//			 quindi diventeranno due nuovi estremi
		//			-una tra le due citt� � gi� un estremo, mentre l'altra � assente, quindi
		//			 quest'ultima sostituir� la prima come estremo
		//			-le citt� sono entrambi estremi, ma non sono associate tra loro quindi
		//			 possono essere collegate e i loro estremi saranno riassociati tra loro 
		//			-le citt� sono entrambi estremi e sono associate tra loro. questo �
		//			 accettabile solo se l'arco che le collega � l'ultimo da inserire,
		//			 altrimenti significa che si sta formando un tour non hamiltoniano,
		//			 quindi l'arco � da scartare
		
		if( depart_is_present && arrive_is_present ){
			if(map_c1.get(depart).equals(arrive) && map_c1.get(arrive).equals(depart)){
				//quarto caso, bisogna controllare che questo sia l'ultimo arco da inserire
				
				if(i != n)
					//non � l'ultimo arco da inserire, quindi � da scartare
					return false;
			}
			else{
				//terzo caso, bisogna eseguire delle riassegnazioni tra gli estremi
				
				//estremo associato alla citt� di partenza
				City depart_bound = map_c1.get(depart);
				
				//estremo associato alla citt� di arrivo
				City arrive_bound = map_c1.get(arrive);
				
				//riassegnazione degli estremi
				map_c1.put(depart_bound, arrive_bound);
				map_c1.put(arrive_bound, depart_bound);
			}
			
			//i due estremi sono da rimuovere dalla mappa
			map_c1.remove(depart);
			map_c1.remove(arrive);
			//e sono da aggiungere al set successivo
			set_c2.add(depart);
			set_c2.add(arrive);
			
			//bisogna aggiungere l'arco nella mappa
			map.get(depart).add(e);
			map.get(arrive).add(e);
			
		}
		else{
			if(depart_is_present == arrive_is_present){
				//primo caso, le due citt� formano una nuova coppia di estremi
				
				//assegnazione nella mappa degli estremi
				map_c1.put(depart, arrive);
				map_c1.put(arrive, depart);
				
				//rimozione dal set delle citt� non ancora visitate
				set_c0.remove(depart);
				set_c0.remove(arrive);
				
				//bisogna aggiungere l'arco nella mappa, ma � richiesto creare un nuovo
				//array per entrambe le citt� nella mappa
				ArrayList<Edge> depart_array = new ArrayList<Edge>();
				depart_array.add(e);
				map.put(depart, depart_array);
				
				ArrayList<Edge> arrive_array = new ArrayList<Edge>();
				arrive_array.add(e);
				map.put(arrive, arrive_array);
				
			}
			else{
				//secondo caso, una citt� fa da estremo e deve essere sostituito dall'altra
				City old_bound = depart;
				City new_bound = arrive;
				
				if(arrive_is_present){
					//la citt� di arrivo � l'estremo che sar� sostituito
					new_bound = depart;
					old_bound = arrive;
				}
				
				//estrai l'estremo corrispondente all'estremo da sostituire
				City other_bound = map_c1.get(old_bound);
				
				//elimina l'associazione tra i vecchi estremi
				map_c1.remove(old_bound);
				
				//crea la nuova associazione
				map_c1.put(new_bound, other_bound);
				map_c1.put(other_bound, new_bound);
				
				//il vecchio estremo passa al set successivo
				set_c2.add(old_bound);
				//il nuovo estremo deve essere rimosso dal set precedente
				set_c0.remove(new_bound);
				
				//bisogna aggiungere l'arco nella mappa
				//il vecchio estremo � gi� presente nella mappa
				map.get(old_bound).add(e);
				//per il nuovo estremo bisogna creare un nuovo ArrayList
				ArrayList<Edge> new_array = new ArrayList<Edge>();
				new_array.add(e);
				map.put(new_bound, new_array);
			}
		}
		//� possibile aggiungere l'arco
		set.add(e);
		
		return true;
	}
	
	private static void completeTour(Set<Edge> set, Map<City, ArrayList<Edge>> map, int i,
																       CityManager manager,
																       Set<City> set_c2,
																 Map<City, City> map_c1,
																       Set<City> set_c0   ){
		
		int n = i;
		
		//inserimento delle non ancora visitate
		Iterator<City> iter = set_c0.iterator();
		
		//ripeti finch� non sono stati aggiunti tutti gli n archi
		//oppure non sono finite le citt�
		while(n < manager.n && iter.hasNext()){
			
			City depart = iter.next();
			
			//� possibile che la citt� sia passata al set successivo durante le iterazioni.
			//pu� comunque cercare un arco, finch� non � stata visitata 2 volte
			if(set_c2.contains(depart))
				//la citt� � stata visitata gi� 2 volte, � da scartare
				continue;
			
			//lista delle citt� pi� vicine a quella di partenza
			City[] nearest = manager.getNearest(depart);
			
			for(City c : nearest){
				
				if(!set_c2.contains(c)){
					//l'arco tra le due citt� pu� essere inserito
					Edge e = manager.getEdge(depart, c);
					if(tryToInsertEdge(e, 1, set, n, manager.n, map, set_c2, map_c1, set_c0)){
						//l'arco � stato aggiunto
						n++;
						break;
					}
				}
			}
			//fine ricerca della citt� pi� vicina da poter collegare.
			
		}
		//fine ricerca tra le citt� non ancora visitate
		
		//inserimento delle citt� visitate una sola volta
		iter = map_c1.keySet().iterator();
		
		//ricerca un nuovo arco finch� non ne sono stati aggiunti n
		//teoricamente una volta trovati n archi, tutte le citt� dovrebbero trovarsi nel
		//set che continene le citt� visitate 2 volte
		while(n < manager.n){
			
			City depart = iter.next();
			
			//� possibile che la citt� sia passata al set successivo durante le iterazioni
			//pu� comunque cercare un arco, finch� non � stata visitata 2 volte
			if(set_c2.contains(depart))
				//la citt� � stata visitata gi� 2 volte, scartata
				continue;
			
			//lista delle citt� pi� vicine a quella di partenza
			City[] nearest = manager.getNearest(depart);
			
			for(City c : nearest){
				
				if(!set_c2.contains(c)){
					//l'arco tra le due citt� pu� essere inserito
					Edge e = manager.getEdge(depart, c);
					if(tryToInsertEdge(e, 1, set, n, manager.n, map, set_c2, map_c1, set_c0)){
						//l'arco � stato aggiunto
						n++;
						break;
					}
				}
			}
			//fine ricerca della citt� pi� vicina da poter collegare.
		}
		//fine ricerca tra le citt� visitate una volta sola		
	}
	
	

}
