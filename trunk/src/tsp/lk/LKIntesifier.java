package tsp.lk;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import tsp.model.*;


public class LKIntesifier implements Intensifier {
	
//------------------------------------------------------------------------------------------
// Attributi
//------------------------------------------------------------------------------------------
	
	// miglior guadagno registrato
	private int best_gain = 0;
	
	// miglior tour registrato
	private Solution best_solution = null;
	
	// soluzione corrente
	private Solution current_solution = null;
	
	// città iniziale corrente
	private City t1_current;
	
	// array di città da collegare
	private City cities[];
	
	// gestore delle città
	private CityManager city_manager;
	
	// numero di città
	int city_number;
	
	
	// set degli archi aggiunti
	private HashSet<Edge> added_Edges;
	
	//numero di scambi effettuati per ottenere l'ultima soluzione migliore
	private int k;
	
	//insieme degli archi x
	private Edge[] x_Edges;
	
	//insieme degli archi y
	private Edge[] y_Edges;
	
	//array dei guadagni progressivi
	private int[] gains;
	
	//flag della direzione
	// true:  scorrimento dal primo elemento della soluzione all'ultimo      
	// false: scorrimento inverso
	private boolean direction;
	
//------------------------------------------------------------------------------------------
// Parametri
//------------------------------------------------------------------------------------------
	
	//numero di città iniziali da valutare al massimo
	//dovrebbe essere circa 8
	private int max_t1 = 8;
	
	//numero di archi y1 da valutare al massimo
	// dovrebbe essere uguale a 5 o 6
	private int max_y1 = 5;
	
	//numero di archi y2 da valutare al massimo
	// dovrebbe essere uguale a 5 o 6
	private int max_y2 = 5;

	//numero di archi yi da valutare al massimo
	// dovrebbe essere uguale a 5 o 6
	private int max_yi = 5;
	
	//numero massimo di archi scambiabili a ogni iterazione
	//dovrebbe essere uguale a 50
	private int max_lambda = 50;
	
//------------------------------------------------------------------------------------------
// Costruttore e setter
//------------------------------------------------------------------------------------------
	
	public LKIntesifier(CityManager city_manager){
		this.city_manager = city_manager;
		this.city_number = city_manager.n;
		this.cities = city_manager.getCities();
	}
	
	public void setParam(int max_t1, int max_y1, int max_y2, int max_yi, int max_lambda){
		
		this.max_t1 = max_t1;
		this.max_y1 = max_y1;
		this.max_y2 = max_y2;
		this.max_yi = max_yi;
		this.max_lambda = max_lambda;
		
	}
	
//------------------------------------------------------------------------------------------
// Metodi di ricerca
//------------------------------------------------------------------------------------------

	// ottimizzazione locale
	public Solution improve(Solution start) {
		
		//la soluzione da migliorare è quella iniziale e la migliore fino ad ora
		best_solution = (Solution)start.clone();
			
		int n = city_number<max_t1 ? city_number : max_t1;
		
		//Per rendere differente ogni fase di miglioramento, si inizia da una città
		//causale come prima città iniziale
		int first_city_index = (int) (Math.random() * (double)city_number);
		
		//ricerca iterando sulle città da prendere come città iniziale del tour
		for(int i = 0; i < n; i++){
					
			// 	 	le inizializzazioni dovrebbero essere fatte ogni volta che si trova
			//      una nuova soluzione migliore globale oppure non si è torvata alcun
			//      guadagno partendo dalla città i-esima
			
			current_solution = (Solution)best_solution.clone();

			//il guadagno iniziale è 0
			best_gain = 0;
			
			//numero di archi scambiati azzerato
			k = 0;
			
			//TODO valutare se eseguire un clear() sugli ArrayList è più veloce di
			//ricostruirli
			
			//il set di archi aggiunti è vuoto
			added_Edges = new HashSet<Edge>();
			
			//gli insiemi degli archi x e y, e il vettore di guadagni sono vuoti
			x_Edges = new Edge[city_number];
			y_Edges = new Edge[city_number];
			
			gains = new int[city_number];
			
			//nuova città iniziale
			t1_current = cities[first_city_index];
			
			find_improvement(cities[first_city_index]);
			
			first_city_index = (first_city_index +1)%city_number;
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
			x_Edges[0] = x1;
			
			//archi candidati ad essere y1, ordinati per lunghezza crescente
			ArrayList<EdgeGain_Pair> y1s = getY1candidates(x1);
			
			// se non esistono archi candidati ad essere y1 cambia x1
			if(y1s == null){
				//backtracking: elimina x1 dal gruppo di archi x
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
				y_Edges[0] = y1;
				
				added_Edges.add(y1);
				
				gains[0] = g1;
				
				//città t3
				City t3 = y1.getDepart();
				
				//ricerca su i due archi x2 possibili
				for(int m = 0; m<2; m++){
					if(m == 0){
						//risultato accettabile (feasible) la scelta di x2 rende il tour
						//hamiltoniano
						
						//città t4
						City t4 = prev(current_solution, t3);
						
						//arco x2
						Edge x2 = createEdge(t4,t3);
						x_Edges[1] = x2;
						
						//flip, scambio di x1 e x2 con y1 e y2* che chiude il tour
						flip(current_solution, t4, t1);
						
						//arco y2* che chiude il nuovo tour
						Edge y2_star = createEdge(t4,t1);
						
						//guadagno g2*
						int g2_star = x2.getLength() - y2_star.getLength();
						
						//guadagno del nuovo tour
						int G2_star = g1 + g2_star;
						
						//il nuovo tour ha un guadagno globale migliore?
						if(G2_star>best_gain){
							
							//il tour è più corto del migliore trovato
							best_solution = (Solution)current_solution.clone();
							
							//aggiorna il guadagno migliore trovato
							best_gain = G2_star;
							
							//aggiorna il numero di archi scambati
							k = 2;
							
							//non c'è bisogno di fare backtrack
							backtrack = false;
						}
						
						//archi candidati ad essere y2, ordinati per guadagno con x3
						//descrescente
						ArrayList<EdgeGain_Pair> y2s = getY2candidates(x2);
						
						int y2s_size;
						
						// ci sono candidati ad essere y2?
						if(y2s == null){
							if(backtrack){
								// se non esistono archi candidati ad essere y2 e non si
								// è trovato alcun miglioramento cambia x2
								
								// backtracking
								current_solution = (Solution)best_solution.clone();
								
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
							
							if(pair2.part_gain <= best_gain){
								//l'arco y2 non migliora il guadagno totale trovato
								if(!backtrack)
									break;
								continue;
							}
							
							//arco y2
							Edge y2 = pair2.edge;
							
							//guadagno g2
							int g2 = pair2.gain;
							
							//guadagno parziale G2
							int G2 = pair2.part_gain;
							
							//registra y2 e il guadagno G2
							y_Edges[1] = y2;
							added_Edges.add(y2);
							
							gains[1] = G2;
							
							//inizializzazione delle variabili del ciclo do-while
							
							//in realtà contiene yi-1, economia sulle variabili
							Edge yi = y2;
							
							//guadagno gi
							int gi = g2;
							
							//Guadagno parziale Gi
							int Gi = G2;
							
							//città t2i-2
							City t_2im2 = t4;
							
							//indice dell'iterazione
							int i = 3;
							
							do{							
								//città t2i-1
								City t_2im1 = yi.getDepart();
							
								//città t2i
								City t2i = next(current_solution,t_2im1);
							
								//arco xi
								Edge xi = createEdge(t_2im1,t2i);
								
								//registra xi
								x_Edges[i-1] = xi;
							
								//arco yi*
								Edge yi_star = createEdge(t2i, t1);
								
								//flip: scambio xi e yi-1* con yi-1 e yi*
								flip(current_solution, t_2im1, t_2im2);
								
								//guadagno gi*
								int gi_star = xi.getLength() - yi_star.getLength();
								
								//Guadagno Gi*
								int Gi_star = gains[i-2] + gi_star;
								
								//il nuovo tour è il migliore fino ad ora?
								if(Gi_star>best_gain){
									
									//il tour è più corto del migliore trovato
									best_solution = (Solution)current_solution.clone();
									
									//aggiorna il guadagno migliore trovato
									best_gain = Gi_star;
									
									//aggiorna il numero di archi scambati
									k = i;
									
									//non c'è bisogno di fare backtrack
									backtrack = false;
								}
								
								yi = null;
								
								// cerca il prossimo arco y
								EdgeGain_Pair yi_pair = getNextY(xi, i);
								
								//è possibile valutare un altro scambio?
								if(yi_pair!=null && i< max_lambda && i<city_number){
									
									//nuovo arco yi
									yi = yi_pair.edge;
									
									//guadagno gi
									gi = yi_pair.gain;
									
									//guadagno parziale Gi
									Gi = yi_pair.part_gain;
									
									//registrazione di yi e gi
									y_Edges[i-1] = yi;
									added_Edges.add(yi);
									
									gains[i-1] = Gi;
									
									t_2im2 = t2i;
									i++;
								}
								
															
							}while(yi!=null && Gi>best_gain);
							
							if(!backtrack)
								break;
							
							//backtrack per riportare il sistema alla soluzione iniziale
							//dovrebbe bastare copiare la soluzione iniziale a quella
							//corrente e svuotare la lista di archi aggiunti e poi effettuare
							//lo scambio tra x1 e y1
							
							current_solution = (Solution)best_solution.clone();
							
							flip(current_solution, t4,t1);

							//TODO valutare l'uso di clear()
							added_Edges = new HashSet<Edge>();
							added_Edges.add(y1);
							
						}
						//fine loop su y2
						
						//se deve essere fatto il backtrack, significa che non è stata
						//trovata una soluzione migliore di quella iniziale, quindi bisogna
						//provare con la seconda scelta di x2. bisogna dunque ritornare alla
						//soluzione iniziale, dato che la corrente potrebbe essere diversa da
						//essa
						if(backtrack)
							current_solution = (Solution)best_solution.clone();
						
					}
					else {
						//risultato non accettabile.
						//la scelta di x2 porta a due cicli: bisogna ritornare a un tour
						//hamiltoniano
						
						//città t4
						City t4 = next(current_solution, t3);
						
						//arco x2
						Edge x2 = createEdge(t3,t4);
						
						//archi candidati a essere y2
						ArrayList<EdgeGain_Pair> y2s = getY2candidates_infeasible(x2,t2);
						
						if(y2s==null){
							//non ci sono candidati a essere y2: backtrack
							continue;
						}
						
						x_Edges[1] = x2;
						
						//ottimizzazione per y2 di lunghezza crescente
						for(int h=0; h<y2s.size(); h++){
							
							EdgeGain_Pair y2_pair = y2s.get(h);
							
							if(y2_pair.part_gain<=best_gain){
								//l'arco non porta alcuna miglioria
								if(!backtrack)
									break;
								continue;
							}
							
							//arco y2
							Edge y2 = y2_pair.edge;
							
							//guadagno g2
							int g2 = y2_pair.gain;
							
							//registra il guadagno G2
							int G2 = g2+g1;
							
							//registrazione di y2 e G2
							y_Edges[1] = y2;
							added_Edges.add(y2);
							
							gains[1] = G2;
							
							//città t5
							City t5 = y2.getDepart();
							
							// in base alla posizione di t5 si può decidere come
							// ricostruire il tour
							if(between(current_solution, t2, t5, t3)){
								//prendendo un qualsiasi arco x con estremo t5 si
								//ritorna a un tour accettabile
								
								for(int l=0; l<2; l++){
									//città t6, valutabile sia quale successore che
									//predecessore di t5
									City t6;
									
									if(l==0){
										//t6 = next(t5)										
										t6 = next(current_solution, t5);
						
										// 3 scambi: flip(t1,t2,t3,t4)
										//			 flip(t5,t6,t4,t2)
										//			 flip(t3,t1,t2,t6)
										
										flip(current_solution,t3,t1);
										flip(current_solution,t4,t5);
										flip(current_solution,t2,t3);
									}
									else {
										//t6 = prev(t5)
										t6 = prev(current_solution, t5);
										
										// 2 scambi: flip(t1,t2,t6,t5)
										//			 flip(t5,t2,t4,t3)
										
										flip(current_solution,t6,t1);
										flip(current_solution,t4,t5);
									}
										
									Edge x3 = createEdge(t5, t6);
									
									x_Edges[2] = x3;
										
									Edge y3_star = createEdge(t6, t1);
										
									//guadagno g3*
									int g3_star = x3.getLength() - y3_star.getLength();
										
									//guadagno progessivo G3*
									int G3_star = g3_star + g1 + g2;
										
									//il nuovo tour ha un guadagno globale migliore?
									if(G3_star>best_gain){
											
										//il tour è più corto del migliore trovato
										best_solution = (Solution)current_solution.clone();
											
										//aggiorna il guadagno migliore trovato
										best_gain = G3_star;
										
										//aggiorna il numero di archi scambati
										k = 3;
											
										//non c'è bisogno di fare backtrack
										backtrack = false;
									}
										
									//loop per la ricerca di altri archi da scambiare
										
									int i = 3;
										
									Edge xi = x3;
										
									Edge yi_star = y3_star;
									
									//ricerca del prossimo arco yi
									EdgeGain_Pair yi_pair = getNextY(xi, i);
										
									Edge yi = null;
									
									int gi;
									
									int Gi = 0;
									
									if(yi_pair!=null){
										yi = yi_pair.edge;
										gi = yi_pair.gain;
										Gi = yi_pair.part_gain;
										
										y_Edges[i-1] = yi;
										added_Edges.add(yi);
										
										gains[i-1] = Gi;
									}
										
									//esistono aun arco per sostituire xi?
									//porta un guadagno migliore?
									while(yi!=null && Gi>best_gain && i < max_lambda
																		&& i < city_number){
											
										i++;
											
										//città t2i-2
										City t_2im2 = yi.getArrive();
											
										//città t2i-1
										City t_2im1 = yi.getDepart();
											
										//città t2i
										City t2i = next(current_solution, t_2im1);
											
										//arco xi
										xi = createEdge(t_2im1, t2i);
										
										//registra xi
										x_Edges[i-1] = xi;
										
										//flip per sostituire xi e yi-1* con yi e yi*
										flip(current_solution, t_2im1, t_2im2);
											
										//arco yi*
										yi_star = createEdge(t2i, t1);
											
										//guadagno gi*
										int gi_star = xi.getLength() - yi_star.getLength();
											
										//Guadagno totale Gi*
										int Gi_star = gains[i-2] + gi_star;
											
										//il nuovo tour ha un guadagno globale migliore?
										if(Gi_star>best_gain){
												
											//il tour è più corto del migliore trovato
											best_solution = (Solution)current_solution.clone();
												
											//aggiorna il guadagno migliore trovato
											best_gain = Gi_star;
											
											//aggiorna il numero di archi scambati
											k = i;
												
											//non c'è bisogno di fare backtrack
											backtrack = false;
										}
											
										yi = null;
										
										//ricerca del nuovo arco yi
										yi_pair = getNextY(xi, i);
										
										if(yi_pair!=null){
											yi = yi_pair.edge;
											gi = yi_pair.gain;
											Gi = yi_pair.part_gain;
											
											y_Edges[i-1] = yi;
											added_Edges.add(yi);
											
											gains[i-1] = Gi;
										}
									}
									//fine loop per la ricerca di possibili scambi
										
									if(!backtrack)
										break;
									
									current_solution = (Solution)best_solution.clone();
									
									//flip(current_solution,t4,t1);
									//flip(current_solution,t5,t4);
									
									//TODO valutare l'uso di clear()
									added_Edges = new HashSet<Edge>();
									added_Edges.add(y1);
									added_Edges.add(y2);
									
									
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
								
								//correntemente la città t6 è precedente a t5 in quanto non
								//sono ancora stati effettuati scambi
								
								//città t6
								City t6 = prev(current_solution,t5);
								
								//arco x3
								Edge x3 = createEdge(t5, t6);
								
								//registra x3
								x_Edges[2] = x3;
								
								//arco y3
								Edge y3 = null;
								
								//guadagno g3
								int g3;
								
								//guadagno totale G3
								int G3 = 0;
								
								EdgeCity_pair y3_pair = getY3candidates(x3, t2, t3);
								
								if(y3_pair!=null){
									y3 = y3_pair.edge_pair.edge;
									g3 = y3_pair.edge_pair.gain;
									G3 = y3_pair.edge_pair.part_gain;
									
									//registra y3 e G3
									y_Edges[2] = y3;
									added_Edges.add(y3);
									
									gains[2] = G3;
								}
								
								if(y3==null || G3 <= best_gain){
									//non ci sono archi candidati a essere y3
									if(!backtrack)
										break;
									
									//continuare significa provare con un altro arco y2,
									//quindi bisogna eliminare y2
									added_Edges.remove(y2);
									
									continue;
								}
								
								//città t7, sicuramente tra t2 e t3
								City t7 = y3.getDepart();
								
								//gli scambi necessari per ricostruire il tour
								//dipendono dalla città t8 scelta. 
								
								//2 possibili archi x4:
								//		uno per t8 = next(t7)
								//		uno per t8 = prev(t7)
								
								//viene scelto l'arco x4 più lungo
								
								//flag per indicare quale dei due archi è stato scelto
								boolean t8_is_next = y3_pair.city_is_next;
								
								//città t8 scelta
								City t8 = y3_pair.edge_city;
								
								//arco x4 scelto. bisogna inserire t8 come arrivo per far
								//funzionare il metodo getNextY. lo scambio è indifferente
								//dato che l'arco è equivalente a un arco (t8,t7)
								Edge x4 = createEdge(t7, t8);
								
								if(t8_is_next){
									// t8 è successivo a t7
									
									//necessari 4 scambi:
									//			flip(t1,t2,t3,t4)
									//			flip(t5,t6,t4,t2)
									//			flip(t8,t7,t2,t6)
									//			flip(t3,t1,t2,t8)
									
									flip(current_solution, t3, t1);
									flip(current_solution, t4, t5);
									flip(current_solution, t2, t8);
									flip(current_solution, t2, t3);
									
								}
								else {
									// t8 è precendente a t7
									
									//necessari 3 scambi
									//			flip(t1,t2,t8,t7)
									//			flip(t7,t2,t4,t3)
									//			flip(t4,t7,t5,t6)
									
									flip(current_solution, t8, t1);
									flip(current_solution, t4, t7);
									flip(current_solution, t5, t4);
									
								}
								
								//registra x3
								x_Edges[3] = x4;
								
								//arco y4*
								Edge y4_star = createEdge(t8, t1);
								
								//guadagno g4*
								int g4_star = x4.getLength() - y4_star.getLength();
								
								//guadagno totale G4*
								int G4_star = g4_star + G3;
								
								//il nuovo tour ha un guadagno globale migliore?
								if(G4_star>best_gain){
										
									//il tour è più corto del migliore trovato
									best_solution = (Solution)current_solution.clone();
										
									//aggiorna il guadagno migliore trovato
									best_gain = G4_star;
									
									//aggiorna il numero di archi scambati
									k = 4;
										
									//non c'è bisogno di fare backtrack
									backtrack = false;
								}
								
								//loop per la ricerca di altri archi da scambiare
								
								int i = 4;
									
								Edge xi = x4;
									
								Edge yi_star = y4_star;
								
								//ricerca del prossimo arco yi
								EdgeGain_Pair yi_pair = getNextY(xi, i);
									
								Edge yi = null;
								
								int gi;
								
								int Gi = 0;
								
								if(yi_pair!=null){
									yi = yi_pair.edge;
									gi = yi_pair.gain;
									Gi = yi_pair.part_gain;
									
									//registra yi e Gi
									y_Edges[i-1] = yi;
									added_Edges.add(yi);
									
									gains[i-1] = Gi;
								}
									
								//esistono aun arco per sostituire xi?
								//migliora il guadagno?
								while(yi!=null && Gi>best_gain && i < max_lambda
																	&& i < city_number){
									i++;
										
									//città t2i-2
									City t_2im2 = yi.getArrive();
										
									//città t2i-1
									City t_2im1 = yi.getDepart();
										
									//città t2i
									City t2i = next(current_solution, t_2im1);
										
									//arco xi
									xi = createEdge(t_2im1, t2i);
									
									//registra xi
									x_Edges[i-1] = xi;
									
									//flip per sostituire xi e yi-1* con yi e yi*
									flip(current_solution, t_2im1, t_2im2);
									
									//arco yi*
									yi_star = createEdge(t2i, t1);
										
									//guadagno gi*
									int gi_star = xi.getLength() - 
															yi_star.getLength();
										
									//Guadagno totale Gi*
									int Gi_star = gains[i-2] + gi_star;
										
									//il nuovo tour ha un guadagno globale migliore?
									if(Gi_star>best_gain){
											
										//il tour è più corto del migliore trovato
										best_solution = (Solution)current_solution.clone();
											
										//aggiorna il guadagno migliore trovato
										best_gain = Gi_star;
										
										//aggiorna il numero di archi scambati
										k = i;
											
										//non c'è bisogno di fare backtrack
										backtrack = false;
									}
										
									yi = null;
									
									//ricerca del nuovo arco yi
									yi_pair = getNextY(xi, i);
									
									if(yi_pair!=null){
										yi = yi_pair.edge;
										gi = yi_pair.gain;
										Gi = yi_pair.part_gain;
										
										//registra yi e Gi
										y_Edges[i-1] = yi;
										added_Edges.add(yi);
										
										gains[i-1] = Gi;
									}
								}
								//fine loop per la ricerca di possibili scambi
								
							}
							
							if(!backtrack)
								break;
							
							current_solution = (Solution)best_solution.clone();
							
							//flip(current_solution, t4,t1);
							
							//TODO valutare l'uso di clear()
							added_Edges = new HashSet<Edge>();
							added_Edges.add(y1);
							
						}
						//fine loop su y2
						
						
					}
					
					if(!backtrack)
						break;
					
					current_solution = (Solution)best_solution.clone();
					
				}
				//fine loop su x2
				
				if(!backtrack)
					break;
				
				current_solution = (Solution)best_solution.clone();
				
				added_Edges.clear();
				
			}
			//fine loop su y1
			
			if(!backtrack)
				break;
			
			current_solution = (Solution)best_solution.clone();
			
			
			
		}
		//fine loop su x1
		
	}

//------------------------------------------------------------------------------------------

	// ricerca dei candidati ad essere y1
	private ArrayList<EdgeGain_Pair> getY1candidates( Edge x1 ){
		
		//città t2
		City t2 = x1.getArrive();
		
		//città successiva a t2
		City t2_next = next(current_solution,t2);
		
		//Lista di città più vicine a t2
		City[] list = city_manager.getNearest(t2);
		
		//indice degli archi candidati
		int c = 0;
		
		//lista degli archi candidati
		ArrayList<EdgeGain_Pair> candidates = new ArrayList<EdgeGain_Pair>();
		
		//numero massimo di archi y1 da valutare
		int iterations = max_y1;
		
		if(iterations>list.length)
			iterations = list.length;
		
		for(int i = 0; i < iterations; i++){
			
			//città t3
			City t3 = list[i];
			
			if(t3 == t1_current || t3 == t2_next){
				// l'arco (t3,t2) non può essere scelto
				continue;
			}
			
			//dato che non ci sono ancora stati scambi, l'arco x2 creato successivamente
			//sarà rompibile
			
			//siccome potrebbero essere valutati due possibili x2, y1 è scelto solo in base
			//al guadagno minimo che porta la prossima sostituzione
			City t4_next = next(current_solution,t3);
			City t4_prev = prev(current_solution,t3);
			
			Edge x2_next = createEdge(t3, t4_next);
			
			Edge x2_prev = createEdge(t4_prev, t3);
			
			//TODO valutare i risultati usando come criterio il guadagno massimo ottenibile
			Edge x2 = x2_next.getLength() < x2_prev.getLength() ? x2_next : x2_prev;
			
			//potenziale arco y1
			Edge y1 = createEdge(t3, t2);
			
			//guadagno g1
			int g1 = x1.getLength() - y1.getLength();
			
			//l'arco porta un guadagno?
			if(g1<=0){
				//il guadagno iniziale è negativo
				//a questo punto si può interrompere il loop, dato che tutte le altre città
				//sono più lontane
				break;
			}
			
			
			//misura di ottimalità dell'arco in base alla differenza con la lunghezza del
			//prossimo arco da scambiare
			int o1 = x2.getLength() - y1.getLength();
			
			//si sfrutta l'ordinamento sui guadagni, per cui in seguito si dovrà
			//aggiustare le informazioni dell'oggetto EdgePair
			candidates.add(new EdgeGain_Pair(y1, o1, g1));
			c++;
			
		}
		
		//se non sono stati trovati candidati ritorna null
		if(c==0)
			return null;
		
		//ordinamento descrescente in base all'ottimalità dell'arco
		Collections.sort(candidates, new EdgePairComparator());
		
		//riparazione degli oggetti EdgeGain_Pair nella lista
		for(EdgeGain_Pair p : candidates){
			p.gain = p.part_gain;
		}
		
		return candidates;
	}
	
//------------------------------------------------------------------------------------------
	
	// ricerca dei candidati ad essere y2
	private ArrayList<EdgeGain_Pair> getY2candidates( Edge x2 ){
		
		//città t4
		City t4 = x2.getDepart();
		
		//città precedente a t4
		City t4_prev = prev(current_solution,t4);
		
		//Lista di città più vicine a t4
		City[] list = city_manager.getNearest(t4);
		
		//Guadagno parziale G2
		int G1 = gains[0];
				
		//indice degli archi candidati
		int c = 0;
				
		//lista degli archi candidati
		ArrayList<EdgeGain_Pair> candidates = new ArrayList<EdgeGain_Pair>();
				
		//numero massimo di archi y2 da valutare
		int iterations = max_y2;
				
		if(iterations>list.length)
			iterations = list.length;
		
		for(int i = 0; i < iterations; i++){
			
			//città t5
			City t5 = list[i];
			
			if(t5==t1_current || t5 == t4_prev){
				//l'arco (t5,t4) non può essere candidato
				continue;
			}
			
			//possibile arco y2
			Edge y2 = createEdge(t5, t4);
			
			//città t6
			City t6 = next(current_solution,t5);
			
			//arco x3 da rompere successivamente
			Edge x3 = createEdge(t5, t6);
			
			if(added_Edges.contains(x3)){
				//y2 non può essere scelto in quanto provocherebbe la rottura di un arco
				//aggiunto precedentemente
				continue;
			}
			
			//guadagno g2
			int g2 = x2.getLength() - y2.getLength();
			
			//guadagno parziale G2
			int G2 = G1 + g2;
			
			if(G2<=0){
				//l'arco rende il guadagno parziale minore o uguale a 0, quindi non è tra 
				//i candidati. Inoltre è inutile continuare la ricerca dato che G2 è 
				//decrescente scorrendo la lista
				break;
			}
			
			//l'ottimalità degli archi è valutata in base al guadagno rispetto all'arco x3
			//ottimalità o2
			int o2 = x3.getLength() - y2.getLength();
			
			//sfrutta temporaneamente l'ordinamento rispetto all'attributo gain per
			//ordinare rispetto a o2, bisogna poi ricalcolare G2 e salvare i dati 
			//correttamente in ogni EdgeGain_Pair nella lista
			candidates.add(new EdgeGain_Pair(y2, o2, g2));
			c++;
			
		}
		
		//sono stati trovati candidati?
		if(c==0)
			return null;
		
		//ordinamento rispetto all'ottimalità
		Collections.sort(candidates, new EdgePairComparator());
		
		//ciclo per ricalcolare G2 e risistemare i dati per io pair
		for(EdgeGain_Pair p : candidates){
			
			int G2 = p.part_gain + G1;
			p.gain = p.part_gain;
			p.part_gain = G2;
			
		}
		
		return candidates;
	}
	
//------------------------------------------------------------------------------------------
	
	// ricerca dei candidati ad essere y2 nel caso in cui la scelta di x2 rende il tour
	//non hamiltoniano
	//Quando il metodo è chiamato non è stato ancora eseguito alcuno scambio
	private ArrayList<EdgeGain_Pair> getY2candidates_infeasible( Edge x2, City t2 ){
			
		//città t4
		City t4 = x2.getArrive();
		
		//città t3
		City t3 = x2.getDepart();
		
		//città t1
		City t1 = t1_current;
		
		//città successiva a t4
		City t4_next = next(current_solution, t4);
		
		//lista di città vicine a t4
		City[] list = city_manager.getNearest(t4);
		
		//Guadagno parziale G1
		int G1 = gains[0];
				
		//indice degli archi candidati
		int c = 0;
				
		//lista degli archi candidati
		ArrayList<EdgeGain_Pair> candidates = new ArrayList<EdgeGain_Pair>();
				
		//numero massimo di archi y2 da valutare
		int iterations = max_y2;
				
		if(iterations>list.length)
			iterations = list.length;
		
		for(int i = 0; i < iterations; i++){
			
			//città t5
			City t5 = list[i];
			
			//potenziale arco y2
			Edge y2 = createEdge(t5, t4);
			
			//guadagno g2
			int g2 = x2.getLength() - y2.getLength();
			
			//guadagno parziale G2
			int G2 = G1 + g2;
			
			//l'arco porta un guadagno?
			if(G2<=0){
				//l'arco non porta alcun guadagno, e così anche quelli successivi
				break;
			}
			
			//arco x3
			Edge x3;
			
			//il criterio per cui il prossimo x3 può essere rotto è valutato controllando 
			//che t5 non sia una delle prime 4 città o la successiva di t4
			if(between(current_solution, t2, t5, t3)){
				//t5 è tra t2 e t3
				
				//si possono avere due archi x3, quindi l'ottimalità è valutata rispetto a
				//quello più corto TODO valutare i risultati usando il più lungo
				
				City t6_next = next(current_solution, t5);
				City t6_prev = prev(current_solution, t5);
				
				Edge x3_next = createEdge(t5, t6_next);
				Edge x3_prev = createEdge(t6_prev, t5);
				
				x3 = x3_next.getLength() < x3_prev.getLength() ? x3_next : x3_prev;
				
			}
			else if(between(current_solution, t4, t5, t1)){
				//t5 è tra t4 e t1
				if(t5 == t4_next){
					//l'arco (t5,t4) non può essere candidato
					continue;
				}
				
				//la città t6 è quella attualmente precedente a t5
				City t6 = prev(current_solution,t5);
				
				x3 = createEdge(t5, t6);
			}
			else{
				//t5 è una delle prime 4 città
				continue;
			}
			
			//ottimalità o2
			int o2 = x3.getLength() - y2.getLength();
			
			//sfrutta temporaneamente l'ordinamento rispetto all'attributo gain per
			//ordinare rispetto a o2, bisogna poi ricalcolare G2 e salvare i dati 
			//correttamente in ogni EdgeGain_Pair nella lista
			candidates.add(new EdgeGain_Pair(y2, o2, g2));
			c++;
			
		}
			
			
		//sono stati trovati candidati?
		if(c==0)
			return null;
		
		//ordinamento rispetto all'ottimalità
		Collections.sort(candidates, new EdgePairComparator());
		
		//ciclo per ricalcolare G2 e risistemare i dati per io pair
		for(EdgeGain_Pair p : candidates){
			
			int G2 = p.part_gain + G1;
			p.gain = p.part_gain;
			p.part_gain = G2;
			
		}
		
		return candidates;
	}

	
//------------------------------------------------------------------------------------------
	
	// ricerca di un candidato ad essere y3
	//quando il metodo viene richiamato non è stato effettuato alcuno scambio
	private EdgeCity_pair getY3candidates( Edge x3, City t2, City t3 ){
		
		//città t6
		City t6 = x3.getArrive();
		
		//lista delle città più vicine a t6
		City[] list = city_manager.getNearest(t6);
		
		//contatore degli archi candidati
		int c = 0;
			
		//Guadagno parziale G2
		int G2 = gains[1];
		
		//arco scelto
		EdgeGain_Pair y3_pair = new EdgeGain_Pair(null, 0, 0);
		
		//città t8 scelta
		City city_edge = null;
		
		//scelta dell'arco x4
		boolean city_is_next = true;
		
		//miglior indice di ottimalità trovato
		int opt = Integer.MIN_VALUE;
				
		//numero massimo di archi y3 da valutare
		int iterations = max_yi;
				
		if(iterations>list.length)
			iterations = list.length;
		
		for(int i = 0; i<iterations; i++){
			
			//città t7
			City t7 = list[i];
			
			//potenziale arco y3
			Edge y3 = createEdge(t7, t6);
			
			//guadagno g3
			int g3 = x3.getLength() - y3.getLength();
			
			//guadagno parziale G3
			int G3 = g3 + G2;
			
			//lo scambio tra x3 e y3 porta ancora a un guadagno?
			if(G3<=0){
				//non c'è guadagno nello scambio, è inutile continuare a cercare un arco
				break;
			}
			
			//la condizione per cui t7 sia tra t2 e t3 permette di assicurare che il 
			//prossimo arco x4 sia rompibile
			
			//t7 è tra t2 e t3?
			if(!between(current_solution, t2, t7, t3)){
				//t7 non si trova tra t2 e t3, quindi l'arco (t6,t7) non può essere y3
				continue;
			}
			
			//due possibili archi x4, si sceglie quello più lungo, sul quale si calcola
			//l'ottimalità dell'arco y3
			
			City t8_next = next(current_solution, t7);
			City t8_prev = prev(current_solution, t7);
			
			Edge x4_next = createEdge(t7, t8_next);
			
			Edge x4_prev = createEdge(t8_prev, t7);
			
			boolean t8_is_next = x4_next.getLength() > x4_prev.getLength();
			
			Edge x4 = t8_is_next ? x4_next : x4_prev;
			
			City t8 = t8_is_next ? t8_next : t8_prev;
			
			//ottimalità o3
			int o3 = x4.getLength() - y3.getLength();
			
			//l'arco è quello più ottimale tra quelli già valutati?
			if(o3 > opt){
				//l'arco è temporaneamente il miglior candidato
				y3_pair.edge = y3;
				y3_pair.gain = g3;
				y3_pair.part_gain = G3;
				
				city_edge = t8;
				
				city_is_next = t8_is_next;
				
				c++;
				opt = o3;
			}
			
		}
		
		//sono stati trovati candidati?
		if(c==0){
			//nessun candidato per y3
			return null;
		}
			
		EdgeCity_pair y3_city_pair = new EdgeCity_pair(y3_pair, city_edge, city_is_next);
		
		return y3_city_pair;
	}
	
//------------------------------------------------------------------------------------------	
	
	//ricerca del prossimo arco y che soddisfa le condizioni per poter valutare uno scambio
	//e controllo del stop criterion
	private EdgeGain_Pair getNextY(Edge xi, int i){
		
		//città t2i
		City t2i = xi.getArrive();
		
		//città t2i-1
		City t2i_prev = prev(current_solution,t2i);
		
		//lista delle città più vicine a t2i
		City[] list = city_manager.getNearest(t2i);
		
		//contatore degli archi candidati
		int c = 0;
			
		//Guadagno parziale Gi-1
		int G_im1 = gains[i-2];
		
		//arco scelto
		EdgeGain_Pair yi_pair = new EdgeGain_Pair(null, 0, 0);
		
		//miglior indice di ottimalità trovato
		int opt = Integer.MIN_VALUE;
				
		//numero massimo di archi yi da valutare
		int iterations = max_yi;
				
		if(iterations>list.length)
			iterations = list.length;
		
		for(int k = 0; k < iterations; k++){
			
			//città t2i+1
			City t_2ip1 = list[k];
			
			if(t_2ip1 == t1_current || t_2ip1 == t2i_prev){
				//l'arco (t2i,t2i+1) non può essere candidato
				continue;
			}
			
			//potenziale arco yi
			Edge yi = createEdge(t_2ip1, t2i);
			
			//guadagno gi
			int gi = xi.getLength() - yi.getLength();
			
			//guadagno parziale Gi
			int Gi = G_im1 + gi;
			
			//lo scambio porta a un guadagno?
			if(Gi <= 0){
				//lo scambio non porta alcun guadagno, si può interrompere la ricerca
				break;
			}
			
			//città t2i+2
			City t_2ip2 = next(current_solution, t_2ip1);
			
			//arco xi+1
			Edge x_ip1 = createEdge(t_2ip1, t_2ip2);
			
			//è possibile rompere il prossimo arco xi+1?
			if(added_Edges.contains(x_ip1)){
				//yi non può essere candidato dato che porterebbe a rompere un arco y
				continue;
			}
			//ottimalità oi
			int oi = x_ip1.getLength() - yi.getLength();
			
			//l'arco è quello più ottimale tra quelli già valutati?
			if(oi > opt){
				//l'arco è temporaneamente il miglior candidato
				yi_pair.edge = yi;
				yi_pair.gain = gi;
				yi_pair.part_gain = Gi;
				
				c++;
				opt = oi;
			}
			
		}
		
		//sono stati trovati candidati?
		if(c==0){
			//nessun candidato per yi
			return null;
		}
		
		return yi_pair;
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
	
//------------------------------------------------------------------------------------------
	
	// città precedente nella soluzione
	private City prev(Solution s, City c){
		if(direction)
			return s.previous(c);
		return s.next(c);
	}
	
//------------------------------------------------------------------------------------------
	
	// città in mezzo ad altre due
	private boolean between(Solution s, City a, City b, City c){
		
		if(direction)
			return s.between(a, b, c);
		return s.between(c, b, a);
	}
	
//------------------------------------------------------------------------------------------
	
	// scambio di due archi
	private void flip(Solution s, City a, City b){
		
		City c1, c2;
		
		if(direction){
			c1 = a;
			c2 = b;
		} else {
			c2 = s.previous(a);
			c1 = s.previous(b);
		}
		
		s.flip(c1, c2);
	}
	
//------------------------------------------------------------------------------------------
	
	//estrazione di un arco dal gestore delle città
	private Edge createEdge(City c1, City c2){
		return city_manager.getEdge(c1, c2);
	}
	
//------------------------------------------------------------------------------------------
// Classi interne per utility
//------------------------------------------------------------------------------------------
	
	// classe interna per memorizzare il guadagno di un arco y
	private class EdgeGain_Pair {
		
		Edge edge;
		int gain;
		
		int part_gain;
		
		EdgeGain_Pair(Edge e, int g, int p){
			edge = e;
			gain = g;
			part_gain = p;
		}
		
	}
	
//------------------------------------------------------------------------------------------
	
	//classe per l'ordinamento di un EdgeGain_Pair in base al guadagno gi di un arco y
	private class EdgePairComparator implements Comparator<EdgeGain_Pair> {

		
		public int compare(EdgeGain_Pair e0, EdgeGain_Pair e1) {
			
			//ordinamento decrescente
			return Integer.compare(e1.gain, e0.gain);
		}
		
	}
	
//------------------------------------------------------------------------------------------
	
	//classe utilizzata per memorizzare quale città t8 viene scelta nel caso di scambio
	//di 4 archi per rendere il tour hamiltoniano
	private class EdgeCity_pair {
		
		EdgeGain_Pair edge_pair;
		City edge_city;
		boolean city_is_next;
		
		EdgeCity_pair(EdgeGain_Pair e, City c, boolean b){
			edge_pair = e;
			edge_city = c;
			city_is_next =b;
		}
	}
	

}
