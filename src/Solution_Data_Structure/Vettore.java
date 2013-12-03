package Solution_Data_Structure;


import tsp.model.City;
import tsp.model.Solution;

public class Vettore implements Solution {
	
	
	City[] soluzione;
	int[] posizione;
	int n;
	int costo;
	
	public Vettore(City[] c, int n){
    	int i;
    	this.n=n;
    	soluzione=new City[n];
    	this.soluzione=c;
    	for(i=0; i<n; i++){
    		posizione[soluzione[i].getCity()]=i;
    	}
    }
    
	@Override
	public City next(City c) {
		// TODO Auto-generated method stub
		int a;
		a=posizione[c.getCity()];
		if(a==(n-1)){
			return soluzione[0];  //controllo che non sia l'ultimo del vettore ed eventualmente ritorno la prima città del vettore
		}
		return soluzione[a+1];
	}

	@Override
	public City previous(City c) {
		// TODO Auto-generated method stub
		int a;
		a=posizione[c.getCity()];
		if(a==0){
			return soluzione[n-1]; //ritorno l'ultima città se sto facendo previous della prima
		}
		return soluzione[a-1];
	}
	@Override
	public boolean between(City a, City b, City c) {
		// TODO Auto-generated method stub
		boolean f=false;
		int pa,pb,pc;
		pa=posizione[a.getCity()];
		pb=posizione[b.getCity()];
		pc=posizione[c.getCity()];
		if(pa<pc){
			if(pa<pb && pb<pc){
				f=true;				//caso a...b...c
			}
		}
		
		if(pc<pa){
			if(pa<pb && pb>pc){
				f=true;				//caso c...a...b
			}
		}
		return f;
	}

	@Override
	public void flip(City a, City b) {
		// TODO Auto-generated method stub
		int pa,pb, nexta, nextb;
		City s1[]= new City[n];
		pa=posizione[a.getCity()];				//metto in delle variabili tutte le posizioni che mi servono
		pb=posizione[b.getCity()];
		nexta=posizione[this.next(a).getCity()];
		nextb=posizione[this.next(b).getCity()];
		s1[pb]=soluzione[nexta];				//scambio la posizione di nexta e b
		s1[nexta]=b;					
		for(int i=(nexta+1); i<pb; i++){
			s1[i]=soluzione[pb-i];				//per tutti gli archi tra nexta e pb, "cambio verso"
		}
		for(int i=0; i<=pa; i++){
			s1[i]=soluzione[i];					//ricopio in s1 le celle della soluzione che non sono variate 
		}
		for(int i=nextb; i<n; i++){
			s1[i]=soluzione[i];
		}
	    
		this.soluzione=s1;
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
	}

	@Override
	public City startFrom() {
		// TODO Auto-generated method stub
		return null;
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
	public Object clone(){
		return null;
	}
	
}
