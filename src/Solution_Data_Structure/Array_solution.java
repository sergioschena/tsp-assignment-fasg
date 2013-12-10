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
		if(a==0){ return cities[size-1];}
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
		
		int pa=position[a.getCity()-1];
		int pb=position[b.getCity()-1];
		int nexta=position[next(a).getCity()-1];
		//int[] pos=position.clone();
		
		if(pa==pb) { return; }
		
		
		if(pa>pb) {flip(b,a);
				   return; }
		
		// aggiungo i nuovi archi e modifico il costo
			Edge old_1=getEdge(a,next(a));
			
			Edge old_2=getEdge(b,next(b));
			
			int cost_1=manager.cost(a, b);
			int cost_2=manager.cost(next(a),next(b));
			Edge new_1= new Edge(a,b, cost_1);
			Edge new_2=new Edge(next(a),next(b),cost_2);
		
			edges.remove(old_1);
			edges.remove(old_2);
			edges.add(new_1);
			edges.add(new_2);
		
			cost=cost-old_1.getLength()-old_2.getLength()+cost_1+cost_2;
		 

		
		//pos[b.getCity()-1]=pos[next(a).getCity()-1];
		cities[pb]=next(a);
		
		//pos[next(a).getCity()-1]=pb;
		cities[pa+1]=b;
			
		
		int i=pa+2;
		int k=pb-1;
		
		
		while(i!=k && i<k){
			swap(cities[i],cities[k]);
			i++;
			k--;
		}
		
		//position=pos;
		for(int j=nexta; j<(pb+1); j++){
			position[cities[j].getCity()-1]=j;
		}
		
				
		return;
	}

	private Edge getEdge(City a, City next) {
		// TODO Auto-generated method stub
		for(Edge e:edges){
			if((e.getDepart().equals(a) && e.getArrive().equals(next)) || (e.getArrive().equals(a) && e.getDepart().equals(next))){
				return e;
			}
		}
		return null;
	}

	private void swap(City city, City city2) {
		// TODO Auto-generated method stub
		int pc=position[city.getCity()-1];
		int pc2=position[city2.getCity()-1];
		
		cities[pc]=city2;
		cities[pc2]=city;
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
			cl.cities = cities.clone();
			cl.position=position.clone();
			cl.edges=(HashSet<Edge>)edges.clone();
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
		// TODO Auto-generated method stub
		CityManager c=new CityManager(cities);
		return new Array_solution(cities,c);
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
	public Set<Edge> getEdges() {
		// TODO Auto-generated method stub
		return this.edges;
	}

}
