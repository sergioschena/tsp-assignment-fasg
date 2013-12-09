package Solution_Data_Structure;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import tsp.model.City;
import tsp.model.CityManager;
import tsp.model.Edge;
import tsp.model.Solution;


public class Vettore implements Solution {
	
	//private static final int MAX_SIZE = 1000;
	CityManager cm;
	City[] soluzione;
	//HashMap<City,Integer> posizione;
	int[] posizione;
	HashSet<Edge> edges;
	int n;
	int costo;
	
	
	public Vettore(City[] c, CityManager manager){
    	int i;
    	this.cm=manager;
    	this.n=c.length;
    	soluzione=new City[n];
    	//posizione=new HashMap<City,Integer>();
    	posizione=new int[n];
    	edges=new HashSet<Edge>();
    	
    	this.soluzione=c.clone();
    	    	
    	for(i=0; i<n; i++){
    		//posizione.put(soluzione[i],i);
    		City a=soluzione[i];
    		posizione[a.getCity()-1]=i;
    		
    		if(i>=1){costo=costo+cm.cost(soluzione[i-1],soluzione[i]);
    				 edges.add(cm.getEdge(soluzione[i-1],soluzione[i]));
    				}
    	}
    	costo=costo+cm.cost(soluzione[n-1],soluzione[0]);
    	edges.add(cm.getEdge(soluzione[n-1],soluzione[0]));
	}
    
	@Override
	public City next(City c) {
		// TODO Auto-generated method stub
		int a;
		//a=posizione.get(c);
		a=posizione[c.getCity()-1];
		a++;
		if(a<n){
			return soluzione[a];
		}
		
		return soluzione[0];
	}

	@Override
	public City previous(City c) {
		// TODO Auto-generated method stub
		
		int a,l;
		l=this.n;
		//a=posizione.get(c);
		a=posizione[c.getCity()-1];
		a--;
		if(a==-1){return soluzione[l-1];}
		return soluzione[a];
	}
	@Override
	public boolean between(City a, City b, City c) {
		// TODO Auto-generated method stub
		
		int pa,pb,pc;
		/*pa=posizione.get(a);
		pb=posizione.get(b);
		pc=posizione.get(c);*/
		
		pa=posizione[a.getCity()-1];
		pb=posizione[b.getCity()-1];
		pc=posizione[c.getCity()-1];
		
		if(pa==pc || pa==pb || pc==pb){
			return false;
		}
		if(pa<pc){
			if(pa<pb && pb<pc){
				return true;				//caso a...b...c
			}
		}
		
		if(pc<pa){
			if(pa<pb && pb>pc){
				return true;				//caso c...a...b
			}
		}
		
		return false;
	}

	@Override
	public void flip(City a, City b) {
		// TODO Auto-generated method stub
		int pa,pb, nexta, nextb;
		City s1[]= new City[n];
		
		//metto in delle variabili tutte le posizioni che mi servono
				/*pa=posizione.get(a);				
				pb=posizione.get(b);
				nexta=posizione.get(next(a));
				nextb=posizione.get(next(b));*/
		pa=posizione[a.getCity()-1];
		pb=posizione[b.getCity()-1];
		nexta=posizione[next(a).getCity()-1];
		nextb=posizione[next(b).getCity()-1];
				
		if(pa>pb){
			flip(b,a);
			return;
		}
		
		if(pa==pb)	  {return;}
		if(nexta==pb) {return;}
		
		Edge old1=this.getEdge(a,next(a));
		Edge old2=this.getEdge(b,next(b));
		Edge newer1=cm.getEdge(a,b);
		Edge newer2=cm.getEdge(next(a),next(b));
		
		
		//aggiorno il vettore posizione
				//posizione.put(next(a), pb);
				//posizione.put(b, nexta);
		
		posizione[next(a).getCity()-1]=pb;
		posizione[b.getCity()-1]=nexta;
		//Inizio a sottrarre al costo i costi degli archi che sto rompendo
		this.costo=this.costo-cm.cost(a,next(a))-cm.cost(b,next(b));
		//Aggiungo il costo dei nuovi archi che vado a creare
		this.costo=this.costo+cm.cost(a,b)+cm.cost(next(a),next(b));
		
		//scambio la posizione di nexta e b
		s1[pb]=next(a);				
				
		for(int i=nexta,k=0; i<pb; i++,k++){
			s1[i]=soluzione[pb-k];				//per tutti gli archi tra nexta e pb, "cambio verso"
			//posizione.put(soluzione[pb-k], i);  //devo modificare anche il vettore posizione!!
			posizione[soluzione[pb-k].getCity()-1]=i;
		}
		
		for(int i=0; i<nexta; i++){
			s1[i]=soluzione[i];					//ricopio in s1 le celle della soluzione che non sono variate 
		}
		for(int i=nextb; i<n; i++){
			s1[i]=soluzione[i];
		}
	    
		this.soluzione=s1.clone();
		
		edges.remove(old1);
		edges.remove(old2);
		edges.add(newer1);
		edges.add(newer2);
				
	}

	private Edge getEdge(City a, City b) {
		// TODO Auto-generated method stub
		for(Edge i:edges){
			if( (i.getDepart().equals(a) && i.getArrive().equals(b)) || (i.getDepart().equals(b) && i.getArrive().equals(a))){
				return i;
			}
		}
		return null;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return costo;
	}

	@Override
	public Solution getSolutionFromCities(City[] cities) {
		// TODO Auto-generated method stub
		 return null;
		//return new Vettore(cities);
	}
	
	@Override
	public Object clone(){
		try {
			Vettore cl= (Vettore) super.clone();
			cl.soluzione=soluzione.clone();
			cl.posizione=posizione.clone();
			HashSet<Edge> clone = (HashSet<Edge>) edges.clone();
			cl.edges=clone;
			return cl;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(City c : soluzione){
			//sb.append(" "+c.getCity()+" ("+c.getX()+", "+c.getY()+") ");
			sb.append(c.getCity()+" - ");
		}
		return sb.toString();
	}
	

	@Override
	public City startFrom() {
		// TODO Auto-generated method stub
		return soluzione[0];
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

	public City[] getSoluzione() {
		// TODO Auto-generated method stub
		return this.soluzione;
	}


	
}
