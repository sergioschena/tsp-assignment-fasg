package Solution_Data_Structure;

import java.util.HashSet;
import java.util.Set;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Edge;
import tsp.model.Solution;
import tsp.tabusearch.TSSolution;

public class Array_solution implements Solution{
	private City[] cities;
	private int size;
	private int cost=0;
	private int[] position;
	private CityManager manager;
	private HashSet<Edge> edges;
	
	public Array_solution(City[] cities, CityManager manager){
		this.cities=cities.clone();
		this.size=cities.length;
		this.manager=manager;
		this.position=new int[size];
		edges= new HashSet<Edge>();
		
		for(int i=0; i<size; i++){
			position[cities[i].getCity()-1]=i;
			//Se sono almeno alla seconda città aggiungo l'arco e sommo il costo
			if(i>0){
				int edge_cost=manager.cost(cities[i-1],cities[i]);
				edges.add(new Edge(cities[i-1],cities[i],edge_cost));
				cost=cost+edge_cost;
			}
		}
		// aggiungo l'arco dall'ultima città alla prima del vettore, e sommo il costo
		int edge_last_cost=manager.cost(cities[size-1],cities[0]);
		edges.add(new Edge(cities[size-1],cities[0],edge_last_cost));
		cost=cost+edge_last_cost;
	}

	@Override
	public City next(City c) {
		// TODO Auto-generated method stub
		int a;
		a=position[c.getCity()-1];
		a++;
		if(a==size){return cities[0];}
		return cities[a];
	}

	@Override
	public City previous(City c) {
		// TODO Auto-generated method stub
		int a=position[c.getCity()-1];
		a--;
		if(a<0){ return cities[size-1];}
		return cities[a];
	}

	@Override
	public boolean between(City a, City b, City c) {
		// TODO Auto-generated method stub
		int pa=position[a.getCity()-1];
		int pb=position[b.getCity()-1];
		int pc=position[c.getCity()-1];
		
		return between(pa,pb,pc);
	}
	
	public boolean between(int pa,int pb,int pc){
		if(pa==pc)
			return false;
		
		return pa > pc ? 
				pb < pc || pa < pb :
				pa < pb && pb < pc;
	}
	@Override
	public void flip(City a, City b) {
		// TODO Auto-generated method stub
		
		City next_a = next(a);
		City next_b = next(b);

		int pa=position[a.getCity()-1];
		int pb=position[b.getCity()-1];
		int nexta=position[next_a.getCity()-1];
		
		if(nexta==pb){return;}
		
			// aggiungo i nuovi archi e modifico il costo
			Edge old_1=manager.getEdge(a,next_a);
			
			Edge old_2=manager.getEdge(b,next_b);
						
			//int cost_1=manager.cost(a, b);
			//int cost_2=manager.cost(next(a),next(b));
			Edge new_1= manager.getEdge(a,b);
			Edge new_2=manager.getEdge(next_a,next_b);
		
			edges.remove(old_1);
			edges.remove(old_2);
			edges.add(new_1);
			edges.add(new_2);
		
			cost=cost-old_1.getLength()-old_2.getLength()+new_1.getLength()+new_2.getLength();
		 
			
			int cnt = (nexta<pb ? pb-nexta-1: size+pb-nexta-1)/2; 
			for(; cnt >= 0; cnt--){
				internalSwap(nexta,pb);
				nexta = (nexta+1)%size;
				pb = (size+pb-1)%size;
			}
				
			//chiamata inutile, swap dovrebbe aggiornare anche la posizione delle città
			//aggiornaPosizione();
						
		return;
	}

	//metodo inutile, basta swap a eseguire lo scambio nel vettore di posizione e delle città
	private void aggiornaPosizione() {
		// TODO Auto-generated method stub
		for(int i=0; i<size; i++){
			position[cities[i].getCity()-1]=i;
		}
	}

	//metodo inutile, esiste già quello di CityManager
	//non importa che l'arco sia lo stesso oggetto, basta che le città agli estremi siano le
	//stesse
	private Edge getEdge(City a, City next) {
		// TODO Auto-generated method stub
		for(Edge e:edges){
			if((e.getDepart().equals(a) && e.getArrive().equals(next)) || (e.getArrive().equals(a) && e.getDepart().equals(next))){
				return e;
			}
		}
		return null;
	}

	private void internalSwap(int next_a,int pb) {
		// TODO Auto-generated method stub
		City c_next_a=cities[next_a];
		City c_b = cities[pb];
		
		cities[next_a]=c_b;
		cities[pb]=c_next_a;
		
		position[c_b.city-1] = next_a;
		position[c_next_a.city-1] = pb;
		//position[pc]=pc2;
		//position[pc2]=pc;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(City c : cities){
			//sb.append(" "+c.getCity()+" ("+c.getX()+", "+c.getY()+") ");
			sb.append(c.getCity()+" - ");
		}
		return sb.toString();
	}
	
	@Override
	public Object clone(){
		try {
			Array_solution cl = (Array_solution) super.clone();
			cl.cities = this.cities.clone();
			cl.position=this.position.clone();
			cl.edges=(HashSet<Edge>)edges.clone();
			cl.size = size;
			cl.cost = cost;
			cl.manager = manager;
			return cl;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return this.cost;
	}

	@Override
	public Solution getSolutionFromCities(City[] cities) {
		//Mai creare un CityManager, ogni volta che se ne crea uno, viene ricalcolata la
		//matrice delle distanze
		return new Array_solution(cities,manager);
	}
	
	@Override
	public Set<Edge> getEdges() {
		// TODO Auto-generated method stub
		return this.edges;
	}

	@Override
	public City startFrom() {
		// TODO Auto-generated method stub
		return cities[0];
	}

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
		int pa = position[a.city-1];
		int pb = position[b.city-1];
		
		City next_a = next(a);
		City next_b = next(b);
		
		City prev_a = previous(a);
		City prev_b = previous(b);
		
		cities[pa]= b;
		cities[pb]= a;
		
		position[b.city-1] = pa;
		position[a.city-1] = pb;
		
		Edge old_1 = manager.getEdge(a,next_a);		
		Edge old_2 = manager.getEdge(b,next_b);
		Edge old_3 = manager.getEdge(a,prev_a);		
		Edge old_4 = manager.getEdge(b,prev_b);
					
		Edge new_1= manager.getEdge(a,next_b);
		Edge new_2=manager.getEdge(a,prev_b);
		Edge new_3= manager.getEdge(b,prev_a);
		Edge new_4=manager.getEdge(next_a,b);
	
		edges.remove(old_1);
		edges.remove(old_2);
		edges.remove(old_3);
		edges.remove(old_4);
		edges.add(new_1);
		edges.add(new_2);
		edges.add(new_3);
		edges.add(new_4);
	
		cost=cost-old_1.getLength()-old_2.getLength()-old_3.getLength()-old_4.getLength()+new_1.getLength()+new_2.getLength()+new_3.getLength()+new_4.getLength();
	}
	

	

}
