package LK;


import java.util.ArrayList;
import java.util.HashSet;

import tsp.model.*;


public class LK_Intesifier implements Intensifier {
	
//------------------------------------------------------------------------------------------
// Attributi
//------------------------------------------------------------------------------------------
	
	// miglior guadagno registrato
	private int best_gain = 0;
	
	// miglior tour registrato
	private Solution best_solution = null;
	
	// soluzione corrente
	private Solution current_solution = null;
	
	// array di città da collegare
	private City cities[];
	
	
	// set degli archi aggiunti
	private HashSet<Edge> added_Edges;
	
	//insieme degli archi x
	private ArrayList<Edge> x_Edges;
	
	//insieme degli archi y
	private ArrayList<Edge> y_Edges;
	
	//array dei guadagni progressivi
	private ArrayList<Integer> gains;
	
	
	//flag della direzione
	// true:  scorrimento dal primo elemento della soluzione all'ultimo      
	// false: scorrimento inverso
	private boolean direction = true;
	
//------------------------------------------------------------------------------------------
// Metodi di ricerca
//------------------------------------------------------------------------------------------

	// ottimizzazione locale
	public Solution improve(Solution start) {
		
		//la soluzione da migliorare è quella iniziale e la migliore fino ad ora
		best_solution = start; // TODO dovrebbe essere una
							   // copia
		
		//ricerca iterando sulle città da prendere come città iniziale del tour
		for(int i = 0; i < cities.length; i++){
			
			// TODO le inizializzazioni dovrebbero essere fatte ogni volta che si trova
			//      una nuova soluzione migliore globale oppure non si è torvata alcun
			//      guadagno partendo dalla città i-esima
			
			current_solution = best_solution; // TODO dovrebbe essere
			  								  //	  una copia

			//il guadagno iniziale è 0
			best_gain = 0;
			
			//TODO valutare se eseguire un clear() sugli ArrayList è più veloce di
			//ricostruirli
			
			//il set di archi aggiunti è vuoto
			added_Edges = new HashSet<Edge>();
			
			//gli insiemi degli archi x e y, e il vettore di guadagni sono vuoti
			x_Edges = new ArrayList<Edge>();
			y_Edges = new ArrayList<Edge>();
			gains = new ArrayList<Integer>();
			
			find_improvement(cities[i]);
		}
		
		return best_solution;
	}

//------------------------------------------------------------------------------------------	
	
	// ricerca del tour migliore partendo da t1. la funzione gestisce i diversi casi
	// iniziali possibili e la fase di backtrack
	private void find_improvement(City t1){
		
		//se non si è trovata una soluzione migliore globalmente, continua il backtracking
		boolean backtrack = true;
		
		direction = false;
		
		// ricerca nei due sensi
		for(int n=0; n<2; n++){
			direction = !direction;
			
			//seconda città t2
			City t2 = next(current_solution, t1);
			
			//arco x1
			Edge x1 = createEdge(t1,t2);
			x_Edges.add(x1);
			
			//archi candidati ad essere y1, ordinati per lunghezza crescente
			ArrayList<EdgeGain_Pair> y1s = getY1candidates(x1);
			
			// se non esistono archi candidati ad essere y1 cambia x1
			if(y1s == null){
				//backtracking: elimina x1 dal gruppo di archi x
				//TODO valutare di aggiungere x1 solo dopo questo controllo
				x_Edges.remove(0);
				continue;
			}
			
			//ottimizzazione per y1 di lunghezza crescente
			for(int j=0; j<y1s.size(); j++){
				
				//paio y1-g1
				EdgeGain_Pair pair1 = y1s.get(j);
				
				//arco y1
				Edge y1 = pair1.edge;
				
				//guadagno g1
				int g1 = pair1.gain;
				
				//registra y1 e G1
				y_Edges.add(y1);
				gains.add(g1);
				
				//città t3
				City t3 = y1.getDepart();
				
				//ricerca su i due archi x2 possibili
				for(int k = 0; k<2; k++){
					if(k == 0){
						//risultato accettabile (feasible) la scelta di x2 rende il tour
						//hamiltoniano
						
						//città t4
						City t4 = prev(current_solution, t3);
						
						//arco x2
						Edge x2 = createEdge(t4,t3);
						x_Edges.add(x2);
						
						//flip, scambio di x1 e x2 con y1 e y2* che chiude il tour
						flip(current_solution, t1, t4);
						
						//arco y2* che chiude il nuovo tour
						Edge y2_star = createEdge(t4,t1);
						
						//guadagno g2*
						int g2_star = x2.getLength() - y2_star.getLength();
						
						//guadagno del nuovo tour
						int G2_star = g1 + g2_star;
						
						//il nuovo tour ha un guadagno globale migliore?
						if(G2_star>best_gain){
							
							//il tour è più corto del migliore trovato
							// TODO dovrebbe essere una copia
							best_solution = current_solution;
							
							//aggiorna il guadagno migliore trovato
							best_gain = G2_star;
							
							//non c'è bisogno di fare backtrack
							backtrack = false;
						}
						
						//archi candidati ad essere y2, ordinati per lunghezza crescente
						ArrayList<EdgeGain_Pair> y2s = getY2candidates(x2);
						
						int y2s_size;
						
						// ci sono candidati ad essere y2?
						if(y2s == null){
							if(backtrack){
								// se non esistono archi candidati ad essere y2 e non si
								// è trovato alcun miglioramento cambia x2
								
								// backtracking
								current_solution = best_solution; // TODO dovrebbe essere
																  //      una copia
							
								//TODO valutare di aggiungere x2 solo dopo questo controllo
								x_Edges.remove(1);
								continue;
							}
							else{
								// nessun candidato, ma si è già ottenuto un tour migliore
								y2s_size = 0;
							}
						}
						else{
							y2s_size = y2s.size();
						}
						
						//ricerca per y2 crescente
						for(int h = 0; h < y2s_size; h++){
							
							//paio y2-g2
							EdgeGain_Pair pair2 = y2s.get(h);
							
							//arco y2
							Edge y2 = pair2.edge;
							
							//guadagno g2
							int g2 = pair2.gain;
							
							//registra y2 e il guadagno G2
							y_Edges.add(y2);
							gains.add(g2 + g1);
							
							//inizializzazione delle variabili del ciclo do-while
							
							//in realtà contiene yi-1, economia sulle variabili
							Edge yi = y2;
							
							//città t2i-2
							City t_2im2 = t4;
							
							//indice dell'iterazione
							int i = 2;
							
							do{							
								//città t2i-1
								City t_2im1 = yi.getDepart();
							
								//città t2i
								City t2i = next(current_solution,t_2im1);
							
								//arco xi
								Edge xi = createEdge(t_2im1,t2i);
							
								//arco yi*
								Edge yi_star = createEdge(t2i, t1);
								
								//flip: scambio xi e yi-1* con yi-1 e yi*
								flip(current_solution, t_2im2,t_2im1);
								
								//guadagno gi*
								int gi_star = xi.getLength() - yi_star.getLength();
								
								//Guadagno Gi*
								int Gi_star = gains.get(i-1) + gi_star;
								
								//il nuovo tour è il migliore fino ad ora?
								if(Gi_star>best_gain){
									
									//il tour è più corto del migliore trovato
									// TODO dovrebbe essere una copia
									best_solution = current_solution;
									
									//aggiorna il guadagno migliore trovato
									best_gain = Gi_star;
									
									//non c'è bisogno di fare backtrack
									backtrack = false;
								}
								
								// cerca il prossimo arco y
								yi = getNextY(xi);
								
								//è possibile valutare un altro scambio?
								if(yi!=null){
									
									//guadagno gi
									// TODO potrebbe essere stato calcolato già da
									//		getNextY()
									
									// TODO registrazione di yi e gi
									
									t_2im2 = t2i;
									i++;
								}
								
															
							}while(yi!=null);
							
							if(!backtrack)
								break;
							
						}
						//fine loop su y2
						
					}
					else {
						//risultato non accettabile:
						//la scelta di x2 porta a due cicli bisogna ritornare a un tour
						//hamiltoniano
						
						//città t4
						City t4 = next(current_solution, t3);
						
						//arco x2
						Edge x2 = createEdge(t4,t3);
						
						//archi candidati a essere y2
						ArrayList<EdgeGain_Pair> y2s = getY2candidates(x2);
						
						if(y2s==null){
							//non ci sono candidati a essere y2: backtrack
							continue;
						}
						
						x_Edges.add(x2);
						
						//ottimizzazione per y2 di lunghezza crescente
						for(int h=0; h<y2s.size(); h++){
							
							EdgeGain_Pair y2_pair = y2s.get(h);
							
							//arco y2
							Edge y2 = y2_pair.edge;
							
							//guadagno g2
							int g2 = y2_pair.gain;
							
							//città t5
							City t5 = y2.getDepart();
							
							// in base alla posizione di t5 si può decidere come
							// ricostruire il tour
							// TODO una volta trovata t6 valutare la chiusura con y3*
							if(between(current_solution, t2, t5, t3)){
								//prendendo un qualsiasi arco x con estremo t5 si
								//ritorna a un tour accettabile
								
								for(int l=0; l<2; l++){
									if(l==0){
										//t6 = next(t5)
																				
										//TODO:
										// 3 scambi: flip(t1,t2,t3,t4)
										//			 flip(t5,t6,t4,t2)
										//			 flip(t3,t1,t2,t6)
									}
									else {
										//t6 = prev(t5)
										
										//TODO:
										// 2 scambi: flip(t1,t2,t6,t5)
										//			 flip(t5,t2,t4,t3)
									}
									
									if(!backtrack)
										break;
									
								}
								// fine loop per i due casi di t6
								
								
							}
							else {
								//t5 tra le città t4 e t1
								
								//per ricostruire il tour sono necessari gli scambi di
								//4 archi. trovata la città t7 è possibile continuare la
								//ricerca sia prendendo t8 come successiva città di t7 sia
								//come precedente. Si prende in considerazione solo l'arco
								//x4 più lungo tra i due che portano al possibile t8
								
								//città t6
								City t6 = next(current_solution,t5);
								
								//arco x3
								Edge x3 = createEdge(t5, t6);
								
								//TODO verificare se chiudendo il tour con y3* si ha un 
								//	   tour migliore. se ciò accade bisogna creare il
								//	   tour e poi disfarlo per continuare la ricerca
								
								//arco y3
								Edge y3 = getY3candidates(x3);
								
								if(y3==null){
									//non ci sono archi candidati a essere y3
									continue;
								}
								
								//registra l'arco x3
								x_Edges.add(x3);
								
								//città t7, sicuramente tra t2 e t3
								City t7 = y3.getDepart();
								
								//gli scambi necessari per ricostruire il tour
								//dipendono dalla città t8 scelta
								
								//2 possibili archi x4:
								//		uno per t8 = next(t7)
								//		uno per t8 = prev(t7)
								
								// città t8 successiva a t7
								City t8_next = next(current_solution,t7);
								Edge x4_next = createEdge(t7, t8_next);
								
								// città t8 precedente a t7
								City t8_prev = prev(current_solution, t7);
								Edge x4_prev = createEdge(t8_prev, t7);
								
								//flag per indicare quale dei due archi è stato scelto
								boolean t8_is_next;
								
								//x4_next è più lungo di x4_prev?
								t8_is_next = x4_next.getLength() > x4_prev.getLength();
								
								if(t8_is_next){
									// t8 è successivo a t7
									
									//TODO necessari 4 scambi:
									//			flip(t1,t2,t3,t4)
									//			flip(t5,t6,t4,t2)
									//			flip(t8,t7,t2,t6)
									//			flip(t3,t1,t2,t8)
								}
								else {
									// t8 è precendente a t7
									
									//TODO necessari 3 scambi
									//			flip(t1,t2,t8,t7)
									//			flip(t7,t2,t4,t3)
									//			flip(t4,t7,t5,t6)
								}
								
							}
							
							if(!backtrack)
								break;
							
						}
						//fine loop su y2
						
						
					}
					
					if(!backtrack)
						break;
					
				}
				//fine loop su x2
				
				if(!backtrack)
					break;
			}
			//fine loop su y1
			
			if(!backtrack)
				break;
			
		}
		//fine loop su x1
		
	}

//------------------------------------------------------------------------------------------

	// ricerca dei candidati ad essere y1
	private ArrayList<EdgeGain_Pair> getY1candidates( Edge x1 ){
		
		// si deve avere g1>0 perchè un arco sia candidabile
		
		// TODO
		return null;
	}
	
//------------------------------------------------------------------------------------------
	
	// ricerca dei candidati ad essere y2
	private ArrayList<EdgeGain_Pair> getY2candidates( Edge x2 ){
		
		// si deve avere g1+g2>0 perchè un arco sia candidabile
		
		// TODO
		return null;
	}
	
//------------------------------------------------------------------------------------------
	
		// ricerca di un candidato ad essere y3
		private Edge getY3candidates( Edge x3 ){
			
			//TODO dovrebbe calcolare anche il guadagno
			
			//TODO la città t7 deve per forza trovarsi tra t2 e t3
			
			// TODO 
			return null;
		}
	
//------------------------------------------------------------------------------------------	
	
	//ricerca del prossimo arco y che soddisfa le condizioni per poter valutare uno scambio
	//e controllo del stop criterion
	private Edge getNextY(Edge xi){
		
		// TODO dovrebbe calcolare anche il guadagno del prossimo arco y
		
		//TODO la città precedente nel tour rispetto a quella da cui valutiamo y2 dovrebbe
		//	   essere scartata dal calcolo in quanto genera un loop
		
		return null;
	}
	
//------------------------------------------------------------------------------------------
// Routine di utility
//------------------------------------------------------------------------------------------
	
	// si necessita di ridefinire i metodi di Solution in accordo con la direzione 
	
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
	
	// la creazione di un arco è dipendente dalla direzione
	private Edge createEdge(City c1, City c2){
		if(direction)
			return new Edge(c1,c2);
		return new Edge(c2,c1);
	}
	
//------------------------------------------------------------------------------------------
// Classi interne per utility
//------------------------------------------------------------------------------------------
	
	// classe interna per memorizzare il guadagno di un arco y
	private class EdgeGain_Pair {
		
		Edge edge;
		int gain;
		
		EdgeGain_Pair(Edge e, int g){
			edge = e;
			gain = g;
		}
		
		Edge getEdge(){
			return edge;
		}
		
		int getGain(){
			return gain;
		}
	}

}
