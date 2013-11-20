package LK;

import tsp.model.*;


public class LK_Intesifier implements Intensifier {
	
//---------------------------------------------------------
// Attributi
//---------------------------------------------------------
	
	// miglior guadagno registrato
	private int best_gain = 0;
	
	// miglior tour registrato
	private Solution best_solution = null;
	
	// soluzione corrente
	private Solution current_solution = null;
	
	// array di città da collegare
	private City cities[];
	
	//flag della direzione
	// true: scorrimento dal primo elemento della soluzione
	//       all'ultimo
	// false: scorrimento inverso
	private boolean direction = true;
	
//---------------------------------------------------------
// Metodi di ricerca
//---------------------------------------------------------

	// ottimizzazione locale
	public Solution improve(Solution start) {
		
		//la soluzione da migliorare è quella iniziale
		//e la migliore fino ad ora
		best_solution = start; // TODO dovrebbe essere una
							   // copia
		
		current_solution = start; // TODO dovrebbe essere
								  // una copia
		
		//il guadagno iniziale è 0
		best_gain = 0;
		
		//ricerca del vicinato iterando sulle città
		//da prendere come città iniziale del tour
		for(int i = 0; i < cities.length; i++){
			find_improvement(cities[i]);
		}
		
		return best_solution;
	}
	
	// ricerca del tour migliore partendo da t1
	private void find_improvement(City t1){
		
		direction = false;
		
		// ricerca nei due sensi
		for(int i=0; i<2; i++){
			direction = !direction;
			
			//seconda città t2
			City t2 = next(current_solution, t1);
			
			//arco x1
			Edge x1 = new Edge(t1,t2);
			
		}
			
		
	}
	
	
//---------------------------------------------------------
// Routine di utility
//---------------------------------------------------------
	
	// si necessita di ridefinire i metodi di Solution
	// in accordo con la direzione
	
	// città successiva nella soluzione
	private City next(Solution s,City c){
		if(direction)
			return s.next(c);
		return s.previous(c);
	}
	
	// città precedente nella soluzione
	private City prev(Solution s, City c){
		if(direction)
			return s.previous(c);
		return s.next(c);
	}
	
	// città in mezzo ad altre due
	private boolean between(Solution s, City a, City b, City c){
		
		boolean res = s.between(a, b, c);
		
		if(direction)
			return res;
		return !res;
	}
	
	// scambio di due archi
	private void flip(Solution s, City a, City b){
		
		City c1, c2;
		
		if(direction){
			c1 = a;
			c2 = b;
		} else {
			c1 = s.previous(a);
			c2 = s.previous(b);
		}
		
		s.flip(c1, c2);
	}

}
